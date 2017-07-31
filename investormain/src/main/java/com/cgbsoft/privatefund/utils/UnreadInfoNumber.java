package com.cgbsoft.privatefund.utils;

import android.app.Activity;
import android.view.View;

import com.cgbsoft.lib.AppInfStore;
import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.utils.constant.RxConstant;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.ViewUtils;
import com.readystatesoftware.viewbadger.BadgeView;

import rx.Observable;

/**
 * @author chenlong
 */
public class UnreadInfoNumber {

    private Activity activity;

    private BadgeView badgeView;

    private View showView;

    private static Observable<Integer> unReadNumberObservable;

    public UnreadInfoNumber(Activity activity, View showView) {
        this.activity = activity;
        this.showView = showView;
        initUnreadInfo(0);
        initRegeist();
    }

    /**
     * 初始化未读消息
     */
    private void initUnreadInfo(int numberNum) {
        if (AppManager.isVisitor(activity)) {
            return;
        }
//        int numberNum = AppManager.getUnreadInfoNumber(activity);
        if (badgeView == null) {
            if (numberNum > 0) {
                badgeView = ViewUtils.createLeftTopRedStringPoint(activity, showView, "");
            }
        } else {
            if (numberNum > 0) {
//                badgeView.setText(String.valueOf(numberNum > 99 ? 99 : numberNum));
                badgeView.setText("");
                badgeView.invalidate();
            } else {
                badgeView.hide();
            }
        }
    }

    /**
     * 注销注册事件
     */
    public void onDestroy() {
        if (null != unReadNumberObservable) {
            RxBus.get().unregister(RxConstant.REFRUSH_UNREADER_INFO_NUMBER_OBSERVABLE, unReadNumberObservable);
            unReadNumberObservable = null;
        }
    }

    private void initRegeist() {
        if (unReadNumberObservable == null) {
            unReadNumberObservable = RxBus.get().register(RxConstant.REFRUSH_UNREADER_INFO_NUMBER_OBSERVABLE, Integer.class);
            unReadNumberObservable.subscribe(new RxSubscriber<Integer>() {
                @Override
                protected void onEvent(Integer integer) {
                    initUnreadInfo(integer);
                }

                @Override
                protected void onRxError(Throwable error) {

                }
            });
        }
    }

}
