package top.yueshushu.learn.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-07-13
 */
@SpringBootTest
@Slf4j
public class ConsumerDemo {
	
	/**
	 * 获取每个主题的数据
	 */
	@Test
	public void getRecordTest() throws Exception {
		//1. 定义消费者配置， 要添加 服务器，反序列化 和组名
		Properties properties = new Properties();
		properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
		properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		// 添加组
		properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "group1");
		
		//2. 构建消费者
		KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer(properties);
		
		//3. 订阅主题，是个集合
		List<String> topicsList = new ArrayList<>();
		topicsList.add("first");
		kafkaConsumer.subscribe(topicsList);
		
		//4. 一直循环，获取消息
		while (true) {
			// 每隔 多长时间获取数据
			ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(Duration.ofSeconds(1));
			if (consumerRecords.isEmpty()) {
				log.info(">>>> no data producer");
				continue;
			}
			
			/*
			
			ConsumerRecord(topic = first, partition = 0, leaderEpoch = 0, offset = 26, CreateTime = 1657697266432,
			serialized key size = -1, serialized value size = 31,
			headers = RecordHeaders(headers = [], isReadOnly = false), key = null,
			value = send kafka text:5,with callback)
			
			 */
			
			consumerRecords.forEach(
					n -> {
						log.info("show data: {},{},{}", n.key(), n.value(), n);
					}
			);
		}
	}
	
	/**
	 * 获取某个主题，单独某个分区的数据。
	 */
	@Test
	public void getPartitionTest() throws Exception {
		//1. 配置属性
		Properties properties = new Properties();
		properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
		properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		//设置组
		properties.put(ConsumerConfig.GROUP_ID_CONFIG, "group1");
		//2. 构建消费者
		KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer(properties);
		
		//3. 订阅某个主题的分区
		List<TopicPartition> topicPartitionList = new ArrayList<>();
		//topicPartitionList.add(new TopicPartition("first",0));
		// 改成1 ，获取第二个分区，就获取不到了。
		topicPartitionList.add(new TopicPartition("first", 1));
		kafkaConsumer.assign(topicPartitionList);
		
		//4. 循环获取数据
		while (true) {
			ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(Duration.ofSeconds(1));
			if (consumerRecords.isEmpty()) {
				log.info(">>> no data producer");
				continue;
			}
			//对数据进行处理
			consumerRecords.forEach(n -> {
				log.info("value:{},object:{}", n.value(), n);
			});
		}
	}
	
	
	/**
	 * 获取某个主题，单独某个分区的数据。
	 */
	@Test
	public void getDiffGroup() throws Exception {
		//1. 配置属性
		Properties properties = new Properties();
		properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
		properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		//设置组, 为第二个组。
		properties.put(ConsumerConfig.GROUP_ID_CONFIG, "group2");
		//2. 构建消费者
		KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer(properties);
		
		//3. 订阅某个主题的分区
		List<TopicPartition> topicPartitionList = new ArrayList<>();
		topicPartitionList.add(new TopicPartition("first", 0));
		kafkaConsumer.assign(topicPartitionList);
		
		//4. 循环获取数据
		while (true) {
			ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(Duration.ofSeconds(1));
			if (consumerRecords.isEmpty()) {
				log.info(">>> no data producer");
				continue;
			}
			//对数据进行处理
			consumerRecords.forEach(n -> {
				log.info("value:{},object:{}", n.value(), n);
			});
		}
	}
	
	
	/**
	 * 相同组，多个消费者对主题的消费。
	 * <p>
	 * <p>
	 * 一个组内有两个消费者的话， 订阅的是主题的话， 则只有一个消费者可以获取到数据。
	 * <p>
	 * 如果订阅的是 主题+分区的话，则两个消费者都可以获取到数据。
	 */
	@Test
	public void groupConsumer1() throws Exception {
		//1. 配置属性
		Properties properties = new Properties();
		properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
		properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		//设置组, 为第二个组。
		properties.put(ConsumerConfig.GROUP_ID_CONFIG, "group3");
		//2. 构建消费者
		KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer(properties);
		
		//3. 订阅某个主题的分区
		List<String> topicsList = new ArrayList<>();
		topicsList.add("first");
		kafkaConsumer.subscribe(topicsList);
		
		//4. 循环获取数据
		while (true) {
			ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(Duration.ofSeconds(1));
			if (consumerRecords.isEmpty()) {
				log.info(">>> consumer1 no data producer");
				continue;
			}
			//对数据进行处理
			consumerRecords.forEach(n -> {
				log.info("consumer1 value:{},object:{}", n.value(), n);
			});
		}
	}
	
	
	/**
	 * 相同组，多个消费者对主题的消费。
	 */
	@Test
	public void groupConsumer2() throws Exception {
		//1. 配置属性
		Properties properties = new Properties();
		properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
		properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		//设置组, 为第二个组。
		properties.put(ConsumerConfig.GROUP_ID_CONFIG, "group3");
		//2. 构建消费者
		KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer(properties);
		
		//3. 订阅某个主题的分区
		List<String> topicsList = new ArrayList<>();
		topicsList.add("first");
		kafkaConsumer.subscribe(topicsList);
		
		//4. 循环获取数据
		while (true) {
			ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(Duration.ofSeconds(1));
			if (consumerRecords.isEmpty()) {
				log.info(">>>consumer2 no data producer");
				continue;
			}
			//对数据进行处理
			consumerRecords.forEach(n -> {
				log.info("consumer2 value:{},object:{}", n.value(), n);
			});
		}
	}
}
