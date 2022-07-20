package top.yueshushu.business.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;
import top.yueshushu.business.api.model.user.User;
import top.yueshushu.business.api.model.user.UserQuery;
import top.yueshushu.business.api.service.UserService;
import top.yueshushu.business.base.page.PageQuery;
import top.yueshushu.business.base.page.PageResult;

/**
 * 用户接口测试
 * @author yuejianli
 * @date 2022-07-19
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class UserServiceTest {
	
	@Resource
	private UserService userService;
	
	/**
	 创建用户对象
	 */
	private User createUser(Integer id) {
		User user = new User();
		if (null != id){
			user.setId(id);
		}
		user.setName("岳泽霖");
		user.setAge(28);
		user.setDescription("一个快乐的程序员");
		user.setBirthday(new Date());
		user.setSex("男");
		return user;
	}
	/**
	 保存或者更新
	 */
	@Test
	public void saveTest(){

		User user1 = createUser(11);
		User user2 = createUser(12);
		User user3 = createUser(13);
		User user4 = createUser(10);
		user4.setName("我要修改了");
		
		List<User> userList = new ArrayList<>();
		userList.add(user1);
		userList.add(user2);
		userList.add(user3);
		userList.add(user4);
		
		// 批量插入多条用户
		userService.saveOrUpdate(userList);
		log.info(">>> save update success ");
	}
	/**
	删除用户
	 */
	@Test
	public void deleteTest () {
		userService.deleteById(10);
		log.info("delete user success");
	}
	
	/**
	 查询用户
	 */
	@Test
	public void listUserTest(){
		UserQuery userQuery = new UserQuery();
		userQuery.setPage(PageQuery.of(10,1));
		
		userQuery.setQueryCondition( UserQuery.QueryCondition.of("",true));
		
		// 设置查询条件
		UserQuery.FilterCondition filterCondition = UserQuery.FilterCondition.builder().sex("男").age(18).build();
		
		userQuery.setFilterCondition(filterCondition);
		
		
		PageResult<User> userPageResult = userService.pageQuery(userQuery);
		
		log.info("user total :{}",userPageResult);
	}
}
