package com.wx.study;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;

public class AccessTokenThread implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(AccessTokenThread.class);

	public static String access_token = null;

	public static String jsapi_ticket = null;

	@Override
	public void run() {
		while (true) {
			try {
				JsonObject json = WeChat.getAccessToken();
				if (null != json) {
					access_token = json.get("access_token").getAsString();
					int expires_in = json.get("expires_in").getAsInt();
					JsonObject ticket = WeChat.getJsapiTicket(access_token);
					jsapi_ticket = ticket.get("ticket").getAsString();
					logger.info("获取access_token成功，有效时长{" + expires_in + "}秒access_token=" + access_token, expires_in, access_token);
					logger.info("获取jsapi_ticket成功，有效时长{" + expires_in + "}秒jsapi_ticket=" + jsapi_ticket, expires_in, jsapi_ticket);
					Thread.sleep((expires_in - 200) * 1000);// 休眠7000秒
				} else {
					Thread.sleep(60 * 1000);
				}
			} catch (InterruptedException e) {
				try {
					Thread.sleep(60 * 1000);
				} catch (InterruptedException ie) {
					logger.error(ie.toString(), ie);
				}
				logger.error(e.toString(), e);
			}
		}
	}

}
