package top.yueshushu.learn.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.yueshushu.learn.mapper.UserMapper;
import top.yueshushu.learn.pojo.User;

/**
    用户实现 ServiceImpl
 **/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User>
        implements UserService {

}
