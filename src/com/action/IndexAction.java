package com.action;

import com.lwzh.web.BaseAction;

public class IndexAction extends BaseAction {

	public void index() {
		setRedirectUrl(getContextPath() + "/login/login.html");
		return;
	}

	public void reg_index() {
	}

}
