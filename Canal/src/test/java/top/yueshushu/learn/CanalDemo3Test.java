package top.yueshushu.learn;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StringUtils;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * canal 事务的处理。
 *
 * @author Yue Jianli
 * @date 2022-04-29
 */
@SpringBootTest
@Slf4j
public class CanalDemo3Test {
    /**
     * 一次性获取多条数据。
     * sql 执行多条。 
     */
    @Test
    public void dataMoreTest() throws Exception {
        //1. 创建 canal连接对象
        CanalConnector canalConnector = CanalConnectors.newSingleConnector(
                new InetSocketAddress(
                        "127.0.0.1", 11111
                ),
                "example",
                "canal",
                "canal"
        );
        canalConnector.connect();
        // 订阅哪个对象
        canalConnector.subscribe("springboot.user");

        for (; ; ) {

             Message message = canalConnector.getWithoutAck(3, 2L, TimeUnit.SECONDS);
            if (message.getId() == -1) {
                // 未获取到数据
                TimeUnit.MILLISECONDS.sleep(500);
                continue;
            }
            log.info(">>>>获取对应的 id: {}",message.getId());
            List<CanalEntry.Entry> entries = message.getEntries();
            for (CanalEntry.Entry entry : entries) {
                CanalEntry.EntryType entryType = entry.getEntryType();
                if (!CanalEntry.EntryType.ROWDATA.equals(entryType)) {
                    continue;
                }
                String tableName = entry.getHeader().getTableName();
                log.info(">>>>对表{} 执行操作", tableName);

                CanalEntry.RowChange rowChange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
                //对类型进行处理
                CanalEntry.EventType eventType = rowChange.getEventType();
                switch (eventType) {
                    case INSERT: {
                        insertHandler(rowChange);
                        break;
                    }
                    case UPDATE: {
                        updateHandler(rowChange);
                        break;
                    }
                    case DELETE: {
                        deleteHandler(rowChange);
                        break;
                    }
                    default: {
                        break;
                    }
                }

            }
            //进行回滚
            canalConnector.rollback();

            //确认ack 配置
          // canalConnector.ack(message.getId());
        }
    }

    private void deleteHandler(CanalEntry.RowChange rowChange) {
        log.info(">>>>执行删除的方法");
        List<CanalEntry.RowData> rowDatasList = rowChange.getRowDatasList();
        for (CanalEntry.RowData rowData : rowDatasList) {

            List<CanalEntry.Column> beforeColumnsList = rowData.getBeforeColumnsList();
            for (CanalEntry.Column column : beforeColumnsList) {

                log.info(">>>>>字段 {} 删除数据 {}", column.getName(), column.getValue());
            }
        }
    }

    private void updateHandler(CanalEntry.RowChange rowChange) {
        log.info(">>>执行更新的方法");
        List<CanalEntry.RowData> rowDatasList = rowChange.getRowDatasList();
        for (CanalEntry.RowData rowData : rowDatasList) {

            List<CanalEntry.Column> beforeColumnsList = rowData.getBeforeColumnsList();
            List<CanalEntry.Column> afterColumnsList = rowData.getAfterColumnsList();
            Map<String, String> beforeValueMap = beforeColumnsList.stream().collect(
                    Collectors.toMap(
                            CanalEntry.Column::getName,
                            CanalEntry.Column::getValue
                    )
            );
            Map<String, String> afterValueMap = afterColumnsList.stream().collect(
                    Collectors.toMap(
                            CanalEntry.Column::getName,
                            CanalEntry.Column::getValue
                    )
            );
            beforeValueMap.forEach((column, beforeValue) -> {
                String afterValue = afterValueMap.get(column);
                Boolean update = beforeValue.equals(afterValue);
                log.info("修改列:{},修改前的值:{},修改后的值:{},是否更新:{}", column, beforeValue, afterValue,
                        update);
            });
        }

    }

    /**
     * 插入数据. 只有后的数据.
     *
     * @param rowChange 行改变
     */
    private void insertHandler(CanalEntry.RowChange rowChange) {
        log.info(">>>执行添加 的方法");
        List<CanalEntry.RowData> rowDatasList = rowChange.getRowDatasList();
        for (CanalEntry.RowData rowData : rowDatasList) {
            List<CanalEntry.Column> afterColumnsList = rowData.getAfterColumnsList();

            for (CanalEntry.Column column : afterColumnsList) {
                if (!StringUtils.hasText(column.getValue())) {
                    continue;
                }
                log.info("字段 {} 插入了数据 {}", column.getName(), column.getValue());
            }

        }
    }

}
