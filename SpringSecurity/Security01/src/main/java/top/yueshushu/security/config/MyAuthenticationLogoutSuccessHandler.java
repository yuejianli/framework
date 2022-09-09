package top.yueshushu.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

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
public class MyAuthenticationLogoutSuccessHandler implements LogoutSuccessHandler {
	@Override
	public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
								Authentication authentication) throws IOException, ServletException {
		httpServletResponse.setContentType("application/json;charset=utf-8");
		Map<String, Object> resp = new HashMap<>();
		resp.put("status", 200);
		resp.put("msg", "注销成功");
		ObjectMapper objectMapper = new ObjectMapper();
		String result = objectMapper.writeValueAsString(resp);
		httpServletResponse.getWriter().write(result);
	}
}
