package app.privatefund.com.im.listener;

import android.util.Log;

import com.cgbsoft.lib.utils.constant.RxConstant;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.tools.LogOutAccount;
import com.cgbsoft.lib.widget.dialog.DefaultDialog;

import app.privatefund.com.im.bean.RCConnect;
import io.rong.eventbus.EventBus;
import io.rong.imlib.RongIMClient;

import static com.cgbsoft.lib.InvestorAppli.*;

/**
 * @author chenlong
 */
public class MyConnectionStatusListener implements RongIMClient.ConnectionStatusListener {
    @Override
    public void onChanged(ConnectionStatus connectionStatus) {
        switch (connectionStatus) {
            case CONNECTED://连接成功。
                Log.e("Conn", "onChanged: CONNECTED");
                RCConnect rcConnect = new RCConnect();
                rcConnect.setConnectStatus("CONNECTED");
                EventBus.getDefault().post(rcConnect);
                RxBus.get().post(RxConstant.RC_CONNECT_STATUS_OBSERVABLE, true);
                break;
            case DISCONNECTED://断开连接。
                Log.e("Conn", "onChanged: DISCONNECTED");
                break;
            case CONNECTING://连接中。
                Log.e("Conn", "onChanged: CONNECTING");
                break;
            case NETWORK_UNAVAILABLE://网络不可用。
                Log.e("Conn", "onChanged: NETWORK_UNAVAILABLE");
                break;
            case KICKED_OFFLINE_BY_OTHER_CLIENT://用户账户在其他设备登录，本机会被踢掉线
                Log.e("Conn", "onChanged: KICKED_OFFLINE_BY_OTHER_CLIENT");
                new DefaultDialog(getContext(), "您的账号已在其它设备上登录，如非本人操作请尽快修改密码。", "", "确定") {
                    @Override
                    public void left() {
                    }

                    @Override
                    public void right() {
                        dismiss();
                        LogOutAccount returnLogin = new LogOutAccount();
                        returnLogin.accounttExit(getContext());
                    }
                }.show();
//                MToast.makeText(MApplication.mCurrentActivityContext,"您的账户在其他设备登录，本机将会掉线，请重新登录", Toast.LENGTH_LONG).show();
//                JPushInterface.setAlias(MApplication.mCurrentActivityContext, "0", new TagAliasCallback() {
//                    @Override
//                    public void gotResult(int arg0, String arg1, Set<String> arg2) {
//                    }
//                });
//                MApplication.setToken(null);
//                MApplication.setUser(null);
//                MApplication.setUserid(null);
//
//                //退出时清空融云token
//                SPSave.getInstance(MApplication.mContext).putString("rctoken",null);
//                SharedPreferences sharedPreferences = MApplication.mContext.getSharedPreferences("tokenExpired.xml", Context.MODE_PRIVATE);
//                SharedPreferences.Editor edit = sharedPreferences.edit();
//                edit.putInt("tokenExpired", 0);
//                edit.commit();
//
//                RongIM.getInstance().getRongIMClient().clearConversations(Conversation.ConversationType.PRIVATE);
//                if (RongIM.getInstance() != null) {
//                    RongIM.getInstance().logout();
//                }
//
//                SPSave.getInstance(MApplication.mContext).putString("managerUid",null);
//                SPSave.getInstance(MApplication.mContext).putString(Contant.token, "");
//                // SPSave.getInstance(getActivity()).putString(Contant.username, "");
//                SPSave.getInstance(MApplication.mContext).putString(Contant.password, "");
//                SPSave.getInstance(MApplication.mContext).putString(Contant.userid, "");
//                EventBus.getDefault().post(new Close());
//                Intent i = new Intent(MApplication.mContext, LoginActivity.class);
//                MApplication.mContext.startActivity(i);
                break;
        }
    }
}
