package top.yueshushu.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

/**
 * 聚合对象操作
 *
 * @author yuejianli
 * @date 2022-07-19
 */
@SpringBootTest
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
public class AggregationTest {
	@Resource
	private MongoTemplate mongoTemplate;
	
	private final String SIMPLE_COLLECATION_NAME = "test_user1";
	/**
	 *分组后统计， 对应的 sql 应该是:
	 * 
	 * select age,count(*) as ageCount from user group by age 
	 */
	@Test
	public void groupAggregationTest(){
		
		//1. 定义分组对象信息 
		GroupOperation age = Aggregation.group("age");
		// 执行某个操作，  count() 表示统计数量的操作.
		GroupOperation ageCount = age.count().as("ageCount");
		
		//2. 添加到对象里面
		Aggregation aggregation = Aggregation.newAggregation(ageCount);
		
		//3. 执行操作
		
		AggregationResults<HashMap> aggregateResults = mongoTemplate.aggregate(aggregation, SIMPLE_COLLECATION_NAME, HashMap.class);
		
		//  n 对象是:{ageCount=1, _id=30}
		aggregateResults.getMappedResults().forEach(
				n->{
					log.info("age {} is count {}",n.get("_id"),n.get("ageCount"));
				}
		);
	}
	
	/**
	  根据性别分组，查询最大的 年龄信息
	 
	 select sex,max(age) as maxAge from user group by sex
	 
	 */
	@Test
	public void groupSexMaxAgeTest(){
		//1. 定义聚合对象
		AggregationOperation aggregationOperation = Aggregation.group("sex").max("age").as("maxAge");
		
		//2. 构建填充到聚合对象里面
		
		Aggregation aggregation = Aggregation.newAggregation(aggregationOperation);
		
		//3. 查询构建聚合对象
		
		AggregationResults<HashMap> aggregationResults = mongoTemplate.aggregate(aggregation, SIMPLE_COLLECATION_NAME, HashMap.class);
		
		// 4. 对结果进行遍历
		// n 对应是: {maxAge=28, _id=女}
		aggregationResults.getMappedResults().forEach(
				n->{
					log.info("sex {} max age:{}",n.get("_id"),n.get("maxAge"));
				}
		);
	}
	
	
	/**
	 根据性别分组，查询最小的 年龄信息
	 
	 select sex,min(age) as maxAge from user group by sex
	 
	 */
	@Test
	public void groupSexMinAgeTest(){
		//1. 定义聚合对象
		AggregationOperation aggregationOperation = Aggregation.group("sex").min("age").as("minAge");
		
		//2. 构建填充到聚合对象里面
		
		Aggregation aggregation = Aggregation.newAggregation(aggregationOperation);
		
		//3. 查询构建聚合对象
		
		AggregationResults<HashMap> aggregationResults = mongoTemplate.aggregate(aggregation, SIMPLE_COLLECATION_NAME, HashMap.class);
		
		// 4. 对结果进行遍历
		// n 对应是: {minAge=24, _id=女}
		aggregationResults.getMappedResults().forEach(
				n->{
					log.info("sex {} min age:{}",n.get("_id"),n.get("minAge"));
				}
		);
	}
	
	/**
	 根据性别分组，查询年龄和信息
	 
	 select sex,sum(age) as maxAge from user group by sex
	 
	 */
	@Test
	public void groupSexSumAgeTest(){
		//1. 定义聚合对象
		AggregationOperation aggregationOperation = Aggregation.group("sex").sum("age").as("sumAge");
		
		//2. 构建填充到聚合对象里面
		
		Aggregation aggregation = Aggregation.newAggregation(aggregationOperation);
		
		//3. 查询构建聚合对象
		
		AggregationResults<HashMap> aggregationResults = mongoTemplate.aggregate(aggregation, SIMPLE_COLLECATION_NAME, HashMap.class);
		
		// 4. 对结果进行遍历
		// n 对应是: {minAge=24, _id=女}
		aggregationResults.getMappedResults().forEach(
				n->{
					log.info("sex {} sum age:{}",n.get("_id"),n.get("sumAge"));
				}
		);
	}
	
