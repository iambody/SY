package com.cgbsoft.lib.widget;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.cgbsoft.lib.Appli;
import com.cgbsoft.lib.R;
import com.cgbsoft.lib.base.model.ProtocolEntity;
import com.cgbsoft.lib.utils.cache.CacheManager;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.Utils;
import com.google.gson.Gson;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import butterknife.ButterKnife;
import rx.Subscription;

import static com.cgbsoft.lib.utils.cache.CacheManager.FILE;
import static com.umeng.socialize.utils.DeviceConfig.context;

/**
 * 规则显示
 * Created by xiaoyu.zhang on 2016/11/22 15:36
 * Email:zhangxyfs@126.com
 *  
 */
public class ProtocolDialog {
    private TextView mConfirm, title;
    private TextView mContent;
    private int type;
    private BaseDialog dialog;
    private String filePath = CacheManager.getCachePath(Appli.getContext(), FILE) + "pro.tp";

    private Subscription subscription;

    public ProtocolDialog(Context context, int type, Handler handler) {
        this.type = type;
        init(context);
    }

    private void init(Context context) {
        dialog = new BaseDialog(context, R.style.CenterCompatDialogTheme);
        dialog.setContentView(R.layout.view_protocol_dialog);
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams windowLP = window.getAttributes();
        windowLP.width = Utils.getScreenWidth(context);
        windowLP.height = Utils.getScreenHeight(context);
        window.setAttributes(windowLP);
        window.setGravity(Gravity.CENTER | Gravity.CENTER);

        title = ButterKnife.findById(dialog, R.id.protocol_title);
        mContent = ButterKnife.findById(dialog, R.id.protocal_content);
        mConfirm = ButterKnife.findById(dialog, R.id.protocol_confirm);

        mContent.setMovementMethod(new ScrollingMovementMethod());

        if (SPreference.getIdtentify(context) == Constant.IDS_INVERSTOR) {
            title.setTextColor(0xfff47900);
            mConfirm.setBackgroundResource(R.drawable.shape_f47900);
        } else {
            title.setTextColor(0xffd73a2e);
            mConfirm.setBackgroundResource(R.drawable.all_red_btn);
        }

        mConfirm.setOnClickListener(v -> {
            if (type == 0) {
                SPreference.saveVisableProtocol(context);
            } else if (type == 1) {
                SPreference.saveVisableMessage(context);
            } else if (type == 2) {

            }
            dialog.dismiss();
        });
        dialog.show();
        getProtocol(context);
    }


    public void getProtocol(Context context) {
        String result = "";
        String titlestr = "";
        String confirmStr = "";
        try {

            if (type == 0) {
                titlestr = context.getString(R.string.protocol_dialog_title);
                confirmStr = context.getString(R.string.protocol_agree);
                getProtocolData();
            } else if (type == 1) {
                InputStream in = context.getResources().openRawResource(R.raw.message);
                int lenght = in.available();
                byte[] buffer = new byte[lenght];
                in.read(buffer);
                in.close();
                result = new String(buffer, "UTF-8");

                titlestr = context.getString(R.string.protocol_dialog_message);
                confirmStr = context.getString(R.string.protocol_know);
                mContent.setText(result);
            } else if (type == 2) {
                titlestr = "资产证明资料";
                confirmStr = context.getString(R.string.protocol_know);

                InputStream in = context.getResources().openRawResource(R.raw.asset);
                int lenght = in.available();
                byte[] buffer = new byte[lenght];
                in.read(buffer);
                in.close();
                result = new String(buffer, "UTF-8");
                mContent.setText(result);
            }

            title.setText(titlestr);
            mConfirm.setText(confirmStr);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getProtocolData() {
        ApiClient.getProtocol().subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                ProtocolEntity.Result result = new Gson().fromJson(s, ProtocolEntity.Result.class);
                if (TextUtils.isEmpty(result.userAgree)) {
                    return;
                }
                mContent.setText(result.userAgree);
                writeSDProtocol(result.userAgree);
            }

            @Override
            protected void onRxError(Throwable error) {
                Utils.log("protocol", error.getMessage());
                getLocalTxtProtocol();
            }
        });
    }

    //协议加载失败，从本地加载一个
    private void getLocalTxtProtocol() {
        String result = "";
        try {
            File resultFile = new File(filePath);
            if (!resultFile.exists()) {
                File file = new File(filePath);
                InputStream in = new FileInputStream(file);
                byte b[] = new byte[(int) file.length()];
                in.read(b);
                in.close();
                result = new String(b, "UTF-8");
            } else {
                InputStream in = context.getResources().openRawResource(R.raw.protocol);
                int lenght = in.available();
                byte[] buffer = new byte[lenght];
                in.read(buffer);
                in.close();
                result = new String(buffer, "UTF-8");
            }
            mContent.setText(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //获取网络协议成功，写入SD
    private void writeSDProtocol(String protocol) {
        FileOutputStream fos = null;
        DataOutputStream dos = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            fos = new FileOutputStream(filePath);
            dos = new DataOutputStream(fos);
            dos.writeBytes(protocol);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fos) {
                    fos.close();
                }
                if (null != dos) {
                    dos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
