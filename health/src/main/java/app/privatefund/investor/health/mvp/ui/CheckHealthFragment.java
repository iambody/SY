package app.privatefund.investor.health.mvp.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.cgbsoft.lib.base.mvp.ui.BaseFragment;
import com.cgbsoft.lib.base.webview.WebViewConstant;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.lib.widget.recycler.RecyclerControl;
import com.dinuscxj.refresh.RecyclerRefreshLayout;

import java.util.HashMap;

import app.privatefund.investor.health.R;
import app.privatefund.investor.health.R2;
import app.privatefund.investor.health.adapter.CheckHealthAdapter;
import app.privatefund.investor.health.mvp.contract.HealthListContract;
import app.privatefund.investor.health.mvp.model.HealthListModel;
import app.privatefund.investor.health.mvp.presenter.HealthListPresenter;
import app.privatefund.investor.health.mvp.ui.listener.HealthListListener;
import butterknife.BindView;

/**
 * @author chenlong
 *
 */
public class CheckHealthFragment extends BaseFragment<HealthListPresenter> implements HealthListContract.View, HealthListListener, RecyclerControl.OnControlGetDataListListener, RecyclerRefreshLayout.OnRefreshListener{

    public static final String FROM_CHECK_HEALTH = "check_health";

    @BindView(R2.id.recyclerRefreshLayout)
    RecyclerRefreshLayout recyclerRefreshLayout;

    @BindView(R2.id.recyclerView)
    RecyclerView recyclerView;

    private RecyclerControl recyclerControl;
    private CheckHealthAdapter checkHealthAdapter;
    private LinearLayoutManager linearLayoutManager;
    private boolean isCheckHealth;

    @Override
    protected int layoutID() {
        return R.layout.fragment_healthlist;
    }

    @Override
    protected void before() {
        super.before();
        isCheckHealth = getArguments() != null && getArguments().getBoolean(FROM_CHECK_HEALTH, false);
    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {
        checkHealthAdapter = new CheckHealthAdapter(this, isCheckHealth);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerControl = new RecyclerControl(recyclerRefreshLayout, linearLayoutManager, this);
        recyclerRefreshLayout.setOnRefreshListener(this);
        recyclerRefreshLayout.setEnabled(false);
//        recyclerRefreshLayout.setRefreshView(new RefreshHeadView(getActivity()), new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new HealthItemDecoration(getActivity(), R.color.white, R.dimen.ui_15_dip));
        recyclerView.setAdapter(checkHealthAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnScrollListener(recyclerControl.getOnScrollListener());
        onRefresh();
    }

    @Override
    protected HealthListPresenter createPresenter() {
        return new HealthListPresenter(getActivity(), this, isCheckHealth);
    }

    @Override
    public void onRefresh() {
        recyclerControl.onRefresh();
    }

    @Override
    public void onVideoListItemClick(int position, ImageView imageView) {
        HealthListModel healthListModel = checkHealthAdapter.getList().get(position);
        HashMap<String ,Object> hashMap = new HashMap<>();
        hashMap.put(WebViewConstant.RIGHT_SHARE, true);
        hashMap.put(WebViewConstant.push_message_title, healthListModel.getTitle());
        hashMap.put(WebViewConstant.push_message_url, healthListModel.getUrl().concat("?healthId=").concat(healthListModel.getId()).concat("&healthImg=")
                .concat(healthListModel.getImageUrl()).concat("&healthTitle=").concat(healthListModel.getTitle()));
        NavigationUtils.startActivityByRouter(getActivity(), RouteConfig.GOTO_RIGHT_SHARE_ACTIVITY, hashMap);
    }

    @Override
    public void onControlGetDataList(boolean isRef) {
        getPresenter().getHealthList(checkHealthAdapter, isRef);
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

    }

    @Override
    public void requestDataSuccess(boolean isRef) {
        recyclerControl.getDataComplete(isRef);
        recyclerControl.setError(getActivity(), false, checkHealthAdapter, new HealthListModel(), "", R.drawable.bg_no_data);
    }

    @Override
    public void requestDataFailure(boolean isRef) {
        recyclerControl.getDataComplete(isRef);
        recyclerControl.setError(getActivity(), true, checkHealthAdapter, new HealthListModel());
    }

    @Override
    public void onErrorClickListener() {
        onRefresh();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        recyclerControl.destory();
    }
}
