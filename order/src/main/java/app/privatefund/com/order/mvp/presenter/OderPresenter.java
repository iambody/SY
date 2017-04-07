package app.privatefund.com.order.mvp.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;

import app.privatefund.com.order.mvp.contract.OderLsContract;

/**
 * desc  订单 p
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 17/4/7-11:33
 */
public class OderPresenter extends BasePresenterImpl<OderLsContract.View> implements  OderLsContract.Presenter {
    public OderPresenter(@NonNull Context context, @NonNull OderLsContract.View view) {
        super(context, view);
    }
}
