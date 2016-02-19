package com.action;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import com.lwzh.tool.IdTool;
import com.lwzh.tool.URLPicLoader;
import com.lwzh.web.BaseAction;
import com.lwzh.web.annotation.P;

public class ToolsAction extends BaseAction {

	private static Map<String, Object> staticMap = new HashMap<String, Object>();
	private long min_seconds = 60l;

	public void loadPic(@P("path") String path) {
		String retPath;
		try {
			retPath = URLPicLoader.download(path, "WX", IdTool.uuid(), IdTool.uuid() + ".png");
			System.err.println(retPath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 会员注册发送短信验证码
	 * @param mobile手机号
	 * @param seconds失效秒数
	 */
	public void sendSMS(@P("mobile") String mobile, @P("seconds") String seconds) {
		HttpServletRequest request = getContext().getReq();
		checkMap();
		long lseconds = isEmpty(seconds) ? min_seconds : Long.parseLong(seconds);
		lseconds = min_seconds >= lseconds ? min_seconds : lseconds;
		Object Smobile = staticMap.get(mobile);
		if (Smobile == null) {
			Map<String, Object> map = new HashMap<String, Object>();
			String randNum = this.getRandNum(6);
//			boolean flag = SendTemplateSMSTool.sendSMS(mobile, randNum, seconds);
						boolean flag = true;
			int code = flag ? 0 : -1;
			map.put("randNum", randNum);
			if (flag) {
				Map<String, Object> inmap = new HashMap<String, Object>();
				inmap.put("seconds", lseconds);
				inmap.put("currentTimeMillis", System.currentTimeMillis());
				staticMap.put(mobile, inmap);
				request.getSession().setAttribute("randNum", randNum);
			}
			setRespData(code, null, map);
			return;
		} else {
			setRespData(-1, null, null);
			return;
		}
	}

	public void voiceVerify(@P("mobile") String mobile) {/*
		checkMap();
		Object Smobile = staticMap.get(mobile);
		if (Smobile == null) {
			Map<String, Object> map = new HashMap<String, Object>();
			String randNum = this.getRandNum(6);
			boolean flag = VoiceVerifyTool.voiceVerify(mobile, randNum);
			//		boolean flag = true;
			int code = flag ? 0 : -1;
			map.put("randNum", randNum);
			if (flag) {
				Map<String, Object> inmap = new HashMap<String, Object>();
				inmap.put("currentTimeMillis", System.currentTimeMillis());
				staticMap.put(mobile, inmap);
			}
			setRespData(code, null, map);
			return;
		} else {
			setRespData(-1, null, null);
			return;
		}
	*/}

	private void checkMap() {
		Iterator<Entry<String, Object>> it = staticMap.entrySet().iterator();
		long currentTimeMillis = System.currentTimeMillis();
		while (it.hasNext()) {
			Entry<String, Object> entry = it.next();
			//			String key = entry.getKey();
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) entry.getValue();
			long seconds = map.get("seconds") != null ? (Long) map.get("seconds") : min_seconds;
			long ScurrentTimeMillis = (Long) map.get("currentTimeMillis");
			if ((currentTimeMillis - ScurrentTimeMillis) / 1000 >= seconds) {
				it.remove();
			}
		}
	}

	public String getRandNum(int charCount) {
		String charValue = "";
		for (int i = 0; i < charCount; i++) {
			char c = (char) (randomInt(0, 10) + '0');
			charValue += String.valueOf(c);
		}
		return charValue;
	}

	public int randomInt(int from, int to) {
		Random r = new Random();
		return from + r.nextInt(to - from);
	}

	public static void main(String[] args) {
		/*long start = System.currentTimeMillis();
		System.err.println(start);
		try {
			Thread.sleep(10000);
			long end = System.currentTimeMillis();
			System.err.println(end);
			System.err.println((end - start) / 1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		long a = 60l;
		long b = 61l;
		System.err.println(a >= b);
	}

}
