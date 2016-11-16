package com.cgbsoft.lib.utils.net;

import android.text.TextUtils;

import com.cgbsoft.lib.Appli;
import com.cgbsoft.lib.base.model.bean.UserInfo;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.exception.ApiException;
import com.cgbsoft.lib.utils.tools.Util;

import java.util.concurrent.TimeUnit;

import okhttp3.Authenticator;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 网络请求基础类
 * Created by xiaoyu.zhang on 2016/11/7 16:07
 * Email:zhangxyfs@126.com
 */
public class OKHTTP {
    private static OKHTTP mInstance;
    private final OkHttpClient mClient;

    private final int HTTP_CONNECTION_TIMEOUT = 5 * 1000;
    private RequestManager requestManager;

    public static OKHTTP getInstance() {
        if (mInstance == null) {
            synchronized (OKHTTP.class) {
                if (mInstance == null) {
                    mInstance = new OKHTTP();
                }
            }
        }
        return mInstance;
    }

    public OKHTTP() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        if (NetConfig.isLocal) {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }
        Interceptor mTokenInterceptor = chain -> {
            okhttp3.Request originalRequest = chain.request();
            if (!SPreference.isLogin(Appli.getContext())) {
                return chain.proceed(originalRequest);
            }
            UserInfo userInfoData = SPreference.getUserInfoData(Appli.getContext());

            String uid = userInfoData == null ? "" : userInfoData.getId();
            String token = SPreference.getToken(Appli.getContext());
            okhttp3.Request authorised = originalRequest.newBuilder()
                    .addHeader(NetConfig.DefaultParams.uid, TextUtils.isEmpty(uid) ? "" : uid)
                    .addHeader(NetConfig.DefaultParams.token, TextUtils.isEmpty(token) ? "" : token)
                    .addHeader(NetConfig.DefaultParams.deviceId, Util.getIMEI(Appli.getContext()))
                    .addHeader(NetConfig.DefaultParams.appVersion, Util.getVersionCode(Appli.getContext()) + "")
                    .addHeader(NetConfig.DefaultParams.appPlatform, "android")
                    .build();
//            Utils.logJson("ApiClient", NetConfig.Login.uid + ":" + uid + "\n" +
//                    NetConfig.Login.access_token + ":" + token + "\n" +
//                    NetConfig.Login.deviceId + ":" + Utils.getIMEI(Appli.getContext()), "d");
            return chain.proceed(authorised);
        };

        Authenticator mAuthenticator = (route, response) -> {
            if (!response.isSuccessful())
                try {
                    throw new ApiException(String.valueOf(response.code()), "请求错误");
                } catch (ApiException e) {
                    e.printStackTrace();
                }
//            Utils.logJson("ApiClient", response.toString());
            return null;
        };

        OkHttpClient client = new OkHttpClient();
        mClient = client.newBuilder().readTimeout(HTTP_CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
                .connectTimeout(HTTP_CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(HTTP_CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
                .addInterceptor(interceptor)
                .retryOnConnectionFailure(true)
                .addNetworkInterceptor(mTokenInterceptor)
                .authenticator(mAuthenticator).build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(mClient)
                .baseUrl(NetConfig.SERVER_ADD + "/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        requestManager = retrofit.create(RequestManager.class);
    }

    public OkHttpClient getOkClient() {
        return mClient;
    }

    public RequestManager getRequestManager() {
        return requestManager;
    }

    public RequestManager getUrlRequestManager(String serverUrl) {
        return new Retrofit.Builder()
                .client(mClient)
                .baseUrl(serverUrl + "/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build().create(RequestManager.class);
    }
}
