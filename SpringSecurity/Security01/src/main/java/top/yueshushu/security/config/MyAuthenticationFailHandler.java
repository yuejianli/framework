package top.yueshushu.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-09-07
 */
public class MyAuthenticationFailHandler implements AuthenticationFailureHandler {
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
		httpServletResponse.setContentType("application/json;charset=utf-8");
		Map<String, Object> resp = new HashMap<>();
		resp.put("status", 500);
		resp.put("msg", "登录失败");
		ObjectMapper objectMapper = new ObjectMapper();
		String result = objectMapper.writeValueAsString(resp);
		httpServletResponse.getWriter().write(result);
	}
}
