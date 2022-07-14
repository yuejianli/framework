package top.yueshushu.learn.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

/**
 * Kafka 启动
 *
 * @author yuejianli
 * @date 2022-06-20
 */
@SpringBootApplication
@Slf4j
public class KafkaApp {
	public static void main(String[] args) {
		SpringApplication.run(KafkaApp.class, args);
		log.info(">>>>> Kafka 启动成功");
	}
}
