package com.cgbsoft.privatefund.mvp.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.cgbsoft.lib.base.model.bean.UnReadCMSG;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.base.mvp.ui.BaseFragment;
import com.cgbsoft.lib.base.webview.BaseWebview;
import com.cgbsoft.lib.base.webview.CWebClient;
import com.cgbsoft.lib.base.webview.CwebNetConfig;
import com.cgbsoft.lib.base.webview.WebViewConstant;
import com.cgbsoft.lib.utils.constant.RxConstant;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.privatefund.R;

import app.privatefund.com.im.bean.RCConnect;
import butterknife.BindView;
import io.rong.eventbus.EventBus;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import rx.Observable;

/**
 *  个人页
 *  Created by xiaoyu.zhang on 2016/11/15 14:08
 *  Email:zhangxyfs@126.com
 *  
 */
public class MineFragment extends BaseFragment {

    private int cUnread;
    @BindView(R.id.webView)
    BaseWebview baseWebview;
    private Observable unreadInfomation;

    @Override
    protected int layoutID() {
        return R.layout.fragment_webview_common;
    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {
        EventBus.getDefault().register(this);

        unreadInfomation = RxBus.get().register(RxConstant.REFRUSH_UNREAD_INFOMATION, Boolean.class);
        unreadInfomation.subscribe(new RxSubscriber<Boolean>() {
            @Override
            protected void onEvent(Boolean aBoolean) {
                if (cUnread != 0) {
                    baseWebview.loadUrl("javascript:message('" + cUnread + "')");
                }
            }

            @Override
            protected void onRxError(Throwable error) {
            }
        });
        baseWebview.loadUrls(CwebNetConfig.minePgge);
        baseWebview.setClick(new CWebClient.WebviewOnClick() {
            @Override
            public void onClick(String result) {
                if(WebViewConstant.AppCallBack.TOC_GO_PRODUCTLS.equals(result)){
                    RxBus.get().post(RxConstant.INVERSTOR_MAIN_PAGE, 1);
                }else if (result.contains(WebViewConstant.AppCallBack.TOC_MALL_STATE)){
                    RxBus.get().post(RxConstant.INVERSTOR_MAIN_PAGE,1);
                }
            }
        });
    }

    @Override
    protected BasePresenterImpl createPresenter() {
        return null;
    }

    @Override
    public void onResume() {
        super.onResume();
        baseWebview.loadUrl("javascript:refresh()");
        baseWebview.loadUrl("javascript:message('" + cUnread + "')");
    }

    public void onEventMainThread(RCConnect rcConnect) {
        refrushUnreadMessage();
        if (cUnread != 0) {
            baseWebview.loadUrl("javascript:message('" + cUnread + "')");
        }
        if (rcConnect.getConnectStatus().equals("CONNECTED")) {
//            checkMessage();
        }
    }

//    public void onEventMainThread(CheckManager rcConnect) {
//        refrushUnreadMessage();
//        mWebview.getWebView().loadUrl("javascript:message('" + cUnread + "')");
////        checkMessage();
//    }

    private void refrushUnreadMessage() {
        int totalUnreadCount = RongIMClient.getInstance().getTotalUnreadCount();
        int intime40003 = RongIMClient.getInstance().getUnreadCount(Conversation.ConversationType.PRIVATE, "INTIME40003");
        int intime40004 = RongIMClient.getInstance().getUnreadCount(Conversation.ConversationType.PRIVATE, "INTIME40004");
        int intime40006 = RongIMClient.getInstance().getUnreadCount(Conversation.ConversationType.PRIVATE, "INTIME40006");
        cUnread = totalUnreadCount - intime40003 - intime40004 - intime40006;
        Log.e("totalUnreadCount", totalUnreadCount + "--------cUnread=" + cUnread);
    }

    public void onEventMainThread(UnReadCMSG unReadMSG) {
        cUnread = unReadMSG.getUnreadCount();
        baseWebview.loadUrl("javascript:message('" + cUnread + "')");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unreadInfomation != null) {
            RxBus.get().unregister(RxConstant.REFRUSH_UNREAD_INFOMATION, unreadInfomation);
        }
    }
}
