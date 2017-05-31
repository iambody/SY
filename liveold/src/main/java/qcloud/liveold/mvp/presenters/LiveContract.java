package qcloud.liveold.mvp.presenters;


import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.view.BaseView;

import java.util.HashMap;

/**
 * desc  直播
 * Created by yangzonghui on 2017/5/15 21:11
 * Email:yangzonghui@simuyun.com
 *  
 */
public interface LiveContract {
    interface presenter extends BasePresenter {

        void memberJoinRoom(String roomNum,String userId);

        void hostCreatRoom(String roomNum,String userId);

        void hostHeartStatus(String roomNum,String userId);

        void hostCloseLive(String roomNum,String userId);

        void memberExitRoom(String roomNum,String userId);

        void getMemberList(String roomId,String userId,long offsetOrderId,long orderId);

        void sendMsg(HashMap<String,Object> map);

    }

    interface view extends BaseView{

        void joinLiveSuc(String s);

        void closeLiveSuc(String s);

        void memberExitSuc(String s);

        void getMemberSuc(String s);

        void sendMsgSuc(String s);
    }
}
