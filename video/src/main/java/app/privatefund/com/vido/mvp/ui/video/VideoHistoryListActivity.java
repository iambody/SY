package app.privatefund.com.vido.mvp.ui.video;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.LogUtils;
import com.cgbsoft.lib.widget.swipefresh.FullyLinearLayoutManager;
import com.kogitune.activity_transition.ActivityTransitionLauncher;

import java.util.List;

import app.privatefund.com.vido.R;
import app.privatefund.com.vido.R2;
import app.privatefund.com.vido.mvp.contract.video.VideoHistoryListContract;
import app.privatefund.com.vido.mvp.presenter.video.VideoHistoryListPresenter;
import app.privatefund.com.vido.mvp.ui.video.adapter.VideoHistoryAdapter;
import app.privatefund.com.vido.mvp.ui.video.listener.VideoHistoryListener;
import app.privatefund.com.vido.mvp.ui.video.model.VideoHistoryModel;
import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;

import static com.cgbsoft.lib.utils.constant.RxConstant.VIDEO_LOCAL_REF_ONE_OBSERVABLE;

/**
 * 播放历史
 */
public class VideoHistoryListActivity extends BaseActivity<VideoHistoryListPresenter> implements VideoHistoryListContract.View, VideoHistoryListener,
        Toolbar.OnMenuItemClickListener {
    @BindView(R2.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R2.id.toolbar)
    Toolbar toolbar;

    @BindView(R2.id.tv_title)
    TextView tv_title;

//    @BindView(R2.id.recyclerRefreshLayout)
//    RecyclerRefreshLayout recyclerRefreshLayout;

    @BindView(R2.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R2.id.ll_avh)
    LinearLayout ll_avh;

    @BindView(R2.id.tv_avh_choiceAll)
    TextView tv_avh_choiceAll;

    @BindView(R2.id.tv_avh_delete)
    TextView tv_avh_delete;
    @BindView(R2.id.more_recyclerView)
    RecyclerView moreRecyclerView;

    View video_history_todaytitle_lay;
    View video_history_moretitle_lay;
    @BindView(R2.id.video_history_empty)
    RelativeLayout videoHistoryEmpty;

    //    private RecyclerControl recyclerControl;
    private FullyLinearLayoutManager linearLayoutManager;
    private VideoHistoryAdapter videoHistoryAdapter;

    //更多
    private FullyLinearLayoutManager morelinearLayoutManager;
    private VideoHistoryAdapter morevideoHistoryAdapter;

    //标记选择的个数
    //今日查看选择的个数
    private int todyCheckedNumber;
    //更多查看选择的个数
    private int moreCheckedNumber;

    private Observable<Integer> refItemObservable;
    private boolean isChoiceAll;

    private MenuItem deleteItem;

    @Override
    protected void after() {
        super.after();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    @Override
    protected int layoutID() {
        return R.layout.activity_video_history;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initFindId();
        videoHistoryAdapter = new VideoHistoryAdapter(this);
        linearLayoutManager = new FullyLinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //更多**********
        morevideoHistoryAdapter = new VideoHistoryAdapter(new HistoryLisener());
        morelinearLayoutManager = new FullyLinearLayoutManager(this);
        morelinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        recyclerControl = new RecyclerControl(recyclerRefreshLayout, linearLayoutManager, this);
//        recyclerRefreshLayout.setOnRefreshListener(this);
//        recyclerRefreshLayout.setEnabled(false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(videoHistoryAdapter);
        recyclerView.setHasFixedSize(true);
        //更多**********
        moreRecyclerView.setLayoutManager(morelinearLayoutManager);
        moreRecyclerView.setAdapter(morevideoHistoryAdapter);
        moreRecyclerView.setHasFixedSize(true);


        toolbar.setTitle(null);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(null);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setNavigationOnClickListener(v -> finish());
        tv_title.setText(R.string.play_history_str);
    }

    private void initFindId() {
        video_history_todaytitle_lay = findViewById(R.id.video_history_todaytitle_lay);
        video_history_moretitle_lay = findViewById(R.id.video_history_moretitle_lay);
    }

    @Override
    protected void data() {
        super.data();
        refItemObservable = RxBus.get().register(VIDEO_LOCAL_REF_ONE_OBSERVABLE, Integer.class);
        refItemObservable.subscribe(new RxSubscriber<Integer>() {
            @Override
            protected void onEvent(Integer integer) {
                LogUtils.Log("jav", "number=" + integer);
                getHandler().postDelayed(() -> onControlGetDataList(true), 100);
            }

            @Override
            protected void onRxError(Throwable error) {

            }
        });
        onRefresh();
    }

    @OnClick(R2.id.tv_avh_choiceAll)
    void choiceAllClick() {
        isChoiceAll = !isChoiceAll;
        List<VideoHistoryModel> todylist = videoHistoryAdapter.getList();
        List<VideoHistoryModel> morelist = morevideoHistoryAdapter.getList();
        for (int i = 0; i < todylist.size(); i++) {
            todylist.get(i).isCheck = isChoiceAll;
        }
        for (int i = 0; i < morelist.size(); i++) {
            morelist.get(i).isCheck = isChoiceAll;
        }
        videoHistoryAdapter.notifyDataSetChanged();
        morevideoHistoryAdapter.notifyDataSetChanged();
        if (isChoiceAll) {
            choiceChangeText(todylist.size() + morelist.size());
        } else {
            unChoiceChangeText();
        }
    }

    @OnClick(R2.id.tv_avh_delete)
    void deleteClick() {
        List<VideoHistoryModel> list = videoHistoryAdapter.getList();
        List<VideoHistoryModel> morelist = morevideoHistoryAdapter.getList();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isCheck) {
                getPresenter().delete(list.get(i).videoId);
            }
        }
        for (int i = 0; i < morelist.size(); i++) {
            if (morelist.get(i).isCheck) {
                getPresenter().delete(morelist.get(i).videoId);
            }
        }
        unChoiceChangeText();
        getHandler().postDelayed(() -> onControlGetDataList(true), 100);
    }

    @Override
    protected VideoHistoryListPresenter createPresenter() {
        return new VideoHistoryListPresenter(this, this);
    }

    @Override
    public void getLocalListSucc(List<VideoHistoryModel> dataList, boolean isRef) {
        if (dataList.size() == 0) {//没做分页所以直接判断就行
            unVisableBottomLayout();
            video_history_todaytitle_lay.setVisibility(View.GONE);
            video_history_moretitle_lay.setVisibility(View.GONE);
            videoHistoryEmpty.setVisibility(View.VISIBLE);
            deleteItem.setVisible(false);
        } else {
            videoHistoryEmpty.setVisibility(View.GONE);
            deleteItem.setVisible(true);
        }

        List<VideoHistoryModel> todylist = getPresenter().getVideoListtody(dataList, true);
        List<VideoHistoryModel> morelist = getPresenter().getVideoListtody(dataList, false);
        video_history_todaytitle_lay.setVisibility(0 == todylist.size() ? View.GONE : View.VISIBLE);
        video_history_moretitle_lay.setVisibility(0 == morelist.size() ? View.GONE : View.VISIBLE);
        if (isRef) {
            videoHistoryAdapter.deleteAllData();
            videoHistoryAdapter.refAllData(todylist);
            //更多
            morevideoHistoryAdapter.deleteAllData();
            morevideoHistoryAdapter.refAllData(morelist);
        } else {
            videoHistoryAdapter.appendToList(dataList);
        }

//        recyclerControl.getDataComplete(isRef);
//        recyclerControl.setError(this, false, videoHistoryAdapter, new VideoHistoryModel(), "", R.drawable.bfjl_kong);
//        recyclerRefreshLayout.setEnabled(false);
    }

    @Override
    public void getLocalListFail(boolean isRef) {
//        recyclerControl.getDataComplete(isRef);
//        recyclerControl.setError(this, true, videoHistoryAdapter, new VideoHistoryModel());
//        recyclerRefreshLayout.setEnabled(false);
    }

    //    @Override
    public void onControlGetDataList(boolean isRef) {
        getPresenter().getLocalVideoInfoList(isRef);
    }

