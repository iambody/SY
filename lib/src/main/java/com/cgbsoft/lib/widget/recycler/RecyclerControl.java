package com.cgbsoft.lib.widget.recycler;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


import com.cgbsoft.lib.utils.tools.Utils;
import com.cgbsoft.privatefund.bean.video.VideoHistoryModel;
import com.dinuscxj.refresh.RecyclerRefreshLayout;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by xiaoyu.zhang on 2016/6/30.
 */

public class RecyclerControl {
    private boolean isRefComplete = true, isLoadMoreComplete = true;
    private RecyclerRefreshLayout recyclerRefreshLayout;
    private LinearLayoutManager linearLayoutManager;
    private GridLayoutManager gridLayoutManager;
    private OnControlGetDataListListener onControlGetDataListListener;
    private OnScrollListener onScrollListener;
    private Subscription delay1Subscribe, delay2Subscribe, delay3Subscribe;

    /**
     * @param recyclerRefreshLayout
     * @param linearLayoutManager
     * @param onControlGetDataListListener
     */
    public RecyclerControl(RecyclerRefreshLayout recyclerRefreshLayout,
                           LinearLayoutManager linearLayoutManager, OnControlGetDataListListener onControlGetDataListListener) {
        this.recyclerRefreshLayout = recyclerRefreshLayout;
        this.linearLayoutManager = linearLayoutManager;
        this.onControlGetDataListListener = onControlGetDataListListener;

        onScrollListener = new OnScrollListener();
    }

    public RecyclerControl(RecyclerRefreshLayout recyclerRefreshLayout,
                           GridLayoutManager gridLayoutManager, OnControlGetDataListListener onControlGetDataListListener) {
        this.recyclerRefreshLayout = recyclerRefreshLayout;
        this.gridLayoutManager = gridLayoutManager;
        this.onControlGetDataListListener = onControlGetDataListListener;

        onScrollListener = new OnScrollListener();
    }

    public void onRefresh() {
        if (isRefComplete) {
            isRefComplete = false;
            delayGetData(true);
        }
    }

    public void getDataComplete(boolean isRef) {
        if (isRef) {
            isRefComplete = true;
        } else {
            isLoadMoreComplete = true;
        }
        recyclerRefreshLayout.setEnabled(true);
        if (isRef)
            recyclerRefreshLayout.setRefreshing(false);
    }

    private void delayComplete(boolean isRef) {
        delay3Subscribe = Observable.just(1).delay(500, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    recyclerRefreshLayout.setEnabled(true);
                    if (isRef)
                        recyclerRefreshLayout.setRefreshing(false);
                }, error -> {
                });
    }

    private boolean isAnimStart() {
        try {
            Field filed = recyclerRefreshLayout.getClass().getDeclaredField("mIsAnimatingToStart");
            filed.setAccessible(true);
            return filed.getBoolean(recyclerRefreshLayout);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    public OnScrollListener getOnScrollListener() {
        return onScrollListener;
    }


    public <A extends BaseAdapter, M extends BaseModel> void setError(Context context, boolean isError, A adpater, M model){
        setError(context, isError, adpater, model, null, 0);
    }

    //是无数据还是网络加载错误
    public  <A extends BaseAdapter, M extends BaseModel> void setError(Context context, boolean isError, A adapter,  M model, String noDataStr, int noDataResId) {
        int listSize = 0;

        if (adapter != null) {
            listSize = adapter.getList().size();
        }

        model.isError = isError;
        if (listSize == 0) {
            if (!isError) {
                model.noDataIvSize = Utils.convertDipOrPx(context, 100);
                model.noDataIvResId = noDataResId;
                model.noDataTvStr = noDataStr;
                model.noDataBtnWidth = 0;
                model.noDataBtnHeight = 0;
                model.noDataBtnStr = "";
                model.type = VideoHistoryModel.ERROR;
            } else {
                model.errorStatus = ErrorDataView.ERROR_NET;
            }
            adapter.appendError(model, 0);
        } else {
            adapter.removeError();
        }
    }

    public void destory() {
        if (delay1Subscribe != null && !delay1Subscribe.isUnsubscribed()) {
            delay1Subscribe.unsubscribe();
            delay1Subscribe = null;
        }
        if (delay2Subscribe != null && !delay2Subscribe.isUnsubscribed()) {
            delay2Subscribe.unsubscribe();
            delay2Subscribe = null;
        }
        if (delay3Subscribe != null && !delay3Subscribe.isUnsubscribed()) {
            delay3Subscribe.unsubscribe();
            delay3Subscribe = null;
        }

        recyclerRefreshLayout = null;
        linearLayoutManager = null;
        gridLayoutManager = null;
        onControlGetDataListListener = null;
        onScrollListener = null;
    }

    private void delayGetData(boolean isRef) {
        delay1Subscribe = Observable.just(1).delay(100, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    recyclerRefreshLayout.setEnabled(false);
                    if (isRef)
                        recyclerRefreshLayout.setRefreshing(true);
                    delay2Subscribe = Observable.just(1).delay(200, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())
                            .subscribe(aLong2 -> {
                                onControlGetDataListListener.onControlGetDataList(isRef);
                            }, error -> {
                            });
                }, error -> {
                });
    }

    private class OnScrollListener extends RecyclerView.OnScrollListener {
        int lastViewPos = -1;
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (isLoadMoreComplete) {
                if (linearLayoutManager != null)
                    lastViewPos = linearLayoutManager.findLastVisibleItemPosition();
                if (gridLayoutManager != null)
                    lastViewPos = gridLayoutManager.findLastVisibleItemPosition();

                if (newState == RecyclerView.SCROLL_STATE_IDLE&&lastViewPos >= (recyclerView.getAdapter().getItemCount() - 1)) {

                    if (isLoadMoreComplete) {
                        isLoadMoreComplete = false;
                        delayGetData(false);
                    }
                }
            }
            onControlGetDataListListener.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (isLoadMoreComplete) {
                if (linearLayoutManager != null)
                    lastViewPos = linearLayoutManager.findLastVisibleItemPosition();
                if (gridLayoutManager != null)
                    lastViewPos = gridLayoutManager.findLastVisibleItemPosition();
            }
            onControlGetDataListListener.onScrolled(recyclerView, dx, dy);
        }
    }

    public interface OnControlGetDataListListener {
        void onControlGetDataList(boolean isRef);

        void onScrollStateChanged(RecyclerView recyclerView, int newState);

        void onScrolled(RecyclerView recyclerView, int dx, int dy);
    }

}
