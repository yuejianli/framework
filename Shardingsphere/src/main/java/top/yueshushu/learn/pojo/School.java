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
 * @ClassName:School
 * @Description 学校实体
 * @Author 两个蝴蝶飞
 * @Date 2021/4/24 20:01
 * @Version 1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("school")
public class School implements Serializable {
    /**
     * @param id id编号
     * @param name 姓名
     * @param description 描述
     */
    @TableId(value="id",type = IdType.AUTO)
    private Long id;
    @TableField(value="name")
    private String name;
    @TableField(value="description")
    private String description;
}


