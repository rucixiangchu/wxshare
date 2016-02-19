package com.lwzh.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.lwzh.Constant;
import com.lwzh.tool.JsonTool;

public class BaseAction {

	// --------- Context ---------
	public Context getContext() {
		return Context.getContext();
	}

	// --------- Session ---------
	public Session getSession() {
		return getContext().getSession();
	}

	// --------- reqMap ---------
	public Map<String, Object> getReqMap() {
		return getContext().getReqMap();
	}

	public Object getReqAttr(String key) {
		Map<String, Object> reqMap = getReqMap();
		return reqMap.get(key);
	}

	public String getReqAttr_String(String key) {
		Object value = getReqAttr(key);
		if (value == null) {
			return null;
		} else {
			return value.toString();
		}
	}

	public String[] getReqAttr_StringArray(String key) {
		Object value = getReqAttr(key);
		if (value == null) {
			return null;
		} else {
			return value.toString().split(",");
		}
	}

	public Map<?, ?> getReqAttr_JsonMap(String key) {
		Object value = getReqAttr(key);
		if (value == null) {
			return null;
		} else {
			return (Map<?, ?>) JsonTool.parse(value.toString());
		}
	}

	public Integer getReqAttr_Integer(String key) {
		Object value = getReqAttr(key);
		if (value == null) {
			return null;
		} else {
			return Integer.valueOf(value.toString());
		}
	}

	public Long getReqAttr_Long(String key) {
		Object value = getReqAttr(key);
		if (value == null) {
			return null;
		} else {
			return Long.valueOf(value.toString());
		}
	}

	public Double getReqAttr_Double(String key) {
		Object value = getReqAttr(key);
		if (value == null) {
			return null;
		} else {
			return Double.valueOf(value.toString());
		}
	}

	public Date getReqAttr_Date(String key, String pattern) throws ParseException {
		Object value = getReqAttr(key);
		if (value == null) {
			return null;
		} else {
			return new SimpleDateFormat(pattern).parse(value.toString());
		}
	}

	public void setReqAttr(String key, Object value) {
		Map<String, Object> reqMap = getReqMap();
		reqMap.put(key, value);
	}

	// --------- respMap ---------
	public Map<String, Object> getRespMap() {
		return getContext().getRespMap();
	}

	public Object getRespAttr(String key) {
		Map<String, Object> respMap = getRespMap();
		return respMap.get(key);
	}

	public void setRespAttr(String key, Object value) {
		Map<String, Object> respMap = getRespMap();
		respMap.put(key, value);
	}

	public void setRespData(int code, String msg, Object data) {
		Map<String, Object> respMap = getRespMap();
		respMap.put(Constant.WEB.RESPONSE_CODE, code);
		respMap.put(Constant.WEB.RESPONSE_MSG, msg);
		respMap.put(Constant.WEB.RESPONSE_DATA, data);
	}

	public void setPage(Map<String, Object> page) {
		Map<String, Object> respMap = getRespMap();
		respMap.put(Constant.WEB.PAGE, page);
	}

	public Map<String, Object> getPage() {
		Map<String, Object> respMap = getRespMap();
		@SuppressWarnings("unchecked")
		Map<String, Object> page = (Map<String, Object>) respMap.get(Constant.WEB.PAGE);
		return page;
	}

	// --------- others ---------
	public String getScheme() {
		return getContext().getScheme();
	}

	public String getServerName() {
		return getContext().getServerName();
	}

	public int getServerPort() {
		return getContext().getServerPort();
	}

	public String getContextPath() {
		return getContext().getContextPath();
	}

	public String getRedirectUrl() {
		return getContext().getRedirectUrl();
	}

	public void setRedirectUrl(String redirectUrl) {
		getContext().setRedirectUrl(redirectUrl);
	}

	public String getRemoteAddr() {
		return getContext().getReq().getRemoteAddr();
	}

	public boolean checkCaptcha(String captchaAnswer) {
		if (isEmpty(captchaAnswer)) {
			return false;
		}

		Session session = getSession();
		String answer = session.getCaptchaAnswer();

		boolean ret = captchaAnswer.equalsIgnoreCase(answer);
		session.setCaptchaAnswer(null);
		return ret;
	}

	// --------- tools ---------
	public boolean isEmpty(String s) {
		if (s == null || "".equals(s) || "null".equals(s)) {
			return true;
		}
		return s.trim().isEmpty();
	}

	public boolean isEmpty(String[] ss) {
		if (ss == null) {
			return true;
		}
		return ss.length == 0;
	}

	public boolean isEmpty(Map<?, ?> map) {
		if (map == null) {
			return true;
		}
		return map.isEmpty();
	}

	public boolean isEmpty(List<?> list) {
		if (list == null) {
			return true;
		}
		return list.isEmpty();
	}

}
