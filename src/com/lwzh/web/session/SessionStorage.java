package com.lwzh.web.session;

import java.beans.PropertyChangeListener;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lwzh.web.Session;

public interface SessionStorage extends PropertyChangeListener {

	public Session getSession(HttpServletRequest request, HttpServletResponse response, String key);

}
