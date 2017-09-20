package app.privatefund.com.vido.mvp.ui.video;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.LogUtils;
import com.cgbsoft.lib.widget.swipefresh.FullyLinearLayoutManager;
import com.kogitune.activity_transition.ActivityTransitionLauncher;
import com.umeng.analytics.MobclickAgent;

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
public class VideoHistoryListActivity extends BaseActivity<VideoHistoryListPresenter> implements VideoHistoryListContract.View, VideoHistoryListener {

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

    ImageView history_back_iv;

    TextView history_del_txt;
    ImageView history_del_iv;

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
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart(Constant.SXY_BFJL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd(Constant.SXY_BFJL);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initFindId();
        videoHistoryAdapter = new VideoHistoryAdapter(this);
        linearLayoutManager = new FullyLinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        //更多**********
        morevideoHistoryAdapter = new VideoHistoryAdapter(new HistoryLisener());
        morelinearLayoutManager = new FullyLinearLayoutManager(this);
        morelinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        morelinearLayoutManager.setSmoothScrollbarEnabled(true);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(videoHistoryAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        //更多**********
        moreRecyclerView.setLayoutManager(morelinearLayoutManager);
        moreRecyclerView.setAdapter(morevideoHistoryAdapter);
        moreRecyclerView.setHasFixedSize(true);


    }

    private void initFindId() {
        history_back_iv = (ImageView) findViewById(R.id.history_back_iv);
        history_del_iv = (ImageView) findViewById(R.id.history_del_iv);
        history_del_txt = (TextView) findViewById(R.id.history_del_txt);
        video_history_todaytitle_lay = findViewById(R.id.video_history_todaytitle_lay);
        video_history_moretitle_lay = findViewById(R.id.video_history_moretitle_lay);
        history_del_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoHistoryAdapter.getList().size() == 1 && morevideoHistoryAdapter.getList().size() == 1) {
                    if (videoHistoryAdapter.getList().get(0).type == VideoHistoryModel.ERROR && morevideoHistoryAdapter.getList().get(0).type == VideoHistoryModel.ERROR)
                        return;
                }
                videoHistoryAdapter.changeCheck();
                morevideoHistoryAdapter.changeCheck();


                if (videoHistoryAdapter.getCheckStatus() && morevideoHistoryAdapter.getCheckStatus()) {
                    visableBottomLayout();
                } else {
                    unVisableBottomLayout();
                }
            }
        });
        history_del_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoHistoryAdapter.getList().size() == 1 && morevideoHistoryAdapter.getList().size() == 1) {
                    if (videoHistoryAdapter.getList().get(0).type == VideoHistoryModel.ERROR && morevideoHistoryAdapter.getList().get(0).type == VideoHistoryModel.ERROR)
                        return;
                }
                videoHistoryAdapter.changeCheck();
                morevideoHistoryAdapter.changeCheck();


                if (videoHistoryAdapter.getCheckStatus() && morevideoHistoryAdapter.getCheckStatus()) {
                    visableBottomLayout();
                } else {
                    unVisableBottomLayout();
                }
            }
        });
        history_back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseContext.finish();
            }
        });
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

        if (!getPresenter().isAnyChoice(list) && !getPresenter().isAnyChoice(morelist)) {
            return;
        }


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
        getHandler().postDelayed(() -> onControlGetDataList(true), 400);
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
//            deleteItem.setVisible(false);
        } else {
            videoHistoryEmpty.setVisibility(View.GONE);
//            deleteItem.setVisible(true);
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


    }

    @Override
    public void getLocalListFail(boolean isRef) {

    }

    //    @Override
    public void onControlGetDataList(boolean isRef) {
        getPresenter().getLocalVideoInfoList(isRef);
    }



    @Override
    public void onErrorClickListener() {
        onRefresh();
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



    private void choiceChangeText(int num) {
        tv_avh_choiceAll.setText(R.string.cancel_choice_all_str);
        tv_avh_delete.setTextColor(getResources().getColor(R.color.app_golden));
        tv_avh_delete.setText(getString(R.string.delete_1_str, String.valueOf(num)));
    }

    private void unChoiceChangeText() {
        tv_avh_choiceAll.setText(R.string.choice_all_str);
        tv_avh_delete.setTextColor(getResources().getColor(R.color.color_999999));
        tv_avh_delete.setText(R.string.delete_str);
    }

    private void visableBottomLayout() {
        ll_avh.setVisibility(View.VISIBLE);

        history_del_txt.setVisibility(View.VISIBLE);
        history_del_iv.setVisibility(View.GONE);
        history_del_txt.setText("取消");


    }

    private void unVisableBottomLayout() {
        ll_avh.setVisibility(View.GONE);
        history_del_txt.setVisibility(View.GONE);
        history_del_iv.setVisibility(View.VISIBLE);

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
