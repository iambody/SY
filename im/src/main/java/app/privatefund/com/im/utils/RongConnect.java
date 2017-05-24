package app.privatefund.com.im.utils;

import android.text.TextUtils;
import android.util.Log;

import com.cgbsoft.lib.AppInfStore;
import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.BaseApplication;
import com.cgbsoft.lib.base.model.RongTokenEntity;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.constant.RxConstant;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.google.gson.Gson;

import java.util.List;

import app.privatefund.com.im.listener.MyReceiveMessageListener;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * @author chenlong
 *
 * 融云连接初始化
 */
public class RongConnect {

    /**
     * 获取融云token
     * @param userId
     */
    public static void initRongTokenConnect(String userId) {
        int rongExpired = AppManager.getRongTokenExpired(BaseApplication.getContext());
        String UID = AppManager.getUserId(BaseApplication.getContext());
        String rongToken = AppManager.getRongToken(BaseApplication.getContext());
        Log.i("LoginPresenter", "rongExpired=" + rongExpired + "-----rongUID=" + UID + "---rongToken=" + rongToken);
        if (!TextUtils.equals(UID, userId) || !TextUtils.equals("2", String.valueOf(rongExpired)) || TextUtils.isEmpty(rongToken)) {
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

    private static void initRongConnect(String rongToken) {
        Log.i("LoginPresenter", "token=" + rongToken);
        if (BaseApplication.getContext().getApplicationInfo().packageName.equals(com.cgbsoft.lib.utils.tools.DeviceUtils.getCurProcessName(BaseApplication.getContext()))) {
            // IMKit SDK调用第二步,建立与服务器的连接
            RongIM.connect(rongToken, new RongIMClient.ConnectCallback() {
                // Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的 Token
                @Override
                public void onTokenIncorrect() {
                    AppInfStore.saveRongTokenExpired(BaseApplication.getContext(), 1);
                    Log.i("LoginPresenter", "RongYun Connect failure");
                }

                @Override
                public void onSuccess(String userid) {
                    Log.i("", "RongYun Connect onSuccess  =" + userid);
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

    private static void getTarget() {
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
