package top.yueshushu.test;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;
import top.yueshushu.model.StringUser;
import top.yueshushu.model.User;
import top.yueshushu.model.UserStatus;

/**
 * 文档操作
 *
 * @author yuejianli
 * @date 2022-07-18
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class DocumentTest {
	
	private String SIMPLE_COLLECTION_NAME = "test_user1";
	private String FIXED_COLLECTION_NAME = "test_user2";
	private String VALIDATE_COLLECTION_NAME = "test_user3";
	
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
	
	/**
	 创建用户对象
	 */
	private StringUser createStringUser(String id) {
		StringUser user = new StringUser();
		if (null != id){
			user.setId(id);
		}
		user.setName("岳泽霖字符串");
		user.setAge(28);
		user.setDescription("一个快乐的程序员");
		user.setBirthday(new Date());
		user.setSex("男");
		user.setUserStatus(new UserStatus().setWeight(130d).setHeight(171d));
		return user;
	}
	
	/**
	插入文档到集合里面
	 插入相同的id 会报错:  Write error: WriteError{code=11000, message='E11000 duplicate key error collection: zk_monitor.test_user1
	 index: _id_ dup key: { _id: 1 }', details={}}.
	 
	 如果 id 非字符串，不会自动创建。
	 如果是字符串，会自动创建， 并且会回显出来。
	 */
	@Test
	public void insertTest(){
//		User user = createUser(1);
//		User insertUser = mongoTemplate.insert(user, SIMPLE_COLLECTION_NAME);
//		log.info(">>> insert user:{}",insertUser);
//
		StringUser user = createStringUser(null);
		StringUser insertUser = mongoTemplate.insert(user, SIMPLE_COLLECTION_NAME);
		log.info(">>> insert no id user:{}",insertUser);
	}
	
	/**
	  批量插入多条数据,
	 使用的方法是   insert
	 */
	@Test
	public void insertManyTest(){
		User user1 = createUser(5);
		User user2 = createUser(6);
		
		List<User> userList = new ArrayList<>();
		userList.add(user1);
		userList.add(user2);
		
		// 批量插入多条用户
		Collection<User> insertUserList = mongoTemplate.insert(userList, SIMPLE_COLLECTION_NAME);
		
		insertUserList.forEach(
				n->{
					log.info("insert user:{}",n);
				}
		);
	}
	
	/**
	 保存或者更新用户
	 如果 id 不存在，就保存。
	 如果id 存在，就更新。
	 */
	
	@Test
	public void saveTest(){
		
		User user = createUser(4);
		
		User saveUser = mongoTemplate.save(user, SIMPLE_COLLECTION_NAME);
		log.info(">>> save user:{}",saveUser);
		
		user = createUser(4);
		user.setName("两个蝴蝶飞");
		
		saveUser = mongoTemplate.save(user,SIMPLE_COLLECTION_NAME);
		log.info(">>> save update user:{}",saveUser);
	}
	
	/**
	 修改对象
	 upsert  更新查询出的第一条记录， 如果未查询到记录，则插入这一条记录。
	 
	 UpdateResult    matchedCount 匹配的数量，   modifiedCount() 更新的数量。
	 */
	@Test
	public void updateTest() {
		
		Query query = new Query();
		// 设置更新的条件， 大于 18
		query.addCriteria(Criteria.where("age").gte(18));
		
		// 设置要更新的值信息
		Update update = new Update().set("age",28).set("description","一个28岁的程序员");
		
		// 进行更新操作
		
		UpdateResult updateResult = mongoTemplate.upsert(query, update, User.class, SIMPLE_COLLECTION_NAME);
		
		// 查询数据 ,
		log.info(">>> total query {}, update {} ",updateResult.getMatchedCount(),updateResult.getModifiedCount());
		
	}
	/**
	 更新第一条记录
	 */
	@Test
	public void updateFirstTest() {
		Query query = new Query();
		// 设置更新的条件， >=29
		query.addCriteria(Criteria.where("age").gte(29));
		
		// 设置要更新的值信息, 可以添加没有的列。
		Update update = new Update().set("age",28).set("description","一个28岁的老程序员").set("heihei","no");
		
		// 进行更新操作
		
		UpdateResult updateResult = mongoTemplate.updateFirst(query, update, User.class, SIMPLE_COLLECTION_NAME);
		
		// 查询数据 ,
		log.info(">>> total query {}, update {} ",updateResult.getMatchedCount(),updateResult.getModifiedCount());
	}
	
	/**
	 更新所有符合条件 记录
	 */
	@Test
	public void updateMoreTest(){
		Query query = new Query();
		// 设置更新的条件， >=18
		query.addCriteria(Criteria.where("age").gte(18));
		
		// 设置要更新的值信息, 可以添加没有的列。
		Update update = new Update().set("age",28).set("description","不知不觉已成年").set("heihei","no");
		
		// 进行更新操作
		
		UpdateResult updateResult = mongoTemplate.updateMulti(query, update, User.class, SIMPLE_COLLECTION_NAME);
		
		// 查询数据 ,
		log.info(">>> total query {}, update {} ",updateResult.getMatchedCount(),updateResult.getModifiedCount());
	}
	
	/**
	 *删除操作
	 */
	@Test
	public void removeTest(){
		
		Query query = new Query();
		// 要删除的条件
		query.addCriteria(Criteria.where("age").is(26));
		// 返回删除的结果
		DeleteResult deleteResult = mongoTemplate.remove(query, SIMPLE_COLLECTION_NAME);
		
		log.info(">>> delete count:{}",deleteResult.getDeletedCount());
	}
	
	/**
	 删除数据，并返回删除的一条数量
	 findAndRemove() 删除的一条记录
	 */
	@Test
	public void removeOneTest() {
		Query query = new Query();
		// 要删除的条件
		query.addCriteria(Criteria.where("age").is(27));
		
		User removeUser = mongoTemplate.findAndRemove(query,User.class,SIMPLE_COLLECTION_NAME);
		
		log.info(">>> delete user:{}",removeUser);
	}
	/**
	 删除所有的符合条件的用户
	 */
	@Test
	public void removeAllTest() {
		Query query = new Query();
		// 要删除的条件
		query.addCriteria(Criteria.where("age").gte(18));
		
		List<User> allAndRemoveList = mongoTemplate.findAllAndRemove(query, User.class, SIMPLE_COLLECTION_NAME);
		allAndRemoveList.forEach(
				n->{
					log.info("delete user:{}",n);
				}
		);
	}
}
