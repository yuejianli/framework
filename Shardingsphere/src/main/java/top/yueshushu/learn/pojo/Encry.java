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
@TableName("encry")
public class Encry implements Serializable {
    /**
     * @param id id编号
     * @param account 账号
     * @param ps ps 密码
     */
    @TableId(value="id",type = IdType.AUTO)
    private Integer id;
    @TableField(value="account")
    private String account;
    @TableField(value="ps_origin")
    private String ps;
}


