package org.nutzview.controller;

import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;

public class DemoController {
	@At("/home")
	@Ok("vm:/home.html")
	public void home() {
	}

	@At("/help")
	@Ok("vm:/help.html")
	public void help() {
	}

	@At("/about")
	@Ok("vm:/about.html")
	public void index() {
	}
}
