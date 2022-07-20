package top.yueshushu.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;
import top.yueshushu.model.User;

/**
 * 文档查询
 *
 * @author yuejianli
 * @date 2022-07-18
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class DocumentQueryTest {
	private String SIMPLE_COLLECTION_NAME = "test_user1";
	
	@Resource
	private MongoTemplate mongoTemplate;
	/**
	 查询全部
	 */
	@Test
	public void findAll() {
		List<User> all = mongoTemplate.findAll(User.class, SIMPLE_COLLECTION_NAME);
		all.forEach(
				n->{
					log.info("user :{}",n);
				}
		);
	}
	/**
	 根据id 查询
	 */
	@Test
	public void findByIdTest(){
		User user = mongoTemplate.findById(1, User.class,SIMPLE_COLLECTION_NAME);
		log.info(">>> user:{}",user);
	}
	
	/**
	  只查询一个
	 */
	@Test
	public void findOneTest() {
		//1. 构建对象信息
		Criteria criteria = Criteria.where("age").gte(20);
		
		Query query = new Query(criteria);
		User one = mongoTemplate.findOne(query, User.class, SIMPLE_COLLECTION_NAME);
		log.info(">> query one: {}",one);
	}
	
	/**
	  多条件查询
	 */
	@Test
	public void findByConditionTest (){
		
		Query query = new Query();
		//1. 构造条件
		Criteria criteria = Criteria.where("age").gte(20);
		
		query.addCriteria(criteria);
		
		
		Criteria sexCriteria = Criteria.where("sex").is("男");
		
		query.addCriteria(sexCriteria);
		
		// 进行查询
		
		List<User> users = mongoTemplate.find(query, User.class, SIMPLE_COLLECTION_NAME);
		users.forEach(
				n->{
					log.info("user :{}",n);
				}
		);
	}
	
	// keyword 查询
	
	@Test
	public void keywordTest (){
		Query query = new Query();
		
		query.addCriteria(Criteria.where("age").gte(20));
		
		// 对 keyword 的处理
		
		String keywrod = "岳泽";
		if (false){
			//精确查询
			Criteria keywordCriteria = new Criteria();
			keywordCriteria.orOperator(Criteria.where("name").is(keywrod),Criteria.where("description").is(keywrod));
			query.addCriteria(keywordCriteria);
		}else{
			// 模糊匹配
			Criteria keywordCriteria = new Criteria();
			keywordCriteria.orOperator(Criteria.where("name").regex(".*?" + keywrod+ ".*"),Criteria.where("description").regex(".*?" + keywrod+ ".*"));
			query.addCriteria(keywordCriteria);
		}
		
		// 进行查询
		List<User> userList = mongoTemplate.find(query, User.class, SIMPLE_COLLECTION_NAME);
		
		userList.forEach(
				n->{
					log.info("user info:{}",n);
				}
		);
	}
	
	/**
	 排序 ， Sort.Order.desc  Sort.Order.asc() 排序
	 */
	@Test
	public void sortTest() {
		
		Query query = new Query();
		query.addCriteria(Criteria.where("age").gte(18));
	//	query.with(Sort.by(Sort.Order.desc("age")));
		
		List<Sort.Order> orderList = new ArrayList<>();
		orderList.add(Sort.Order.desc("age"));
		orderList.add(Sort.Order.desc("id"));
		query.with(Sort.by(orderList));
		
		List<User> userList = mongoTemplate.find(query, User.class, SIMPLE_COLLECTION_NAME);
		userList.forEach(
				n->{
					log.info("user sort info:{}",n);
				}
		);
	}
	/**
	 分页， 使用的是  skip() 跳过  limit() 最大展示数量
	 */
	@Test
	public void pageTest() {
		
		Query query = new Query();
		query.addCriteria(Criteria.where("age").gte(18));
		
		//
		int pageNum = 2;
		int pageSize = 2;
		
		query.with(Sort.by(Sort.Order.desc("age"))).skip((pageNum-1)*pageSize).limit(pageSize);
		
		List<User> userList = mongoTemplate.find(query, User.class, SIMPLE_COLLECTION_NAME);
		userList.forEach(
				n->{
					log.info("user page info:{}",n);
				}
		);
	}
	/**
	 存在  exist 测试
	 */
	@Test
	public void existTest() {
		
		Query query = new Query();
		//存在年龄的
		query.addCriteria(Criteria.where("oo").exists(true));
		
		List<User> userList = mongoTemplate.find(query, User.class, SIMPLE_COLLECTION_NAME);
		userList.forEach(
				n->{
					log.info("user page info:{}",n);
				}
		);
	}
	
	
	
}
