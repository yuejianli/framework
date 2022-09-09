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
@TableName("role")
@Data
public class Role {
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;
	@TableField("name")
	private String name;
	@TableField("description")
	private String description;
}
