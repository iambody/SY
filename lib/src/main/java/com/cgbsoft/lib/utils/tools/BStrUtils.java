package com.cgbsoft.lib.utils.tools;
/**
 * Created by datutu on 17/3/20.
 */

import android.content.Context;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * desc UI中用到的Bean需要进行数据二次操作的方法 &&和context相关的操作处理
 * author Wangyongkui wangyongkui@simuyun.com
 * date 17/3/20 16:03
 */
public class BStrUtils {

    /**
     * 过滤掉特色字符
     * @return
     */
    public static String replaceSpeialStr(String resouceStr) {
        String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(resouceStr);
        return m.replaceAll("").trim();
    }

    /**
     * 判断给定字符串是否空白串 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     *
     * @param input
     * @return boolean
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * 根据上下文获取 所引用的类名
     */
    public static String GetClassNameFromContext(Context context) {
        Class<?> clas = context.getClass();
        return clas.getName();

    }

    /**
     * 根据上下文获取父类的名字
     */
    public static String GetParentsNameFromContext(Context PcContext) {
        Class<?> classs = PcContext.getClass();
        Class<?> Supperclass = classs.getSuperclass();
        return Supperclass.getName();

    }
    /**
     * 判断如果null返回
     **/
    public static String NullToStr(String str) {

        if (null == str || "".equals(str)) {
            return "--";
        }
        return str;
    }
    /**
     * List转换String
     *
     * @param list
     *            :需要转换的List
     * @return String转换后的字符串
     */

}
