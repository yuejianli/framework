package top.yueshushu.learn;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import top.yueshushu.learn.pojo.Dict;
import top.yueshushu.learn.pojo.ReadWrite;
import top.yueshushu.learn.service.DictService;
import top.yueshushu.learn.service.ReadWriteService;

import javax.annotation.Resource;

/**
测试使用 读写分离配置
 **/
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class ReadWriteTest {
    @Resource
    private ReadWriteService readWriteService;
    @Test
    public void insertTest(){
        // 几个数据库 的 id 都是一致 的
        ReadWrite readWrite = new ReadWrite();
        readWrite.setName("读写分离");
        readWrite.setDescription("读");

        readWriteService.save(readWrite);
    }

    @Test
    public void getByIdTest(){
        // 从一个数据库中查询即可
        ReadWrite dict = readWriteService.getById(
                1
        );
        log.info("信息:{}",dict);
    }

    @Test
    public void deleteTest(){
        // 会全部删除
        readWriteService.removeById(
               1
        );
        log.info("信息:删除数据成功");
    }

}
