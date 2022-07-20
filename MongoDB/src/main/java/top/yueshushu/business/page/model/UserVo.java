package top.yueshushu.business.page.model;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 用户展示Vo
 *
 * @author yuejianli
 * @date 2022-07-19
 */
@Data
public class UserVo implements Serializable {
	private Integer id;
	private String name;
	private Integer age;
	private String sex;
	private String description;
	private Date birthday;
}
