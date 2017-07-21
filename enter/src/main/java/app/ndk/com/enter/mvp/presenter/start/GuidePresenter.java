package app.ndk.com.enter.mvp.presenter.start;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;

import app.ndk.com.enter.mvp.contract.start.GuideContract;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/7/21-09:59
 */
public class GuidePresenter extends BasePresenterImpl<GuideContract.view> implements GuideContract.Presenter {
    public GuidePresenter(@NonNull Context context, @NonNull GuideContract.view view) {
        super(context, view);
    }
}
