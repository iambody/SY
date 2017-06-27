package app.privatefund.com.vido.mvp.ui.video;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.cgbsoft.lib.base.mvp.ui.BaseLazyFragment;
import com.cgbsoft.lib.listener.listener.ListItemClickListener;
import com.cgbsoft.lib.utils.tools.LogUtils;
import com.cgbsoft.lib.widget.recycler.SimpleItemDecoration;
import com.cgbsoft.lib.widget.swipefresh.CustomRefreshFootView;
import com.cgbsoft.lib.widget.swipefresh.CustomRefreshHeadView;

import java.util.ArrayList;
import java.util.List;

import app.privatefund.com.vido.R;
import app.privatefund.com.vido.R2;
import app.privatefund.com.vido.adapter.VideoListAdapter;
import app.privatefund.com.vido.bean.VideoAllModel;
import app.privatefund.com.vido.mvp.contract.video.VideoSchoolAllInfContract;
import app.privatefund.com.vido.mvp.presenter.video.VideoSchoolAllInfPresenter;
import butterknife.BindView;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/6/26-18:08
 */
public class VidoListFragment extends BaseLazyFragment<VideoSchoolAllInfPresenter> implements VideoSchoolAllInfContract.View, OnLoadMoreListener, OnRefreshListener {
    @BindView(R2.id.swipe_refresh_header)
    CustomRefreshHeadView swipeRefreshHeader;
    @BindView(R2.id.swipe_target)
    RecyclerView swipeTarget;
    @BindView(R2.id.swipe_load_more_footer)
    CustomRefreshFootView swipeLoadMoreFooter;
    @BindView(R2.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;

    private LinearLayoutManager linearLayoutManager;
    /**
     * 类别的数据
     */
    public String CatoryValue;

    /**
     * 数据源
     */
    public List<VideoAllModel.VideoListModel> videoListModelList;

    /**
     * Ap
     */
    public VideoListAdapter videoListAdapter;

    private int CurrentPostion = 0;


    public VidoListFragment(String postionString) {
        this.CatoryValue = postionString;
    }

    @Override
    protected void create(Bundle Mybundle) {
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_fragment_video_ls;
    }

    @Override
    protected void initViewsAndEvents(View view) {
    }

    @Override
    protected void onFirstUserVisible() {
        LogUtils.Log("fffa", "第一次可见:" + CatoryValue);
        videoListAdapter = new VideoListAdapter(null == videoListModelList ? new ArrayList<>() : videoListModelList, fBaseActivity);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        swipeToLoadLayout.setOnRefreshListener(this);
        linearLayoutManager = new LinearLayoutManager(fBaseActivity);
        swipeTarget.setLayoutManager(linearLayoutManager);
        swipeTarget.addItemDecoration(new SimpleItemDecoration(fBaseActivity, R.color.gray_font, R.dimen.ui_1_dip));

        videoListAdapter.setOnItemClickListener(new ListItemClickListener<VideoAllModel.VideoListModel>() {
            @Override
            public void onItemClick(int position, VideoAllModel.VideoListModel videoListModel) {

            }

            @Override
            public void onErrorClickListener() {

            }
        });
        swipeTarget.setAdapter(videoListAdapter);
    }

    @Override
    protected void onUserVisible() {
        LogUtils.Log("fffa", "可见:" + CatoryValue);
    }


    @Override
    protected void onUserInvisible() {

    }

    @Override
    protected void DetoryViewAndThing() {
        LogUtils.Log("fffa", "销毁:" + CatoryValue);
    }

    @Override
    protected VideoSchoolAllInfPresenter createPresenter() {
        return new VideoSchoolAllInfPresenter(fBaseActivity, this);
    }


    @Override
    public void getSchoolAllDataSucc(String data) {

    }

    @Override
    public void getSchoolAllDataError(String message) {

    }


    public void freshData(List<VideoAllModel.VideoListModel> videoListModels) {
        videoListModelList = videoListModels;
        CurrentPostion = 1;
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onRefresh() {

    }
}
