package com.lwzh.action;

import java.awt.Color;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.captcha.Captcha;
import nl.captcha.servlet.CaptchaServletUtil;
import nl.captcha.text.renderer.DefaultWordRenderer;
import nl.captcha.text.renderer.WordRenderer;

import com.lwzh.web.BaseAction;
import com.lwzh.web.Context;
import com.lwzh.web.Session;
import com.lwzh.web.annotation.P;

public class EchoAction extends BaseAction {

	public void time(@P("pattern") String pattern) {
		if (pattern == null) {
			setRespData(0, null, System.currentTimeMillis());
		} else {
			String time = new SimpleDateFormat(pattern).format(new Date());
			setRespData(0, null, time);
		}
	}

	private WordRenderer wordRenderer;

	public WordRenderer getWordRenderer() {
		if (wordRenderer == null) {
			List<Font> fonts = new ArrayList<Font>();
			fonts.add(new Font("Courier", Font.BOLD, 30));
			List<Color> colors = new ArrayList<Color>();
			colors.add(Color.black);
			wordRenderer = new DefaultWordRenderer(colors, fonts);
		}
		return wordRenderer;
	}

	public void captcha(@P("width") Integer width, @P("height") Integer height) {
		if (width == null || width <= 0) {
			width = 200;
		}
		if (height == null || height <= 0) {
			height = 50;
		}

		Captcha captcha = new Captcha.Builder(width, height).addText(getWordRenderer()).addBorder().build();
		Session session = getSession();
		session.setCaptchaAnswer(captcha.getAnswer());
		Context context = Context.getContext();
		CaptchaServletUtil.writeImage(context.getResp(), captcha.getImage());
	}

	public void getMyInfo() {
		String remoteAddress = getRemoteAddr();
		setRespAttr("remoteAddress", remoteAddress);
	}

}
