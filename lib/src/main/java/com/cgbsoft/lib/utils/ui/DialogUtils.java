package com.cgbsoft.lib.utils.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cgbsoft.lib.R;

/**
 * @author chenlong
 */
public class DialogUtils {

  /**
   * 对话框中回调的接口
   */
  public static class SimpleDialogListener {
    public void OnClickPositive() {}
    public void OnClickNegative() {}
  }

  public static class ItemDialogListener {
    public void onClickItem(int item) {}
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
    Button button = (Button)layout.findViewById(R.id.button);
    button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dialog.dismiss();
        dialogListener.OnClickPositive();
      }
    });
    dialog.setCancelable(false);
    dialog.show();
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
