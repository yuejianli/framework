package top.yueshushu.learn;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @ClassName:ShardingSphereApplication
 * @Description ShardingSphere 讲解
 * @Author 岳建立
 * @Date 2021/4/26 12:01
 * @Version 1.0
 **/
@SpringBootApplication
@MapperScan(value="top.yueshushu.learn.mapper")
public class ShardingSphereApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShardingSphereApplication.class,args);
        System.out.println("运行 ShardingSphere 配置文件");
    }
}
