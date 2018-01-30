package com.cgbsoft.privatefund.mvp.presenter.home;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.cgbsoft.lib.AppInfStore;
import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.model.HomeEntity;
import com.cgbsoft.lib.base.model.UserInfoDataEntity;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.BStrUtils;
import com.cgbsoft.lib.utils.tools.LogUtils;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.lib.widget.dialog.DefaultDialog;
import com.cgbsoft.privatefund.bean.product.PublishFundRecommendBean;
import com.cgbsoft.privatefund.mvp.contract.home.MainHomeContract;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

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
                LogUtils.Log("HomeEntityResult", "s");
            }

            @Override
            protected void onRxError(Throwable error) {
                getView().getResultError(error.getMessage());
            }
        }));

    }

    /**
     * 获取首页的缓存数据
     */
    @Override
    public void getHomeCache() {
        getView().getCacheResult(AppManager.getHomeCache(getContext()));
    }

    /**
     * 获取用户信息
     */
    @Override
    public void getUserInf(int type) {
        addSubscription(ApiClient.getUserInfo(AppManager.getUserId(getContext())).subscribe(new RxSubscriber<UserInfoDataEntity.UserInfo>() {
            @Override
            protected void onEvent(UserInfoDataEntity.UserInfo userInfo) {
                if (userInfo != null) {
                    AppInfStore.saveUserInfo(getContext(), userInfo);
                    getView().getUseriInfsucc(type);
                }
            }

            @Override
            protected void onRxError(Throwable error) {
                error.toString();
            }
        }));
    }

    @Override
    public void getPublicFundRecommend() {
        addSubscription(ApiClient.getHomePublicFundRecommend().subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String publishFundRecommendBean) {

                if (!BStrUtils.isEmpty(publishFundRecommendBean)) {
                    try {
                        JSONObject object = new JSONObject(publishFundRecommendBean);
                        if (null != object && object.has("result")) {
                            String result =  object.getString("result");
                            PublishFundRecommendBean publishFundRecommendBean1 = new Gson().fromJson(result, PublishFundRecommendBean.class);
                            if (null != publishFundRecommendBean1) {
                                getView().getPublicFundResult(publishFundRecommendBean1);
                            } else {
                                getView().getPublicFundError("公募基金数据格式有误");
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        getView().getPublicFundError("公募基金数据格式有误");
                    }
                }
            }

            @Override
            protected void onRxError(Throwable error) {

                getView().getPublicFundError(error.getMessage());
            }
        }));
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
     * 游客模式消失下边大布局的的animator
     */
    public void initDismissCardAnimator(View V) {
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(V, "alpha", 1f, 0f);

        ObjectAnimator transAnimator = ObjectAnimator.ofFloat(V, "translationX", 0f);

        ObjectAnimator scalexAnimator = ObjectAnimator.ofFloat(V, "scaleX", 1f, 0.9f, 0.5f, 0.3f, 0.2f, 0.1f, 0f);
        ObjectAnimator scaleyAnimator = ObjectAnimator.ofFloat(V, "scaleY", 1f, 0.94f, 0.94f, 0.94f, 0.94f, 0.9f, 0f);
        AnimatorSet animationSet = new AnimatorSet();
        animationSet.play(alphaAnimator).with(transAnimator).with(scalexAnimator).with(scaleyAnimator);
        animationSet.setDuration(1 * 400);
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

    /**
     * 非游客模式消失下边大布局的的animator
     */
    public void initUserDismissCardAnimator(View V, View v1, View v2) {
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(V, "alpha", 1f, 0f);

        ObjectAnimator transAnimator = ObjectAnimator.ofFloat(V, "translationX", 0f);
        ObjectAnimator transyAnimator = ObjectAnimator.ofFloat(V, "translationY", 0f);
        ObjectAnimator scalexAnimator = ObjectAnimator.ofFloat(V, "scaleX", 1f, 0.9f, 0.5f, 0.3f, 0.2f, 0.1f, 0f);
        ObjectAnimator scaleyAnimator = ObjectAnimator.ofFloat(V, "scaleY", 1f, 0.84f, 0.84f, 0.84f, 0.84f, 0.84f, 0f);
        AnimatorSet animationSet = new AnimatorSet();
        animationSet.play(alphaAnimator).with(transAnimator).with(scalexAnimator).with(scaleyAnimator).with(transyAnimator);
        animationSet.setDuration(1 * 400);
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
        v1.setVisibility(View.GONE);
        v2.setVisibility(View.GONE);
    }

    public void gotoConnectAdviser() {
        DefaultDialog dialog = new DefaultDialog(getContext(), "是否联系私行家", "取消", "呼叫") {
            @Override
            public void left() {
                dismiss();
            }

            @Override
            public void right() {
                dismiss();
                try {
                    NavigationUtils.startDialgTelephone(getContext(), AppManager.getUserInfo(getContext()).adviserPhone);
                } catch (Exception e) {

                }

            }
        };
        dialog.show();
    }


}
