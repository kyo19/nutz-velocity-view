package org.nutzview.util;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nutz.lang.Strings;
import org.nutz.mvc.ActionHandler;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.NutFilter;
import org.nutz.mvc.RequestPath;
import org.nutz.mvc.SessionProvider;
import org.nutz.mvc.config.AtMap;
import org.nutz.mvc.config.FilterNutConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 同 JSP/Serlvet 容器的挂接点
 * 
 * @author wwei
 */
public class SKServiceFilter extends NutFilter implements Filter {

	private static final Logger log = LoggerFactory.getLogger(SKServiceFilter.class);

	private ActionHandler handler;

	private static final String IGNORE = "^.+\\.(jsp|png|gif|jpg|js|css|jspx|jpeg|swf|ico)$";

	private Pattern ignorePtn;

	private boolean skipMode;

	private String selfName;

	private SessionProvider sp;

	private boolean needRealName = true;

	public void init(FilterConfig conf) throws ServletException {
		Mvcs.setServletContext(conf.getServletContext());
		this.selfName = conf.getFilterName();
		System.out.println("SelfName>>>>>>>>>>>>>>>>>>>>>>>>>>" + selfName);
		Mvcs.set(selfName, null, null);

		FilterNutConfig config = new FilterNutConfig(conf);

		Mvcs.setNutConfig(config);

		// 如果仅仅是用来更新 Message 字符串的，不加载 Nutz.Mvc 设定
		String skipMode = Strings.sNull(conf.getInitParameter("skip-mode"), "false").toLowerCase();
		if (!"true".equals(skipMode)) {
			handler = new ActionHandler(config);
			System.out.println("-------------------ActionHandler URL------------------");
			
			System.out.println("-------------------Mapping URL------------------");
			AtMap atMap = config.getAtMap();
			Map<String, Method> mapping = atMap.getMethodMapping();
			Set<Entry<String, Method>> entrySet = mapping.entrySet();
			for (Entry<String, Method> entry : entrySet) {
				System.out.println(entry.getKey() + "   " + entry.getValue());
			}
			System.out.println("-------------------Mapping URL------------------");

			String regx = Strings.sNull(config.getInitParameter("ignore"), IGNORE);
			if (!"null".equalsIgnoreCase(regx)) {
				ignorePtn = Pattern.compile(regx, Pattern.CASE_INSENSITIVE);
			}
		} else
			this.skipMode = true;
		sp = config.getSessionProvider();

		log.info("\n***********************************************\n" + "         欢迎使用产业社区应用开发平台 V1.0"
				+ "\n***********************************************\n");

	}

	public void destroy() {
		Mvcs.resetALL();
		Mvcs.set(selfName, null, null);
		if (handler != null)
			handler.depose();
		Mvcs.setServletContext(null);
		Mvcs.close();
	}

	@SuppressWarnings("unchecked")
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException,
			ServletException {
		Mvcs.resetALL();
		try {
			if (sp != null)
				req = sp.filter((HttpServletRequest) req, (HttpServletResponse) resp,
						Mvcs.getServletContext());
			if (needRealName && skipMode) {
				// 直接无视自己的名字!!到容器取nutzservlet的名字!!
				Enumeration<String> names = Mvcs.getServletContext().getAttributeNames();
				while (names.hasMoreElements()) {
					String name = (String) names.nextElement();
					if (name.endsWith("_localization")) {
						this.selfName = name.substring(0, name.length() - "_localization".length());
						break;
					}
				}
				needRealName = false;
			}
			Mvcs.set(this.selfName, (HttpServletRequest) req, (HttpServletResponse) resp);
			if (!skipMode) {
				RequestPath path = Mvcs.getRequestPathObject((HttpServletRequest) req);
				if (null == ignorePtn || !ignorePtn.matcher(path.getUrl()).find()) {
					if (handler.handle((HttpServletRequest) req, (HttpServletResponse) resp))
						return;
				}
			}
			// 更新 Request 必要的属性
			Mvcs.updateRequestAttributes((HttpServletRequest) req);
			// 本过滤器没有找到入口函数，继续其他的过滤器
			chain.doFilter(req, resp);
		} finally {
			Mvcs.resetALL();
		}
	}
}
