package app.live.com.mvp.presenter;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.cgbsoft.lib.BaseApplication;
import com.cgbsoft.lib.base.model.RongTokenEntity;
import com.cgbsoft.lib.utils.cache.OtherDataProvider;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.google.gson.Gson;
import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.core.ILiveLoginManager;

import org.json.JSONException;
import org.json.JSONObject;

import app.live.com.mvp.model.MySelfInfo;
import app.live.com.mvp.presenter.viewinface.LoginView;
import app.live.com.mvp.presenter.viewinface.LogoutView;
import app.live.com.utils.SxbLog;

/**
 * 登录的数据处理类
 */
public class LoginHelper extends Presenter {
    private Context mContext;
    private static final String TAG = LoginHelper.class.getSimpleName();
    private LoginView mLoginView;
    private LogoutView mLogoutView;

    public LoginHelper(Context context) {
        mContext = context;
    }

    public LoginHelper(Context context, LoginView loginView) {
        mContext = context;
        mLoginView = loginView;
    }

    public LoginHelper(Context context, LogoutView logoutView) {
        mContext = context;
        mLogoutView = logoutView;
    }


    //登录模式登录
    private StandardLoginTask loginTask;

    class StandardLoginTask extends AsyncTask<String, Integer, UserServerHelper.RequestBackInfo> {

        @Override
        protected UserServerHelper.RequestBackInfo doInBackground(String... strings) {

            return UserServerHelper.getInstance().loginId(strings[0], strings[1]);
        }

        @Override
        protected void onPostExecute(UserServerHelper.RequestBackInfo result) {

            if (result != null) {
                if (result.getErrorCode() == 0) {
                    MySelfInfo.getInstance().writeToCache(mContext);
                    //登录
                    iLiveLogin(MySelfInfo.getInstance().getId(), MySelfInfo.getInstance().getUserSig());
                } else {
                    mLoginView.liveLoginFail("Module_TLSSDK", result.getErrorCode(), result.getErrorInfo());
                }
            }

        }
    }


    public void iLiveLogin(String id, String sig) {
        //登录
        ILiveLoginManager.getInstance().iLiveLogin(id, sig, new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {
                if (mLoginView != null)
                    mLoginView.liveLoginSucc();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                if (mLoginView != null)
                    mLoginView.liveLoginFail(module, errCode, errMsg);
            }
        });
    }

    public void getLiveSign(String userId){
        ApiClient.getLiveSign(userId).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                try {
                    JSONObject js = new JSONObject(s);
                    String sig = js.getString("user_sig");
                    mLoginView.getLiveSignSuc(sig);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void onRxError(Throwable error) {

            }
        });
    }


    /**
     * 退出imsdk <p> 退出成功会调用退出AVSDK
     */
    public void iLiveLogout() {
        //TODO 新方式登出ILiveSDK
        ILiveLoginManager.getInstance().iLiveLogout(new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {
                SxbLog.i(TAG, "IMLogout succ !");
                //清除本地缓存
                MySelfInfo.getInstance().clearCache(mContext);
                mLogoutView.logoutSucc();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                SxbLog.e(TAG, "IMLogout fail ：" + module + "|" + errCode + " msg " + errMsg);
            }
        });
    }

    /**
     * 独立模式 登录
     */
    public void standardLogin(String id, String password) {
        loginTask = new StandardLoginTask();
        loginTask.execute(id, password);

    }


    /**
     * 独立模式 注册
     */
    public void standardRegister(final String id, final String psw) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                final UserServerHelper.RequestBackInfo result = UserServerHelper.getInstance().registerId(id, psw);
//                if (null != mContext) {
//                    ((Activity) mContext).runOnUiThread(new Runnable() {
//                        public void run() {
//
//                            if (result != null && result.getErrorCode() == 0) {
//                                standardLogin(id, psw);
//                            } else if (result != null) {
//                                //
//                                Toast.makeText(mContext, "  " + result.getErrorCode() + " : " + result.getErrorInfo(), Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//                }
//            }
//        }).start();
    }


    /**
     * 独立模式 登出
     */
    public void standardLogout(final String id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                UserServerHelper.RequestBackInfo result = UserServerHelper.getInstance().logoutId(id);
                if (result != null && (result.getErrorCode() == 0 || result.getErrorCode() == 10008)) {
                }
            }
        }).start();
        iLiveLogout();
    }


    @Override
    public void onDestory() {
        mLoginView = null;
        mLogoutView = null;
        mContext = null;
    }
}
