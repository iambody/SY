package com.cgbsoft.lib.utils.rxjava;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * User: Axl_Jacobs(Axl.Jacobs@gmail.com)
 * Date: 2016-09-03
 * Time: 11:16
 * FIXME
 * 处理Rx线程
 */
public class RxSchedulersHelper {
    //处理Rx线程
    public static <T> Observable.Transformer<T, T> io_main() {
        return tObservable -> tObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
