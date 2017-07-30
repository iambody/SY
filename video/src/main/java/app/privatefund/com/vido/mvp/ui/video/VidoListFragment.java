package app.privatefund.com.vido.mvp.ui.video;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.mvp.ui.BaseLazyFragment;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.listener.listener.ListItemClickListener;
import com.cgbsoft.lib.utils.tools.LogUtils;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.lib.widget.recycler.SimpleItemDecoration;
import com.cgbsoft.lib.widget.swipefresh.CustomRefreshFootView;
import com.cgbsoft.lib.widget.swipefresh.CustomRefreshHeadView;
import com.cgbsoft.privatefund.bean.video.VideoAllModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import app.privatefund.com.vido.R;
import app.privatefund.com.vido.R2;
import app.privatefund.com.vido.VideoNavigationUtils;
import app.privatefund.com.vido.adapter.VideoListAdapter;
import app.privatefund.com.vido.mvp.contract.video.VideoListContract;
import app.privatefund.com.vido.mvp.presenter.video.VideoListPresenter;
import butterknife.BindView;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/6/26-18:08
 */
public class VidoListFragment extends BaseLazyFragment<VideoListPresenter> implements VideoListContract.View, OnLoadMoreListener, OnRefreshListener {
    @BindView(R2.id.swipe_refresh_header)
    CustomRefreshHeadView swipeRefreshHeader;
    @BindView(R2.id.swipe_target)
    RecyclerView swipeTarget;
    @BindView(R2.id.swipe_load_more_footer)
    CustomRefreshFootView swipeLoadMoreFooter;
    @BindView(R2.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;

    public static final String FRAGMETN_PARAMS = "video_params_fragment";

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
     *
     */
    public VideoListAdapter videoListAdapter;

    //标记第几页的位置
    private int CurrentPostion = 0;
    //标记是否是架子啊更多

    private boolean isLoadMore;



    @Override
    protected void create(Bundle Mybundle) {
        if (getArguments() != null) {
            CatoryValue = getArguments().getString(FRAGMETN_PARAMS, "");
        }
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
        swipeTarget.addItemDecoration(new SimpleItemDecoration(fBaseActivity, R.color.app_bg, R.dimen.ui_5_dip));

        videoListAdapter.setOnItemClickListener(new ListItemClickListener<VideoAllModel.VideoListModel>() {
            @Override
            public void onItemClick(int position, VideoAllModel.VideoListModel videoListModel) {
//                VideoNavigationUtils.stareVideoDetail(fBaseActivity, videoListModel.videoId, videoListModel.coverImageUrl);
//                Intent toHomeIntent = new Intent(fBaseActivity, LoginActivity.class);
//                toHomeIntent.putExtra(LoginActivity.TAG_GOTOLOGIN, true);
//                UiSkipUtils.toNextActivityWithIntent(baseActivity, toHomeIntent);
                if (AppManager.isVisitor(fBaseActivity)) {
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("insidegotologin", true);
                    NavigationUtils.startActivityByRouter(fBaseActivity, RouteConfig.GOTO_LOGIN, map);
                } else {
                    VideoNavigationUtils.stareVideoDetail(fBaseActivity, videoListModel.videoId, videoListModel.coverImageUrl);
                }
            }

        });
        swipeTarget.setAdapter(videoListAdapter);

        //第一次显示的时候全部不需要加载数据  非全部需要进行请求网络数据
        if (null == videoListModelList) {//
            getPresenter().getVideoList(CatoryValue, CurrentPostion);
        }

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
    protected VideoListPresenter createPresenter() {
        return new VideoListPresenter(fBaseActivity, this);
    }

    @Override
    public void getVideoDataSucc(String data) {

        clodLsAnim(swipeToLoadLayout);
        List<VideoAllModel.VideoListModel> videoListModels = new Gson().fromJson(data, new TypeToken<List<VideoAllModel.VideoListModel>>() {
        }.getType());
        FreshAp(videoListModels, isLoadMore);


        isLoadMore = false;
    }

    @Override
    public void getVideoDataError(String message) {
        clodLsAnim(swipeToLoadLayout);


        isLoadMore = false;
    }


    public void freshData(List<VideoAllModel.VideoListModel> videoListModels) {
        videoListModelList = videoListModels;
        CurrentPostion = 1;
    }


    public void FreshAp(List<VideoAllModel.VideoListModel> videoListModels, boolean isAdd) {
//        if (null == videoListModelList) videoListModelList = new ArrayList<>();
//
//        if (isAdd) {
////            videoListModelList.addAll(videoListModels);
//
//            videoListAdapter.freshAp(videoListModels);
//        } else {
//            videoListModelList = videoListModels;
//        }
        if (null == videoListModels) videoListModels = new ArrayList<>();
        if (isAdd) {
            if (0 != videoListModels.size())
                videoListAdapter.addFreshAp(videoListModels);
        } else {
            videoListAdapter.freshAp(videoListModels);
        }

    }

    @Override
    public void onLoadMore() {
        isLoadMore = true;
        CurrentPostion = CurrentPostion + 1;
        getPresenter().getVideoList(CatoryValue, CurrentPostion);
    }

    @Override
    public void onRefresh() {
        isLoadMore = false;
        CurrentPostion = 0;
        getPresenter().getVideoList(CatoryValue, CurrentPostion);
    }

}
