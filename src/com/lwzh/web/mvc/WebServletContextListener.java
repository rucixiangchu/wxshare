package com.lwzh.web.mvc;

import httl.spi.loaders.ServletLoader;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

public class WebServletContextListener implements ServletContextListener {

	private static final Logger logger = LoggerFactory.getLogger(WebServletContextListener.class);

	@Override
	public void contextInitialized(ServletContextEvent event) {
		logger.info("WebServletContextListener.contextInitialized");
		// redirect jdk log to slf4j
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();
		ServletLoader.setServletContext(event.getServletContext());
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		logger.info("WebServletContextListener.contextDestroyed");
	}

}
