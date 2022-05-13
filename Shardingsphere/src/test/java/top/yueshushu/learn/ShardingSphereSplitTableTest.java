package top.yueshushu.learn;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import top.yueshushu.learn.pojo.User;
import top.yueshushu.learn.service.UserService;

import javax.annotation.Resource;

/**
测试使用  水平 分表
 **/
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class ShardingSphereSplitTableTest {
    @Resource
    private UserService userService;
    @Test
    public void insertTest(){
        //1. 构建对象
        User user=new User();
        user.setName("欢欢");
        user.setAge(22);
        user.setSex("女");
        user.setDescription("一个非常可爱的女孩纸");
        //2. 添加方法
        userService.save(user);
        log.info("添加成功,{}",user);
    }
    @Test
    public void batchInsertTest(){
        for (int i =0;i<10;i++){
            //1. 构建对象
            User user=new User();
            user.setName("欢欢"+i);
            user.setAge(22+i);
            user.setSex("女");
            user.setDescription("一个非常可爱的女孩纸");
            //2. 添加方法
            userService.save(user);
            log.info("添加成功,{}",user);
        }
    }

    @Test
    public void findByIdTest(){
       // User user=userService.getById(730474199373578241L);
        User user=userService.getById(730474199373578242L);
        log.info("输出用户信息 {}",user);
    }
}
