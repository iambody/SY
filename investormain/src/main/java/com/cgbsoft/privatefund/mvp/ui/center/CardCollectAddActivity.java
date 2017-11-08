package com.cgbsoft.privatefund.mvp.ui.center;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.cgbsoft.lib.base.model.CardListEntity;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.utils.constant.RxConstant;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.widget.dialog.LoadingDialog;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.adapter.CardListAdapter;
import com.cgbsoft.privatefund.bean.living.LivingResultData;
import com.cgbsoft.privatefund.bean.living.PersonCompare;
import com.cgbsoft.privatefund.model.CredentialStateMedel;
import com.cgbsoft.privatefund.mvp.contract.center.CardCollectContract;
import com.cgbsoft.privatefund.mvp.presenter.center.CardCollectPresenterImpl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import app.ocrlib.com.LivingManger;
import app.ocrlib.com.LivingResult;
import app.ocrlib.com.facepicture.FacePictureActivity;
import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;

import static com.cgbsoft.lib.utils.constant.RxConstant.SELECT_INDENTITY_ADD;

/**
 * Created by fei on 2017/8/15.
 */

public class CardCollectAddActivity extends BaseActivity<CardCollectPresenterImpl> implements CardCollectContract.CardCollectView, OnRefreshListener {

    private LoadingDialog mLoadingDialog;
    //    @BindView(R.id.toolbar)
//    protected Toolbar toolbar;
    @BindView(R.id.title_mid)
    TextView titleTV;
    @BindView(R.id.title_left)
    ImageView ivBack;
    @BindView(R.id.swipe_target)
    RecyclerView recyclerView;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mRefreshLayout;
    @BindView(R.id.iv_card_list_empty)
    RelativeLayout emptyBg;

    private List<CardListEntity.CardBean> datas = new ArrayList<>();
    private CardListAdapter adapter;
    private String indentityCode;
    private Observable<Integer> register;
    private Observable<PersonCompare> registerPerson;
    private CredentialStateMedel credentialStateMedel;
    private LivingManger livingManger;
    private CardListEntity.CardBean cardBean;
    private String TAG = "CardCollectAddActivity";

    @Override
    public void showLoadDialog() {
        try {
            if (mLoadingDialog.isShowing()) {
                return;
            }
            mLoadingDialog.show();
        } catch (Exception e) {
        }
    }

    @Override
    public void hideLoadDialog() {
        mLoadingDialog.dismiss();
    }

