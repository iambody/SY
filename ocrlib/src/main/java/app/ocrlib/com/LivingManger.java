package app.ocrlib.com;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.privatefund.bean.ocr.LivingSign;
import com.google.gson.Gson;
import com.webank.wbcloudfaceverify2.tools.ErrorCode;
import com.webank.wbcloudfaceverify2.tools.IdentifyCardValidate;
import com.webank.wbcloudfaceverify2.tools.WbCloudFaceVerifySdk;
import com.webank.wbcloudfaceverify2.ui.FaceVerifyStatus;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * desc   活体检测工具类
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/10/17-19:08
 */
public class LivingManger {
    private static LivingResult livingResult;
    //上下文
    private static Context livingContext;
    //身份证名字
    private static String Cardname;
    //身份证号
    private static String Cardid;
    //是否显示成功页面  暂时显示成功失败页面
    private static boolean isShowSuccess = true;
    private static boolean isShowFail = true;
    //主题颜色
    private static String color;

    private static ProgressDialog progressDlg;

    private static SharedPreferences sp;

    //    private static String userId = "testCloudFaceVerify" + System.currentTimeMillis();
//    private static String nonce = "52014832029547845621032584562012";
//    private static String KKKeyicen = "JG7ipvk02aMb0BTnciy+EvStgPHb4oAzfiioi3uIpE3DrZKVYQG+Xc2ykjuUNmXuk4BK5zaeOpHdO9mdUlhvgqp7KZurrDdqvVt5fkeYDWtx26mQu2s7b4Rjn6Y7CbH9jhJrHBLXqcBm7zqhnJ3Y8AhJ9nQPbPnCY1Ln8IN4ejYdY4La49gkzvAFb1N21ipOImAt1HHU0RbuXDz/OVsJ4ZVQFXp8DmdniweHVi049dIPvokwj9z5A0kONQRgrfRvIMLgGQXGfT2+A1:D682K+7lEzGtuexx5F1e3K2IC8ZXuxiOD7hFc478HPSLq0e0U619G99/OePSPD+uhURbobew==";
//    private static String APPID = "TIDAjl3C";
    private static LivingSign livingSign;

    private LivingManger() {
    }


    public LivingManger(Context livingContext, String cardname, String cardid, LivingResult ocrResult) {
        this.livingResult = ocrResult;
        this.livingContext = livingContext;
        Cardname = cardname;
        Cardid = cardid;
        initConifg();

    }

    //开始初始化*********************************
    private void initConifg() {
        sp = livingContext.getSharedPreferences("FaceVerify", Context.MODE_PRIVATE);

        initProgress();
        //默认选择黑色模式 也可以选择白色（需要在build.gradle里更换对应白色资源包）
        color = WbCloudFaceVerifySdk.BLACK;
//        color = WbCloudFaceVerifySdk.WHITE;


    }

