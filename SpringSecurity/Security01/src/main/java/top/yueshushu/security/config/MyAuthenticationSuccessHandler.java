package top.yueshushu.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

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
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
										Authentication authentication) throws IOException, ServletException {
		httpServletResponse.setContentType("application/json;charset=utf-8");
		Map<String, Object> resp = new HashMap<>();
		resp.put("status", 200);
		resp.put("msg", "登录成功");
		ObjectMapper objectMapper = new ObjectMapper();
		String result = objectMapper.writeValueAsString(resp);
		httpServletResponse.getWriter().write(result);
	}
}
