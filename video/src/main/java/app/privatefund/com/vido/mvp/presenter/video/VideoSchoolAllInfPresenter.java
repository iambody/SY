package app.privatefund.com.vido.mvp.presenter.video;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.BStrUtils;

import app.privatefund.com.vido.mvp.contract.video.VideoSchoolAllInfContract;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/6/26-21:00
 */
public class VideoSchoolAllInfPresenter extends BasePresenterImpl<VideoSchoolAllInfContract.View> implements VideoSchoolAllInfContract.Presenter {

    public VideoSchoolAllInfPresenter(@NonNull Context context, @NonNull VideoSchoolAllInfContract.View view) {
        super(context, view);
    }

    @Override
    public void getVideoSchoolAllInf() {
        addSubscription(ApiClient.videoSchoolAllInf().subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                if (!BStrUtils.isEmpty(s)) {
                    getView().getSchoolAllDataSucc(getV2String(s));
                } else {
                    getView().getSchoolAllDataSucc(getContext().getResources().getString(com.cgbsoft.lib.R.string.netdata_empty));
                }
            }

            @Override
            protected void onRxError(Throwable error) {
                getView().getSchoolAllDataError(error.getMessage());
            }
        }));
    }


}
