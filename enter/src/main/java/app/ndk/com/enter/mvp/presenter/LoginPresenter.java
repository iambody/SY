package app.ndk.com.enter.mvp.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.cgbsoft.lib.AppInfStore;
import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.BaseApplication;
import com.cgbsoft.lib.base.model.RongTokenEntity;
import com.cgbsoft.lib.base.model.UserInfoDataEntity;
import com.cgbsoft.lib.base.model.WXUnionIDCheckEntity;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.constant.RxConstant;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.LogUtils;
import com.cgbsoft.lib.utils.tools.MD5Utils;
import com.cgbsoft.lib.widget.CustomDialog;
import com.cgbsoft.lib.widget.dialog.LoadingDialog;
import com.google.gson.Gson;

import java.util.List;

import app.ndk.com.enter.R;
import app.ndk.com.enter.mvp.contract.LoginContract;
import app.privatefund.com.im.listener.MyReceiveMessageListener;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import rx.Observable;

/**
 * Created by xiaoyu.zhang on 2016/11/17 11:45
 * Email:zhangxyfs@126.com
 *  
 */
public class LoginPresenter extends BasePresenterImpl<LoginContract.View> implements LoginContract.Presenter {

    public LoginPresenter(Context context, LoginContract.View view) {
        super(context, view);
    }

