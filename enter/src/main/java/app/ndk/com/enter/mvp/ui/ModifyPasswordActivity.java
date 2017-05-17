package app.ndk.com.enter.mvp.ui;

import android.os.Bundle;

import com.cgbsoft.lib.base.webview.BaseWebViewActivity;
import com.cgbsoft.lib.base.webview.WebViewConstant;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.widget.dialog.LoadingDialog;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import app.ndk.com.enter.R;
import app.ndk.com.enter.mvp.contract.ModifyPasswordContract;
import app.ndk.com.enter.mvp.presenter.ModifyPasswordPresenter;

/**
 * @author chenlong
 *         修改密码
 */
public class ModifyPasswordActivity extends BaseWebViewActivity<ModifyPasswordPresenter> implements ModifyPasswordContract.View {

    private LoadingDialog mLoadingDialog;

    @Override
    protected ModifyPasswordPresenter createPresenter() {
        return new ModifyPasswordPresenter(this, this);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mLoadingDialog = LoadingDialog.getLoadingDialog(this, getString(R.string.sending_str), false, false);
        mWebview.setClick(result -> {
            if (result.startsWith(WebViewConstant.AppCallBack.MODIFY_PASSWORD)) {// 修改密码
                String params = null;
                try {
                    params = URLDecoder.decode(result, "utf-8");
                    String[] values = params.split(":");
                    ((ModifyPasswordPresenter) getPresenter()).modifyPassword(mLoadingDialog, SPreference.getUserInfoData(this).userName, values[2], values[3]);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void regSucc() {
    }

    @Override
    public void regFail() {
        mWebview.loadUrl("javascript:setData(\"修改失败\")");
    }
}
