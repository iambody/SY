package app.privatefund.investor.health.mvp.ui.listener;

import android.widget.ImageView;

import com.cgbsoft.lib.widget.recycler.OnBaseListener;

/**
 * @author chenlong
 */
public interface HealthListListener extends OnBaseListener {
    void onVideoListItemClick(int position, ImageView imageView);
}
