package top.yueshushu.learn.consumer.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author yjl
 * @date 2022-04-29
 */
@Data
public class DBDataChange implements Serializable {

    private static final long serialVersionUID = -1L;
    /**
     * 数据库名
     */
    private String schemaName;
    /**
     * 监控的表名
     */
    private String tableName;
    /**
     * 对应的事件类型  INSERT  UPDATE  DELETE
     */
    private String eventType;
    /**
     * 操作时间
     */
    private long operatorTime;
    /**
     * 执行时间
     */
    private long executeTime;
    /**
     * 构建时间
     */
    private long buildTime;
    /**
     * 执行的 sql
     */
    private String sql;
    /**
     二维数组结构，一次dml，可能操作多行
     map.key: columnName  针对的是同一条sql的处理。
     */
    private List<Map<String, Detail>> dataList;

    public void addDetail(Map<String, Detail> detailMap) {
        if (null == dataList) {
            this.dataList = new ArrayList<>();
        }
        this.dataList.add(detailMap);
    }

    public void setDataList(List<Map<String, Detail>> dataList) {
        this.dataList = dataList;
    }

    @Data
    public static class Detail implements Serializable {

        private static final long serialVersionUID = 1L;
        /**
         * 列名
         */
        String columnName;
        /**
         * 是否更新
         */
        boolean updated;
        /**
         * 之前的值
         */
        Object before;
        /**
         * 现在的值
         */
        Object after;
    }
}
