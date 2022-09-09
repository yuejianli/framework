package top.yueshushu.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;
import top.yueshushu.security.mapper.RoleMapper;
import top.yueshushu.security.mapper.UserMapper;
import top.yueshushu.security.mapper.UserRoleMapper;
import top.yueshushu.security.model.Role;
import top.yueshushu.security.model.User;
import top.yueshushu.security.model.UserRole;
import top.yueshushu.security.service.UserService;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-09-09
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {
	@Resource
	private RoleMapper roleMapper;
	@Resource
	private UserMapper userMapper;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("name", username);
		List<User> userList = userMapper.selectList(queryWrapper);
		
		if (CollectionUtils.isEmpty(userList)) {
			return null;
		}
		
		User user = userList.get(0);
		
		List<Role> roleList = roleMapper.listRolesByUserId(user.getId());
		
		user.setRoleList(roleList);
		
		return user;
		
	}
}
