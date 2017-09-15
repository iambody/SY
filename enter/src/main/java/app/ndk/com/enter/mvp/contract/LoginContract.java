package app.ndk.com.enter.mvp.contract;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.view.BaseView;
import com.cgbsoft.lib.widget.CustomDialog;
import com.cgbsoft.lib.widget.WeiChatLoginDialog;
import com.cgbsoft.lib.widget.dialog.LoadingDialog;


public interface LoginContract {

    interface Presenter extends BasePresenter {

        /**
         * 登录接口
         *
         * @param un   用户名
         * @param pwd  密码
         * @param isWx 是否微信登录
         */
        void toNormalLogin(@NonNull LoadingDialog loadingDialog, String un, String pwd, String publicKey, boolean isWx);

        /**
         * 微信登录
         */
        void toWxLogin(@NonNull LoadingDialog loadingDialog, WeiChatLoginDialog.Builder builder, String unionid, String sex, String nickName, String headimgurl, String openid, String toWxLogin);

        /**
         * 微信登录
         *
         * @param loadingDialog
         * @param unionid
         * @param sex
         * @param nickName
         * @param headimgurl
         */
        void toDialogWxLogin(@NonNull LoadingDialog loadingDialog, String unionid, String sex, String nickName, String headimgurl, String openid, String toWxLogin);


        /**
         * 游客登录接口
         */
        void invisterLogin(Context context);

        /**
         * 获取登录公钥
         */
        void toGetPublicKey();

        void getNavigation();

//        /**
//         * 获取融云Token id
//         * @param userId
//         */
//        void getRongToken(String userId);
//
//        /**
//         * 初始化融云连接
//         * @param RongToken
//         */
//        void initRongConnect(String RongToken);
    }

    interface View extends BaseView {
        void loginSuccess();

        void loginFail();

        void toBindActivity();

        void invisterloginSuccess();

        void invisterloginFail();

        void publicKeySuccess(String str);


    }
}
