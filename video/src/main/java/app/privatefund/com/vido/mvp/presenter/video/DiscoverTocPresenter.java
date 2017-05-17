package app.privatefund.com.vido.mvp.presenter.video;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;

import app.privatefund.com.vido.mvp.contract.video.DiscoverTocContract;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/15-14:42
 */
public class DiscoverTocPresenter extends BasePresenterImpl<DiscoverTocContract.view> implements  DiscoverTocContract.Presenter {


    public DiscoverTocPresenter(@NonNull Context context, @NonNull DiscoverTocContract.view view) {
        super(context, view);
    }

    @Override
    public void test() {

    }
}
