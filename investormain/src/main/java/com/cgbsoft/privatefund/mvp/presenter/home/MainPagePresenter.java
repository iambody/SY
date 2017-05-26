package com.cgbsoft.privatefund.mvp.presenter.home;

import android.content.Context;
import android.text.TextUtils;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.BaseApplication;
import com.cgbsoft.lib.TaskInfo;
import com.cgbsoft.lib.base.model.RongTokenEntity;
import com.cgbsoft.lib.base.model.SignInEntity;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.utils.cache.OtherDataProvider;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.privatefund.mvp.contract.home.MainPageContract;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 首页功能实现，数据调用
 * Created by xiaoyu.zhang on 2016/11/10 16:18
 *  
 */
public class MainPagePresenter extends BasePresenterImpl<MainPageContract.View> implements MainPageContract.Presenter {

    public MainPagePresenter(Context context, MainPageContract.View view) {
        super(context, view);
    }

    public void getDataList() {

    }

    @Override
    public void getRongToken() {
        String rongExpired = OtherDataProvider.getRongTokenExpired(BaseApplication.getContext());
        String rongUID = OtherDataProvider.getRongUid(BaseApplication.getContext());
        String rongToken = OtherDataProvider.getRongToken(BaseApplication.getContext());

        String userId = AppManager.getUserId(BaseApplication.getContext());

        if (!TextUtils.equals(rongUID, userId) || !TextUtils.equals("2", rongExpired)) {
            OtherDataProvider.saveRongUid(BaseApplication.getContext(), userId);
            OtherDataProvider.saveRongExpired(BaseApplication.getContext(), "2");

            String needExpired = TextUtils.equals(rongExpired, "1") ? "1" : null;
            ApiClient.getTestRongToken(needExpired, userId).subscribe(new RxSubscriber<String>() {
                @Override
                protected void onEvent(String s) {
                    RongTokenEntity.Result result = new Gson().fromJson(s, RongTokenEntity.Result.class);
                    OtherDataProvider.saveRongToken(BaseApplication.getContext(), result.rcToken);
                }

                @Override
                protected void onRxError(Throwable error) {

                }
            });
        } else {
            if (SPreference.getUserInfoData(BaseApplication.getContext()) != null) {

            }
        }
    }

    /**
     * 获取直播列表
     */
    @Override
    public void getLiveList() {
        ApiClient.getLiveList(AppManager.getUserId(getContext())).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(s);
                } catch (JSONException e) {
                    getView().hasLive(false, null);
                    e.printStackTrace();
                }
                if (jsonArray != null && jsonArray.length() > 0) {
                    Gson g = new Gson();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject liveJson = (JSONObject) jsonArray.get(i);
                            if (liveJson.getInt("state") == 1) {
                                System.out.println("-----------live---on");
                                getView().hasLive(true, liveJson);
                                break;
                            } else {
                                getView().hasLive(false, null);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                } else {
                    getView().hasLive(false, null);
                }
            }

            @Override
            protected void onRxError(Throwable error) {
                error.toString();
            }
        });
    }

    //todo 测试用
    public void toSignIn(){
        String uid = AppManager.getUserId(BaseApplication.getContext());
        ApiClient.testSignIn(uid).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                SignInEntity.Result result = new Gson().fromJson(s, SignInEntity.Result.class);

            }

            @Override
            protected void onRxError(Throwable error) {

            }
        });
    }

    @Override
    public void initDayTask(){
        ApiClient.initDayTask().subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                try {
                    JSONObject ja = new JSONObject(s);
                    JSONArray result = ja.getJSONArray("result");
                    TaskInfo.saveTaskStatus(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void onRxError(Throwable error) {

            }
        });
    }
}
