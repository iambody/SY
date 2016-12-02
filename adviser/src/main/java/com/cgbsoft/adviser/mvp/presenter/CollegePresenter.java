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

import java.util.ArrayList;
import java.util.List;

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
        }
        addSubscription(ApiClient.getCollegeHeadList().flatMap(base -> {
            int dataSize = base.rows.size();
            //第一个
            if (dataSize > 0) {
                CollegeModel model = new CollegeModel();
                model.type = CollegeModel.HEAD;
                model.headBgUrl = base.rows.get(0).coverImageUrl;
                model.headBgContent = base.rows.get(0).shortName;
                adapter.refOne(model, 0);
            }
            //推荐的title及4个元素
            List<CollegeModel> list = new ArrayList<>();
            list.add(new CollegeModel(CollegeModel.COMM_HEAD));

            if (dataSize == 5) {
                for (int i = 1; i < 5; i++) {
                    CollegeModel model = new CollegeModel();
                    model.type = CollegeModel.COMM;
                    model.headBgUrl = base.rows.get(i).coverImageUrl;
                    model.headBgContent = base.rows.get(i).shortName;
                    list.add(model);
                }
            } else if (dataSize > 1 && dataSize < 5) {
                for (int i = 1; i < 5; i++) {
                    CollegeModel model = new CollegeModel();
                    model.type = CollegeModel.COMM;
                    if (i < dataSize) {
                        model.headBgUrl = base.rows.get(i).coverImageUrl;
                        model.headBgContent = base.rows.get(i).shortName;
                    } else {
                        model.isVisable = false;
                    }
                    list.add(model);
                }
            }
            adapter.appendToList(list);

            return ApiClient.getCollegeOtherList();
        }).subscribe(new RxSubscriber<CollegeVideoEntity.Result>() {
            @Override
            protected void onEvent(CollegeVideoEntity.Result result) {
                adapter.addOne(new CollegeModel(CollegeModel.OHTER_HEAD));
                List<CollegeModel> list = new ArrayList<>();
                int dataSize = result.rows.size();
                for (int i = 1; i < dataSize; i++) {
                    CollegeModel model = new CollegeModel();
                    model.type = CollegeModel.COMM;
                    model.headBgUrl = result.rows.get(i).coverImageUrl;
                    model.headBgContent = result.rows.get(i).shortName;
                    list.add(model);
                }
                adapter.appendToList(list);
                getView().getCollegeDataSucc(isRef);
            }

            @Override
            protected void onRxError(Throwable error) {
                getView().getCollegeDataFail(isRef);
            }
        }));
    }
}
