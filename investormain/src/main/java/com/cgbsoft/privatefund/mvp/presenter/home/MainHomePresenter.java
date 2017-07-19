package com.cgbsoft.privatefund.mvp.presenter.home;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.model.HomeEntity;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.utils.imgNetLoad.Imageload;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.LogUtils;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.lib.widget.dialog.DefaultDialog;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.mvp.contract.home.MainHomeContract;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;

import java.util.List;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/6/26-21:08
 */
public class MainHomePresenter extends BasePresenterImpl<MainHomeContract.View> implements MainHomeContract.Presenter {
    public MainHomePresenter(@NonNull Context context, @NonNull MainHomeContract.View view) {
        super(context, view);
    }

    @Override
    public void getHomeData() {
        addSubscription(ApiClient.getSxyHomeData().subscribe(new RxSubscriber<HomeEntity.Result>() {
            @Override
            protected void onEvent(HomeEntity.Result result) {
                getView().getResultSucc(result);
                LogUtils.Log("s", "s");
            }

            @Override
            protected void onRxError(Throwable error) {
                getView().getResultError(error.getMessage());
            }
        }));
//        addSubscription(ApiClient.getSxyHomeDataTest().subscribe(new RxSubscriber<String>() {
//            @Override
//            protected void onEvent(String s) {
//                LogUtils.Log("s", s);
//            }
//
//            @Override
//            protected void onRxError(Throwable error) {
//                LogUtils.Log("s", error.getMessage());
//            }
//        }));

    }

    /**
     * 开始展示登录模式下的服务码的布局
     */
    public void showCardLayAnimation(View V) {
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(V, "alpha", 0f, 0.2f, 0.5f, 1f);
        ObjectAnimator scalexAnimator = ObjectAnimator.ofFloat(V, "scaleX", 0f, 1f);
        ObjectAnimator scaleyAnimator = ObjectAnimator.ofFloat(V, "scaleY", 0f, 1f);
        AnimatorSet animationSet = new AnimatorSet();
        animationSet.play(alphaAnimator).with(scalexAnimator).with(scaleyAnimator);
        animationSet.setDuration(1 * 500);
        animationSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animationSet.start();
    }

    /**
     * 消失下边大布局的的animator
     */
    public void initDismissCardAnimator(View V) {
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(V, "alpha", 1f, 0f);

        ObjectAnimator transAnimator = ObjectAnimator.ofFloat(V, "translationX", 0f, -600f);

        ObjectAnimator scalexAnimator = ObjectAnimator.ofFloat(V, "scaleX", 1f, 0.9f, 0.5f, 0.3f, 0.2f, 0.1f, 0f);

        AnimatorSet animationSet = new AnimatorSet();
        animationSet.play(alphaAnimator).with(transAnimator).with(scalexAnimator);
        animationSet.setDuration(1 * 1000);
        animationSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                V.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animationSet.start();

    }

    public void gotoConnectAdviser() {
        DefaultDialog dialog = new DefaultDialog(getContext(), "是否联系投资顾问", "取消", "呼叫") {
            @Override
            public void left() {
                dismiss();
            }

            @Override
            public void right() {
                dismiss();
                NavigationUtils.startDialgTelephone(getContext(), AppManager.getUserInfo(getContext()).adviserPhone);
            }
        };
        dialog.show();
    }
//    //进行签到动作
//    @Override
//    public void todoSign() {
////        addSubscription(ApiClient.SxySign().subscribe(new RxSubscriber<String>() {
////            @Override
////            protected void onEvent(String s) {
////                if (!BStrUtils.isEmpty(s)) {
////                    SignBean signBean = new Gson().fromJson(getV2String(s), SignBean.class);//
////
////                    getView().getSignResult(signBean.resultMessage);
////                } else {
////                    getView().getSignResult("签到失败");
////                }
////
////            }
////
////            @Override
////            protected void onRxError(Throwable error) {
////
////            }
////        }));
//    }


}
