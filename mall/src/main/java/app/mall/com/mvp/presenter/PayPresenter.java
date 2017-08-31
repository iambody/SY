package app.mall.com.mvp.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import app.mall.com.mvp.contract.PayContract;

/**
 * desc
 * Created by yangzonghui on 2017/5/20 20:47
 * Email:yangzonghui@simuyun.com
 *  
 */
public class PayPresenter extends BasePresenterImpl<PayContract.View> implements PayContract.Presenter {


    public PayPresenter(@NonNull Context context, @NonNull PayContract.View view) {
        super(context, view);
    }


    public void getRechargeConfig(){
        addSubscription(ApiClient.getRechargeConfig().subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                try {
                    getView().getRechargeConfigSuc(new JSONObject(s).getJSONObject("result").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void onRxError(Throwable error) {
//                getView().getRechargeConfigSuc(error.toString());
            }
        }));
    }

    @Override
    public void checkRechargeResult(Map<String, Object> map) {
        addSubscription(ApiClient.checkRecharge(map).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                try {
                    getView().checkRecharge(new JSONObject(s).getJSONObject("result").toString());
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
    public void ydRecharge(Map<String, Object> map) {
        addSubscription(ApiClient.ydRecharge(map).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                try {
                    getView().rechargeResult(new JSONObject(s).getJSONObject("result").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void onRxError(Throwable error) {

            }
        }));
    }






}
