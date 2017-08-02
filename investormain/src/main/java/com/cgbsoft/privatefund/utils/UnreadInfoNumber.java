package com.cgbsoft.privatefund.utils;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.utils.constant.RxConstant;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.CollectionUtils;
import com.cgbsoft.lib.utils.tools.ViewUtils;
import com.cgbsoft.privatefund.R;
import com.readystatesoftware.viewbadger.BadgeView;

import java.util.List;

import app.privatefund.com.im.utils.RongCouldUtil;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import rx.Observable;

/**
 * @author chenlong
 */
public class UnreadInfoNumber {

    private Activity activity;

    private BadgeView badgeView;

    private View showView;

    private boolean fromFirstPage;

    public static boolean hasUnreadNumber;

    private static Observable<Integer> unReadNumberObservable;

    public UnreadInfoNumber(Activity activity, View showView, boolean fromFirstPage) {
        this.activity = activity;
        this.showView = showView;
        this.fromFirstPage = fromFirstPage;
        initUnreadNumber();
        initRegeist();
        if (fromFirstPage) {
            initUnreadInfoAndPosition();
        } else {
            initUnreadInfo();
        }
    }

    private void initUnreadNumber() {
        List<Conversation> list = RongIM.getInstance().getConversationList();
        if (!CollectionUtils.isEmpty(list)) {
            for (Conversation conversation : list) {
                if (RongCouldUtil.getInstance().customConversation(conversation.getTargetId()) || Constant.msgCustomerService.equals(conversation.getSenderUserId()) ||
                        conversation.getTargetId().equals(AppManager.getUserInfo(activity).getToC().bandingAdviserId)) {
                    hasUnreadNumber = conversation.getUnreadMessageCount() > 0;
                    break;
                }
            }
        }
    }

    /**
     * 初始化未读消息
     */
    public void initUnreadInfo() {
        if (AppManager.isVisitor(activity)) {
            return;
        }
        initUnreadNumber();
//        int numberNum = AppManager.getUnreadInfoNumber(activity);
        if (badgeView == null) {
            if (hasUnreadNumber) {
                badgeView = ViewUtils.createLeftTopRedStringPoint(activity, showView, "");
            }
        } else {
            if (hasUnreadNumber) {
//                badgeView.setText(String.valueOf(numberNum > 99 ? 99 : numberNum));
                badgeView.setText("");
                badgeView.invalidate();
            } else {
                badgeView.hide();
            }
        }
    }

    public void initUnreadInfoAndPosition() {
        if (showView instanceof ImageView) {
            initUnreadNumber();
            ImageView imageView = (ImageView) showView;
            imageView.setImageResource(hasUnreadNumber ? R.drawable.select_news_new_red_point : R.drawable.main_home_new_iv);
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
                    hasUnreadNumber = integer != 0;
                    if (fromFirstPage) {
                        initUnreadInfoAndPosition();
                    } else {
                        initUnreadInfo();
                    }
                }

                @Override
                protected void onRxError(Throwable error) {

                }
            });
        }
    }

}
