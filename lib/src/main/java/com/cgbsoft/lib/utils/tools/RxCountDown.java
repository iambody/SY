package com.cgbsoft.lib.utils.tools;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/7/19-09:35
 */
public class RxCountDown {
    /**
     * 倒计时定时器定时器
     *
     * @param time
     * @return
     */
    public static Observable<Integer> countdown(int time) {
        if (time < 0) time = 0;

        final int countTime = time;


        return Observable.interval(0, 1, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<Long, Integer>() {
                    @Override
                    public Integer call(Long increaseTime) {
                        return countTime - increaseTime.intValue();
                    }
                })
                .take(countTime + 1);

    }

    /**
     * 计时器
     */

    private static Subscription keeptimeSubscription;
    private static int times = 0;

    /**
     * 开始计时
     *
     * @param keepTimeInterfaces
     */
    public static void keepTimeDown(keepTimeInterface keepTimeInterfaces) {
        if (null != keeptimeSubscription) return;

        keeptimeSubscription = Observable.interval(1, 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        times = times + 1;

                        if (null != keepTimeInterfaces) {
                            keepTimeInterfaces.allTime(times);
                        }
                    }
                });
    }

    /**
     * 结束计时
     */
    public static void stopKeepTime() {
        if (null != keeptimeSubscription &&! keeptimeSubscription.isUnsubscribed()) {
            keeptimeSubscription.unsubscribe();
            times = 0;
        }
    }

    public interface keepTimeInterface {
        public void allTime(int allTime);
    }

    /**
     * 重新封装的定时器****************************************************************************
     */
    private static Subscription countDownmSubscription;

    public static void countTimeDown(int time, ICountTime iCountTime1) {
        if (null != countDownmSubscription) return;
        if (time < 0) time = 0;

        final int countTime = time;

        countDownmSubscription = Observable.interval(0, 1, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<Long, Integer>() {
                    @Override
                    public Integer call(Long increaseTime) {
                        return countTime - increaseTime.intValue();
                    }
                })
                .take(countTime + 1).subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        iCountTime1.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        iCountTime1.onNext(integer);
                    }
                });
    }

    public interface ICountTime {
        void onCompleted();

        void onNext(int timer);
    }

    public static void stopCountTime() {
        if (null != countDownmSubscription && !countDownmSubscription.isUnsubscribed()) {
            countDownmSubscription.unsubscribe();
            countDownmSubscription = null;
        }
    }

    /***
     * 重新封装的新的********************************************************************************
     *
     */
    private static Subscription mSubscription;
    private IRxNext iRxNext;

    public static void rollPoling(int time, IRxNext next) {
        mSubscription = Observable.interval(time, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        if (null != next)
                            next.doNext();


                    }
                });
    }


    /**
     * 取消订阅
     */
    public static void cancel() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }

    }


    public interface IRxNext {
        void doNext();
    }


}
