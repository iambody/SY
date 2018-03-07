package com.cgbsoft.lib.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;


/**
 * Created by wangpeng on 18-1-29.
 * <p>
 *  自动平均文字分布的TextView,例如：
 *  “四个字时”
 *  “四   字”
 */
public class AverageTextView extends TextView {
	private Context context;
	private int viewWidth;
	private int viewHight;
	private int oneWidth;
	private int oneHight;
	private int num;
	private TextPaint p;
	private String[] texts;

	public AverageTextView(Context context) {
		super(context);
		this.context = context;
	}

	public AverageTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}
	private void initData() {
		p = getPaint();
		this.setPadding(0, 0, 0, 0);
		viewWidth = this.getWidth();
		viewHight = this.getHeight();

		this.oneWidth  = ce("汉").width();
		this.oneHight = ce("汉").height();
		this.setText("");
	}

	@Override
	public void setText(CharSequence text, BufferType type) {
		if (TextUtils.isEmpty(text)) return;

		this.num = text.length();
		String[] strs = new String[num];
		for (int i = 0; i < num; i++) {
			strs[i] = String.valueOf(text.charAt(i));
		}
		this.texts = strs;
		super.setText(text,type);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		initData();
		drawText(canvas);
	//	super.onDraw(canvas);
	}

	private void drawText(Canvas canvas) {
		if (num == 0)
			return;
		int lineWidth = viewWidth / num;
		int drawY = (viewHight - oneHight) / 2 + oneHight;
		for (int i = 0; i < num; i++) {
			if (i == num - 1) {// 最后一个
				canvas.drawText(texts[i],
						viewWidth - oneWidth, drawY, p);
			} else if (i != 0 && i != num - 1) {//
				canvas.drawText(texts[i], i * lineWidth + lineWidth / 2
						- oneWidth / 2, drawY, p);
			} else {// 第一个
				canvas.drawText(texts[i], i * lineWidth,
						drawY, p);
			}
		}
	}

	public Rect ce(String str) {
		Rect rect = new Rect();
		p.getTextBounds(str, 0, 1, rect);
		return rect;
	}


	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

}