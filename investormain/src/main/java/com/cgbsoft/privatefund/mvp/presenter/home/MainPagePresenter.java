package com.cgbsoft.privatefund.mvp.presenter.home;

import android.content.Context;
import android.text.TextUtils;

import com.cgbsoft.lib.AppInfStore;
import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.BaseApplication;
import com.cgbsoft.lib.TaskInfo;
import com.cgbsoft.lib.base.model.RongTokenEntity;
import com.cgbsoft.lib.base.model.SignInEntity;
import com.cgbsoft.lib.base.model.UserInfoDataEntity;
import com.cgbsoft.lib.base.model.bean.UserInfo;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.contant.Contant;
import com.cgbsoft.lib.utils.cache.OtherDataProvider;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.widget.MToast;
import com.cgbsoft.privatefund.mvp.contract.home.MainPageContract;
import com.commui.prompt.mvp.ui.SignInDialog;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

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
                    jsonArray = new JSONObject(s).getJSONArray("result");
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

    public void toSignIn() {
        addSubscription(ApiClient.testSignIn(AppManager.getUserId(getContext())).subscribe(new RxSubscriber<String>() {
            protected void onEvent(String s) {
                try {
                    JSONObject response = new JSONObject(s);
                    if (response.has("msg")) {
                        new MToast(getContext()).show(response.getString("msg"), 0);
                    } else {

                        SignInDialog signDialog = new SignInDialog(getContext());
                        signDialog.setData(response);
                        signDialog.show();
                        getView().signInSuc();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void onRxError(Throwable error) {

            }
        }));
    }

    @Override
    public void initDayTask() {
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

    @Override
    public void getUserInfo() {
        ApiClient.getUserInfo(AppManager.getUserId(getContext())).subscribe(new RxSubscriber<UserInfoDataEntity.UserInfo>() {
            @Override
            protected void onEvent(UserInfoDataEntity.UserInfo userInfo) {
                if (userInfo != null) {
                    AppInfStore.saveUserInfo(getContext(), userInfo);
                }
            }

            @Override
            protected void onRxError(Throwable error) {
                error.toString();
            }
        });
    }


}
