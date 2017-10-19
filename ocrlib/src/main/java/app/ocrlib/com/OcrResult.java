package app.ocrlib.com;

import com.cgbsoft.privatefund.bean.ocr.IdentityCard;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/10/17-19:10
 */
public interface OcrResult {
    void OcrSucceed(IdentityCard identityCard);

    void OcrFailed(String meg);
}
