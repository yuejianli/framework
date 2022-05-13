package top.yueshushu.learn;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import top.yueshushu.learn.pojo.User;
import top.yueshushu.learn.service.UserService;

import javax.annotation.Resource;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
测试使用，水平分库
 **/
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class ShardingSphereSplitDBTest {
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
        user.setSchoolId(100L);
        userService.save(user);
        log.info("添加成功,{}",user);
    }
    @Test
    public void batchInsertTest(){
        for (int i =0;i<50;i++){
            int randNum = new Random().nextInt(5);
            try{
                TimeUnit.MILLISECONDS.sleep(randNum);
            }catch (Exception e){
                e.printStackTrace();
            }
            //1. 构建对象
            User user=new User();
            user.setName("欢欢"+i);
            user.setAge(22+i);
            user.setSex("女");
            user.setSchoolId(Long.parseLong(randNum+""));
            user.setDescription("一个非常可爱的女孩纸");
            //2. 添加方法
            userService.save(user);
            log.info("添加成功,{}",user);
        }
    }

    @Test
    public void findByIdTest(){
        User one = userService.lambdaQuery()
                .eq(User::getSchoolId, 4)
                .eq(User::getId, 730483809123827712L)
                .one();
        log.info("输出用户信息:{}",one);

       one = userService.lambdaQuery()
                .eq(User::getSchoolId, 1)
                .eq(User::getId, 730483809249656833L)
                .one();
        log.info("输出用户信息:{}",one);
    }
}
