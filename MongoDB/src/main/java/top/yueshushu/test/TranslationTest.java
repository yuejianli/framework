package top.yueshushu.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;
import top.yueshushu.model.User;
import top.yueshushu.model.UserStatus;

/**
 * 事务测试
 *
 * @author yuejianli
 * @date 2022-07-18
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class TranslationTest {
	
	private String SIMPLE_COLLECTION_NAME = "test_user1";
	
	@Resource
	private MongoTemplate mongoTemplate;
	
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
		user.setUserStatus(new UserStatus().setWeight(130d).setHeight(171d));
		return user;
	}
	
	
	@Test
	@Transactional(rollbackFor = Exception.class)
	public void insertTest(){
		Integer id = 11;
		User user = createUser(id);
		User insertUser = mongoTemplate.insert(user, SIMPLE_COLLECTION_NAME);
		log.info(">>> insert user:{}",insertUser);
		
		int a = 10/0;
		log.info(">>> user:{}",mongoTemplate.findById(id,User.class,SIMPLE_COLLECTION_NAME));

	}
	
}
