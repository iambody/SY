package com.cgbsoft.privatefund.mvp.ui.center;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.cgbsoft.lib.base.model.CardListEntity;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.constant.RxConstant;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.DataStatistApiParam;
import com.cgbsoft.lib.widget.dialog.LoadingDialog;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.adapter.CardListAdapter;
import com.cgbsoft.privatefund.bean.living.LivingResultData;
import com.cgbsoft.privatefund.bean.living.PersonCompare;
import com.cgbsoft.lib.base.model.bean.CredentialStateMedel;
import com.cgbsoft.privatefund.mvp.contract.center.CardCollectContract;
import com.cgbsoft.privatefund.mvp.presenter.center.CardCollectPresenterImpl;
import com.cgbsoft.privatefund.mvp.ui.home.CrenditralGuideActivity;
import com.chenenyu.router.annotation.Route;

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

/**
 * Created by fei on 2017/8/10.
 */
@Route(RouteConfig.CardCollectActivity)
public class CardCollectActivity extends BaseActivity<CardCollectPresenterImpl> implements CardCollectContract.CardCollectView, OnRefreshListener {
    public static final String TAG = "CardCollectActivity";
    //    @BindView(R.id.toolbar)
//    protected Toolbar toolbar;
    @BindView(R.id.title_mid)
    TextView titleTV;
    @BindView(R.id.title_left)
    ImageView back;
    @BindView(R.id.iv_title_right)
    ImageView ivRight;
    @BindView(R.id.swipe_target)
    RecyclerView recyclerView;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mRefreshLayout;
    private LoadingDialog mLoadingDialog;
    private List<CardListEntity.CardBean> datas = new ArrayList<>();
    private CardListAdapter adapter;
    private CredentialStateMedel credentialStateMedel;
    private String indentityCode;
    private LivingManger livingManger;
    private CardListEntity.CardBean cardBean;
    private Observable<PersonCompare> register;

    @OnClick(R.id.title_left)
    public void backClick() {
        this.finish();
    }

    @OnClick(R.id.iv_title_right)
    public void addCard() {
        Intent intent = new Intent(this, CardCollectAddActivity.class);
        intent.putExtra("indentityCode", indentityCode);
        startActivity(intent);
        DataStatistApiParam.cardCollectPlus();
    }

    @Override
    protected int layoutID() {
        return R.layout.activity_cardcollect;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        back.setVisibility(View.VISIBLE);
//        credentialStateMedel = (CredentialStateMedel) getIntent().getSerializableExtra("credentialStateMedel");
        indentityCode = getIntent().getStringExtra("indentityCode");
        if ("1001".equals(indentityCode)) {
            ivRight.setVisibility(View.VISIBLE);
            ivRight.setImageDrawable(getResources().getDrawable(R.drawable.card_list_add_selector));
        }
        titleTV.setText(getResources().getString(R.string.card_collect));
        initView(savedInstanceState);
    }

