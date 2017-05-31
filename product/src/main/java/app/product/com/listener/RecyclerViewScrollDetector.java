package app.product.com.listener;

import android.support.v7.widget.RecyclerView;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/26-15:23
 */
public class RecyclerViewScrollDetector extends RecyclerView.OnScrollListener {
    private int mScrollThreshold;
    private scrollInterface scrollInterface;

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//        boolean isSignificantDelta = Math.abs(dy) > mScrollThreshold;
//        if (isSignificantDelta) {
//            if (dy > 0) {
//                scrollInterface.onScrollUp();
//            } else {
//                scrollInterface.onScrollDown();
//            }
//        }
        if (!recyclerView.canScrollVertically(-1)) {
            scrollInterface.onTop();
        }else{

        }
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        switch (newState){
            case 0://正在滚动
                scrollInterface.scrolling();
                break;
            case 1://正在被外部拖拽,一般为用户正在用手指滚动
                break;
            case 3://自动滚动开始
                break;

        }
    }

    public interface scrollInterface {

        public void onTop();

        public void scrolling();

    }

    public RecyclerViewScrollDetector(RecyclerViewScrollDetector.scrollInterface scrollInterface) {
        this.scrollInterface = scrollInterface;
    }

//    public void setScrollThreshold(int scrollThreshold) {
//        mScrollThreshold = scrollThreshold;
//    }
}
