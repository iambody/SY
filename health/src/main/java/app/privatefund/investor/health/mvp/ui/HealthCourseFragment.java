package app.privatefund.investor.health.mvp.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.cgbsoft.lib.base.mvp.ui.BaseLazyFragment;
import com.cgbsoft.lib.base.webview.BaseWebview;
import com.cgbsoft.lib.base.webview.WebViewConstant;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.constant.RxConstant;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.CollectionUtils;
import com.cgbsoft.lib.utils.tools.DataStatistApiParam;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.lib.utils.tools.NetUtils;
import com.cgbsoft.lib.utils.tools.PromptManager;
import com.cgbsoft.lib.utils.tools.Utils;
import com.cgbsoft.lib.widget.dialog.LoadingDialog;
import com.cgbsoft.lib.widget.swipefresh.CustomRefreshFootView;
import com.cgbsoft.lib.widget.swipefresh.CustomRefreshHeadView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import app.privatefund.investor.health.R;
import app.privatefund.investor.health.R2;
import app.privatefund.investor.health.adapter.HealthCourseAdapter;
import app.privatefund.investor.health.mvp.contract.HealthCourseListContract;
import app.privatefund.investor.health.mvp.model.HealthCourseEntity;
import app.privatefund.investor.health.mvp.presenter.HealthCoursePresenter;
import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;

/**
 * @author chenlong
 */
public class HealthCourseFragment extends BaseLazyFragment<HealthCoursePresenter> implements HealthCourseListContract.View, OnLoadMoreListener, OnRefreshListener {

    public static final String INIT_LIST_DATA_PARAMS = "list_data_params";
    public static final int REQUEST_BACK_CODE = 10;
    private static int LIMIT_PAGE = 20;

    @BindView(R2.id.swipe_refresh_header)
    CustomRefreshHeadView swipeRefreshHeader;

    @BindView(R2.id.swipe_target)
    RecyclerView swipeTarget;

    @BindView(R2.id.swipe_load_more_footer)
    CustomRefreshFootView swipeLoadMoreFooter;

    @BindView(R2.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;

    @BindView(R2.id.empty_textview)
    TextView emptyTextView;

    @BindView(R2.id.empty_ll)
    LinearLayout emptyLinearlayout;

    @BindView(R2.id.fragment_videoschool_noresult_lay)
    RelativeLayout fragmentVideoschoolNoresultLay;

    private Observable<Boolean> rerushListObservable;
    private HealthCourseEntity.HealthCourseListModel currentListModel;
    private HealthCourseAdapter checkHealthAdapter;
    private LinearLayoutManager linearLayoutManager;
    private LoadingDialog mLoadingDialog;

    /**
     * 类别的数据
     */
    private int CurrentPostion = 0;
    private boolean isLoadMore;
    private int totalCount;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_health_course_list;
    }

    @Override
    protected void onFirstUserVisible() {
        mLoadingDialog = LoadingDialog.getLoadingDialog(getActivity(), "", false, false);
        emptyTextView.setText(String.format(getString(R.string.empty_text_descrption), "文章"));
        checkHealthAdapter = new HealthCourseAdapter(getActivity(), new ArrayList<>());
        swipeToLoadLayout.setOnLoadMoreListener(this);
        swipeToLoadLayout.setOnRefreshListener(this);
        linearLayoutManager = new LinearLayoutManager(fBaseActivity);
        swipeTarget.setLayoutManager(linearLayoutManager);
        swipeTarget.addItemDecoration(new HealthItemDecoration(fBaseActivity, R.color.app_split_line, R.dimen.ui_z_dip));
        checkHealthAdapter.setOnItemClickListener((position, discoveryListModel) -> {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put(WebViewConstant.push_message_title, discoveryListModel.getShortName());
            hashMap.put(WebViewConstant.push_message_url, Utils.appendWebViewUrl(discoveryListModel.getDetailUrl()).concat("?id=").concat(discoveryListModel.getId()));
            NavigationUtils.startActivityForResultByRouter(getActivity(), RouteConfig.GOTO_RIGHT_SHARE_ACTIVITY, hashMap, REQUEST_BACK_CODE);
            DataStatistApiParam.operateHealthIntroduceClick(discoveryListModel.getTitle());
            this.currentListModel = discoveryListModel;
        });
        swipeTarget.setAdapter(checkHealthAdapter);
        getPresenter().getHealthCourseList(String.valueOf(CurrentPostion * LIMIT_PAGE));
        initObsaverble();
    }

    private void initObsaverble() {
        rerushListObservable = RxBus.get().register(RxConstant.COURSE_HEALTH_LIST_REFRUSH_OBSERVABLE, Boolean.class);
        rerushListObservable.subscribe(new RxSubscriber<Boolean>() {
            @Override
            protected void onEvent(Boolean aBoolean) {
                if (currentListModel != null) {
                    checkHealthAdapter.notifyDataReadCount(currentListModel);
                }
            }

            @Override
            protected void onRxError(Throwable error) {
            }
        });
    }

