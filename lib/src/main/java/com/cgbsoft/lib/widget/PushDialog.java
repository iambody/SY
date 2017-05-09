package com.cgbsoft.lib.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cgbsoft.lib.R;
import com.cgbsoft.lib.base.model.AppResourcesEntity;
import com.cgbsoft.lib.base.model.bean.OtherInfo;
import com.cgbsoft.lib.utils.cache.CacheManager;
import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.utils.db.DBConstant;
import com.cgbsoft.lib.utils.db.DaoUtils;
import com.cgbsoft.lib.utils.dm.core.DownloadManagerPro;
import com.cgbsoft.lib.utils.dm.core.DownloadManagerProListener;
import com.cgbsoft.lib.utils.dm.database.elements.Task;
import com.cgbsoft.lib.utils.imgNetLoad.Imageload;
import com.cgbsoft.lib.utils.rxjava.RxSchedulersHelper;
import com.cgbsoft.lib.utils.tools.Utils;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;

import rx.Observable;

import static com.cgbsoft.lib.utils.db.DBConstant.APP_DOWNLOAD_PATH;

/**
 * 下载apkdialog
 * Created by xiaoyu.zhang on 2016/11/24 13:59
 * Email:zhangxyfs@126.com
 *  
 */
public abstract class PushDialog extends BaseDialog {
    private Context _context;
    private boolean _isOpenWindow, _isCompel, _isCouldClickBack = true;

    private Resources resources;
    private int screenWidth;

    private TextView tv_vcd_title;
    private TextView tv_vcd_message;
    private Button btn_vcd_sure;
    private ImageView iv_vcd_cancel;

    private ImageView push_dlg_img;
    private String title, content, left, right, imgUrl;

    /**
     * 初始化
     *
     * @param context 上下文
     * @param title   标题
     * @param content 内容
     * @param right   确认文字
     * @param left    取消
     * @param imgUrl  图片，如果使用默认火箭，传空
     */
    public PushDialog(Context context, String title, String content, String right, String left, String imgUrl) {
        this(context, R.style.ios_dialog_alpha);
        _context = context;
        this.title = title;
        this.content = content;
        this.right = right;
        this.left = left;
        this.imgUrl = imgUrl;
    }

    public PushDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        this(context, R.style.ios_dialog_alpha);
    }

    public PushDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_push_dialog);
        init();
    }

    public void init() {
        tv_vcd_title = (TextView) findViewById(R.id.tv_vcd_title);
        tv_vcd_message = (TextView) findViewById(R.id.tv_vcd_message);
        btn_vcd_sure = (Button) findViewById(R.id.btn_vcd_sure);
        iv_vcd_cancel = (ImageView) findViewById(R.id.iv_vcd_cancel);
        push_dlg_img = (ImageView) findViewById(R.id.push_dialog_image);

        btn_vcd_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                right();
            }
        });
        iv_vcd_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                left();
            }
        });
        if (!TextUtils.isEmpty(imgUrl)) {
            Imageload.display(_context, imgUrl, push_dlg_img);
        } else {
            Imageload.display(_context, R.drawable.bg_custom_dialog, push_dlg_img);
        }
        btn_vcd_sure.setText(right);
        tv_vcd_message.setText(content);
        tv_vcd_title.setText(title);
    }

    public abstract void left();

    public abstract void right();

}