package com.cgbsoft.adviser.mvp.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cgbsoft.adviser.mvp.contract.CollegeContract;
import com.cgbsoft.adviser.mvp.ui.college.adapter.CollegeAdapter;
import com.cgbsoft.adviser.mvp.ui.college.model.CollegeModel;
import com.cgbsoft.lib.base.model.CollegeVideoEntity;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;

/**
 * Created by xiaoyu.zhang on 2016/12/1 13:43
 * Email:zhangxyfs@126.com
 *  
 */
public class CollegePresenter extends BasePresenterImpl<CollegeContract.View> implements CollegeContract.Presenter {

    public CollegePresenter(@NonNull Context context, @NonNull CollegeContract.View view) {
        super(context, view);
    }


    @Override
    public void getCollegeData(CollegeAdapter adapter, boolean isRef) {
        if (isRef) {
            adapter.clear();
            addSubscription(ApiClient.getCollegeHeadList().flatMap(base -> {
                if (base.rows.size() > 0) {
                    CollegeModel model = new CollegeModel();
                    model.type = CollegeModel.HEAD;
                    model.headBgUrl = base.rows.get(0).coverImageUrl;
                    model.headBgContent = base.rows.get(0).shortName;

                    adapter.refOne(model, 0);
                }

                return ApiClient.getCollegeOtherList();
            }).subscribe(new RxSubscriber<CollegeVideoEntity.Result>() {
                @Override
                protected void onEvent(CollegeVideoEntity.Result result) {

                }

                @Override
                protected void onRxError(Throwable error) {
                    getView().getCollegeHeadDataFail(isRef);
                }
            }));
        }
    }
}
