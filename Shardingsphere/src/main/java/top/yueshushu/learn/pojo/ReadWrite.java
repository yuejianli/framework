package top.yueshushu.learn.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
读写分离时使用的
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_read_write")
public class ReadWrite implements Serializable {
    /**
     * @param id id编号
     * @param name 名称
     * @param description 描述
     */
    @TableId(value="id",type = IdType.AUTO)
    private Integer id;
    @TableField(value="name")
    private String name;
    @TableField(value="description")
    private String description;
}


