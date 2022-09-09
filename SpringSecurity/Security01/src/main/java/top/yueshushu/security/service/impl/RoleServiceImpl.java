package top.yueshushu.security.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import top.yueshushu.security.mapper.RoleMapper;
import top.yueshushu.security.model.Role;
import top.yueshushu.security.service.RoleService;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-09-09
 */
@Service
@Slf4j
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

}
