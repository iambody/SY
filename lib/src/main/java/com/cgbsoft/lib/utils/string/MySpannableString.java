package com.cgbsoft.lib.utils.string;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.widget.TextView;

/**
 * 同一文本，不同样式
 * 
 */
public class MySpannableString {
	public static SpannableString setNewStringStyle(String str, int start,
                                                    int end, float textSize, int color) {
		SpannableString style = new SpannableString(str);
		style.setSpan(new RelativeSizeSpan(textSize), start, end,
				Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		if (color != 0) {
			style.setSpan(new ForegroundColorSpan(color), start, end,
					Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		}
		return style;
	}

	public static SpannableString setNewStringStyle1(TextView tv, String str,
                                                     int start, int end, float textSize, int color) {
//        String substring = str.substring(0, start);
//        DecimalFormat df = new DecimalFormat("###,###");
//        String formatSub = df.format(Double.parseDouble(substring));
//        str = str.replace(substring, formatSub);
//        start = str.indexOf(".");
//        end=str.length();
        SpannableString style = new SpannableString(str);
		if (!TextUtils.isEmpty(str)) {
			style.setSpan(new RelativeSizeSpan(textSize), start, end,
					Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
			if (color != 0) {
				style.setSpan(new ForegroundColorSpan(color), start, end,
						Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
			}
			tv.setText(style);

		} else {
			tv.setText(str);
		}
		return style;
	}

	/**
	 * 获取文本
	 * @param context 上下文
	 * @param source 文本
	 * @param bgcolor 背景颜色
	 * @param colorid 文本颜色
	 * @param click 点击事件
	 * @return
	 */
	public static SpannableString TextSpan(Context context, String source,
                                           int bgcolor, int colorid, ClickableSpan click) {

		SpannableString spanableInfo = new SpannableString(source);
		spanableInfo.setSpan(new BackgroundColorSpan(context.getResources()
				.getColor(bgcolor)), 0, spanableInfo.length(),
				Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		spanableInfo.setSpan(click, 0, spanableInfo.length(),
				Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
		spanableInfo.setSpan(new ForegroundColorSpan(context.getResources()
				.getColor(colorid)), 0, spanableInfo.length(),
				Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		return spanableInfo;
	}
}
