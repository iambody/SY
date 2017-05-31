package qcloud.liveold.mvp.presenters;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.BaseApplication;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import qcloud.liveold.mvp.model.CurLiveInfo;
import qcloud.liveold.mvp.views.LiveActivity;
import rx.Observable;

/**
 * desc
 * Created by yangzonghui on 2017/5/26 11:15
 * Email:yangzonghui@simuyun.com
 *  
 */
public class LivePresenter extends BasePresenterImpl<LiveContract.view> implements LiveContract.presenter {

    public LivePresenter(@NonNull Context context, @NonNull LiveContract.view view) {
        super(context, view);
    }

    @Override
    public void memberJoinRoom(String roomNum, String userId) {
        ApiClient.memberJoinRoom(roomNum, userId).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                getView().joinLiveSuc(s);
            }

            @Override
            protected void onRxError(Throwable error) {

            }
        });
    }

    @Override
    public void hostCreatRoom(String roomNum, String userId) {

    }


    @Override
    public void hostHeartStatus(String roomNum, String userId) {
        ApiClient.hostHeart(roomNum, userId).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                getView().joinLiveSuc(s);
            }

            @Override
            protected void onRxError(Throwable error) {

            }
        });
    }

    @Override
    public void hostCloseLive(String roomNum, String userId) {
        ApiClient.hostCloseLive(roomNum, userId).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
//                getView().joinLiveSuc(s);
            }

            @Override
            protected void onRxError(Throwable error) {

            }
        });
    }

    @Override
    public void memberExitRoom(String roomNum, String userId) {
        ApiClient.memberExitRoom(roomNum, userId).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                getView().memberExitSuc(s);
            }

            @Override
            protected void onRxError(Throwable error) {

            }
        });
    }

    @Override
    public void getMemberList(String roomId, String userId, long offsetOrderId, long orderId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("room_id",roomId);
        map.put("user_id",userId);
        map.put("offsetOrderId",offsetOrderId);
        map.put("orderId",orderId);
        ApiClient.getLiveMember(map).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                getView().getMemberSuc(s);
            }

            @Override
            protected void onRxError(Throwable error) {

            }
        });
    }

    @Override
    public void sendMsg(HashMap<String, Object> map) {

        ApiClient.sendLiveMsg(map).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                getView().getMemberSuc(s);
            }

            @Override
            protected void onRxError(Throwable error) {

            }
        });
    }
}
