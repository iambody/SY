package app.mall.com.mvp.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;

import java.util.HashMap;
import java.util.Map;

import app.mall.com.model.MallAddressBean;
import app.mall.com.mvp.contract.MallContract;

/**
 * desc  商城地址编辑
 * Created by yangzonghui on 2017/5/10 12:44
 * Email:yangzonghui@simuyun.com
 *  
 */
public class MallPresenter extends BasePresenterImpl<MallContract.View> implements MallContract.Presenter {

    public MallPresenter(@NonNull Context context, @NonNull MallContract.View view) {
        super(context, view);
    }

    @Override
    public void saveMallAddress(final MallAddressBean model) {
        Map<String,String> params = new HashMap<>();
        params.put("user_id", SPreference.getUserId(getContext()));
        params.put("shopping_name",model.getShopping_name());
        params.put("id",model.getId());
        params.put("address",model.getAddress());
        params.put("phone",model.getPhone());

        addSubscription(ApiClient.saveMallAddress(params).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                getView().saveAddressSucc(model);
            }

            @Override
            protected void onRxError(Throwable error) {
                getView().saveAddressErr(error.toString());
            }
        }));
    }

    @Override
    public void addMaddAddress(MallAddressBean model) {
        Map<String,String> params = new HashMap<>();
        params.put("user_id", SPreference.getUserId(getContext()));
        params.put("shopping_name",model.getShopping_name());
        params.put("id",model.getId());
        params.put("address",model.getAddress());
        params.put("phone",model.getPhone());
        addSubscription(ApiClient.addAddress(params).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {


            }

            @Override
            protected void onRxError(Throwable error) {

            }
        }));
    }

    @Override
    public void deleteMallAddress(String addressId) {
        Map<String,String> params = new HashMap<>();
        params.put("user_id", SPreference.getUserId(getContext()));
        params.put("id",addressId);
        addSubscription(ApiClient.deleteMallAddress(params).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {


            }

            @Override
            protected void onRxError(Throwable error) {

            }
        }));
    }

    @Override
    public void getMallAddressList() {
        addSubscription(ApiClient.getMallAddress(SPreference.getUserId(getContext())).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {


            }

            @Override
            protected void onRxError(Throwable error) {

            }
        }));
    }

    @Override
    public void setDefaultAddress(String id) {
        addSubscription(ApiClient.setDefauleMallAddress(SPreference.getUserId(getContext()),id).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {


            }

            @Override
            protected void onRxError(Throwable error) {

            }
        }));
    }
}
