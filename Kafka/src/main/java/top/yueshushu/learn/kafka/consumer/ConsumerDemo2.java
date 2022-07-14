package top.yueshushu.learn.kafka.consumer;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.*;

import lombok.extern.slf4j.Slf4j;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-07-13
 */
@SpringBootTest
@Slf4j
public class ConsumerDemo2 {
	/**
	 * 分区策略处理
	 */
	@Test
	public void strategyTest() {
		//1. 进行配置
		Properties properties = new Properties();
		properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
		properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "group4");
		
		//配置策略 默认是 Range
		// 随机策略
		properties.setProperty(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, RoundRobinAssignor.class.getName());
		//粘性
		properties.setProperty(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, StickyAssignor.class.getName());
		
		
		//2. 构建消费者
		KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer(properties);
		
		List<String> topicsList = new ArrayList<>();
		topicsList.add("first");
		kafkaConsumer.subscribe(topicsList);
		
		//3. 循环获取数据
		while (true) {
			ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(Duration.ofSeconds(1));
			if (consumerRecords.isEmpty()) {
				log.info("no data");
				continue;
			}
			consumerRecords.forEach(
					n -> {
						log.info("value :{}", n.value());
					}
			);
		}
		
	}
	
	
	/**
	 * 自动提交
	 */
	@Test
	public void autoCommitTest() {
		//1. 进行配置
		Properties properties = new Properties();
		properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
		properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "group5");
		
		//配置自动提交
		//是否开启自动提交
		properties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
		// 自动提交的时间间隔
		properties.setProperty(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
		
		//2. 构建消费者
		KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer(properties);
		
		List<String> topicsList = new ArrayList<>();
		topicsList.add("first");
		kafkaConsumer.subscribe(topicsList);
		
		//3. 循环获取数据
		while (true) {
			ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(Duration.ofSeconds(1));
			if (consumerRecords.isEmpty()) {
				log.info("no data");
				continue;
			}
			consumerRecords.forEach(
					n -> {
						log.info("value :{}", n.value());
					}
			);
		}
		
	}
	
	
	/**
	 * 手动提交
	 */
	@Test
	public void handlerCommitTest() {
		//1. 进行配置
		Properties properties = new Properties();
		properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
		properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "group5");
		
		//配置自动提交
		//是否开启自动提交
		properties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
		
		
		//2. 构建消费者
		KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer(properties);
		
		List<String> topicsList = new ArrayList<>();
		topicsList.add("first");
		kafkaConsumer.subscribe(topicsList);
		
		//3. 循环获取数据
		while (true) {
			ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(Duration.ofSeconds(1));
			if (consumerRecords.isEmpty()) {
				log.info("no data");
				continue;
			}
			consumerRecords.forEach(
					n -> {
						log.info("value :{}", n.value());
					}
			);
			//手动提交，是同步。
			kafkaConsumer.commitSync();
			
			//手动提交，是异步提交。
			kafkaConsumer.commitAsync();
		}
	}
	
	
	/**
	 * seek, 寻找offset 进行提交， 根据位置.
	 */
	@Test
	public void seekTest() {
		//1. 进行配置
		Properties properties = new Properties();
		properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
		properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "group5");
		
		//配置自动提交
		//是否开启自动提交
		properties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
		
		
		//2. 构建消费者
		KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer(properties);
		
		List<String> topicsList = new ArrayList<>();
		topicsList.add("first");
		kafkaConsumer.subscribe(topicsList);
		
		//指定位置
		Set<TopicPartition> topicPartitionSet = new HashSet<>();
		while (topicPartitionSet.isEmpty()) {
			kafkaConsumer.poll(Duration.ofSeconds(1));
			
			topicPartitionSet = kafkaConsumer.assignment();
		}
		
		//对每一个分区，都指定相同的位置.
		for (TopicPartition topicPartition : topicPartitionSet) {
			//设置位置
			kafkaConsumer.seek(topicPartition, 100);
		}
		
		
		//3. 循环获取数据
		while (true) {
			ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(Duration.ofSeconds(1));
			if (consumerRecords.isEmpty()) {
				log.info("no data");
				continue;
			}
			consumerRecords.forEach(
					n -> {
						log.info("value :{}", n.value());
					}
			);
			//手动提交，是同步。
			kafkaConsumer.commitSync();
			
			//手动提交，是异步提交。
			kafkaConsumer.commitAsync();
		}
	}
	
	
	/**
	 * seek, 寻找offset 进行提交， 根据时间
	 */
	@Test
	public void seekTimeTest() {
		//1. 进行配置
		Properties properties = new Properties();
		properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
		properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "group5");
		
		//配置自动提交
		//是否开启自动提交
		properties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
		
		
		//2. 构建消费者
		KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer(properties);
		
		List<String> topicsList = new ArrayList<>();
		topicsList.add("first");
		kafkaConsumer.subscribe(topicsList);
		
		//指定位置
		Set<TopicPartition> topicPartitionSet = new HashSet<>();
		while (topicPartitionSet.isEmpty()) {
			kafkaConsumer.poll(Duration.ofSeconds(1));
			
			topicPartitionSet = kafkaConsumer.assignment();
		}
		
		//对每一个分区，都指定相同的位置.
		HashMap<TopicPartition, Long> timestampToSearch = new HashMap<>();
		// 封装集合存储，每个分区对应一天前的数据
		for (TopicPartition topicPartition : topicPartitionSet) {
			timestampToSearch.put(topicPartition, System.currentTimeMillis() - 1 * 24 * 3600 * 1000);
		} // 获取从 1 天前开始消费的每个分区的 offset
		Map<TopicPartition, OffsetAndTimestamp> offsets =
				kafkaConsumer.offsetsForTimes(timestampToSearch);
		// 遍历每个分区，对每个分区设置消费时间。
		for (TopicPartition topicPartition : topicPartitionSet) {
			OffsetAndTimestamp offsetAndTimestamp = offsets.get(topicPartition);
			// 根据时间指定开始消费的位置
			if (offsetAndTimestamp != null) {
				kafkaConsumer.seek(topicPartition,
						offsetAndTimestamp.offset());
			}
		}
		
		//3. 循环获取数据
		while (true) {
			ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(Duration.ofSeconds(1));
			if (consumerRecords.isEmpty()) {
				log.info("no data");
				continue;
			}
			consumerRecords.forEach(
					n -> {
						log.info("value :{}", n.value());
					}
			);
			//手动提交，是同步。
			kafkaConsumer.commitSync();
			
			//手动提交，是异步提交。
			kafkaConsumer.commitAsync();
		}
	}
}


