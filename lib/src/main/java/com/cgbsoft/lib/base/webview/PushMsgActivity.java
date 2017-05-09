package com.cgbsoft.lib.base.webview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cgbsoft.lib.R;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.utils.tools.ThreadUtils;
import com.cgbsoft.lib.widget.IOSDialog;

/**
 * 通用的WebView页面
 */
public class PushMsgActivity extends Activity {

	public static final int SAVE_REQUST = 300;
	public static final int RELATIVE_ASSERT = 301;
	public static final int ASSERT_PROVE = 302;
	public static final int BACK_RESULT_CODE = 401;
	public static final String SAVE_PARAM = "saveValue";
	public static final String BACK_PARAM = "backValue";
	String url = "";
	private String title;
	private RelativeLayout relativeLayout;
	private ImageView cloudImage;
	private boolean isLive;
	private boolean isLookZhiBao;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.acitivity_userinfo);
//		LogTest.Log("testpage","我执行的朴push新页面！！！！！！！！！！！！");
		bindViews();

		url = getIntent().getStringExtra(WebViewConstant.push_message_url);
		if(!url.contains("http")){
			url = BaseWebNetConfig.baseParentUrl + url;
		}
		if (!TextUtils.isEmpty(getIntent().getStringExtra(WebViewConstant.Jump_Info_KeyWord))) {
		  String keyWords = getIntent().getStringExtra(WebViewConstant.Jump_Info_KeyWord);
			if (url.contains("?")) {
				url += "&" + WebViewConstant.Jump_Info_KeyWord  + "=" + keyWords + "";
			} else  {
				url += "?" + WebViewConstant.Jump_Info_KeyWord  + "=" + keyWords + "";
			}
		}
		System.out.println("-------urls=" + url);

		title = getIntent().getStringExtra(WebViewConstant.push_message_title);
		if (getIntent().getBooleanExtra(WebViewConstant.PAGE_SHARE_WITH_EMAIL, false)) {
//			WebveiwUtil webveiwUtil = new WebveiwUtil(this);
//			webveiwUtil.setShareWithEmail(true);
//			mWebview.setWebwebUtil(webveiwUtil);
		}
		relativeLayout.setVisibility(getIntent().getBooleanExtra(WebViewConstant.PAGE_SHOW_TITLE, false) ? View.GONE : View.VISIBLE);
		if(TextUtils.isEmpty(title)){
			title = "";
		}

		if (getIntent().getBooleanExtra(WebViewConstant.RIGHT_SHARE, false)) {
			// showTileRight("分享");
//			Drawable drawable = getResources().getDrawable(R.drawable.fenxiang_share_nor);
//			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//			titleRight.setCompoundDrawables(drawable, null, null, null);
//			titleRight.setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					String javascript = "javascript:shareClick()";
//					mWebview.getWebView().loadUrl(javascript);
//				}
//			});
		}


	//	showTileMid(title);
		mWebview.loadUrls(url);

		final String value = getIntent().getStringExtra(WebViewConstant.push_message_value);
		if (!TextUtils.isEmpty(value) && getIntent().getBooleanExtra(WebViewConstant.PAGE_INIT, false)) {
			mWebview.postDelayed(new Runnable() {
				@Override
				public void run() {
					String javascript = "javascript:Tools.init('" + value + "')";
					mWebview.loadUrl(javascript);
				}
			}, 1000);
		}

		if (getIntent().getBooleanExtra(WebViewConstant.RIGHT_SAVE, false)) {
			//showTileRight("保存");
//			titleRight.setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					String jascript = "javascript:Tools.save()";
//					mWebview.loadUrl(jascript);
//				}
//			});
		}

		cloudImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				if (SPreference.getToCBean(PushMsgActivity.this) != null && TextUtils.isEmpty(SPreference.getToCBean(PushMsgActivity.this).getBandingAdviserId())) {
//					Intent intent = new Intent(PushMsgActivity.this, VisitNoBindActivity.class);
//					startActivity(intent);
//				} else {
//					if (isLive  && !isLookZhiBao) {
//						isLookZhiBao = true;
//						//joinLive();
//					} else {
//						Intent intent = new Intent(PushMsgActivity.this, CloudMenuActivity.class);
//						intent.putExtra(CloudMenuActivity.PARAM_PRODUCT, true);
//						PushMsgActivity.this.startActivity(intent);
//					}
//				}
			}
		});
		if ("风险评测".equals(title)) {
//			titleLeft.setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					backEvent();
//				}
//			});
		} else {
//			titleLeft.setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					if (getIntent().getBooleanExtra(Contant.PUSH_MESSAGE_COME_HERE, false)) {
//						NavigationUtils.startMessageList(context);
//					}
//					finish();
//				}
//			});
		}

		if (url.contains("/apptie/new_detail_tob.html")||url.contains("/discover/details.html")){
			getCoinTask();
		}
	}

	private void getCoinTask() {
//		DatabaseUtils databaseUtils = new DatabaseUtils(PushMsgActivity.this);
//		MyTaskBean myTaskBean = databaseUtils.getMyTask("查看资讯");
//		if (myTaskBean != null && myTaskBean.getState() == 0) {
//			getCoinTask(databaseUtils, myTaskBean);
//		}
	}

