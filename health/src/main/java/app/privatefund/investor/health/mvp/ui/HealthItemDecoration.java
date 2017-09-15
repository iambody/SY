package app.privatefund.investor.health.mvp.ui;

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
 * 日期 2017/5/6-19:17
 */
public class HealthItemDecoration extends RecyclerView.ItemDecoration{

    protected Drawable divider;
    protected int margin ;
    private Context context;
//    private static int MARGIN_LEFT_RIGHT = 30;

    public HealthItemDecoration(Context context, int ColorId, int HeightId) {
        this.context = context;
        this.margin = context.getResources().getDimensionPixelOffset(HeightId);
        this.divider = ContextCompat.getDrawable(context, ColorId);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        int value = parent.getAdapter().getItemCount();
        int position = parent.getChildAdapterPosition(view);
        if (position != value - 1) {
            outRect.bottom = margin;
        }
//        outRect.left = MARGIN_LEFT_RIGHT;
//        outRect.right = MARGIN_LEFT_RIGHT;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        drawVertical(c, parent);
    }

    private void drawVertical(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();

        final RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        for (int i = 0; i < parent.getChildCount(); i++) {
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
