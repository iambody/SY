package com.cgbsoft.privatefund.mvp.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.base.mvp.ui.BaseFragment;
import com.cgbsoft.lib.mvp.model.video.VideoInfoModel;
import com.cgbsoft.lib.utils.imgNetLoad.Imageload;
import com.cgbsoft.lib.utils.tools.CollectionUtils;
import com.cgbsoft.privatefund.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author chenlong
 */
public class HorizontalScrollFragment extends BaseFragment {

    public static final String GET_VIDEO_PARAMS = "get_video_params";
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.goto_look_video)
    Button button;

    private List<VideoInfoModel> list;

    @Override
    protected int layoutID() {
        return R.layout.fragment_horizontal;
    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {
        list = getArguments()!= null ? getArguments().getParcelableArrayList(GET_VIDEO_PARAMS) : new ArrayList<>();
        System.out.println("------list=" + list.size());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new MyHolderAdapter(getActivity(), list));
        recyclerView.setVisibility(CollectionUtils.isEmpty(list) ? View.GONE : View.VISIBLE);
        button.setVisibility(CollectionUtils.isEmpty(list) ? View.VISIBLE : View.GONE);
    }

    @OnClick(R.id.goto_look_video)
    void gotoLookVideo() {
        // togo video
    }

    @Override
    protected BasePresenterImpl createPresenter() {
        return null;
    }

    public class MyHolderAdapter extends RecyclerView.Adapter<ViewHolder> {
        private LayoutInflater mInflater;
        private List<VideoInfoModel> mDatas;

        public MyHolderAdapter(Context context, List<VideoInfoModel> datats) {
            mInflater = LayoutInflater.from(context);
            mDatas = datats;
        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = mInflater.inflate(R.layout.fragment_horizontal_item, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(view);
            viewHolder.mImg = (ImageView)view.findViewById(R.id.mine_video_image_id);
            viewHolder.mTxt = (TextView)view.findViewById(R.id.mine_video_text);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            System.out.println("------vass=" + mDatas.get(position).videoName);
            Imageload.display(getContext(), mDatas.get(position).videoCoverUrl, holder.mImg);
            holder.mTxt.setText(mDatas.get(position).videoName);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0){
            super(arg0);
        }
        ImageView mImg;
        TextView mTxt;
    }
}
