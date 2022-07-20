package top.yueshushu.model;

import org.springframework.data.mongodb.core.mapping.MongoId;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 用户实体信息
 *
 * @author yuejianli
 * @date 2022-07-18
 */
@Data
public class StringUser implements Serializable {
	// @MongoId 注解，定义 主键
	@MongoId
	private String id;
	private String name;
	private Integer age;
	private String sex;
	private String description;
	private Date birthday;
	
	private UserStatus userStatus;
}
