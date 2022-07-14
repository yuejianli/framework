package top.yueshushu.learn.kafka.producer;


import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

/**
 * 生产者处理
 *
 * @author yuejianli
 * @date 2022-06-27
 */
@SpringBootTest
@Slf4j
public class ProducerDemo {
	
	public static final int MAX_MESSAGE_COUNT = 5;
	
	/**
	 * 一个简单的程序，发送消息到 Kafka 主题
	 * 没有回调 ， 指定 topic 和发送的内容
	 */
	@Test
	public void noCallBackTest() throws Exception {
		//. 1. 配置属性信息
		Properties properties = new Properties();
		// 指定服务器
		properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
		
		//2 . 指定序列化方式
		// 指定 key 序列化方式
		properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		// 指定 value 序列化方式
		properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		
		//3. 构建生产者对象
		KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(properties);
		
		//4. 发送消息
		for (int i = 0; i < MAX_MESSAGE_COUNT; i++) {
			ProducerRecord producerRecord = new ProducerRecord("first", "kafka send text :" + i);
			kafkaProducer.send(producerRecord);
		}
		log.info(">>> 发送信息成功 :{}", kafkaProducer);
		//5. 关闭
		kafkaProducer.close();
		
	}
	
	/**
	 * 具有回调的信息，发送消息
	 */
	@Test
	public void callbackTest() throws Exception {
		//1. 指定属性
		Properties properties = new Properties();
		properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
		
		//2. 指定 key 和 value 序列化方式
		properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		
		//3. 构建 KafkaProducer 对象
		KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(properties);
		
		//4. 发送信息
		for (int i = 0; i <= MAX_MESSAGE_COUNT; i++) {
			kafkaProducer.send(new ProducerRecord<>("first", "send kafka text:" + i + ",with callback"), (recordMetadata, e) -> {
				if (e == null) {
					log.info("发送 到主题 {}, 分区 {} 成功",
							recordMetadata.topic(),
							recordMetadata.partition());
				} else {
					log.error("发送数据失败", e);
				}
			});
			
			TimeUnit.MILLISECONDS.sleep(50);
		}
		
		//5. 关闭
		kafkaProducer.close();
	}
	
	/**
	 * 同步发送消息到 kafka
	 * .get() 方法
	 */
	@Test
	public void synchronousSendMessageTest() throws Exception {
		//. 1. 构建属性
		Properties properties = new Properties();
		properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
		
		//2. 配置 Key 和 value 序列化方式
		properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		
		//3. 配置 KafkaProducer 对象
		KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(properties);
		
		//4. 发送数据
		for (int i = 0; i < MAX_MESSAGE_COUNT; i++) {
			// .get() 表示 同步发送
			kafkaProducer.send(new ProducerRecord("first", "发送数据到 kafka" + i), (recordMetadata, e) -> {
				if (e == null) {
					log.info(">>> 发送数据到 主题 {} 成功", recordMetadata.topic());
				} else {
					log.error("发送数据 失败", e);
				}
			}).get();
			
			TimeUnit.MILLISECONDS.sleep(50);
		}
		
		// 5. 关闭
		kafkaProducer.close();
	}
	
}
