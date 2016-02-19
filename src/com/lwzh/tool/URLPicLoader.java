package com.lwzh.tool;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import com.lwzh.action.FileSystemAction;
import com.lwzh.web.Context;

public class URLPicLoader {

	public static void main(String[] args) throws Exception {
		String path = download("http://wx.qlogo.cn/mmopen/g3MonUZtNHkdmzicIlibx6iaFqAc56vxLSUfpb6n5WKSYVY0ChQKkiaJSgQ1dZuTOgvLLrhJbERQQ4eMsv84eavHiaiceqxibJxCfHe/0", "WX",
				IdTool.uuid(), IdTool.uuid() + ".png");
		System.err.println(path);

	}

	/**
	* @Description:  下载文件到本地
	* @author maofangchao
	* @param urlString被下载的文件地址
	* @param filename本地文件名
	* @throws Exception各种异常
	* @throws
	 */
	public static String download(String urlString, String fieldName, String source_id, String fileName) throws Exception {
		File rootDir = FileSystemAction.getRootDir(Context.getContext());
		File fsRootDir = FileSystemAction.getFsRootDir(Context.getContext());
		File file = new File(fsRootDir, "upload/" + fieldName + "/" + source_id + "/" + IdTool.uuid() + "/" + fileName).getCanonicalFile();
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		System.err.println(file.toURI().toString());
		URL url = new URL(urlString);// 构造URL
		URLConnection con = url.openConnection();// 打开连接
		InputStream is = con.getInputStream();// 输入流
		byte[] bs = new byte[1024];// 1K的数据缓冲
		int len;// 读取到的数据长度
		OutputStream os = new FileOutputStream(file);// 输出的文件流
		while ((len = is.read(bs)) != -1) {// 开始读取
			os.write(bs, 0, len);
		}
		os.close();// 完毕，关闭所有链接
		is.close();
		return file.toURI().toString().substring(rootDir.toURI().toString().length() - 1);// 数据库中保存原图地址
	}

}
