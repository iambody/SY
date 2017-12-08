package app.ocrlib.com.utils;


import android.animation.TypeEvaluator;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/12/8-10:47
 */
public class MyTypeEvaluator implements TypeEvaluator<TypeBean> {
     // 属性动画封装了一个因子fraction，我们设置动画时需要setDuration(xxxx)，例如时间为1000ms，那么当到达100ms时，fraction就为0.1
    // fraction也就是当前时间占总时间的百分比，startValue和endValue就是我们传入的初始值和结束值
   //
    @Override
    public TypeBean evaluate(float fraction, TypeBean startValue, TypeBean endValue) {
        return null;
    }
}