//	private void getCoinTask(final DatabaseUtils databaseUtils, final MyTaskBean myTaskBean) {
//		JSONObject js = new JSONObject();
//		try {
//			js.put("taskName", myTaskBean.getTaskName());
//			js.put("taskType", myTaskBean.getTaskType() + "");
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		new CoinTask(context).start(js.toString(), new HttpResponseListener() {
//			@Override
//			public void onResponse(JSONObject response) {
//				try {
//					String ratio = response.getString("ratio");
//					int coinRatioNum = response.getInt("coinRatioNum");
//					int coinNum = response.getInt("coinNum");
//					if (ratio.equals("1.0")) {
//						Toast.makeText(context, "完成【查看资讯】任务,获得" + coinRatioNum + "个云豆", Toast.LENGTH_SHORT).show();
//					} else {
//						Toast.makeText(context, "完成【查看资讯】任务,获得" + coinRatioNum + "（" + coinNum + " X " + ratio + "）个云豆", Toast.LENGTH_SHORT).show();
//					}
//					MApplication.getUser().setMyPoint(MApplication.getUser().getMyPoint() + coinRatioNum);
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//				myTaskBean.setState(2);
//				databaseUtils.updataMyTask(myTaskBean);
//				EventBus.getDefault().post(new RefreshUserinfo());
//			}
//
//			@Override
//			public void onErrorResponse(String error, int statueCode) {
//				try {
//					JSONObject js = new JSONObject(error);
//					String message = js.getString("message");
//					if (message.contains("已领取过云豆")) {
//						myTaskBean.setState(2);
//						databaseUtils.updataMyTask(myTaskBean);
//					}
////                    new MToast(context).show(message, 0);
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

//	public void onEventMainThread(WXCallBack callBack) {
//		if (callBack.getTag().equals(Contant.SHARE_POSTER)) {
//			mWebview.getWebView().loadUrl("javascript:shareActionSuc()");
//		}
//	}


//	public void onEventMainThread(LiveStatus liveStatus) {
//		isLive = liveStatus.isLive();
//		if (liveStatus.isLive()) {
//			isLookZhiBao = false;
//			cloudImage.setImageResource(R.drawable.yunjianzhibo);
//		} else {
//			cloudImage.setImageResource(R.drawable.yunjian_c);
//		}
//	}

//	public void onEventMainThread(RefrushHtmlPage refrushHtmlPage) {
//		System.out.println("--------refrushHtmlPage=" + refrushHtmlPage.getValue());
//		mWebview.getWebView().loadUrl("javascript:setGesture('" + refrushHtmlPage.getValue() + "')");
//	}

	@Override
	public void onBackPressed() {
		if (url.contains("rankList_share")) {
			System.out.println("--------back=1");
			mWebview.loadUrl("javascript:delectChart()");
		}

		if ("风险评测".equals(title)) {
			backEvent();
			return;
		}
//		if (getIntent().getBooleanExtra(Contant.PUSH_MESSAGE_COME_HERE, false)) {
//			NavigationUtils.startMessageList(context);
//		}

		if (url.contains("rankList_share")) {
			ThreadUtils.runOnMainThreadDelay(new Runnable() {
				@Override
				public void run() {
					PushMsgActivity.super.onBackPressed();
				}
			}, 1000);
		} else {
			super.onBackPressed();
		}
	}

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

	private void backEvent() {
		new IOSDialog(PushMsgActivity.this, "", getString(R.string.risk_comment_prmpt), "取消", "确定") {
			@Override
			public void left() {
				this.dismiss();
			}

			@Override
			public void right() {
				finish();
			}
		}.show();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if ("设置".equals(title) || url.contains("/calendar/index.html") || url.contains("invite_ordinary.html") || url.contains("set_det_gesture.html")) {
			 mWebview.loadUrl("javascript:refresh()");
		} else if (url.contains("apptie/detail.html")) {
			cloudImage.setVisibility(View.VISIBLE);
		}
		try {
			mWebview.getClass().getMethod("onResume").invoke(mWebview, (Object[]) null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == BACK_RESULT_CODE) { // 处理h5返回问题
			int index = data.getIntExtra(BACK_PARAM, -1);
			if (index != 0 && (url.contains("/apptie/detail.html") || url.contains("/calendar/index.html"))) { //
				return;
			}
			Intent intent = new Intent();
			intent.putExtra(BACK_PARAM, index);
			setResult(BACK_RESULT_CODE, intent);
			finish();
		} else if (requestCode == SAVE_REQUST ) {
			if (data == null) {
				return;
			}
			String value = data.getStringExtra(SAVE_PARAM);
			if (!TextUtils.isEmpty(value)) {
				String laun = "javascript:Tools.saveSuccess('" + value + "');";
				mWebview.loadUrl(laun);
			}
		} else if(requestCode == RELATIVE_ASSERT) {
			int status = Integer.valueOf(SPreference.getToCBean(PushMsgActivity.this).getStockAssetsStatus());
			String laun = "javascript:localUp(sUserAgg,'stockAssetsStatus'," + status + ",'toC')";
			mWebview.loadUrl(laun);
		} else if (requestCode == ASSERT_PROVE) {
			int status = Integer.valueOf(SPreference.getToCBean(PushMsgActivity.this).getStockAssetsStatus());
			String laun = "javascript:localUp(sUserAgg,'assetsCertificationStatus'," + status + ",'toC')";
			mWebview.loadUrl(laun);
		} else if (requestCode == Constant.REQUEST_IMAGE) {
			if (resultCode == RESULT_OK) {
//				List<String> mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
//				if (mSelectPath != null && mSelectPath.size() > 0) {
//					Intent intent = new Intent(this, ClipImageActivity.class);
//					intent.putExtra("mSelectPath", mSelectPath.get(0));
//					startActivity(intent);
//				}
			}
		}
	}

	private BaseWebview mWebview;

	private void bindViews() {
		mWebview = (BaseWebview) findViewById(R.id.webview);
		relativeLayout = (RelativeLayout) findViewById(R.id.title_title);
		cloudImage = (ImageView) findViewById(R.id.menu_cloud);
	}

	@Override
	protected void onDestroy() {
		mWebview.clearAnimation();
		mWebview.removeAllViews();
		mWebview.destroy();
		super.onDestroy();
	}

//	public void onEventMainThread(EventBusUpdateHeadImage event) {
//		String laun = "javascript:setHeadImage('" + event.getRemoteAddress() + "');";
//		mWebview.loadUrl(laun);
//	}
}
