package app.ndk.com.enter.mvp.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cgbsoft.lib.AppInfStore;
import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.model.UserInfoDataEntity;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.encrypt.RSAUtils;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.BStrUtils;
import com.cgbsoft.lib.utils.tools.LogUtils;
import com.cgbsoft.lib.utils.tools.MD5Utils;
import com.cgbsoft.lib.utils.tools.PromptManager;
import com.cgbsoft.lib.widget.dialog.LoadingDialog;
import com.cgbsoft.privatefund.bean.StrResult;
import com.chenenyu.router.Router;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;

import app.ndk.com.enter.R;
import app.ndk.com.enter.mvp.contract.SetPasswordContract;


/**
 * Created by xiaoyu.zhang on 2016/11/23 15:50
 * Email:zhangxyfs@126.com
 *  
 */
public class SetPasswordPresenter extends BasePresenterImpl<SetPasswordContract.View> implements SetPasswordContract.Presenter {
    private Context context;

    public String myPublicKey;

    public SetPasswordPresenter(Context context, SetPasswordContract.View view) {
        super(context, view);
        this.context = context;
    }

    @Override
    public void resetPwd(final LoadingDialog loadingDialog, String un, String pwd, String code, boolean isFromGesture) {
    public void resetPwd(final LoadingDialog loadingDialog, String un, String pwd, String code, String publickeys) {
        loadingDialog.setLoading(getContext().getString(R.string.reseting_str));
        loadingDialog.show();
        addSubscription(ApiClient.resetTestPwd(un, MD5Utils.getShortMD5(pwd), code).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                loadingDialog.setResult(true, "重置成功", 1000, () -> {
                    if (isFromGesture) {//是从忘记手势密码进来的、
                        getView().setGesturePassword();
                    } else {//正常忘记密码
                        toNormalLogin(loadingDialog, un, pwd, false);
                    }
                });
                loadingDialog.setResult(true, "重置成功", 1000, () -> toNormalLogin(loadingDialog, un, pwd, false, publickeys));
            }

            @Override
            protected void onRxError(Throwable error) {
                loadingDialog.setResult(true, "重置失败", 1000);
            }
        }));
    }

    /**
     * 登录接口
     *
     * @param un   用户名
     * @param pwd  密码
     * @param isWx 是否微信登录
     */
    @Override
    public void toNormalLogin(@NonNull final LoadingDialog loadingDialog, final String un, String pwd, boolean isWx, String publicKeys) {
        loadingDialog.setLoading(getContext().getString(R.string.la_login_loading_str));
        loadingDialog.show();
        pwd = isWx ? pwd : MD5Utils.getShortMD5(pwd);
        //******* 登录
        JSONObject object = new JSONObject();
        String RsaStr = "";
        try {
            object.put("userName", un);
            object.put("password", pwd);
            object.put("client", AppManager.isInvestor(getContext()) ? "C" : "B");
            RsaStr = RSAUtils.GetRsA(getContext(), object.toString(), publicKeys);

        } catch (Exception e) {
            e.printStackTrace();
            PromptManager.ShowCustomToast(getContext(), getContext().getResources().getString(R.string.rsa_encryput_error));
            loadingDialog.dismiss();
            return;
        }
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("sign", RsaStr);
        //*******
        addSubscription(ApiClient.toV2Login(paramMap).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                UserInfoDataEntity.Result loginBean = new Gson().fromJson(getV2String(s), UserInfoDataEntity.Result.class);
                AppInfStore.saveUserId(getContext(), loginBean.userId);
                AppInfStore.saveUserToken(getContext(), BStrUtils.decodeSimpleEncrypt(loginBean.token));
                AppInfStore.saveIsLogin(getContext(), true);

                if (loginBean.userInfo != null) {
                    SPreference.saveUserInfoData(getContext(), new Gson().toJson(loginBean.userInfo));
                    AppInfStore.saveUserAccount(getContext(), un);
                }
                loadingDialog.setResult(true, getContext().getString(R.string.la_login_succ_str), 1000, () -> {
                    Router.build(RouteConfig.GOTOCMAINHONE).go(getContext());
                    getView().toFinish();
                });
            }

            @Override
            protected void onRxError(Throwable error) {
                loadingDialog.setResult(false, getContext().getString(R.string.la_getinfo_error_str), 1000);
            }
        }));
    }

    @Override
    public void toGetPublicKey() {
        addSubscription(ApiClient.getLoginPublic().subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                StrResult result = new Gson().fromJson(s, StrResult.class);
                getView().publicKeySuccess(result.result);
            }

            @Override
            protected void onRxError(Throwable error) {
                LogUtils.Log("user", error.toString());
            }
        }));
    }


    @Override
    public void detachView() {
        super.detachView();
        context = null;
    }
}
