package com.lwzh.tool;

public class AuctionTool {

	/**
	* @Description:获取竞价阶梯  
	* @author maofangchao
	* @param curprice出价
	 */
	public static double getStepPrice(double curprice) {
		double stepPrice = 100.0;
		if (curprice < 10000.0) {
			stepPrice = 100.0;
		} else if (10000.0 <= curprice && curprice < 50000.0) {
			stepPrice = 200.0;
		} else if (50000.0 <= curprice && curprice < 100000.0) {
			stepPrice = 500.0;
		} else if (100000.0 <= curprice) {
			stepPrice = 1000.0;
		} else {
			stepPrice = 100.0;
		}
		return stepPrice;
	}

}
