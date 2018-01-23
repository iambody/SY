package com.cgbsoft.lib.base.mvp.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.cgbsoft.lib.BaseApplication;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.utils.cache.OtherDataProvider;
import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.utils.db.dao.DaoSession;
import com.cgbsoft.lib.utils.tools.DataStatisticsUtils;
import com.cgbsoft.lib.utils.tools.Utils;
import com.cgbsoft.lib.widget.WeakHandler;
import com.trello.rxlifecycle.components.support.RxFragment;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 基本Fragment
 */

public abstract class BaseFragment<P extends BasePresenterImpl> extends RxFragment implements Constant {

    private BaseApplication mBaseApplication;
    private WeakHandler mBaseHandler;//handler
    protected View mFragmentView;
    private DaoSession mDaoSession;//数据库
    private Unbinder mUnbinder;//用于butterKnife解绑
    private P mPresenter;//功能调用
    protected Activity baseActivity;
    protected int screenWidth;
    //控件是否已经初始化
    protected boolean isCreateView = false;
    //是否已经加载过数据
    private boolean isLoadData = false;

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
        baseActivity = getActivity();
        before();
        if (mFragmentView == null && layoutID() > 0) {
            mFragmentView = inflater.inflate(layoutID(), container, false);
        }

        screenWidth = Utils.getScreenWidth(baseActivity);
        after(mFragmentView);
        init(mFragmentView, savedInstanceState);
        data();
        isCreateView = true;
        return mFragmentView;
    }

    /**
     * 此方法在控件初始化前调用，所以不能在此方法中直接操作控件会出现空指针
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && isCreateView) {
            viewBeShow();
            lazyLoad();
        } else {
            viewBeHide();
        }
    }

    protected void viewBeShow() {

    }

    protected void viewBeHide() {

    }

    private void lazyLoad() {
        //如果没有加载过就加载，否则就不再加载了
        if (!isLoadData) {
            loadData();
            isLoadData = true;
        }
    }

    protected void setIsLoad(boolean isLoad) {
        isLoadData = isLoad;
    }

    protected void loadData() {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //第一个fragment会调用
        if (getUserVisibleHint()) {
            lazyLoad();
        }
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
        mBaseApplication = (BaseApplication) getActivity().getApplication();
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
    protected BaseApplication getAppli() {
        return mBaseApplication;
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
        DataStatisticsUtils.push(getContext().getApplicationContext(), data, false);
    }

    protected void toDataStatistics(int grp, int act, String[] args) {
        HashMap<String, String> data = new HashMap<>();
        data.put("grp", String.valueOf(grp));
        data.put("act", String.valueOf(act));
        for (int i = 1; i <= args.length; i++) {
            data.put("arg" + i, args[i - 1]);
        }
        DataStatisticsUtils.push(getContext().getApplicationContext(), data, false);
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

    protected void clodLsAnim(SwipeToLoadLayout swipeToLoadLayout) {
        if (null == swipeToLoadLayout) return;
        if (swipeToLoadLayout.isLoadingMore()) swipeToLoadLayout.setLoadingMore(false);
        if (swipeToLoadLayout.isRefreshing()) swipeToLoadLayout.setRefreshing(false);
    }

    protected void showToast(int strId) {
        Toast.makeText(baseActivity.getApplicationContext(), getResources().getString(strId), Toast.LENGTH_SHORT).show();
    }

    // 判断权限集合
    protected boolean needPermissions(String... permissions) {
        //判断版本是否兼容
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false;
        }
        boolean isNeed;
        for (String permission : permissions) {
            isNeed = needsPermission(permission);
            if (isNeed) {
                return true;
            }
        }
        return false;
    }

    // 判断是否缺少权限
    protected boolean needsPermission(String permission) {
        return ContextCompat.checkSelfPermission(BaseApplication.getContext(), permission) != PackageManager.PERMISSION_GRANTED;
    }

    public boolean onBackPressed(Context context) {
        return false;
    }

    private Fragment hindFragment, targetFragment;

    private void recoverView(Bundle bundle) {
        if (null != bundle) {
            //内存重启时候调用
//            targetFragment=getFragmentManager().findFragmentById(targetFragment.getc)
        } else {
            //正常模式下
        }
    }
}
