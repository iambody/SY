package app.privatefund.investor.health.mvp.ui;

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

    /**
     * 类别的数据
     */
    private int CurrentPostion = 0;
    private static int LIMIT_PAGE = 20;
    private boolean isLoadMore;
    private int total;

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
        String modelHtml = getPresenter().getLocalHealthModelPath();
        iconToModel.setVisibility(TextUtils.isEmpty(modelHtml) ? View.GONE : View.VISIBLE);
        if (!TextUtils.isEmpty(modelHtml)) {
            baseWebview.loadUrls("content://".concat(modelHtml));
        }
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
        healthProjectListRl.setVisibility(View.GONE);
        healthProjectModelRl.setVisibility(View.VISIBLE);
    }

    @OnClick(R2.id.icon_to_list)
    public void toGOHealthList() {
        healthProjectListRl.setVisibility(View.VISIBLE);
        healthProjectModelRl.setVisibility(View.GONE);
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
            fragmentVideoschoolNoresultLay.setVisibility(View.GONE);
        }
        if (View.VISIBLE == fragmentVideoschoolNoresultLay.getVisibility()) {//一直隐藏
            fragmentVideoschoolNoresultLay.setVisibility(View.GONE);
        }

        if (CollectionUtils.isEmpty(healthProjectlist) && !isLoadMore) {
            swipeToLoadLayout.setVisibility(View.GONE);
            emptyLinearlayout.setVisibility(View.VISIBLE);
        } else {
            swipeToLoadLayout.setVisibility(View.VISIBLE);
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
        } else {
            PromptManager.ShowCustomToast(fBaseActivity, getResources().getString(R.string.error_net));
        }
        isLoadMore = false;
    }

    @OnClick(R2.id.fragment_videoschool_noresult)
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
