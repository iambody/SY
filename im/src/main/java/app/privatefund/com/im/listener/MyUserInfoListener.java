package app.privatefund.com.im.listener;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

/**
 * Created by lee on 2016/4/7.
 */
public class MyUserInfoListener implements RongIM.UserInfoProvider {
    private Context context;

    public MyUserInfoListener(Context context) {
        this.context = context;
    }

    @Override
    public UserInfo getUserInfo(final String userId) {
        Log.i(this.getClass().getName(), "getUserInfo");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final UserInfo[] userInfo = new UserInfo[1];
        final String[] name = new String[1];
        final String[] portraitUri = new String[1];
//        new RCUserInfoTask(context).start(jsonObject.toString(), new HttpResponseListener() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    System.out.println("---------------RCUserInfoTask");
//                    Log.i("MyUserInfoListener", "RCUserInfoTask=" + response.toString());
//                    name[0] = response.getString("name");
//                    portraitUri[0] = response.getString("portraitUri");
//                    if (TextUtils.isEmpty(portraitUri[0])) {
//                        portraitUri[0] = Domain.defaultRemoteLogin;
//                    }
//                    userInfo[0] = new UserInfo(userId, name[0], Uri.parse(portraitUri[0]));
//                    RongIM.getInstance().refreshUserInfoCache(userInfo[0]);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onErrorResponse(String error, int statueCode) {
//            }
//        });
        return userInfo[0];
    }
}
