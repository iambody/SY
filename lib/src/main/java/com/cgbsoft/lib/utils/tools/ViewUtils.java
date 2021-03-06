package com.cgbsoft.lib.utils.tools;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.R;
import com.readystatesoftware.viewbadger.BadgeView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author chenlong on 16/9/5.
 */
public class ViewUtils {
    public static final String PASSWROD_TYPE_START_SIX = "******";
    public static final String PASSWROD_TYPE_START_FOUR = "****";

    public static BadgeView createLeftTopRedPoint(Context context, View view, int value) {
        BadgeView badge = new BadgeView(context, view);
        badge.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        badge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
        badge.setText(String.valueOf(value > 99 ? 99 : value));
        badge.setTextColor(Color.parseColor("#ffffff"));
        badge.setBadgeBackgroundColor(Color.parseColor("#d73a2e"));
        badge.setVisibility(View.VISIBLE);
        badge.show();
        return badge;
    }

    public static BadgeView createLeftTopRedStringPoint(Context context, View view, String value) {
        BadgeView badge = new BadgeView(context, view);
        badge.setTextSize(TypedValue.COMPLEX_UNIT_SP, 6);
        badge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
        badge.setText(value);
        badge.setTextColor(Color.parseColor("#ffffff"));
        badge.setBadgeBackgroundColor(Color.parseColor("#d73a2e"));
        badge.setVisibility(View.VISIBLE);
        badge.show();
        return badge;
    }

    public static BadgeView createLeftTopRedStringPointAndPosition(Context context, View view, String value) {
        BadgeView badge = new BadgeView(context, view);
        badge.setTextSize(TypedValue.COMPLEX_UNIT_SP, 6);
        badge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
        badge.setPaddingRelative(5, 0, 0, 5);
        badge.setText(value);
        badge.setTextColor(Color.parseColor("#ffffff"));
        badge.setBadgeBackgroundColor(Color.parseColor("#d73a2e"));
        badge.setVisibility(View.VISIBLE);
        badge.show();
        return badge;
    }

    public static void textViewFormatPasswordType(TextView textView) {
        String values = textView.getText().toString();
        values = values.replace(values, PASSWROD_TYPE_START_SIX);
        textView.setTextColor(ContextCompat.getColorStateList(textView.getContext(), R.color.black));
        textView.setText(values);
    }

    public static void textViewFormatPasswordType(TextView textView, String subString) {
        String values = textView.getText().toString();
        if (TextUtils.isEmpty(values)) {
            return;
        }
        int startIndex = values.indexOf(subString);
        int endIndex = values.length();
//        int endIndex = startIndex + subString.length();
        if (startIndex < 0 ) {
            startIndex = 0;
        }
        if (endIndex > values.length()) {
            endIndex = values.length();
        }
        String newStr = values.substring(startIndex, endIndex);
        values = values.replace(newStr, PASSWROD_TYPE_START_SIX);
        textView.setText(values);
    }

    public static void TextViewFormatNumberType(TextView textView, String showNumber) {
        String values = textView.getText().toString();
        values = values.replaceAll(PASSWROD_TYPE_START_SIX, showNumber);
        textView.setText(values);
    }

