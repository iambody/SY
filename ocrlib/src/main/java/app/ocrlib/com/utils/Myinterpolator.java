package app.ocrlib.com.utils;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.animation.BaseInterpolator;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/12/8-11:28
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
public class Myinterpolator extends BaseInterpolator {
    @Override
    public float getInterpolation(float input) {
        return input;
    }
}
