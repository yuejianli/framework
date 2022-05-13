package top.yueshushu.learn.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.yueshushu.learn.mapper.ReadWriteMapper;
import top.yueshushu.learn.mapper.SchoolMapper;
import top.yueshushu.learn.pojo.ReadWrite;
import top.yueshushu.learn.pojo.School;

/**
 ReadWrite 实现 ServiceImpl
 **/
@Service
public class ReadWriteServiceImpl extends ServiceImpl<ReadWriteMapper, ReadWrite>
        implements ReadWriteService {

}
