package top.yueshushu.learn;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.CanalEntry.RowChange;
import com.alibaba.otter.canal.protocol.Message;
import com.google.protobuf.ByteString;
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
 * canal 获取数据库变更处理.
 *
 * @author Yue Jianli
 * @date 2022-04-28
 *
 * -- 使用命令登录：mysql -u canal -p
 * -- 创建用户 用户名：canal 密码：canal
 * create user 'canal'@'%' identified by 'canal';
 * -- 授权 *.*表示所有库
 * grant SELECT, REPLICATION SLAVE, REPLICATION CLIENT on *.* to 'canal'@'%' identified by 'Canal@123456';
 *
 * 一条记录重复执行的话，不会发送日志。
 */
@SpringBootTest
@Slf4j
public class CanalDemoTest {
    /**
     * 一个简单的canal 的连接测试程序
     */
    @Test
    public void connectionTest() {
        //1. 创建连接  填充对应的地址信息 ,要监控的实例和相应的用户名和密码
        CanalConnector canalConnector = CanalConnectors.newSingleConnector(
                new InetSocketAddress(
                        "127.0.0.1", 11111
                ),
                "example",
                "canal",
                "canal"
        );
        //2. 进行连接
        canalConnector.connect();
        log.info(">>>连接成功:{}", canalConnector);
    }

    /**
     * 获取数据信息. 可以发现,未获取到数据 .  这个应该是实时的.
     */
    @Test
    public void getDataTest() {
        //1. 创建连接
        CanalConnector canalConnector = CanalConnectors.newSingleConnector(
                new InetSocketAddress("127.0.0.1", 11111),
                "example",
                "canal",
                "canal"
        );
        // 进行连接
        canalConnector.connect();

        //3. 注册,看使用哪个数据库表
        canalConnector.subscribe("springboot.user");

        //4. 获取 1条数据
        Message message = canalConnector.get(1);
        log.info("获取的数据:id:{},数据:{}", message.getId(), message);
        if (message.getId() == -1) {
            log.info(">>>未获取到数据");
            return;
        }
        //5. 获取相应的数据集合
        List<CanalEntry.Entry> entries = message.getEntries();
        for (CanalEntry.Entry entry : entries) {
            log.info(">>>获取数据 {}", entry);
            //获取表名
            CanalEntry.Header header = entry.getHeader();
            log.info(">>>获取表名:{}", header.getTableName());
            CanalEntry.EntryType entryType = entry.getEntryType();
            log.info(">>获取类型 {}:,对应的信息:{}", entryType.getNumber(), entryType.name());

            //获取数据
            ByteString storeValue = entry.getStoreValue();
            log.info(">>>输出存储的值:{}", storeValue);
        }
    }

