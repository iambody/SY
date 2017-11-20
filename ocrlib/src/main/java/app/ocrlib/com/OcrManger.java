package app.ocrlib.com;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.PromptManager;
import com.webank.mbank.ocr.WbCloudOcrSDK;
import com.webank.mbank.ocr.net.EXIDCardResult;
import com.webank.mbank.ocr.tools.ErrorCode;
import com.webank.normal.tools.WLogger;

import java.security.SecureRandom;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/10/17-19:07
 */
public class OcrManger {
    private static final String TAG = "OcrManger";
    private static ProgressDialog progressDlg;
    private static AppHandler appHandler;
    private static SignUseCase signUseCase;
    private static OcrResult ocrResult;
    private static Context ocrContext;
    //正面照片
    public static final int OCR_FRONT = 0;
    //反面照片
    public static final int OCR_BACK = 1;

    private static int identityCardSide;

    /**
     * 拉起ocr sdk时用到的参数  进攻本地测试使用 后续删除   我们线上环境参数都通过后台接口获取！！！！！
     **/
    static String userId = "ocr" + System.currentTimeMillis();
    static String nonce = randomAlphabetic(32);
    static String orderNo = "test" + System.currentTimeMillis();
    static String appId = "TIDA0001";
    static String openApiAppVersion = "1.0.0";

    //默认的OCR 正反面
    static WbCloudOcrSDK.WBOCRTYPEMODE normalType = WbCloudOcrSDK.WBOCRTYPEMODE.WBOCRSDKTypeNormal;
    //正面的OCR
    static WbCloudOcrSDK.WBOCRTYPEMODE frontSideType = WbCloudOcrSDK.WBOCRTYPEMODE.WBOCRSDKTypeFrontSide;//;
    //背面的OCR
    static WbCloudOcrSDK.WBOCRTYPEMODE backSideType = WbCloudOcrSDK.WBOCRTYPEMODE.WBOCRSDKTypeFrontSide;//WBOCRSDKTypeNormal;

    private OcrManger() {
    }

    /**
     * @param context   需要传Activity 不要传application的context
     * @param cardSide
     * @param ocrResult
     */
    public OcrManger(Context context, int cardSide, OcrResult ocrResult) {
        this.ocrContext = context;
        this.identityCardSide = cardSide;
        this.ocrResult = ocrResult;
        //开始初始化
        appHandler = new AppHandler();
        signUseCase = new SignUseCase(appHandler);
        initProgress();
    }


    //开始进行获取sign的操作
    public void startOcr() {
        orderNo = "test" + System.currentTimeMillis();
        progressDlg.show();
        signUseCase.execute(AppHandler.DATA_MODE_OCR, "TIDA0001", userId, nonce);
    }

