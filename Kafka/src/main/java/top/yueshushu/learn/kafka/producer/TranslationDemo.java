package top.yueshushu.learn.kafka.producer;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

/**
 * 事务处理
 *
 * @author yuejianli
 * @date 2022-06-27
 */
@SpringBootTest
@Slf4j
public class TranslationDemo {
	
	/**
	 * 没有事务处理
	 */
	@Test
	public void noTranslationTest() throws Exception {
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
		
		//关闭幂等性
		properties.setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "false");
		properties.setProperty(ProducerConfig.ACKS_CONFIG, "1");
		// 去掉配置为 ALL
		//3. 定义对象
		KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(properties);
		//4. 发送消息
		for (int i = 0; i < 5; i++) {
			kafkaProducer.send(new ProducerRecord<>("first", "send translation1 message" + i), new Callback() {
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
		TimeUnit.MILLISECONDS.sleep(50);
		// 会发送 0 1 2 3 四条信息
		int a = 10 / 0;    //5. 关闭
		kafkaProducer.close();
	}
	
	
	/**
	 * 存在事务处理,当失败后，并不会发送消息。
	 */
	@Test
	public void withTranslationTest() throws Exception {
		//1. 定义 配置
		Properties properties = new Properties();
		properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
		// 序列化
		properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		//3. 优化配置参数  原先默认是 16K
		//开启性
		properties.setProperty(ProducerConfig.ACKS_CONFIG, "all");
		//配置事务id,id 可以任意起名,必须要设置
		properties.setProperty(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "transactional_first_0");
		//3. 定义对象
		KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(properties);
		//4. 发送消息
		// 初始化事务
		kafkaProducer.initTransactions();
		// 开启事务
		kafkaProducer.beginTransaction();
		
		try {
			for (int i = 0; i < 5; i++) {
				kafkaProducer.send(new ProducerRecord<>("first", "send translation3  message" + i), new Callback() {
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
			// 不会发送任何消息
			int a = 10 / 0;
			//提交事务
			kafkaProducer.commitTransaction();
		} catch (Exception e) {
			// 有异常，终止事务
			log.error("有错误", e);
			kafkaProducer.abortTransaction();
		} finally {
			//5. 关闭
			kafkaProducer.close();
		}
	}
}
