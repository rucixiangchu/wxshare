package com.lwzh.tool;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class MathsTool {
	
	private static DecimalFormat formate = new DecimalFormat();
	
	/**
	* @Description:  截取小数点后两位
	* @author maofangchao
	* @param number需要截取的double参数
	 */
	public static String getTwoDic(double number) {
		formate.setMaximumFractionDigits(2);
		formate.setGroupingSize(0);
		formate.setRoundingMode(RoundingMode.FLOOR);
		return formate.format(number);
	}

	/**
	* @Description:科学计数法转小数  
	* @author maofangchao
	* @param number
	* @return
	* @throws
	 */
	public static String getDecimals(double number) {
		BigDecimal decimals = new BigDecimal(number);
		return decimals.toPlainString();
	}
	
	public static String subDouble(double number){
		return getTwoDic(Double.parseDouble(getDecimals(number)));
	}

}
