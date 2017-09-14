package com.commui.prompt.mvp.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.base.webview.WebViewConstant;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.utils.tools.DataStatistApiParam;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.lib.utils.tools.NetUtils;
import com.cgbsoft.lib.utils.tools.PromptManager;
import com.cgbsoft.lib.widget.dialog.LoadingDialog;
import com.cgbsoft.lib.widget.recycler.SimpleItemDecoration;
import com.cgbsoft.privatefund.bean.commui.DayTaskBean;
import com.chenenyu.router.annotation.Route;
import com.commui.prompt.mvp.adapter.MyTaskAdapter;
import com.commui.prompt.mvp.contract.MyTaskContract;
import com.commui.prompt.mvp.listener.MyTaskListener;
import com.commui.prompt.mvp.model.MyTaskBean;
import com.commui.prompt.mvp.presenter.MyTaskPresenter;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import app.privatefund.com.cmmonui.R;
import app.privatefund.com.cmmonui.R2;
import butterknife.BindView;
import butterknife.OnClick;

/**
 *
 */
@Route(RouteConfig.INVTERSTOR_MAIN_TASK)
public class MyTaskActivity extends BaseActivity<MyTaskPresenter> implements MyTaskContract.View, MyTaskListener {
    @BindView(R2.id.title_left)
    ImageView back;
    @BindView(R2.id.title_mid)
    TextView titleTV;
    @BindView(R2.id.rv_adt)
    RecyclerView rcv_commui_task;
    MyTaskAdapter adapter;

    @BindView(R2.id.fragment_videoschool_noresult)
    ImageView fragmentVideoschoolNoresult;
    @BindView(R2.id.fragment_videoschool_noresult_lay)
    LinearLayout fragmentVideoschoolNoresultLay;


    private Comparator comparator;
    private boolean isFromC;
    private LoadingDialog mLoadingDialog;

    @OnClick(R2.id.title_left)
    public void clickBack() {
        this.finish();
    }
    @Override
    protected int layoutID() {
        return R.layout.activity_my_task;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mLoadingDialog = LoadingDialog.getLoadingDialog(baseContext, "", false, false);
        back.setVisibility(View.VISIBLE);
        titleTV.setText(getResources().getString(R.string.task_bonus));
        isFromC = AppManager.isInvestor(getApplicationContext());
        adapter = new MyTaskAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(baseContext);
        rcv_commui_task.setLayoutManager(linearLayoutManager);
        rcv_commui_task.addItemDecoration(new SimpleItemDecoration(baseContext,R.color.app_divider_gary, R.dimen.ui_1_dip,20));
        rcv_commui_task.setAdapter(adapter);
        rcv_commui_task.setHasFixedSize(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPresenter().getTaskList();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart(Constant.SXY_RENWU);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd(Constant.SXY_RENWU);
    }

    @Override
    protected void after() {
        super.after();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    @Override
    protected void data() {
        super.data();

//        ViewGroup.LayoutParams lp = iv_adt_bg.getLayoutParams();
//        lp.width = Utils.getScreenWidth(this);
//        lp.height = lp.width * 321 / 750;
//        iv_adt_bg.setLayoutParams(lp);

    }

    @Override
    protected MyTaskPresenter createPresenter() {
        return new MyTaskPresenter(this, this);
    }

    @Override
    public void showLoadDialog() {
        if (mLoadingDialog.isShowing()) {
            return;
        }
        mLoadingDialog.show();
    }

    @Override
    public void hideLoadDialog() {
        mLoadingDialog.dismiss();
    }

    /**
     * 请求任务列表成功
     * @param list
     */
    @Override
    public void getTaskLitSuc(ArrayList<DayTaskBean> list) {
        Collections.sort(list);
        DayTaskBean dayTaskBean = new DayTaskBean();
        dayTaskBean.type = MyTaskBean.ISHEADER;
        dayTaskBean.setStatus("-1");
        dayTaskBean.setTaskName("列表头部");
        list.add(0, dayTaskBean);
        adapter.refAllData(list);
        fragmentVideoschoolNoresultLay.setVisibility(View.GONE);
        rcv_commui_task.setVisibility(View.VISIBLE);
    }

    @Override
    public void getTaskListErr(Throwable error) {
        if (0 == adapter.getItemCount()) {
            fragmentVideoschoolNoresultLay.setVisibility(View.VISIBLE);
            rcv_commui_task.setVisibility(View.GONE);
        }
    }

    @Override
    public void finishTaskSuc(String id) {

    }

    @Override
    public void signSuc() {
//        TaskInfo.complentTask("每日签到");
        List<DayTaskBean> list = adapter.getList();
        for (DayTaskBean dayTaskBean : list) {
            if ("每日签到".equals(dayTaskBean.getTaskName())) {
                dayTaskBean.setStatus("1");
            }
        }
        Collections.sort(list);
//        adapter.refAllData(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void signErr(Throwable error) {
        Toast.makeText(baseContext.getApplicationContext(),"签到失败,请稍后再试!",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onErrorClickListener() {

    }

    @Override
    public void onItemClickListener(int position) {
        DayTaskBean model = adapter.getList().get(position);
        if (model.getStatus().equals("1")) {
            return;
        }
        switch (model.getTaskType()) {
            case MyTaskBean.ITEM_INFO_STR://查看资讯  增加需要跳转到C的页面
                infoTaskClick();
                break;
            case MyTaskBean.ITEM_PROD_STR:// 查看产品   增加需要跳转到C的页面
                productTaskClick();
                break;
            case MyTaskBean.ITEM_SHARE_INFO_STR://分享资讯   增加需要跳转到C的页面
                infoTaskClick();
                break;
            case MyTaskBean.ITEM_SHARE_PROD_STR:// 分享产品   增加需要跳转到C的页面
                productTaskClick();
                break;
            case MyTaskBean.ITEM_VIDEO_STR://跳转到学习视频    增加需要跳转到C的页面
                videoTaskClick();
                break;
            case MyTaskBean.ITEM_SIGN_STR://每日签到
                signTask();
                DataStatistApiParam.signInEveryDay();
                break;
        }
    }

    private void signTask() {
        getPresenter().finishTask(String.valueOf(MyTaskBean.ITEM_SIGN));
    }

    private void videoTaskClick() {
        if (isFromC) {
            NavigationUtils.jumpNativePage(this, WebViewConstant.Navigation.VIDEO_PAGE);
            this.finish();
        } else {

        }
        this.finish();
    }

    private void productTaskClick() {
        if (isFromC) {//跳转到查看产品页面
            NavigationUtils.jumpNativePage(this, WebViewConstant.Navigation.PRODUCT_PAGE);
            this.finish();
        } else {

        }
        this.finish();
    }

    private void infoTaskClick() {
        if (isFromC) {//C端的跳转-查看资讯 INFORMATION_PAGE
            NavigationUtils.jumpNativePage(this, WebViewConstant.Navigation.INFORMATION_PAGE);
            this.finish();
        } else {//原有B端的跳转

        }
        this.finish();
    }

    @OnClick(R2.id.fragment_videoschool_noresult)
    public void onViewnoresultClicked() {
        if (NetUtils.isNetworkAvailable(this)) {//有网
            if (adapter != null && adapter.getItemCount() == 0) {
                getPresenter().getTaskList();
            }
        } else {
            PromptManager.ShowCustomToast(this, getResources().getString(R.string.error_net));
        }
    }
}
