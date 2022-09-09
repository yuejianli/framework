package top.yueshushu.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-09-06
 */
// @Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/js/**", "/css/**", "/images/**");
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
				.withUser("root")
				.password("root")
				.roles("admin");
	}
	
	/**
	 简单配置
	 */
//	@Override
//	public void configure(HttpSecurity httpSecurity) throws Exception {
//		httpSecurity.authorizeRequests()
//				.anyRequest()
//				.authenticated()
//				.and()
//				.formLogin()
//				.permitAll();
//	}
	
	/**
	 配置一些 页面， 成功，失败 页面
	 */
//
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		http.authorizeRequests()
//				.anyRequest().authenticated()
//				.and()
//				.formLogin()
//				.loginPage("/login.html")
//				//指定提交网址
//				.loginProcessingUrl("/doLogin")
//				// 指定参数
//				.usernameParameter("name")
//				.passwordParameter("passwd")
//				// 设置成功后的回调地址
//				.defaultSuccessUrl("/index")
////				.successForwardUrl("/index")
//				.failureUrl("/fail")
//				//.failureForwardUrl("/fail.html")
//				// 注销登录
////				.and()
////				.logout()
////				.logoutUrl("/logout")
////				.logoutRequestMatcher(new AntPathRequestMatcher("/logout","GET"))
////				.logoutSuccessUrl("/index")
////				.deleteCookies()
////				.clearAuthentication(true)
////				.invalidateHttpSession(true)
//				.permitAll()
//				.and()
//				.csrf().disable();
//	}
	
	/**
	 * 成功之后，对 成功方式进行自定义行为。
	 */
	protected void configure2(HttpSecurity http) throws Exception {
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
				.successHandler(successHanlder())
//				.successForwardUrl("/index")
				.failureUrl("/fail")
				//.failureForwardUrl("/fail.html")
				// 注销登录
//				.and()
//				.logout()
//				.logoutUrl("/logout")
//				.logoutRequestMatcher(new AntPathRequestMatcher("/logout","GET"))
//				.logoutSuccessUrl("/index")
//				.deleteCookies()
//				.clearAuthentication(true)
//				.invalidateHttpSession(true)
				.permitAll()
				.and()
				.csrf().disable();
	}
	
	/**
	 * 对成功之后的行为进行处理
	 * 对应的 action 为: form action="/doLogin?target=/hello"
	 */
	private SavedRequestAwareAuthenticationSuccessHandler successHanlder() {
		SavedRequestAwareAuthenticationSuccessHandler savedRequestAwareAuthenticationSuccessHandler = new SavedRequestAwareAuthenticationSuccessHandler();
		savedRequestAwareAuthenticationSuccessHandler.setDefaultTargetUrl("/index");
		savedRequestAwareAuthenticationSuccessHandler.setTargetUrlParameter("target");
		return savedRequestAwareAuthenticationSuccessHandler;
	}
	
	
	/**
	 * 成功之后，对 成功方式进行自定义行为。
	 */
	protected void configure3(HttpSecurity http) throws Exception {
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
//				.successForwardUrl("/index")
				.failureUrl("/fail")
				//.failureForwardUrl("/fail.html")
				.permitAll()
				.and()
				.csrf().disable();
	}
	
	/**
	 * 指定失败返回 json
	 */
	protected void configure4(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.anyRequest().authenticated()
				.and()
				.formLogin()
				.loginPage("/faillogin.html")
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
				.csrf().disable();
	}
	
	/**
	 * 配置注销，返回json
	 */
	protected void configure5(HttpSecurity http) throws Exception {
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
				.invalidateHttpSession(true)
				.defaultLogoutSuccessHandlerFor((req, httpServletResponse, auth) -> {
					httpServletResponse.setContentType("application/json;charset=utf-8");
					Map<String, Object> resp = new HashMap<>();
					resp.put("status", 200);
					resp.put("msg", "使用 logoutXxx 注销成功");
					ObjectMapper objectMapper = new ObjectMapper();
					String result = objectMapper.writeValueAsString(resp);
					httpServletResponse.getWriter().write(result);
				}, new AntPathRequestMatcher("/logout", "GET"))
				.defaultLogoutSuccessHandlerFor((req, httpServletResponse, auth) -> {
					httpServletResponse.setContentType("application/json;charset=utf-8");
					Map<String, Object> resp = new HashMap<>();
					resp.put("status", 200);
					resp.put("msg", "使用 logout1 注销成功");
					ObjectMapper objectMapper = new ObjectMapper();
					String result = objectMapper.writeValueAsString(resp);
					httpServletResponse.getWriter().write(result);
				}, new AntPathRequestMatcher("/logout1", "GET"))
				.defaultLogoutSuccessHandlerFor((req, httpServletResponse, auth) -> {
					httpServletResponse.setContentType("application/json;charset=utf-8");
					Map<String, Object> resp = new HashMap<>();
					resp.put("status", 200);
					resp.put("msg", "使用 logout2 注销成功");
					ObjectMapper objectMapper = new ObjectMapper();
					String result = objectMapper.writeValueAsString(resp);
					httpServletResponse.getWriter().write(result);
				}, new AntPathRequestMatcher("/logout2", "POST"))
				.clearAuthentication(true)
				.logoutSuccessUrl("/login.html")
				.and()
				.csrf().disable();
	}
}
