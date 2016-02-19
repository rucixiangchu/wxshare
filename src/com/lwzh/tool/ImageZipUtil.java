package com.lwzh.tool;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.File;
import java.io.FileOutputStream;

import javax.imageio.ImageIO;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class ImageZipUtil {

	/** 
	 * 先截图原文件，再压缩
	 * @param oldFile  要进行剪切压缩的文件 
	 * @param newFile  新文件 
	 * @param width  宽度 //设置宽度时（高度传入0，等比例缩放） 
	 * @param height 高度 //设置高度时（宽度传入0，等比例缩放） 
	 * @param quality 质量 
	 * @return 返回压缩后的文件的全路径 
	 */
	public static String cutImageZip(File oldFile, File newFile, int destWidth, int destHeight, float quality) {
		if (oldFile == null) {
			return null;
		}
		try {
			BufferedImage srcFile = ImageIO.read(oldFile);
			int srcWidth = srcFile.getWidth();
			int srcHeight = srcFile.getHeight();
			if (srcWidth > destWidth || srcHeight > destHeight) {
				int frow = 0;
				int froh = 0;
				if (srcWidth > srcHeight) {// 计算切片的横向和纵向数量
					int res = srcWidth - srcHeight;
					int mul = res / 2;
					frow = srcWidth - mul - srcHeight;
				} else if (srcHeight > srcWidth) {
					int res = srcHeight - srcWidth;
					int mul = res / 2;
					froh = srcHeight - mul - srcWidth;
				}
				if (frow != 0 || froh != 0) {
					ImageFilter cropFilter = new CropImageFilter(frow, froh, srcWidth > srcHeight ? srcHeight : srcWidth, srcWidth < srcHeight ? srcWidth : srcHeight);
					Image img = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(srcFile.getSource(), cropFilter));
					fileUpload(img, destWidth, destHeight, newFile, quality);
				} else {
					fileUpload(srcFile, destWidth, destHeight, newFile, quality);
				}
			} else {
				//长宽都小于300
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newFile.getAbsolutePath();
	}

	public static void fileUpload(Image img, int destWidth, int destHeight, File newFile, float quality) {
		FileOutputStream out;
		try {
			BufferedImage tag = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_RGB);
			tag.getGraphics().drawImage(img, 0, 0, destWidth, destHeight, null);
			out = new FileOutputStream(newFile);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			JPEGEncodeParam jep = JPEGCodec.getDefaultJPEGEncodeParam(tag);
			jep.setQuality(quality, true);
			encoder.encode(tag, jep);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}