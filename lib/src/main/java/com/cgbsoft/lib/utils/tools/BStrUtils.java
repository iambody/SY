package com.cgbsoft.lib.utils.tools;
/**
 * Created by datutu on 17/3/20.
 */
import android.content.Context;
import android.widget.TextView;
import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.R;

import org.json.JSONArray;

import java.util.ArrayList;
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
     *
     * @return
     */
    public static String replaceSpeialStr(String resouceStr) {
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
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
     * 判断如果null返回
     **/
    public static String NullToStr1(String str) {

        if (null == str || "".equals(str)) {
            return "";
        }
        return str;
    }
    /**
     * Json
     * 字符串数组转换成json数组
     *
     * @param strings
     * @return
     */
    public static JSONArray LsToJsonArray(List<String> strings) {
        JSONArray jsonArray = new JSONArray();
        if (null == strings) return jsonArray;
        for (String str : strings) {
            jsonArray.put(str);
        }
        return jsonArray;
    }

    /**
     * Json
     * 字符串转换成json数组
     * @param strings
     * @return
     */
    public static JSONArray StrToJsonArray(String strings) {
        if (isEmpty(strings)) return LsToJsonArray(null);
        List<String> stringList = new ArrayList<>();
        stringList.add(strings);
        return LsToJsonArray(stringList);
    }
    /**
     * Textview放置文本
     */
    public static void SetTxt(TextView T, String Str) {
        T.setText(NullToStr(Str));
    }
    /**
     * Textview放置文本
     */
    public static void SetTxt1(TextView T, String Str) {
        T.setText(NullToStr1(Str));
    }
    /**
     * 转换为亿
     * @param num
     * @return
     */
    public static String getYi(String num) {
        try {
            Float f = Float.parseFloat(num);
            if (f >= 10000.00f) {
                int vas = f.intValue();
                if (vas%10000 == 0) {
                    return vas/10000 + "亿";
                }
                f = f / 10000;
                String d = String.format("%.2f", f);
                return d + "亿";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return num;
    }
    public static void switchColorToBandC(Context context, TextView textView) {
        textView.setTextColor(AppManager.isInvestor(context) ? context.getResources().getColor(R.color.orange) : context.getResources().getColor(R.color.color5));
    }
}
