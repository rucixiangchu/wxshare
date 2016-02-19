package com.lwzh.web;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Map;

import com.lwzh.tool.JsonTool;

public class Session {

	private final PropertyChangeSupport propertyChangeSupport;
	private final String id;
	private final Map<String, Object> data;

	// private AccessService accessService;

	public Session(String id, Map<String, Object> data, PropertyChangeListener propertyChangeListener) {
		this.propertyChangeSupport = new PropertyChangeSupport(this);
		this.propertyChangeSupport.addPropertyChangeListener(propertyChangeListener);
		this.id = id;
		this.data = data;
	}

	public String getId() {
		return id;
	}

	public Object get(String key) {
		return data.get(key);
	}

	public Object put(String key, Object newValue) {
		Object oldValue = data.put(key, newValue);
		propertyChangeSupport.firePropertyChange(key, oldValue, newValue);
		return oldValue;
	}

	// public void setAccessService(AccessService accessService) {
	// this.accessService = accessService;
	// }
	//
	// public boolean checkDomAccess(String dom) {
	// return accessService.checkDomAccess(getUserId(), dom);
	// }

	/*
	 * public boolean cda(String dom) { return checkDomAccess(dom); }
	 */

	public void invalidate() {
		data.clear();
		propertyChangeSupport.firePropertyChange("invalidate", null, null);
	}

	private static final String i18n = "i18n";

	public String getI18n() {
		return (String) get(Session.i18n);
	}

	public void setI18n(String i18n) {
		put(Session.i18n, i18n);
	}

	private static final String loginName = "loginName";

	public String getLoginName() {
		return (String) get(Session.loginName);
	}

	public void setLoginName(String loginName) {
		put(Session.loginName, loginName);
	}

	private static final String userId = "userId";

	public String getUserId() {
		return (String) get(Session.userId);
	}

	public void setUserId(String userId) {
		put(Session.userId, userId);
	}

	private static final String mobile = "mobile";

	public String getMobile() {
		return (String) get(Session.mobile);
	}

	public void setMobile(String mobile) {
		put(Session.mobile, mobile);
	}

	private static final String captchaAnswer = "captchaAnswer";

	public String getCaptchaAnswer() {
		return (String) get(Session.captchaAnswer);
	}

	public void setCaptchaAnswer(String captchaAnswer) {
		put(Session.captchaAnswer, captchaAnswer);
	}

	public boolean isLogin() {
		return getLoginName() != null;
	}

	public String dataToJson() {
		return JsonTool.toJson(data);
	}

	public void fromJson(String json) {
		@SuppressWarnings("unchecked")
		Map<String, Object> map = (Map<String, Object>) JsonTool.parse(json);
		for (String key : map.keySet()) {
			Object value = map.get(key);
			put(key, value);
		}
	}

	@Override
	public String toString() {
		return "Session [id=" + id + ", data=" + data + "]";
	}

}
