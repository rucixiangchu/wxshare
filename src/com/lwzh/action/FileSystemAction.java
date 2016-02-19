package com.lwzh.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lwzh.Constant;
import com.lwzh.tool.IdTool;
import com.lwzh.tool.ImageZipUtil;
import com.lwzh.web.BaseAction;
import com.lwzh.web.Context;
import com.lwzh.web.annotation.P;

public class FileSystemAction extends BaseAction {

	private static final Logger logger = LoggerFactory.getLogger(FileSystemAction.class);

	public static File getRootDir(Context context) {
		try {
			File rootDir = new File(context.getReq().getServletContext().getRealPath("/")).getCanonicalFile();
			return rootDir;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static File getFsRootDir(Context context) {
		try {
			File rootDir = getRootDir(context);
			File fsRootDir = new File(rootDir, Constant.FS.FS_ROOT_DIR).getCanonicalFile();
			if (!fsRootDir.exists()) {
				fsRootDir.mkdirs();
			}
			return fsRootDir;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void upload() {
		HttpServletRequest request = getContext().getReq();
		System.err.println(FileSystemAction.getRootDir(getContext()));
		System.err.println("----" + request.getContextPath());
		System.err.println(request.getRemoteHost());
	}
	public void json() {
		HttpServletRequest request = getContext().getReq();
		System.err.println(FileSystemAction.getRootDir(getContext()));
		System.err.println("----" + request.getContextPath());
		System.err.println(request.getRemoteHost());
	}

	/**
	* @Description:  
	* @author maofangchao
	* @param temp是否是临时文件
	* @param is_zip是否生成缩略图
	* @throws
	 */
	public void uploadFile(@P("temp") Boolean temp, @P("is_zip") Boolean is_zip) {
		temp = temp == null ? false : temp;
		is_zip = is_zip == null ? true : is_zip;
		HttpServletRequest request = getContext().getReq();
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (!isMultipart) {
			setRespData(-1, null, null);
			return;
		}
		List<String> fileList = new ArrayList<String>();
		try {
			ServletFileUpload upload = new ServletFileUpload();
			FileItemIterator iter = upload.getItemIterator(request);
			while (iter.hasNext()) {
				StringBuffer sb = new StringBuffer();
				FileItemStream item = iter.next();
				String fieldName = item.getFieldName();
				InputStream stream = item.openStream();
				if (item.isFormField()) {
					logger.debug("Form field : " + fieldName + "=" + Streams.asString(stream));
					continue;
				}

				String fileName = item.getName();
				logger.debug("File field : " + fieldName + "=" + fileName);
				if (temp) {
					fieldName = "temp/" + fieldName;
				}
				File rootDir = FileSystemAction.getRootDir(getContext());
				File fsRootDir = FileSystemAction.getFsRootDir(getContext());
				File saveFile = new File(fsRootDir, "upload/" + fieldName + "/" + IdTool.uuid() + "/" + fileName).getCanonicalFile();
				if (!saveFile.getParentFile().exists()) {
					saveFile.getParentFile().mkdirs();
				}
				logger.debug("Save file to : " + saveFile.getAbsolutePath());
				FileOutputStream fos = new FileOutputStream(saveFile);
				IOUtils.copy(stream, fos);
				fos.close();
				String filePath = saveFile.toURI().toString().substring(rootDir.toURI().toString().length() - 1);
				sb.append(filePath);
				if (is_zip) {
					File newFile = new File(saveFile.getParentFile().toPath() + "\\slt_" + System.currentTimeMillis() + saveFile.getName()); // 将要转换出的小图文件);
					ImageZipUtil.cutImageZip(saveFile, newFile, 300, 300, 0.7f);
					String sltPath = newFile.toURI().toString().substring(rootDir.toURI().toString().length() - 1);// 数据库中保存缩略图地址
					sb.append(",").append(sltPath);
				}
				fileList.add(sb.toString());
				stream.close();
			}
		} catch (Exception e) {
			logger.error(e.toString(), e);
			setRespData(-1, null, null);
			return;
		}

		setRespAttr("fileList", fileList);
		setRespData(0, null, null);
		return;
	}
}