    /**
     * 获取数据信息. 获取现在的数据.  再次执行时,就没有这个数据了.
     */
    @Test
    public void getNowDataTest() {
        //1. 创建连接
        CanalConnector canalConnector = CanalConnectors.newSingleConnector(
                new InetSocketAddress("127.0.0.1", 11111),
                "example",
                "canal",
                "canal"
        );
        // 进行连接
        canalConnector.connect();

        //3. 注册,看使用哪个数据库表
        canalConnector.subscribe("springboot.user");
        for (;;) {
            //4. 获取 1条数据
            Message message = canalConnector.get(1);
            log.info("获取的数据:id:{},数据:{}", message.getId(), message);
            if (message.getId() == -1) {
                log.info(">>>未获取到数据");
                try {
                    TimeUnit.MILLISECONDS.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }
            //5. 获取相应的数据集合
            List<CanalEntry.Entry> entries = message.getEntries();
            for (CanalEntry.Entry entry : entries) {
                log.info(">>>获取数据 {}", entry);
                //获取表名
                CanalEntry.Header header = entry.getHeader();
                log.info(">>>获取表名:{}", header.getTableName());
                CanalEntry.EntryType entryType = entry.getEntryType();
                log.info(">>获取类型 {}:,对应的信息:{}", entryType.getNumber(), entryType.name());

                //获取数据
                ByteString storeValue = entry.getStoreValue();
                log.info(">>>输出存储的值:{}", storeValue);
            }
        }
    }

    /**
     * 将 storeValue 进行解析,解析成我们能看懂的语句.
     * 对数据库 cud 进行处理操作观看一下.
     * 发现,点是不好的,也有多余的记录信息.
     *
     * @throws Exception 异常
     */
    @Test
    public void convertDataTest() throws Exception {
        //1. 创建连接
        CanalConnector canalConnector = CanalConnectors.newSingleConnector(
                new InetSocketAddress("127.0.0.1", 11111),
                "example",
                "canal", "canal"
        );
        //2. 进行连接
        canalConnector.connect();
        canalConnector.subscribe("springboot.user");

        for (;;) {
            //获取信息
            Message message = canalConnector.get(1);
            if (message.getId() == -1L) {
                // log.info("未获取到数据");
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }
            List<CanalEntry.Entry> entryList = message.getEntries();
            //对获取到的数据进行处理
            log.info(">>获取到{}条数据", entryList.size());
            for (CanalEntry.Entry entry : entryList) {
                CanalEntry.Header header = entry.getHeader();
                log.info(">>>获取表名:{}", header.getTableName());
                //获取类型.
                CanalEntry.EntryType entryType = entry.getEntryType();
                log.info(">>类型编号 {},类型名称:{}", entryType.getNumber(), entryType.name());

                //获取存入日志的值
                ByteString storeValue = entry.getStoreValue();
                //将这个值进行解析
                CanalEntry.RowChange rowChange = RowChange.parseFrom(storeValue);
                String sql = rowChange.getSql();
                log.info(">>>获取对应的sql:{}", sql);
                // 这个sql 可能是 批量的sql语句
                List<CanalEntry.RowData> rowDatasList = rowChange.getRowDatasList();
                for (CanalEntry.RowData rowData : rowDatasList) {
                    log.info(">>>获取信息:{}", rowData);
                    //对数据进行处理
                    List<CanalEntry.Column> beforeColumnsList = rowData.getBeforeColumnsList();
                    List<CanalEntry.Column> afterColumnsList = rowData.getAfterColumnsList();

                    beforeColumnsList.forEach(
                            n -> log.info("哪个列{},原先是{},是否被更新{}", n.getName(),
                                    n.getValue(), n.getUpdated())
                    );
                    afterColumnsList.forEach(
                            n -> log.info("哪个列{},后来是{},是否被更新{}", n.getName(), n.getValue(), n.getUpdated())
                    );
                }
            }
        }

    }

    /**
     * 类型转换数据
     *
     * @throws Exception 异常
     */
    @Test
    public void dataTypeTest() throws Exception {
        CanalConnector canalConnector = CanalConnectors.newSingleConnector(
                new InetSocketAddress(
                        "127.0.0.1", 11111
                ),
                "example",
                "canal", "canal"
        );
        canalConnector.connect();
        canalConnector.subscribe("springboot.user");

        for(;;){
            Message message = canalConnector.get(1);
            if (message.getId() == -1) {
                TimeUnit.SECONDS.sleep(1);
                continue;
            }

            List<CanalEntry.Entry> entries = message.getEntries();
            for (CanalEntry.Entry entry : entries) {
                CanalEntry.EntryType entryType = entry.getEntryType();
                //只要 RowData 数据类型的
                if (!CanalEntry.EntryType.ROWDATA.equals(entryType)) {
                    continue;
                }
                String tableName = entry.getHeader().getTableName();
                log.info(">>>对表 {} 进行操作", tableName);
                ByteString storeValue = entry.getStoreValue();
                RowChange rowChange = RowChange.parseFrom(storeValue);
                //行改变
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
        }

    }

    private void deleteHandler(RowChange rowChange) {
        log.info(">>>>执行删除的方法");
        List<CanalEntry.RowData> rowDatasList = rowChange.getRowDatasList();
        for (CanalEntry.RowData rowData : rowDatasList) {

            List<CanalEntry.Column> beforeColumnsList = rowData.getBeforeColumnsList();
            for (CanalEntry.Column column : beforeColumnsList) {

                log.info(">>>>>字段 {} 删除数据 {}", column.getName(), column.getValue());
            }
        }
    }

    private void updateHandler(RowChange rowChange) {
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
    private void insertHandler(RowChange rowChange) {
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
