package app.privatefund.com.im.listener;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.cgbsoft.lib.base.model.GroupInfoEntity;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.net.NetConfig;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Group;

/**
 * @author chenlong
 *         <p>
 *         群组信息提供者
 */
public class MyGroupInfoListener implements RongIM.GroupInfoProvider {

    public MyGroupInfoListener() {
    }

    public Group getGroupInfo(String groupID) {
        final Group[] groups = new Group[1];
        ApiClient.getGroupInfo(groupID).subscribe(new RxSubscriber<GroupInfoEntity.Result>() {
            @Override
            protected void onEvent(GroupInfoEntity.Result result) {
                Log.i("MyGroupInfoListener", "RCGroupInfoTask=" + result);
                if (result != null) {
                    String id = result.getId();
                    String name = result.getName();
                    String imageUrl = result.getHeadImgUrl();
                    if (!TextUtils.isEmpty(imageUrl)) {
                        if (imageUrl.startsWith("[")) {
                            imageUrl = getUrlFromHead(imageUrl);
                        }
                    } else {
                        imageUrl = NetConfig.defaultRemoteLogin;
                    }
                    Group group = new Group(id, name, Uri.parse(imageUrl));
                    RongIM.getInstance().refreshGroupInfoCache(group);
                }
            }

            @Override
            protected void onRxError(Throwable error) {
            }
        });
        return groups[0];
    }

//    public Group getGroupInfo(String groupID) {
//        final Group[] groups = new Group[1];
//        ApiClient.getTestGetGroupInfo(groupID).subscribe(new RxSubscriber<String>() {
//            @Override
//            protected void onEvent(String s) {
//                Log.i("MyGroupInfoListener", "RCGroupInfoTask=" + s);
//                try {
//                    JSONObject jsonObject = new JSONObject(s);
//                    String id = jsonObject.getString("id");
//                    String name = jsonObject.getString("name");
//                    String imageUrl = jsonObject.getString("headImgUrl");
//                    if (!TextUtils.isEmpty(imageUrl)) {
//                        if (imageUrl.startsWith("[")) {
//                            imageUrl = getUrlFromHead(imageUrl);
//                        }
//                    } else {
//                        imageUrl = NetConfig.defaultRemoteLogin;
//                    }
//                    Group group = new Group(id, name, Uri.parse(imageUrl));
//                    RongIM.getInstance().refreshGroupInfoCache(group);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            protected void onRxError(Throwable error) {
//            }
//        });
//        return groups[0];
//    }

    private String getUrlFromHead(String url) {
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(url);
            return ((JSONObject) jsonArray.get(0)).optString("url");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return url;
    }
}
