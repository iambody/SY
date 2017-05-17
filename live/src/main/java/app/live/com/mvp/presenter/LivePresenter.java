package app.live.com.mvp.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;

import app.live.com.mvp.contract.LiveContract;

/**
 * desc
 * Created by yangzonghui on 2017/5/16 15:52
 * Email:yangzonghui@simuyun.com
 *  
 */
public class LivePresenter extends BasePresenterImpl<LiveContract.view> implements LiveContract.presenter{

    public LivePresenter(@NonNull Context context, @NonNull LiveContract.view view) {
        super(context, view);
    }
}
