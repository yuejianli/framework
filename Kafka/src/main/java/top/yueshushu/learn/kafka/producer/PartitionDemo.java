package top.yueshushu.learn.kafka.producer;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

/**
 * 分区配置  修改成3个分区
 * kafka-topics.bat --bootstrap-server localhost:9092 --alter --topic first --partitions 3
 * <p>
 * 分区，从 0 开始。
 *
 * @author yuejianli
 * @date 2022-06-27
 */
@SpringBootTest
@Slf4j
public class PartitionDemo {
	/**
	 * 手动指定分区
	 */
	@Test
	public void setPartitionTest() throws Exception {
		
		//1. 指定属性
		Properties properties = new Properties();
		properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
		
		//2. 指定 key 和 value 序列化方式
		properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		
		//3. 构建 KafkaProducer 对象
		KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(properties);
		
		//4. 发送信息
		for (int i = 0; i <= 5; i++) {
			// 指定 第一个分区， 没有 key
			kafkaProducer.send(new ProducerRecord<>("first", 1, "", "send kafka text:" + i + ",with callback"), new Callback() {
				@Override
				public void onCompletion(RecordMetadata recordMetadata, Exception e) {
					if (e == null) {
						log.info("发送 到主题 {}, 分区 {} 成功",
								recordMetadata.topic(),
								recordMetadata.partition());
					} else {
						log.error("发送数据失败", e);
					}
				}
			});
			
			TimeUnit.MILLISECONDS.sleep(50);
		}
		
		//5. 关闭
		kafkaProducer.close();
	}
	
	/**
	 * 使用 key 来处理分区
	 */
	@Test
	public void keyPartitionTest() throws Exception {
		
		//1. 指定属性
		Properties properties = new Properties();
		properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
		
		//2. 指定 key 和 value 序列化方式
		properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		
		//3. 构建 KafkaProducer 对象
		KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(properties);
		
		//4. 发送信息
		char startKey = 'A';
		int offset = 0;
		for (int i = 0; i <= 5; i++) {
			// 指定 第一个分区， 没有 key
			kafkaProducer.send(new ProducerRecord<>("first", null, String.valueOf(startKey + (++offset)), "send kafka text:" + i + ",with callback"), new Callback() {
				@Override
				public void onCompletion(RecordMetadata recordMetadata, Exception e) {
					if (e == null) {
						log.info("发送 到主题 {}, 分区 {} 成功",
								recordMetadata.topic(),
								recordMetadata.partition());
					} else {
						log.error("发送数据失败", e);
					}
				}
			});
			
			TimeUnit.MILLISECONDS.sleep(50);
		}
		
		//5. 关闭
		kafkaProducer.close();
	}
	
	
	/**
	 * 不指定的话，使用的是粘性分区
	 */
	@Test
	public void nianPartitionTest() throws Exception {
		
		//1. 指定属性
		Properties properties = new Properties();
		properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
		
		//2. 指定 key 和 value 序列化方式
		properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		
		//3. 构建 KafkaProducer 对象
		KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(properties);
		
		//4. 发送信息
		char startKey = 'A';
		int offset = 0;
		for (int i = 0; i <= 5; i++) {
			// 指定 第一个分区， 没有 key
			kafkaProducer.send(new ProducerRecord<>("first", "send kafka text:" + i + ",with callback"), new Callback() {
				@Override
				public void onCompletion(RecordMetadata recordMetadata, Exception e) {
					if (e == null) {
						log.info("发送 到主题 {}, 分区 {} 成功",
								recordMetadata.topic(),
								recordMetadata.partition());
					} else {
						log.error("发送数据失败", e);
					}
				}
			});
			
			TimeUnit.MILLISECONDS.sleep(50);
		}
		
		//5. 关闭
		kafkaProducer.close();
	}
	
	
	/**
	 * 自定义分区策略器
	 */
	@Test
	public void selfPartitionTest() throws Exception {
		
		//1. 指定属性
		Properties properties = new Properties();
		properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
		
		//2. 指定 key 和 value 序列化方式
		properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		
		//定义分区策略器
		properties.setProperty(ProducerConfig.PARTITIONER_CLASS_CONFIG, MyPartitioner.class.getName());
		
		//3. 构建 KafkaProducer 对象
		KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(properties);
		
		//4. 发送信息
		for (int i = 0; i <= 5; i++) {
			// 指定 第一个分区， 没有 key
			String value;
			switch (i) {
				case 0: {
					value = "A1";
					break;
				}
				case 1: {
					value = "B1";
					break;
				}
				case 2: {
					value = "C1";
					break;
				}
				case 3: {
					value = "A2";
					break;
				}
				case 4: {
					value = "B2";
					break;
				}
				default: {
					value = "C2";
					break;
				}
			}
			kafkaProducer.send(new ProducerRecord<>("first", value), new Callback() {
				@Override
				public void onCompletion(RecordMetadata recordMetadata, Exception e) {
					if (e == null) {
						log.info("发送 到主题 {}, 分区 {} 成功",
								recordMetadata.topic(),
								recordMetadata.partition());
					} else {
						log.error("发送数据失败", e);
					}
				}
			});
			
			TimeUnit.MILLISECONDS.sleep(50);
		}
		
		//5. 关闭
		kafkaProducer.close();
	}
	
}
