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
import com.cgbsoft.privatefund.R;

import java.lang.reflect.Field;
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

    private View showView;

//    private BadgeView badgeView;

    private boolean fromBackPoint;

    public static boolean hasUnreadNumber;

    private static Observable<Integer> unReadNumberObservable;

    public UnreadInfoNumber(Activity activity, View showView, boolean fromBackPoint) {
        this.activity = activity;
        this.showView = showView;
        this.fromBackPoint = fromBackPoint;
        initRegeist();
        initUnreadInfoAndPosition();
    }

//    /**
//     * 初始化未读消息
//     */
//    public void initUnreadInfo() {
//        if (AppManager.isVisitor(activity)) {
//            return;
//        }
//        initUnreadNumber();
////        int numberNum = AppManager.getUnreadInfoNumber(activity);
//        if (badgeView == null) {
//            if (hasUnreadNumber) {
//                badgeView = ViewUtils.createLeftTopRedStringPoint(activity, showView, "");
//            }
//        } else {
//            if (hasUnreadNumber) {
////                badgeView.setText(String.valueOf(numberNum > 99 ? 99 : numberNum));
//                badgeView.setText("");
//                badgeView.invalidate();
//            } else {
//                badgeView.hide();
//            }
//        }
//    }

    public void initUnreadInfoAndPosition() {
        if (showView instanceof ImageView) {
            initUnreadNumber();
            ImageView imageView = (ImageView) showView;
            imageView.setImageResource(hasUnreadNumber ? (fromBackPoint ? R.drawable.select_news_new_black_red_point : R.drawable.select_news_new_white_red_point) : (fromBackPoint ? R.drawable.select_happy_life_toolbar_right : R.drawable.main_home_new_iv));
        }
    }

    public boolean hasShowRedPoint() {
        ImageView resourceId = (ImageView)showView;
        Class imageClass = resourceId.getClass();
        try {
            Field field = imageClass.getDeclaredField("mResource");
            int resoutId = (int)field.get(resourceId);
            return resoutId == R.drawable.select_news_new_black_red_point || resoutId == R.drawable.select_news_new_white_red_point;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
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

    private void initRegeist() {
        if (unReadNumberObservable == null) {
            unReadNumberObservable = RxBus.get().register(RxConstant.REFRUSH_UNREADER_INFO_NUMBER_OBSERVABLE, Integer.class);
            unReadNumberObservable.subscribe(new RxSubscriber<Integer>() {
                @Override
                protected void onEvent(Integer integer) {
                    hasUnreadNumber = integer != 0;
                    initUnreadInfoAndPosition();
                }

                @Override
                protected void onRxError(Throwable error) {

                }
            });
        }
    }
}
