package top.yueshushu.simpleuse;

import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.FuzzyQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-08-11
 */
@SpringBootTest
@Slf4j
public class ComplexSearchTest {
	
	private RestHighLevelClient restHighLevelClient;
	
	@Before
	public void initClient() {
		RestClientBuilder restClientBuilder = RestClient.builder(new HttpHost("127.0.0.1", 9200, "http"));
		restHighLevelClient = new RestHighLevelClient(restClientBuilder);
	}
	
	/**
	 * 全部查询
	 * <p>
	 * findAll
	 */
	@Test
	public void findAllTest() throws Exception {
		SearchRequest searchRequest = new SearchRequest();
		// 设置索引
		searchRequest.indices("user");
		
		/**
		 构建条件
		 */
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchAllQuery());
		searchRequest.source(searchSourceBuilder);
		
		// 进行请求
		SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		
		SearchHits result = searchResponse.getHits();
		log.info(">>> 花费的时间:{}", searchResponse.getTook());
		log.info(">>>是否超时:{}", searchResponse.isTimedOut());
		log.info(">>>> 总的数量:{}", result.getTotalHits());
		log.info(">>>>最大的匹配分数值:{}", result.getMaxScore());
		log.info(">>>>查询结果输出开始");
		Arrays.stream(result.getHits()).forEach(
				n -> log.info(">>>获取内容:{}", n.getSourceAsString())
		);
		log.info(">>>> 查询结果输出结束");
	}
	
	/**
	 * 单条件查询, 类似于  age =
	 * term 为  =
	 */
	@Test
	public void getByAgeTest() throws Exception {
		SearchRequest searchRequest = new SearchRequest();
		searchRequest.indices("user");
		
		// 构建条件
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.termQuery("age", 28));
		
		searchRequest.source(searchSourceBuilder);
		
		// 进行查询
		SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		// 查询查询的内容
		SearchHits result = searchResponse.getHits();
		
		log.info(">>>> 花费的时间:{}", searchResponse.getTook());
		log.info(">>> 是否超时:{}", searchResponse.isTimedOut());
		log.info(">>>>总的数量:{}", result.getTotalHits());
		log.info(">>>>最大匹配值:{}", result.getMaxScore());
		log.info(">>>>查询结果输出开始");
		
		Arrays.stream(result.getHits()).forEach(
				n -> {
					log.info(">>>> 内容:{}", n.getSourceAsString());
					log.info(">>>> 内容:{}", n.getSourceAsMap());
				}
		);
		
		log.info(">>>>查询结果输出结束");
	}
	
	
	/**
	 * 分页处理
	 * <p>
	 * 分页,  limit startIndex, length
	 */
	@Test
	public void pageTest() throws Exception {
		SearchRequest searchRequest = new SearchRequest();
		// 设置索引
		searchRequest.indices("user");
		
		/**
		 构建条件
		 */
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchAllQuery());
		
		// 进行分页
		searchSourceBuilder.from((2 - 1) * 2);
		searchSourceBuilder.size(2);
		
		
		searchRequest.source(searchSourceBuilder);
		
		// 进行请求
		SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		
		SearchHits result = searchResponse.getHits();
		log.info(">>> 花费的时间:{}", searchResponse.getTook());
		log.info(">>>是否超时:{}", searchResponse.isTimedOut());
		log.info(">>>> 总的数量:{}", result.getTotalHits());
		log.info(">>>>最大的匹配分数值:{}", result.getMaxScore());
		log.info(">>>>查询结果输出开始");
		Arrays.stream(result.getHits()).forEach(
				n -> {
					log.info(">>>获取内容:{}", n.getSourceAsString());
				}
		);
		log.info(">>>> 查询结果输出结束");
	}
	
	/**
	 * 排序处理
	 * <p>
	 * 排序,  order by ...
	 */
	@Test
	public void orderByTest() throws Exception {
		SearchRequest searchRequest = new SearchRequest();
		// 设置索引
		searchRequest.indices("user");
		
		/**
		 构建条件
		 */
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchAllQuery());
		
		// 按照年龄降序
		searchSourceBuilder.sort("age", SortOrder.DESC);
		// 进行分页
