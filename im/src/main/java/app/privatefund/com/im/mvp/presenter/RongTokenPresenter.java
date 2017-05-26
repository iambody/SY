//package app.privatefund.com.im.mvp.presenter;
//
//import android.content.Context;
//import android.support.annotation.NonNull;
//import android.text.TextUtils;
//import android.util.Log;
//
//import com.cgbsoft.lib.AppInfStore;
//import com.cgbsoft.lib.AppManager;
//import com.cgbsoft.lib.BaseApplication;
//import com.cgbsoft.lib.base.model.RongTokenEntity;
//import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
//import com.cgbsoft.lib.utils.cache.SPreference;
//import com.cgbsoft.lib.utils.net.ApiClient;
//import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
//import com.google.gson.Gson;
//
//import java.util.List;
//
//import app.privatefund.com.im.listener.MyReceiveMessageListener;
//import app.privatefund.com.im.mvp.contract.RongTokenContract;
//import io.rong.imkit.RongIM;
//import io.rong.imlib.RongIMClient;
//import io.rong.imlib.model.Conversation;
//
///**
// * @author chenlong
// */
//public class RongTokenPresenter extends BasePresenterImpl<RongTokenContract.View> implements RongTokenContract.Presenter {
//
//    public RongTokenPresenter(@NonNull Context context, @NonNull RongTokenContract.View view) {
//        super(context, view);
//    }
//
//    @Override
//    public void getRongToken(String userId) {
//        int rongExpired = AppManager.getRongTokenExpired(BaseApplication.getContext());
//        String rongUID = AppManager.getUserId(BaseApplication.getContext());
//        String rongToken = AppManager.getRongToken(BaseApplication.getContext());
//        if ((!TextUtils.equals(rongUID, userId) || !TextUtils.equals("2", String.valueOf(rongExpired))) && TextUtils.isEmpty(rongToken)) {
//            AppInfStore.saveUserId(BaseApplication.getContext(), userId);
//            AppInfStore.saveRongTokenExpired(BaseApplication.getContext(), 2);
//            String needExpired = rongExpired == 1 ? "1" : null;
//            ApiClient.getTestRongToken(needExpired, userId).subscribe(new RxSubscriber<String>() {
//                @Override
//                protected void onEvent(String s) {
//                    RongTokenEntity.Result result = new Gson().fromJson(s, RongTokenEntity.Result.class);
//                    AppInfStore.saveRongToken(BaseApplication.getContext(), result.rcToken);
//                    if (SPreference.getUserInfoData(BaseApplication.getContext()) != null) {
//                        initRongConnect(result.rcToken);
//                    }
//                }
//
//                @Override
//                protected void onRxError(Throwable error) {
//                    Log.e("----getRcToken", error.getMessage());
//                }
//            });
//        } else {
//            if (SPreference.getUserInfoData(BaseApplication.getContext()) != null) {
//                initRongConnect(AppManager.getRongToken(BaseApplication.getContext()));
//            }
//        }
//    }
//
//    @Override
//    public void initRongConnect(String rongToken) {
//        Log.i("ConnectRongYun", "token=" + rongToken);
//        if (getContext().getApplicationInfo().packageName.equals(com.cgbsoft.lib.utils.tools.DeviceUtils.getCurProcessName(getContext()))) {
//             // IMKit SDK调用第二步,建立与服务器的连接
//            RongIM.connect(rongToken, new RongIMClient.ConnectCallback() {
//                // Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的 Token
//                @Override
//                public void onTokenIncorrect() {
//                    AppInfStore.saveRongTokenExpired(getContext(), 1);
//                    Log.i("ConnectRongYun", "RongYun token=" + rongToken);
//                }
//
//                @Override
//                public void onSuccess(String userid) {
//                    Log.i("ConnectRongYun", "RongYun onSuccess  =" + userid);
//                    getTarget();
//                    if (RongIM.getInstance() != null && RongIM.getInstance().getRongIMClient() != null) {
//                      // 设置连接状态变化的监听器.
//                      // RongIM.getInstance().getRongIMClient().setConnectionStatusListener(new MyConnectionStatusListener());
//                    }
//                    if (RongIM.getInstance() != null) {
//                        RongIM.getInstance().setOnReceiveUnreadCountChangedListener(new MyReceiveMessageListener(), Conversation.ConversationType.PRIVATE, Conversation.ConversationType.GROUP);
//                        RongIM.getInstance().enableNewComingMessageIcon(true);//显示新消息提醒
//                        RongIM.getInstance().enableUnreadMessageIcon(true);//显示未读消息数目
//                    }
////                    EventBus.getDefault().post(new CheckManager());
//                }
//
//                /**
//                 * 连接融云失败
//                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
//                 */
//                @Override
//                public void onError(RongIMClient.ErrorCode errorCode) {
//                    Log.i("ConnectRongYun", "4.RongYun onError =" + errorCode + ",code=" + errorCode.toString() + ",token=" + rongToken + ",value=" + errorCode.getValue());
//                    getTarget();
////                    RcToken rcToken = new RcToken();
////                    rcToken.setRcToken(token);
////                    EventBus.getDefault().post(rcToken);
//                }
//            });
//        }
//    }
//
//
//    private void getTarget() {
//        if (RongIM.getInstance().getRongIMClient() != null) {
//            List<Conversation> conversationList = RongIM.getInstance().getRongIMClient().getConversationList();
//            Log.i("ConnectRongYun", "5.1 RongYun conversationList = " + conversationList);
//            if (conversationList != null) {
//                Log.i("ConnectRongYun", "5.2 RongYun conversationList size= " + conversationList.size());
//                for (int i = 0; i < conversationList.size(); i++) {
//                    Log.i("ConnectRongYun", "5.3 RongYun targetid=" + conversationList.get(i).getTargetId());
//                }
//            }
//        }
//    }
//}
