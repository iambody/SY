package com.cgbsoft.lib.mvp.presenter.video;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cgbsoft.lib.R;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.mvp.contract.video.VideoHistoryListContract;
import com.cgbsoft.lib.mvp.model.video.VideoInfoModel;
import com.cgbsoft.lib.mvp.ui.video.model.VideoHistoryModel;
import com.cgbsoft.lib.utils.db.DaoUtils;
import com.cgbsoft.lib.utils.tools.DataUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaoyu.zhang on 2016/12/12 17:41
 * Email:zhangxyfs@126.com
 *  
 */
public class VideoHistoryListPresenter extends BasePresenterImpl<VideoHistoryListContract.View> implements VideoHistoryListContract.Presenter {
    private DaoUtils daoUtils;

    public VideoHistoryListPresenter(@NonNull Context context, @NonNull VideoHistoryListContract.View view) {
        super(context, view);
        daoUtils = new DaoUtils(context, DaoUtils.W_VIDEO);
    }

    @Override
    public void getLocalVideoInfoList(boolean isRef) {
        List<VideoInfoModel> list = daoUtils.getAllVideoInfoHistory();
        List<VideoHistoryModel> dataList = new ArrayList<>();
        if (list != null && list.size() >= 0) {
            for (int i = 0; i < list.size(); i++) {
                VideoHistoryModel model = new VideoHistoryModel();
                model.type = VideoHistoryModel.LIST;
                model.videoCoverUrl = list.get(i).videoCoverUrl;
                model.videoId = list.get(i).videoId;
                model.videoTitle = list.get(i).videoName;
                model.time = String.format(getContext().getString(R.string.played_str), DataUtils.long2Data(list.get(i).currentTime * 1000));
                dataList.add(model);
            }
            getView().getLocalListSucc(dataList, isRef);
        } else
            getView().getLocalListFail(isRef);
    }

    @Override
    public void delete(String videoId){
        daoUtils.deleteVideoInfoHistory(videoId);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (daoUtils != null) {
            daoUtils.destory();
            daoUtils = null;
        }
    }
}
