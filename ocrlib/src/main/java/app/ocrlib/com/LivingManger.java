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
import com.cgbsoft.lib.utils.tools.PromptManager;
import com.cgbsoft.privatefund.bean.living.LivingResultData;
import com.cgbsoft.privatefund.bean.living.LivingSign;
import com.google.gson.Gson;
import com.webank.wbcloudfaceverify2.tools.ErrorCode;
import com.webank.wbcloudfaceverify2.tools.IdentifyCardValidate;
import com.webank.wbcloudfaceverify2.tools.WbCloudFaceVerifySdk;
import com.webank.wbcloudfaceverify2.ui.FaceVerifyStatus;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

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
    //有效期
    private static String cardValidity;
    //credentialCode
    private static String credentialCode;
    //customerCode
    private static String customerCode;
    //type
    private static String type;
    //性别 男女
    private static String sex;
    //生日
    private static String birthday;
    //一串正反面
    private static List<String> imageUrl;
    //是否显示成功页面  暂时显示成功失败页面
    private static boolean isShowSuccess = true;
    private static boolean isShowFail = true;
    //主题颜色
    private static String color;
    private static ProgressDialog progressDlg;
    private static SharedPreferences sp;
    private static LivingSign livingSign;
    private static int MangerType;
    private LivingManger() {
    }

    /**
     * 身份证流程的活体检测构造
     *
     * @param livingContext
     * @param cardname
     * @param cardid
     * @param credentialcode
     * @param customercode
     * @param Type
     * @param ocrResult
     */
    public LivingManger(Context livingContext, String cardname, String cardid, String cardvalidity, String credentialcode, String customercode, String sex, String birthday, String Type, List<String> imageurl, LivingResult ocrResult) {
        this.livingResult = ocrResult;
        this.livingContext = livingContext;
        this.Cardname = cardname;
        this.Cardid = cardid;
        this.credentialCode = credentialcode;
        this.customerCode = customercode;
        this.type = Type;
        this.imageUrl = imageurl;
        this.cardValidity = cardvalidity;
        this.MangerType = 2;
        this.sex = sex;
        this.birthday = birthday;
        initConifg();
    }

    /**
     * 公用的活体检验构造函数
     *
     * @param ocrResult
     */
    public LivingManger(Context livingContext, String credentialcode, String customercode, LivingResult ocrResult) {
        this.livingResult = ocrResult;
        this.livingContext = livingContext;
        this.credentialCode = credentialcode;
        this.customerCode = customercode;
        this.MangerType = 1;

        initConifg();

    }

    /******************开始初始化*****************/
    private void initConifg() {
        sp = livingContext.getSharedPreferences("FaceVerify", Context.MODE_PRIVATE);
        initProgress();
        //默认选择黑色模式 也可以选择白色（需要在build.gradle里更换对应白色资源包）
        color = WbCloudFaceVerifySdk.BLACK;
        //color = WbCloudFaceVerifySdk.WHITE;
    }

    /************开始活体检测**********************/
    public static void startLivingMatch() {
        if (1 == MangerType) {//如果是公用的
            publicStartLivingMatch();
            return;
        }
        if (Cardname != null && Cardname.length() != 0) {
            if (Cardid != null && Cardid.length() != 0) {
                if (Cardid.contains("x")) {
                    Cardid = Cardid.replace('x', 'X');
                }

                IdentifyCardValidate vali = new IdentifyCardValidate();
                String msg = vali.validate_effective(Cardid);
                if (msg.equals(Cardid)) {
                    progressDlg.show();
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
     * 公用人脸锁先获取sign
     */
    private static void publicStartLivingMatch() {
        //开始网络请求==成功就开始登录 失败重新请求
        ApiClient.getLivingSign().subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    livingSign = new Gson().fromJson(object.getString("result"), LivingSign.class);
                    Cardname = livingSign.getIdCardName();
                    Cardid = livingSign.getIdCardNum();
                    /****需要先判断身份证号***/
                    if (Cardname != null && Cardname.length() != 0) {
                        if (Cardid != null && Cardid.length() != 0) {
                            if (Cardid.contains("x")) {
                                Cardid = Cardid.replace('x', 'X');
                            }

                            IdentifyCardValidate vali = new IdentifyCardValidate();
                            String msg = vali.validate_effective(Cardid);
                            if (msg.equals(Cardid)) {
                                openCloudFaceService(FaceVerifyStatus.Mode.MIDDLE, livingSign.getSign(), livingSign.getAppId(), livingSign.getNonce(), livingSign.getUserId(), livingSign.getLicence(), livingSign.getOrderNum());
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
//                    openCloudFaceService(FaceVerifyStatus.Mode.MIDDLE, livingSign.getSign(), livingSign.getAppId(), livingSign.getNonce(), livingSign.getUserId(), livingSign.getLicence(), livingSign.getOrderNum());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void onRxError(Throwable error) {
                Log.i("ss", error.getMessage());
                progressDlg.dismiss();
                PromptManager.ShowCustomToast(livingContext, "获取sign失败");
            }
        });
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
        //data.putBoolean(WbCloudFaceVerifySdk.VIDEO_CHECK, true);
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

                        if (resultCode == 0) {//成功
                            //需要通知后台
                            if (2 == MangerType) {
                                sendDataResult(imageUrl, Cardid, Cardname, cardValidity, orderNum, faceCode, credentialCode, customerCode, type);
                            } else {
                                sendCommontDataResult(orderNum, faceCode, livingSign.getIdCardNum(), livingSign.getIdCardName(), credentialCode, customerCode);
                            }
                            //已经通知后台 if (null != livingResult) livingResult.livingSucceed();
                            if (!isShowSuccess) {
                                Toast.makeText(livingContext, "刷脸成功", Toast.LENGTH_SHORT).show();
                            }
                        } else {//失败
                            //需要通知后台
                            if (2 == MangerType) {
                                sendDataResult(imageUrl, Cardid, Cardname, cardValidity, orderNum, faceCode, credentialCode, customerCode, type);
                            } else {
                                sendCommontDataResult(orderNum, faceCode, livingSign.getIdCardNum(), livingSign.getIdCardName(), credentialCode, customerCode);
                            }
                            //已经通知后台
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
                //需要通知后台
                if (2 == MangerType) {
                    sendDataResult(imageUrl, Cardid, Cardname, cardValidity, orderNum, errorCode, credentialCode, customerCode, type);
                } else {
                    sendCommontDataResult(orderNum, errorCode, livingSign.getIdCardNum(), livingSign.getIdCardName(), credentialCode, customerCode);
                }

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
                PromptManager.ShowCustomToast(livingContext, "获取sign失败");
            }
        });
    }

    /**
     * 二次通知后台
     * 最后三个参数credentialCode||customerCode||type是从证件夹传进来的 返回的数据0成功 1客服审核 2ocr错误3标识失败
     */
    public static void sendDataResult(List<String> imageUrl, String cardNum, String cardName, String cardValidity, String orderNo, String faceCode, String credentialCode, String customerCode, String type) {
        Log.i("活体living", "开始请求活体接口");
        //需要获取结果的
        ApiClient.getLivingQueryDataResult(imageUrl, cardNum, cardName, cardValidity, orderNo, faceCode, credentialCode, customerCode, type, sex, birthday).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String data) {
                Log.i("活体living", " 活体接口返回成功" + data);
//                if (null != livingResult) livingResult.livingSucceed(data);
                try {
                    JSONObject obj = new JSONObject(data);
                    String result = obj.getString("result");
                    LivingResultData livingResultData = new Gson().fromJson(result, LivingResultData.class);
                    if (null != livingResult) livingResult.livingSucceed(livingResultData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                PromptManager.ShowCustomToast(livingContext, "返回成功了");
            }

            @Override
            protected void onRxError(Throwable error) {
                Log.i("活体living", " 活体接口返回失败" + error.getMessage());
                if (null != livingResult)
                    livingResult.livingFailed(new LivingResultData(error.getMessage(), "3"));
                PromptManager.ShowCustomToast(livingContext, error.getMessage());
            }
        });
    }

    /**
     * 公用锁的通知后台模式0成功 1客服审核 2ocr错误3标识失败
     */
    public static void sendCommontDataResult(String orderNo, String faceCode, String number, String name, String credentialCode, String customerCode) {
        ApiClient.getLivingQueryCommntDataResult(orderNo, faceCode, number, name, credentialCode, customerCode).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String data) {
                try {
                    JSONObject obj = new JSONObject(data);
                    String result = obj.getString("result");
                    LivingResultData livingResultData = new Gson().fromJson(result, LivingResultData.class);
                    if (null != livingResult) livingResult.livingSucceed(livingResultData);
                } catch (JSONException e) {
                    e.printStackTrace();
                    livingResult.livingFailed(new LivingResultData("解析错误", "3" ));
                }
            }

            @Override
            protected void onRxError(Throwable error) {
                if (null != livingResult)
                    livingResult.livingFailed(new LivingResultData(error.getMessage(), "3" ));
                PromptManager.ShowCustomToast(livingContext, error.getMessage());
            }
        });
    }
}

