package com.cgbsoft.privatefund.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;


/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/11/29-16:42
 */
public class SemicircleView extends View {
    private int circleColor = Color.parseColor("#4f000000");
    private Paint circlePaint;

    public SemicircleView(Context context) {
        super(context);
        initview();
    }

    public SemicircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initview();
    }

    public SemicircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.stewardview, 0, 0);
//        circleColor = typedArray.getColor(R.styleable.stewardview_viewcolor, circleColor);
//        typedArray.recycle();
//        initview();
    }

    private void initview() {
        circlePaint = new Paint();
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setColor(circleColor);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        initview();
/**
 * 这是一个居中的圆
 */
//        float x = (getWidth() - getHeight() / 2) / 2;
//        float y = getHeight() / 4;
        RectF oval = new RectF();                     //RectF对象
        oval.left = 0;                              //左边
        oval.top = 0;                                   //上边
        oval.right = getHeight();                             //右边
        oval.bottom = getHeight();


        canvas.drawArc(oval, 0, 90, false, circlePaint);//画圆弧，这个时候，绘制没有经过圆心


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    /**
     * 适配宽度
     *
     * @param widthMeasure
     * @return
     */
    private int measureWidth(int widthMeasure) {
        int result;
        int spacMode = MeasureSpec.getMode(widthMeasure);
        int spaceSize = MeasureSpec.getSize(widthMeasure);
        if (MeasureSpec.EXACTLY == spacMode) {
            result = spaceSize;
        } else {
            result = 100;
            if (MeasureSpec.AT_MOST == spacMode) {
                result = Math.min(result, spaceSize);
            }
        }
        return result;
    }

    /**
     * 适配高度
     *
     * @param heightMeasure
     * @return
     */
    private int measureHeight(int heightMeasure) {
        int result;
        int spacMode = MeasureSpec.getMode(heightMeasure);
        int spaceSize = MeasureSpec.getSize(heightMeasure);
        if (MeasureSpec.EXACTLY == spacMode) {
            result = spaceSize;
        } else {
            result = 100;
            if (MeasureSpec.AT_MOST == spacMode) {
                result = Math.min(result, spaceSize);
            }
        }
        return result;
    }
}
