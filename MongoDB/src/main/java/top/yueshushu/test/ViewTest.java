package top.yueshushu.test;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;
import top.yueshushu.model.User;

/**
 * 视图操作
 *
 * @author yuejianli
 * @date 2022-07-18
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class ViewTest {
	@Resource
	private MongoTemplate mongoTemplate;
	/**
 		创建视图
	 */
	@Test
	public void createView() {
		String viewName = "v_user";
		String collectionName = "test_user1";
		
		boolean viewExists = mongoTemplate.collectionExists(viewName);
		log.info("exist view :{}",viewExists?"存在视图":"不存在视图");
		
		// 定义视图的管道，可设置多个筛选条件
		List<Bson> pipeline = new ArrayList<>();
		pipeline.add(Document.parse("{\"$match\":{\"sex\":\"男\"}}"));
		
		//执行创建视图
		mongoTemplate.getDb().createView(viewName,collectionName,pipeline);
		
		// 再次看视图是否存在
		 viewExists = mongoTemplate.collectionExists(viewName);
		log.info("exist view :{}",viewExists?"存在视图":"不存在视图");
	}
	/**
	利用视图进行查询
	 */
	@Test
	public void viewShowTest(){
		List<User> userList = mongoTemplate.findAll(User.class, "v_user");
		userList.forEach(
				n->{
					log.info("user is:{}",n);
				}
		);
	}
	/**
	 删除视图
	 */
	@Test
	public void deleteViewTest(){
		String viewName = "v_user";
		
		if (mongoTemplate.collectionExists(viewName)){
			log.info(">>> count view {},begin drop",viewName);
			// 删除集合
			mongoTemplate.getDb().getCollection(viewName).drop();
		}
		
		boolean viewExists = mongoTemplate.collectionExists(viewName);
		log.info(">>> view remove :{}",!viewExists?"成功":"失败");
	}
}
