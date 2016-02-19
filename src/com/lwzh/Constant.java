package com.lwzh;

public class Constant {

	public static final String CHARSET_UTF8 = "UTF-8";

	public static final long SECOND = 1000;
	public static final long MINUTE = 60 * SECOND;
	public static final long HOUR = 60 * MINUTE;
	public static final long DAY = 24 * HOUR;
	public static final long WEEK = 7 * DAY;
	public static final long MONTH = 30 * DAY;
	public static final long YEAR = 365 * DAY;

	public static final class WEB {
		public static final String CONTEXT = "context";
		public static final String SESSION = "session";

		/**
		 * 删除Cookie
		 */
		public static final int COOKIE_EXPIRY_DELETE = 0;
		/**
		 * 关闭浏览器失效
		 */
		public static final int COOKIE_EXPIRY_DEFAULT = -1;
		public static final int COOKIE_EXPIRY_ONE_DAY = 60 * 60 * 24;
		public static final int COOKIE_EXPIRY_ONE_WEEK = COOKIE_EXPIRY_ONE_DAY * 7;
		public static final int COOKIE_EXPIRY_ONE_MONTH = COOKIE_EXPIRY_ONE_DAY * 30;
		public static final int COOKIE_EXPIRY_ONE_YEAR = COOKIE_EXPIRY_ONE_DAY * 365;

		public static final String I18N = "i18n";
		public static final String I18N_zh_CN = "zh_CN";
		public static final String I18N_en_US = "en_US";

		public static final String RESPONSE_CODE = "code";
		public static final String RESPONSE_MSG = "msg";
		public static final String RESPONSE_DATA = "data";
		public static final String PAGE = "page";

	}

	public static final class FS {
		public static final String FS_ROOT_DIR = "fs_root";
		public static final String FS_PAN_DIR = "fs_pan";
	}

}
