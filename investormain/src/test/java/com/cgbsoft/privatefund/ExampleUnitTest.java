package com.cgbsoft.privatefund;

import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.LogUtils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {

        List<String> aaa = new ArrayList<>();
        aaa.add("aaaa");
        aaa.add("bbbb");
        aaa.add("cccc");
        aaa.add("vvvv");
        aaa.add("aabb");
        Observable.from(aaa).filter(new Func1<String, Boolean>() {
            @Override
            public Boolean call(String s) {
                return s.endsWith("bb");
            }
        }).map(new Func1<String, String>() {
            @Override
            public String call(String s) {
                return "我的map数据=》" + s;
            }
        }).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                LogUtils.Log("zxcvb", s);
            }

            @Override
            protected void onRxError(Throwable error) {
                LogUtils.Log("zxcvb", "错误");
            }
        });

    }
}