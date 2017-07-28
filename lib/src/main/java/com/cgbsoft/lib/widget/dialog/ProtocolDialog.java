package com.cgbsoft.lib.widget.dialog;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.cgbsoft.lib.BaseApplication;
import com.cgbsoft.lib.R;
import com.cgbsoft.lib.base.model.ProtocolEntity;
import com.cgbsoft.lib.base.model.bean.OtherInfo;
import com.cgbsoft.lib.utils.cache.CacheManager;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.db.DBConstant;
import com.cgbsoft.lib.utils.db.DaoUtils;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.Utils;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import butterknife.ButterKnife;

import static com.cgbsoft.lib.utils.cache.CacheManager.FILE;

/**
 * 规则显示
 * Created by xiaoyu.zhang on 2016/11/22 15:36
 * Email:zhangxyfs@126.com
 *  
 */
public class ProtocolDialog implements DBConstant {
    private TextView mConfirmTv, titleTv, mContentTv;
    private int type;
    private BaseDialog dialog;
    private String filePath = CacheManager.getCachePath(BaseApplication.getContext(), FILE) + "pro.tp";

    private DaoUtils daoUtils;
    private Context context;

    public ProtocolDialog(Context context, int type, Handler handler) {
        this.type = type;
        init(context);
    }

    private void init(Context context) {
        daoUtils = new DaoUtils(context, DaoUtils.W_OTHER);
        this.context = context;
        dialog = new BaseDialog(context, R.style.CenterCompatDialogTheme);
        dialog.setContentView(R.layout.view_protocol_dialog);
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams windowLP = window.getAttributes();
        windowLP.width = Utils.getScreenWidth(context);
        windowLP.height = Utils.getScreenHeight(context);
        window.setAttributes(windowLP);
        window.setGravity(Gravity.CENTER | Gravity.CENTER);

        titleTv = ButterKnife.findById(dialog, R.id.protocol_title);
        mContentTv = ButterKnife.findById(dialog, R.id.protocal_content);
        mConfirmTv = ButterKnife.findById(dialog, R.id.protocol_confirm);

        mContentTv.setMovementMethod(new ScrollingMovementMethod());

//        if (AppManager.isInvestor(context)) {
//            titleTv.setTextColor(0xfff47900);
//            mConfirmTv.setBackgroundResource(R.drawable.shape_f47900);
//        } else {
//            titleTv.setTextColor(0xffd73a2e);
//            mConfirmTv.setBackgroundResource(R.drawable.all_red_btn);
//        }

        mConfirmTv.setOnClickListener(v -> {
            if (type == 0) {
                SPreference.saveVisableProtocol(context);
            } else if (type == 1) {
                SPreference.saveVisableMessage(context);
            } else if (type == 2) {

            }
            daoUtils.destory();
            daoUtils = null;
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
                displayLocalTxt(context, "message.txt");

                titlestr = context.getString(R.string.protocol_dialog_message);
                confirmStr = context.getString(R.string.protocol_know);
                mContentTv.setText(result);
            } else if (type == 2) {
                titlestr = "资产证明资料";
                confirmStr = context.getString(R.string.protocol_know);

                displayLocalTxt(context, "asset.txt");
            }

            titleTv.setText(titlestr);
            mConfirmTv.setText(confirmStr);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getProtocolData() {
        ApiClient.getProtocol().subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                ProtocolEntity.Result result = new Gson().fromJson(s, ProtocolEntity.Result.class);
                if (TextUtils.isEmpty(result.result)) {
                    return;
                }
                mContentTv.setText(result.result);
                daoUtils.saveOrUpdataOther(PROTO_TITLE, result.result);
            }

            @Override
            protected void onRxError(Throwable error) {
                Utils.log("protocol", error.getMessage());
                getLocalTxtProtocol(context);
            }
        });
    }

    //协议加载失败，从本地加载一个
    private void getLocalTxtProtocol(Context context) {
        OtherInfo info = daoUtils.getOtherInfo(PROTO_TITLE);
        if (info != null) {
            mContentTv.setText(info.getContent());
        } else {
            displayLocalTxt(context, "protocol.txt");
        }
    }

    private void displayLocalTxt(Context context, String fileName) {
        try {
            File file = new File(CacheManager.getCachePath(context, CacheManager.RES) + fileName);
            if (file.isFile() && file.exists()) {
                InputStreamReader reader = new InputStreamReader(new FileInputStream(file), "utf-8");
                BufferedReader bufferedReader = new BufferedReader(reader);
                String text = null;
                StringBuilder sb = new StringBuilder();
                while ((text = bufferedReader.readLine()) != null) {
                    sb.append(text).append("\n");
                }
                mContentTv.setText(sb.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
