package app.ocrlib.com;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;


public class AppHandler {
    public static final int ERROR_DATA = -100;
    public static final int ERROR_LOCAL = -101;
    public static final String DATA_MODE_EASY = "data_mode_easy";
    public static final String DATA_MODE_MID = "data_mode_mid";
    public static final String DATA_MODE_ADVANCED = "data_mode_advanced";
    public static final String DATA_MODE_OCR = "data_mode_ocr";
    private static final int WHAT_SIGN = 1;
    private static final int ARG1_SUCCESS = 1;
    private static final int ARG1_FAILED = 2;
    private static final String DATA_MODE = "data_mode";
    private static final String DATA_SIGN = "data_sign";
    private static final String DATA_CODE = "data_code";
    private static final String DATA_MSG = "data_msg";

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == WHAT_SIGN) {
                if (msg.arg1 == ARG1_SUCCESS) {
                    String sign = msg.getData().getString(DATA_SIGN);
                    if (msg.getData().getString(DATA_MODE).equals(DATA_MODE_MID)) {//活体检测
//                        LivingManger.openCloudFaceService(FaceVerifyStatus.Mode.MIDDLE, sign);
                    } else if (msg.getData().getString(DATA_MODE).equals(DATA_MODE_OCR)) { //ocr
                        OcrManger.startOcrActivity(sign);
                    }
                } else {
                    int code = msg.getData().getInt(DATA_CODE);
                    String message = msg.getData().getString(DATA_MSG);
                    Log.e("AppHandler", "请求失败:[" + code + "]," + message);

                    //      Toast.makeText(activity, "请求失败:[" + code + "]," + message, Toast.LENGTH_SHORT).show();
//                    if (FirstActivity.isOcrActivity()){
//                        activityFirst.hideLoading();
//                    }else {
//                        activity.hideLoading();
//                    }


                }
            }
        }
    };




    public void sendSignError(int code, String msg) {
        Message message = new Message();
        message.what = WHAT_SIGN;
        message.arg1 = ARG1_FAILED;
        final Bundle data = new Bundle();
        data.putInt(DATA_CODE, code);
        data.putString(DATA_MSG, msg);
        message.setData(data);
        handler.sendMessage(message);
    }

    public void sendSignSuccess(String mode, String sign) {
        Message message = new Message();
        message.what = WHAT_SIGN;
        message.arg1 = ARG1_SUCCESS;
        final Bundle data = new Bundle();
        data.putString(DATA_SIGN, sign);
        data.putString(DATA_MODE, mode);
        message.setData(data);
        handler.sendMessage(message);
    }
}
