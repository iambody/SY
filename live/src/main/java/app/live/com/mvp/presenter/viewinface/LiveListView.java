package app.live.com.mvp.presenter.viewinface;


import java.util.ArrayList;

import app.live.com.mvp.model.RoomInfoJson;
import app.live.com.mvp.presenter.UserServerHelper;


/**
 *  列表页面回调
 */
public interface LiveListView extends MvpView{


    void showRoomList(UserServerHelper.RequestBackInfo result, ArrayList<RoomInfoJson> roomlist);
}
