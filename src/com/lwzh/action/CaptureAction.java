package com.lwzh.action;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Map;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lwzh.tool.IdTool;
import com.lwzh.web.BaseAction;
import com.lwzh.web.annotation.P;

public class CaptureAction extends BaseAction {

	private static final Logger logger = LoggerFactory.getLogger(CaptureAction.class);

	private String getPhantomJSExeFile() {
		String os_name = System.getProperty("os.name").toLowerCase();
		if (os_name.contains("windows")) {
			return "phantomjs.exe";
		} else if (os_name.contains("linux")) {
			return "phantomjs";
		} else {
			throw new RuntimeException("Illegal os_name : " + os_name);
		}
	}

	public void rasterize(@P("url") String url, @P("width") String width, @P("height") String height) {
		if (isEmpty(url)) {
			setRespData(-1, null, null);
			return;
		}
		if (width == null) {
			width = "1280px";
		}
		if (height == null) {
			height = "720px";
		}
		Map<String, Object> reqMap = getReqMap();
		for (String k : reqMap.keySet()) {
			if (k.equals("url") || k.equals("width") || k.equals("height")) {
				continue;
			}
			String v = (String) reqMap.get(k);
			if (url.contains("?")) {
				url += "&" + k + "=" + v;
			} else {
				url += "?" + k + "=" + v;
			}
		}

		try {
			File phantomjsHomeDir = new File(FileSystemAction.getRootDir(getContext()), "WEB-INF/classes/phantomjs/");
			File exeFile = new File(phantomjsHomeDir, "bin/" + getPhantomJSExeFile());
			File jsFile = new File(phantomjsHomeDir, "js/rasterize.js");
			File outFile = new File(FileSystemAction.getFsRootDir(getContext()), "capture/temp/" + IdTool.uuid() + ".png");

			CommandLine cmdLine = new CommandLine(exeFile.getCanonicalPath());
			cmdLine.addArgument(jsFile.getCanonicalPath());
			cmdLine.addArgument(url);
			cmdLine.addArgument(outFile.getCanonicalPath());
			// cmdLine.addArgument(width + "*" + height);
			cmdLine.addArgument(width);

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ByteArrayOutputStream err = new ByteArrayOutputStream();
			DefaultExecutor executor = new DefaultExecutor();
			executor.setStreamHandler(new PumpStreamHandler(out, err));
			int exitValue = executor.execute(cmdLine);
			logger.debug("exitValue=" + exitValue);
			logger.debug("out=" + out.toString());
			logger.debug("err=" + err.toString());
			logger.debug("outFile=" + outFile.getCanonicalPath());

			String redirectUrl = outFile.toURI().toString().substring(FileSystemAction.getRootDir(getContext()).toURI().toString().length() - 1);
			logger.debug("redirectUrl=" + redirectUrl);
			setRedirectUrl(redirectUrl);
		} catch (Exception e) {
			logger.error(e.toString(), e);
			setRespData(-1, null, null);
		}
	}

}
