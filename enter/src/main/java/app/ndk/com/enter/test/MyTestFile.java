package app.ndk.com.enter.test;

import java.util.List;

import app.ndk.com.enter.mvp.bean.BindBean;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/8/20-14:01
 */
public class MyTestFile {
    public void test(List<BindBean> dats) {
        Observable.just(dats).map(new Func1<List<BindBean>, String>() {
            @Override
            public String call(List<BindBean> bindBeen) {
                return null;
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {

            }
        })
    }
}
