package com.cgbsoft.lib.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.cgbsoft.lib.R;
import com.wang.avi.AVLoadingIndicatorView;

/**
 * @author DingLei
 * @version : v1.0
 * @Description : 加载对话框控件
 * @date 2013-10-23
 */
public class LoadingDialog extends Dialog {

    private Context mContext;
    private LayoutInflater inflater;
    private LayoutParams lp;
    private TextView loadtext;
    private AVLoadingIndicatorView progressBar;
    private ImageView imgSuccess;
    private ImageView imgFail;


    public static LoadingDialog getLoadingDialog(Context context, String loadingText) {
        return getLoadingDialog(context, loadingText, true, true);
    }

    public static LoadingDialog getLoadingDialog(Context context, boolean cancelable, boolean outeSiteCanceled) {
        LoadingDialog dialog = new LoadingDialog(context);
        dialog.setCanceledOnTouchOutside(outeSiteCanceled);
        dialog.setCancelable(cancelable);
        return dialog;
    }

    public static LoadingDialog getLoadingDialog(Context context, String loadingText, boolean cancelable, boolean outeSiteCanceled) {
        LoadingDialog dialog = new LoadingDialog(context);
        if (!invalidString(loadingText)) {
            dialog.setLoadText(loadingText);
        }
        dialog.setCanceledOnTouchOutside(outeSiteCanceled);
        dialog.setCancelable(cancelable);
        return dialog;
    }

    public LoadingDialog(Context context) {
        super(context, R.style.confirm_dialog);
        this.mContext = context;

        View layout = LayoutInflater.from(context).inflate(R.layout.widget_loadingdialog, null);
        loadtext = (TextView) layout.findViewById(R.id.loading_text);
        progressBar = (AVLoadingIndicatorView) layout.findViewById(R.id.loading_bar);
        imgSuccess = (ImageView) layout.findViewById(R.id.img_success);
        imgFail = (ImageView) layout.findViewById(R.id.img_fail);
        setContentView(layout);
//        // 设置window属性
//        lp = getWindow().getAttributes();
//        lp.gravity = Gravity.CENTER;
//        lp.dimAmount = 0; // 去背景遮盖
//        lp.alpha = 0.5f;

    }

    public void setDimAmount(boolean b) {
        if (b) {
            getWindow().setAttributes(lp);
        }
    }

    public void setLoadText(final String content) {
        loadtext.setVisibility(View.VISIBLE);

        if (!TextUtils.isEmpty(content)) {
            loadtext.setText(content);
        }
    }

    public void setLoadText(int resId) {
        if (loadtext.getVisibility() != View.VISIBLE) {
            loadtext.setVisibility(View.VISIBLE);
        }
        loadtext.setText(resId);
    }

    public void setLoading(String text) {
        setLoadText(text);
        progressBar.setVisibility(View.VISIBLE);
        imgSuccess.setVisibility(View.GONE);
        imgFail.setVisibility(View.GONE);
    }

    public void setResult(boolean isSucc, String text) {
        setResult(isSucc, text, 0, null);
    }

    public void setResult(boolean isSucc, String text, int duration) {
        setResult(isSucc, text, duration, null);
    }

    public void setResult(boolean isSucc, String text, int duration, OnDissListener onDismissListener) {
        try {
            if (duration <= 0 && isShowing()) {
                dismiss();
            }
            progressBar.setVisibility(View.GONE);
            imgSuccess.setVisibility(isSucc ? View.VISIBLE : View.GONE);
            imgFail.setVisibility(isSucc ? View.GONE : View.VISIBLE);
            setLoadText(text);

            if (duration > 0)
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (LoadingDialog.this.isShowing())
                            LoadingDialog.this.dismiss();
                        if (onDismissListener != null) {
                            onDismissListener.dissComplete();
                        }
                    }
                }, duration);
        } catch (Exception e) {
            Log.i("s",e.getMessage());
            e.printStackTrace();
        }
    }

    public static boolean invalidString(Object obj) {
        return obj == null || "".equals(obj.toString())
                || "null".equals(obj.toString());
    }

    public interface OnDissListener {
        void dissComplete();
    }

    private long showTime;
    @Override
    public void show() {
        super.show();
//        showTime = System.currentTimeMillis();
    }

    @Override
    public void dismiss() {
//        if(System.currentTimeMillis()-showTime<300){
//            new Timer().schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    dismiss();
//                }
//            },System.currentTimeMillis()-showTime+50);
//            return;
//        }
//        showTime = 0;
        super.dismiss();
    }
}
