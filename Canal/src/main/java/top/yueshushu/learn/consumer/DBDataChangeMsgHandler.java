package top.yueshushu.learn.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry.*;
import com.alibaba.otter.canal.protocol.Message;
import com.alibaba.otter.canal.protocol.exception.CanalClientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import top.yueshushu.learn.consumer.dto.DBDataChange;
import top.yueshushu.learn.consumer.util.JdbcTypeUtil;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author yjl
 * @date 2022-04-29
 */
@Slf4j
@Component
public class DBDataChangeMsgHandler {

    @Value("${canal.server.ip:127.0.0.1}")
    private String canalServerIp;
    @Value("${canal.port:11111}")
    private int canalPort;
    @Value("${canal.destination:example}")
    private String destination;
    @Value("${canal.username:}")
    private String username;
    @Value("${canal.password:}")
    private String password;
    @Value("${canal.schemaName:springboot}")
    private String schemaName;
    @Value("${canal.consumer.start:false}")
    private boolean start;

    @Resource
    private ApplicationContext ctx;

    /**
     * CanalConnector 连接对象
     */
    private CanalConnector connector;

    private Map<String, DBDataChangeConsumer> consumerByKey;
    /**
     * 是否正在启动中
     */
    private volatile boolean run;

    /**
     * 系统启动时，进行初始化监听
     */
    @PostConstruct
    private void init() {
        log.info(">>>>>>开始进行初始化");
        if (!start) {
            log.info("System will not start canal consumer...");
            return;
        }

        consumerByKey = findConsumer();
        if (CollectionUtils.isEmpty(consumerByKey)) {
            log.info("no consumer. System won't listen on canal");
            return;
        }
        createConnector();
    }

    /**
     * 系统关闭时，进行断开连接。
     */
    @PreDestroy
    public void disconnect() {
        run = false;
        if (null != connector) {
            connector.disconnect();
        }
    }

    /**
     * 构建监听的信息集合，即 subscribe()方法中的内容key
     * @return
     */
    private Map<String, DBDataChangeConsumer> findConsumer() {
        // 获取实现该 接口的所有实现类信息。
        Map<String, DBDataChangeConsumer> consumers = ctx.getBeansOfType(DBDataChangeConsumer.class);
        Map<String, DBDataChangeConsumer> tempConsumerByKey = new HashMap<>(consumers.size(), 1.0f);
        consumers.values().forEach(consumer -> {
            //没有表名，不进行处理了。 筛选
            if (StringUtils.isEmpty(consumer.getTableName())) {
                log.warn("consumer.tableName is empty, ignore. consumer: " + consumer);
                return;
            }
            String schema = consumer.getSchemaName();
            DBDataChangeConsumer previousConsumer = tempConsumerByKey.put(StringUtils.isEmpty(schema) ? this.schemaName : schema + "." + consumer.getTableName(), consumer);
            if (null != previousConsumer) {
                log.warn("consumer for table is duplicated. only keep one. tableName:{}, ignore consumer:{}", previousConsumer.getTableName(), previousConsumer);
            }
        });
        return tempConsumerByKey;
    }

    /**
     * 创建连接处理
     */
    private void createConnector() {
        // 创建连接
        connector = CanalConnectors.newSingleConnector(
                new InetSocketAddress(canalServerIp, canalPort), destination, username, password);
        connector.connect();

        String subscribe = String.join(",", consumerByKey.keySet());
        // .*代表database，..*代表table
        connector.subscribe(subscribe);
        //错误的数据回滚
        connector.rollback();

        Thread thread = new Thread(() -> {
            run = true;
            pullMsg();
        }, "canal-consumer-thread-01");
        thread.start();
    }

    /**
     * 获取消息，并对消息进行处理
     */
    private void pullMsg() {

        int batchSize = 1000;
        while (run) {
            try {
                // 获取指定数量的数据
                Message message = connector.getWithoutAck(batchSize);
                long batchId = message.getId();
                int size = message.getEntries().size();
                if (batchId == -1 || size == 0) {
                    try {
                        Thread.sleep(1000);
                        continue;
                    } catch (InterruptedException ignored) {
                        log.warn("线程中断异常");
                        Thread.currentThread().interrupt();
                    }
                }
                /*
                 * 对数据进行处理 封装成 List<DBDataChange> 对象
                 */
                List<DBDataChange> dataChanges = parseData(message.getEntries());
                if (!CollectionUtils.isEmpty(dataChanges)) {
                    // 前期验证简单，量小，非核心业务，直接同步处理，不异步线程池化
                    // 有数据，对数据进行处理。
                    dispatch(dataChanges);
                }
                // 提交确认
                connector.ack(batchId);
            } catch (Exception e) {
                log.error("pull msg error", e);
                if (CanalClientException.class.isAssignableFrom(e.getClass())) {
                    log.warn("连接无效，进行重试");
                    reconnect();
                }
            }
        }
    }

    private void reconnect() {

        while (true) {
            try {
                Thread.sleep(1000L * 60L);
            } catch (InterruptedException ignore) {
                log.warn("线程中断异常");
                Thread.currentThread().interrupt();
            }

            try {
                connector.disconnect();
                connector.connect();
                log.warn("canal reconnect success...");
                break;
            } catch (CanalClientException e) {
                log.error("reconnect exception", e);
            }

        }
    }

