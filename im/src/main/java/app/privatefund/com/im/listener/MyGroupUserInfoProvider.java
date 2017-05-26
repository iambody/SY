package app.privatefund.com.im.listener;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.net.NetConfig;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;

import org.json.JSONException;
import org.json.JSONObject;

import io.rong.imkit.RongIM;
import io.rong.imkit.model.GroupUserInfo;

/**
 *  @author chenlong
 *
 *  群组用户信息提供者
 */
public class MyGroupUserInfoProvider implements RongIM.GroupUserInfoProvider {

    private Context context;

    public MyGroupUserInfoProvider(Context context) {
        this.context = context;
    }

    @Override
    public GroupUserInfo getGroupUserInfo(final String groupID, final String userId) {
        GroupUserInfo groupUserInfo = new GroupUserInfo(groupID, userId, "");
        ApiClient.goTestGetRongUserInfo(userId).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                Log.i("MyGroupUserInfoProvider", "getGroupUserInfo=" + s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String imageUrl = jsonObject.getString("portraitUri");
                    if (TextUtils.isEmpty(imageUrl)) {
                        imageUrl = NetConfig.defaultRemoteLogin;
                    }
                    RongIM.getInstance().refreshGroupUserInfoCache(new GroupUserInfo(groupID, userId, jsonObject.getString("name")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void onRxError(Throwable error) {
            }
        });
        return groupUserInfo;
    }
}
