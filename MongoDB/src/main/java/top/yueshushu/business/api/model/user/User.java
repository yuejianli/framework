package top.yueshushu.business.api.model.user;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import top.yueshushu.business.api.model.Base;
import top.yueshushu.business.api.model.BaseEntity;

/**
 * 用户中间转换层
 * @author yuejianli
 * @date 2022-07-19
 */
@Data
public class User extends BaseEntity implements Base,Serializable {
	private Integer id;
	private String name;
	private Integer age;
	private String sex;
	private String description;
	private Date birthday;
}
