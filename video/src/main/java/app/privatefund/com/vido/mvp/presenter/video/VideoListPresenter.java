package app.privatefund.com.vido.mvp.presenter.video;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;

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
    public void getVideoList() {

    }
}