    /**
     * 登录接口
     *
     * @param un   用户名
     * @param pwd  密码
     * @param isWx 是否微信登录
     */
    @Override
    public void toNormalLogin(@NonNull LoadingDialog loadingDialog, String un, String pwd, boolean isWx) {
        loadingDialog.setLoading(getContext().getString(R.string.la_login_loading_str));
        loadingDialog.show();
        pwd = isWx ? pwd : MD5Utils.getShortMD5(pwd);

        //todo 测试时候调用该接口，
        addSubscription(ApiClient.toTestLogin(un, pwd).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                UserInfoDataEntity.Result loginBean = new Gson().fromJson(s, UserInfoDataEntity.Result.class);
                AppInfStore.saveUserId(getContext().getApplicationContext(),loginBean.userId);
                AppInfStore.saveUserToken(getContext().getApplicationContext(), loginBean.token);
                AppInfStore.saveIsLogin(getContext().getApplicationContext(),true);
                AppInfStore.saveUserAccount(getContext().getApplicationContext(), un);
                Log.i("LoginPresenter", "-------userid=" + loginBean.userId + "------rongYunToken=" + loginBean.token);
                if (loginBean.userInfo != null) {
                    SPreference.saveUserInfoData(getContext(), new Gson().toJson(loginBean.userInfo));
                }
                getRongToken(loginBean.userId);
                loadingDialog.setResult(true, getContext().getString(R.string.la_login_succ_str), 1000, () -> getView().loginSuccess());
            }

            @Override
            protected void onRxError(Throwable error) {
                LogUtils.Log("LoginPresenter", "----error=" + error.toString());
                loadingDialog.dismiss();
//              loadingDialog.setResult(false, getContext().getString(R.string.la_getinfo_error_str), 1000, () -> getView().loginFail());
            }
        }));

        /*addSubscription(ApiClient.toLogin(un, pwd).subscribe(new RxSubscriber<UserInfoDataEntity.Result>() {
            @Override
            protected void onEvent(UserInfoDataEntity.Result loginBean) {
                SPreference.saveUserId(getContext().getApplicationContext(), loginBean.userId);
                SPreference.saveToken(getContext().getApplicationContext(), loginBean.token);

                SPreference.saveLoginFlag(getContext(), true);
                if (loginBean.userInfo != null)
                    SPreference.saveUserInfoData(getContext(), new Gson().toJson(loginBean.userInfo));
                loadingDialog.setResult(true, getContext().getString(R.string.la_login_succ_str), 1000, () -> getView().loginSuccess());
            }

            @Override
            protected void onRxError(Throwable error) {
                loadingDialog.setResult(false, getContext().getString(R.string.la_getinfo_error_str), 1000, () -> getView().loginFail());
            }
        }));*/
    }

    /**
     * 微信登录
     *
     * @param loadingDialog
     * @param unionid
     * @param sex
     * @param nickName
     * @param headimgurl
     */
    @Override
    public void toWxLogin(@NonNull LoadingDialog loadingDialog, CustomDialog.Builder builder, String unionid, String sex, String nickName, String headimgurl) {
        addSubscription(ApiClient.wxTestUnioIDCheck(unionid).flatMap(s -> {
            WXUnionIDCheckEntity.Result result = new Gson().fromJson(s, WXUnionIDCheckEntity.Result.class);
            if (TextUtils.equals(result.isExist, "0")) {
                UserInfoDataEntity.Result r = new UserInfoDataEntity.Result();
                r.token = "-1";
                return Observable.just(new Gson().toJson(r));
            } else {
                return ApiClient.toTestWxLogin(sex, nickName, unionid, headimgurl);
            }
        }).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                UserInfoDataEntity.Result result = new Gson().fromJson(s, UserInfoDataEntity.Result.class);
                if (TextUtils.equals(result.token, "-1")) {
                    loadingDialog.dismiss();
                    builder.setMessage(getContext().getString(R.string.la_cd_content_str, nickName));
                    builder.create().show();
                } else {
                    AppInfStore.saveUserId(getContext().getApplicationContext(), result.token);
                    AppInfStore.saveIsLogin(getContext().getApplicationContext(), true);
                    AppInfStore.saveUserId(getContext().getApplicationContext(), result.userId);
                    if (result.userInfo != null)
                        SPreference.saveUserInfoData(getContext().getApplicationContext(), new Gson().toJson(result.userInfo));
                    if (TextUtils.equals(result.isBind, "2")) {//1:已绑定，2：未绑定，3：绑定中
                        loadingDialog.dismiss();
                        loadingDialog.setResult(true, getContext().getString(R.string.al_need_bind_phone_str), 1000, () -> getView().toBindActivity());
                    } else
                        loadingDialog.setResult(true, getContext().getString(R.string.la_login_succ_str), 1000, () -> getView().loginSuccess());
                }
            }

            @Override
            protected void onRxError(Throwable error) {
                loadingDialog.setResult(false, getContext().getString(R.string.la_getinfo_error_str), 1000, () -> getView().loginFail());
            }
        }));
    }

    /**
     * 微信登录
     * @param loadingDialog
     * @param unionid
     * @param sex
     * @param nickName
     * @param headimgurl
     */
    @Override
    public void toDialogWxLogin(@NonNull LoadingDialog loadingDialog, String unionid, String sex, String nickName, String headimgurl) {
        loadingDialog.setLoading(getContext().getString(R.string.la_login_loading_str));
        loadingDialog.show();
        addSubscription(ApiClient.toTestWxLogin(sex, nickName, unionid, headimgurl).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                UserInfoDataEntity.Result result = new Gson().fromJson(s, UserInfoDataEntity.Result.class);
                AppInfStore.saveUserId(getContext().getApplicationContext(), result.token);
                AppInfStore.saveIsLogin(getContext().getApplicationContext(), true);
                AppInfStore.saveUserId(getContext().getApplicationContext(), result.userId);
                if (result.userInfo != null)
                    SPreference.saveUserInfoData(getContext().getApplicationContext(), new Gson().toJson(result.userInfo));
                if (TextUtils.equals(result.isBind, "2")) {//1:已绑定，2：未绑定，3：绑定中
                    loadingDialog.dismiss();
                    loadingDialog.setResult(true, getContext().getString(R.string.al_need_bind_phone_str), 1000, () -> getView().toBindActivity());
                } else
                    loadingDialog.setResult(true, getContext().getString(R.string.la_login_succ_str), 1000, () -> getView().loginSuccess());
            }

            @Override
            protected void onRxError(Throwable error) {
                loadingDialog.setResult(false, getContext().getString(R.string.la_getinfo_error_str), 1000, () -> getView().loginFail());
            }
        }));
    }

    @Override
    public void getRongToken(String userId) {
        int rongExpired = AppManager.getRongTokenExpired(BaseApplication.getContext());
        String rongUID = AppManager.getUserId(BaseApplication.getContext());
        String rongToken = AppManager.getRongToken(BaseApplication.getContext());
        Log.i("LoginPresenter", "rongExpired=" + rongExpired + "-----rongUID=" + rongUID + "---rongToken=" + rongToken);
        if ((!TextUtils.equals(rongUID, userId) || !TextUtils.equals("2", String.valueOf(rongExpired)))) {
            AppInfStore.saveUserId(BaseApplication.getContext(), userId);
            AppInfStore.saveRongTokenExpired(BaseApplication.getContext(), 2);
            String needExpired = rongExpired == 1 ? "1" : null;
            ApiClient.getTestRongToken(needExpired, userId).subscribe(new RxSubscriber<String>() {
                @Override
                protected void onEvent(String s) {
                    Log.i("LoginPresenter", "getRongToken=" + s);
                    RongTokenEntity.Result result = new Gson().fromJson(s, RongTokenEntity.Result.class);
                    AppInfStore.saveRongToken(BaseApplication.getContext(), result.rcToken);
                    if (SPreference.getUserInfoData(BaseApplication.getContext()) != null) {
                        initRongConnect(result.rcToken);
                    }
                }

                @Override
                protected void onRxError(Throwable error) {
                    Log.e("LoginPresenter", error.getMessage());
                }
            });
        } else {
            if (SPreference.getUserInfoData(BaseApplication.getContext()) != null) {
                initRongConnect(AppManager.getRongToken(BaseApplication.getContext()));
            }
        }
    }

    @Override
    public void initRongConnect(String rongToken) {
        Log.i("LoginPresenter", "token=" + rongToken);
        if (getContext().getApplicationInfo().packageName.equals(com.cgbsoft.lib.utils.tools.DeviceUtils.getCurProcessName(getContext()))) {
            // IMKit SDK调用第二步,建立与服务器的连接
            RongIM.connect(rongToken, new RongIMClient.ConnectCallback() {
                // Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的 Token
                @Override
                public void onTokenIncorrect() {
                    AppInfStore.saveRongTokenExpired(getContext(), 1);
                    Log.i("LoginPresenter", "RongYun Connect failure");
                }

                @Override
                public void onSuccess(String userid) {
                    Log.i("LoginPresenter", "RongYun Connect onSuccess  =" + userid);
                    getTarget();
                    if (RongIM.getInstance() != null && RongIM.getInstance().getRongIMClient() != null) {
                        // 设置连接状态变化的监听器.
                        // RongIM.getInstance().getRongIMClient().setConnectionStatusListener(new MyConnectionStatusListener());
                    }
                    if (RongIM.getInstance() != null) {
                        RongIM.getInstance().setOnReceiveUnreadCountChangedListener(new MyReceiveMessageListener(), Conversation.ConversationType.PRIVATE, Conversation.ConversationType.GROUP);
                        RongIM.getInstance().enableNewComingMessageIcon(true);//显示新消息提醒
                        RongIM.getInstance().enableUnreadMessageIcon(true);//显示未读消息数目
                    }
                    RxBus.get().post(RxConstant.RC_CONNECT_STATUS_OBSERVABLE, true);
                }

                /**
                 * 连接融云失败
                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    Log.i("LoginPresenter", "RongYun Connect error =" + errorCode + ",code=" + errorCode.toString() + ",token=" + rongToken + ",value=" + errorCode.getValue());
                    getTarget();
                    RxBus.get().post(RxConstant.RC_CONNECT_STATUS_OBSERVABLE, false);
                }
            });
        }
    }

    private void getTarget() {
        if (RongIM.getInstance().getRongIMClient() != null) {
            List<Conversation> conversationList = RongIM.getInstance().getRongIMClient().getConversationList();
            Log.i("ConnectRongYun", "5.1 RongYun conversationList = " + conversationList);
            if (conversationList != null) {
                Log.i("ConnectRongYun", "5.2 RongYun conversationList size= " + conversationList.size());
                for (int i = 0; i < conversationList.size(); i++) {
                    Log.i("ConnectRongYun", "5.3 RongYun targetid=" + conversationList.get(i).getTargetId());
                }
            }
        }
    }
}
