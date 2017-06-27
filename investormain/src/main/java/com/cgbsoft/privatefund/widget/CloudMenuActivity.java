//package com.cgbsoft.privatefund.widget;
//
//import android.app.Activity;
//import android.graphics.drawable.Drawable;
//import android.os.Bundle;
//import android.os.Handler;
//import android.support.v4.content.ContextCompat;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.cgbsoft.lib.AppManager;
//import com.cgbsoft.lib.InvestorAppli;
//import com.cgbsoft.lib.base.webview.CwebNetConfig;
//import com.cgbsoft.lib.base.webview.WebViewConstant;
//import com.cgbsoft.lib.contant.RouteConfig;
//import com.cgbsoft.lib.utils.constant.RxConstant;
//import com.cgbsoft.lib.utils.rxjava.RxBus;
//import com.cgbsoft.lib.utils.tools.DataStatistApiParam;
//import com.cgbsoft.lib.utils.tools.DimensionPixelUtil;
//import com.cgbsoft.lib.utils.tools.NavigationUtils;
//import com.cgbsoft.privatefund.R;
//import com.chenenyu.router.annotation.Route;
//import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
//import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
//import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
//
//import java.util.HashMap;
//
//import io.rong.imkit.RongIM;
//import io.rong.imlib.model.Conversation;
//
///**
// * 云键菜单
// * @author chenlong
// */
//@Route(RouteConfig.GOTO_CLOUD_MENU_ACTIVITY)
//public class CloudMenuActivity extends Activity {
//
//	private FloatingActionMenu floatingActionMenu;
//	public static final String PARAM_PRODUCT = "product_detail";
//	public static final String HAS_LIVE_STATUS = "hasLive";
//	private boolean hasLive;
//
//	@Override
//	protected void onCreate(Bundle arg0) {
//		super.onCreate(arg0);
//		setContentView(R.layout.activity_cloud_menu);
//		View view = findViewById(R.id.root_window);
//		view.setOnClickListener(v -> {
//            if (floatingActionMenu != null) {
//                floatingActionMenu.close(true);
//                CloudMenuActivity.this.finish();
//                CloudMenuActivity.this.overridePendingTransition(R.anim.home_fade_in, R.anim.home_fade_out);
//            }
//        });
//		getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//		floatingActionMenu = initSatelliteMenu(this, getLayoutInflater(), getIntent().getBooleanExtra(PARAM_PRODUCT, false));
//		hasLive = getIntent().getBooleanExtra(HAS_LIVE_STATUS, false);
//	}
//
//	@Override
//	protected void onResume() {
//		super.onResume();
//		new Handler().postDelayed(() -> {
//            if (!floatingActionMenu.isOpen())
//                floatingActionMenu.open(true);
//        }, 100);
//	}
//
//	private FloatingActionMenu initSatelliteMenu(final Activity activity, LayoutInflater layoutInflater, boolean isProductDetail) {
//		ImageView icon = new ImageView(activity); // Create an icon
//		icon.setImageResource(R.drawable.ic_guanbi_nor);
//		FloatingActionButton.LayoutParams layoutParams = new FloatingActionButton.LayoutParams(180, 180);
//		//FloatingActionButton.LayoutParams layoutParams = new FloatingActionButton.LayoutParams(200, 200);
//		if (isProductDetail) {
//			layoutParams.setMargins(0, 0, DimensionPixelUtil.dp2px(activity, 34), DimensionPixelUtil.dp2px(activity, 79));
//			layoutParams.setLayoutDirection(Gravity.BOTTOM | Gravity.RIGHT);
//		} else {
//			layoutParams.setLayoutDirection(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
//			layoutParams.setMargins(0, 0, 0, 5);
//		}
//		// setBackgroundDrawable(ContextCompat.getDrawable(activity, R.drawable.bg_transparent))
//
//		FloatingActionButton.LayoutParams contentParams = new FloatingActionButton.LayoutParams(180, 180);
//
//		final FloatingActionButton actionButton = new FloatingActionButton.Builder(activity).setLayoutParams(layoutParams).
//				setContentView(icon, contentParams).setPosition(isProductDetail ? FloatingActionButton.POSITION_BOTTOM_RIGHT : FloatingActionButton.POSITION_BOTTOM_CENTER).build();
//
//
//		TextView one = (TextView)layoutInflater.inflate(R.layout.item_textview_drawable, null);
//		one.setText(R.string.vbnb_call_str);
//		showCompoundDrawable(one, ContextCompat.getDrawable(activity, R.drawable.selector_bottom_call));
//		View first = buildSubButton(activity, one, isProductDetail);
//		first.setOnClickListener(v -> {
//            NavigationUtils.startDialgTelephone(activity, AppManager.getUserInfo(CloudMenuActivity.this).getAdviserPhone());
//            CloudMenuActivity.this.finish();
//            CloudMenuActivity.this.overridePendingTransition(R.anim.home_fade_in, R.anim.home_fade_out);
//            DataStatistApiParam.onStatisToCMenuCallCustom();
//
//        });
//
//		TextView two = (TextView)layoutInflater.inflate(R.layout.item_textview_drawable, null);
//		two.setText(R.string.vbnb_meet_str);
//		showCompoundDrawable(two, ContextCompat.getDrawable(activity, R.drawable.selector_bottom_meet));
//		View second = buildSubButton(activity, two, isProductDetail);
//		second.setOnClickListener(v -> {
//            RongIM.getInstance().startConversation(CloudMenuActivity.this, Conversation.ConversationType.PRIVATE, AppManager.getUserInfo(CloudMenuActivity.this).getToC().getBandingAdviserId(), AppManager.getUserInfo(CloudMenuActivity.this).getAdviserRealName());
//            CloudMenuActivity.this.finish();
//            CloudMenuActivity.this.overridePendingTransition(R.anim.home_fade_in, R.anim.home_fade_out);
//            DataStatistApiParam.onStatisToCMenuCallDuihua();
//        });
//
//		TextView three = (TextView)layoutInflater.inflate(R.layout.item_textview_drawable, null);
//		three.setText(!hasLive ? R.string.vbnb_tougu_dangan : R.string.vbnb_live_str);
//		showCompoundDrawable(three, ContextCompat.getDrawable(activity, !hasLive ? R.drawable.select_mine_tougu : R.drawable.selector_bottom_live));
//		View third = buildSubButton(activity, three, isProductDetail);
//		third.setOnClickListener(v -> {
//            if (((InvestorAppli)InvestorAppli.getContext()).isTouGuOnline()) {
//                HashMap hashMap = new HashMap();
//                hashMap.put(WebViewConstant.push_message_url, CwebNetConfig.mineTouGu);
//                hashMap.put(WebViewConstant.push_message_title, "我的投顾");
//                NavigationUtils.startActivityByRouter(CloudMenuActivity.this, RouteConfig.GOTO_BASE_WEBVIEW, hashMap);
//                CloudMenuActivity.this.finish();
//			} else {
//                RxBus.get().post(RxConstant.Open_PAGE_LIVE_OBSERVABLE, true);
////					Intent intent2 = new Intent(CloudMenuActivity.this, AVLiveListActivity.class);
////					intent2.putExtra(AVLiveListActivity.ZHIBO_PARAMS, true);
////					startActivity(intent2);
//                CloudMenuActivity.this.finish();
//                CloudMenuActivity.this.overridePendingTransition(R.anim.home_fade_in, R.anim.home_fade_out);
//			}
//			DataStatistApiParam.onStatisToCMenuZhibo();
//        });
//
//		TextView four = (TextView)layoutInflater.inflate(R.layout.item_textview_drawable, null);
//		four.setText(R.string.vbnb_sms_str);
//		showCompoundDrawable(four, ContextCompat.getDrawable(activity, R.drawable.selector_bottom_sms));
//		View fourth = buildSubButton(activity, four, isProductDetail);
//		fourth.setOnClickListener(v -> {
//            NavigationUtils.startDialogSendMessage(activity, AppManager.getUserInfo(CloudMenuActivity.this).getAdviserPhone());
//            CloudMenuActivity.this.finish();
//            CloudMenuActivity.this.overridePendingTransition(R.anim.home_fade_in, R.anim.home_fade_out);
//            DataStatistApiParam.onStatisToCMenuMessage();
//        });
//
//		TextView five = (TextView)layoutInflater.inflate(R.layout.item_textview_drawable, null);
//		five.setText(R.string.vbnb_cs_str);
//		showCompoundDrawable(five, ContextCompat.getDrawable(activity, R.drawable.selector_bottom_cs));
//		View fiveth = buildSubButton(activity, five, isProductDetail);
//		fiveth.setOnClickListener(v -> {
//            RongIM.getInstance().startPrivateChat(CloudMenuActivity.this, "dd0cc61140504258ab474b8f0a38bb56", "平台客服");
//            CloudMenuActivity.this.finish();
//            CloudMenuActivity.this.overridePendingTransition(R.anim.home_fade_in, R.anim.home_fade_out);
//            DataStatistApiParam.onStatisToCMenuKefu();
//        });
//
//		final FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(activity)
//				.addSubActionView(first)
//				.addSubActionView(second)
//				.addSubActionView(third)
//				.addSubActionView(fourth)
//				.addSubActionView(fiveth)
//				.setRadius(isProductDetail ? DimensionPixelUtil.dp2px(CloudMenuActivity.this, 130) : 415).setStartAngle(isProductDetail ? 150 : 200).setEndAngle(isProductDetail ? 280 : 340)
//				.attachTo(actionButton)
//				.build();
//		actionMenu.setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {
//			@Override
//			public void onMenuOpened(FloatingActionMenu floatingActionMenu) {}
//
//			@Override
//			public void onMenuClosed(FloatingActionMenu floatingActionMenu) {
//				new Handler().postDelayed(() -> {
//                    actionMenu.close(true);
//                    CloudMenuActivity.this.finish();
//                    CloudMenuActivity.this.overridePendingTransition(R.anim.home_fade_in, R.anim.home_fade_out);
//                }, 100);
//			}
//		});
//
//		actionButton.setOnClickListener(v -> {
//            if (actionMenu.isOpen()) {
//                actionMenu.close(true);
//                CloudMenuActivity.this.finish();
//                CloudMenuActivity.this.overridePendingTransition(R.anim.home_fade_in, R.anim.home_fade_out);
//            }
//        });
//		return actionMenu;
//	}
//
//	private static View buildSubButton (Activity activity, TextView textView, boolean isProductDetail) {
//		FrameLayout.LayoutParams blueContentParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
//				FrameLayout.LayoutParams.MATCH_PARENT);
//		SubActionButton.Builder itemBuilder = new SubActionButton.Builder(activity);
//		itemBuilder.setBackgroundDrawable(ContextCompat.getDrawable(activity, R.drawable.bg_transparent));
////		int subActionButtonSizeWidth = 230;
////		int subActionButtonSizeHeight = 230;
//		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
//		itemBuilder.setLayoutParams(params);
//		return itemBuilder.setContentView(textView, blueContentParams).build();
//	}
//
//	private static void showCompoundDrawable(TextView textView, Drawable drawable) {
//		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//		textView.setCompoundDrawables(null, drawable, null, null);
//	}
//}
