package com.cgbsoft.lib.utils.tools;


import android.content.Context;
import android.text.SpannableString;
import android.widget.TextView;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.R;

import org.json.JSONArray;

import java.math.BigDecimal;
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
     *
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
    public static void setTv(TextView T, String Str) {
        T.setText(NullToStr(Str));
    }

    /**
     * Textview放置文本
     */
    public static void setTv1(TextView T, String Str) {
        T.setText(NullToStr1(Str));
    }

    public static void setSp(TextView T, SpannableString Str) {
        if (null == Str) T.setText("");
        T.setText(Str);
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
     *
     * @param num
     * @return
     */
    public static String getYi(String num) {
        try {
            Float f = Float.parseFloat(num);
            if (f >= 10000.00f) {
                int vas = f.intValue();
                if (vas % 10000 == 0) {
                    return vas / 10000 + "亿";
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
        textView.setTextColor(AppManager.isInvestor(context) ? context.getResources().getColor(R.color.app_golden) : context.getResources().getColor(R.color.color5));
    }

    /**
     * 对V2接口中获取的token进行处理的操作
     *
     * @param token
     * @return
     */
    public static String decodeSimpleEncrypt(String token) {
        String fir = token.substring(0, 1);
        String last = token.substring(1, 2);
        token = token.substring(2, token.length());
        String result = last + fir + token;

        int total = result.length();
        int half = Math.round(total / 2);
        if (total % 2 == 0) {
            half = Math.round(total / 2) - 1;
        }
        token = replaceBeginAndEnd(result, half);
        return token;
    }


    /**
     * 将一个数组以某一元素分界，将这个元素之前的部分与之后的部分互换位置
     * 主要思路: 将之前的部分与之后的部分分别逆序，再将整体逆序即可
     *
     * @param str 要进行操作的字符串
     * @param i   作为分界线的元素的下标
     * @return 转化之后生成的字符串
     */
    public static String replaceBeginAndEnd(String str, int i) {
        if (str == null || i < 0 || str.length() <= i) {
            return str;
        }
        char[] chars = str.toCharArray();
        reverseCharArray(chars, 0, i - 1);
        reverseCharArray(chars, i + 1, chars.length - 1);
        reverseCharArray(chars, 0, chars.length - 1);
        return new String(chars);
    }

    /**
     * 用来翻转一个数组的某一部分
     *
     * @param charArray 要进行操作的数组
     * @param begin     要翻转的部分第一个元素的下标
     * @param end       要翻转的部分最后一个元素的下标
     */
    public static void reverseCharArray(char[] charArray, int begin, int end) {
        char tmp;
        while (begin < end) {
            tmp = charArray[begin];
            charArray[begin] = charArray[end];
            charArray[end] = tmp;
            begin++;
            end--;
        }
    }

    /**
     * 去掉小数后面的0
     */
    public static String replacePoint(String count) {
        if (count.indexOf(".") > 0) {
            count = count.replaceAll("0+?$", "");//去掉多余的0
            count = count.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return count;
    }


    /**
     * float 转换四舍五入 成int
     */
    public static int floatToint(Context context, float f) {
        if (f < 0) {
            PromptManager.ShowCustomToast(context, "金额小于0元");
            return (int) f;
        }
        return Math.round(f);
    }

    /**
     * 小数点后面保留一位
     */
    public static double holdOnePoint(double d) {
        BigDecimal b = new BigDecimal(d);
        return b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
    }


    /**
     * 对象转整数
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static long toLong(String obj) {
        try {
            return Long.parseLong(obj);
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * null字段转""
     */
    public static String nullToEmpty(String str) {
        if (null == str) return "";
        return str;
    }

    public static int postionChineseStr(String str) {

        Pattern pattern = Pattern.compile("[\u4e00-\u9fa5]");
        char c[] = str.toCharArray();
        for (int i = 0; i < c.length; i++) {
            Matcher matcher = pattern.matcher(String.valueOf(c[i]));
            if (matcher.matches()) {
                return i;
            }
        }
        return 0;
    }

    public static boolean isChineseStr(String str) {

        Pattern pattern = Pattern.compile("[\u4e00-\u9fa5]");
        char c[] = str.toCharArray();
        for (int i = 0; i < c.length; i++) {
            Matcher matcher = pattern.matcher(String.valueOf(c[i]));
            if (matcher.matches()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否包含
     *
     * @param str
     * @return
     */
    public static boolean isSpeialStr(String str) {
        String regEx = "[~!/@#$%^&*()-_=+\\|[{}];:\'\",<.>/?]+";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.find();
    }

    public static int postionSpeialStr(String str) {
        String regEx = "[~!/@#$%^&*()-_=+\\|[{}];:\'\",<.>/?]+";
        Pattern p = Pattern.compile(regEx);
        char c[] = str.toCharArray();
        for (int i = 0; i < c.length; i++) {
            Matcher matcher = p.matcher(String.valueOf(c[i]));
            if (matcher.matches()) {
                return i;
            }
        }
        return 0;


    }

    /**
     * 是否包含数字
     */

    public static boolean hasDigit(String content) {
        boolean flag = false;
        Pattern p = Pattern.compile(".*\\d+.*");
        Matcher m = p.matcher(content);
        if (m.find()) {
            flag = true;
        }
        return flag;
    }

    /**
     * 是否包含数字的位置
     */

    public static int lastPostionDigit(String str) {

        Pattern p = Pattern.compile(".*\\d+.*");

        char c[] = str.toCharArray();
        for (int i = 0; i < c.length; i++) {
            Matcher matcher = p.matcher(String.valueOf(c[i]));
            if (matcher.matches()) {
                return beginPostionDigit(str) + getNumbers(str).length() - 1;
            }
        }
        return 0;
    }

    //截取数字
    public static String getNumbers(String content) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }

    /**
     * 是否包含数字的位置
     */
    public static int beginPostionDigit(String str) {
        Pattern p = Pattern.compile(".*\\d+.*");
        char c[] = str.toCharArray();
        for (int i = 0; i < c.length; i++) {
            Matcher matcher = p.matcher(String.valueOf(c[i]));
            if (matcher.matches()) {
                return i;
            }
        }
        return 0;
    }

    public static boolean homeIsRato(String rato) {
        if (BStrUtils.isEmpty(rato)) return false;
        if (rato.endsWith("%")) return true;
        return false;
    }

    /**
     * 以逗号隔开
     */
    public static List<String> regexUtilSplit(String str, String regx) {
        String[] dataStr = str.split(regx);
        List<String> ls = new ArrayList<>();
        if (null == dataStr || dataStr.length <= 0) return null;
        for (String s : dataStr) {
            ls.add(s);
        }
        return ls;
    }
}
