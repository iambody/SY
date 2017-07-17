package com.cgbsoft.privatefund.mvp.presenter.home;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.cgbsoft.lib.base.model.HomeEntity;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.LogUtils;
import com.cgbsoft.privatefund.mvp.contract.home.MainHomeContract;

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
