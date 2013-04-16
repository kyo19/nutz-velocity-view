package org.nutzview;

import java.util.Properties;

import org.apache.velocity.app.Velocity;
import org.nutz.mvc.NutConfig;
import org.nutz.mvc.Setup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ICommunitySetup implements Setup {

	private static final Logger log = LoggerFactory.getLogger(ICommunitySetup.class);

	@Override
	public void init(NutConfig config) {
		velocityInit();
	}

	@Override
	public void destroy(NutConfig config) {
		// TODO Auto-generated method stub

	}

	private void velocityInit() {
		log.info("Veloctiy引擎初始化...");
		Properties p = new Properties();
		p.setProperty("resource.loader", "file,classloader");
		p.setProperty("file", "org.apache.velocity.tools.view.WebappResourceLoader");
		p.setProperty("classloader.resource.loader.class", "org.nutzview.util.MyClasspathResourceLoader");
		p.setProperty("classloader.resource.loader.path", "templates");
		p.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
		p.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8");
		p.setProperty("velocimacro.library.autoreload", "false");
		p.setProperty("classloader.resource.loader.root", "templates");
		p.setProperty("velocimarco.library.autoreload", "true");
		p.setProperty("runtime.log.error.stacktrace", "false");
		p.setProperty("runtime.log.warn.stacktrace", "false");
		p.setProperty("runtime.log.info.stacktrace", "false");
		p.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.SimpleLog4JLogSystem");
		p.setProperty("runtime.log.logsystem.log4j.category", "velocity_log");

		Velocity.init(p);
		log.info("Veloctiy引擎初始化完成。");
	}
}
