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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaoyu.zhang on 2016/12/1 13:43
 * Email:zhangxyfs@126.com
 */
public class CollegePresenter extends BasePresenterImpl<CollegeContract.View> implements CollegeContract.Presenter {
    private int index = 0;

    public CollegePresenter(@NonNull Context context, @NonNull CollegeContract.View view) {
        super(context, view);
    }


    @Override
    public void getCollegeData(CollegeAdapter adapter, boolean isRef) {
        if (isRef) {
            index = 0;
        } else {
            index++;
        }

        List<CollegeModel> dataList = new ArrayList<>();
        if (isRef) {
            //todo 测试代码，服务器改变接口后需要使用下面的代码
            addSubscription(ApiClient.getTestCollegeHeadList().flatMap(s -> {
                List<CollegeVideoEntity.Row> rows = new Gson().fromJson(s, new TypeToken<List<CollegeVideoEntity.Row>>() {
                }.getType());

                int dataSize = rows.size();
                //第一个
                if (dataSize > 0) {
                    CollegeModel model = new CollegeModel();
                    model.type = CollegeModel.HEAD;
                    model.headBgUrl = rows.get(0).coverImageUrl;
                    model.headBgContent = rows.get(0).shortName;
                    model.videoId = rows.get(0).videoId;
                    dataList.add(model);
                }
                //推荐title item
                dataList.add(new CollegeModel(CollegeModel.COMM_HEAD));
                if (dataSize >= 5) {
                    for (int i = 1; i < 5; i++) {
                        CollegeModel model = new CollegeModel();
                        model.type = CollegeModel.COMM;
                        model.bottomVideoImgUrl = rows.get(i).coverImageUrl;
                        model.bottomVideoTitle = rows.get(i).videoName;
                        model.bottomVideoContent = rows.get(i).shortName;
                        model.videoId = rows.get(i).videoId;
                        dataList.add(model);
                    }
                } else if (dataSize > 1 && dataSize < 5) {
                    for (int i = 1; i < 5; i++) {
                        CollegeModel model = new CollegeModel();
                        model.type = CollegeModel.COMM;
                        if (i < dataSize) {
                            model.bottomVideoImgUrl = rows.get(i).coverImageUrl;
                            model.bottomVideoTitle = rows.get(i).videoName;
                            model.bottomVideoContent = rows.get(i).shortName;
                            model.videoId = rows.get(i).videoId;
                        } else {
                            model.isVisable = false;
                        }
                        dataList.add(model);
                    }
                }
                return ApiClient.getTestVideoOtherList(0, "");
            }).subscribe(new RxSubscriber<String>() {
                @Override
                protected void onEvent(String s) {
                    List<CollegeVideoEntity.Row> rows = new Gson().fromJson(s, new TypeToken<List<CollegeVideoEntity.Row>>() {
                    }.getType());
                    dataList.add(new CollegeModel(CollegeModel.OHTER_HEAD));
                    int dataSize = rows.size();
                    for (int i = 1; i < dataSize; i++) {
                        CollegeModel model = new CollegeModel();
                        model.type = CollegeModel.OTHER;
                        model.bottomVideoImgUrl = rows.get(i).coverImageUrl;
                        model.bottomVideoTitle = rows.get(i).videoName;
                        model.bottomVideoContent = rows.get(i).shortName;
                        model.videoId = rows.get(i).videoId;
                        model.videoPlayUrl = rows.get(i).sdvideoUrl;
                        dataList.add(model);
                    }
                    adapter.deleteAllData();
                    adapter.refAllData(dataList);
                    getView().getCollegeDataSucc(isRef);
                }

                @Override
                protected void onRxError(Throwable error) {
                    getView().getCollegeDataFail(isRef);
                }
            }));
        } else {
            addSubscription(ApiClient.getTestVideoOtherList(index, "").subscribe(new RxSubscriber<String>() {
                @Override
                protected void onEvent(String s) {
                    List<CollegeVideoEntity.Row> rows = new Gson().fromJson(s, new TypeToken<List<CollegeVideoEntity.Row>>() {
                    }.getType());
                    List<CollegeModel> list = new ArrayList<>();
                    int dataSize = rows.size();
                    for (int i = 1; i < dataSize; i++) {
                        CollegeModel model = new CollegeModel();
                        model.type = CollegeModel.OTHER;
                        model.bottomVideoImgUrl = rows.get(i).coverImageUrl;
                        model.bottomVideoTitle = rows.get(i).videoName;
                        model.bottomVideoContent = rows.get(i).shortName;
                        model.videoId = rows.get(i).videoId;
                        model.videoPlayUrl = rows.get(i).sdvideoUrl;
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

        /*addSubscription(ApiClient.getCollegeHeadList().flatMap(base -> {
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

            return ApiClient.getVideoOtherList();
        }).subscribe(new RxSubscriber<CollegeVideoEntity.Result>() {
            @Override
            protected void onEvent(CollegeVideoEntity.Result result) {
                adapter.addOne(new CollegeModel(CollegeModel.OHTER_HEAD));
                List<CollegeModel> list = new ArrayList<>();
                int dataSize = result.rows.size();
                for (int i = 1; i < dataSize; i++) {
                    CollegeModel model = new CollegeModel();
                    model.type = CollegeModel.OTHER;
                    model.bottomVideoImgUrl = result.rows.get(i).coverImageUrl;
                    model.bottomVideoTitle = result.rows.get(i).videoName;
                    model.bottomVideoContent = result.rows.get(i).shortName;
                    model.videoId = result.rows.get(i).videoId;
                    model.videoPlayUrl = result.rows.get(i).sdvideoUrl;
                    list.add(model);
                }
                adapter.appendToList(list);
                getView().getCollegeDataSucc(isRef);
            }

            @Override
            protected void onRxError(Throwable error) {
                getView().getCollegeDataFail(isRef);
            }
        }));*/
    }
}
