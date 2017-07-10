/*
 *	Copyright (c) 2013, Yulong Information Technologies
 *	All rights reserved.
 *  
 *  @Project: aNoahwm
 *  @author: Robot
 *	@email: feng88724@126.com
 */
package com.cgbsoft.privatefund.utils;

import android.text.TextUtils;
import android.widget.TextView;

import com.cgbsoft.lib.utils.string.MySpannableString;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串处理工具类
 * @author Robot
 * @weibo http://weibo.com/feng88724
 * @date May 16, 2013	
 */
public class StringKit {

	/**
	 * 判断是否为空字符串
	 * @param src
	 * @return
	 */
	public static boolean isEmpty(String src) {
		return src == null || src.trim().length() == 0;
	}
	
	/**
	 * 判断是否是空字符串(包括null, 长度为0, 只包含空格)
	 * @return
	 */
	public static boolean isNotEmpty(String src) {
		return !isEmpty(src);
	}
	
	//**************************************************************
    //InputStream 与 String 互转
    //**************************************************************
    /**
     * 字符串转为输入流
     * @param str
     * @return
     */
    public static InputStream string2Stream(String str) {
    	if(isEmpty(str)) return null;
        ByteArrayInputStream stream = new ByteArrayInputStream(str.getBytes());
        return stream;
    }
    
    /**
     * 输入流转为字符串
     * @param inStream
     * @return
     * @throws IOException
     */
    public static String stream2String(InputStream inStream) throws IOException {
    	return new String(stream2Bytes(inStream));
    }
    
    /**
     * 输入流转为字符数组
     * @param inStream
     * @return
     * @throws IOException
     */
    public static byte[] stream2Bytes(InputStream inStream) throws IOException {
 		ByteArrayOutputStream outStream = null;
 		try {
 			outStream = new ByteArrayOutputStream();
 			byte[] buffer = new byte[1024];
 			int len = 0;
 			while ((len = inStream.read(buffer)) != -1) {
 				outStream.write(buffer, 0, len);
 			}
 			return outStream.toByteArray();
 		} finally {
 			try {
 				if(inStream != null) {
 					inStream.close();
 				}
 				if(outStream != null) {
 					outStream.close();
 				}
 			}catch(Exception e) {
 			}
 		}
    }
    
    public static String getFormatNumBerString(BigDecimal num) {
    	DecimalFormat df = new DecimalFormat(",##0.00");
    	String result = df.format(num);
		return result;
    }
    public static String getFormatNumBerString(String num) {
        if (TextUtils.isEmpty(num)||"--".equals(num)) {
            return "";
        }
        String endStr="";
        if (num.contains(".")) {
            endStr = num.substring(num.indexOf("."));
            num = num.substring(0, num.indexOf("."));
        }
        DecimalFormat df = new DecimalFormat("###,###");
        String result = df.format(Double.parseDouble(num));
//        String s = "";
//        if (num.contains(".")) {
//            s=num.substring(num.indexOf("."));
//        }
        return result+endStr;
    }
    /**
     * 指定位子后字体小写
     * @param tv
     * @param content
     */
    public static void showTextStyle(TextView tv, String content, String startPosition){
    	if (StringKit.isNotEmpty(content)&&content.contains(startPosition)) {
    		MySpannableString.setNewStringStyle1(tv, content, content.indexOf(startPosition),content.length(),
    				(float) 0.7, 0);
    	}else{
    		tv.setText(content);
    	}
    }
    /**
     * 提取jsonp(...)字符中的内容
     *
     * @param response
     * @return
     */
    public static String getJsonpContent(String response) {
        if (response != null && response.contains("jsonp")) {
            Pattern p = Pattern.compile("jsonp\\((.*)\\)"/* "jsonp\\(([^)]*)\\)" */);
            Matcher m = p.matcher(response);
            if (m.find() && m.groupCount() >= 1) {
                return m.group(1);
            } else {
                return response;
            }
        } else {
            return response;
        }
    }


}
