package com.cgbsoft.privatefund.mvp.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.utils.imgNetLoad.Imageload;
import com.cgbsoft.lib.utils.net.NetConfig;
import com.cgbsoft.lib.utils.tools.MyBitmapUtils;
import com.cgbsoft.lib.utils.tools.ThreadUtils;
import com.cgbsoft.lib.widget.GestureImageView;
import com.cgbsoft.privatefund.R;
import com.chenenyu.router.annotation.Route;

import butterknife.BindView;

/**
 * 放大照片
 *
 * @author chenlong
 */
@Route("investornmain_smoothimageactivity")
public class SmoothImageActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener {

	@BindView(R.id.show_image)
	GestureImageView imageView;

	@BindView(R.id.toolbar)
	protected Toolbar toolbar;

	@BindView(R.id.title_mid)
	protected TextView titleMid;

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
		toolbar.setTitle("");
		titleMid.setText("图片预览");
		setSupportActionBar(toolbar);
		toolbar.setOnMenuItemClickListener(this);
		toolbar.setNavigationIcon(com.cgbsoft.lib.R.drawable.ic_back_black_24dp);
		toolbar.setNavigationOnClickListener(v -> finish());
		initImage();
	}

	@Override
	protected BasePresenterImpl createPresenter() {
		return null;
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
