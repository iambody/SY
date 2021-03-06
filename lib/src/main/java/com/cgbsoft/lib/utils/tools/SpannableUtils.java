package com.cgbsoft.lib.utils.tools;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/7/13-09:11
 */
public class SpannableUtils {

    private SpannableUtils() {

    }

    /**
     * 改变字符串中某一段文字的字号
     * setTextSize("",24,0,2) = null;
     * setTextSize(null,24,0,2) = null;
     * setTextSize("abc",-2,0,2) = null;
     * setTextSize("abc",24,0,4) = null;
     * setTextSize("abc",24,-2,2) = null;
     * setTextSize("abc",24,0,2) = normal string
     */
    public static SpannableString setTextSize(String content, int startIndex, int endIndex, int fontSize) {
        if (TextUtils.isEmpty(content) || fontSize <= 0 || startIndex >= endIndex || startIndex < 0 || endIndex >= content.length()) {
            return null;
        }
        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan(new AbsoluteSizeSpan(fontSize), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }
    public static SpannableString setTextSize1(String content, int startIndex, int endIndex, int fontSize) {
        if (TextUtils.isEmpty(content) || fontSize <= 0 || startIndex >= endIndex || startIndex < 0 ) {
            return null;
        }
        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan(new AbsoluteSizeSpan(fontSize), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }
    public static SpannableString setBackSize(String content,int startIndex,int fontSize){
       return setTextSize(content,startIndex,content.length()-1,fontSize);
    }
    public static SpannableString setTextSub(String content, int startIndex, int endIndex) {
        if (TextUtils.isEmpty(content) || startIndex < 0 || endIndex >= content.length() || startIndex >= endIndex) {
            return null;
        }

        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan(new SubscriptSpan(), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }

    public static SpannableString setTextSuper(String content, int startIndex, int endIndex) {
        if (TextUtils.isEmpty(content) || startIndex < 0 || endIndex >= content.length() || startIndex >= endIndex) {
            return null;
        }

        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan(new SuperscriptSpan(), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }

    public static SpannableString setTextStrikethrough(String content, int startIndex, int endIndex) {
        if (TextUtils.isEmpty(content) || startIndex < 0 || endIndex >= content.length() || startIndex >= endIndex) {
            return null;
        }

        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan(new StrikethroughSpan(), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }

    public static SpannableString setTextUnderline(String content, int startIndex, int endIndex) {
        if (TextUtils.isEmpty(content) || startIndex < 0 || endIndex >= content.length() || startIndex >= endIndex) {
            return null;
        }

        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan(new UnderlineSpan(), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }

    public static SpannableString setTextBold(String content, int startIndex, int endIndex) {
        if (TextUtils.isEmpty(content) || startIndex < 0 || endIndex >= content.length() || startIndex >= endIndex) {
            return null;
        }

        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }

    public static SpannableString setTextItalic(String content, int startIndex, int endIndex) {
        if (TextUtils.isEmpty(content) || startIndex < 0 || endIndex >= content.length() || startIndex >= endIndex) {
            return null;
        }

        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan(new StyleSpan(android.graphics.Typeface.ITALIC), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }

    public static SpannableString setTextBoldItalic(String content, int startIndex, int endIndex) {
        if (TextUtils.isEmpty(content) || startIndex < 0 || endIndex >= content.length() || startIndex >= endIndex) {
            return null;
        }

        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD_ITALIC), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }

    public static SpannableString setTextForeground(String content, int startIndex, int endIndex, int foregroundColor) {
        if (TextUtils.isEmpty(content) || startIndex < 0 || endIndex >= content.length() || startIndex >= endIndex) {
            return null;
        }

        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan(new ForegroundColorSpan(foregroundColor), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }

    /**
     * 中间颜色和字号的设置
     */
    public static SpannableString setTextColorSize(Context context, String content, int startIndex, int endIndex, int centerColor, int centerTextSize) {
        if (TextUtils.isEmpty(content) || startIndex < 0 || endIndex >= content.length() || startIndex >= endIndex) {
            return null;
        }

        SpannableString spannableString = new SpannableString(content);
//        spannableString.setSpan(new ForegroundColorSpan( foregroundColor ), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //字号调整
//        spannableString.setSpan(new AbsoluteSizeSpan(sideColor), 0, startIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new AbsoluteSizeSpan(centerTextSize), startIndex, endIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //颜色调整
        spannableString.setSpan(new ForegroundColorSpan(context.getResources().getColor(centerColor)), startIndex,
                endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        spannableString.setSpan(new ForegroundColorSpan(context.getResources().getColor(sideColor)), 0,
//                startIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        spannableString.setSpan(new ForegroundColorSpan(context.getResources().getColor(sideColor)), endIndex,
//                alllenth, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    public static SpannableString setTextBackground(String content, int startIndex, int endIndex, int backgroundColor) {
        if (TextUtils.isEmpty(content) || startIndex < 0 || endIndex >= content.length() || startIndex >= endIndex) {
            return null;
        }

        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan(new BackgroundColorSpan(backgroundColor), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }

    /**
     * 设置文本的超链接
     *
     * @param content    需要处理的文本
     * @param startIndex
     * @param endIndex   被处理文本中需要处理字串的开始和结束索引
     * @param url        文本对应的链接地址，需要注意格式：
     *                   （1）电话以"tel:"打头，比如"tel:02355692427"
     *                   （2）邮件以"mailto:"打头，比如"mailto:zmywly8866@gmail.com"
     *                   （3）短信以"sms:"打头，比如"sms:02355692427"
     *                   （4）彩信以"mms:"打头，比如"mms:02355692427"
     *                   （5）地图以"geo:"打头，比如"geo:68.426537,68.123456"
     *                   （6）网络以"http://"打头，比如"http://www.google.com"
     */
    public static SpannableString setTextURL(String content, int startIndex, int endIndex, String url) {
        if (TextUtils.isEmpty(content) || startIndex < 0 || endIndex >= content.length() || startIndex >= endIndex) {
            return null;
        }

        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan(new URLSpan(url), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }

    public static SpannableString setTextImg(String content, int startIndex, int endIndex, Drawable drawable) {
        if (TextUtils.isEmpty(content) || startIndex < 0 || endIndex >= content.length() || startIndex >= endIndex) {
            return null;
        }

        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan(new ImageSpan(drawable), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }

}