    /**
     * 开始启动SDK进行OCR识别
     *
     * @param sign
     */
    public static void startOcrActivity(String sign) {
        hideLoading();
        //启动SDK，进入SDK界面
        Bundle data = new Bundle();
        WbCloudOcrSDK.InputData inputData = new WbCloudOcrSDK.InputData(
                orderNo,
                appId,
                openApiAppVersion,
                nonce,
                userId,
                sign);
        data.putSerializable(WbCloudOcrSDK.INPUT_DATA, inputData);
        data.putString(WbCloudOcrSDK.TITLE_BAR_COLOR, "#ffffff");

        data.putString(WbCloudOcrSDK.TITLE_BAR_CONTENT, "身份证识别");
        data.putString(WbCloudOcrSDK.WATER_MASK_TEXT, "仅供本次业务使用");
        data.putLong(WbCloudOcrSDK.SCAN_TIME, 20000);
        //启动SDK，进入SDK界面
        WbCloudOcrSDK.getInstance().init(ocrContext, data, new WbCloudOcrSDK.OcrLoginListener() {
            @Override
            public void onLoginSuccess() {  //登录成功,拉起SDk页面
                if (progressDlg != null) {
                    progressDlg.dismiss();
                }
                WbCloudOcrSDK.getInstance().startActivityForOcr(ocrContext, new WbCloudOcrSDK.IDCardScanResultListener() {  //证件结果回调接口
                    @Override
                    public void onFinish(EXIDCardResult exidCardResult, String resultCode, String resultMsg) {
                        // resultCode为0，则识别成功；否则识别失败
                        if ("0".equals(resultCode)) {
                            WLogger.d(TAG, "识别成功");
                            // 识别成功  第三方应用对识别的结果进行操作
                            try {
                                PromptManager.ShowCustomToast(ocrContext, "识别成功成功");
                                EXIDCardResult result = WbCloudOcrSDK.getInstance().getResultReturn();

//                                if (null != ocrResult) ocrResult.OcrSucceed(eXIDCardToCard(result));
//                                Intent i = new Intent(FirstActivity.this, ResultActivity.class);
//                                startActivity(i);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            WLogger.d(TAG, "识别失败");
                            if (null != ocrResult) ocrResult.OcrFailed("识别失败");
                        }

                    }
                }, (0 == identityCardSide) ? frontSideType : backSideType);
            }

            @Override
            public void onLoginFailed(String errorCode, String errorMsg) {
                if (progressDlg != null) {
                    progressDlg.dismiss();
                }
                if (null != ocrResult) ocrResult.OcrFailed("登录失败");
                if (errorCode.equals(ErrorCode.IDOCR_LOGIN_PARAMETER_ERROR)) {
                    Toast.makeText(ocrContext, "传入参数有误！" + errorMsg, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ocrContext, "登录OCR sdk失败！" + "errorCode= " + errorCode + " ;errorMsg=" + errorMsg, Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


//    private static IdentityCard eXIDCardToCard(EXIDCardResult result) {
//        IdentityCard identityCard = new IdentityCard();
//        identityCard.setAddress(result.address);
//        identityCard.setBirth(result.birth);
//        identityCard.setFrontFullImageSrc(result.frontFullImageSrc);
//        identityCard.setIdNo(result.cardNum);
//        identityCard.setName(result.name);
//        identityCard.setNation(result.nation);
//        identityCard.setOcrId(result.ocrId);
//        identityCard.setSign(result.sign);
//        identityCard.setSex(result.sex);
//        return identityCard;
//    }

    /**
     * 初始化进度条
     */
    private void initProgress() {
        if (progressDlg != null) {
            progressDlg.dismiss();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            progressDlg = new ProgressDialog(ocrContext);
        } else {
            progressDlg = new ProgressDialog(ocrContext);
            progressDlg.setInverseBackgroundForced(true);
        }
        progressDlg.setMessage("加载中...");
        progressDlg.setIndeterminate(true);
        progressDlg.setCanceledOnTouchOutside(false);
        progressDlg.setCancelable(true);
        progressDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDlg.setCancelable(false);
    }

    /**
     *
     */
    public static void hideLoading() {
        if (progressDlg != null) {
            progressDlg.dismiss();
        }
        if (null != ocrContext) {
            ocrContext = null;
        }
    }

    public static void destory() {
        if (null != ocrResult) ocrResult = null;
        if (null != ocrContext) ocrContext = null;
        if (null != signUseCase) signUseCase = null;
        if (null != appHandler) appHandler = null;
        if (null != progressDlg) {
            progressDlg.dismiss();
            progressDlg = null;
        }
    }

    /**
     * 获取一个随机数
     *
     * @return
     */
    public static String randomAlphabetic(int count) {
        String rst = "";
        try {
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < count; i++) {
                int randomNum = Math.abs(secureRandom.nextInt() % 52);
                if (randomNum > 26) {
                    stringBuffer.append((char) ('a' + (randomNum - 26)));
                } else {
                    stringBuffer.append((char) ('A' + randomNum));
                }
            }

            rst = stringBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rst;
    }

    /**
     * 请求server后台的sign
     */
    private void getSign() {
        //开始网络请求==成功就开始登录 失败重新请求
        ApiClient.getOcrSign().subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {

            }

            @Override
            protected void onRxError(Throwable error) {

            }
        });
    }
}
