package com.lwzh.web.session;

import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lwzh.Constant;
import com.lwzh.web.Context;
import com.lwzh.web.Session;

public class MemorySessionStorage implements SessionStorage {

	@Override
	public Session getSession(HttpServletRequest request, HttpServletResponse response, String key) {
		Session session = (Session) request.getSession(true).getAttribute(Constant.WEB.SESSION);
		if (session == null) {
			Map<String, Object> data = new HashMap<String, Object>();
			session = new Session(key, data, this);
			request.getSession().setAttribute(Constant.WEB.SESSION, session);
		}
		return session;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String name = evt.getPropertyName();
		Session session = (Session) evt.getSource();
		String id = session.getId();
		if ("invalidate".equals(name)) {
			Context context = Context.getContext();
			if (context == null) {
				throw new RuntimeException("context=" + context + ", session.id=" + id);
			}
			context.getReq().getSession().invalidate();
		} else {
			// In memory, do nothing.
		}
	}

}
