package top.yueshushu.learn.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.yueshushu.learn.mapper.EncryMapper;
import top.yueshushu.learn.mapper.ReadWriteMapper;
import top.yueshushu.learn.pojo.Encry;
import top.yueshushu.learn.pojo.ReadWrite;

/**
 ReadWrite 实现 ServiceImpl
 **/
@Service
public class EncryServiceImpl extends ServiceImpl<EncryMapper, Encry>
        implements EncryService {

}
