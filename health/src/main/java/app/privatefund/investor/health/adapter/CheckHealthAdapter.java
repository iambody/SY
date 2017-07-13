package app.privatefund.investor.health.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.cgbsoft.lib.utils.imgNetLoad.Imageload;
import com.cgbsoft.lib.widget.recycler.BaseAdapter;

import app.privatefund.investor.health.R;
import app.privatefund.investor.health.holder.HealthListHolder;
import app.privatefund.investor.health.mvp.model.HealthListModel;
import app.privatefund.investor.health.mvp.ui.listener.HealthListListener;

/**
 * @author chenlong
 */
public class CheckHealthAdapter extends BaseAdapter<HealthListModel, HealthListListener, RecyclerView.ViewHolder> {

    private boolean fromCheckHealth;

    public CheckHealthAdapter(HealthListListener listener, boolean isCheckHealth) {
        super(listener);
        this.fromCheckHealth = isCheckHealth;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HealthListModel.BOTTOM) {
            return new HealthListHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_health_list, parent, false), listener);
        }
        return onCreateErrorViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        HealthListModel model = list.get(position);
        if (model.type == HealthListModel.BOTTOM) {
            HealthListHolder vlh = (HealthListHolder) holder;
            Imageload.display(vlh.context, model.getImageUrl(), 0, 0, 2, vlh.imageView, R.drawable.bg_default, R.drawable.bg_default);
        } else {
            bindErrorHolder(model, holder);
        }
    }
}
