package app.privatefund.com.im.listener;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.cgbsoft.lib.utils.net.NetConfig;
import com.cgbsoft.lib.utils.tools.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import app.privatefund.com.im.bean.GroupMember;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

/**
 * @author chenlong
 *         <p>
 *         群组成员信息提供者
 */
public class MyGroupMembersProvider implements RongIM.IGroupMembersProvider {

    private Context context;

    public MyGroupMembersProvider(Context context) {
        this.context = context;
    }

    @Override
    public void getGroupMembers(String groupId, final RongIM.IGroupMemberCallback iGroupMemberCallback) {
//        new GroupChatGroupMemberTask(context).start(ApiParams.requestGroupMemberList(groupId),
//            new HttpResponseListener() {
//                @Override
//                public void onResponse(JSONObject response) {
//                    Log.i(MyGroupMembersProvider.this.getClass().getName(), "getGroupMemberst=" + response.toString());
//                    try {
//                        String string = response.get("result").toString();
//                        if (!TextUtils.isEmpty(string)) {
//                            Gson g = new Gson();
//                            List<GroupMember> datas = g.fromJson(string, new TypeToken<List<GroupMember>>() {}.getType());
//                            iGroupMemberCallback.onGetGroupMembersResult(formatGroupInfoToUserList(datas));
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void onErrorResponse(String error, int statueCode) {
//                }
//            });
    }

    private List<UserInfo> formatGroupInfoToUserList(List<GroupMember> list) {
        List<UserInfo> returnList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(list)) {
            for (GroupMember groupMember : list) {
                List<GroupMember.GroupMemberPerson> orgNumber = groupMember.getMembers();
                for (GroupMember.GroupMemberPerson person : orgNumber) {
                    if (!TextUtils.isEmpty(person.getUserId()) && !TextUtils.isEmpty(person.getUserName())) {
                        String url = person.getImageUrl();
                        if (TextUtils.isEmpty(url)) {
                            url = NetConfig.defaultRemoteLogin;
                        }
                        UserInfo userInfo = new UserInfo(person.getUserId(), person.getUserName(), Uri.parse(url));
                        returnList.add(userInfo);
                    }
                }
            }
        }
        return returnList;
    }
}
