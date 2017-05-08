package com.cgbsoft.lib.utils.tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.R;
import com.readystatesoftware.viewbadger.BadgeView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/6-17:12
 */
public class ViewUtils {

    public static BadgeView createTopRightBadgerView(Context context, View view) {
        BadgeView badge = new BadgeView(context, view);
        badge.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        badge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
        badge.setTextColor(Color.parseColor("#ffffff"));
        badge.setBadgeBackgroundColor(Color.parseColor("#d73a2e"));
        badge.show();
        return badge;
    }

    public static void scaleUserAchievment(TextView textView, String achievment, float relativeValue) {
        SpannableString textSize = new SpannableString(achievment);
        textSize.setSpan(new RelativeSizeSpan(relativeValue), achievment.length() - 1, achievment.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        textView.setText(textSize);
    }

    public static void formatColorUserAchievment(TextView textView, String value, String... target) {
        SpannableString textSize = new SpannableString(value);
        for (String str : target) {
            System.out.println("----------str=" + str + "-----value==" + value);
            if (!TextUtils.isEmpty(value)) {
                int start = value.indexOf(str);
                textSize.setSpan(new ForegroundColorSpan(Color.parseColor("#ea1202")), start, start + str.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }
        }
        textView.setText(textSize);
    }

//    public static boolean setTextColor(Context context, TextView textView, List<String> keyTitleList) {
//        boolean success = false;
//        String values = textView.getText().toString();
//        SpannableString textSize = new SpannableString(values);
//        if (!CollectionUtils.isEmpty(keyTitleList)) {
//            for (String keyTitle : keyTitleList) {
//                int startIndex = values.indexOf(keyTitle);
//                if (startIndex >= 0) {
//                    textSize.setSpan(new ForegroundColorSpan(Utils.isVisteRole(context) ? Color.parseColor("#f47900") :
//                            Color.parseColor("#ea1202")), startIndex, startIndex + keyTitle.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//                    success = true;
//                }
//            }
//            textView.setText(textSize);
//        }
//        return success;
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
    public static void switchColorToBandC(Context context, TextView textView) {
        textView.setTextColor(AppManager.isInvestor(context) ? context.getResources().getColor(R.color.orange) : context.getResources().getColor(R.color.color5));
    }
//    /**
//     * 设置带超链接的文字，并使超链接可以点击
//     */
//    public static void setTextAndRenderLink(Context context, TextView textView, int textResId, int textLinkColorRes,
//                                            OnClickHyperlinkListener listener) {
//        setTextAndRenderLink(context, textView, context.getString(textResId), textLinkColorRes, listener);
//    }
//
//    /**
//     * 设置带超链接的文字，并使超链接可以点击
//     */
//    public static void setTextAndRenderLink(Context context, TextView textView, String text, int textLinkColorRes,
//                                            OnClickHyperlinkListener listener) {
//        if (textView != null && !TextUtils.isEmpty(text)) {
//            List<String> urls = extractUrl(text);
//            textView.setText(null);
//            if (urls != null && urls.size() > 0) {
//                int lastPos = 0; // 记录每次截取的终点
//                for (String url : urls) {
//                    int begin = text.indexOf(url, lastPos);
//                    int end = begin + url.length();
//                    String normalText = text.substring(lastPos, begin);
//                    textView.append(normalText);
//                    SpannableString link = new SpannableString(url);
//                    CustomClickableSpan clickableSpan = new CustomClickableSpan(context, textLinkColorRes, listener);
//                    clickableSpan.setSpanText(url);
//
//                    link.setSpan(clickableSpan, 0, url.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    textView.append(link);
//                    lastPos = end;
//                }
//                // append最后一段normal text
//                String normalText = text.substring(lastPos);
//                textView.append(normalText);
//
//                textView.setMovementMethod(LinkMovementMethod.getInstance());
//                textView.setHighlightColor(Color.TRANSPARENT);
//            } else {
//                textView.setText(text);
//            }
//        }
//    }

//    public static FloatingActionMenu initSatelliteMenu(final Activity activity, LayoutInflater layoutInflater) {
//        ImageView icon = new ImageView(activity); // Create an icon
//        icon.setImageResource(R.drawable.yunjian);
//        FloatingActionButton.LayoutParams layoutParams = new FloatingActionButton.LayoutParams(250, 250);
//        layoutParams.setMargins(0, 0, 0, 0);
//
//        final FloatingActionButton actionButton = new FloatingActionButton.Builder(activity).setLayoutParams(layoutParams).
//                setContentView(icon).setPosition(FloatingActionButton.POSITION_BOTTOM_CENTER).build();
//
//        TextView one = (TextView)layoutInflater.inflate(R.layout.textview_drawable, null);
//        one.setText(R.string.menu_call_teacher);
//        showCompoundDrawable(one, ContextCompat.getDrawable(activity, R.drawable.hujiaoguwen_nor));
//        View first = buildSubButton(activity, one);
//        first.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                NavigationUtils.startDialgTelephone(activity, MApplication.getUser().getAdviserPhone());
//            }
//        });
//
//        TextView two = (TextView)layoutInflater.inflate(R.layout.textview_drawable, null);
//        two.setText(R.string.menu_session);
//        showCompoundDrawable(two, ContextCompat.getDrawable(activity, R.drawable.duihua_nor));
//        View second = buildSubButton(activity, two);
//        second.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(activity.getApplicationContext(), ConversationActivity.class);
//                activity.startActivity(i);
//            }
//        });
//
//        TextView three = (TextView)layoutInflater.inflate(R.layout.textview_drawable, null);
//        three.setText(R.string.menu_now_video);
//        showCompoundDrawable(three, ContextCompat.getDrawable(activity, R.drawable.zhibo_nor));
//        View third = buildSubButton(activity, three);
//        third.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Intent intent3 = new Intent(activity, LiveActivity.class);
////                activity.startActivity(intent3);
////                JoinLiveRoom joinLiveRoom = new JoinLiveRoom();
////                EventBus.getDefault().post(joinLiveRoom);
//                //TODO 直播
//            }
//        });
//
//        TextView four = (TextView)layoutInflater.inflate(R.layout.textview_drawable, null);
//        four.setText(R.string.menu_message);
//        showCompoundDrawable(four, ContextCompat.getDrawable(activity, R.drawable.duanxin_nor));
//        View fourth = buildSubButton(activity, four);
//        fourth.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                NavigationUtils.startDialogSendMessage(activity, activity.getResources().getString(R.string.hotline));
//            }
//        });
//
//        TextView five = (TextView)layoutInflater.inflate(R.layout.textview_drawable, null);
//        five.setText(R.string.menu_custom);
//        showCompoundDrawable(five, ContextCompat.getDrawable(activity, R.drawable.kefu_nor));
//        View fiveth = buildSubButton(activity, five);
//        fiveth.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(activity.getApplicationContext(), ConversationActivity.class);
//                activity.startActivity(i);
//                //NavigationUtils.startDialgTelephone(activity, "4001888848");
//            }
//        });
//
//        final FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(activity)
//                .addSubActionView(first)
//                .addSubActionView(second)
//                .addSubActionView(third)
//                .addSubActionView(fourth)
//                .addSubActionView(fiveth)
//                .setRadius(350).setStartAngle(150).setEndAngle(320)
//                .attachTo(actionButton)
//                .build();
//
//        return actionMenu;
//
////        actionButton.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                if (MApplication.getUser().getToC() != null && TextUtils.isEmpty(MApplication.getUser().getToC().getBandingAdviserId())) {
////                    Intent intent = new Intent(activity, VisitNoBindActivity.class);
////                    activity.startActivity(intent);
////                    actionMenu.close(true);
////                } else {
////                    actionMenu.toggle(true);
////                }
////            }
////        });
//    }

//    private static View buildSubButton (Activity activity, TextView textView) {
//        FrameLayout.LayoutParams blueContentParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
//                FrameLayout.LayoutParams.MATCH_PARENT);
//        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(activity);
//        itemBuilder.setBackgroundDrawable(ContextCompat.getDrawable(activity, R.drawable.bg_transparent));
//        int subActionButtonSizeWidth = 200;
//        int subActionButtonSizeHeight = 250;
//        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(subActionButtonSizeWidth, subActionButtonSizeHeight);
//        itemBuilder.setLayoutParams(params);
//        return itemBuilder.setContentView(textView, blueContentParams).build();
//    }

    private static void showCompoundDrawable(TextView textView, Drawable drawable) {
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        textView.setCompoundDrawables(null, drawable, null, null);
    }


//    public static List<String> extractUrl(String text) {
//        List<String> list = new ArrayList<>();
//        Matcher m = RegexUtil.getMatcher(text, RegexUtil.URL);
//        while (m.find()) {
//            list.add(m.group());
//        }
//        return list;
//    }

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
//
//    public static void switchColorToBandC(Context context, TextView textView) {
//        textView.setTextColor(Utils.isVisteRole(context) ? context.getResources().getColor(R.color.orange) : context.getResources().getColor(R.color.color5));
//    }
//
//    /**
//     * 初始化Webview
//     * @param mWebview
//     */
//    public static void initWebViewSetData(MWebview mWebview) {
//        WebSettings settings = mWebview.getWebView().getSettings();
//        settings.setJavaScriptEnabled(true);
//        settings.setDomStorageEnabled(true);
//        settings.setBuiltInZoomControls(true);
//        mWebview.getWebView().setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                return true;
//            }
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//            }
//        });
//        mWebview.loadUrl(Domain.pageInit);
//    }

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

//    /**
//     * 初始化用户头像  优先使用本地修改的头像，再请求网络头像
//     * @param imageView
//     */
//    public static void initUserPhoto(Context context, ImageView imageView, String changPath) {
//        if (!TextUtils.isEmpty(changPath)) {
//            Bitmap bitmap = MyBitmapUtils.getLoacalBitmap(changPath);
//            if (bitmap != null) {
//                imageView.setImageBitmap(bitmap);
//                return;
//            }
//        }
//        String userId = SPSave.getInstance(context).getString("userId");
//        if ((userId != null) && (MApplication.getUserid() != null) && userId.equals(MApplication.getUserid())) {
//            String url = SPSave.getInstance(MApplication.mContext).getString("headUrl");
//            if (!TextUtils.isEmpty(url) && !url.startsWith("http")) {
//                url = Domain.urlStr + url;
//            }
//            BitmapUtils bu = new BitmapUtils(context);
//            bu.configDefaultLoadFailedImage(R.drawable.touxiang_default);
//            bu.display(imageView, url);
//        }
//    }

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

}
