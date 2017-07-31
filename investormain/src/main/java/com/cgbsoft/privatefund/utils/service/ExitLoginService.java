package com.cgbsoft.privatefund.utils.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cgbsoft.lib.AppInfStore;
import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.BaseApplication;
import com.cgbsoft.lib.InvestorAppli;
import com.cgbsoft.lib.R;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.utils.constant.RxConstant;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.tools.Utils;
import com.cgbsoft.lib.widget.FloatView;
import com.cgbsoft.lib.widget.dialog.DefaultDialog;

import app.ndk.com.enter.mvp.ui.LoginActivity;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import qcloud.liveold.mvp.presenters.LiveHelper;

import static android.view.WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;

/**
 * Created by xiaoyu.zhang on 2016/12/21.
 */

public class ExitLoginService extends Service {
//    private FloatView floatView;
//    private View childView;

//    private RelativeLayout rl_vcd;
//    private TextView tv_vcd_title;
//    private ProgressBar pb_vcd;
//    private TextView tv_vcd_message;
//    private Button btn_vcd_sure;
//    private Button iv_vcd_cancel;

    private static int mCode;
//    private ImageView bg_dialog;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        String msg = "";
//        childView = LayoutInflater.from(this).inflate(R.layout.view_dialog_default, null);
//        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(Utils.getScreenWidth(this), Utils.getScreenHeight(this));
//        childView.setLayoutParams(lp);
//
//        floatView = new FloatView(this, 0, 0, childView, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
//        floatView.setIsAllowTouch(false);

//        rl_vcd = (RelativeLayout) childView.findViewById(R.id.rl_vcd);
//        tv_vcd_title = (TextView) childView.findViewById(R.id.tv_vcd_title);
//        pb_vcd = (ProgressBar) childView.findViewById(R.id.pb_vcd);
//        LinearLayout hideLayout = (LinearLayout) childView.findViewById(R.id.dialog_bottom_layout);
//        hideLayout.setVisibility(View.GONE);
//        LinearLayout sureButton = (LinearLayout) childView.findViewById(R.id.dialog_single_layout);
//        sureButton.setVisibility(View.VISIBLE);
//        tv_vcd_message = (TextView) childView.findViewById(R.id.default_dialog_content);
//        btn_vcd_sure = (Button) childView.findViewById(R.id.default_dialog_queren1);
//        iv_vcd_cancel = (Button) childView.findViewById(R.id.default_dialog_quxiao);
//        bg_dialog = (ImageView) childView.findViewById(R.id.bg_download);
//        rl_vcd.setBackgroundResource(R.color.color_89000000);
//        tv_vcd_title.setText("");
//        pb_vcd.setVisibility(View.GONE);

//        if (mCode == 510) {
//            msg = getString(com.cgbsoft.privatefund.R.string.token_error_510_str);
//        } else if (mCode == 511) {
//            msg = getString(com.cgbsoft.privatefund.R.string.token_error_511_str);
//        }
//
//        DefaultDialog dialog = new DefaultDialog(, msg, null, "确认"){
//
//            @Override
//            public void left() {
//
//            }
//
//            @Override
//            public void right() {
//                relogin();
//                dismiss();
//            }
//        };
//        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//        dialog.setCancelable(false);
//        dialog.show();
//        tv_vcd_message.setText(msg);
//        iv_vcd_cancel.setVisibility(View.GONE);
//        btn_vcd_sure.setOnClickListener(v -> relogin());
//        if (AppManager.isInvestor(getApplicationContext())){
//            bg_dialog.setImageResource(R.drawable.bg_investor_dialog);
//            btn_vcd_sure.setBackgroundResource(R.drawable.btn_orange_bg_sel);
//        }
//        floatView.addToWindow();
    }

    private void relogin() {
//        floatView.removeFromWindow();
        Intent intent = new Intent();
        intent.setClass(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        AppInfStore.saveIsLogin(getApplicationContext(), false);
        AppInfStore.saveUserAccount(getApplicationContext(), null);
        AppInfStore.saveRongTokenExpired(this, 0);
        ((InvestorAppli)InvestorAppli.getContext()).setRequestCustom(false);
        SPreference.putBoolean(InvestorAppli.getContext(), Constant.weixin_login, false);
        if(RongIM.getInstance().getRongIMClient()!=null){
            RongIM.getInstance().getRongIMClient().clearConversations(Conversation.ConversationType.PRIVATE);
            RongIM.getInstance().getRongIMClient().clearConversations(Conversation.ConversationType.GROUP);
        }
        if (RongIM.getInstance() != null) {
            RongIM.getInstance().disconnect();
        }

        RxBus.get().post(RxConstant.CLOSE_MAIN_OBSERVABLE, true);

        stopService();
    }

    public static void startService(int code) {
        if (!Utils.isServiceRunning(BaseApplication.getContext(), ExitLoginService.class.getName())) {
            mCode = code;
            BaseApplication.getContext().startService(new Intent(BaseApplication.getContext(), ExitLoginService.class));
        }
    }

    public static void stopService() {
        if (Utils.isServiceRunning(BaseApplication.getContext(), ExitLoginService.class.getName())) {
            BaseApplication.getContext().stopService(new Intent(BaseApplication.getContext(), ExitLoginService.class));
        }
    }
}
