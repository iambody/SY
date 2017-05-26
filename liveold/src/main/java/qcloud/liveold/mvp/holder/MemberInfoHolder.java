package qcloud.liveold.mvp.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import qcloud.liveold.R;


/**
 * Created by lee on 2016/12/2.
 */
public class MemberInfoHolder extends RecyclerView.ViewHolder {
    public ImageView imageView;

    public MemberInfoHolder(View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.title_view);
    }
}