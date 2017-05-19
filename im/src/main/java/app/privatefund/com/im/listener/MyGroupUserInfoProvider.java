package app.privatefund.com.im.listener;

import android.content.Context;
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
        JSONObject jsonObject = new JSONObject();
        try {
            System.out.println("--------groupID=" + groupID + "--------userId==" + userId);
            jsonObject.put("uid", userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final GroupUserInfo[] userInfo = new GroupUserInfo[1];
        final String[] name = new String[1];
        final String[] portraitUri = new String[1];
//        new RCUserInfoTask(context).start(jsonObject.toString(), new HttpResponseListener() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    Log.i("MyUserInfoListener", "RCUserInfoTask=" + response.toString());
//                    name[0] = response.getString("name");
//                    portraitUri[0] = response.getString("portraitUri");
//                    userInfo[0] = new GroupUserInfo(groupID, userId, name[0]);
//                    RongIM.getInstance().refreshGroupUserInfoCache(userInfo[0]);
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
