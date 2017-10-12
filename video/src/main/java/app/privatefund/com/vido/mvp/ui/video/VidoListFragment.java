package app.privatefund.com.vido.mvp.ui.video;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.mvp.ui.BaseLazyFragment;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.tools.LogUtils;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.lib.utils.tools.NetUtils;
import com.cgbsoft.lib.utils.tools.PromptManager;
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
import butterknife.OnClick;

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
    @BindView(R2.id.fragment_videoschool_noresult)
    ImageView fragmentVideoschoolNoresult;
    @BindView(R2.id.fragment_videoschool_noresult_lay)
    RelativeLayout fragmentVideoschoolNoresultLay;


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
    //标记是否是否加载更多

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
        swipeTarget.addItemDecoration(new SimpleItemDecoration(fBaseActivity, R.color.app_bg, R.dimen.ui_15_dip));

        videoListAdapter.setOnItemClickListener((position, videoListModel) -> {
            if (AppManager.isVisitor(fBaseActivity)) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("insidegotologin", true);
                NavigationUtils.startActivityByRouter(fBaseActivity, RouteConfig.GOTO_LOGIN, map);
            } else {
                if (!NetUtils.isNetworkAvailable(fBaseActivity)) {
                    PromptManager.ShowCustomToast(fBaseActivity, "请连接网络");
                    return;
                }
                VideoNavigationUtils.stareVideoDetail(fBaseActivity, videoListModel.videoId, videoListModel.coverImageUrl);

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
        swipeToLoadLayout.setVisibility(View.VISIBLE);
        fragmentVideoschoolNoresultLay.setVisibility(View.GONE);


        clodLsAnim(swipeToLoadLayout);
        List<VideoAllModel.VideoListModel> videoListModels = new Gson().fromJson(data, new TypeToken<List<VideoAllModel.VideoListModel>>() {
        }.getType());
        FreshAp(videoListModels, isLoadMore);


        isLoadMore = false;
    }

    @Override
    public void getVideoDataError(String message) {
        clodLsAnim(swipeToLoadLayout);
        if (!isLoadMore && 0 == videoListAdapter.getItemCount()) {//下拉刷新或者是初始化
            fragmentVideoschoolNoresultLay.setVisibility(View.VISIBLE);
            swipeToLoadLayout.setVisibility(View.GONE);
        }
        isLoadMore = false;
    }

    public void freshData(List<VideoAllModel.VideoListModel> videoListModels) {
        videoListModelList = videoListModels;
        CurrentPostion = 1;
    }


    public void FreshAp(List<VideoAllModel.VideoListModel> videoListModels, boolean isAdd) {

        if (null == videoListModels) videoListModels = new ArrayList<>();
        if (isAdd) {
            if (0 != videoListModels.size()) {//获取数据
                videoListAdapter.addFreshAp(videoListModels);
            } else {//加载完毕
                PromptManager.ShowCustomToast(fBaseActivity,getResources().getString(R.string.no_moredata));
            }
        } else {
            videoListAdapter.freshAp(videoListModels);
            if(0==videoListModels.size()){
                PromptManager.ShowCustomToast(fBaseActivity,getResources().getString(R.string.no_initvideo));
            }
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

    @OnClick(R2.id.fragment_videoschool_noresult)
    public void onnoresultClicked() {
        if (NetUtils.isNetworkAvailable(fBaseActivity)) { //有网
            if (null == videoListModelList) {//
                getPresenter().getVideoList(CatoryValue, CurrentPostion);
            }
        } else {
            PromptManager.ShowCustomToast(fBaseActivity, getResources().getString(R.string.error_net));
        }
    }


}
