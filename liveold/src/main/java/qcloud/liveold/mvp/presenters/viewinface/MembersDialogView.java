package qcloud.liveold.mvp.presenters.viewinface;



import java.util.ArrayList;

import qcloud.liveold.mvp.model.MemberInfo;


/**
 * 成员列表回调
 */
public interface MembersDialogView extends MvpView {

    void showMembersList(ArrayList<MemberInfo> data);

}
