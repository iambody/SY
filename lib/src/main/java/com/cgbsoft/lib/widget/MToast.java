package com.cgbsoft.lib.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cgbsoft.lib.R;


/**
 * @author lee
 *         全局toast
 */
public class MToast extends Toast {
    private LayoutInflater inflate;
    private int marginTop = 0;

    public MToast(Context context) {
        super(context);
        inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void showLoginFailure(String msg) {
        LinearLayout v = (LinearLayout) inflate.inflate(R.layout.view_toast, null);
        TextView tv = (TextView) v.findViewById(R.id.text);
        tv.setText(msg);
        setView(v);
        setDuration(Toast.LENGTH_LONG);
        setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, marginTop);
        show();

    }

    public void show(CharSequence msg, int time) {
        LinearLayout v = (LinearLayout) inflate.inflate(R.layout.view_toast, null);
        TextView tv = (TextView) v.findViewById(R.id.text);
        tv.setText(msg);
        setView(v);
        setDuration(time);
        setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, marginTop);
        show();
    }


    public static MToast makeText(Context context, CharSequence text, int duration) {
        MToast mToast = new MToast(context);
        mToast.show(text, duration);
        return mToast;
    }
}
