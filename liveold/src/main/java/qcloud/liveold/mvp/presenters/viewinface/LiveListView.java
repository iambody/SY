package qcloud.liveold.mvp.presenters.viewinface;



import java.util.ArrayList;

import qcloud.liveold.mvp.model.LiveInfoJson;


/**
 *  列表页面回调
 */
public interface LiveListView extends MvpView{

    void showFirstPage(ArrayList<LiveInfoJson> livelist);
}
