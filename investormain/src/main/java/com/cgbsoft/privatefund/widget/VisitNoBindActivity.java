//package com.cgbsoft.privatefund.widget;
//
//import android.os.Bundle;
//import android.support.v7.widget.Toolbar;
//import android.widget.TextView;
//
//import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
//import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
//import com.cgbsoft.lib.base.webview.BaseWebview;
//import com.cgbsoft.lib.base.webview.CwebNetConfig;
//import com.cgbsoft.privatefund.R;
//
//import butterknife.BindView;
//
///**
// * 为绑定用户信息填写页面
// * @author chenlong
// */
//public class VisitNoBindActivity extends BaseActivity {
//
//	@BindView(R.id.toolbar)
//	protected Toolbar toolbar;
//
//	@BindView(R.id.title_mid)
//	protected TextView titleMid;
//
//	@BindView(R.id.webview)
//	protected BaseWebview mWebview;
//
//	@Override
//	protected int layoutID() {
//		return R.layout.acitivity_userinfo;
//	}
//
//	@Override
//	protected void init(Bundle savedInstanceState) {
//		setSupportActionBar(toolbar);
//		toolbar.setNavigationIcon(R.drawable.ic_back_black_24dp);
//		toolbar.setNavigationOnClickListener(v -> finish());
//
//		titleMid.setText("填写信息");
//		String url = CwebNetConfig.noBindUserInfo;
//		mWebview.loadUrls(url);
//	}
//
//	@Override
//	protected BasePresenterImpl createPresenter() {
//		return null;
//	}
//}
