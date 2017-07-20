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
public class SimpleItemDecoration extends RecyclerView.ItemDecoration {

    private int marginEdge=0;
    protected Drawable divider;
    protected int margin;
    private Context context;

    public SimpleItemDecoration(Context context, int ColorId, int HeightId) {
        this.context = context;
        this.margin = context.getResources().getDimensionPixelOffset(HeightId);
        this.divider = ContextCompat.getDrawable(context, ColorId);
    }
    public SimpleItemDecoration(Context context, int ColorId, int HeightId,int margin) {
        this.context = context;
        this.margin = context.getResources().getDimensionPixelOffset(HeightId);
        this.divider = ContextCompat.getDrawable(context, ColorId);
        this.marginEdge=margin;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        int childAdapterPosition = parent.getChildAdapterPosition(view);

        int lastCount = parent.getAdapter().getItemCount() - 1;

        //如果当前条目与是最后一个条目，就不设置divider padding
        if (childAdapterPosition == lastCount) {
            outRect.set(0, 0, 0, 0);
            return;
        }
        outRect.bottom = margin;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        drawVertical(c, parent);
    }

    private void drawVertical(Canvas c, RecyclerView parent) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        if (marginEdge != 0) {
            left+=marginEdge;
            right-=marginEdge;
        }

        final RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        for (int i = 0; i < parent.getChildCount()-1; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            drawTop(left, right, child, c, layoutManager, params);
            drawBottom(child, left, right, c, layoutManager, params);

        }
    }

    private void drawTop(int left, int right, View child, Canvas c,
                         RecyclerView.LayoutManager layoutManager, RecyclerView.LayoutParams params) {
        final int bottom = child.getTop() + params.topMargin;
        final int top = bottom - layoutManager.getTopDecorationHeight(child);
        divider.setBounds(left, top, right, bottom);
        divider.draw(c);
    }

    private void drawBottom(View child, int left, int right, Canvas c,
                            RecyclerView.LayoutManager layoutManager, RecyclerView.LayoutParams params) {
        final int top = child.getBottom() + params.bottomMargin;
        final int bottom = top + layoutManager.getBottomDecorationHeight(child);
        divider.setBounds(left, top, right, bottom);
        divider.draw(c);
    }

}
