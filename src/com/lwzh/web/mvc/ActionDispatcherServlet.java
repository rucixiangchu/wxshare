package com.lwzh.web.mvc;

import httl.Engine;
import httl.Template;

import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.Advised;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.lwzh.Constant;
import com.lwzh.tool.HttpTool;
import com.lwzh.web.BaseAction;
import com.lwzh.web.Context;
import com.lwzh.web.Session;
import com.lwzh.web.annotation.P;
import com.lwzh.web.annotation.PParser;
import com.lwzh.web.session.SessionStorage;
import com.wx.study.AccessTokenThread;

public class ActionDispatcherServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(ActionDispatcherServlet.class);

	private static final Set<String> indexPaths = new HashSet<String>();
	static {
		indexPaths.add("/");
		indexPaths.add("/index.html");
	}

	private ApplicationContext springIoc;
	private Engine httlEngine;

	private SessionStorage sessionStorage;
	//	private AccessService accessService;
//	private SsoAction sso;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		String springConfig = config.getInitParameter("springConfig");
		logger.info("init springIoc : " + springConfig);
		springIoc = new ClassPathXmlApplicationContext(springConfig);
		httlEngine = Engine.getEngine("httl_config.properties");

		sessionStorage = (SessionStorage) springIoc.getBean("sessionStorage");
		new Thread(new AccessTokenThread()).start();
	}

	@Override
	public void destroy() {
		((ClassPathXmlApplicationContext) springIoc).close();
		super.destroy();
	}

	private BaseAction getAction(String actionName) {
		try {
			return springIoc.getBean(actionName, BaseAction.class);
		} catch (BeansException e) {
			logger.warn(e.toString());
			return null;
		}
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) {
		logger.error(HttpTool.toString(req));
		try {
			this.handleRequest(req, resp);
		} catch (Throwable e) {
			logger.error(e.toString() + "\r\n" + HttpTool.toString(req), e);
			writeError(req, resp);
		} finally {
			done(req, resp);
		}
	}

	private void handleRequest(HttpServletRequest req, HttpServletResponse resp) throws Throwable {
		// 检查访问路径
		final String contextPath = req.getContextPath();
		final String requestURI = req.getRequestURI();

		String path = requestURI.substring(contextPath.length());
		String[] ss = path.split("/");
		if (indexPaths.contains(path)) {
			path = "/index.html";
			ss = new String[3];
			ss[0] = "";
			ss[1] = "index";
			ss[2] = "index.html";
		} else if (ss.length != 3) {
			logger.warn("Illegal path : " + path + ", requestURI=" + requestURI + ", contextPath=" + contextPath + "\r\n" + HttpTool.toString(req));
			resp.sendRedirect(contextPath + "/");
			return;
		}

		// 访问路径转化为beanName，methodName
		// ss[0] is ""
		String beanName = ss[1];
		String methodName = ss[2];
		String suffix = null;
		int suffixPos = ss[2].indexOf(".");
		if (suffixPos != -1) {
			methodName = ss[2].substring(0, suffixPos);
			suffix = ss[2].substring(suffixPos + 1);
		}
		if (beanName == null || beanName.isEmpty() || methodName == null || methodName.isEmpty()) {
			logger.warn("Illegal beanName or methodName : " + beanName + "/" + methodName + "\r\n" + HttpTool.toString(req));
			resp.sendRedirect(contextPath + "/");
			return;
		}

		// 在Spring中查找action
		BaseAction theAction = getAction(beanName);
		if (theAction == null) {
			logger.warn("No bean was found : " + beanName + "\r\n" + HttpTool.toString(req));
			resp.sendRedirect(contextPath + "/");
			return;
		}

		// 查找action的方法
		Map<String, Object> arg = (Map<String, Object>) parseReqParameter(req);
		Method theMethod = parseTheMethod(theAction, methodName);
		if (theMethod == null) {
			logger.warn("No method was found : " + methodName + " for " + beanName + "\r\n" + HttpTool.toString(req));
			resp.sendRedirect(contextPath + "/");
			return;
		}
		Object[] theArgs = parseTheArgs(theAction, theMethod, arg);

		// 设置Context
		Context.setContext(new Context(req, resp, sessionStorage));
		Context context = Context.getContext();
		context.getReqMap().putAll(arg);
		Session session = context.getSession();

		// 执行Action方法
		String tag = "Execute Action [" + session.getId() + "][" + req.hashCode() + "]";
		callAction(tag, theAction, theMethod, theArgs);

		// 重定向
		String redirectUrl = theAction.getRedirectUrl();
		if (redirectUrl != null && !redirectUrl.isEmpty()) {
			resp.sendRedirect(redirectUrl);
			return;
		}

		// 返回内容
		Map<String, Object> model = theAction.getRespMap();
		if (suffix == null || suffix.isEmpty()) {
			writeNull(req, resp);
		} else if (suffix.equals("html")) {
			model.put(Constant.WEB.CONTEXT, context);
			model.put(Constant.WEB.SESSION, session);
			model.put(Constant.WEB.I18N, new HashMap<String, String>());
			writeHtml(req, resp, path, model);
		} else if (suffix.equals("json")) {
			writeJson(req, resp, model);
		} else if (suffix.equals("do")) {
			writeNull(req, resp);
		} else {
			logger.warn("Illegal suffix : " + suffix);
			writeNull(req, resp);
		}
	}

	private Map<String, Object> parseReqParameter(HttpServletRequest req) {
		Map<String, Object> ret = new HashMap<String, Object>();
		Map<String, String[]> parameterMap = req.getParameterMap();
		for (Entry<String, String[]> entry : parameterMap.entrySet()) {
			String key = entry.getKey();
			String[] values = entry.getValue();
			if (values.length == 0) {
				ret.put(key, "");
			} else if (values.length == 1) {
				ret.put(key, values[0]);
			} else {
				ret.put(key, values);
			}
		}
		return ret;
	}

	private Method parseTheMethod(Object theBean, String methodName) {
		Method theMethod = null;
		Class<?> theClass = theBean.getClass();
		if (theBean instanceof Advised) {
			Advised advised = (Advised) theBean;
			theClass = advised.getTargetClass();
		}
		Method[] methods = theClass.getMethods();
		for (Method method : methods) {
			if (method.getDeclaringClass() != theClass) {
				continue;
			}
			if (method.getName().startsWith("set")) {
				continue;
			}
			if (method.getName().equals(methodName)) {
				theMethod = method;
				break;
			}
		}
		return theMethod;
	}

	private Object parseType(Object value, Class<?> parameterType, P classP, P methodP, P parameterP) throws ParseException {
		if (value == null) {
			return null;
		}

		if (value instanceof String && ((String) value).isEmpty()) {
			boolean emptyValueAsNull = PParser.parseEmptyValueAsNull(classP, methodP, parameterP);

			if (parameterType == String.class) {
				return emptyValueAsNull ? null : "";
			} else {
				return null;
			}
		}

		if (parameterType == int.class || parameterType == Integer.class) {
			return Integer.parseInt((String) value);
		} else if (parameterType == long.class || parameterType == Long.class) {
			return Long.parseLong((String) value);
		} else if (parameterType == float.class || parameterType == Float.class) {
			return Float.parseFloat((String) value);
		} else if (parameterType == double.class || parameterType == Double.class) {
			return Double.parseDouble((String) value);
		} else if (parameterType == boolean.class || parameterType == Boolean.class) {
			return Boolean.parseBoolean((String) value);
		} else if (parameterType == String[].class && value.getClass() == String.class) {
			return new String[] { (String) value };
		} else if (parameterType == Date.class) {
			String datePattern = PParser.parseDatePattern(classP, methodP, parameterP);
			SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
			return sdf.parse((String) value);
		} else {
			return value;
		}
	}

	private Object[] parseTheArgs(BaseAction theAction, Method theMethod, Map<String, Object> arg) throws ParseException {
		P classP = theAction.getClass().getAnnotation(P.class);
		P methodP = theMethod.getAnnotation(P.class);

		Annotation[][] pass = theMethod.getParameterAnnotations();
		Class<?>[] pts = theMethod.getParameterTypes();
		Object[] theArgs = new Object[pass.length];
		for (int i = 0; i < pass.length; i++) {
			Annotation[] pas = pass[i];
			for (Annotation pa : pas) {
				if (pa.annotationType() == P.class) {
					P p = (P) pa;
					String v = p.value();
					if (arg.containsKey(v)) {
						Object value = arg.get(v);
						Class<?> pt = pts[i];
						theArgs[i] = parseType(value, pt, classP, methodP, p);
					}
				}
			}
		}
		return theArgs;
	}

	private Object callAction(String tag, final BaseAction theAction, final Method theMethod, final Object[] theArgs) throws Throwable {
		logger.debug(tag + " >>>1. " + theMethod.toGenericString());
		logger.debug(tag + " >>>2. " + theAction.getReqMap());

		Object theRet = null;
		if (theArgs == null || theArgs.length == 0) {
			theRet = theMethod.invoke(theAction);
		} else {
			theRet = theMethod.invoke(theAction, theArgs);
		}

		logger.debug(tag + " <<<3. " + theAction.getRespMap());
		logger.debug(tag + " <<<4. " + theRet);
		return theRet;
	}


	private void writeNull(HttpServletRequest req, HttpServletResponse resp) {
	}

	private void writeError(HttpServletRequest req, HttpServletResponse resp) {
	}


	private void writeHtml(HttpServletRequest req, HttpServletResponse resp, String path, Map<String, Object> model) throws Exception {
		resp.setCharacterEncoding(Constant.CHARSET_UTF8);
		Writer out = resp.getWriter();
		Template template = httlEngine.getTemplate(path);
		template.render(model, out);
	}

	private void writeJson(HttpServletRequest req, HttpServletResponse resp, Map<String, Object> model) throws Exception {
		resp.setCharacterEncoding(Constant.CHARSET_UTF8);
		Writer out = resp.getWriter();
		out.write(JSON.toJSONStringWithDateFormat(model, "yyyy-MM-dd HH:mm:ss", SerializerFeature.WriteDateUseDateFormat));
	}

	private void done(HttpServletRequest req, HttpServletResponse resp) {
		Context.setContext(null);
	}

}
