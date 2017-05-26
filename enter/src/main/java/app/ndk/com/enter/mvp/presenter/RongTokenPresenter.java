//package app.ndk.com.enter.mvp.presenter;
//
//import android.content.Context;
//import android.support.annotation.NonNull;
//import android.text.TextUtils;
//import android.util.Log;
//
//import com.cgbsoft.lib.AppInfStore;
//import com.cgbsoft.lib.AppManager;
//import com.cgbsoft.lib.BaseApplication;
//import com.cgbsoft.lib.base.model.RongTokenEntity;
//import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
//import com.cgbsoft.lib.utils.cache.SPreference;
//import com.cgbsoft.lib.utils.net.ApiClient;
//import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
//import com.cgbsoft.lib.widget.CustomDialog;
//import com.cgbsoft.lib.widget.dialog.LoadingDialog;
//import com.google.gson.Gson;
//
//import java.util.List;
//
//import app.ndk.com.enter.mvp.contract.LoginContract;
//import app.privatefund.com.im.listener.MyReceiveMessageListener;
//import io.rong.imkit.RongIM;
//import io.rong.imlib.RongIMClient;
//import io.rong.imlib.model.Conversation;
//
///**
// * @author chenlong
// */
//public class RongTokenPresenter extends BasePresenterImpl<LoginContract.View> implements LoginContract.Presenter {
//
//    public RongTokenPresenter(@NonNull Context context, @NonNull LoginContract.View view) {
//        super(context, view);
//    }
//
//
//    @Override
//    public void toNormalLogin(@NonNull LoadingDialog loadingDialog, String un, String pwd, boolean isWx) {
//
//    }
//
//    @Override
//    public void toWxLogin(@NonNull LoadingDialog loadingDialog, CustomDialog.Builder builder, String unionid, String sex, String nickName, String headimgurl) {
//
//    }
//
//    @Override
//    public void toDialogWxLogin(@NonNull LoadingDialog loadingDialog, String unionid, String sex, String nickName, String headimgurl) {
//
//    }
//}
