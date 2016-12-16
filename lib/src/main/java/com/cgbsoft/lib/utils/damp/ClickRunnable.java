package com.cgbsoft.lib.utils.damp;

import android.view.View;

/**
 * Created by win8 -1 on 2016/1/14.
 */
public class ClickRunnable implements Runnable {
    private View view;
    private OnClickListener onClickListener;

    public ClickRunnable(View view, OnClickListener onClickListener) {
        this.view = view;
        this.onClickListener = onClickListener;
    }

    public View getView() {
        return view;
    }

    @Override
    public void run() {
        onClickListener.onClick(view);
    }

    public interface OnClickListener {
        void onClick(View v);
    }
}
