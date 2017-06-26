package com.cgbsoft.lib.base.mvp.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.base.mvp.view.BaseView;

/**
 * desc  翻页presenter
 * Created by yangzonghui on 2017/6/22 15:20
 * Email:yangzonghui@simuyun.com
 *  
 */
public class BasePagePresenter extends BasePresenterImpl {

    public BasePagePresenter(@NonNull Context context, @NonNull BaseView view) {
        super(context, view);
    }

}
