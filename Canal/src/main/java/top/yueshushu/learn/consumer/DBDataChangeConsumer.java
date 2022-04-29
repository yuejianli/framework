package top.yueshushu.learn.consumer;
import top.yueshushu.learn.consumer.dto.DBDataChange;

import java.util.List;

/**
 * 数据库配置信息
 */
public interface DBDataChangeConsumer {
    /**
     * 获取数据库配置信息。
     * 针对单个数据库时，可以设置默认的 方法，返回默认的数据库。
     * @return 返回默认的数据库名
     */
    default String getSchemaName() {
        return "springboot";
    }

    /**
     * 获取表名
     * @return 返回对应的表名。
     * 每一个实现 Impl 都是针对的一个表
     */
    String getTableName();

    /**
     * 接收数据之后的处理
     * @param dataChanges
     */
    void accept(List<DBDataChange> dataChanges);
}
