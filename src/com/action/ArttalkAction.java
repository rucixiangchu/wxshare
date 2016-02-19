package com.action;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.lwzh.web.BaseAction;
import com.lwzh.web.annotation.P;
import com.wx.study.AccessTokenThread;
import com.wx.study.HttpClient;
import com.wx.study.WeChat;

public class ArttalkAction extends BaseAction {

	/**
	* @Description:分享艺论页面  
	* @author maofangchao
	* @param arttalk_id
	* @throws
	 */
	@P(emptyValueAsNull = "false")
	public void paimai(@P("code") String code) {
		HttpServletRequest request = getContext().getReq();
		if (!isEmpty(code)) {
			String nonce_str = UUID.randomUUID().toString();
			String timestamp = Long.toString(System.currentTimeMillis() / 1000);
			String requesturl = request.getRequestURL().toString();
			String requesturi = request.getRequestURI();
			String queryString = request.getQueryString();
			String url = requesturl + (isEmpty(queryString) ? "" : "?" + queryString);
			StringBuffer link = new StringBuffer();
			link.append(requesturl.replaceFirst(requesturi, "/share/share?path=")).append(requesturi);
			Map<String, Object> art = new HashMap<String, Object>();
			art.put("signature", WeChat.getJsSignature(AccessTokenThread.jsapi_ticket, nonce_str, timestamp, url));
			art.put("timestamp", timestamp);
			art.put("noncestr", nonce_str);
			art.put("link", link.toString());
			art.put("imgUrl", requesturl.replaceFirst(requesturi, "/arttalk/images/fenxiang_logo.png"));
			setRespAttr("art", art);
		} else {
			return;
		}
	}

	public String getOpenId(String code) {
		String open_id_url = String.format(WeChat.AUTHORIZE_OPEN_ID_URL, WeChat.APPID, WeChat.APPSECRET, code);
		String result = HttpClient.httpGet(open_id_url);
		return result;
	}

}
