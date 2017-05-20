package app.live.com.mvp.presenter.viewinface;

import java.util.ArrayList;

import app.live.com.mvp.model.MemberInfo;


/**
 * 成员列表回调
 */
public interface MembersDialogView extends MvpView {

    void showMembersList(ArrayList<MemberInfo> data);

}
