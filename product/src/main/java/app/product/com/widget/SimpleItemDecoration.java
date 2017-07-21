package app.product.com.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.cgbsoft.lib.utils.tools.DimensionPixelUtil;
import app.product.com.R;
import app.product.com.mvc.adapter.SearchAdatper;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/6-19:17
 */
public class SimpleItemDecoration extends RecyclerView.ItemDecoration {

    protected Drawable divider;
    protected Drawable headerDrawble;
    protected int margin;
    private Context context;
    private static int MARGIN_TOP = 15;

    public SimpleItemDecoration(Context context,int ColorId,int HeightId) {
        this.context = context;
        this.margin = context.getResources().getDimensionPixelOffset(HeightId);
        this.divider = ContextCompat.getDrawable(context, ColorId);
        this.headerDrawble = ContextCompat.getDrawable(context, R.drawable.bg_white);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        RecyclerView.ViewHolder viewHolder = parent.getChildViewHolder(view);
        SearchAdatper.BaseViewHolder baseViewHolder = null;
        outRect.bottom = margin;
        if (viewHolder instanceof SearchAdatper.BaseViewHolder) {
            baseViewHolder = (SearchAdatper.BaseViewHolder) viewHolder;
        }

        if (viewHolder instanceof SearchAdatper.FooterViewHolder ||
                (baseViewHolder != null && baseViewHolder.displayFinished)) {
            outRect.bottom = DimensionPixelUtil.dip2px(context, MARGIN_TOP);
            outRect.left = 15;
            outRect.right = 15;
        }

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
