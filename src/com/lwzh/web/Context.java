package com.lwzh.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lwzh.Constant;
import com.lwzh.web.session.SessionStorage;

public class Context {

	private static final ThreadLocal<Context> contextTL = new ThreadLocal<Context>();

	public static void setContext(Context context) {
		contextTL.set(context);
	}

	public static Context getContext() {
		return contextTL.get();
	}

	private final HttpServletRequest req;
	private final HttpServletResponse resp;

	private final Session session;
	private final Map<String, Object> reqMap;
	private final Map<String, Object> respMap;

	private final String scheme;
	private final String serverName;
	private final int serverPort;
	private final String contextPath;
	private final String requestURI;
	private final String path;

	private String redirectUrl;

	public Context(HttpServletRequest request, HttpServletResponse response, SessionStorage sessionStorage) {
		req = request;
		resp = response;

		String key = req.getSession(true).getId();
		Session session = sessionStorage.getSession(req, resp, key);
		session.setI18n(Constant.WEB.I18N_zh_CN);

		this.session = session;
		reqMap = new HashMap<String, Object>();
		respMap = new HashMap<String, Object>();

		scheme = req.getScheme();
		serverName = req.getServerName();
		serverPort = req.getServerPort();
		contextPath = req.getContextPath();
		requestURI = req.getRequestURI();
		path = requestURI.substring(contextPath.length());
	}

	public HttpServletRequest getReq() {
		return req;
	}

	public HttpServletResponse getResp() {
		return resp;
	}

	public Session getSession() {
		return session;
	}

	public Map<String, Object> getReqMap() {
		return reqMap;
	}

	public Map<String, Object> getRespMap() {
		return respMap;
	}

	public String getScheme() {
		return scheme;
	}

	public String getServerName() {
		return serverName;
	}

	public int getServerPort() {
		return serverPort;
	}

	public String getContextPath() {
		return contextPath;
	}

	public String getRootUrl() {
		StringBuilder sb = new StringBuilder();
		sb.append(getScheme()).append("://");
		sb.append(getServerName()).append(":");
		sb.append(getServerPort()).append("/");
		sb.append(getContextPath());
		return sb.toString();
	}

	public String getRequestURI() {
		return requestURI;
	}

	public String getPath() {
		return path;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	// --------- Cookie ---------
	public Cookie[] getCookies() {
		return req.getCookies();
	}

	public Cookie getCookieByName(String name) {
		Cookie[] cookies = getCookies();
		if (cookies == null) {
			return null;
		}
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(name)) {
				return cookie;
			}
		}
		return null;
	}

	public String getCookieValueByName(String name) {
		Cookie cookie = getCookieByName(name);
		if (cookie == null) {
			return null;
		} else {
			return cookie.getValue();
		}
	}

	/**
	 * @param name
	 * @param value
	 * @param maxAge
	 *            单位秒
	 * @param path
	 */
	public Cookie setCookieValueByName(String name, String value, int maxAge, String path) {
		Cookie cookie = getCookieByName(name);
		if (cookie == null) {
			cookie = new Cookie(name, value);
			resp.addCookie(cookie);
		}
		cookie.setValue(value);
		cookie.setMaxAge(maxAge);
		if (path != null) {
			cookie.setPath(path);
		}
		return cookie;
	}

}
