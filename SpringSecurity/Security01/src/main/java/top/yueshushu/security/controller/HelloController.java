package top.yueshushu.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-09-06
 */
@Controller
public class HelloController {
	
	@GetMapping("/index")
	@ResponseBody
	public String index() {
		return "Success Index";
	}
	
	@GetMapping("/fail")
	@ResponseBody
	public String fail() {
		return "Fail";
	}
	
	
	@GetMapping("/faillogin.html")
	public String faillogin() {
		return "faillogin";
	}
	
	
	@GetMapping("/hello")
	@ResponseBody
	public String hello() {
		return "Hello, 两个蝴蝶飞,学习 Security";
	}
}
