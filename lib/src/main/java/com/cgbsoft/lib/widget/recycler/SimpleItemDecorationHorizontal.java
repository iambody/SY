package com.cgbsoft.lib.widget.recycler;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/6/27-15:35
 */
public class SimpleItemDecorationHorizontal extends RecyclerView.ItemDecoration {

    protected Drawable divider;
    protected int margin;
    private Context context;

    public SimpleItemDecorationHorizontal(Context context, int ColorId, int HeightId) {
        this.context = context;
        this.margin = context.getResources().getDimensionPixelOffset(HeightId);
        this.divider = ContextCompat.getDrawable(context, ColorId);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int childAdapterPosition = parent.getChildAdapterPosition(view);
//        if (childAdapterPosition == parent.getChildCount() - 1) {
//            outRect.right = 0;
//        }else{
            outRect.right = margin;
//        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        drawHorizontal(c, parent);
    }

    private void drawHorizontal(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();

        final RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        for (int i = 0; i < parent.getChildCount()-1; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            drawLeft(top, bottom, child, c, layoutManager, params);
            drawRight(child, top, bottom, c, layoutManager, params);
        }
    }

    private void drawLeft(int top, int bottom, View child, Canvas c, RecyclerView.LayoutManager layoutManager, RecyclerView.LayoutParams params) {
        final int right = child.getLeft() + params.leftMargin;
        final int left = right - layoutManager.getLeftDecorationWidth(child);
        divider.setBounds(left, top, right, bottom);
        divider.draw(c);
    }

    private void drawRight(View child, int top, int bottom, Canvas c, RecyclerView.LayoutManager layoutManager, RecyclerView.LayoutParams params) {
        final int left = child.getRight() + params.rightMargin;
        final int right = left + layoutManager.getRightDecorationWidth(child);
        divider.setBounds(left, top, right, bottom);
        divider.draw(c);
    }

}
