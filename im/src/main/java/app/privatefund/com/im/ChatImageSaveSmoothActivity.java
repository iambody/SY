//package app.privatefund.com.im;
//
//import android.graphics.Bitmap;
//import android.graphics.drawable.Drawable;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
//import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
//
//import java.io.File;
//
///**
// * 聊天图片放大保存
// *
// * @author chenlong
// */
//public class ChatImageSaveSmoothActivity extends BaseActivity {
//	public static final String IMAGE_SAVE_PATH_LOCAL = "relative_asset_local";
//	public static final String IMAGE_THREM_LOCAL = "image_thmb_local";
//	private LinearLayout saveLinearLayout;
//	private TextView tx_save;
//	private TextView tx_cancel;
//	private String thumbUrl;
//	private boolean downloadFinished;
//	private Bitmap saveBitmap;
//	private LinearLayout progressLinearlayout;
//	@Override
//	protected void onCreate(Bundle arg0) {
//		super.onCreate(arg0);
//		setContentView(R.layout.activity_smooth_image);
//		saveLinearLayout = (LinearLayout)findViewById(R.id.show_save_page);
//		tx_save = (TextView)findViewById(R.id.saveImage);
//		tx_cancel = (TextView)findViewById(R.id.cancel);
//		progressLinearlayout = (LinearLayout) findViewById(R.id.progress_ll);
//		saveLinearLayout = (LinearLayout)findViewById(R.id.show_save_page);
//		final String localUrl  = getIntent().getStringExtra(IMAGE_SAVE_PATH_LOCAL);
//		final ImageView imageView = (ImageView) findViewById(R.id.show_image);
//		imageView.setImageResource(R.drawable.background_white);
//		showTileLeft();
//		showTileMid("图片预览");
//		titleRight.setVisibility(View.GONE);
//		titleLeft.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				finish();
//			}
//		});
//		imageView.setOnLongClickListener(new View.OnLongClickListener() {
//			@Override
//			public boolean onLongClick(View v) {
//				saveLinearLayout.setVisibility(View.VISIBLE);
//				return true;
//			}
//		});
//		saveLinearLayout.setOnLongClickListener(new View.OnLongClickListener() {
//			@Override
//			public boolean onLongClick(View v) {
//				saveLinearLayout.setVisibility(View.GONE);
//				return true;
//			}
//		});
//		tx_save.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if (!downloadFinished || saveBitmap == null) {
//					MToast.makeText(ChatImageSaveSmoothActivity.this, "图片还没有下载完成", Toast.LENGTH_SHORT).show();
//					return;
//				}
//				tx_save.setEnabled(false);
//				saveLinearLayout.setVisibility(View.GONE);
//
//				String filePath = "simuyun" + File.separator + "chatimage";
//				String fileName = System.currentTimeMillis() + ".png";
//				SDCardUtil.saveFileToSDCardCustomDir(saveBitmap, filePath, fileName);
//				File file = new File(SDCardUtil.getSDCardBaseDir() + File.separator + filePath, fileName);
//				NavigationUtils.sendBroadcastToAlrm(ChatImageSaveSmoothActivity.this, file);
//				MToast.makeText(ChatImageSaveSmoothActivity.this, "图片已成功保存到相册", Toast.LENGTH_SHORT).show();
//			}
//		});
//		tx_cancel.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				saveLinearLayout.setVisibility(View.GONE);
//			}
//		});
//		initImage(imageView, localUrl);
//	}
//
//	@Override
//	protected int layoutID() {
//		return 0;
//	}
//
//	@Override
//	protected void init(Bundle savedInstanceState) {
//
//	}
//
//	@Override
//	protected BasePresenterImpl createPresenter() {
//		return null;
//	}
//
//	private BitmapLoadCallBack bitmapLoadCallBack = new BitmapLoadCallBack() {
//		@Override
//		public void onLoadCompleted(View view, String s, Bitmap bitmap, BitmapDisplayConfig bitmapDisplayConfig, BitmapLoadFrom bitmapLoadFrom) {
//			downloadFinished = true;
//			saveBitmap = bitmap;
//			((ImageView)view).setImageBitmap(bitmap);
//			progressLinearlayout.setVisibility(View.GONE);
//		}
//
//		@Override
//		public void onLoadFailed(View view, String s, Drawable drawable) {
//			downloadFinished = false;
//			saveBitmap = null;
//			progressLinearlayout.setVisibility(View.GONE);
//			MToast.makeText(ChatImageSaveSmoothActivity.this, s, Toast.LENGTH_SHORT).show();
//		}
//	};
//
//	private void initImage(final ImageView imageView, final String imagePath) {
//		if (TextUtils.isEmpty(imagePath)) {
//			return;
//		}
//		final BitmapUtils bu = new BitmapUtils(this);
//		if (!TextUtils.isEmpty(imagePath) && !imagePath.startsWith("http") && imagePath.contains(Contant.UPLOAD_CERTIFICATE_TYPE)) {
//			final String urls = Domain.urlStr + imagePath;
//			progressLinearlayout.setVisibility(View.VISIBLE);
//			ThreadUtils.runOnMainThreadDelay(new Runnable() {
//				@Override
//				public void run() {
//					bu.display(imageView, urls, bitmapLoadCallBack);
//				}
//			}, 500);
//		} else if (imagePath.startsWith("http")) {
//			progressLinearlayout.setVisibility(View.VISIBLE);
//			ThreadUtils.runOnMainThreadDelay(new Runnable() {
//				@Override
//				public void run() {
//					bu.display(imageView, imagePath, bitmapLoadCallBack);
//				}
//			}, 500);
//		} else {
//			downloadFinished = true;
//			saveBitmap = MyBitmapUtils.getLoacalBitmap(imagePath);
//			imageView.setImageBitmap(saveBitmap);
//		}
//	}
//
////	@Override
////	protected void onDestroy() {
////		super.onDestroy();
////		if (saveBitmap != null) {
////			saveBitmap.recycle();
////			saveBitmap = null;
////		}
////	}
//}