    /**
     * 显示loading弹窗
     */
    @Override
    public void showLoadDialog() {
        if (mLoadingDialog.isShowing()) {
            return;
        }
        mLoadingDialog.show();
    }

    /**
     * 隐藏loading弹窗
     */
    @Override
    public void hideLoadDialog() {
        mLoadingDialog.dismiss();
    }

    @Override
    protected void onUserVisible() {
    }

    @Override
    protected void onUserInvisible() {
    }

    @Override
    protected void DetoryViewAndThing() {
    }

    @Override
    protected void initViewsAndEvents(View view) {
    }

    @Override
    protected void create(Bundle Mybundle) {
    }

    @Override
    public void onPause() {
        super.onPause();
//        if (isCheckHealth) {
//            MobclickAgent.onPageEnd(Constant.SXY_JIANKANG_JC);
//        } else {
//            MobclickAgent.onPageEnd(Constant.SXY_JIANKANG_YL);
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        if (isCheckHealth) {
//            MobclickAgent.onPageStart(Constant.SXY_JIANKANG_JC);
//        } else {
//            MobclickAgent.onPageStart(Constant.SXY_JIANKANG_YL);
//        }
    }

    @Override
    protected HealthCoursePresenter createPresenter() {
        return new HealthCoursePresenter(getActivity(), this);
    }

    public void FreshAp(List<HealthCourseEntity.HealthCourseListModel> healthListModelList, boolean isAdd) {
        checkHealthAdapter.refrushData(healthListModelList, !isAdd);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
//            if (isCheckHealth) {
//                DataStatistApiParam.operateHealthCheckClick();
//            } else {
//                DataStatistApiParam.operateHealthMedcialClick();
//            }
        }
        System.out.println("-----isVisibleToUser=" + isVisibleToUser);

    }

    @Override
    public void onLoadMore() {
        if (totalCount != 0 && checkHealthAdapter.getItemCount() != 0 && checkHealthAdapter.getItemCount() < totalCount) {
            CurrentPostion = CurrentPostion + 1;
            isLoadMore = true;
            getPresenter().getHealthCourseList(String.valueOf(CurrentPostion * LIMIT_PAGE));
            DataStatistApiParam.operatePrivateBankDiscoverDownLoadClick();
        } else {
            Toast.makeText(getContext(), "已经加载全部数据", Toast.LENGTH_SHORT).show();
            clodLsAnim(swipeToLoadLayout);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (rerushListObservable != null) {
            RxBus.get().unregister(RxConstant.COURSE_HEALTH_LIST_REFRUSH_OBSERVABLE, rerushListObservable);
        }
    }

    @Override
    public void onRefresh() {
        CurrentPostion = 0;
        isLoadMore = false;
        getPresenter().getHealthCourseList(String.valueOf(CurrentPostion * LIMIT_PAGE));
        DataStatistApiParam.operatePrivateBankDiscoverUpRefrushClick();
    }

    @Override
    public void requestDataSuccess(List<HealthCourseEntity.HealthCourseListModel> healthListModelList, int total) {
        if (View.GONE == swipeToLoadLayout.getVisibility()) { // 一直显示
            swipeToLoadLayout.setVisibility(View.VISIBLE);
            fragmentVideoschoolNoresultLay.setVisibility(View.GONE);
        }
        if (View.VISIBLE == fragmentVideoschoolNoresultLay.getVisibility()) { // 一直隐藏
            fragmentVideoschoolNoresultLay.setVisibility(View.GONE);
        }

        if (CollectionUtils.isEmpty(healthListModelList) && !isLoadMore) {
            swipeToLoadLayout.setVisibility(View.GONE);
            emptyLinearlayout.setVisibility(View.VISIBLE);
        } else {
            swipeToLoadLayout.setVisibility(View.VISIBLE);
            emptyLinearlayout.setVisibility(View.GONE);
        }
        this.totalCount = total;
        clodLsAnim(swipeToLoadLayout);
        FreshAp(healthListModelList, isLoadMore);
        isLoadMore = false;
    }

    @Override
    public void requestDataFailure(String errMsg) {
        clodLsAnim(swipeToLoadLayout);
        if (!isLoadMore && 0 == checkHealthAdapter.getItemCount()) {
            fragmentVideoschoolNoresultLay.setVisibility(View.VISIBLE);
            swipeToLoadLayout.setVisibility(View.GONE);
            emptyLinearlayout.setVisibility(View.GONE);
        } else {
            PromptManager.ShowCustomToast(fBaseActivity, getResources().getString(R.string.error_net));
        }
        isLoadMore = false;
    }

    @OnClick(R2.id.fragment_videoschool_noresult)
    public void onViewnoresultClicked() {
        if (NetUtils.isNetworkAvailable(fBaseActivity)) {//有网
            if (checkHealthAdapter != null) {
                CurrentPostion = 0;
                isLoadMore = false;
                getPresenter().getHealthCourseList(String.valueOf(CurrentPostion * LIMIT_PAGE));
            }
        } else {
            PromptManager.ShowCustomToast(fBaseActivity, getResources().getString(R.string.error_net));
        }
    }

}
