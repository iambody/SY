package com.cgbsoft.lib.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cgbsoft.lib.R;

import static com.umeng.socialize.utils.DeviceConfig.context;

/**
 * Created by xiaoyu.zhang on 2016/11/22 12:19
 * Email:zhangxyfs@126.com
 *  
 */
public class CustomDialog extends Dialog {
    private View parent;

    public CustomDialog(Context context) {
        super(context);
    }

    public CustomDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected CustomDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public class Builder {
        private String title;
        private String message;
        private String positiveButtonText;
        private String negativeButtonText;
        private View contentView;
        private boolean canceledOnTouchOutside = true;
        private boolean canceledOnClickBack = true;
        private float progressMax, progress;
        private DialogInterface.OnClickListener positiveButtonClickListener, negativeButtonClickListener;
        private boolean isSetPositiveListener;

        /**
         * Set the Dialog message from String
         *
         * @param message
         * @return
         */
        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        /**
         * Set the Dialog message from resource
         *
         * @param message
         * @return
         */
        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }

        /**
         * Set the Dialog title from resource
         *
         * @param title
         * @return
         */
        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        /**
         * Set the Dialog title from String
         *
         * @param title
         * @return
         */
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        /**
         * Set a custom content view for the Dialog. If a message is set, the
         * contentView is not added to the Dialog...
         *
         * @param v
         * @return
         */
        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        public Builder setProgress(float progress, float max) {
            this.progress = progress;
            this.progressMax = max;
            return this;
        }

        /**
         * 设置点击外面是否消失
         *
         * @param b
         * @return
         */
        public Builder setCanceledOnTouchOutside(boolean b) {
            this.canceledOnTouchOutside = b;
            return this;
        }

        /**
         * 设置点击返回键是否消失
         *
         * @param b
         * @return
         */
        public Builder setCanceledOnClickBack(boolean b) {
            this.canceledOnClickBack = b;
            return this;
        }

        /**
         * Set the positive button resource and it's listener
         *
         * @param positiveButtonText
         * @param listener
         * @return
         */
        public Builder setPositiveButton(int positiveButtonText, DialogInterface.OnClickListener listener) {
            this.positiveButtonText = (String) context.getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        /**
         * Set the positive button text and it's listener
         *
         * @param positiveButtonText
         * @param listener
         * @return
         */
        public Builder setPositiveButton(String positiveButtonText, DialogInterface.OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            this.isSetPositiveListener = true;
            return this;
        }

        public boolean isSetPositiveListener() {
            return isSetPositiveListener;
        }

        /**
         * Set the negative button resource and it's listener
         *
         * @param negativeButtonText
         * @param listener
         * @return
         */
        public Builder setNegativeButton(int negativeButtonText, DialogInterface.OnClickListener listener) {
            this.negativeButtonText = (String) context.getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        /**
         * Set the negative button text and it's listener
         *
         * @param negativeButtonText
         * @param listener
         * @return
         */
        public Builder setNegativeButton(String negativeButtonText, DialogInterface.OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        /**
         * Create the custom dialog
         */
        public CustomDialog create() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final CustomDialog dialog = new CustomDialog(context, R.style.CenterCompatDialogTheme);
            parent = inflater.inflate(R.layout.view_custom_dialog, null);
//            int width = (int) (Utils.getScreenWidth(context) * 0.9);
            dialog.addContentView(parent, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            // set the dialog title
            if (title != null) {
                ((TextView) parent.findViewById(R.id.tv_vcd_title)).setText(title);
            } else {
                parent.findViewById(R.id.tv_vcd_title).setVisibility(View.GONE);
            }
            // set the confirm button
            if (positiveButtonText != null) {
                Button surBtn = (Button) parent.findViewById(R.id.btn_vcd_sure);
//                if(leftTextColor > 0){
//                    surBtn.setTextColor(context.getResources().getColor(leftTextColor));
//                }
//                if(leftBackResId > 0){
//                    surBtn.setBackgroundResource(leftBackResId);
//                }
                surBtn.setText(positiveButtonText);
                if (positiveButtonClickListener != null) {
                    parent.findViewById(R.id.btn_vcd_sure).setOnClickListener(v -> {
                        positiveButtonClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                    });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                parent.findViewById(R.id.btn_vcd_sure).setVisibility(View.GONE);
            }
            // set the cancel button
            if (negativeButtonText != null) {
                ImageButton cancelBtn = (ImageButton) parent.findViewById(R.id.iv_vcd_cancel);
//                if(rightTextColor > 0){
//                    cancelBtn.setTextColor(context.getResources().getColor(rightTextColor));
//                }
//                if(rightBackResId > 0){
//                    cancelBtn.setBackgroundResource(rightBackResId);
//                }
//                cancelBtn.setText(negativeButtonText);
                if (negativeButtonClickListener != null) {
                    parent.findViewById(R.id.iv_vcd_cancel).setOnClickListener(v -> {
                        negativeButtonClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
//                        if (mOnVisableEditListener != null) {
//                            mOnVisableEditListener.rightBtnClick((EditText) parent.findViewById(R.id.editText));
//                        }
                    });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                parent.findViewById(R.id.iv_vcd_cancel).setVisibility(View.GONE);
            }

            // set the content message
            if (message != null) {
                TextView tv = ((TextView) parent.findViewById(R.id.tv_vcd_message));
//                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) tv.getLayoutParams();
//                lp.width = width;
//                tv.setLayoutParams(lp);
                tv.setText(message);
                parent.findViewById(R.id.pb_vcd).setVisibility(View.GONE);
            } else if (contentView != null) {
                // if no message set
                // add the contentView to the dialog body
//				((LinearLayout) layout.findViewById(R.id.content)).removeAllViews();
//				((LinearLayout) layout.findViewById(R.id.content)).addView(contentView, new LayoutParams(
//						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            } else {
                parent.findViewById(R.id.tv_vcd_message).setVisibility(View.GONE);
                parent.findViewById(R.id.pb_vcd).setVisibility(View.VISIBLE);
                ((ProgressBar) parent.findViewById(R.id.pb_vcd)).setProgress((int) progress);
                ((ProgressBar) parent.findViewById(R.id.pb_vcd)).setMax((int) progressMax);

//                ((TextView) parent.findViewById(R.id.progressValue)).setText(((int) ((progress * 100) / progressMax)) + "%");
            }
            dialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
            dialog.setCancelable(canceledOnClickBack);

            dialog.setContentView(parent);
            return dialog;
        }
    }
}