	/**
	 根据性别分组，查询平均年龄
	 
	 select sex,avg(age) as maxAge from user group by sex
	 
	 */
	@Test
	public void groupSexAvgAgeTest(){
		//1. 定义聚合对象
		AggregationOperation aggregationOperation = Aggregation.group("sex").avg("age").as("avgAge");
		
		//2. 构建填充到聚合对象里面
		
		Aggregation aggregation = Aggregation.newAggregation(aggregationOperation);
		
		//3. 查询构建聚合对象
		
		AggregationResults<HashMap> aggregationResults = mongoTemplate.aggregate(aggregation, SIMPLE_COLLECATION_NAME, HashMap.class);
		
		// 4. 对结果进行遍历
		// n 对应是: {minAge=24, _id=女}
		aggregationResults.getMappedResults().forEach(
				n->{
					log.info("sex {} avg age:{}",n.get("_id"),n.get("avgAge"));
				}
		);
	}
	/**
	 先排序，排序后取每组最开始的一个，
	 也是组合分组。
	 */
	@Test
	public void sortFirstTest (){
		
		// 先排序
		SortOperation ageSort = Aggregation.sort(Sort.by(Sort.Order.desc("age")));
		
		//1. 定义聚合对象
		AggregationOperation aggregationOperation = Aggregation.group("sex").first("age").as("maxAge");
		
		//2. 构建填充到聚合对象里面
		
		Aggregation aggregation = Aggregation.newAggregation(ageSort,aggregationOperation);
		
		//3. 查询构建聚合对象
		
		AggregationResults<HashMap> aggregationResults = mongoTemplate.aggregate(aggregation, SIMPLE_COLLECATION_NAME, HashMap.class);
		
		// 4. 对结果进行遍历
		// n 对应是: {minAge=24, _id=女}
		aggregationResults.getMappedResults().forEach(
				n->{
					log.info("sex {} max age:{}",n.get("_id"),n.get("maxAge"));
				}
		);
	}
	
	
	/**
	 先排序，排序后取每组最后一个
	 也是组合分组。
	 */
	@Test
	public void sortLastTest (){
		
		// 先排序
		SortOperation ageSort = Aggregation.sort(Sort.by(Sort.Order.desc("age")));
		
		//1. 定义聚合对象
		AggregationOperation aggregationOperation = Aggregation.group("sex").last("age").as("minAge");
		
		//2. 构建填充到聚合对象里面
		
		Aggregation aggregation = Aggregation.newAggregation(ageSort,aggregationOperation);
		
		//3. 查询构建聚合对象
		
		AggregationResults<HashMap> aggregationResults = mongoTemplate.aggregate(aggregation, SIMPLE_COLLECATION_NAME, HashMap.class);
		
		// 4. 对结果进行遍历
		// n 对应是: {minAge=24, _id=女}
		aggregationResults.getMappedResults().forEach(
				n->{
					log.info("sex {} min age:{}",n.get("_id"),n.get("minAge"));
				}
		);
	}
	
	/**
	 先排序，排序后取每组最后一个
	 将属性 组装在一起 ，不进行去重.
	 */
	@Test
	public void sortPushTest (){
		
		// 先排序
		SortOperation ageSort = Aggregation.sort(Sort.by(Sort.Order.desc("age")));
		
		//1. 定义聚合对象
		AggregationOperation aggregationOperation = Aggregation.group("sex").push("description").as("desc");
		
		//2. 构建填充到聚合对象里面
		
		Aggregation aggregation = Aggregation.newAggregation(ageSort,aggregationOperation);
		
		//3. 查询构建聚合对象
		
		AggregationResults<HashMap> aggregationResults = mongoTemplate.aggregate(aggregation, SIMPLE_COLLECATION_NAME, HashMap.class);
		
		// 4. 对结果进行遍历
		// n 对应是: {minAge=24, _id=女}
		aggregationResults.getMappedResults().forEach(
				n->{
					log.info("sex {} min description:{}",n.get("_id"),n.get("desc"));
				}
		);
	}
	
	/**
	match 匹配信息
	 
	  where age >= 18 group by sex
	 */
	@Test
	public void matchTest (){
		
		// 先查询 age > = 18
		AggregationOperation matchAggregation = Aggregation.match(Criteria.where("age").gte(18));
		
		//1. 定义聚合对象
		AggregationOperation aggregationOperation = Aggregation.group("sex").max("age").as("maxAge");
		
		//2. 构建填充到聚合对象里面
		
		Aggregation aggregation = Aggregation.newAggregation(matchAggregation,aggregationOperation);
		
		//3. 查询构建聚合对象
		
		AggregationResults<HashMap> aggregationResults = mongoTemplate.aggregate(aggregation, SIMPLE_COLLECATION_NAME, HashMap.class);
		
		// 4. 对结果进行遍历
		// n 对应是: {minAge=24, _id=女}
		aggregationResults.getMappedResults().forEach(
				n->{
					log.info("sex {} max age:{}",n.get("_id"),n.get("maxAge"));
				}
		);
	}
	
	
	/**
	  先分组， 再按照分组后的结果进行排序。
	 */
	@Test
	public void groupSortTest (){
		
		// 先排序

		
		//1. 定义聚合对象
		AggregationOperation aggregationOperation = Aggregation.group("sex")
				.max("age").as("maxAge")
				.count().as("ageCount");
		
		AggregationOperation ageSort = Aggregation.sort(Sort.by(Sort.Order.desc("maxAge")));
		
		//2. 构建填充到聚合对象里面
		
		Aggregation aggregation = Aggregation.newAggregation(ageSort,aggregationOperation);
		
		//3. 查询构建聚合对象
		
		AggregationResults<HashMap> aggregationResults = mongoTemplate.aggregate(aggregation, SIMPLE_COLLECATION_NAME, HashMap.class);
		
		// 4. 对结果进行遍历
		// n 对应是: {ageCount=5, maxAge=30, _id=男}
		aggregationResults.getMappedResults().forEach(
				n->{
					log.info("sex {}  max age:{}, count {}",n.get("_id"),n.get("maxAge"),n.get("ageCount"));
				}
		);
	}
	
	
	
