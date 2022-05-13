package top.yueshushu.learn;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import top.yueshushu.learn.pojo.Dict;
import top.yueshushu.learn.pojo.School;
import top.yueshushu.learn.service.DictService;
import top.yueshushu.learn.service.SchoolService;

import javax.annotation.Resource;

/**
测试使用 公共表
 **/
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class ShardingSphereDictTest {
    @Resource
    private DictService dictService;
    @Test
    public void insertTest(){
        // 几个数据库 的 id 都是一致 的
        Dict dict = new Dict();
        dict.setModule("sex");
        dict.setDvalue("男");

        dictService.save(dict);
    }

    @Test
    public void getByIdTest(){
        // 从一个数据库中查询即可
        Dict dict = dictService.getById(
                730521582266482689L
        );
        log.info("信息:{}",dict);
    }

    @Test
    public void deleteTest(){
        // 会全部删除
       dictService.removeById(
               730521582266482689L
        );
        log.info("信息:删除数据成功");
    }

}
