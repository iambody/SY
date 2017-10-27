package app.privatefund.investor.health.mvp.ui;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.cgbsoft.lib.utils.tools.CollectionUtils;
import com.cgbsoft.lib.utils.tools.DataStatistApiParam;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.lib.utils.tools.NetUtils;
import com.cgbsoft.lib.utils.tools.PromptManager;
import com.cgbsoft.lib.utils.tools.Utils;
import com.cgbsoft.lib.widget.dialog.LoadingDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import app.privatefund.investor.health.R;
import app.privatefund.investor.health.R2;
import app.privatefund.investor.health.adapter.HealthSummaryAdapter;
import app.privatefund.investor.health.mvp.contract.HealthSummaryListContract;
import app.privatefund.investor.health.mvp.model.HealthProjectListEntity;
import app.privatefund.investor.health.mvp.presenter.HealthSummparyPresenter;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author chenlong
 */
public class HealthSummaryFragment extends BaseLazyFragment<HealthSummparyPresenter> implements HealthSummaryListContract.View, OnLoadMoreListener, OnRefreshListener {

    @BindView(R2.id.root_container_id)
    LinearLayout rootView;

    @BindView(R2.id.swipe_target)
    RecyclerView swipeTarget;

    @BindView(R2.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;

    @BindView(R2.id.fragment_videoschool_noresult_lay)
    RelativeLayout fragmentVideoschoolNoresultLay;

    @BindView(R2.id.empty_textview)
    TextView emptyTextView;

    @BindView(R2.id.empty_ll)
    LinearLayout emptyLinearlayout;

    @BindView(R2.id.health_project_list_ll)
    RelativeLayout healthProjectListRl;

    @BindView(R2.id.health_project_model_ll)
    RelativeLayout healthProjectModelRl;

    @BindView(R2.id.basewebview)
    BaseWebview baseWebview;

    @BindView(R2.id.icon_to_model)
    ImageView iconToModel;

    @BindView(R2.id.icon_to_list)
    ImageView iconToList;

    private LoadingDialog mLoadingDialog;
    private HealthSummaryAdapter checkHealthAdapter;
    private LinearLayoutManager linearLayoutManager;
    private AnimatorSet mRightOutSet;
    private AnimatorSet mLeftInSet;

    /**
     * 类别的数据
     */
    private String modelHtml;
    private int CurrentPostion = 0;
    private static int LIMIT_PAGE = 20;
    private boolean isLoadMore;
    private int total;
    private boolean mIsShowList = true;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_health_summary_list;
    }

    @Override
    protected void onFirstUserVisible() {
        mLoadingDialog = LoadingDialog.getLoadingDialog(getActivity(), "", false, false);
        emptyTextView.setText(String.format(getString(R.string.empty_text_descrption), "项目"));
        checkHealthAdapter = new HealthSummaryAdapter(getActivity(), new ArrayList<>());
        swipeToLoadLayout.setOnLoadMoreListener(this);
        swipeToLoadLayout.setOnRefreshListener(this);
        linearLayoutManager = new LinearLayoutManager(fBaseActivity);
        swipeTarget.setLayoutManager(linearLayoutManager);
        swipeTarget.addItemDecoration(new HealthItemDecoration(fBaseActivity, R.color.app_split_line, R.dimen.ui_1_dip));
        checkHealthAdapter.setOnItemClickListener((position, discoveryListModel) -> {
            HashMap<String ,Object> hashMap = new HashMap<>();
            hashMap.put(WebViewConstant.RIGHT_SHARE, true);
            hashMap.put(WebViewConstant.push_message_title, discoveryListModel.getTitle());
            hashMap.put(WebViewConstant.push_message_url, Utils.appendWebViewUrl(discoveryListModel.getUrl()).concat("?healthId=").concat(discoveryListModel.getId()).concat("&healthImg=")
                    .concat(discoveryListModel.getImageUrl()).concat("&healthTitle=").concat(discoveryListModel.getTitle()).concat("&goCustomFeedBack=0"));
            NavigationUtils.startActivityByRouter(getActivity(), RouteConfig.GOTO_RIGHT_SHARE_ACTIVITY, hashMap);
            DataStatistApiParam.operateHealthIntroduceClick(discoveryListModel.getTitle());
        });
        swipeTarget.setAdapter(checkHealthAdapter);
        getPresenter().getHealthList(String.valueOf(CurrentPostion * LIMIT_PAGE));
        modelHtml = getPresenter().getLocalHealthModelPath();
        iconToModel.setVisibility(TextUtils.isEmpty(modelHtml) ? View.GONE : View.VISIBLE);
        if (!TextUtils.isEmpty(modelHtml)) {
            baseWebview.loadUrls("file://".concat(modelHtml));
        }
        setAnimators();
        setCameraDistance();
    }

