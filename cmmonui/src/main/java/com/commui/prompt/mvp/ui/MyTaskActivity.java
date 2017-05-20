package com.commui.prompt.mvp.ui;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.utils.tools.Utils;
import com.commui.prompt.mvp.adapter.MyTaskAdapter;
import com.commui.prompt.mvp.contract.MyTaskContract;
import com.commui.prompt.mvp.listener.MyTaskListener;
import com.commui.prompt.mvp.model.MyTaskBean;
import com.commui.prompt.mvp.presenter.MyTaskPresenter;

import java.util.ArrayList;
import java.util.Comparator;

import app.privatefund.com.cmmonui.R;
import app.privatefund.com.cmmonui.R2;
import butterknife.BindView;

public class MyTaskActivity extends BaseActivity<MyTaskPresenter> implements MyTaskContract.View, MyTaskListener {

    @BindView(R2.id.rv_adt)
    RecyclerView rcv_commui_task;
    @BindView(R2.id.iv_adt_back)
    ImageView img_task_back;
    @BindView(R2.id.tv_adt_bean)
    TextView tv_task_ydCount;
    private MyTaskAdapter adapter;
    @BindView(R2.id.iv_adt_bg)
    private ImageView iv_adt_bg;
    @BindView(R2.id.tv_adt_title)
    private TextView tv_adt_title;

    private Comparator comparator;
    private boolean isFromC;
    private GridLayoutManager gridLayoutManager;


    @Override
    protected int layoutID() {
        return R.layout.activity_my_task;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        isFromC = AppManager.isInvestor(getApplicationContext());
        adapter = new MyTaskAdapter(this, isFromC);
        gridLayoutManager = new GridLayoutManager(this, 1);
        rcv_commui_task.setLayoutManager(gridLayoutManager);
        rcv_commui_task.setAdapter(adapter);
        rcv_commui_task.setHasFixedSize(true);
        iv_adt_bg.setImageResource(isFromC ? R.drawable.sy_task_banner_c : R.drawable.sy_task_banner);
        img_task_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void data() {
        super.data();
        tv_adt_title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        tv_task_ydCount.setText(String.valueOf(123123));

        ViewGroup.LayoutParams lp = iv_adt_bg.getLayoutParams();
        lp.width = Utils.getScreenWidth(this);
        lp.height = lp.width * 321 / 750;
        iv_adt_bg.setLayoutParams(lp);

        comparator = new Comparator() {
            @Override
            public int compare(Object lhs, Object rhs) {
                MyTaskBean model1 = (MyTaskBean) lhs;
                MyTaskBean model2 = (MyTaskBean) rhs;

                if (model1.status >= model2.status) {
                    return 0;
                }
                return -1;
            }
        };

        getPresenter().getTaskList();
    }

    @Override
    protected MyTaskPresenter createPresenter() {
        return new MyTaskPresenter(this, this);
    }

    @Override
    public void getTaskLitSuc(ArrayList<MyTaskBean> list) {
        adapter.refAllData(list);
    }

    @Override
    public void finishTaskSuc(String id) {

    }

    @Override
    public void onErrorClickListener() {

    }

    @Override
    public void onItemClickListener(int position) {
        MyTaskBean model = adapter.getList().get(position);
        if (model.status == 1) {
            return;
        }
        switch (model.item_status) {
            case MyTaskBean.ITEM_INFO://查看资讯  增加需要跳转到C的页面
                infoTaskClick();
                break;
            case MyTaskBean.ITEM_PROD:// 查看产品   增加需要跳转到C的页面
                productTaskClick();

                break;
            case MyTaskBean.ITEM_SHARE_INFO://分享资讯   增加需要跳转到C的页面
                infoTaskClick();
                break;
            case MyTaskBean.ITEM_SHARE_PROD:// 分享产品   增加需要跳转到C的页面
                productTaskClick();
                break;
            case MyTaskBean.ITEM_VIDEO://跳转到学习视频    增加需要跳转到C的页面
                videoTaskClick();
                break;
            case MyTaskBean.ITEM_SIGN://每日签到
                signTask();
                break;
        }


    }

    private void signTask() {
        getPresenter().finishTask(String.valueOf(MyTaskBean.ITEM_SIGN));
    }

    private void videoTaskClick() {
        if (isFromC) {

        } else {

        }
        this.finish();
    }

    private void productTaskClick() {
        if (isFromC) {

        } else {

        }
        this.finish();
    }

    private void infoTaskClick() {
        if (isFromC) {//C端的跳转

        } else {//原有B端的跳转

        }
        this.finish();
    }
}
