package app.privatefund.investor.health.mvp.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cgbsoft.lib.base.mvp.ui.BaseFragment;
import com.cgbsoft.lib.base.webview.WebViewConstant;
import com.cgbsoft.lib.utils.tools.CollectionUtils;
import com.cgbsoft.lib.utils.tools.NetUtils;
import com.cgbsoft.lib.utils.tools.PromptManager;
import com.cgbsoft.lib.widget.ExtendWebView;
import com.cgbsoft.lib.widget.dialog.LoadingDialog;
import com.cgbsoft.lib.widget.recycler.SimpleItemDecorationHorizontal;

import java.util.List;

import app.privatefund.investor.health.R;
import app.privatefund.investor.health.R2;
import app.privatefund.investor.health.adapter.HealthIntroduceFlagRecyclerAdapter;
import app.privatefund.investor.health.mvp.contract.HealthIntroduceContract;
import app.privatefund.investor.health.mvp.model.HealthIntroduceNavigationEntity;
import app.privatefund.investor.health.mvp.presenter.HealthIntroducePresenter;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author chenlong
 */
public class IntroduceHealthFragmentNew extends BaseFragment<HealthIntroducePresenter> implements HealthIntroduceContract.View {

    @BindView(R2.id.health_introduce_rv)
    RecyclerView recyclerView;

    @BindView(R2.id.ll_category_all)
    LinearLayout categoryHealthLayout;

    @BindView(R2.id.health_introduce_has_result)
    LinearLayout healthIntroduceHasFlag;

    @BindView(R2.id.fragment_introduce_network_error)
    LinearLayout introductNetError;

    @BindView(R2.id.empty_ll)
    LinearLayout healthIntroduceDataEmpty;

    @BindView(R2.id.webview)
    ExtendWebView baseWebview;
    private static final int HAS_DATA = 0;
    private static final int HAS_DATA_NO = 1;
    private static final int HAS_DATA_ERROR = 2;

    private String category;
    private LoadingDialog mLoadingDialog;
    private LinearLayoutManager linearLayoutManager;
    private HealthIntroduceFlagRecyclerAdapter healthIntroduceFlagRecyclerAdapter;

    @Override
    protected int layoutID() {
        return R.layout.fragment_introduce_health_new;
    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {
        mLoadingDialog = LoadingDialog.getLoadingDialog(baseActivity, "", false, false);
        linearLayoutManager = new LinearLayoutManager(baseActivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new SimpleItemDecorationHorizontal(baseActivity, android.R.color.transparent, R.dimen.ui_10_dip));
        recyclerView.setHasFixedSize(true);
        healthIntroduceFlagRecyclerAdapter = new HealthIntroduceFlagRecyclerAdapter(baseActivity);
        recyclerView.setAdapter(healthIntroduceFlagRecyclerAdapter);
        healthIntroduceFlagRecyclerAdapter.setCategoryItemClickListener((view1, posBean) -> {
            category = posBean.getCode();
            getPresenter().initNavigationContent(baseWebview, posBean);
        });
        baseWebview.setOnScrollChangedCallback(new ExtendWebView.OnScrollChangedCallback() {
            @Override
            public void onScrollUp() {
                if (categoryHealthLayout.getVisibility() == View.VISIBLE) {
                    categoryHealthLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onScrollDown() {
                if (categoryHealthLayout.getVisibility() == View.GONE) {
                    categoryHealthLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        getPresenter().introduceNavigation(String.valueOf(WebViewConstant.Navigation.HEALTH_INTRODUCTION_PAGE));
    }

    @Override
    protected HealthIntroducePresenter createPresenter() {
        return new HealthIntroducePresenter(getContext(), this);
    }

    @Override
    public void hideLoadDialog() {
        mLoadingDialog.dismiss();
    }

    @Override
    public void showLoadDialog() {
        if (null == mLoadingDialog) {
            mLoadingDialog = LoadingDialog.getLoadingDialog(baseActivity, "", false, false);
        }
        if (!mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
        }
    }

    private void showErrorAndNoData(int flag) {
        switch (flag) {
            case HAS_DATA:
                healthIntroduceHasFlag.setVisibility(View.VISIBLE);
                introductNetError.setVisibility(View.GONE);
                healthIntroduceDataEmpty.setVisibility(View.GONE);
                break;
            case HAS_DATA_NO:
                healthIntroduceHasFlag.setVisibility(View.GONE);
                introductNetError.setVisibility(View.GONE);
                healthIntroduceDataEmpty.setVisibility(View.VISIBLE);
                break;
            case HAS_DATA_ERROR:
                healthIntroduceHasFlag.setVisibility(View.GONE);
                introductNetError.setVisibility(View.VISIBLE);
                healthIntroduceDataEmpty.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void requestNavigationSuccess(List<HealthIntroduceNavigationEntity> list) {
        if (!CollectionUtils.isEmpty(list)) {
            showErrorAndNoData(HAS_DATA);
            list.get(0).setIsCheck(1);
            healthIntroduceFlagRecyclerAdapter.setDatas(list);
            getPresenter().initNavigationContent(baseWebview, list.get(0));
        } else {
            showErrorAndNoData(HAS_DATA_NO);
        }
    }

    @Override
    public void requestNavigationFailure(String errorMsg) {
        if (NetUtils.isNetworkAvailable(getContext())) {
            Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
        } else {
            showErrorAndNoData(HAS_DATA_ERROR);
            PromptManager.ShowCustomToast(getContext(), getResources().getString(R.string.notify_no_network));
        }
    }

    @OnClick(R2.id.fragment_introduce_network_error)
    public void healthIntroduceError() {
        getPresenter().introduceNavigation(category);
    }
}