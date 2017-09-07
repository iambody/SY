package com.cgbsoft.lib.utils.tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.cgbsoft.lib.AppInfStore;
import com.cgbsoft.lib.InvestorAppli;
import com.cgbsoft.lib.utils.constant.RxConstant;
import com.cgbsoft.lib.utils.rxjava.RxBus;

import java.util.HashMap;

/**
 * @author chenlong
 *         登出账号
 */
public class LogOutAccount {

    private boolean needExitApp;

    public LogOutAccount() {}

    public LogOutAccount(boolean needExitApp) {
        this.needExitApp = needExitApp;
    }

    public void accounttExit(Context context) {
        try {
            RxBus.get().post(RxConstant.CLOSE_MAIN_OBSERVABLE, true);
            AppInfStore.saveIsLogin(context, false);
            AppInfStore.saveUserInfo(context, null);
            AppInfStore.saveUserId(context, "");
            AppInfStore.saveUserToken(context, "");
            AppInfStore.saveRongTokenExpired(context, 0);
            ((InvestorAppli) InvestorAppli.getContext()).setRequestCustom(false);
            AppInfStore.saveUserAccount(context, null);
            AppInfStore.saveIsVisitor(context, true);
            HashMap<String,Object>map=new HashMap<>();
            map.put("ialoginout",true);
            map.put("fromValidatePassword",needExitApp);
//            NavigationUtils.startActivityByRouter(context, "enter_loginactivity",map, Intent.FLAG_ACTIVITY_CLEAR_TASK);
            NavigationUtils.startActivityByRouter(context, "enter_loginactivity",map);
            ((Activity) context).finish();

            //退出时清空融云token en
//            SharedPreferences sharedPreferences = context.getSharedPreferences("tokenExpired.xml", Context.MODE_PRIVATE);
//            SharedPreferences.Editor edit = sharedPreferences.edit();
//            edit.putInt("tokenExpired", 0);
//            edit.commit();
//            if (RongIM.getInstance() != null) {
//                RongIM.getInstance().logout();
//            }

//            mLoginHeloper.imLogout();
//            EventBus.getDefault().post(new Close());
//            ((MApplication)MApplication.getInstance()).setRequestCustom(false);
//            ((MApplication) MApplication.getInstance()).setShowPlaying(false);
//            final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.login");
            // 添加微信平台
//            UMWXHandler wxHandler = new UMWXHandler(context, Contant.weixin_appID, Contant.weixin_appSecret);
//            wxHandler.addToSocialSDK();
//            mController.deleteOauth(context, SHARE_MEDIA.WEIXIN, new SocializeListeners.SocializeClientListener() {
//                @Override
//                public void onStart() {
//                }
//
//                @Override
//                public void onComplete(int i, SocializeEntity socializeEntity) {
//
//                }
//            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
