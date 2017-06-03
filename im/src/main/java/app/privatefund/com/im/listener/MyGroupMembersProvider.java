package app.privatefund.com.im.listener;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.net.NetConfig;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.CollectionUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
        ApiClient.getTestGetGroupMember(groupId).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                Log.i(MyGroupMembersProvider.this.getClass().getName(), "getGroupMemberst=" + s);
                try {
                    if (!TextUtils.isEmpty(s)) {
                        Gson g = new Gson();{}
                        List<GroupMember> datas = g.fromJson(s, new TypeToken<List<GroupMember>>() {}.getType());
                        iGroupMemberCallback.onGetGroupMembersResult(formatGroupInfoToUserList(datas));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void onRxError(Throwable error) {}
        });
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
