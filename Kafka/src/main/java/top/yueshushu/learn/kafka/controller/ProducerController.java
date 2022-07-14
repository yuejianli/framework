package top.yueshushu.learn.kafka.controller;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-07-14
 */
@RestController
@Slf4j
public class ProducerController {
	
	@Resource
	private KafkaTemplate kafkaTemplate;
	
	@RequestMapping("/sendMessage")
	public String sendMessage(String message) {
		ListenableFuture first = kafkaTemplate.send("first", message);
		log.info(">>> send message to topic success");
		return "kafka:" + message;
	}
}