    public static void scaleUserAchievment(TextView textView, String achievment, float relativeValue) {
        if (!TextUtils.isEmpty(achievment)) {
            SpannableString textSize = new SpannableString(achievment);
            textSize.setSpan(new RelativeSizeSpan(relativeValue), achievment.length() - 1, achievment.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            textView.setText(textSize);
        }
    }

    public static void scaleUserAchievment(TextView textView, String achievment, int start, int end, float relativeValue) {
        SpannableString textSize = new SpannableString(achievment);
        textSize.setSpan(new RelativeSizeSpan(relativeValue), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        textView.setText(textSize);
    }

    public static void formatColorUserAchievment(TextView textView, String value, String... target) {
        SpannableString textSize = new SpannableString(value);
        for (String str : target) {
            if (!TextUtils.isEmpty(value)) {
                int start = value.indexOf(str);
                textSize.setSpan(new ForegroundColorSpan(Color.parseColor("#bf9b69")), start, start + str.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }
        }
        textView.setText(textSize);
    }

    public static boolean setTextColor(Context context, TextView textView, List<String> keyTitleList) {
        boolean success = false;
        String values = textView.getText().toString();
        SpannableString textSize = new SpannableString(values);
        if (keyTitleList != null && keyTitleList.size() > 0) {
            for (String keyTitle : keyTitleList) {
                int startIndex = values.indexOf(keyTitle);
                if (startIndex >= 0) {
                    textSize.setSpan(new ForegroundColorSpan(Color.parseColor("#bf9b69")), startIndex, startIndex + keyTitle.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    success = true;
                }
            }
            textView.setText(textSize);
        }
        return success;
    }

    /**
     * 左侧添加图片
     * @param textView
     * @param
     * @param
     */
    public static void setTextViewLeftIv(Context context,TextView  textView,int ivResuorceId){
        Drawable dwLeft = context.getResources().getDrawable(ivResuorceId);
        dwLeft.setBounds(0, 0, dwLeft.getMinimumWidth(), dwLeft.getMinimumHeight());
        textView.setCompoundDrawables(dwLeft, null, null, null);
    }

    public static void cancleTextViewLeftIv(TextView  textView){
        textView.setCompoundDrawables(null, null, null, null);
    }
//    public static boolean containKeyName(TextView textView , List<String> keyTitleList) {
//        String values = textView.getText().toString();
//        if (!CollectionUtils.isEmpty(keyTitleList)) {
//            for (String key : keyTitleList) {
//                if (values.contains(key)) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }

    public static void setTextColor(TextView textView, String value, int colorRes) {
        String target = textView.getText().toString();
        int index = target.indexOf(value);
        if (index >= 0) {
            SpannableString textSize = new SpannableString(target);
            textSize.setSpan(new ForegroundColorSpan(colorRes), index, index + value.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            textView.setText(textSize);
        }
    }

    public static void setTextColorAndLink(Context context, TextView textView, int textResId, int textLinkColorRes,
                                           OnClickHyperlinkListener listener) {
        setTextColorAndLink(textView, context.getResources().getString(textResId), textLinkColorRes, listener);
    }

    public static void setTextColorAndLink(TextView textView, String target, int textLinkColorRes, final OnClickHyperlinkListener listener) {
        String resource = textView.getText().toString();
        int index = resource.indexOf(target);
        SpannableString spannableString1 = new SpannableString(resource);
        spannableString1.setSpan(new ClickableSpan(){
            @Override
            public void onClick(View widget) {
                listener.onClick(widget, null);

            }
        }, index, index + target.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString1.setSpan(new ForegroundColorSpan(textLinkColorRes),index, index + target.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        textView.setText(spannableString1);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public static void setLinkTextView(TextView textView, String target, final OnClickHyperlinkListener listener) {
        String resource = textView.getText().toString();
        int index = resource.indexOf(target);
        SpannableString spannableString1 = new SpannableString(resource);
        spannableString1.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                listener.onClick(widget, null);

            }
        }, index, index + target.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
    }

    /**
     * 设置带超链接的文字，并使超链接可以点击
     */
    public static void setTextAndRenderLink(Context context, TextView textView, int textResId, int textLinkColorRes,
                                            OnClickHyperlinkListener listener) {
        setTextAndRenderLink(context, textView, context.getString(textResId), textLinkColorRes, listener);
    }

    /**
     * 设置带超链接的文字，并使超链接可以点击
     */
    public static void setTextAndRenderLink(Context context, TextView textView, String text, int textLinkColorRes,
                                            OnClickHyperlinkListener listener) {
        if (textView != null && !TextUtils.isEmpty(text)) {
            List<String> urls = extractUrl(text);
            textView.setText(null);
            if (urls != null && urls.size() > 0) {
                int lastPos = 0; // 记录每次截取的终点
                for (String url : urls) {
                    int begin = text.indexOf(url, lastPos);
                    int end = begin + url.length();
                    String normalText = text.substring(lastPos, begin);
                    textView.append(normalText);
                    SpannableString link = new SpannableString(url);
                    CustomClickableSpan clickableSpan = new CustomClickableSpan(context, textLinkColorRes, listener);
                    clickableSpan.setSpanText(url);

                    link.setSpan(clickableSpan, 0, url.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    textView.append(link);
                    lastPos = end;
                }
                // append最后一段normal text
                String normalText = text.substring(lastPos);
                textView.append(normalText);

                textView.setMovementMethod(LinkMovementMethod.getInstance());
                textView.setHighlightColor(Color.TRANSPARENT);
            } else {
                textView.setText(text);
            }
        }
    }

    private static void showCompoundDrawable(TextView textView, Drawable drawable) {
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        textView.setCompoundDrawables(null, drawable, null, null);
    }


    public static List<String> extractUrl(String text) {
        List<String> list = new ArrayList<>();
        Matcher m = RegexUtil.getMatcher(text, RegexUtil.URL);
        while (m.find()) {
            list.add(m.group());
        }
        return list;
    }

    private static class CustomClickableSpan extends ClickableSpan {
        private String spanText;
        private final int textLinkColorRes;
        private final OnClickHyperlinkListener listener;
        private Context context;

        public CustomClickableSpan(Context context, int textLinkColorRes, OnClickHyperlinkListener listener) {
            this.textLinkColorRes = textLinkColorRes;
            this.listener = listener;
            this.context = context;
        }

        public String getSpanText() {
            return spanText;
        }

        public void setSpanText(String spanText) {
            this.spanText = spanText;
        }

        @Override
        public void updateDrawState(@NonNull TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(context.getResources().getColor(textLinkColorRes));
            ds.setUnderlineText(true);
        }

        @Override
        public void onClick(View widget) {
            if (!TextUtils.isEmpty(getSpanText()) &&
                    (getSpanText().startsWith("http://") || getSpanText().startsWith("https://"))) {
                if (listener != null) {
                    listener.onClick(widget, getSpanText());
                }
            }
        }
    }

    public static void switchColorToBandC(Context context, TextView textView) {
        textView.setTextColor(AppManager.isInvestor(context) ? context.getResources().getColor(R.color.orange) : context.getResources().getColor(R.color.color5));
    }

    /**
     * 获取手机的宽度
     * @return
     */
    public static int getDisplayWidth(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        // 获取屏幕信息
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        return screenWidth;
    }

    public interface OnClickHyperlinkListener {
        void onClick(View v, String linkText);
    }

    /**
     * Hide window input soft keyboard
     */
    public static void hideInputMethod(View currentFocus) {
        if (currentFocus == null) {
            return;
        }

        InputMethodManager inputManager =
                (InputMethodManager) currentFocus.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
    }

    /**
     * Show window input soft keyboard
     */
    public static void showInputMethod(View currentFocus) {
        InputMethodManager inputManager =
                (InputMethodManager) currentFocus.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(currentFocus, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * Show window input soft keyboard
     */
    public static void showInputMethod(Window window) {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                | WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    public static void setWindowStatusBarColor(Activity activity, int colorResId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(activity.getResources().getColor(colorResId));

                //底部导航栏
                //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String formateMoneyPattern(String money, boolean isNeedFlag) {
        String flag = "";
        if (!TextUtils.isEmpty(money)) {
            if (isNeedFlag && (money.startsWith("+"))) {
                flag = money.substring(0, 1);
            }
            double moneyDouble = Double.parseDouble(money);
            DecimalFormat df = new DecimalFormat("#.00");
            if (Math.abs(moneyDouble) > 10000 * 10000) {
                return flag.concat(df.format(moneyDouble/(10000 * 10000)));
            } else if (Math.abs(moneyDouble) > 10000) {
                return flag.concat(df.format(moneyDouble/(10000)));
            } else {
                return flag.concat(String.valueOf(moneyDouble));
            }
        }
        return "";
    }

    public static String getMoneyUnit(String money) {
        if (!TextUtils.isEmpty(money)) {
            double moneyDouble = Double.parseDouble(money);
            if (Math.abs(moneyDouble) > 10000 * 10000) {
                return "亿";
            } else if (Math.abs(moneyDouble) > 10000) {
                return "万";
            } else {
                return "元";
            }
        }
        return "元";
    }

    public static String formatNumberPatter(String targetValue, int unitNumber) {
        String partten = "%." + unitNumber + "f";
        if (!TextUtils.isEmpty(targetValue)) {
            try {
                return String.format(partten, Float.parseFloat(targetValue));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return targetValue;
    }

    public static void showTextByValue(Context context, TextView textView, String value) {
        if (!TextUtils.isEmpty(value)) {
            double myDoubleValue = Double.parseDouble(value);
            if (myDoubleValue > 0) {
                textView.setTextColor(ContextCompat.getColorStateList(context, R.color.increase_income_color));
            } else if (myDoubleValue < 0) {
                textView.setTextColor(ContextCompat.getColorStateList(context, R.color.decrease_income_color));
            } else {
                textView.setTextColor(ContextCompat.getColorStateList(context, R.color.black));
            }
        }
    }

    public static String productEncodyStr(String cardNumber) {
        StringBuffer sb = new StringBuffer();
        if (!TextUtils.isEmpty(cardNumber)) {
            char[] bs = cardNumber.toCharArray();
            int i = 0;
            while (i++ < bs.length) {
                sb.append("*");
            }
            return sb.toString();
        }
        return "";
    }

    public static boolean checkIdNumberRegex(String idNumber) {
        return Pattern.matches("^([0-9]{17}[0-9Xx])|([0-9]{15})$", idNumber);
    }

    public static boolean checkPhoneNumberRegex(String phoneNumber) {
        String mobileRegex = "^1(3|4|5|7|8)\\d{9}$";
        return phoneNumber.matches(mobileRegex);
    }
}