//    @Override
//    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//
//    }
//
//    @Override
//    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//
//    }

    @Override
    public void onErrorClickListener() {
        onRefresh();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.firstBtn) {
            if (videoHistoryAdapter.getList().size() == 1 && morevideoHistoryAdapter.getList().size() == 1) {
                if (videoHistoryAdapter.getList().get(0).type == VideoHistoryModel.ERROR && morevideoHistoryAdapter.getList().get(0).type == VideoHistoryModel.ERROR)
                    return false;
            }
            videoHistoryAdapter.changeCheck();
            morevideoHistoryAdapter.changeCheck();


            if (videoHistoryAdapter.getCheckStatus() && morevideoHistoryAdapter.getCheckStatus()) {
                visableBottomLayout();
            } else {
                unVisableBottomLayout();
            }
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.page_menu, menu);
        deleteItem = menu.findItem(R.id.firstBtn);
        MenuItem secItem = menu.findItem(R.id.secondBtn);
        deleteItem.setTitle(R.string.delete_str);
        deleteItem.setIcon(R.drawable.ic_local_delete);
        secItem.setVisible(false);
        return true;
    }

    //    @Override
    public void onRefresh() {
        getHandler().postDelayed(() -> onControlGetDataList(true), 100);
    }

    @Override
    public void onItemClick(int position, ImageView iv_avh_cover) {
        if (videoHistoryAdapter.getCheckStatus()) {
            ll_avh.setVisibility(View.GONE);
            unVisableBottomLayout();
            unChoiceChangeText();
            videoHistoryAdapter.changeCheck();
        }

        VideoHistoryModel model = videoHistoryAdapter.getList().get(position);
        if (model != null) {
            Intent intent = new Intent(this, VideoDetailActivity.class);
            intent.putExtra("videoId", model.videoId);
            intent.putExtra("videoCoverUrl", model.videoCoverUrl);
            ActivityTransitionLauncher.with(this).from(iv_avh_cover).launch(intent);
        }
    }

    @Override
    public void onCheck(int position, boolean isCheck) {
        videoHistoryAdapter.getList().get(position).isCheck = isCheck;
        int choiceNum = 0;
        List<VideoHistoryModel> list = videoHistoryAdapter.getList();
        List<VideoHistoryModel> morelist = morevideoHistoryAdapter.getList();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isCheck) {
                choiceNum++;
            }
        }
        todyCheckedNumber = choiceNum;
        if ((todyCheckedNumber + moreCheckedNumber) == 0) {
            unChoiceChangeText();
        } else {
            choiceChangeText(todyCheckedNumber + moreCheckedNumber);
        }

        if ((todyCheckedNumber + moreCheckedNumber) != (list.size() + morelist.size())) {
            isChoiceAll = false;
            tv_avh_choiceAll.setText(R.string.choice_all_str);
        } else {
            isChoiceAll = true;
            tv_avh_choiceAll.setText(R.string.cancel_choice_all_str);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (refItemObservable != null) {
            RxBus.get().unregister(VIDEO_LOCAL_REF_ONE_OBSERVABLE, refItemObservable);
        }
    }

