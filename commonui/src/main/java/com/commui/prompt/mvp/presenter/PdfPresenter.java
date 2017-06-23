package com.commui.prompt.mvp.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.commui.prompt.mvp.contract.PdfContract;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/6/21-17:35
 */
public class PdfPresenter extends BasePresenterImpl<PdfContract.View> implements PdfContract.Presenter{
    public PdfPresenter(@NonNull Context context, @NonNull PdfContract.View view) {
        super(context, view);
    }
}
