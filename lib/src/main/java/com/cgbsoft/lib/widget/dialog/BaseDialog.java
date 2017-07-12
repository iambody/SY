package com.cgbsoft.lib.widget.dialog;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.cgbsoft.lib.R;


public class BaseDialog extends Dialog {
	public interface OnCustomDialogListener {
		public void back(String name);
	}

	public BaseDialog(Context context, boolean cancelable,
					  OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	public BaseDialog(Context context, int theme) {
		super(context,theme);
	}

	public BaseDialog(Context context) {
		this(context, R.style.base_dialog_style);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		super.onCreate(savedInstanceState);

	}
	/**
	 * 当dialog现实时候开启几个按钮的动画set
	 *
	 * @param VV
	 */
	private void SetAnimation(View VV) {
		AnimatorSet animationSet = new AnimatorSet();
		ObjectAnimator Translate = ObjectAnimator.ofFloat(VV, "translationY", 1000f, 0f);
		ObjectAnimator Alpha = ObjectAnimator.ofFloat(VV, "alpha", 0f, 0.4f, 06f, 0.7f, 0.9f, 1f);
		Alpha.setDuration(1000);
		animationSet.playSequentially(Translate);
		animationSet.setDuration(1000);
		animationSet.start();
	}


	/**
	 * 当关闭时候开始消失动画的set
	 *
	 * @param VV
	 */
	private void SetDisissAnimation(View VV) {
		AnimatorSet animationSet = new AnimatorSet();
		ObjectAnimator Translate = ObjectAnimator.ofFloat(VV, "translationY", 1000f, 0f);
		ObjectAnimator Alpha = ObjectAnimator.ofFloat(VV, "alpha", 0f, 0.4f, 06f, 0.7f, 0.9f, 1f);
		Alpha.setDuration(1000);
		animationSet.playSequentially(Translate);
		animationSet.setDuration(1000);
		animationSet.start();
	}

}