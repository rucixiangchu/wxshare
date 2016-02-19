package com.wx.study;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.Formatter;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class WeChat {

	private static final Logger logger = LoggerFactory.getLogger(WeChat.class);

	/**
	 * 公众号APPID
	 */
	public static final String APPID = "wxdc0b406649da2a03";
	/**
	 * 公众号密匙
	 */
	public static final String APPSECRET = "0887fcfab4d969af63d9da416a739070";

	public static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";// 获取access

	public static final String JSAPI_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=%s&type=jsapi";// 获取jsapi_ticket
	
	/**
	 * 用户同意授权，获取code
	 */
	public static final String AUTHORIZE_SNSAPI_BASE_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_base&state=123#wechat_redirect";
	
	public static final String AUTHORIZE_SNSAPI_USERINFO_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_userinfo&state=123#wechat_redirect";
	/**
	 * 获取openid
	 */
	public static final String AUTHORIZE_OPEN_ID_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
	
	/**
	 * 接口说明创建获取ACCESS_TOKEN的URL
	 * 修改者名字   maofangchao
	 * 修改日期   2016年1月24日
	 * @return
	 * @return String
	 */
	public static String createAccessTokenUrl() {
		return String.format("%s?grant_type=client_credential&appid=%s&secret=%s", ACCESS_TOKEN_URL, APPID, APPSECRET);
	}

	public static String createJsapiTicketUrl(String access_token) {
		return String.format(JSAPI_TICKET_URL, access_token);
	}

	public static JsonObject getAccessToken() {
		String result = httpGet(createAccessTokenUrl());
		JsonParser jsonparer = new JsonParser();// 初始化解析json格式的对象
		return jsonparer.parse(result).getAsJsonObject();
	}

	public static JsonObject getJsapiTicket(String access_token) {
		String result = httpGet(createJsapiTicketUrl(access_token));
		JsonParser jsonparer = new JsonParser();// 初始化解析json格式的对象
		return jsonparer.parse(result).getAsJsonObject();
	}

	public static String getJsSignature(String jsapi_ticket, String noncestr, String timestamp, String url) {
		/*String nonce_str = UUID.randomUUID().toString();  
		String timestamp = Long.toString(System.currentTimeMillis() / 1000);  */
		String signature_url = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + noncestr + "&timestamp=" + timestamp + "&url=" + url;
		String signature = null;
		//        String signature = QEncodeUtil.sha1(s);  
		try {
			//			MessageDigest md = MessageDigest.getInstance("SHA-1");
			// 将三个参数字符串拼接成一个字符串进行sha1加密  
			//			byte[] digest = md.digest(signature_url.getBytes());

			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(signature_url.getBytes("UTF-8"));
			signature = byteToHex(crypt.digest());
		} catch (Exception e) {
			logger.error(e.toString(), e);
		}
		return signature;
	}

	/**
	 * 接口说明 发起GET请求
	 * 修改者名字   maofangchao
	 * 修改日期   2016年1月24日
	 * @param url get请求的URL
	 */
	@SuppressWarnings("finally")
	public static String httpGet(String url) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet get = new HttpGet(url);
		String responseContent = null;
		try {
			HttpResponse response = (HttpResponse) httpclient.execute(get);
			HttpEntity entity = response.getEntity();
			responseContent = EntityUtils.toString(entity, "UTF-8");
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			}
		} catch (Exception e) {
			logger.error(e.toString(), e);
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				logger.error(e.toString(), e);
			}
			return responseContent;
		}
	}

	private static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}

	/** 
	 * 将字节数组转换为十六进制字符串 
	 *  
	 * @param byteArray 
	 * @return 
	 */
	private static String byteToStr(byte[] byteArray) {
		String strDigest = "";
		for (int i = 0; i < byteArray.length; i++) {
			strDigest += byteToHexStr(byteArray[i]);
		}
		return strDigest;
	}

	/** 
	 * 将字节转换为十六进制字符串 
	 *  
	 * @param mByte 
	 * @return 
	 */
	private static String byteToHexStr(byte mByte) {
		char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		char[] tempArr = new char[2];
		tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
		tempArr[1] = Digit[mByte & 0X0F];

		String s = new String(tempArr);
		return s;
	}

}
