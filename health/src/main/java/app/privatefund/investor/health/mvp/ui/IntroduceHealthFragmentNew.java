package app.privatefund.investor.health.mvp.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cgbsoft.lib.base.mvp.ui.BaseFragment;
import com.cgbsoft.lib.base.webview.WebViewConstant;
import com.cgbsoft.lib.utils.ZipResourceDownload;
import com.cgbsoft.lib.utils.tools.CollectionUtils;
import com.cgbsoft.lib.utils.tools.NetUtils;
import com.cgbsoft.lib.utils.tools.PromptManager;
import com.cgbsoft.lib.utils.tools.TrackingHealthDataStatistics;
import com.cgbsoft.lib.widget.MyBaseWebview;
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
    MyBaseWebview baseWebview;
    private static final int HAS_DATA = 0;
    private static final int HAS_DATA_NO = 1;
    private static final int HAS_DATA_ERROR = 2;
    private static final int DISTANCE_MAR_LENGT = 40;

    private String category;
    private LoadingDialog mLoadingDialog;
    private LinearLayoutManager linearLayoutManager;
    private HealthIntroduceFlagRecyclerAdapter healthIntroduceFlagRecyclerAdapter;
    private GestureDetector gestureDetector;
    private List<View> oldList;
    private ZipResourceDownload zipResourceDownload;

    @Override
    protected int layoutID() {
        return R.layout.fragment_introduce_health_new;
    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {
        zipResourceDownload = new ZipResourceDownload(getActivity());
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
            TrackingHealthDataStatistics.introduceClickFlag(getContext(), posBean.getTitle());
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (Math.abs(dx) > 5) {
                    if (dx > 0) {
                        TrackingHealthDataStatistics.introduceLeftScroll(getContext());
                    } else {
                        TrackingHealthDataStatistics.introduceRightScroll(getContext());
                    }
                }
            }
        });
        baseWebview.setOnScrollChangedCallback(new MyBaseWebview.OnScrollChangedCallback() {
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
//        testDilaog();
//        testds();
    }

//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        getActivity().getWindow().getDecorView().addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
//            @Override
//            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
//
//                if (oldList != null && getAllChildViews(getActivity().getWindow().getDecorView()).size() > oldList.size()) {
//
//                    for (View view :getAllChildViews(getActivity().getWindow().getDecorView())) {
//
//                        if (!oldList.contains(view)) {
//                            view.setVisibility(View.GONE);
//                        }
//                    }
//                }
//
//                ArrayList<View> outView= new ArrayList<View>();
//                getActivity().getWindow().getDecorView().findViewsWithText(outView, "微信好友", View.FIND_VIEWS_WITH_TEXT);
//                int size = outView.size();
//                if (outView != null && outView.size() > 0) {
//                    oldList =getAllChildViews(getActivity().getWindow().getDecorView());
//                    outView.get(0).setVisibility(View.GONE);
//                }
//
//                ArrayList<View> outViewQuan= new ArrayList<View>();
//                getActivity().getWindow().getDecorView().findViewsWithText(outViewQuan, "微信朋友圈", View.FIND_VIEWS_WITH_TEXT);
//                int sizeQUan = outViewQuan.size();
//                if (outViewQuan != null && outViewQuan.size() > 0) {
//                    oldList =getAllChildViews(getActivity().getWindow().getDecorView());
//                    outViewQuan.get(0).setVisibility(View.GONE);
//                }
//
//                ArrayList<View> outViewqq= new ArrayList<View>();
//                getActivity().getWindow().getDecorView().findViewsWithText(outViewqq, "QQ浏览器", View.FIND_VIEWS_WITH_TEXT);
//                if (outViewqq != null && outViewqq.size() > 0) {
//                    oldList =getAllChildViews(getActivity().getWindow().getDecorView());
//                    outViewqq.get(0).setVisibility(View.GONE);
//                }
//
//                ArrayList<View> friendQQ= new ArrayList<View>();
//                getActivity().getWindow().getDecorView().findViewsWithText(friendQQ, "QQ好友", View.FIND_VIEWS_WITH_TEXT);
//                if (friendQQ != null && friendQQ.size() > 0) {
//                    oldList =getAllChildViews(getActivity().getWindow().getDecorView());
//                    friendQQ.get(0).setVisibility(View.GONE);
//                }
//
//                ArrayList<View> friendQuan = new ArrayList<View>();
//                getActivity().getWindow().getDecorView().findViewsWithText(friendQuan, "QQ空间", View.FIND_VIEWS_WITH_TEXT);
//                if (friendQuan != null && friendQuan.size() > 0) {
//                    oldList =getAllChildViews(getActivity().getWindow().getDecorView());
//                    friendQuan.get(0).setVisibility(View.GONE);
//                }
//            }
//        });
//    }
//
//    private List<View> getAllChildViews(View view) {
//        List<View> allchildren = new ArrayList<View>();
//        if (view instanceof ViewGroup) {
//            ViewGroup vp = (ViewGroup) view;
//            for (int i = 0; i < vp.getChildCount(); i++) {
//                View viewchild = vp.getChildAt(i);
//                allchildren.add(viewchild);
//                allchildren.addAll(getAllChildViews(viewchild));
//            }
//        }
//        return allchildren;
//    }


    @Override
    public void viewBeShow() {
        super.viewBeShow();
        if (zipResourceDownload != null) {
            zipResourceDownload.initZipResource();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (zipResourceDownload != null) {
            zipResourceDownload.closeDilaog();
        }
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
        getPresenter().introduceNavigation(String.valueOf(WebViewConstant.Navigation.HEALTH_INTRODUCTION_PAGE));
    }
}