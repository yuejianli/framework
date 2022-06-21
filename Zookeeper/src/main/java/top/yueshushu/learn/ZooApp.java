package top.yueshushu.learn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-06-20
 */
@SpringBootApplication
@Slf4j
public class ZooApp {
	public static void main(String[] args) {
		SpringApplication.run(ZooApp.class, args);
		log.info(">>>>> Zookeeper 启动成功");
	}
}
