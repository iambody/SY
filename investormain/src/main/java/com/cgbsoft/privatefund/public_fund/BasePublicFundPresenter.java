package com.cgbsoft.privatefund.public_fund;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.base.mvp.view.BaseView;
import com.cgbsoft.lib.utils.exception.ApiException;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.BStrUtils;
import com.cgbsoft.lib.widget.MToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import rx.Observable;

/**
 * Created by wangpeng on 18-1-31.
 */

public class BasePublicFundPresenter extends BasePresenterImpl {
    public static final String UNEXPECTED = "unexpected"; // 意料之外的情况

    public BasePublicFundPresenter(@NonNull Context context, @NonNull BaseView view) {
        super(context, view);
    }

    /**
     * 从金证获取公募基金的相关信息
     * @param parms 请求参数
     */
    public void getFundDataFormJZ(Map<String,Object> parms,PreSenterCallBack preSenterCallBack){
        ApiClient.getPublicFundFormProxy(parms).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                parseResultFormServer(s,preSenterCallBack);
            }

            @Override
            protected void onRxError(Throwable error) {
                if(error instanceof ApiException && preSenterCallBack!=null){
                    preSenterCallBack.field(((ApiException) error).getCode(),error.getMessage());
                }
                error.printStackTrace();
            }
        });


    }

    /**
     * 处理公募基金接口信息
     * @param observable
     */
    public void handlerPublicFundResult(Observable<String> observable,PreSenterCallBack preSenterCallBack){
        observable.subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                parseResultFormServer(s,preSenterCallBack);
            }

            @Override
            protected void onRxError(Throwable error) {
                error.printStackTrace();
                String errorCode = UNEXPECTED;
                if(error instanceof ApiException && "500".equals(((ApiException) error).getCode())){
                    MToast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    errorCode = ((ApiException) error).getCode();
                }
                if(preSenterCallBack!=null){
                    preSenterCallBack.field(errorCode,error.getMessage());
                }
            }
        });
    }


    public void parseResultFormServer(String s ,PreSenterCallBack preSenterCallBack){
        String message = "";
        String result = "";
        try {
            result =  new JSONObject(s).getString("result");
            message =  new JSONObject(s).getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(!BStrUtils.isEmpty(message)){
            if(preSenterCallBack!=null) preSenterCallBack.field(0+"",message);
        }else {
            if(preSenterCallBack!=null) preSenterCallBack.even(result);
        }
    }


    public interface PreSenterCallBack<T> {
        void even(T t);
        void field(String errorCode,String errorMsg);
    }
}
