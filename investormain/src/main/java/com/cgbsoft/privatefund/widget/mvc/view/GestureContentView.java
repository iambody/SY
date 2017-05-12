package com.cgbsoft.privatefund.widget.mvc.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cgbsoft.lib.utils.tools.Utils;
import com.cgbsoft.privatefund.R;

import java.util.ArrayList;
import java.util.List;

public class GestureContentView extends ViewGroup {
    private int baseNum = 6;
    private int[] screenDispaly;
    private int screenWidth;
    private int blockWidth;
    private List<GesturePoint> list;
    private Context context;
    private boolean isVerify;
    private GestureDrawline gestureDrawline;

    public GestureContentView(Context context, boolean isVerify, String passWord, GestureDrawline.GestureCallBack callBack) {
        super(context);
        screenDispaly = Utils.getScreenDispaly(context);
        screenWidth = screenDispaly[0];
        blockWidth = screenWidth / 4;
        this.list = new ArrayList<GesturePoint>();
        this.context = context;
        this.isVerify = isVerify;
        addChild();
        gestureDrawline = new GestureDrawline(context, list, isVerify, passWord, callBack);
    }

    private void addChild() {
        int startPostion = (screenWidth - blockWidth * 3) / 2;
        for (int i = 0; i < 9; i++) {
            ImageView image = new ImageView(context);
            image.setBackgroundResource(R.drawable.gesture_node_normal);
            this.addView(image);
            invalidate();
            int row = i / 3;
            int col = i % 3;
            int leftX = startPostion + col * blockWidth + blockWidth / baseNum;
            int topY = row * blockWidth + blockWidth / baseNum;
            int rightX = startPostion + col * blockWidth + blockWidth - blockWidth / baseNum;
            int bottomY = row * blockWidth + blockWidth - blockWidth / baseNum;
            GesturePoint p = new GesturePoint(leftX, rightX, topY, bottomY, image, i + 1);
            this.list.add(p);
        }
    }

    public void setParentView(ViewGroup parent) {
        int width = screenDispaly[0];
        LayoutParams layoutParams = new LayoutParams(width, width);
        this.setLayoutParams(layoutParams);
        gestureDrawline.setLayoutParams(layoutParams);
        parent.addView(gestureDrawline);
        parent.addView(this);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            int row = i / 3;
            int col = i % 3;
            View v = getChildAt(i);
            int startPostion = (screenWidth - blockWidth * 3) / 2;
            v.layout(startPostion + col * blockWidth + blockWidth / baseNum, row * blockWidth + blockWidth / baseNum, startPostion + col * blockWidth + blockWidth - blockWidth / baseNum, row * blockWidth + blockWidth - blockWidth / baseNum);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            v.measure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    /**
     * @param delayTime
     */
    public void clearDrawlineState(long delayTime) {
        gestureDrawline.clearDrawlineState(delayTime);
    }
}
