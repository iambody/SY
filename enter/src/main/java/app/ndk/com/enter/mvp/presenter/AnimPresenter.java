package app.ndk.com.enter.mvp.presenter;

import android.content.Context;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;

import app.ndk.com.enter.mvp.contract.AnimContract;

/**
 * Created by xiaoyu.zhang on 2016/11/17 14:58
 * Email:zhangxyfs@126.com
 *  
 */
public class AnimPresenter extends BasePresenterImpl<AnimContract.View> implements AnimContract.Presenter {
    public AnimPresenter(Context context, AnimContract.View view) {
        super(context, view);
    }
}
