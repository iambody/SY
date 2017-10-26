package com.cgbsoft.privatefund.mvp.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.utils.imgNetLoad.Imageload;
import com.cgbsoft.lib.utils.net.NetConfig;
import com.cgbsoft.lib.utils.tools.MyBitmapUtils;
import com.cgbsoft.lib.utils.tools.ThreadUtils;
import com.cgbsoft.lib.widget.GestureImageView;
import com.cgbsoft.privatefund.R;
import com.chenenyu.router.annotation.Route;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 放大照片
 *
 * @author chenlong
 */
@Route(RouteConfig.SMOOT_IMAGE_ACTIVITY)
public class SmoothImageActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener {

	@BindView(R.id.show_image)
	GestureImageView imageView;

//	@BindView(R.id.toolbar)
//	protected Toolbar toolbar;

	@BindView(R.id.title_mid)
	protected TextView titleMid;

	@BindView(R.id.title_right_text)
	protected TextView rightTextView;

	@BindView(R.id.iv_back)
	ImageView ivBack;
	private String localUrl;
	private MenuItem menuItem;

	@Override
	protected int layoutID() {
		return R.layout.activity_smooth_image;
	}

	@Override
	protected void init(Bundle savedInstanceState) {
		localUrl = getIntent().getStringExtra(IMAGE_SAVE_PATH_LOCAL);
		imageView.setImageResource(R.drawable.bg_white);
		titleMid.setText("图片预览");
		rightTextView.setVisibility(getIntent().getBooleanExtra(IMAGE_RIGHT_DELETE, false) ? View.VISIBLE : View.GONE);
		Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_local_video_delete);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		rightTextView.setCompoundDrawables(drawable, null, null, null);
//		setSupportActionBar(toolbar);
//		toolbar.setOnMenuItemClickListener(this);
//		toolbar.setNavigationIcon(com.cgbsoft.lib.R.drawable.ic_back_black_24dp);
//		toolbar.setNavigationOnClickListener(v -> finish());
		ivBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		initImage();
	}

	@Override
	protected BasePresenterImpl createPresenter() {
		return null;
	}

	@OnClick(R.id.title_right_text)
	public void onClickRightText() {
		Intent intent = new Intent();
		intent.putExtra("deletPath", getIntent().getStringExtra(IMAGE_SAVE_PATH_LOCAL));
		setResult(Activity.RESULT_OK, intent);
		finish();
	}

	private void initImage() {
		if (TextUtils.isEmpty(localUrl)) {
			return;
		}
		if (!TextUtils.isEmpty(localUrl) && !localUrl.startsWith("http") && localUrl.contains(Constant.UPLOAD_CERTIFICATE_TYPE)) {
			final String urls = NetConfig.UPLOAD_FILE + localUrl;
			System.out.println("-------imagePath1=" + localUrl);
			ThreadUtils.runOnMainThreadDelay(() -> Imageload.display(SmoothImageActivity.this, urls, imageView), 500);
		} else if (localUrl.startsWith("http")) {
			ThreadUtils.runOnMainThreadDelay(() -> Imageload.display(SmoothImageActivity.this, localUrl, imageView), 500);
		} else {
			imageView.setImageBitmap(MyBitmapUtils.getLoacalBitmap(localUrl));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (getIntent().getBooleanExtra(IMAGE_RIGHT_DELETE, false)) {
			getMenuInflater().inflate(R.menu.page_menu, menu);
            menuItem = menu.findItem(R.id.firstBtn);
            MenuItem secItem = menu.findItem(R.id.secondBtn);
			menuItem.setIcon(R.drawable.ic_local_video_delete);
            secItem.setVisible(false);
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		if (item.getItemId() == R.id.firstBtn) {
			Intent intent = new Intent();
			intent.putExtra("deletPath", getIntent().getStringExtra(IMAGE_SAVE_PATH_LOCAL));
			setResult(Activity.RESULT_OK, intent);
			finish();
		}
		return false;
	}
}
