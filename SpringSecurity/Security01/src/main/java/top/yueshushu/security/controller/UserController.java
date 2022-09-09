package top.yueshushu.security.controller;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import top.yueshushu.security.model.User;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-09-07
 */
@RestController
public class UserController {
	
	@GetMapping("/user")
	public String user() {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		User user = (User) authentication.getPrincipal();
		String name = user.getUsername();
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		
		List<String> roles = new ArrayList<>();
		if (!CollectionUtils.isEmpty(authorities)) {
			authorities.forEach(
					n -> {
						roles.add(n.getAuthority());
					}
			);
		}
		
		Object credentials = authentication.getCredentials();
		
		String result = String.format("用户名是:%s,凭证是:%s,角色是:%s", name, credentials, roles.toString());
		
		return result;
	}
	
	/**
	 * 要配置在启动参数 vm 里面
	 * <p>
	 * -Dspring.security.strategy=MODE_INHERITABLETHREADLOCAL
	 */
	@GetMapping("/asyncUser")
	public String asyncUser() {
		
		AtomicReference<String> result = new AtomicReference<>("");
		
		new Thread(() -> {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			
			if (null == authentication) {
				result.set("未获取到对象");
				return;
			}
			
			User user = (User) authentication.getPrincipal();
			String name = user.getUsername();
			Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
			
			List<String> roles = new ArrayList<>();
			if (!CollectionUtils.isEmpty(authorities)) {
				authorities.forEach(
						n -> {
							roles.add(n.getAuthority());
						}
				);
			}
			
			Object credentials = authentication.getCredentials();
			
			String single = String.format("用户名是:%s,凭证是:%s,角色是:%s", name, credentials, roles.toString());
			
			result.set(single);
		}).start();
		
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return result.get();
	}
	
	@RequestMapping("/authentication")
	public String authentication(Authentication authentication) {
		User user = (User) authentication.getPrincipal();
		String name = user.getUsername();
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		
		List<String> roles = new ArrayList<>();
		if (!CollectionUtils.isEmpty(authorities)) {
			authorities.forEach(
					n -> {
						roles.add(n.getAuthority());
					}
			);
		}
		
		Object credentials = authentication.getCredentials();
		
		String result = String.format("用户名是:%s,凭证是:%s,角色是:%s", name, credentials, roles.toString());
		
		return result;
	}
	
	@RequestMapping("/principal")
	public String authentication(Principal principal) {
		UsernamePasswordAuthenticationToken user = (UsernamePasswordAuthenticationToken) principal;
		String name = user.getName();
		String result = String.format("用户名是:%s", name);
		return result;
	}
}
