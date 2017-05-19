package app.privatefund.com.im.listener;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Group;

/**
 *  @author chenlong
 *
 *  群组信息提供者
 */
public class MyGroupInfoListener implements RongIM.GroupInfoProvider {

    private Context context;
    private String id;
    private String name;
    private String headImageUrl;
    private boolean asynchRequest;

    public MyGroupInfoListener(Context context) {
        this.context = context;
    }

    public MyGroupInfoListener (Context context , String id, String name, String headImageUrl, boolean asynchRequest) {
        this.id = id;
        this.name = name;
        this.headImageUrl = headImageUrl;
        this.asynchRequest = asynchRequest;
    }

    @Override
    public Group getGroupInfo(String groupID) {
        if (asynchRequest) {
            return new Group(id, name, Uri.parse(headImageUrl));
        }
        JSONObject jsonObject = new JSONObject();
        try {
            System.out.println("--------getGroupInfo=" + groupID);
            jsonObject.put("groupId", groupID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final Group[] groups = new Group[1];
        final String[] id = new String[1];
        final String[] name = new String[1];
        final String[] headImgUrl = new String[1];
//        new RCGroupInfoTask(context).start(jsonObject.toString(), new HttpResponseListener() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    Log.i("MyGroupInfoListener", "RCGroupInfoTask=" + response.toString());
//                    id[0] = response.getString("id");
//                    name[0] = response.getString("name");
//                    String imageUrl = response.getString("headImgUrl");
//                    if (!TextUtils.isEmpty(imageUrl)) {
//                        if (imageUrl.startsWith("[")) {
//                            headImgUrl[0] = getUrlFromHead(imageUrl);
//                        } else {
//                            headImgUrl[0] = imageUrl;
//                        }
//                    } else {
//                        headImgUrl[0] = Domain.defaultRemoteLogin;
//                    }
//                    groups[0] = new Group(id[0], name[0], Uri.parse(headImgUrl[0]));
//                    RongIM.getInstance().refreshGroupInfoCache(groups[0]);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onErrorResponse(String error, int statueCode) {
//            }
//        });
        return groups[0];
    }

    private String getUrlFromHead(String url) throws JSONException {
        JSONArray jsonArray = new JSONArray(url);
        return ((JSONObject)jsonArray.get(0)).optString("url");
    }
}
