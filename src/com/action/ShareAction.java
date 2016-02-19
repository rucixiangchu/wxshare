package com.action;

import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lwzh.web.BaseAction;
import com.lwzh.web.annotation.P;
import com.wx.study.WeChat;

public class ShareAction extends BaseAction {

	private static final Logger logger = LoggerFactory.getLogger(ShareAction.class);

	public void share(@P("path") String path) {
		String redirect_url = createRedirectUrl(path);
		try {
			String auth_url = String.format(WeChat.AUTHORIZE_SNSAPI_USERINFO_URL, WeChat.APPID, URLEncoder.encode(redirect_url, "UTF-8"));
			getContext().getResp().sendRedirect(auth_url);
		} catch (Exception e) {
			logger.error(e.toString(), e);
		}
	}

	public String createRedirectUrl(String path) {
		StringBuffer redirect_url = new StringBuffer();
		redirect_url.append(getRootUrl()).append(path);
		return redirect_url.toString();
	}

	public String getRootUrl() {
		StringBuilder sb = new StringBuilder();
		sb.append(getScheme()).append("://").append(getServerName());
		return sb.toString();
	}
}
