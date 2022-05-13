package top.yueshushu.learn.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.yueshushu.learn.mapper.DictMapper;
import top.yueshushu.learn.mapper.SchoolMapper;
import top.yueshushu.learn.pojo.Dict;
import top.yueshushu.learn.pojo.School;

/**
    School 实现 ServiceImpl
 **/
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict>
        implements DictService {

}
