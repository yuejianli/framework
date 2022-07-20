package top.yueshushu.business.repository.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 用户Do
 *
 * @author yuejianli
 * @date 2022-07-19
 */
@Document(collection = "test_user1")
@Data
public class UserDo implements Serializable {
	// @MongoId 注解，定义 主键
	@MongoId
	private Integer id;
	private String name;
	private Integer age;
	private String sex;
	private String description;
	private Date birthday;
}
