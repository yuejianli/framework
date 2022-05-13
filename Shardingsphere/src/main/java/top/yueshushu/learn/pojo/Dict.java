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
@TableName("dict")
public class Dict implements Serializable {
    /**
     * @param id id编号
     * @param module 型号
     * @param dvalue 值信息
     */
    @TableId(value="id",type = IdType.AUTO)
    private Long id;
    @TableField(value="module")
    private String module;
    @TableField(value="dvalue")
    private String dvalue;
}


