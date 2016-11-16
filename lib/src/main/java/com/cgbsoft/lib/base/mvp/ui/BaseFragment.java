package com.cgbsoft.lib.base.mvp.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cgbsoft.lib.Appli;
import com.cgbsoft.lib.base.model.bean.DaoSession;
import com.cgbsoft.lib.utils.constant.Constant;
import com.trello.rxlifecycle.components.support.RxFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 基本Fragment
 * Created by user on 2016/11/4.
 */

public abstract class BaseFragment<DATA> extends RxFragment implements Constant {
    private Appli mAppli;
    protected List<DATA> mDataList;
    protected View fragmentView;
    private DaoSession daoSession;
    private Unbinder unbinder;

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
        if (fragmentView == null && layoutID() > 0) {
            fragmentView = inflater.inflate(layoutID(), container, false);
        }
        after(fragmentView);
        init(fragmentView);
        data();
        return fragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    protected void before() {
        mDataList = new ArrayList<>();
        mAppli = (Appli) getActivity().getApplication();
    }

    protected void after(View view) {
        unbinder = ButterKnife.bind(this, view);
    }

    protected abstract int layoutID();

    protected abstract void init(View view);

    protected void data() {

    }

    protected Appli getAppli() {
        return mAppli;
    }

    protected DaoSession getDaoSession() {
        if (daoSession == null) {
            daoSession = getAppli().getDaoSession();
        }
        return daoSession;
    }
}
