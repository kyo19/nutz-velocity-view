package org.nutzview.controller;

import java.util.HashMap;
import java.util.Map;

import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutzview.model.IModel;
import org.nutzview.model.VelocityModel;

@IocBean
public class DemoController {
	
	@At("/home")
	@Ok("vm:/home.html")
	public IModel home() {
		// Map<String, String> param = new HashMap<String, String>();
		// param.put("_title_name", "第一个页面Home.html");
		IModel model = new VelocityModel();

		return model;
	}

	@At("/help")
	@Ok("vm:/help.html")
	public IModel help() {
		IModel model = new VelocityModel();
		model.setTitleName("Help页面");
		model.put("data", "这是一个测试help.html页面的例子");
		return model;
	}

	@At("/about")
	@Ok("vm:/about.html")
	public IModel about() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("msg", "测试信息");
		map.put("m", "关于页面");

		IModel model = new VelocityModel();
		model.setTitleName("About页面");
		model.addValue(map);

		return model;
	}

	@At("/about1")
	@Ok("vm:/about.html")
	public IModel about1(@Param("username") String username) {
		IModel model = new VelocityModel();
		model.setTitleName(username);
		return model;
	}
	
	@At("/test")
	@Ok("vm:/about.html")
	public String test() {
		return "test页面";
	}
}
