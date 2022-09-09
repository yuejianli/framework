package top.yueshushu.security.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import top.yueshushu.security.mapper.UserMapper;
import top.yueshushu.security.mapper.UserRoleMapper;
import top.yueshushu.security.model.User;
import top.yueshushu.security.model.UserRole;
import top.yueshushu.security.service.UserRoleService;
import top.yueshushu.security.service.UserService;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-09-09
 */
@Service
@Slf4j
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

}
