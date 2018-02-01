package com.cgbsoft.privatefund.mvp.presenter.home;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.cgbsoft.lib.AppInfStore;
import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.TaskInfo;
import com.cgbsoft.lib.base.model.UserInfoDataEntity;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.contant.Contant;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.BStrUtils;
import com.cgbsoft.lib.utils.tools.LogUtils;
import com.cgbsoft.lib.widget.dialog.HomeSignDialog;
import com.cgbsoft.privatefund.bean.commui.SignBean;
import com.cgbsoft.privatefund.bean.product.PublicFundInf;
import com.cgbsoft.privatefund.mvp.contract.home.MainPageContract;
import com.cgbsoft.privatefund.mvp.ui.home.RedPacketActivity;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class MainPagePresenter extends BasePresenterImpl<MainPageContract.View> implements MainPageContract.Presenter {

    public MainPagePresenter(Context context, MainPageContract.View view) {
        super(context, view);
    }

    public void getDataList() {

    }

    /**
     * 红包雨
     */
    @Override
    public void loadRedPacket() {

        ApiClient.loadRedPacket(AppManager.getUserId(getContext())).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject result = jsonObject.getJSONObject("result");
                    String redpage_url = result.getString("redpage_url");
                    if (!TextUtils.isEmpty(redpage_url)) {
                        Intent intent = new Intent(getContext(), RedPacketActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.putExtra(Contant.red_packet_url, redpage_url);
                        getContext().startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void onRxError(Throwable error) {
                String s = error.toString();
            }
        });
    }

    /**
     * 公募
     */
    @Override
    public void loadPublicFundInf() {
        addSubscription(ApiClient.getPublicFundInf().subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                try {
                    if (!BStrUtils.isEmpty(s)) {
                        JSONObject jsonObject = new JSONObject(s);
                        if (!jsonObject.has("result")) return;
                        String resultStr = jsonObject.getString("result");
                        AppInfStore.savePublicFundInf(getContext(), new Gson().fromJson(resultStr, PublicFundInf.class));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            protected void onRxError(Throwable error) {
                Log.i("s", "sss");
            }
        }));
    }

    /**
     * 获取直播列表
     */
    @Override
    public void getProLiveList() {
        ApiClient.getProLiveList(AppManager.getUserId(getContext())).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONObject(s).getJSONArray("result");
                    System.out.println("-----------live---result---:" + jsonArray.toString());
                } catch (JSONException e) {
                    getView().hasLive(2, null);
                    e.printStackTrace();
                }
                int liveState = 0;
                if (jsonArray != null && jsonArray.length() > 0) {
                    Gson g = new Gson();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject liveJson = (JSONObject) jsonArray.get(i);
                            if (liveJson.getInt("state") == 1) {
                                System.out.println("-----------live---on---:" + liveJson);
                                getView().hasLive(1, liveJson);
                                liveState = 1;
                                break;
                            } else {
                                liveState = 0;
//                                getView().hasLive(false, null);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if (liveState == 0) {
                        try {
                            getView().hasLive(0, (JSONObject) jsonArray.get(0));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                } else {
                    getView().hasLive(2, null);
                }
            }

            @Override
            protected void onRxError(Throwable error) {
                error.toString();
            }
        });
    }

    public void actionPoint(HashMap<String, Object> map) {
        addSubscription(ApiClient.ActionPoint(map).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                try {
                    JSONObject js = new JSONObject(s);
                    JSONObject result = js.getJSONObject("result");
                    int ydMallState = Integer.parseInt(result.getString("ydMallState"));
                    if (ydMallState != 1) {
                        SPreference.putBoolean(getContext(), "ydMallState", false);
                    } else {
                        SPreference.putBoolean(getContext(), "ydMallState", true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void onRxError(Throwable error) {
                error.toString();
            }
        }));
    }

    public void toSignIn() {
        addSubscription(ApiClient.SxySign().subscribe(new RxSubscriber<String>() {
            protected void onEvent(String s) {
                if (!BStrUtils.isEmpty(s)) {
                    SignBean signBean = new Gson().fromJson(getV2String(s), SignBean.class);
                    if ("1".equals(signBean.resultCode)) {
                        HomeSignDialog homeSignDialog = new HomeSignDialog(getContext(), signBean);
                        homeSignDialog.show();
                    }
                }
            }

            @Override
            protected void onRxError(Throwable error) {
                LogUtils.Log("s", "s");
            }
        }));
    }

    @Override
    public void initDayTask() {
        addSubscription(ApiClient.initDayTask().subscribe(new RxSubscriber<String>() {
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
        }));
    }

    @Override
    public void getUserInfo(boolean tofreshHome) {
        addSubscription(ApiClient.getUserInfo(AppManager.getUserId(getContext())).subscribe(new RxSubscriber<UserInfoDataEntity.UserInfo>() {
            @Override
            protected void onEvent(UserInfoDataEntity.UserInfo userInfo) {
                if (userInfo != null) {
                    AppInfStore.saveUserInfo(getContext(), userInfo);
                    if ("0".equals(userInfo.isSingIn)) {
                        toSignIn();
                    }
//                    getView().toFreshUserinfHome()
                    if (tofreshHome) {
                        getView().toFreshUserinfHome();
                    }
                }
            }

            @Override
            protected void onRxError(Throwable error) {
                error.toString();
            }
        }));
    }
//    /**
//     * 下载文件
//     * @param url
//     * @param target 保存的文件
//     * @param callback
//     */
//    public void getZipFile(String url, File target, FileDownloadCallback callback) {
//        if (!TextUtils.isEmpty(url) && target != null) {
//            FileDownloadTask task = new FileDownloadTask(url, target, callback);
//            task.execute();
//        }
//    }
}
