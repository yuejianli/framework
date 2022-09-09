package top.yueshushu.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import top.yueshushu.security.service.UserService;
import top.yueshushu.security.service.impl.UserServiceImpl;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-09-06
 */
@Configuration
public class SelfUserWebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Resource
	private UserService userService;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		
		return new BCryptPasswordEncoder();
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/js/**", "/css/**", "/images/**");
	}
	
	public static void main(String[] args) {
		String encode = new BCryptPasswordEncoder().encode("123456");
		System.out.println(encode);
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
	}
	
	/**
	 * 配置注销，返回json
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.anyRequest().authenticated()
				.and()
				.formLogin()
				.loginPage("/login.html")
				//指定提交网址
				.loginProcessingUrl("/doLogin")
				// 指定参数
				.usernameParameter("name")
				.passwordParameter("passwd")
				// 设置成功后的回调地址
				.successHandler(new MyAuthenticationSuccessHandler())
				.failureHandler(new MyAuthenticationFailHandler())
				.permitAll()
				.and()
				.logout()
				//.logoutUrl("/logout")
				.logoutRequestMatcher(
						new OrRequestMatcher(
								new AntPathRequestMatcher("/logout1", "GET"),
								new AntPathRequestMatcher("/logout", "GET"),
								new AntPathRequestMatcher("/logout2", "POST")
						)
				)
				.logoutSuccessHandler(new MyAuthenticationLogoutSuccessHandler())
				.invalidateHttpSession(true)
				.clearAuthentication(true)
				.logoutSuccessUrl("/login.html")
				.and()
				.csrf().disable();
	}
}
