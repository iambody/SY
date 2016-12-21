package com.cgbsoft.privatefund.mvp.presenter.start;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;

import com.cgbsoft.lib.base.model.AppResourcesEntity;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.mvp.model.video.VideoInfoModel;
import com.cgbsoft.lib.utils.cache.CacheManager;
import com.cgbsoft.lib.utils.cache.OtherDataProvider;
import com.cgbsoft.lib.utils.constant.VideoStatus;
import com.cgbsoft.lib.utils.db.DBConstant;
import com.cgbsoft.lib.utils.db.DaoUtils;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.privatefund.mvp.contract.start.WelcomeContract;
import com.google.gson.Gson;
import com.lzy.okserver.download.DownloadManager;
import com.lzy.okserver.download.DownloadService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * 欢迎页功能实现，数据调用
 * Created by xiaoyu.zhang on 2016/11/16 09:04
 * Email:zhangxyfs@126.com
 *  
 */
public class WelcomePersenter extends BasePresenterImpl<WelcomeContract.View> implements WelcomeContract.Persenter {
    private Observable<Boolean> welcomeFinishObservable;
    private DaoUtils daoUtils, daoVideoUtils;

    public WelcomePersenter(Context context, WelcomeContract.View view) {
        super(context, view);
        daoUtils = new DaoUtils(context, DaoUtils.W_OTHER);
        daoVideoUtils = new DaoUtils(context, DaoUtils.W_VIDEO);
        DownloadManager downloadManager = DownloadService.getDownloadManager();
        downloadManager.getThreadPool().setCorePoolSize(1);
        downloadManager.setTargetFolder(CacheManager.getCachePath(context, CacheManager.VIDEO));
        downloadManager.stopAllTask();
        List<VideoInfoModel> list = daoVideoUtils.getAllVideoInfo();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                VideoInfoModel vim = list.get(i);
                if (vim.status == VideoStatus.WAIT || vim.status == VideoStatus.DOWNLOADING) {
                    vim.status = VideoStatus.NONE;
                    daoVideoUtils.saveOrUpdateVideoInfo(vim);
                }
            }
        }
    }

    /**
     * 获取首页背景图片，应用升级信息。
     */
    @Override
    public void getData() {
        addSubscription(ApiClient.getTestAppResources().subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                AppResourcesEntity.Result appResources = new Gson().fromJson(s, AppResourcesEntity.Result.class);
                if (appResources != null) {
                    daoUtils.saveOrUpdataOther(DBConstant.APP_UPDATE_INFO, new Gson().toJson(appResources));
                    OtherDataProvider.saveWelcomeImgUrl(getContext().getApplicationContext(), appResources.img916);
                    if (getView() != null)
                        getView().getDataSucc(appResources.img916);
                } else {
                    if (getView() != null)
                        getView().getDataSucc("");
                }
            }

            @Override
            protected void onRxError(Throwable error) {
                String url = OtherDataProvider.getWelcomeImgUrl(getContext().getApplicationContext());
                if (!TextUtils.isEmpty(url)) {
                    if (getView() != null)
                        getView().getDataSucc(url);
                } else {
                    if (getView() != null)
                        getView().getDataError(error);
                }
            }
        }));

        /*addSubscription(ApiClient.getAppResources().subscribe(new RxSubscriber<AppResourcesEntity.Result>() {
            @Override
            protected void onEvent(AppResourcesEntity.Result appResources) {
                if (appResources != null) {
                    daoUtils.saveOrUpdataOther(DBConstant.APP_UPDATE_INFO, new Gson().toJson(appResources));
                    OtherDataProvider.saveWelcomeImgUrl(getContext().getApplicationContext(), appResources.img916);
                    if (getView() != null)
                        getView().getDataSucc(appResources.img916);
                } else {
                    if (getView() != null)
                        getView().getDataSucc("");
                }

            }

            @Override
            protected void onRxError(Throwable error) {
                String url = OtherDataProvider.getWelcomeImgUrl(getContext().getApplicationContext());
                if (!TextUtils.isEmpty(url)) {
                    if (getView() != null)
                        getView().getDataSucc(url);
                } else {
                    if (getView() != null)
                        getView().getDataError(error);
                }
            }
        }));*/
    }

    /**
     * 关闭当前页面
     */
    @Override
    public void createFinishObservable() {
        welcomeFinishObservable = RxBus.get().register(WELCOME_FINISH_OBSERVABLE, Boolean.class);
        welcomeFinishObservable.observeOn(AndroidSchedulers.mainThread())
                .filter(b -> b).subscribe(new RxSubscriber<Boolean>() {
            @Override
            protected void onEvent(Boolean aBoolean) {
                RxBus.get().unregister(WELCOME_FINISH_OBSERVABLE, welcomeFinishObservable);
                getView().finishThis();
            }

            @Override
            protected void onRxError(Throwable e) {

            }
        });
    }

    /**
     * 初始化一些信息
     */
    @Override
    public void toInitInfo() {
        addSubscription(ApiClient.getIP().subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String json) {
                if (json.contains("var returnCitySN")) {
                    String substring = json.substring(19, json.length() - 1);
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(substring);
                        String cip = jsonObject.getString("cip");
                        String cname = jsonObject.getString("cname");
                        OtherDataProvider.saveCity(getContext().getApplicationContext(), cname);
                        OtherDataProvider.saveIP(getContext().getApplicationContext(), cip);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            protected void onRxError(Throwable error) {
            }
        }));
    }

    @Override
    public void getMyLocation() {
        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
//        if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
//            return;
//        }
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location curLoc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (null == curLoc) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    String strAddr = getAddressFromLocation(location);
                    //todo
                    if (TextUtils.isEmpty(strAddr)) {
//                        view.onLocationChanged(-1, 0, 0, strAddr);
                    } else {
//                        view.onLocationChanged(0, location.getLatitude(), location.getLongitude(), strAddr);
                    }
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                @Override
                public void onProviderEnabled(String provider) {
                }

                @Override
                public void onProviderDisabled(String provider) {
                }
            });
        } else {
            String strAddr = getAddressFromLocation(curLoc);
            //todo
            if (TextUtils.isEmpty(strAddr)) {
//                view.onLocationChanged(-1, 0, 0, strAddr);
            } else {
//                view.onLocationChanged(0, curLoc.getLatitude(), curLoc.getLongitude(), strAddr);
            }
        }
    }

    private String getAddressFromLocation(Location location) {
        Geocoder geocoder = new Geocoder(getContext());

        try {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            List<Address> list = geocoder.getFromLocation(latitude, longitude, 1);
            if (list.size() > 0) {
                Address address = list.get(0);
                return address.getAddressLine(0);
            }
        } catch (IOException e) {
        }

        return "";
    }

    @Override
    public void detachView() {
        super.detachView();
        daoUtils.destory();
        daoUtils = null;
        daoVideoUtils.destory();
        daoVideoUtils = null;
    }
}
