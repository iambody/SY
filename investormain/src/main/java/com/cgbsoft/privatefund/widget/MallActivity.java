package com.cgbsoft.privatefund.widget;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.base.webview.BaseWebview;
import com.cgbsoft.lib.base.webview.CwebNetConfig;
import com.cgbsoft.privatefund.R;
import com.chenenyu.router.annotation.Route;

import butterknife.BindView;
@Route("investornmain_mallactivity")
public class MallActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener {

//    @BindView(R.id.toolbar)
//    protected Toolbar toolbar;

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.title_mid)
    protected TextView titleMid;

    @BindView(R.id.webview)
    protected BaseWebview mWebview;

    private View myView = null;
    private WebChromeClient.CustomViewCallback myCallback = null;

    @Override
    protected int layoutID() {
        return R.layout.activity_mall;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        String url = getIntent().getStringExtra("url");
        String title = getIntent().getStringExtra("title");
        titleMid.setText(title);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
//        toolbar.setNavigationOnClickListener(v -> finish());
//        toolbar.setOnMenuItemClickListener(this);
        url = CwebNetConfig.baseParentUrl+ "/" + url;
        mWebview.loadUrl(url);
    }

    @Override
    protected void data() {
//        mWebview.setWebChromeClient(new WebChromeClient() {
//            @Override
//            public void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback) {
//                if (myCallback != null) {
//                    myCallback.onCustomViewHidden();
//                    myCallback = null;
//                    return;
//                }
//                ViewGroup parent = (ViewGroup) mWebview.getParent();
//                String s = parent.getClass().getName();
//                parent.removeView(mWebview);
//                parent.addView(view);
//                myView = view;
//                myCallback = callback;
//            }
//
//            @Override
//            public void onHideCustomView() {
//                if (myView != null) {
//                    if (myCallback != null) {
//                        myCallback.onCustomViewHidden();
//                        myCallback = null ;
//                    }
//                    ViewGroup parent = (ViewGroup) myView.getParent();
//                    parent.removeView(myView);
//                    parent.addView(mWebview);
//                    myView = null;
//                }
//            }
//        });
    }

    @Override
    protected BasePresenterImpl createPresenter() {
        return null;
    }

//    public void onEventMainThread(MyAddress myAddress){
//        mWebview.loadUrl("javaScript:products.setAddress('" + myAddress.getId() + "','" + myAddress.getName() + "','" + myAddress.getPhone() + "','" + myAddress.getAddress() + "')",true);
//    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            mWebview.getClass().getMethod("onPause").invoke(mWebview,(Object[])null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            mWebview.getClass().getMethod("onResume").invoke(mWebview, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }
}
