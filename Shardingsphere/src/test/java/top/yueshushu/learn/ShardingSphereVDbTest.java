package top.yueshushu.learn;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import top.yueshushu.learn.pojo.School;
import top.yueshushu.learn.pojo.User;
import top.yueshushu.learn.service.SchoolService;
import top.yueshushu.learn.service.UserService;

import javax.annotation.Resource;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
测试使用 垂直分库
 **/
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class ShardingSphereVDbTest {
    @Resource
    private SchoolService schoolService;
    @Test
    public void insertTest(){
        School school = new School();
        school.setName("北京");
        school.setDescription("北京大学");
        schoolService.save(school);
    }

    @Test
    public void getByIdTest(){
        School school = schoolService.getById(
                730518335342510081L
        );
        log.info("信息:{}",school);
    }


}
