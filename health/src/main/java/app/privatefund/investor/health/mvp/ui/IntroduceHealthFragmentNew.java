package app.privatefund.investor.health.mvp.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.cgbsoft.lib.base.mvp.ui.BaseFragment;
import com.cgbsoft.lib.base.webview.BaseWebview;
import com.cgbsoft.lib.base.webview.WebViewConstant;
import com.cgbsoft.lib.utils.tools.CollectionUtils;
import com.cgbsoft.lib.utils.tools.LogUtils;
import com.cgbsoft.lib.widget.dialog.LoadingDialog;
import com.cgbsoft.lib.widget.recycler.SimpleItemDecorationHorizontal;

import java.util.List;

import app.privatefund.investor.health.R2;
import app.privatefund.investor.health.adapter.HealthIntroduceFlagRecyclerAdapter;
import app.privatefund.investor.health.mvp.contract.HealthIntroduceContract;
import app.privatefund.investor.health.mvp.model.HealthIntroduceNavigationEntity;
import app.privatefund.investor.health.mvp.presenter.HealthIntroducePresenter;
import butterknife.BindView;

/**
 * @author chenlong
 */
public class IntroduceHealthFragmentNew extends BaseFragment<HealthIntroducePresenter> implements HealthIntroduceContract.View {
//    @BindView(R2.id.ll_category_all)
//    LinearLayout categoryAll;

    @BindView(R2.id.health_introduce_rv)
    RecyclerView recyclerView;

    @BindView(R2.id.webview)
    BaseWebview baseWebview;


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
        healthIntroduceFlagRecyclerAdapter.setCategoryItemClickListener((view1, oldPosition, position, posBean) -> {
            LogUtils.Log("aaa", "click item");
            if (TextUtils.equals(category, posBean.getCode())) {
                return;
            }
            category = posBean.getCode();
            getPresenter().initNavigationContent(baseWebview, posBean);
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

    @Override
    public void requestNavigationSuccess(List<HealthIntroduceNavigationEntity> list) {
        healthIntroduceFlagRecyclerAdapter.setDatas(list);
        if (!CollectionUtils.isEmpty(list)) {
            getPresenter().initNavigationContent(baseWebview, list.get(0));
        }
    }

    @Override
    public void requestNavigationFailure(String errorMsg) {
        Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
    }

}
