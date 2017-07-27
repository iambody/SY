package com.cgbsoft.privatefund.mvp.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.base.mvp.ui.BaseFragment;
import com.cgbsoft.lib.base.webview.WebViewConstant;
import com.cgbsoft.lib.mvp.model.video.VideoInfoModel;
import com.cgbsoft.lib.utils.imgNetLoad.Imageload;
import com.cgbsoft.lib.utils.tools.CollectionUtils;
import com.cgbsoft.lib.utils.tools.DimensionPixelUtil;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.utils.MainTabManager;
import com.cgbsoft.privatefund.utils.receiver.HoriizontalItemDecoration;

import java.util.ArrayList;
import java.util.List;

import app.privatefund.com.vido.VideoNavigationUtils;
import app.privatefund.com.vido.mvp.ui.video.VideoDownloadListActivity;
import app.privatefund.com.vido.mvp.ui.video.VideoHistoryListActivity;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author chenlong
 */
public class HorizontalScrollFragment extends BaseFragment {

    public static final String GET_VIDEO_PARAMS = "get_video_params";
    public static final String IS_VIDEO_PLAY_PARAMS = "is_play_video_params";

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.goto_has_video_data)
    LinearLayout linearLayout;

    @BindView(R.id.goto_look_video)
    Button button;

    private MyHolderAdapter myHolderAdapter;
    private ChangeHeightListener listener;

    private List<VideoInfoModel> list;
    private boolean isPlay;
    private int heaight;

    @Override
    protected int layoutID() {
        return R.layout.fragment_horizontal;
    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {
        list = getArguments()!= null ? getArguments().getParcelableArrayList(GET_VIDEO_PARAMS) : new ArrayList<>();
        isPlay = getArguments() != null && getArguments().getBoolean(IS_VIDEO_PLAY_PARAMS, false);
        System.out.println("------list=" + list.size());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.addItemDecoration(new HoriizontalItemDecoration(getActivity(), R.color.white, R.dimen.ui_10_dip));
        recyclerView.setLayoutManager(linearLayoutManager);
        myHolderAdapter = new MyHolderAdapter(getActivity(), list, isPlay);
        recyclerView.setAdapter(myHolderAdapter);
        linearLayout.setVisibility(CollectionUtils.isEmpty(list) ? View.GONE : View.VISIBLE);
        button.setVisibility(CollectionUtils.isEmpty(list) ? View.VISIBLE : View.GONE);
        measureHeight();
    }

    private void measureHeight() {
        heaight = CollectionUtils.isEmpty(list) ? DimensionPixelUtil.dip2px(getActivity(), 150) : DimensionPixelUtil.dip2px(getActivity(), 240);
        if (listener != null) {
            listener.changeData(isPlay ? 0 : 1 ,heaight);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        System.out.println("---values="  +context.getClass().getSimpleName());
        if(context instanceof MainPageActivity){
            listener =  (MineFragment)MainTabManager.getInstance().getFragmentByIndex(R.id.nav_right_second, 0);
        }
    }

    public void refrushData(List<VideoInfoModel> list) {
        System.out.println("------list=" + list.size());
        this.list = list;
        linearLayout.setVisibility(CollectionUtils.isEmpty(list) ? View.GONE : View.VISIBLE);
        button.setVisibility(CollectionUtils.isEmpty(list) ? View.VISIBLE : View.GONE);
        myHolderAdapter.refrushData(list);
        measureHeight();
    }

    @OnClick(R.id.goto_look_more)
    void gotoLookAllVideo() {
        if (isPlay) {
            NavigationUtils.startActivity(getActivity(), VideoHistoryListActivity.class);
        } else {
            NavigationUtils.startActivity(getActivity(), VideoDownloadListActivity.class);
        }
    }

    @OnClick(R.id.goto_look_video)
    void gotoLookVideo() {
        if (isPlay) {
            NavigationUtils.jumpNativePage(getActivity(), WebViewConstant.Navigation.VIDEO_PAGE);
        } else {
            NavigationUtils.jumpNativePage(getActivity(), WebViewConstant.Navigation.VIDEO_PAGE);
//            NavigationUtils.startActivity(getActivity(), VideoDownloadListActivity.class);
        }
    }

    @Override
    protected BasePresenterImpl createPresenter() {
        return null;
    }

    public class MyHolderAdapter extends RecyclerView.Adapter<ViewHolder> {
        private LayoutInflater mInflater;
        private List<VideoInfoModel> mDatas;
        private boolean isPlayVideo;

        public MyHolderAdapter(Context context, List<VideoInfoModel> datats, boolean isPlayVideo) {
            mInflater = LayoutInflater.from(context);
            mDatas = (!CollectionUtils.isEmpty(datats) && datats.size() > 10) ? mDatas.subList(0, 11) : datats;
            this.isPlayVideo = isPlayVideo;
        }

        public void refrushData(List<VideoInfoModel> videoInfoModelList) {
            if (!CollectionUtils.isEmpty(videoInfoModelList)) {
                mDatas = (!CollectionUtils.isEmpty(videoInfoModelList) && videoInfoModelList.size() > 10) ? mDatas.subList(0, 11) : videoInfoModelList;
                notifyDataSetChanged();
            }
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
            VideoInfoModel videoInfoModel = mDatas.get(position);
            Imageload.display(getContext(), videoInfoModel.videoCoverUrl, holder.mImg);
            holder.mTxt.setText(videoInfoModel.videoName);
            holder.rootView.setOnClickListener(v ->
                    VideoNavigationUtils.stareVideoDetail(getActivity(), String.valueOf(videoInfoModel.id), videoInfoModel.videoCoverUrl));
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0){
            super(arg0);
            this.rootView = arg0;
        }
        View rootView;
        ImageView mImg;
        TextView mTxt;
    }

    public interface ChangeHeightListener{
        void changeData(int position,int height);
    }
}
