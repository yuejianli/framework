package top.yueshushu.learn;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import top.yueshushu.learn.pojo.Encry;
import top.yueshushu.learn.pojo.ReadWrite;
import top.yueshushu.learn.service.EncryService;
import top.yueshushu.learn.service.ReadWriteService;

import javax.annotation.Resource;

/**
测试使用 数据脱敏
 **/
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class EncryTest {
    @Resource
    private EncryService encryService;
    @Test
    public void insertTest(){
        // 几个数据库 的 id 都是一致 的
        Encry encry = new Encry();
        encry.setAccount("读写分离");
        encry.setPs("123456");

        encryService.save(encry);
    }

    @Test
    public void getByIdTest(){
        // 从一个数据库中查询即可
        Encry encry = encryService.getById(
                1
        );
        log.info("信息:{}",encry);
    }

    @Test
    public void deleteTest(){
        // 会全部删除
        encryService.removeById(
               1
        );
        log.info("信息:删除数据成功");
    }

}
