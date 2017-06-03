package app.privatefund.com.im.listener;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.cgbsoft.lib.base.model.RongUserEntity;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.net.NetConfig;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;

import org.json.JSONException;
import org.json.JSONObject;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

/**
 * @author chenlong
 *
 * 融云用户信息
 */
public class MyUserInfoListener implements RongIM.UserInfoProvider {

    @Override
    public UserInfo getUserInfo(final String userId) {
        Log.i(this.getClass().getName(), "getUserInfo");
        UserInfo[] userInfos = new UserInfo[1];
        ApiClient.getRongUserInfo(userId).subscribe(new RxSubscriber<RongUserEntity.Result>() {
            @Override
            protected void onEvent(RongUserEntity.Result result) {
                Log.i("MyUserInfoListener", "RCUserInfoTask=" + result);
                if (result != null) {
                    String imageUrl = result.getPortraitUri();
                    if (TextUtils.isEmpty(imageUrl)) {
                        imageUrl = NetConfig.defaultRemoteLogin;
                    }
                    UserInfo userInfo = new UserInfo(userId, result.getName(), Uri.parse(imageUrl));
                    RongIM.getInstance().refreshUserInfoCache(userInfo);
                }
            }

            @Override
            protected void onRxError(Throwable error) {
            }
        });
        return userInfos[0];
    }
//    @Override
//    public UserInfo getUserInfo(final String userId) {
//        Log.i(this.getClass().getName(), "getUserInfo");
//        UserInfo[] userInfos = new UserInfo[1];
//        ApiClient.goTestGetRongUserInfo(userId).subscribe(new RxSubscriber<String>() {
//            @Override
//            protected void onEvent(String s) {
//                Log.i("MyUserInfoListener", "RCUserInfoTask=" + s);
//                try {
//                    JSONObject jsonObject = new JSONObject(s);
//                    String imageUrl = jsonObject.getString("portraitUri");
//                    if (TextUtils.isEmpty(imageUrl)) {
//                        imageUrl = NetConfig.defaultRemoteLogin;
//                    }
//                    UserInfo userInfo = new UserInfo(userId, jsonObject.getString("name"), Uri.parse(imageUrl));
//                    RongIM.getInstance().refreshUserInfoCache(userInfo);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            protected void onRxError(Throwable error) {
//            }
//        });
//        return userInfos[0];
//    }
}
