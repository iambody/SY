package com.cgbsoft.privatefund.mvp.ui.center;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cgbsoft.lib.base.model.IndentityEntity;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.utils.tools.LogUtils;
import com.cgbsoft.lib.widget.dialog.LoadingDialog;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.adapter.IndentityAdapter;
import com.cgbsoft.privatefund.mvp.contract.center.SelectIndentityContract;
import com.cgbsoft.privatefund.mvp.presenter.center.SelectIndentityPresenterImpl;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by fei on 2017/8/11.
 */

public class SelectIndentityActivity extends BaseActivity<SelectIndentityPresenterImpl> implements SelectIndentityContract.SelectIndentityView{

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;
    @BindView(R.id.title_mid)
    TextView titleTV;
    @BindView(R.id.swipe_target)
    RecyclerView recyclerView;
    @BindView(R.id.bt_person_button)
    Button personButton;
    @BindView(R.id.bt_institution_button)
    Button institutionButton;
    @BindView(R.id.indentity_next)
    Button indentityNext;

    private IndentityAdapter indentityAdapter;

    private List<IndentityEntity.IndentityItem> datas=new ArrayList<>();
    private List<IndentityEntity.IndentityItem> leftItems =new ArrayList<>();
    private List<IndentityEntity.IndentityItem> rightItems =new ArrayList<>();
    private LoadingDialog mLoadingDialog;
    private boolean isLeftSelect =true;
    private String titleLeft;
    private String titleRight;
    private int currentPositionLeft=-1;
    private int currentpositionRight=-1;
    private boolean isInLand;
    private String indentityCode;

    @OnClick(R.id.indentity_next)
    public void nextButtonClick(){
        if (isInLand) {
            //去上传证件照
            Intent intent = new Intent(SelectIndentityActivity.this, UploadIndentityCradActivity.class);
            startActivity(intent);
        } else {
            //去证件列表
            Intent intent = new Intent(SelectIndentityActivity.this, CardCollectActivity.class);
            intent.putExtra("indentityCode",indentityCode);
            startActivity(intent);
        }
    }
    @OnClick(R.id.bt_person_button)
    public void personButtonClick(){
        if (isLeftSelect) {
            return;
        }
        isLeftSelect =true;
        personButton.setBackground(getResources().getDrawable(R.drawable.indentity_button_bg_press));
        personButton.setTextColor(getResources().getColor(R.color.white));
        institutionButton.setBackground(getResources().getDrawable(R.drawable.indentity_button_bg_normal));
        institutionButton.setTextColor(getResources().getColor(R.color.black));
        if (leftItems.size() == 0) {
            getPresenter().getIndentityList();
        } else {
            datas.clear();
            datas.addAll(leftItems);
            indentityAdapter.setCheckPosition(currentPositionLeft);
            indentityAdapter.notifyDataSetChanged();
        }
    }
    @OnClick(R.id.bt_institution_button)
    public void institutionButtonClick(){
        if (!isLeftSelect) {
            return;
        }
        isLeftSelect =false;
        institutionButton.setBackground(getResources().getDrawable(R.drawable.indentity_button_bg_press));
        institutionButton.setTextColor(getResources().getColor(R.color.white));
        personButton.setBackground(getResources().getDrawable(R.drawable.indentity_button_bg_normal));
        personButton.setTextColor(getResources().getColor(R.color.black));
        if (rightItems.size() == 0) {
            getPresenter().getIndentityList();
        } else {
            datas.clear();
            datas.addAll(rightItems);
            indentityAdapter.setCheckPosition(currentpositionRight);
            indentityAdapter.notifyDataSetChanged();
        }
    }
    @Override
    protected int layoutID() {
        return R.layout.activity_select_indentity;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        titleTV.setText(getResources().getString(R.string.indentity_select_str));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(com.cgbsoft.lib.R.drawable.ic_back_black_24dp);
        toolbar.setNavigationOnClickListener(v -> finish());
        initView(savedInstanceState);
    }

    private void initView(Bundle savedInstanceState) {
        mLoadingDialog = LoadingDialog.getLoadingDialog(this, "", false, false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(baseContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        indentityAdapter = new IndentityAdapter(this, datas);
        recyclerView.setAdapter(indentityAdapter);
        indentityAdapter.setOnItemClickListener(new IndentityAdapter.OnMyItemClickListener() {

            @Override
            public void click(int position,int currentPos) {
                if (isLeftSelect) {
                    currentPositionLeft = currentPos;
                    currentpositionRight=-1;
                } else {
                    currentPositionLeft=-1;
                    currentpositionRight=currentPos;
                }
                IndentityEntity.IndentityItem selectBean = datas.get(currentPos);
                indentityCode = selectBean.getCode();
                if (TextUtils.isEmpty(indentityCode)) {
                    return;
                }
                if (indentityCode.startsWith("1001")) {//大陆居民
                    isInLand=true;
                } else {//非大陆居民
                    isInLand=false;
                }
            }
        });
        getPresenter().getIndentityList();
    }

    @Override
    protected SelectIndentityPresenterImpl createPresenter() {
        return new SelectIndentityPresenterImpl(this,this);
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

    @Override
    public void getIndentityListSuccess(List<IndentityEntity.IndentityBean> indentityBeen) {
        if (null == indentityBeen || indentityBeen.size() == 0) {
            return;
        }
        this.leftItems.clear();
        this.rightItems.clear();
        for (int i=0;i<indentityBeen.size();i++) {
            IndentityEntity.IndentityBean bean = indentityBeen.get(i);
            if (i == 0) {
                titleLeft=bean.getTitle();
                this.leftItems.addAll(bean.getResult());
                personButton.setText(titleLeft);
            }
            if (i == 1) {
                titleRight = bean.getTitle();
                this.rightItems.addAll(bean.getResult());
                institutionButton.setText(titleRight);
            }
        }

        datas.clear();
        if (isLeftSelect) {
            datas.addAll(leftItems);
        } else {
            datas.addAll(rightItems);
        }
        indentityAdapter.notifyDataSetChanged();
    }

    @Override
    public void getIndentityListError(Throwable error) {
        Toast.makeText(getApplicationContext(),"获取数据失败",Toast.LENGTH_SHORT).show();
    }

}
