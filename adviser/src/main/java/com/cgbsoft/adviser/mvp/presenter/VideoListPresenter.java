package com.cgbsoft.adviser.mvp.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.cgbsoft.adviser.mvp.contract.VideoListContract;
import com.cgbsoft.adviser.mvp.ui.college.adapter.VideoListAdapter;
import com.cgbsoft.adviser.mvp.ui.college.model.VideoListModel;
import com.cgbsoft.lib.base.model.CollegeVideoEntity;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaoyu.zhang on 2016/12/7 15:02
 * Email:zhangxyfs@126.com
 *  
 */
public class VideoListPresenter extends BasePresenterImpl<VideoListContract.View> implements VideoListContract.Presenter {
    private int index = 0;

    public VideoListPresenter(@NonNull Context context, @NonNull VideoListContract.View view) {
        super(context, view);
    }

    @Override
    public void getVideoListData(VideoListAdapter adapter, String title, boolean isRef) {
        String type = title;
        if (isRef) {
            index = 0;
        } else {
            index++;
        }
        if (TextUtils.equals(type, "视频列表")) {
            type = "";
        }

        addSubscription(ApiClient.getTestVideoOtherList(index, type).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                List<CollegeVideoEntity.Row> rows = new Gson().fromJson(s, new TypeToken<List<CollegeVideoEntity.Row>>() {
                }.getType());
                List<VideoListModel> list = new ArrayList<>();
                for (int i = 0; i < rows.size(); i++) {
                    VideoListModel model = new VideoListModel();
                    model.type = VideoListModel.BOTTOM;
                    model.leftImgUrl = rows.get(i).coverImageUrl;
                    model.timeStr = rows.get(i).createDate;
                    model.content = rows.get(i).shortName;
                    model.title = rows.get(i).videoName;
                    model.heartNum = rows.get(i).likes;
                    model.videoId = rows.get(i).videoId;
                    list.add(model);
                }
                if (isRef) {
                    adapter.deleteAllData();
                    adapter.refAllData(list);
                } else {
                    adapter.appendToList(list);
                }
                getView().getVideoListDataSucc(isRef);
            }

            @Override
            protected void onRxError(Throwable error) {
                getView().getVideoListDataFail(isRef);
            }
        }));
    }
}
