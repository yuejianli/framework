package top.yueshushu.model;

import java.io.Serializable;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 用户的体重信息
 *
 * @author yuejianli
 * @date 2022-07-18
 */
@Data
@Accessors(chain = true)
public class UserStatus implements Serializable {
	/**
		@param weight 体重
		@param height 身高
	 */
	private Double weight;
	private Double height;
}
