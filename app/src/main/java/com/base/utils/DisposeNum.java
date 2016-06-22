package com.base.utils;

import java.math.BigDecimal;

/** --对金额小数点的进位或者舍位-- */
public class DisposeNum {

	// 进位
	public static String numUP(int bit, String money) {
		BigDecimal bigDecimal = new BigDecimal(money).setScale(bit, BigDecimal.ROUND_UP);
		return subZeroAndDot(String.valueOf(bigDecimal));
	}

	public static String numUP(int bit, Double money) {
		BigDecimal bigDecimal = new BigDecimal(String.valueOf(money)).setScale(bit, BigDecimal.ROUND_UP);
		return subZeroAndDot(String.valueOf(bigDecimal));
	}

	// 退位
	public static String numDown(int bit, String money) {
		BigDecimal bigDecimal = new BigDecimal(money).setScale(bit, BigDecimal.ROUND_DOWN);
		return subZeroAndDot(String.valueOf(bigDecimal));
	}

	public static String numDown(int bit, Double money) {
		BigDecimal bigDecimal = new BigDecimal(String.valueOf(money)).setScale(bit, BigDecimal.ROUND_DOWN);
		return subZeroAndDot(String.valueOf(bigDecimal));
	}
	
	
	 /** 
     * 使用java正则表达式去掉多余的.与0 
     */  
    public static String subZeroAndDot(String s){  
        if(s.indexOf(".") > 0){  
            s = s.replaceAll("0+?$", "");//去掉多余的0  
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉  
        }  
        return s;  
    }  
	
	
}
