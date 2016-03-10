package com.zehao.util;

public class Tool {
	public static String NVL(String source) {
	    return ((source == null)||(source.equals(""))? "": source);
	  }
}
