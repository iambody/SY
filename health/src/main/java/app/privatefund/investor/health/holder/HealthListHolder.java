package app.privatefund.investor.health.holder;

import android.view.View;
import android.widget.ImageView;

import com.cgbsoft.lib.widget.recycler.BaseHolder;

import app.privatefund.investor.health.R2;
import app.privatefund.investor.health.mvp.ui.listener.HealthListListener;
import butterknife.BindView;

/**
 * @author chenlong
 */
public class HealthListHolder extends BaseHolder {

    @BindView(R2.id.image_view)
    public ImageView imageView;

    public HealthListHolder(View itemView, HealthListListener listener) {
        super(itemView);
        imageView.setOnClickListener(v -> listener.onVideoListItemClick(getAdapterPosition(), imageView));
    }
}
