package com.cgbsoft.lib.utils.net;

import android.content.Context;
import android.text.TextUtils;

import com.cgbsoft.lib.Appli;
import com.cgbsoft.lib.base.mvp.model.BaseResult;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.exception.ApiException;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.tools.NetUtils;
import com.cgbsoft.lib.utils.tools.Utils;
import com.google.gson.Gson;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.concurrent.TimeUnit;

import okhttp3.Authenticator;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.cgbsoft.lib.utils.constant.RxConstant.RE_LOGIN_OBSERVABLE;

/**
 * 网络请求基础类
 * Created by xiaoyu.zhang on 2016/11/7 16:07
 * Email:zhangxyfs@126.com
 */
public class OKHTTP {
    private static final String TAG = "OKHTTP";
    private static OKHTTP mInstance;
    private final OkHttpClient mClient;

    public static final int HTTP_CONNECTION_TIMEOUT = 5 * 1000;
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
        //log 拦截器
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        if (NetConfig.isLocal) {
            logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            logInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }
        //添加head 的拦截器
        Interceptor headInterceptor = chain -> {
            Context context = Appli.getContext();
            okhttp3.Request originalRequest = chain.request();

            String uid = SPreference.getUserId(context);
            String token = SPreference.getToken(context);

            uid = TextUtils.isEmpty(uid) ? "" : uid;
            token = TextUtils.isEmpty(token) ? "" : token;

            Request.Builder builder = originalRequest.newBuilder();
            builder.addHeader(NetConfig.DefaultParams.uid, uid);
            builder.addHeader(NetConfig.DefaultParams.token, token);
            builder.addHeader(NetConfig.DefaultParams.deviceId, Utils.getIMEI(context));
            builder.addHeader(NetConfig.DefaultParams.appVersion, String.valueOf(Utils.getVersionCode(context)));
            builder.addHeader(NetConfig.DefaultParams.appPlatform, "android");
            okhttp3.Request authorised = builder.build();
            Utils.log("ApiClient", "uid:" + uid + "\n" +
                    "token:" + token + "\n" +
                    "deviceId:" + Utils.getIMEI(Appli.getContext()) + "\n" +
                    "version:" + Utils.getVersionCode(context), "d");
            return chain.proceed(authorised);
        };

        //http code 拦截器
        Interceptor codeInterceptor = chain -> {
            Response response = chain.proceed(chain.request());
            ResponseBody responseBody = response.body();
            Charset UTF8 = Charset.forName("UTF-8");

            Utils.log(TAG, response.request().url().toString() + " " + response.code(), "d");
            String message = "";
            if (response.code() != 200) {
                if (response.code() == 500) {
                    message = "请求错误";
                } else if (response.code() == 511 || response.code() == 510) {
                    message = "token失效";
                    SPreference.quitLogin(Appli.getContext());
                    RxBus.get().post(RE_LOGIN_OBSERVABLE, response.code());
                }
                httpCodeInterceptor(responseBody, UTF8, response, message);
            }
            return response;
        };

        //应用拦截器：设置缓存策略
        Interceptor cacheInterceptor = chain -> {
            Request request = chain.request();

            NetUtils.NetState state = NetUtils.getNetState();
            //无网的时候强制使用缓存
            if (state == NetUtils.NetState.NET_NO) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }
            Response response = chain.proceed(request);

            //有网的时候读接口上的@Headers里的配置
            if (state != NetUtils.NetState.NET_NO) {
                String cacheControl = request.cacheControl().toString();
                return response.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .build();
            } else {
                int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
                return response.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
        };

        //身份验证拦截器 如果得到401 Not Authorized未授权的错误
        Authenticator authenticator = (route, response) -> {
            if (!response.isSuccessful())
                throw new ApiException(String.valueOf(response.code()), "请求错误");
            Utils.log(TAG, response.toString());
            return null;
        };

        OkHttpClient client = new OkHttpClient();
        mClient = client.newBuilder().readTimeout(HTTP_CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
                .connectTimeout(HTTP_CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(HTTP_CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(logInterceptor)//设置应用拦截器，主要用于设置公共参数，头信息，日志拦截等
                .addInterceptor(codeInterceptor)
                .addNetworkInterceptor(headInterceptor)//设置网络拦截器，主要用于重试或重写
                .authenticator(authenticator).build();

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

    private void httpCodeInterceptor(ResponseBody responseBody, Charset UTF8, Response response, String msg) throws IOException {
        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE); // Buffer the entire body.
        Buffer buffer = source.buffer();

        Charset charset = UTF8;
        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
            try {
                charset = contentType.charset(UTF8);
            } catch (UnsupportedCharsetException e) {
                throw new ApiException(String.valueOf(response.code()), response.message());
            }

            if (responseBody.contentLength() != 0) {
                BaseResult result = new Gson().fromJson(buffer.clone().readString(charset), BaseResult.class);
                if (result != null && !TextUtils.isEmpty(result.message)) {
                    msg = result.message;
                }
                throw new ApiException(String.valueOf(response.code()), msg);
            }
        }
        throw new ApiException(String.valueOf(response.code()), response.message());
    }

}