    private void dispatch(List<DBDataChange> dataChanges) {

        Map<String, List<DBDataChange>> dataChangesByKey =
                dataChanges.stream().collect(Collectors.groupingBy(dataChange -> dataChange.getSchemaName() + "." + dataChange.getTableName()));
        dataChangesByKey.forEach((key, values) -> {
            DBDataChangeConsumer dbDataChangeConsumer = consumerByKey.get(key);
            if (dbDataChangeConsumer != null) {
                try {
                    dbDataChangeConsumer.accept(values);
                } catch (Exception e) {
                    log.error("consume error. key: " + key + ", values: " + JSON.toJSONString(values), e);
                }
            } else {
                log.warn("no consumer. key: " + key);
            }
        });
    }

    /**
     * 对数据进行封装，处理
     * @param entryList 数据信息
     * @return 返回封装好的数据信息对象
     */
    private static List<DBDataChange> parseData(List<Entry> entryList) {

        List<DBDataChange> dataChanges = new ArrayList<>(entryList.size());
        for (Entry entry : entryList) {
            if (entry.getEntryType() == EntryType.TRANSACTIONBEGIN
                    || entry.getEntryType() == EntryType.TRANSACTIONEND) {
                continue;
            }

            RowChange rowChange;
            try {
                rowChange = RowChange.parseFrom(entry.getStoreValue());
            } catch (Exception e) {
                throw new RuntimeException("ERROR ## parser of eromanga-event has an error,data:" + entry.toString(), e);
            }
            // canal 一般监听的是 数据的变化情况 。
            if (rowChange.getIsDdl()) {
                // ddl不处理
                continue;
            }

            EventType eventType = rowChange.getEventType();
            DBDataChange dbDataChange = new DBDataChange();
            dataChanges.add(dbDataChange);

            dbDataChange.setSchemaName(entry.getHeader().getSchemaName());
            dbDataChange.setTableName(entry.getHeader().getTableName());
            dbDataChange.setEventType(eventType.toString());
            dbDataChange.setExecuteTime(entry.getHeader().getExecuteTime());
            dbDataChange.setBuildTime(System.currentTimeMillis());
            dbDataChange.setSql(rowChange.getSql());

            for (RowData rowData : rowChange.getRowDatasList()) {
                if (eventType == EventType.DELETE) {
                    // 删除，处理的是之前的数据
                    dbDataChange.addDetail(toBeforeChange(rowData.getBeforeColumnsList()));
                } else if (eventType == EventType.INSERT) {
                    // 插入，处理的是 之后的数据
                    dbDataChange.addDetail(toAfterChange(rowData.getAfterColumnsList()));
                } else {
                    Map<String, DBDataChange.Detail> beforeDetailMap = toBeforeChange(rowData.getBeforeColumnsList());
                    Map<String, DBDataChange.Detail> afterDetailMap = toAfterChange(rowData.getAfterColumnsList());
                    beforeDetailMap.forEach((key, beforeValue) ->
                            afterDetailMap.merge(key, beforeValue, (newV, oldV) -> {
                                newV.setBefore(oldV.getBefore());
                                return newV;
                            })
                    );
                    dbDataChange.addDetail(afterDetailMap);
                }
            }
        }

        return dataChanges;
    }

    /**
     * 处理 删除， 或者修改前的字段信息
     * @param columnsList 列
     * @return 返回对应的 column 为key的 对象信息
     */
    private static Map<String, DBDataChange.Detail> toBeforeChange(List<Column> columnsList) {

        Map<String, DBDataChange.Detail> detailMap = new HashMap<>(columnsList.size(), 1.0f);
        for (Column column : columnsList) {
            DBDataChange.Detail detail = buildDetail(column);
            detail.setBefore(JdbcTypeUtil.typeConvert(column.getName(), column.getValue(), column.getSqlType(), column.getMysqlType()));
            detailMap.put(detail.getColumnName(), detail);
        }
        return detailMap;
    }

    /**
     * 处理 插入， 修改后的字段信息
     * @param columnsList column 对象
     * @return 返回相应的类
     */
    private static Map<String, DBDataChange.Detail> toAfterChange(List<Column> columnsList) {

        Map<String, DBDataChange.Detail> detailMap = new HashMap<>(columnsList.size(), 1.0f);
        for (Column column : columnsList) {
            DBDataChange.Detail detail = buildDetail(column);
            detail.setAfter(JdbcTypeUtil.typeConvert(column.getName(), column.getValue(), column.getSqlType(), column.getMysqlType()));
            detailMap.put(detail.getColumnName(), detail);
        }
        return detailMap;
    }
    /**
     * 根据 column 构建对象
     * @param column column 对象
     * @return 返回相应的类
     */
    private static DBDataChange.Detail buildDetail(Column column) {
        DBDataChange.Detail detail = new DBDataChange.Detail();
        detail.setColumnName(column.getName());
        detail.setUpdated(column.getUpdated());
        return detail;
    }


}
