package org.nutzview.view;

import java.io.StringWriter;
import java.io.Writer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;
import org.apache.velocity.io.VelocityWriter;
import org.apache.velocity.util.SimplePool;
import org.nutz.mvc.view.AbstractPathView;
import org.nutzview.model.IModel;
import org.nutzview.model.ModelProcesser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VelocityLayoutView extends AbstractPathView {

	private static final Logger LOG = LoggerFactory.getLogger(VelocityLayoutView.class);

	protected static final int WRITER_BUFFER_SIZE = 8 * 1024;

	protected SimplePool writerPool = new SimplePool(40);

	/**
	 * 默认的Layout页面模板
	 */
	public static final String DEFAULT_DEFAULT_LAYOUT = "layout/default.html";

	public static final String KEY_SCREEN_CONTENT = "_screen_content";

	public VelocityLayoutView(String dest) {
		super(dest);
	}

	@Override
	public void render(HttpServletRequest req, HttpServletResponse resp, Object obj) throws Exception {
		String path = evalPath(req, obj);

		resp.setContentType("text/html;charset=\"UTF-8\"");
		resp.setCharacterEncoding("UTF-8");
		System.out.println(obj);
		System.out.println(obj.getClass());
		try {
			
			IModel model = ModelProcesser.getInstance().process(obj);
			
			Context context = new VelocityContext();
			StringWriter sw = new StringWriter();

			context.put("base", req.getContextPath());
			context.put("data", model.getValue("data"));
			context.put("_title_name", model.getTitleName());
			context.put("request", req);
			context.put("session", req.getSession());

			Template template = Velocity.getTemplate(path);
			template.merge(context, sw);

			context.put(KEY_SCREEN_CONTENT, sw.toString());

			Template tmp = Velocity.getTemplate(DEFAULT_DEFAULT_LAYOUT);

			internalRenderTemplate(tmp, context, resp.getWriter());

		} catch (Exception e) {
			LOG.error("模板引擎错误", e);
			throw new Exception(e);
		}

	}

	private void internalRenderTemplate(Template template, Context context, Writer writer) throws Exception {
		VelocityWriter velocityWriter = null;
		try {

			velocityWriter = (VelocityWriter) writerPool.get();

			if (velocityWriter == null) {
				velocityWriter = new VelocityWriter(writer, WRITER_BUFFER_SIZE, true);
			} else {
				velocityWriter.recycle(writer);
			}
			template.merge(context, velocityWriter);

		} catch (Exception pee) {
			throw new Exception(pee);
		} finally {
			if (velocityWriter != null) {
				velocityWriter.flush();
				velocityWriter.recycle(null);
				writerPool.put(velocityWriter);
			}
			writer.flush();
			writer.close();
		}
	}

	public Context createVmContext(Object obj) {

		return null;
	}
}
