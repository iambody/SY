package com.cgbsoft.lib.base.mvp.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cgbsoft.lib.Appli;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.utils.cache.OtherDataProvider;
import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.utils.db.dao.DaoSession;
import com.cgbsoft.lib.utils.tools.DataStatisticsUtils;
import com.cgbsoft.lib.widget.WeakHandler;
import com.trello.rxlifecycle.components.support.RxFragment;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 基本Fragment
 * Created by user on 2016/11/4.
 */

public abstract class BaseFragment<P extends BasePresenterImpl> extends RxFragment implements Constant {
    private Appli mAppli;
    private WeakHandler mBaseHandler;//handler
    private View mFragmentView;
    private DaoSession mDaoSession;//数据库
    private Unbinder mUnbinder;//用于butterKnife解绑
    private P mPresenter;//功能调用

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        before();
        if (mFragmentView == null && layoutID() > 0) {
            mFragmentView = inflater.inflate(layoutID(), container, false);
        }
        after(mFragmentView);
        init(mFragmentView, savedInstanceState);
        data();
        return mFragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        OtherDataProvider.addTopActivity(getContext().getApplicationContext(), getClass().getName());
    }

    protected void before() {
        mAppli = (Appli) getActivity().getApplication();
    }

    protected void after(View view) {
        mUnbinder = ButterKnife.bind(this, view);
        mBaseHandler = new WeakHandler();
        mPresenter = createPresenter();
    }

    protected abstract int layoutID();

    protected abstract void init(View view, Bundle savedInstanceState);

    protected abstract P createPresenter();

    protected void data() {

    }

    /**
     * 获取application
     *
     * @return
     */
    protected Appli getAppli() {
        return mAppli;
    }

    /**
     * 获取数据库
     *
     * @return
     */
    protected DaoSession getmDaoSession() {
        if (mDaoSession == null) {
            mDaoSession = getAppli().getDaoSession();
        }
        return mDaoSession;
    }

    /**
     * 获取presenter
     *
     * @return
     */
    protected P getPresenter() {
        return mPresenter;
    }

    /**
     * 获取handler
     *
     * @return
     */
    protected WeakHandler getHandler() {
        return mBaseHandler;
    }


    /**
     * 统计
     *
     * @param grp
     * @param act
     * @param arg1
     */
    protected void toDataStatistics(int grp, int act, String arg1) {
        HashMap<String, String> data = new HashMap<>();
        data.put("grp", String.valueOf(grp));
        data.put("act", String.valueOf(act));
        data.put("arg1", arg1);
        DataStatisticsUtils.push(getContext().getApplicationContext(), data);
    }

    protected void toDataStatistics(int grp, int act, String[] args){
        HashMap<String, String> data = new HashMap<>();
        data.put("grp", String.valueOf(grp));
        data.put("act", String.valueOf(act));
        for (int i = 1; i <= args.length; i++) {
            data.put("arg" + i, args[i - 1]);
        }
        DataStatisticsUtils.push(getContext().getApplicationContext(), data);
    }


    /**
     * 打开activity
     *
     * @param pClass
     */
    protected void openActivity(Class<?> pClass) {
        openActivity(pClass, null);
    }

    protected void openActivity(Class<?> pClass, Bundle pBundle) {
        Intent intent = new Intent(getContext(), pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivity(intent);
    }
}
