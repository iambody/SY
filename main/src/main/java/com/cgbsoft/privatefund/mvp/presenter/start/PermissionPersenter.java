package com.cgbsoft.privatefund.mvp.presenter.start;

import android.content.Context;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.privatefund.mvp.contract.start.PermissionContract;

/**
 * 权限管理页面功能实现，数据调用
 * Created by xiaoyu.zhang on 2016/11/17 09:15
 * Email:zhangxyfs@126.com
 *  
 */
public class PermissionPersenter extends BasePresenterImpl<PermissionContract.View> implements PermissionContract.Persenter {

    public PermissionPersenter(Context context, PermissionContract.View view) {
        super(context, view);
    }
}
