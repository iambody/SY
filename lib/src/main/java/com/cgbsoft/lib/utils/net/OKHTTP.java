package com.cgbsoft.lib.utils.net;

import android.content.Context;
import android.text.TextUtils;

import com.cgbsoft.lib.Appli;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.exception.ApiException;
import com.cgbsoft.lib.utils.tools.Utils;

import java.util.concurrent.TimeUnit;

import okhttp3.Authenticator;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
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

    /**
     * 初始化
     */
    private OKHTTP() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        if (NetConfig.isLocal) {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }
        Interceptor mTokenInterceptor = chain -> {
            Context context = Appli.getContext();
            okhttp3.Request originalRequest = chain.request();

            String uid = SPreference.getUserId(context);
            String token = SPreference.getToken(context);

            Request.Builder builder = originalRequest.newBuilder();
            if (!TextUtils.isEmpty(uid)) {
                builder.addHeader(NetConfig.DefaultParams.uid, uid);
            }
            if (!TextUtils.isEmpty(token)) {
                builder.addHeader(NetConfig.DefaultParams.token, token);
            }
            builder.addHeader(NetConfig.DefaultParams.deviceId, Utils.getIMEI(context))
                    .addHeader(NetConfig.DefaultParams.appVersion, Utils.getVersionCode(context) + "")
                    .addHeader(NetConfig.DefaultParams.appPlatform, "android");
            okhttp3.Request authorised = builder.build();
            Utils.log("ApiClient", "uid:" + uid + "\n" +
                    "token:" + token + "\n" +
                    "deviceId:" + Utils.getIMEI(Appli.getContext()), "d");
            return chain.proceed(authorised);
        };

        Authenticator mAuthenticator = (route, response) -> {
            if (!response.isSuccessful())
                try {
                    throw new ApiException(String.valueOf(response.code()), "请求错误");
                } catch (ApiException e) {
                    e.printStackTrace();
                }
            Utils.log("ApiClient", response.toString());
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

    RequestManager getRequestManager() {
        return requestManager;
    }

    RequestManager getRequestManager(boolean isNeedReset) {
        if (isNeedReset) {
            mInstance = new OKHTTP();
        }
        return requestManager;
    }

    RequestManager getRequestManager(String serverUrl, boolean isNeedGson) {
        if (isNeedGson) {
            return getRequestManager(serverUrl);
        } else {
            return new Retrofit.Builder()
                    .client(mClient)
                    .baseUrl(serverUrl + "/")
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build().create(RequestManager.class);
        }
    }


    RequestManager getRequestManager(String serverUrl) {
        return new Retrofit.Builder()
                .client(mClient)
                .baseUrl(serverUrl + "/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build().create(RequestManager.class);
    }
}
