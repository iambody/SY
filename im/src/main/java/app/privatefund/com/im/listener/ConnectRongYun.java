//package app.privatefund.com.im.listener;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.text.TextUtils;
//import android.util.Log;
//
//import com.cgbsoft.lib.AppInfStore;
//import com.cgbsoft.lib.AppManager;
//import com.cgbsoft.lib.utils.cache.SPreference;
//import org.json.JSONException;
//import org.json.JSONObject;
//import java.util.List;
//import io.rong.imkit.RongIM;
//import io.rong.imlib.RongIMClient;
//import io.rong.imlib.model.Conversation;
//
///**
// * @author chenlong
// *         <p>
// *         融云连接，包括请求rctoken和建立连接
// */
//public class ConnectRongYun {
//
//
//    public Context context;
//
//
//    /**
//     * 获取融云token
//     */
//    private void getRcToken(Context context) {
//        final String[] token = {null};
//        int tokenExpired = AppManager.getRongTokenExpired(context);
//        String userId = AppManager.getUserId(context);
//        String rcToken1 = AppManager.getRongToken(context);
//        if ((!userId.equals(AppManager.getUserId(context))) || tokenExpired != 2 || TextUtils.isEmpty(rcToken1) || tokenExpired == 0) {   //融云登录失败值为1，成功为2，首次登录不传值为0
//            AppInfStore.saveRongTokenExpired(context, 2);
//
//            Log.i("----getRcToken", "RcTokenTask");
//            new RcTokenTask(context).start(j.toString(), new HttpResponseListener() {
//                @Override
//                public void onResponse(JSONObject response) {
//                    try {
//                        token[0] = response.getString("rcToken");
//                        SPreference.putString(context, "rctoken", token[0]);  //首次登录保存token
//                        Log.i("----getRcToken", "success");
//                        if (SPreference.getUserInfoData(context) != null) {
//                            connRong(context, SPreference.getUserInfoData(context).getRcToken());
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void onErrorResponse(String error, int statueCode) {
//                    Log.e("----getRcToken", "error");
//                }
//            });
//
//        } else {
//            Log.e("------tokenExpired", "tokenExpired");
//            if (SPreference.getUserInfoData(context) != null) {
//                connRong(context, SPreference.getUserInfoData(context).getRcToken());
//            }
//        }
//    }
//
//    /**
//     * 建立与融云服务器的连接
//     *
//     * @param token
//     */
//    public void connect(final Context context, final String token) {
//        this.context = context;
//        Log.i("ConnectRongYun", "1.token=" + token);
//        String packageName = context.getApplicationInfo().packageName;
//        if (context.getApplicationInfo().packageName.equals(com.cgbsoft.lib.utils.tools.DeviceUtils.getCurProcessName(context))) {
//            /**
//             * IMKit SDK调用第二步,建立与服务器的连接
//             */
//            RongIM.connect(token, new RongIMClient.ConnectCallback() {
//                /**
//                 * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的 Token
//                 */
//                @Override
//                public void onTokenIncorrect() {
//                    SPreference.putInt(context, RONGYUN_TOKEN_SPPREFERENCE_EXPIRED, 1);
//                    Log.d("RCTOKEN", "--onTokenIncorrect");
//                    Log.i("ConnectRongYun", "1.1 RongYun token=" + token);
//                }
//
//                /**
//                 * 连接融云成功
//                 * @param userid 当前 token
//                 */
//                @Override
//                public void onSuccess(String userid) {
//                    Log.i("", "2.RongYun onSuccess  =" + userid);
//                    getTarget();
//                    if (RongIM.getInstance() != null && RongIM.getInstance().getRongIMClient() != null) {
//                        /**
//                         * 设置连接状态变化的监听器.
//                         */
//                        // RongIM.getInstance().getRongIMClient().setConnectionStatusListener(new MyConnectionStatusListener());
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
//                    Log.i("ConnectRongYun", "4.RongYun onError =" + errorCode + ",code=" + errorCode.toString() + ",token=" + token + ",value=" + errorCode.getValue());
//                    getTarget();
////                    RcToken rcToken = new RcToken();
////                    rcToken.setRcToken(token);
////                    EventBus.getDefault().post(rcToken);
//                }
//            });
//        }
//    }
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