    @Override
    public void getCardListSuccess(List<CardListEntity.CardBean> cardBeans) {
        clodLsAnim(mRefreshLayout);
        datas.clear();
        datas.addAll(cardBeans);
        if (datas.size() == 0) {
            emptyBg.setVisibility(View.VISIBLE);
        } else {
            emptyBg.setVisibility(View.GONE);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void getCardListError(Throwable error) {
        clodLsAnim(mRefreshLayout);
    }

    @Override
    public void getLivingCountSuccess(String s) {
        try {
            JSONObject js = new JSONObject(s);
            JSONObject result = js.getJSONObject("result");
            String failCount = result.getString("failCount");
            //”0”:已过期。”1”:未过期。“2”：无历史 注意：没有活体验身历史的情况，返回空字符串。
            String validCode = result.getString("validCode");
            if ("0".equals(validCode)) {
                if ("3".equals(failCount)) {
                    Toast.makeText(this, "失败次数过多，", Toast.LENGTH_LONG).show();
                } else {
                    livingManger = new LivingManger(this, "100101", "1001", new LivingResult() {
                        @Override
                        public void livingSucceed(LivingResultData resultData) {
                            resultData.getRecognitionCode();
                            jumpDetial();
                        }

                        @Override
                        public void livingFailed(LivingResultData resultData) {

                        }

                    });
                    livingManger.startLivingMatch();
                }
            } else if ("1".equals(validCode)) {
                startMatchImg();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void startMatchImg() {
        startActivity(new Intent(this, FacePictureActivity.class).putExtra(FacePictureActivity.TAG_NEED_PERSON, true).putExtra(FacePictureActivity.PAGE_TAG, TAG));
    }

    private void jumpDetial() {
        credentialStateMedel = new CredentialStateMedel();
        credentialStateMedel.setCredentialCode(cardBean.getCode());
        credentialStateMedel.setCredentialState(cardBean.getStateCode());
        credentialStateMedel.setCredentialTypeName(cardBean.getName());
        credentialStateMedel.setCredentialStateName(cardBean.getStateName());
        credentialStateMedel.setCustomerIdentity(cardBean.getCode().substring(0, 4));
        credentialStateMedel.setCustomerType(cardBean.getCode().substring(0, 2));
        credentialStateMedel.setCredentialDetailId(cardBean.getId());
        Intent intent = new Intent(this, UploadIndentityCradActivity.class);

        if (null != credentialStateMedel) {
            intent.putExtra("credentialStateMedel", credentialStateMedel);
        }
        startActivity(intent);
    }

    @Override
    public void getLivingCountError(Throwable error) {

    }

    @Override
    protected int layoutID() {
        return R.layout.activity_cardcollect;
    }

    @OnClick(R.id.title_left)
    public void backClick() {
        this.finish();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ivBack.setVisibility(View.VISIBLE);
        mLoadingDialog = LoadingDialog.getLoadingDialog(this, "", false, false);
        titleTV.setText(getResources().getString(R.string.card_collect_add));
//        setSupportActionBar(toolbar);
//        toolbar.setNavigationIcon(com.cgbsoft.lib.R.drawable.ic_back_black_24dp);
//        toolbar.setNavigationOnClickListener(v -> finish());
        initView(savedInstanceState);
        initCallBack();
        register = RxBus.get().register(SELECT_INDENTITY_ADD, Integer.class);
        register.subscribe(new RxSubscriber<Integer>() {
            @Override
            protected void onEvent(Integer integer) {
                CardCollectAddActivity.this.finish();
            }

            @Override
            protected void onRxError(Throwable error) {

            }
        });
    }

    public void initCallBack() {
        registerPerson = RxBus.get().register(RxConstant.COMPLIANCE_PERSON_COMPARE, PersonCompare.class);
        registerPerson.subscribe(new RxSubscriber<PersonCompare>() {
            @Override
            protected void onEvent(PersonCompare personCompare) {
                if (TAG.equals(personCompare.getCurrentPageTag())){
                    //0代表成功 1代表失败  int值
                    if (0 == personCompare.getResultTage()) {
                        jumpDetial();
                    } else {

                    }
                }
            }

            @Override
            protected void onRxError(Throwable error) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != register) {
            RxBus.get().unregister(SELECT_INDENTITY_ADD, register);
        }
        if (null != registerPerson) {
            RxBus.get().unregister(RxConstant.COMPLIANCE_PERSON_COMPARE, registerPerson);
        }
    }

    private void initView(Bundle savedInstanceState) {
        indentityCode = getIntent().getStringExtra("indentityCode");
        mRefreshLayout.setLoadMoreEnabled(false);
        mRefreshLayout.setOnRefreshListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new CardListAdapter(datas, this);
        recyclerView.setAdapter(adapter);
        adapter.setItemClickListener(new CardListAdapter.CardListItemClick() {
            @Override
            public void itemClick(int position, CardListEntity.CardBean cardBean) {
                dealClick(cardBean);
            }
        });
    }


    private void dealClick(CardListEntity.CardBean cardBean) {
        this.cardBean = cardBean;
        if (cardBean.getCode().startsWith("1001") && (!cardBean.getCode().equals("100101")) && (!"10".equals(cardBean.getStateCode()))) {
            getPresenter().getLivingCount();
        } else {
            credentialStateMedel = new CredentialStateMedel();
            credentialStateMedel.setCredentialCode(cardBean.getCode());
            credentialStateMedel.setCredentialState(cardBean.getStateCode());
            credentialStateMedel.setCredentialTypeName(cardBean.getName());
            credentialStateMedel.setCredentialStateName(cardBean.getStateName());
            credentialStateMedel.setCustomerIdentity(cardBean.getCode().substring(0, 4));
            credentialStateMedel.setCustomerType(cardBean.getCode().substring(0, 2));
            credentialStateMedel.setCredentialDetailId(cardBean.getId());
            Intent intent = new Intent(this, UploadIndentityCradActivity.class);
//        intent.putExtra("credentialStateMedel", credentialStateMedel);
            if (null != credentialStateMedel) {
                intent.putExtra("credentialStateMedel", credentialStateMedel);
            } else {
            }

//        intent.putExtra("credentialCode", cardBean.getCode());
//        intent.putExtra("indentityCode", indentityCode);
//        intent.putExtra("firstUrl", firstUrl);
//        intent.putExtra("secondUrl", secondUrl);
//        intent.putExtra("stateCode", stateCode);
//        intent.putExtra("stateName", cardBean.getStateName());
//        intent.putExtra("title", cardBean.getName());
//        intent.putExtra("customerName", cardBean.getCustomerName());
//        intent.putExtra("customerNum", cardBean.getNumber());
//        intent.putExtra("depict", cardBean.getComment());
            startActivity(intent);
        }


//        this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPresenter().getCardListAdd(indentityCode);
    }

    @Override
    protected CardCollectPresenterImpl createPresenter() {
        return new CardCollectPresenterImpl(this, this);
    }

    @Override
    public void onRefresh() {
        getPresenter().getCardListAdd(indentityCode);
    }
}
