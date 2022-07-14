package top.yueshushu.learn.kafka.consumerlistener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-07-14
 */
@Component
@Slf4j
public class TopicKafkaListener {
	
	@KafkaListener(topics = {"first"})
	public void firstTopicListener(Object message) {
		log.info(">>>>get data : {}", message);
	}
}
