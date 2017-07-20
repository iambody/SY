package com.commui.prompt.mvp.holder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cgbsoft.lib.widget.recycler.BaseHolder;
import com.commui.prompt.mvp.listener.MyTaskListener;

import app.privatefund.com.cmmonui.R2;
import butterknife.BindView;

/**
 * desc  taskHolder
 * Created by yangzonghui on 2017/5/11 12:08
 * Email:yangzonghui@simuyun.com
 *  
 */
public class MyTaskHeaderHolder extends BaseHolder {


    public MyTaskHeaderHolder(View itemView, final MyTaskListener listener) {
        super(itemView);
    }

}
