package top.yueshushu.security;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

import lombok.extern.slf4j.Slf4j;

/**
 * SecurityApp
 *
 * @author yuejianli
 * @date 2022-09-06
 */
@SpringBootApplication
@MapperScan(basePackages = "top.yueshushu.security.mapper")
@Slf4j
// 该注解开启之后可以通过注解实现用户的权限登录
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityApp {
	public static void main(String[] args) {
		SpringApplication.run(SecurityApp.class, args);
		log.info(">> 学习使用 Security ");
	}
}
