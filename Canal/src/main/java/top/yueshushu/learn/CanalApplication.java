package top.yueshushu.learn;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @ClassName:CanalApplication
 * @Description canal 讲解
 * @Author 岳建立
 * @Date 2021/4/26 12:01
 * @Version 1.0
 **/
@SpringBootApplication
@Slf4j
public class CanalApplication {
    public static void main(String[] args) {
        SpringApplication.run(CanalApplication.class,args);
        log.info("运行 Canal 资源");
    }
}
