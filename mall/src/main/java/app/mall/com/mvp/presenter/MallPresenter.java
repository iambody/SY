package app.mall.com.mvp.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.model.MallAddress;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.base.webview.BaseWebViewActivity;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.constant.RxConstant;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.mall.com.model.MallAddressBean;
import app.mall.com.mvp.contract.MallContract;
import rx.Observable;

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
        Map<String, String> params = new HashMap<>();
        params.put("user_id", AppManager.getUserId(getContext()));
        params.put("shopping_name", model.getShopping_name());
        params.put("id", model.getId());
        params.put("address", model.getAddress());
        params.put("phone", model.getPhone());
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
    public void addMallAddress(final MallAddressBean model) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", AppManager.getUserId(getContext()));
        params.put("shopping_name", model.getShopping_name());
        params.put("id", model.getId());
        params.put("address", model.getAddress());
        params.put("phone", model.getPhone());
        params.put("default_flag",model.getDefault_flag());
        addSubscription(ApiClient.addAddress(params).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                String id = "";
                try {
                    JSONObject js = new JSONObject(s);
                    id = js.getString("id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                model.setId(id);
                getView().addAddressSuc(model);
                RxBus.get().post(RxConstant.MALL_CHOICE_ADDRESS, new MallAddress(model.getPhone(),model.getId(),model.getShopping_name(),model.getAddress()));
            }

            @Override
            protected void onRxError(Throwable error) {

            }
        }));
    }

    @Override
    public void deleteMallAddress(final String addressId) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", AppManager.getUserId(getContext()));
        params.put("id", addressId);
        addSubscription(ApiClient.deleteMallAddress(params).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                getView().deleteSuc(addressId);
            }

            @Override
            protected void onRxError(Throwable error) {

            }
        }));
    }

    @Override
    public void getMallAddressList() {
        addSubscription(ApiClient.getMallAddress(AppManager.getUserId(getContext())).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                try {
                    JSONArray ja = new JSONArray(s);
                    List<MallAddressBean> rows = new Gson().fromJson(s, new TypeToken<List<MallAddressBean>>() {
                    }.getType());
                    getView().getMallAddressLitSuc((ArrayList<MallAddressBean>) rows);
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
    public void setDefaultAddress(final String id) {
        addSubscription(ApiClient.setDefauleMallAddress(AppManager.getUserId(getContext()), id).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                getView().setDefaultSuc(id);
            }

            @Override
            protected void onRxError(Throwable error) {
            }
        }));
    }
}