//		searchSourceBuilder.from((2-1)*2);
//		searchSourceBuilder.size(2);
		
		
		searchRequest.source(searchSourceBuilder);
		
		// 进行请求
		SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		
		SearchHits result = searchResponse.getHits();
		log.info(">>> 花费的时间:{}", searchResponse.getTook());
		log.info(">>>是否超时:{}", searchResponse.isTimedOut());
		log.info(">>>> 总的数量:{}", result.getTotalHits());
		log.info(">>>>最大的匹配分数值:{}", result.getMaxScore());
		log.info(">>>>查询结果输出开始");
		Arrays.stream(result.getHits()).forEach(
				n -> {
					log.info(">>>获取内容:{}", n.getSourceAsString());
				}
		);
		log.info(">>>> 查询结果输出结束");
	}
	
	/**
	 * 只展示,或者排除某些字段
	 * <p>
	 * select 字段1,字段2 from 表名
	 */
	@Test
	public void includeTest() throws Exception {
		SearchRequest searchRequest = new SearchRequest();
		// 设置索引
		searchRequest.indices("user");
		
		/**
		 构建条件
		 */
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchAllQuery());
		
		
		String[] includes = {"id", "name"};
		String[] excludes = {};
		
		searchSourceBuilder.fetchSource(includes, excludes);
		
		// 按照年龄降序
		searchSourceBuilder.sort("age", SortOrder.DESC);
		// 进行分页
