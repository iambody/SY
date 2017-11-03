package app.ocrlib.com;

import com.cgbsoft.privatefund.bean.living.LivingResultData;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/10/17-19:10
 */
public interface LivingResult {
    void livingSucceed(LivingResultData resultData);

    void livingFailed(LivingResultData resultData);
}
