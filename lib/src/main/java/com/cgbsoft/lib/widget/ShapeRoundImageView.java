package com.cgbsoft.lib.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.cgbsoft.lib.utils.tools.DimensionPixelUtil;
import com.cgbsoft.lib.utils.tools.LogUtils;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/6/30-15:41
 */
public class ShapeRoundImageView extends ImageView {
    private Paint mPaint; //画笔

    private int mRadius; //圆形图片的半径

    private float mScale; //图片的缩放比例

    private Context context;

    public ShapeRoundImageView(Context context) {
        super(context);
        this.context=context;
    }

    public ShapeRoundImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ShapeRoundImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //因为是圆形图片，所以应该让宽高保持一致
//        int size = Math.min(getMeasuredWidth(), getMeasuredHeight());
        mRadius = getMeasuredHeight() / 2;
//        setMeasuredDimension(size+ DimensionPixelUtil.dip2px(getContext(),10), size);
//        LogUtils.Log("ivv", "onMeasure=>mRadius" + mRadius);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        LogUtils.Log("ivv", "onDraw==>");
        mPaint = new Paint();
        Bitmap bitmap = drawableToBitamp(getDrawable());

        //初始化BitmapShader，传入bitmap对象
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        //计算缩放比例
        mScale = (mRadius * 2.0f) / Math.min(bitmap.getHeight(), bitmap.getWidth());

        Matrix matrix = new Matrix();
        matrix.setScale(mScale, mScale);
        bitmapShader.setLocalMatrix(matrix);


        mPaint.setShader(bitmapShader);
        mPaint.setAntiAlias(true);
        //先画一张背景
         Paint bgPaint=new Paint();
        bgPaint.setColor(Color.BLACK);
        canvas.drawRect(0,0,mRadius+DimensionPixelUtil.dip2px(getContext(),5),mRadius*2,bgPaint);

        //画圆形，指定好中心点坐标、半径、画笔
        canvas.drawCircle(mRadius+DimensionPixelUtil.dip2px(getContext(),5), mRadius, mRadius, mPaint);

        //给图形加边框
        Paint paint =new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(10);
        paint.setColor(Color.BLACK);
        canvas.drawCircle(mRadius+DimensionPixelUtil.dip2px(getContext(),5), mRadius, mRadius-5,
                paint);
    }

    /**
     * drawable转bitmap
     *
     * @param drawable
     * @return
     */
    private Bitmap drawableToBitamp(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bd = (BitmapDrawable) drawable;
            return bd.getBitmap();
        }
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 通过半径进行绘制圆角
     *
     * @return
     */
    public static Bitmap getCroppedBitmap(Bitmap bmp, int radius) {
        Bitmap sbmp;
        if (bmp.getWidth() != radius || bmp.getHeight() != radius)
            sbmp = Bitmap.createScaledBitmap(bmp, radius, radius, false);
        else
            sbmp = bmp;
        Bitmap output = Bitmap.createBitmap(sbmp.getWidth(), sbmp.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xffa19774;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        //设置笔的颜色
        paint.setColor(Color.parseColor("#BAB399"));
        //画圆
        canvas.drawCircle(sbmp.getWidth() / 2 + 0.5f,
                sbmp.getHeight() / 2 + 0.5f, sbmp.getWidth() / 2 + 0.5f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(sbmp, rect, rect, paint);

        return output;
    }
}
