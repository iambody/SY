package app.privatefund.com.vido.mvp.presenter.video;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.BStrUtils;

import app.privatefund.com.vido.mvp.contract.video.VideoListContract;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/6/23-14:47
 */
public class VideoListPresenter extends BasePresenterImpl<VideoListContract.View> implements VideoListContract.Presenter {
    public VideoListPresenter(@NonNull Context context, @NonNull VideoListContract.View view) {
        super(context, view);
    }

    @Override
    public void getVideoList(String CatoryValue, int CurrentPostion) {
        addSubscription(ApiClient.videoSchoolLs(CatoryValue, CurrentPostion, AppManager.getCustomRole(getContext()),AppManager.getCustomType(getContext())).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                if (!BStrUtils.isEmpty(s)) {
                    getView().getVideoDataSucc(getV2String(s));
                } else {
                    getView().getVideoDataError("失败");
                }
            }

            @Override
            protected void onRxError(Throwable error) {
                getView().getVideoDataError(error.getMessage());

            }
        }));

    }
}