//    private void setRefLayoutMarginBottom(int dp) {
//        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) recyclerRefreshLayout.getLayoutParams();
//        lp.setMargins(0, 0, 0, Utils.convertDipOrPx(this, dp));
//        recyclerRefreshLayout.setLayoutParams(lp);
//    }

    private void choiceChangeText(int num) {
        tv_avh_choiceAll.setText(R.string.cancel_choice_all_str);
        tv_avh_delete.setTextColor(getResources().getColor(R.color.color_f22502));
        tv_avh_delete.setText(getString(R.string.delete_1_str, String.valueOf(num)));
    }

    private void unChoiceChangeText() {
        tv_avh_choiceAll.setText(R.string.choice_all_str);
        tv_avh_delete.setTextColor(getResources().getColor(R.color.color_999999));
        tv_avh_delete.setText(R.string.delete_str);
    }

    private void visableBottomLayout() {
        ll_avh.setVisibility(View.VISIBLE);
//        setRefLayoutMarginBottom(44);
        deleteItem.setIcon(null);
        deleteItem.setTitle(R.string.cancel_str);
    }

    private void unVisableBottomLayout() {
        ll_avh.setVisibility(View.GONE);
//        setRefLayoutMarginBottom(0);
        deleteItem.setIcon(R.drawable.ic_local_delete);
        deleteItem.setTitle(R.string.delete_str);
    }


    class HistoryLisener implements VideoHistoryListener {


        @Override
        public void onErrorClickListener() {

        }

        @Override
        public void onItemClick(int position, ImageView iv_avh_cover) {
            if (morevideoHistoryAdapter.getCheckStatus()) {
                ll_avh.setVisibility(View.GONE);
                unVisableBottomLayout();
                unChoiceChangeText();
                morevideoHistoryAdapter.changeCheck();
            }

            VideoHistoryModel model = morevideoHistoryAdapter.getList().get(position);
            if (model != null) {
                Intent intent = new Intent(baseContext, VideoDetailActivity.class);
                intent.putExtra("videoId", model.videoId);
                intent.putExtra("videoCoverUrl", model.videoCoverUrl);
                ActivityTransitionLauncher.with(baseContext).from(iv_avh_cover).launch(intent);
            }
        }

        @Override
        public void onCheck(int position, boolean isCheck) {
            morevideoHistoryAdapter.getList().get(position).isCheck = isCheck;

            int choiceNum = 0;
            List<VideoHistoryModel> list = morevideoHistoryAdapter.getList();
            List<VideoHistoryModel> todylist = videoHistoryAdapter.getList();

            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).isCheck) {
                    choiceNum++;
                }
            }
            moreCheckedNumber = choiceNum;
            if ((todyCheckedNumber + moreCheckedNumber) == 0) {
                unChoiceChangeText();
            } else {
                choiceChangeText(todyCheckedNumber + moreCheckedNumber);
            }

            if ((todyCheckedNumber + moreCheckedNumber) != (list.size() + todylist.size())) {
                isChoiceAll = false;
                tv_avh_choiceAll.setText(R.string.choice_all_str);
            } else {
                isChoiceAll = true;
                tv_avh_choiceAll.setText(R.string.cancel_choice_all_str);
            }
        }
    }
}
