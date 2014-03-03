package org.nutzview.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * URL重写 http://www.hn12396.com -> 对应根目录 http://xxxx.hn12396.com -> http://www.hn12396.com/nvv/xxxx
 * 
 * @author llnyt
 * 
 */
public class URLFilter implements Filter {

	private static final String DOMAIN = ".hn12396.com";
	private Map<String, String> urls = new HashMap<String, String>();

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		urls.put("help", "help");
		urls.put("about", "about");
		urls.put("home", "/home");
		urls.put("test", "test");
		urls.put("about1", "about1");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		String host = req.getHeader("Host").trim();
		// System.out.println(host);
		int indexOf = host.indexOf(":");
		host = host.substring(0, indexOf);
		boolean startsWith = host.endsWith(DOMAIN);
		// System.out.println(startsWith);

		String url = req.getPathInfo();
		if (null == url) {
			url = req.getServletPath();
			System.out.println("url>>>>:" + url);
			if (url.endsWith(".css")) {
				chain.doFilter(request, response);
				return;
			}
		}
		if (startsWith) {
			int index = host.indexOf(DOMAIN);
			String hostname = host.substring(0, index);
			// System.out.println(index);
			// System.out.println(hostname);
			String forwardUrl = urls.get(hostname);
			req.getRequestDispatcher(forwardUrl).forward(req, resp);
			return;
		}

		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {

	}

}