    private void initView(Bundle savedInstanceState) {
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setLoadMoreEnabled(false);
        mLoadingDialog = LoadingDialog.getLoadingDialog(this, "", false, false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new CardListAdapter(datas, this);
        recyclerView.setAdapter(adapter);
        initCallBack();
        adapter.setItemClickListener(new CardListAdapter.CardListItemClick() {
            @Override
            public void itemClick(int position, CardListEntity.CardBean cardBean) {
                goToUploadPage(cardBean);
                DataStatistApiParam.cardCollectClick(cardBean.getCode());
            }
        });
    }

    private void initCallBack() {
        register = RxBus.get().register(RxConstant.COMPLIANCE_PERSON_COMPARE, PersonCompare.class);
        register.subscribe(new RxSubscriber<PersonCompare>() {
            @Override
            protected void onEvent(PersonCompare personCompare) {
                if (TAG.equals(personCompare.getCurrentPageTag())) {
                    //0代表成功 1代表失败  int值
                    if (0 == personCompare.getResultTage()) {
                        jumpDetial();
                        Toast.makeText(baseContext,"身份验证通过",Toast.LENGTH_LONG).show();
                        DataStatistApiParam.sensitiveBodyExam(cardBean.getCode(),"成功","拍照");

                    } else {
                        Toast.makeText(baseContext,"识别失败，请点击重试",Toast.LENGTH_LONG).show();
                        DataStatistApiParam.sensitiveBodyExam(cardBean.getCode(),"失败","拍照");

                    }
                }
            }

            @Override
            protected void onRxError(Throwable error) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPresenter().getCardList(indentityCode);
    }

    /**
     * 点击条目跳转到上传照片页面
     *
     * @param cardBean
     */
    private void goToUploadPage(CardListEntity.CardBean cardBean) {
        this.cardBean = cardBean;

        String stateCode = cardBean.getStateCode();
//        if (!"5".equals(stateCode)) {//证件审核状态code码：5：未上传；10：审核中；30：已驳回；50：已通过；70：已过期
//            List<CardListEntity.ImageBean> images = cardBean.getImageUrl();
//            firstUrl=images.get(0).getUrl();
//            if (images.size() == 2) {
//                secondUrl=images.get(1).getUrl();
//            }
//        }
        credentialStateMedel = new CredentialStateMedel();
        credentialStateMedel.setCredentialCode(cardBean.getCode());
        credentialStateMedel.setCredentialState(cardBean.getStateCode());
        credentialStateMedel.setCredentialTypeName(cardBean.getName());
        credentialStateMedel.setCredentialStateName(cardBean.getStateName());
        credentialStateMedel.setCustomerIdentity(cardBean.getCode().substring(0, 4));
        credentialStateMedel.setCustomerType(cardBean.getCode().substring(0, 2));
        credentialStateMedel.setCredentialDetailId(cardBean.getId());
        //10（审核中）  50（通过）--》   大陆进  详情
        // 大陆证件非身份证状态不等于
        //
        if ("10".equals(cardBean.getStateCode())) {
            Intent intent = new Intent(this, UploadIndentityCradActivity.class);
            intent.putExtra("credentialStateMedel", credentialStateMedel);
            startActivity(intent);
        } else if ("50".equals(cardBean.getStateCode())) {
            if ("0".equals(credentialStateMedel.getCustomerImageState())){
                Intent intent = new Intent(baseContext, CrenditralGuideActivity.class);
                intent.putExtra("credentialStateMedel", credentialStateMedel);
                startActivity(intent);
            }else {
                Intent intent = new Intent(this, UploadIndentityCradActivity.class);
                intent.putExtra("credentialStateMedel", credentialStateMedel);
                startActivity(intent);
            }
        } else {
            if (cardBean.getCode().startsWith("1001")) {  //大陆需要上传其他证件  先判断次数
                getPresenter().getLivingCount();
//                Intent intent = new Intent(this, UploadIndentityCradActivity.class);
//                intent.putExtra("credentialStateMedel", credentialStateMedel);
//                startActivity(intent);
            } else {
                Intent intent = new Intent(baseContext, CrenditralGuideActivity.class);
                intent.putExtra("credentialStateMedel", credentialStateMedel);
                startActivity(intent);
            }
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


    }

    @Override
    protected CardCollectPresenterImpl createPresenter() {
        return new CardCollectPresenterImpl(this, this);
    }

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
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != livingManger)
            livingManger.destory();
        if (null != register)
            RxBus.get().unregister(RxConstant.COMPLIANCE_PERSON_COMPARE, register);
    }

    @Override
    public void getCardListError(Throwable error) {
        clodLsAnim(mRefreshLayout);
    }

    /**
     * 非大陆审核通过  判断有无person
     * 大陆需要上传其他证件  先判断次数
     *
     * @param s
     */
    @Override
    public void getLivingCountSuccess(String s) {
        Log.i("aaasasasa",s);
        try {
            JSONObject js = new JSONObject(s);
            JSONObject result = js.getJSONObject("result");
            String failCount = result.getString("failCount");
            //”0”:已过期。”1”:未过期。“2”：无历史 注意：没有活体验身历史的情况，返回空字符串。
            String validCode = result.getString("validCode");
            if ("0".equals(validCode)) {
                if ("3".equals(failCount)) {
                    Toast.makeText(this, "非常抱歉，您今日的人脸核身次数超过限制，请明日尝试", Toast.LENGTH_LONG).show();
                } else {
                    livingManger = new LivingManger(this, "100101", "1001", new LivingResult() {
                        @Override
                        public void livingSucceed(LivingResultData resultData) {
                            switch (resultData.getRecognitionCode()) {
                                case "0":
                                    resultData.getRecognitionCode();
                                    jumpDetial();
                                    DataStatistApiParam.sensitiveBodyExam(cardBean.getCode(),"成功","活体");

                                    break;
                                case "1":
                                    Toast.makeText(baseContext, "识别失败。", Toast.LENGTH_LONG).show();
                                    DataStatistApiParam.sensitiveBodyExam(cardBean.getCode(),"失败","活体");
//                                NavigationUtils.startActivity(DatumManageActivity.this, RiskEvaluationActivity.class);
                                    break;
                                case "2":
                                    DataStatistApiParam.sensitiveBodyExam(cardBean.getCode(),"失败","活体");
                                    break;
                                case "3":
                                    Toast.makeText(baseContext, "识别失败。", Toast.LENGTH_LONG).show();
                                    DataStatistApiParam.sensitiveBodyExam(cardBean.getCode(),"失败","活体");
                                    break;
                            }

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

    private void jumpDetial() {
        credentialStateMedel = new CredentialStateMedel();
        credentialStateMedel.setCredentialCode(cardBean.getCode());
        credentialStateMedel.setCredentialState(cardBean.getStateCode());
        credentialStateMedel.setCredentialTypeName(cardBean.getName());
        credentialStateMedel.setCredentialStateName(cardBean.getStateName());
        credentialStateMedel.setCustomerIdentity(cardBean.getCode().substring(0, 4));
        credentialStateMedel.setCustomerType(cardBean.getCode().substring(0, 2));
        credentialStateMedel.setCredentialDetailId(cardBean.getId());
        Intent intent = new Intent(CardCollectActivity.this, UploadIndentityCradActivity.class);
        if (null != credentialStateMedel) {
            intent.putExtra("credentialStateMedel", credentialStateMedel);
        }
        startActivity(intent);
    }

    private void startMatchImg() {
        startActivity(new Intent(this, FacePictureActivity.class).putExtra(FacePictureActivity.TAG_NEED_PERSON, true).putExtra(FacePictureActivity.PAGE_TAG, TAG));
    }

    @Override
    public void getLivingCountError(Throwable error) {

    }

    @Override
    public void onRefresh() {
        getPresenter().getCardList(indentityCode);
    }
}
