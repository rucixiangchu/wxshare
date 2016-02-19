package com.lwzh.tool;

import java.util.Arrays;
import java.util.Enumeration;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HttpTool {

	private static final String rn = "\r\n";
	private static final String t = "\t";

	public static String toString(HttpServletRequest req) {
		StringBuilder sb = new StringBuilder();
		sb.append("HttpServletRequest >>> ").append(rn);

		String contextPath = req.getContextPath();
		sb.append("contextPath=").append(contextPath).append(rn);

		String requestURI = req.getRequestURI();
		sb.append("requestURI=").append(requestURI).append(rn);

		sb.append("Headers : ").append(rn);
		Enumeration<String> headers = req.getHeaderNames();
		while (headers.hasMoreElements()) {
			String header = headers.nextElement();
			String value = req.getHeader(header);

			sb.append(t).append(header).append("=");
			sb.append(value);
			sb.append(rn);
		}

		sb.append("Attributes : ").append(rn);
		Enumeration<String> attributes = req.getAttributeNames();
		while (attributes.hasMoreElements()) {
			String attribute = attributes.nextElement();
			Object value = req.getAttribute(attribute);

			sb.append(t).append(attribute).append("=");
			if (value instanceof String[]) {
				sb.append(Arrays.asList((String[]) value));
			} else {
				sb.append(value);
			}
			sb.append(rn);
		}

		sb.append("Parameters : ").append(rn);
		Enumeration<String> parameters = req.getParameterNames();
		while (parameters.hasMoreElements()) {
			String parameter = parameters.nextElement();
			Object value = req.getParameter(parameter);

			sb.append(t).append(parameter).append("=");
			if (value instanceof String[]) {
				sb.append(Arrays.asList((String[]) value));
			} else {
				sb.append(value);
			}
			sb.append(rn);
		}

		sb.append("Cookies : ").append(rn);
		Cookie[] cookies = req.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				sb.append(t).append(cookie.getName()).append("=").append(cookie.getValue());
				sb.append(", ").append(cookie.getDomain());
				sb.append(", ").append(cookie.getPath());
				sb.append(", ").append(cookie.getMaxAge());
				sb.append(rn);
			}
		}

		sb.setLength(sb.length() - 2);
		return sb.toString();
	}

	public static String toString(HttpServletResponse resp) {
		StringBuilder sb = new StringBuilder();
		sb.append("HttpServletResponse <<< ").append(rn);

		int status = resp.getStatus();
		sb.append("status=").append(status).append(rn);

		sb.setLength(sb.length() - 2);
		return sb.toString();
	}

	public static String toString(HttpServletRequest req, HttpServletResponse resp) {
		StringBuilder sb = new StringBuilder();
		sb.append(toString(req)).append(rn);
		sb.append(toString(resp));
		return sb.toString();
	}

}
