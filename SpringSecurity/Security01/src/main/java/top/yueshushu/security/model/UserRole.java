package top.yueshushu.security.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-09-09
 */
@TableName("user_role")
@Data
public class UserRole {
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;
	@TableField("user_id")
	private Integer userId;
	@TableField("role_id")
	private Integer roleId;
}