	/**
	 查询后分页处理
	 */
	@Test
	public void groupPageTest (){
		//1. 定义聚合对象
		AggregationOperation aggregationOperation = Aggregation.group("sex")
				.max("age").as("maxAge")
				.min("age").as("minAge")
				.sum("age").as("sumAge")
				.avg("age").as("avgAge");
		
		SkipOperation page = new SkipOperation(1);
		LimitOperation limitOperation = new LimitOperation(2);
		
		//2. 构建填充到聚合对象里面
		
		Aggregation aggregation = Aggregation.newAggregation(aggregationOperation,page,limitOperation);
		
		//3. 查询构建聚合对象
		
		AggregationResults<HashMap> aggregationResults = mongoTemplate.aggregate(aggregation, SIMPLE_COLLECATION_NAME, HashMap.class);
		
		// 4. 对结果进行遍历
		// n 对应是: {ageCount=5, maxAge=30, _id=男}
		aggregationResults.getMappedResults().forEach(
				n->{
					log.info("sex {}  max age:{}, min age {}, sum age {}, avg age {}",
							n.get("_id"),n.get("maxAge"),n.get("minAge"),n.get("sumAge"),n.get("avgAge"));
				}
		);
	}
	
	
	/**
	 查询， project 只显示某些字段 select maxAge,avgAge
	 */
	@Test
	public void groupProjectTest (){
		//1. 定义聚合对象
		AggregationOperation aggregationOperation = Aggregation.group("sex")
				.max("age").as("maxAge")
				.min("age").as("minAge")
				.sum("age").as("sumAge")
				.avg("age").as("avgAge");
		
		ProjectionOperation projectionOperation = Aggregation.project("maxAge","avgAge");
		
		//2. 构建填充到聚合对象里面
		
		Aggregation aggregation = Aggregation.newAggregation(aggregationOperation,projectionOperation);
		
		//3. 查询构建聚合对象
		
		AggregationResults<HashMap> aggregationResults = mongoTemplate.aggregate(aggregation, SIMPLE_COLLECATION_NAME, HashMap.class);
		
		// 4. 对结果进行遍历
		// n 对应是: {ageCount=5, maxAge=30, _id=男}
		aggregationResults.getMappedResults().forEach(
				n->{
					log.info("sex {}  max age:{}, min age {}, sum age {}, avg age {}",
							n.get("_id"),n.get("maxAge"),n.get("minAge"),n.get("sumAge"),n.get("avgAge"));
				}
		);
	}
	
	
	
	/**
	 查询， project 只显示某些字段 select maxAge,avgAge
	 */
	@Test
	public void groupProjectUnwindTest (){
		//1. 定义聚合对象
		AggregationOperation aggregationOperation = Aggregation.group("sex")
				.max("age").as("maxAge")
				.min("age").as("minAge")
				.sum("age").as("sumAge")
				.avg("age").as("avgAge");
		
		ProjectionOperation projectionOperation = Aggregation.project("maxAge","avgAge","sumAge");
		
		UnwindOperation avgAgeOperation = Aggregation.unwind("avgAge");
		
		//2. 构建填充到聚合对象里面
		
		Aggregation aggregation = Aggregation.newAggregation(aggregationOperation,projectionOperation,avgAgeOperation);
		
		//3. 查询构建聚合对象
		
		AggregationResults<HashMap> aggregationResults = mongoTemplate.aggregate(aggregation, SIMPLE_COLLECATION_NAME, HashMap.class);
		
		// 4. 对结果进行遍历
		// n 对应是: {sumAge=52, maxAge=28, avgAge=26.0, _id=女}
		aggregationResults.getMappedResults().forEach(
				n->{
					log.info("sex {}  max age:{}, min age {}, sum age {}, avg age {}",
							n.get("_id"),n.get("maxAge"),n.get("minAge"),n.get("sumAge"),n.get("avgAge"));
				}
		);
	}
}
