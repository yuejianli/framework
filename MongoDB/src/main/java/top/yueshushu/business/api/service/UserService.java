package top.yueshushu.business.api.service;

import java.util.List;

import top.yueshushu.business.api.model.user.User;
import top.yueshushu.business.api.model.user.UserQuery;
import top.yueshushu.business.base.page.PageResult;

/**
 * 用户 service 接口
 *
 * @author yuejianli
 * @date 2022-07-19
 */

public interface UserService {
	/**
	 保存或者更新
	 */
	 void saveOrUpdate(List<User> userList);
	/**
	 根据id 删除
	 */
	 void deleteById (Integer id);
	 /**
	  * 分页查询数据
	  * @param userQuery 用户查询对象
	  */
	 PageResult<User> pageQuery(UserQuery userQuery);
}
