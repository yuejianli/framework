package top.yueshushu.test;

import com.mongodb.client.ListIndexesIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;

import org.bson.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

/**
 * 索引的相关操作
 * @author yuejianli
 * @date 2022-07-18
 */
@SpringBootTest
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
public class IndexTest {
	@Resource
	private MongoTemplate mongoTemplate;
	
	private final String  SIMPLE_COLLECTION_NAME = "test_user1";
	
	/**
	 创建普通的升序索引
	 createIndex    Indexes.ascending
	 */
	@Test
	public void createAscIndexTest (){
		
		mongoTemplate.getCollection(SIMPLE_COLLECTION_NAME)
				.createIndex(Indexes.ascending("age","sex"));
	}
	/**
	 创建普通的降序索引
	 createIndex  Indexes.descending
	 */
	@Test
	public void createDescIndexTest(){
		mongoTemplate.getCollection(SIMPLE_COLLECTION_NAME)
				.createIndex(Indexes.descending("name"));
		
	}
	
	/**
		创建文字索引
	 */
	@Test
	public void createTextIndexTest(){
		mongoTemplate.getCollection(SIMPLE_COLLECTION_NAME)
				.createIndex(Indexes.text("name"));
		
	}
	
	
	/**
	 创建哈希索引
	 */
	@Test
	public void createHashIndexTest(){
		mongoTemplate.getCollection(SIMPLE_COLLECTION_NAME)
				.createIndex(Indexes.hashed("name"));
		
	}
	
	/**
	 创建唯一索引
	 */
	@Test
	public void createUniqueIndexTest(){
		
		IndexOptions indexOptions = new IndexOptions();
		indexOptions.unique(true);
		indexOptions.name("unique_name");
		
		mongoTemplate.getCollection(SIMPLE_COLLECTION_NAME)
				.createIndex(Indexes.ascending("name"),indexOptions);
		
	}
	
	/**
	 创建局部索引
	 */
	@Test
	public void createPartialIndexTest(){
		
		IndexOptions indexOptions = new IndexOptions();
		indexOptions.unique(true);
		indexOptions.name("unique_name");
		indexOptions.partialFilterExpression(Filters.exists("name",true));
		mongoTemplate.getCollection(SIMPLE_COLLECTION_NAME)
				.createIndex(Indexes.ascending("name"),indexOptions);
		
	}
	
	/**
	 查询所有索引
	 
	 >>> user index :Document{{v=2, key=Document{{_id=1}}, name=_id_}}
	>>> user index :Document{{v=2, key=Document{{age=1, sex=1}}, name=age_1_sex_1}}
	  >>> user index :Document{{v=2, key=Document{{name=-1}}, name=name_-1}}
	 */
	@Test
	public void showIndexTest(){
		ListIndexesIterable<Document> documents = mongoTemplate.getDb().getCollection(SIMPLE_COLLECTION_NAME).listIndexes();
		List<String> indexNameList = new ArrayList<>();
		documents.forEach(
				n->{
					Object name = n.get("name");
					indexNameList.add(name.toString());
				}
		);
		//  index names :[_id_, age_1_sex_1, name_-1]
		log.info(">>> index names :{}",indexNameList);
	}
	
	/**
	 删除单个索引
	 */
	@Test
	public void removeSingleIndex() {
		showIndexTest();
		//删除索引
		mongoTemplate.getCollection(SIMPLE_COLLECTION_NAME).dropIndex("name_-1");
		
		showIndexTest();
	}
	
	/**
	 删除全部索引
	 */
	@Test
	public void removeAllIndex() {
		showIndexTest();
		//删除全部索引
		mongoTemplate.getCollection(SIMPLE_COLLECTION_NAME).dropIndexes();
		// _id_ 索引是默认的，不能删除。
		showIndexTest();
	}
	
}
