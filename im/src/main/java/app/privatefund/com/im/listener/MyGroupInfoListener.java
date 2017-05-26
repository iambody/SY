package app.privatefund.com.im.listener;

import android.net.Uri;
import android.os.Parcel;
import android.text.TextUtils;
import android.util.Log;

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
        Group group = new Group(groupID, "", null);
        ApiClient.getTestGetGroupInfo(groupID).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                Log.i("MyGroupInfoListener", "RCGroupInfoTask=" + s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    group.setId(jsonObject.getString("id"));
                    group.setName(jsonObject.getString("name"));
                    String imageUrl = jsonObject.getString("headImgUrl");
                    if (!TextUtils.isEmpty(imageUrl)) {
                        if (imageUrl.startsWith("[")) {
                            imageUrl = getUrlFromHead(imageUrl);
                        }
                    } else {
                        imageUrl = NetConfig.defaultRemoteLogin;
                    }
                    group.setPortraitUri(Uri.parse(imageUrl));
                    RongIM.getInstance().refreshGroupInfoCache(group);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void onRxError(Throwable error) {
            }
        });
        return group;
    }

    private String getUrlFromHead(String url) throws JSONException {
        JSONArray jsonArray = new JSONArray(url);
        return ((JSONObject) jsonArray.get(0)).optString("url");
    }
}
