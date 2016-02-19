package com.lwzh.action;

import com.lwzh.web.BaseAction;

public class SsoAction extends BaseAction {/*

	private SsoService ssoService;

	public void setSsoService(SsoService ssoService) {
		this.ssoService = ssoService;
	}

	@Access(needLogin = true, needAC = false)
	public void to(@P("url") String url) {
		Session session = getSession();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("session_json", session.dataToJson());
		String sso_sign = ssoService.buildSign(params);

		Map<String, Object> reqMap = getReqMap();
		for (String k : reqMap.keySet()) {
			if (k.equals("url")) {
				continue;
			}
			String v = (String) reqMap.get(k);
			if (url.contains("?")) {
				url += "&" + k + "=" + v;
			} else {
				url += "?" + k + "=" + v;
			}
		}
		if (url.contains("?")) {
			url += "&sso_sign=" + sso_sign;
		} else {
			url += "?sso_sign=" + sso_sign;
		}
		setRedirectUrl(url);
	}

	@Access(needLogin = false, needAC = false)
	public void login(@P("sso_sign") String sso_sign) {
		Map<String, Object> params = ssoService.checkSign(sso_sign);
		if (isEmpty(params)) {
			return;
		}

		Session session = getSession();
		String session_json = (String) params.get("session_json");
		session.fromJson(session_json);
	}

*/}
