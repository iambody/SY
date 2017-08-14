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
 * 日期 2017/8/11-20:23
 * 需要画一张半圆去进行拼接
 */
public class AdviserLayoutView extends View {
    Paint smallRecPaint;
    Paint smallCirPaint;


    int smallRecWidth = 40;
    int smallRecHeight = 60;
    int smallCirRadius = 30;

    int smallRecColor;
    int smallCirColor;

    RectF smallCirRectF;//= new RectF();


    public AdviserLayoutView(Context context) {
        super(context);
    }

    public AdviserLayoutView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    private void init() {
        //小矩形
        smallRecColor = Color.BLACK;
        smallRecPaint = new Paint();
        smallRecPaint.setColor(smallRecColor);
        //小半圆
        smallCirColor = Color.BLACK;
        smallCirPaint = new Paint();
        smallCirPaint.setColor(smallCirColor);
        //小半圆的RectF

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        smallCirRectF = new RectF(-getWidth(), 0, getWidth(), getHeight());
        smallCirPaint.setColor(Color.BLACK);
//        canvas.drawArc(smallCirRectF,);
//        canvas.drawRect(0, 0, smallRecWidth, smallRecHeight, smallRecPaint);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    private int measureWidth(int widthMeeasureSpec) {

        int result = 0;
        int widthMode = MeasureSpec.getMode(widthMeeasureSpec);
        int widthtSize = MeasureSpec.getSize(widthMeeasureSpec);
        if (MeasureSpec.EXACTLY == widthMode) {
            result = widthtSize;
        } else {
            result = smallCirRadius + smallRecWidth;
            if (MeasureSpec.AT_MOST == widthMode) {
                result = Math.min(result, widthtSize);
            }
        }
        return result;
    }


    private int measureHeight(int heightMeasureSpec) {

        int result = 0;
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);


        if (MeasureSpec.EXACTLY == heightMode) {
            result = heightSize;
        } else {
            result = smallRecHeight;
            if (MeasureSpec.AT_MOST == heightMode) {
                result = Math.min(result, heightSize);
            }
        }
        return result;

    }
}
