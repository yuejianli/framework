package top.yueshushu.test;

import com.mongodb.client.MongoCollection;

import org.bson.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.validation.Validator;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Set;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;
import top.yueshushu.service.MongoDBCollectionService;

/**
 * 集合的相关操作
 * @author yuejianli
 * @date 2022-07-18
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class CollectionTest {
	@Resource
	private MongoDBCollectionService mongoDBCollectionService;

	@Resource
	private MongoTemplate mongoTemplate;
	
	/**
	 创建普通的集合
	 
	 已经存在的集合，再次添加会报错:
	 
	 The full response is {"ok": 0.0, "errmsg": "Collection already exists. NS: zk_monitor.test_user1", "code": 48, "codeName": "NamespaceExists"}
	 */
	@Test
	public void createSimpleTest(){
		String collectionName = "test_user1";
		MongoCollection<Document> collection = mongoTemplate.createCollection(collectionName);
		log.info("create simple Collection {}",collection);
	}
	/**
	 创建固定大小的集合
	 */
	@Test
	public void createFixedTest(){
		String collectionName = "test_user2";
		
		long size = 2048L;
		long maxCount = 3L ;
		// 构建集合选项
		CollectionOptions collectionOptions = CollectionOptions.empty()
				//固定的集合， 最大为 2048KB  或者 是3 条记录
					.capped()
					.size(size)
					.maxDocuments(maxCount);
		// 创建集合
		mongoTemplate.createCollection(collectionName,collectionOptions);
		log.info("create capped collection success");
	}
	
	/**
	 创建验证集合
	 
	 * 创建集合并在文档"插入"与"更新"时进行数据效验，如果符合创建集合设置的条件就进允许更新与插入，否则则按照设置的设置的策略进行处理。
	 *
	 * * 效验级别：
	 *   - off：关闭数据校验。
	 *   - strict：(默认值) 对所有的文档"插入"与"更新"操作有效。
	 *   - moderate：仅对"插入"和满足校验规则的"文档"做"更新"操作有效。对已存在的不符合校验规则的"文档"无效。
	 * * 执行策略：
	 *   - error：(默认值) 文档必须满足校验规则，才能被写入。
	 *   - warn：对于"文档"不符合校验规则的 MongoDB 允许写入，但会记录一条告警到 mongod.log 中去。日志内容记录报错信息以及该"文档"的完整记录。
	 */
	@Test
	public void createValidateTest(){
		String collectionName = "test_user3";
		 // 年龄大于等于18
		Criteria criteria = Criteria.where("age").gte(18);
		// 构建 集合选项
		CollectionOptions collectionOptions = CollectionOptions.empty()
					.validator(Validator.criteria(criteria))
				// 严格模式，还有 offValidation()  moderateValidation
					.strictValidation()
				// 有 warnOnValidationError
					.failOnValidationError();
		
		// 创建集合
		mongoTemplate.createCollection(collectionName,collectionOptions);
		
		log.info("create validate collection success");
	}
	
	/**
	  查询全部的集合列表
	 getCollectionNames()  获取所有的集合列表
	 */
	@Test
	public void findCollectionTest() {
		Set<String> collectionNames = mongoTemplate.getCollectionNames();
		// [lcd_device_model, tool, middleware_info, tool_job, test_user3, pricetag_refresh_status, store, alarm_rule, log_info, application, middleware_user, user, operation_log, alarm_setting, middleware_log,
		// tool_merchant, ap_station, alarm_type, user_token, alarm_message, lcd_device, ap_station_online_offline_info, api_log, pricetag, test_user2, merchant, test_user1]
		log.info(">>> find collection names is :{}",collectionNames);
	}
	/**
	 查询集合是否存在
	 */
	@Test
	public void existTest(){
		
		boolean flagUser1 = mongoTemplate.collectionExists("test_user1");
		
		log.info(">>>collection test_user1 is:{}",flagUser1?"存在":"不存在");
		
		boolean flagUser4 = mongoTemplate.collectionExists("test_user4");
		log.info(">>> collection test_user4 is:{}",flagUser4?"存在":"不存在");
		
	}
	
	/**
	 删除集合   可以删除一个不存在的集合
	 
	 dropCollection
	 */
	@Test
	public void dropTest(){
		String collectionName = "test_user4";
//		// 创建普通集合
//		mongoTemplate.createCollection(collectionName);
//		// 看集合是否存在
//		boolean b = mongoTemplate.collectionExists(collectionName);
//
//		log.info(">>> create after, collection {} is:{}",collectionName,b?"存在":"不存在");
		
		// 删除集合
		mongoTemplate.dropCollection(collectionName);
		
		
		boolean b = mongoTemplate.collectionExists(collectionName);
		
		log.info(">>> drop after,collection {} is:{}",collectionName,b?"存在":"不存在");
	}
	
	
	
	
}