    // 设置动画
    private void setAnimators() {
        mRightOutSet = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(),R.animator.right_out);
        mLeftInSet = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.left_in);

        // 设置点击事件
        mRightOutSet.addListener(new AnimatorListenerAdapter() {
            @Override public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                rootView.setClickable(false);
            }
        });
        mLeftInSet.addListener(new AnimatorListenerAdapter() {
            @Override public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                rootView.setClickable(true);
            }
        });
    }

    private void setCameraDistance() {
        int distance = 16000;
        float scale = getResources().getDisplayMetrics().density * distance;
        healthProjectListRl.setCameraDistance(scale);
        healthProjectModelRl.setCameraDistance(scale);
    }

    @Override
    protected void onUserVisible() {}

    @Override
    protected void onUserInvisible() {}

    @Override
    protected void DetoryViewAndThing() {}

    @Override
    protected void initViewsAndEvents(View view) {}

    @Override
    protected void create(Bundle Mybundle) {}

    @Override
    protected HealthSummparyPresenter createPresenter() {
        return new HealthSummparyPresenter(getActivity(), this);
    }

    public void FreshAp(List<HealthProjectListEntity.HealthProjectItemEntity> healthListModelList, boolean isAdd) {
        checkHealthAdapter.refrushData(healthListModelList, !isAdd);
    }

    @OnClick(R2.id.icon_to_model)
    public void toGOHealthModel() {
        flipCard();
        healthProjectListRl.setVisibility(View.GONE);
        healthProjectModelRl.setVisibility(View.VISIBLE);
    }

    @OnClick(R2.id.icon_to_list)
    public void toGOHealthList() {
        flipCard();
        healthProjectListRl.setVisibility(View.VISIBLE);
        healthProjectModelRl.setVisibility(View.GONE);
    }

    public void flipCard() {
        if (!mIsShowList) {
            mRightOutSet.setTarget(healthProjectListRl);
            mLeftInSet.setTarget(healthProjectModelRl);
            mRightOutSet.start();
            mLeftInSet.start();
            mIsShowList = true;
        } else {
            mRightOutSet.setTarget(healthProjectModelRl);
            mLeftInSet.setTarget(healthProjectListRl);
            mRightOutSet.start();
            mLeftInSet.start();
            mIsShowList = false;
        }
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
    public void onLoadMore() {
        if (total != 0 && checkHealthAdapter.getItemCount() != 0 && checkHealthAdapter.getItemCount() < total) {
            CurrentPostion = CurrentPostion + 1;
            isLoadMore = true;
            getPresenter().getHealthList(String.valueOf(CurrentPostion * LIMIT_PAGE));
            DataStatistApiParam.operatePrivateBankDiscoverDownLoadClick();
        } else {
            Toast.makeText(getContext(), "已经加载全部数据", Toast.LENGTH_SHORT).show();
            clodLsAnim(swipeToLoadLayout);
        }
    }

    @Override
    public void onRefresh() {
        CurrentPostion = 0;
        isLoadMore = false;
        getPresenter().getHealthList(String.valueOf(CurrentPostion * LIMIT_PAGE));
        DataStatistApiParam.operatePrivateBankDiscoverUpRefrushClick();
    }

    @Override
    public void requestDataSuccess(HealthProjectListEntity healthProjectListEntity) {
        total = healthProjectListEntity.getTotal();
        List<HealthProjectListEntity.HealthProjectItemEntity> healthProjectlist = healthProjectListEntity.getRows();
        if (View.GONE == swipeToLoadLayout.getVisibility()) {//一直显示
            swipeToLoadLayout.setVisibility(View.VISIBLE);
            iconToModel.setVisibility(TextUtils.isEmpty(modelHtml) ? View.GONE : View.VISIBLE);
            fragmentVideoschoolNoresultLay.setVisibility(View.GONE);
        }
        if (View.VISIBLE == fragmentVideoschoolNoresultLay.getVisibility()) {//一直隐藏
            fragmentVideoschoolNoresultLay.setVisibility(View.GONE);
        }

        if (CollectionUtils.isEmpty(healthProjectlist) && !isLoadMore) {
            swipeToLoadLayout.setVisibility(View.GONE);
            emptyLinearlayout.setVisibility(View.VISIBLE);
            iconToModel.setVisibility(View.GONE);
        } else {
            swipeToLoadLayout.setVisibility(View.VISIBLE);
            iconToModel.setVisibility(TextUtils.isEmpty(modelHtml) ? View.GONE : View.VISIBLE);
            emptyLinearlayout.setVisibility(View.GONE);
            if (CollectionUtils.isEmpty(healthProjectlist)) {
                Toast.makeText(getContext(), "已经加载全部数据", Toast.LENGTH_SHORT).show();
            }
        }

        clodLsAnim(swipeToLoadLayout);
        FreshAp(healthProjectlist, isLoadMore);
        isLoadMore = false;
    }

    @Override
    public void requestDataFailure(String errMsg) {
        clodLsAnim(swipeToLoadLayout);
        if (!isLoadMore && 0 == checkHealthAdapter.getItemCount()) {
            fragmentVideoschoolNoresultLay.setVisibility(View.VISIBLE);
            swipeToLoadLayout.setVisibility(View.GONE);
            emptyLinearlayout.setVisibility(View.GONE);
            iconToModel.setVisibility(View.GONE);
        } else {
            PromptManager.ShowCustomToast(fBaseActivity, getResources().getString(R.string.error_net));
        }
        isLoadMore = false;
    }

    @OnClick(R2.id.fragment_videoschool_noresult_lay)
    public void onViewnoresultClicked() {
        if (NetUtils.isNetworkAvailable(fBaseActivity)) { // 有网
            if (checkHealthAdapter != null) {
                CurrentPostion = 0;
                isLoadMore = false;
                getPresenter().getHealthList(String.valueOf(CurrentPostion * LIMIT_PAGE));
            }
        } else {
            PromptManager.ShowCustomToast(fBaseActivity, getResources().getString(R.string.error_net));
        }
    }
}
