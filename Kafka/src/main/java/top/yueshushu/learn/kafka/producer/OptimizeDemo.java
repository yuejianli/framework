package top.yueshushu.learn.kafka.producer;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Properties;

import lombok.extern.slf4j.Slf4j;

/**
 * 优化处理
 *
 * @author yuejianli
 * @date 2022-06-27
 */
@SpringBootTest
@Slf4j
public class OptimizeDemo {
	/**
	 * 优化处理
	 */
	@Test
	public void optimizeTest() {
		//1. 定义 配置
		Properties properties = new Properties();
		properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
		// 序列化
		properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		//3. 优化配置参数  原先默认是 16K
		properties.setProperty(ProducerConfig.BATCH_SIZE_CONFIG, String.valueOf(16 * 1024 * 2));
		// 原先默认是 32M
		properties.setProperty(ProducerConfig.BUFFER_MEMORY_CONFIG, String.valueOf(32 * 1024 * 1000 * 2));
		//设置拉取的最大等待时长， 默认是 0
		properties.setProperty(ProducerConfig.LINGER_MS_CONFIG, String.valueOf(5));
		// 设置压缩类型
		properties.setProperty(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
		
		//3. 定义对象
		KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(properties);
		//4. 发送消息
		for (int i = 0; i < 5; i++) {
			kafkaProducer.send(new ProducerRecord<>("first", "send message" + i), new Callback() {
				@Override
				public void onCompletion(RecordMetadata metadata, Exception exception) {
					if (null == exception) {
						log.info(">>>往 {} 发送消息 成功", metadata.topic());
					} else {
						log.error(">>> 发送消息失败", exception);
					}
				}
			});
		}
		//5. 关闭
		kafkaProducer.close();
	}
	
	
	/**
	 * ack 配置处理
	 */
	@Test
	public void ackTest() {
		//1. 定义 配置
		Properties properties = new Properties();
		properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
		// 序列化
		properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		//3. 优化配置参数  原先默认是 16K
		properties.setProperty(ProducerConfig.BATCH_SIZE_CONFIG, String.valueOf(16 * 1024 * 2));
		// 原先默认是 32M
		properties.setProperty(ProducerConfig.BUFFER_MEMORY_CONFIG, String.valueOf(32 * 1024 * 1000 * 2));
		//设置拉取的最大等待时长， 默认是 0
		properties.setProperty(ProducerConfig.LINGER_MS_CONFIG, String.valueOf(5));
		// 设置压缩类型
		properties.setProperty(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
		
		// 配置 ack 的类型 为 all , 小写.
		properties.setProperty(ProducerConfig.ACKS_CONFIG, "all");
		// 配置重试次数, 重试3次。 默认是  Integer.MAX_VALUE
		properties.setProperty(ProducerConfig.RETRIES_CONFIG, "3");
		
		
		//3. 定义对象
		KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(properties);
		//4. 发送消息
		for (int i = 0; i < 5; i++) {
			kafkaProducer.send(new ProducerRecord<>("first", "send try message" + i), new Callback() {
				@Override
				public void onCompletion(RecordMetadata metadata, Exception exception) {
					if (null == exception) {
						log.info(">>>往 {} 发送消息 成功", metadata.topic());
					} else {
						log.error(">>> 发送消息失败", exception);
					}
				}
			});
		}
		//5. 关闭
		kafkaProducer.close();
	}
}
