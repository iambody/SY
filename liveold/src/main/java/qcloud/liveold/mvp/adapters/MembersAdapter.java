package qcloud.liveold.mvp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.tools.DataStatistApiParam;

import java.util.ArrayList;

import qcloud.liveold.R;
import qcloud.liveold.mvp.holder.MemberInfoHolder;
import qcloud.liveold.mvp.model.MemberInfo;
import qcloud.liveold.mvp.views.LiveUserInfoDialog;


/**
 * 成员列表适配器
 */
public class MembersAdapter extends RecyclerView.Adapter<MemberInfoHolder> {

    private Context context;
    private ArrayList<MemberInfo> arrayList;

    public MembersAdapter(Context context, ArrayList<MemberInfo> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public MemberInfoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MemberInfoHolder holder = new MemberInfoHolder(View.inflate(parent.getContext(), R.layout.live_member_item, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(MemberInfoHolder holder, int position) {
        final MemberInfo memberInfo = arrayList.get(position);

        if (!TextUtils.isEmpty(memberInfo.getAvatar())) {
            Glide.with(context).load(memberInfo.getAvatar()).into(holder.imageView);
        }

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new LiveUserInfoDialog(context, memberInfo.getAvatar(), memberInfo.getUserName()) {
                    @Override
                    public void left() {
                        this.dismiss();
                    }
                }.show();
                String videoName = SPreference.getString(context, "liveName");
                if (AppManager.isInvestor(context)) {
                    DataStatistApiParam.onClickLiveRoomHeadImageToC(videoName);
                } else {
                    DataStatistApiParam.onClickLiveRoomHeadImageToB(videoName);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return arrayList == null ? 0 : arrayList.size();
    }
}