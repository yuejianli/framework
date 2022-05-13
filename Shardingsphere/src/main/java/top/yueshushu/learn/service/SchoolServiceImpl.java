package top.yueshushu.learn.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.yueshushu.learn.mapper.SchoolMapper;
import top.yueshushu.learn.mapper.UserMapper;
import top.yueshushu.learn.pojo.School;
import top.yueshushu.learn.pojo.User;

/**
    School 实现 ServiceImpl
 **/
@Service
public class SchoolServiceImpl extends ServiceImpl<SchoolMapper, School>
        implements SchoolService {

}
