package app.privatefund.com.im.data;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cgbsoft.lib.R;
import com.cgbsoft.lib.widget.recycler.BaseAdapter;

/**
 * 融云service的adapter
 * Created by xiaoyu.zhang on 2016/11/25 15:32
 * Email:zhangxyfs@126.com
 *  
 */
public class RongServiceAdapter extends BaseAdapter<RongServiceModel, RongServiceListener, RecyclerView.ViewHolder> {

    public RongServiceAdapter(RongServiceListener listener) {
        super(listener);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == RongServiceModel.NORMAL) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_custom_dialog, null);
            return new RongServiceHolder(view, listener);
        }
        return onCreateErrorViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemViewType(int position) {
        return getList().get(position).type;
    }
}
