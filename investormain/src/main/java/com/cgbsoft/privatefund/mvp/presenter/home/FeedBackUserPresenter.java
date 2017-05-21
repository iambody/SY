package com.cgbsoft.privatefund.mvp.presenter.home;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.privatefund.mvp.contract.home.FeedBackUserContract;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * @author chenlong
 *
 */
public class FeedBackUserPresenter extends BasePresenterImpl<FeedBackUserContract.View> implements FeedBackUserContract.Presenter {

    public FeedBackUserPresenter(@NonNull Context context, @NonNull FeedBackUserContract.View view) {
        super(context, view);
    }

    @Override
    public void feedBack(String content, JSONArray imageUrl) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("content", content);
        hashMap.put("imageUrl", imageUrl);
//        ApiClient.feedBackUser(hashMap).subscribe(new RxSubscriber<CommonEntity.Result>() {
//            @Override
//            protected void onEvent(CommonEntity.Result result) {
//                if ("suc".equals(result.results)) {
//                    getView().requestSuccess();
//                } else {
//                    getView().requestFailure("提交意见失败");
//                }
//            }
//
//            @Override
//            protected void onRxError(Throwable error) {
//                getView().requestFailure(error.getMessage());
//            }
//        });
        ApiClient.toTestFeedBackUser(hashMap).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String result) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    String results = jsonObject.get("result").toString();
                    if ("suc".equals(results)) {
                        getView().requestSuccess();
                    } else {
                        getView().requestFailure("提交意见失败");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void onRxError(Throwable error) {
                getView().requestFailure(error.getMessage());
            }
        });
    }
}
