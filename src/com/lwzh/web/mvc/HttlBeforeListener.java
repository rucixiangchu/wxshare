package com.lwzh.web.mvc;

import httl.Context;
import httl.spi.Listener;
import httl.spi.loaders.ServletLoader;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.decorators.LruCache;
import org.apache.ibatis.cache.impl.PerpetualCache;

import com.lwzh.Constant;
import com.lwzh.tool.FileTool;
import com.lwzh.web.Session;

public class HttlBeforeListener implements Listener {

	private Cache i18nCache;

	public Cache getI18nCache() {
		if (i18nCache == null) {
			synchronized (this) {
				if (i18nCache == null) {
					i18nCache = new LruCache(new PerpetualCache("i18nCache"));
				}
			}
		}
		return i18nCache;
	}

	private Map<String, String> getI18n(String path, Session session, Context context) throws IOException {
		String lang = session.getI18n();
		String i18nFile = path + "." + lang + ".i18n";
		boolean reloadable = context.getEngine().getProperty("reloadable", true);
		ServletContext servletContext = ServletLoader.getServletContext();
		if (reloadable) {
			Map<String, String> i18n = FileTool.readKvFile(servletContext.getResourceAsStream(i18nFile));
			return i18n;
		} else {
			@SuppressWarnings("unchecked")
			Map<String, String> i18n = (Map<String, String>) getI18nCache().getObject(i18nFile);
			if (i18n == null) {
				i18n = FileTool.readKvFile(servletContext.getResourceAsStream(i18nFile));
				if (i18n != null) {
					getI18nCache().putObject(i18nFile, i18n);
				}
			}
			return i18n;
		}
	}

	@Override
	public void render(Context context) throws IOException, ParseException {
		String templateFileName = context.getTemplate().getName();
		Session session = (Session) context.get(Constant.WEB.SESSION);
		@SuppressWarnings("unchecked")
		Map<String, String> i18nMap = (Map<String, String>) context.get(Constant.WEB.I18N);
		Map<String, String> map = getI18n(templateFileName, session, context);
		if (map != null) {
			i18nMap.putAll(map);
		}
	}

}