//		searchSourceBuilder.from((2-1)*2);
//		searchSourceBuilder.size(2);
		
		
		searchRequest.source(searchSourceBuilder);
		
		// 进行请求
		SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		
		SearchHits result = searchResponse.getHits();
		log.info(">>> 花费的时间:{}", searchResponse.getTook());
		log.info(">>>是否超时:{}", searchResponse.isTimedOut());
		log.info(">>>> 总的数量:{}", result.getTotalHits());
		log.info(">>>>最大的匹配分数值:{}", result.getMaxScore());
		log.info(">>>>查询结果输出开始");
		Arrays.stream(result.getHits()).forEach(
				n -> {
					log.info(">>>获取内容:{}", n.getSourceAsString());
				}
		);
		log.info(">>>> 查询结果输出结束");
	}
	
	/**
	 * bool 查询
	 */
	@Test
	public void boolTest() throws Exception {
		SearchRequest searchRequest = new SearchRequest();
		// 设置索引
		searchRequest.indices("user");
		
		/**
		 构建条件
		 */
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		// 必须是
		boolQueryBuilder.must(QueryBuilders.matchQuery("age", 29));
		// 一定不能是
		boolQueryBuilder.mustNot(QueryBuilders.matchQuery("sex", "女"));
		//可能是
		boolQueryBuilder.should(QueryBuilders.matchQuery("name", "岳泽霖"));
		
		searchSourceBuilder.query(boolQueryBuilder);
		
		
		// 按照年龄降序
		searchSourceBuilder.sort("age", SortOrder.DESC);
		// 进行分页
//		searchSourceBuilder.from((2-1)*2);
//		searchSourceBuilder.size(2);
		
		
		searchRequest.source(searchSourceBuilder);
		
		// 进行请求
		SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		
		SearchHits result = searchResponse.getHits();
		log.info(">>> 花费的时间:{}", searchResponse.getTook());
		log.info(">>>是否超时:{}", searchResponse.isTimedOut());
		log.info(">>>> 总的数量:{}", result.getTotalHits());
		log.info(">>>>最大的匹配分数值:{}", result.getMaxScore());
		log.info(">>>>查询结果输出开始");
		Arrays.stream(result.getHits()).forEach(
				n -> {
					log.info(">>>获取内容:{}", n.getSourceAsString());
				}
		);
		log.info(">>>> 查询结果输出结束");
	}
	
	
	/**
	 * range 范围查询,  between and
	 */
	@Test
	public void rangeTest() throws Exception {
		SearchRequest searchRequest = new SearchRequest();
		// 设置索引
		searchRequest.indices("user");
		
		/**
		 构建条件
		 */
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		
		RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("age");
		
		rangeQueryBuilder.gte(24);
		rangeQueryBuilder.lte(30);
		
		searchSourceBuilder.query(rangeQueryBuilder);
		
		
		// 按照年龄降序
		searchSourceBuilder.sort("age", SortOrder.DESC);
		// 进行分页
//		searchSourceBuilder.from((2-1)*2);
//		searchSourceBuilder.size(2);
		
		
		searchRequest.source(searchSourceBuilder);
		
		// 进行请求
		SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		
		SearchHits result = searchResponse.getHits();
		log.info(">>> 花费的时间:{}", searchResponse.getTook());
		log.info(">>>是否超时:{}", searchResponse.isTimedOut());
		log.info(">>>> 总的数量:{}", result.getTotalHits());
		log.info(">>>>最大的匹配分数值:{}", result.getMaxScore());
		log.info(">>>>查询结果输出开始");
		Arrays.stream(result.getHits()).forEach(
				n -> {
					log.info(">>>获取内容:{}", n.getSourceAsString());
				}
		);
		log.info(">>>> 查询结果输出结束");
	}
	
	/**
	 * like 模糊查询
	 */
	@Test
	public void likeTest() throws Exception {
		SearchRequest searchRequest = new SearchRequest();
		// 设置索引
		searchRequest.indices("user");
		
		/**
		 构建条件
		 */
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		
		// 模糊几个 ,一个.
		FuzzyQueryBuilder fuzzyQueryBuilder = QueryBuilders.fuzzyQuery("name", "岳泽");
		
		fuzzyQueryBuilder.fuzziness(Fuzziness.ONE);
		
		searchSourceBuilder.query(fuzzyQueryBuilder);
		
		
		// 按照年龄降序
		searchSourceBuilder.sort("age", SortOrder.DESC);
		// 进行分页
//		searchSourceBuilder.from((2-1)*2);
//		searchSourceBuilder.size(2);
		
		
		searchRequest.source(searchSourceBuilder);
		
		// 进行请求
		SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		
		SearchHits result = searchResponse.getHits();
		log.info(">>> 花费的时间:{}", searchResponse.getTook());
		log.info(">>>是否超时:{}", searchResponse.isTimedOut());
		log.info(">>>> 总的数量:{}", result.getTotalHits());
		log.info(">>>>最大的匹配分数值:{}", result.getMaxScore());
		log.info(">>>>查询结果输出开始");
		Arrays.stream(result.getHits()).forEach(
				n -> {
					log.info(">>>获取内容:{}", n.getSourceAsString());
				}
		);
		log.info(">>>> 查询结果输出结束");
	}
	
	
	/**
	 * hight 高亮展示
	 */
	@Test
	public void hightTest() throws Exception {
		SearchRequest searchRequest = new SearchRequest();
		// 设置索引
		searchRequest.indices("user");
		
		/**
		 构建条件
		 */
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		
		// 模糊几个 ,一个.
		searchSourceBuilder.query(QueryBuilders.termQuery("age", 29));
		
		
		// 按照年龄降序
		searchSourceBuilder.sort("age", SortOrder.DESC);
		// 进行分页
//		searchSourceBuilder.from((2-1)*2);
//		searchSourceBuilder.size(2);
		
		
		// 构建  hight
		HighlightBuilder highlightBuilder = new HighlightBuilder();
		// 前缀
		highlightBuilder.preTags("<font color='red'>");
		//后缀
		highlightBuilder.postTags("</font>");
		//高亮字段
		highlightBuilder.field("name");
		
		searchSourceBuilder.highlighter(highlightBuilder);
		
		searchRequest.source(searchSourceBuilder);
		
		// 进行请求
		SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		
		SearchHits result = searchResponse.getHits();
		log.info(">>> 花费的时间:{}", searchResponse.getTook());
		log.info(">>>是否超时:{}", searchResponse.isTimedOut());
		log.info(">>>> 总的数量:{}", result.getTotalHits());
		log.info(">>>>最大的匹配分数值:{}", result.getMaxScore());
		log.info(">>>>查询结果输出开始");
		Arrays.stream(result.getHits()).forEach(
				n -> {
					log.info(">>>获取内容:{}", n.getSourceAsString());
					Map<String, HighlightField> highlightFields = n.getHighlightFields();
					
					highlightFields.entrySet().forEach(
							hn -> {
								System.out.println(hn.getKey() + "," + hn.getValue().getName());
							}
					);
				}
		);
		log.info(">>>> 查询结果输出结束");
		log.info(searchResponse.toString());
	}
	
	
	/**
	 * 聚全处理
	 * {"took":4,"timed_out":false,"_shards":{"total":1,"successful":1,"skipped":0,"failed":0},
	 * "hits":{"total":{"value":6,"relation":"eq"},"max_score":null,"hits":[{"_index":"user","_type":"_doc","_id":"3","_score":null,
	 * "_source":{"id":3,"name":"岳泽霖","nickName":"两个蝴蝶飞,哈哈","age":29,"sex":"男","description":"一个快乐的程序员"},"sort":[29]},
	 * {"_index":"user","_type":"_doc","_id":"4","_score":null,"_source":{"id":4,"name":"岳叔叔","nickName":"两个蝴蝶飞,哈哈","age":29,"sex":"女","description":"一个快乐的程序员呀呀"},
	 * "sort":[29]},{"_index":"user","_type":"_doc","_id":"1","_score":null,"_source":{"id":1,"name":"岳建立","nickName":"两个蝴蝶飞","age":28,"sex":"男","description":"一个快乐的程序员"},
	 * "sort":[28]},{"_index":"user","_type":"_doc","_id":"2","_score":null,"_source":{"id":2,"name":"岳建立","nickName":"两个蝴蝶飞","age":28,"sex":"男","description":"一个快乐的程序员"},"sort":[28]},
	 * {"_index":"user","_type":"_doc","_id":"6","_score":null,"_source":{"id":6,"name":"周小欢","nickName":"周小欢","age":24,"sex":"女","description":"一个快乐的程序员呀呀"},"sort":[24]},
	 * {"_index":"user","_type":"_doc","_id":"5","_score":null,"_source":{"id":5,"name":"周欢","nickName":"两个蝴蝶飞,哈哈","age":22,"sex":"女","description":"一个快乐的程序员呀呀"},"sort":[22]}]},
	 * "aggregations":{"sum#sumAge":{"value":160.0},"max#maxAge":{"value":29.0},"min#minAge":{"value":22.0},"avg#avgAge":{"value":26.666666666666668}}}
	 */
	@Test
	public void aggreTest() throws Exception {
		SearchRequest searchRequest = new SearchRequest();
		// 设置索引
		searchRequest.indices("user");
		
		/**
		 构建条件
		 */
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		
		
		searchSourceBuilder.query(QueryBuilders.matchAllQuery());
		
		searchSourceBuilder.aggregation(AggregationBuilders.max("maxAge").field("age"));
		searchSourceBuilder.aggregation(AggregationBuilders.min("minAge").field("age"));
		searchSourceBuilder.aggregation(AggregationBuilders.sum("sumAge").field("age"));
		searchSourceBuilder.aggregation(AggregationBuilders.avg("avgAge").field("age"));
		
		// 按照年龄降序
		searchSourceBuilder.sort("age", SortOrder.DESC);
		// 进行分页
//		searchSourceBuilder.from((2-1)*2);
//		searchSourceBuilder.size(2);
		
		
		searchRequest.source(searchSourceBuilder);
		
		// 进行请求
		SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		
		SearchHits result = searchResponse.getHits();
		log.info(">>> 花费的时间:{}", searchResponse.getTook());
		log.info(">>>是否超时:{}", searchResponse.isTimedOut());
		log.info(">>>> 总的数量:{}", result.getTotalHits());
		log.info(">>>>最大的匹配分数值:{}", result.getMaxScore());
		log.info(">>>>查询结果输出开始");
		Arrays.stream(result.getHits()).forEach(
				n -> {
					log.info(">>>获取内容:{}", n.getSourceAsString());
				}
		);
		log.info(">>>> 查询结果输出结束");
		
		log.info(searchResponse.toString());
	}
	
	/**
	 * 分组统计   select age,count(1) from user group by age
	 * <p>
	 * "aggregations":{"lterms#age_groupby":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"key":28,"doc_count":2},{"key":29,"doc_count":2},{"key":22,"doc_count":1},{"key":24,"doc_count":1}]}}}
	 */
	@Test
	public void groupTest() throws Exception {
		SearchRequest searchRequest = new SearchRequest();
		// 设置索引
		searchRequest.indices("user");
		
		/**
		 构建条件
		 */
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		
		
		searchSourceBuilder.query(QueryBuilders.matchAllQuery());
		
		searchSourceBuilder.aggregation(AggregationBuilders.terms("age_groupby").field("age"));
		
		// 按照年龄降序
		searchSourceBuilder.sort("age", SortOrder.DESC);
		// 进行分页
//		searchSourceBuilder.from((2-1)*2);
//		searchSourceBuilder.size(2);
		
		
		searchRequest.source(searchSourceBuilder);
		
		// 进行请求
		SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		
		SearchHits result = searchResponse.getHits();
		log.info(">>> 花费的时间:{}", searchResponse.getTook());
		log.info(">>>是否超时:{}", searchResponse.isTimedOut());
		log.info(">>>> 总的数量:{}", result.getTotalHits());
		log.info(">>>>最大的匹配分数值:{}", result.getMaxScore());
		log.info(">>>>查询结果输出开始");
		Arrays.stream(result.getHits()).forEach(
				n -> {
					log.info(">>>获取内容:{}", n.getSourceAsString());
				}
		);
		log.info(">>>> 查询结果输出结束");
		
		log.info(searchResponse.toString());
	}
}
