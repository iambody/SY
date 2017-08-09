package com.cgbsoft.lib.utils.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cgbsoft.lib.R;
import com.cgbsoft.lib.base.webview.BaseWebViewActivity;
import com.cgbsoft.lib.base.webview.WebViewConstant;
import com.cgbsoft.lib.widget.dialog.DefaultDialog;
import com.cgbsoft.lib.widget.dialog.DownloadAdviserDialog;
import com.lzy.okserver.download.DownloadInfo;

/**
 * @author chenlong
 */
public class DialogUtils {

    /**
     * 对话框中回调的接口
     */
    public static class SimpleDialogListener {
        public void OnClickPositive() {
        }

        public void OnClickNegative() {
        }
    }

    public static class ItemDialogListener {
        public void onClickItem(int item) {
        }
    }


    /**
     * 确认提示单个对话框
     */
    public static Dialog DialogConfirmSingle(Context context, String titleRes, String butText, String canText,
                                             final SimpleDialogListener dialogListener) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(titleRes);
        builder.setPositiveButton(butText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                dialogListener.OnClickPositive();
            }
        });
        builder.setNegativeButton(canText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(false);
        builder.show();
        Dialog dialog = builder.create();
        return dialog;
    }

    /**
     * 确认提示单个对话框
     */
    public static void DialogSimplePrompt(Context context, @StringRes int res,
                                          final SimpleDialogListener dialogListener) {
        final Dialog dialog = new Dialog(context, R.style.gesture_password_dialog);
        LinearLayout layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.dialog_simple_prompt, null);
        dialog.setContentView(layout);
        TextView textView = (TextView) layout.findViewById(R.id.content);
        textView.setText(res);
        Button button = (Button) layout.findViewById(R.id.button);
        button.setOnClickListener(v -> {
            dialog.dismiss();
            dialogListener.OnClickPositive();
        });
        dialog.setCancelable(false);
        dialog.show();
    }

    public static Dialog createSwitchBcDialog(final Context context) {
        String title = "切换到投资顾问功能已下线，如有需要请点击确认前往下载'私募云-投顾版'";
        DefaultDialog dialog = new DefaultDialog(context, title, "取消", "确认") {
            @Override
            public void left() {
                dismiss();
            }

            @Override
            public void right() {
                Intent i = new Intent(getContext(), BaseWebViewActivity.class);
                i.putExtra(WebViewConstant.push_message_url, "http://www.simuyun.com");
                i.putExtra(WebViewConstant.push_message_title, "下载投资顾问版");
                i.putExtra(WebViewConstant.RIGHT_SAVE, false);
                i.putExtra(WebViewConstant.RIGHT_SHARE, false);
                i.putExtra(WebViewConstant.PAGE_INIT, false);
                context.startActivity(i);

//                new DownloadAdviserDialog(context, true, true);
                dismiss();
            }
        };
        return dialog;
    }

    /**
     * 日历添加提醒
     */
    public static void CalendarAddPrompt(Context context,
                                         final ItemDialogListener dialogListener) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.NoTitleAllTransDialog);
        builder.setTitle("添加提醒");
        String[] itmes = new String[]{"无", "5分钟", "10分钟", "30分钟"};
        builder.setItems(itmes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                dialogListener.onClickItem(which);
            }
        });
        builder.setCancelable(true);
        builder.show();
        Dialog dialog = builder.create();
    }


}