    /**
     * 开始活体检测
     */
    public static void startLivingMatch() {
        if (Cardname != null && Cardname.length() != 0) {
            if (Cardid != null && Cardid.length() != 0) {
                if (Cardid.contains("x")) {
                    Cardid = Cardid.replace('x', 'X');
                }

                IdentifyCardValidate vali = new IdentifyCardValidate();
                String msg = vali.validate_effective(Cardid);
                if (msg.equals(Cardid)) {
                    progressDlg.show();
//                    signUseCase.execute(AppHandler.DATA_MODE_MID, APPID, userId, nonce);
                    getSign();

                } else {
                    Toast.makeText(livingContext, "用户证件号错误", Toast.LENGTH_SHORT).show();
                    return;
                }
            } else {
                Toast.makeText(livingContext, "用户证件号不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            Toast.makeText(livingContext, "用户姓名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    /**
     * 开始人脸识别的登录操作登录成功即可进行ui接入
     *
     * @param mode
     * @param sign
     */
    public static void openCloudFaceService(final FaceVerifyStatus.Mode mode, String sign, String APPID, String nonce, String userId, String keyicen, final String orderNum) {
        final String modeShowGuide = mode.toString();
        Bundle data = new Bundle();
        WbCloudFaceVerifySdk.InputData inputData = new WbCloudFaceVerifySdk.InputData(
                Cardname,
                "01",
                Cardid,
                orderNum,
                "ip=xxx.xxx.xxx.xxx",
                "lgt=xxx,xxx;lat=xxx.xxx",
                APPID,
                "1.0.0",
                nonce,
                userId,
                sign,
                sp.getBoolean(modeShowGuide, true),
                mode,
                keyicen);

        data.putSerializable(WbCloudFaceVerifySdk.INPUT_DATA, inputData);
        //是否展示刷脸成功页面，默认展示
        data.putBoolean(WbCloudFaceVerifySdk.SHOW_SUCCESS_PAGE, isShowSuccess);
        //是否展示刷脸失败页面，默认展示
        data.putBoolean(WbCloudFaceVerifySdk.SHOW_FAIL_PAGE, isShowFail);
        //颜色设置
        data.putString(WbCloudFaceVerifySdk.COLOR_MODE, color);
        //是否对录制视频进行检查,默认不检查
//        data.putBoolean(WbCloudFaceVerifySdk.VIDEO_CHECK, true);
        WbCloudFaceVerifySdk.getInstance().init(livingContext, data, new WbCloudFaceVerifySdk.FaceVerifyLoginListener() {
            @Override
            public void onLoginSuccess() {
                progressDlg.dismiss();

                WbCloudFaceVerifySdk.getInstance().startActivityForSecurity(new WbCloudFaceVerifySdk.FaceVerifyResultForSecureListener() {
                    @Override
                    public void onFinish(int resultCode, boolean nextShowGuide, String faceCode, String faceMsg, String sign, Bundle extendData) {
                        if (faceCode == null) {
                            faceCode = "";
                        }
                        if (faceMsg == null) {
                            faceMsg = "";
                        }

                        if (resultCode == 0) {
                            //需要通知后台*********************
                            ApiClient.livingQueryResult(orderNum).subscribe(new RxSubscriber<String>() {
                                @Override
                                protected void onEvent(String s) {

                                }

                                @Override
                                protected void onRxError(Throwable error) {

                                }
                            });
                            //已经通知后台***********************
                            if (null != livingResult) livingResult.livingSucceed();
                            if (!isShowSuccess) {
                                Toast.makeText(livingContext, "刷脸成功", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            if (null != livingResult) livingResult.livingFailed();
                            if (!isShowFail) {
                                Toast.makeText(livingContext, "刷脸失败：errorCode=" + resultCode + " ;faceCode= " + faceCode + " ;faceMsg=" + faceMsg, Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
            }

            @Override
            public void onLoginFailed(String errorCode, String errorMsg) {
                progressDlg.dismiss();
                if (null != livingResult) livingResult.livingFailed();
                if (errorCode.equals(ErrorCode.FACEVERIFY_LOGIN_PARAMETER_ERROR)) {
                    Toast.makeText(livingContext, "传入参数有误！" + errorMsg, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(livingContext, "登录刷脸sdk失败！" + "errorCode= " + errorCode + " ;errorMsg=" + errorMsg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 初始化进度条
     */
    private void initProgress() {
        if (progressDlg != null) {
            progressDlg.dismiss();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            progressDlg = new ProgressDialog(livingContext);
        } else {
            progressDlg = new ProgressDialog(livingContext);
            progressDlg.setInverseBackgroundForced(true);
        }
        progressDlg.setMessage("加载中...");
        progressDlg.setIndeterminate(true);
        progressDlg.setCanceledOnTouchOutside(false);
        progressDlg.setCancelable(true);
        progressDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDlg.setCancelable(false);
    }

    public static void destory() {
        if (null != progressDlg) {
            progressDlg.dismiss();
            progressDlg = null;
        }

    }

    /**
     * 请求server后台的sign
     */
    private static void getSign() {
        //开始网络请求==成功就开始登录 失败重新请求
        ApiClient.getLivingSign().subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                try {
                    JSONObject object = new JSONObject(s);


                    livingSign = new Gson().fromJson(object.getString("result"), LivingSign.class);
                    openCloudFaceService(FaceVerifyStatus.Mode.MIDDLE, livingSign.getSign(), livingSign.getAppId(), livingSign.getNonce(), livingSign.getUserId(), livingSign.getLicence(), livingSign.getOrderNum());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void onRxError(Throwable error) {
                Log.i("ss", error.getMessage());
                progressDlg.dismiss();
            }
        });
    }

}
