//package com.cgbsoft.privatefund.widget;
//
//import android.annotation.SuppressLint;
//import android.graphics.Color;
//import android.graphics.drawable.Drawable;
//import android.os.Bundle;
//import android.support.v4.content.ContextCompat;
//import android.text.TextUtils;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.cgbsoft.privatefund.R;
//import com.cgbsoft.privatefund.bean.CollectionBeen;
//import com.cgbsoft.privatefund.bean.MyTaskBean;
//import com.cgbsoft.privatefund.bean.NewsBean;
//import com.cgbsoft.privatefund.bean.RefreshUserinfo;
//import com.cgbsoft.privatefund.bean.ZixunInfo;
//import com.cgbsoft.privatefund.bean.share.BShare;
//import com.cgbsoft.privatefund.connect.ApiParams;
//import com.cgbsoft.privatefund.constant.Domain;
//import com.cgbsoft.privatefund.db.DatabaseUtils;
//import com.cgbsoft.privatefund.http.HttpResponseListener;
//import com.cgbsoft.privatefund.pop.MToast;
//import com.cgbsoft.privatefund.task.CoinTask;
//import com.cgbsoft.privatefund.task.SupportTask;
//import com.cgbsoft.privatefund.utils.DataStatisticsUtils;
//import com.cgbsoft.privatefund.view.dialog.CommonShareDialog;
//import com.cgbsoft.privatefund.view.dialog.ShareYaoqinDialog;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.HashMap;
//
//import de.greenrobot.event.EventBus;
//
///**
// * 资讯详情页面
// *
// * @author lee
// */
//public class FoundNewsDetailActivity extends BaseActivity {
//
//    public static final String NEW_PARAM_NAME = "found_new_param";
//    public static final String NEW_DETAIL_FLAG = "found_new_detail_flag";
//    private LinearLayout mShare_layout;
//    private TextView mShare;
//    private LinearLayout shoucang_layout;
//    private LinearLayout dianzan_layout;
//    private TextView shoucangTextView;
//    private TextView agreeTextView;
//    private NewsBean newsBean;
//    private String url;
//    private String shareurl;
//    private DatabaseUtils databaseUtils;
//
//    /**
//     * 公用 的share 弹出框
//     * @param arg0
//     */
//    private CommonShareDialog commonShareDialog;
//    @Override
//    protected void onCreate(Bundle arg0) {
//        super.onCreate(arg0);
//        setContentView(R.layout.acitivity_foundnews_detail);
//        databaseUtils = new DatabaseUtils(this);
//        newsBean = (NewsBean) getIntent().getSerializableExtra(NEW_PARAM_NAME);
//        bindViews();
//        initData();
//        // 点击分享
//        mShare.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                share();
//                MyShare();
//                String[] param = new String[]{newsBean.getCategory(), MApplication.getUser().getToB().isColorCloud(), MApplication.getUser().getToB().getOrganizationName(), "分享"};
//                HashMap<String, String> params = DataStatisticsUtils.getParams("1018", "10093", param);
//                DataStatisticsUtils.push(FoundNewsDetailActivity.this, params);
////                 mShare_layout.setVisibility(View.INVISIBLE);
////                titleRight.setBackgroundResource(R.drawable.produ、盘ct_plus);
//            }
//        });
////        mShare_layout.setOnClickListener(new OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                mShare_layout.setVisibility(View.INVISIBLE);
////                titleRight.setBackgroundResource(R.drawable.product_plus);
////            }
////        });
//
//        dianzan_layout.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if ("1".equals(newsBean.getIsLike())) {
//                    new MToast(FoundNewsDetailActivity.this).show("你已经点赞该资讯", 0);
//                    return;
//                }
//                supportInfo();
//            }
//        });
//        shoucang_layout.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Drawable drawable = null;
//                if (databaseUtils.hadCollected(CollectionBeen.formatNewsBeanToCollectionBeen(newsBean))) {
//                    databaseUtils.delCollectionBean(CollectionBeen.formatNewsBeanToCollectionBeen(newsBean));
//                    drawable = ContextCompat.getDrawable(FoundNewsDetailActivity.this, R.drawable.shoucang_nor);
//                    new MToast(FoundNewsDetailActivity.this).show("你已取消收藏该资讯", 0);
//                } else {
//                    databaseUtils.saveCollectionBean(CollectionBeen.formatNewsBeanToCollectionBeen(newsBean));
//                    drawable = ContextCompat.getDrawable(FoundNewsDetailActivity.this, R.drawable.shoucang_down);
//                    new MToast(FoundNewsDetailActivity.this).show("你已收藏该资讯", 0);
//                }
//                showCompoundDrawable(shoucangTextView, drawable);
//                String[] param = new String[]{newsBean.getCategory(), MApplication.getUser().getToB().isColorCloud(), MApplication.getUser().getToB().getOrganizationName(), "收藏"};
//                HashMap<String, String> params = DataStatisticsUtils.getParams("1018", "10092", param);
//                DataStatisticsUtils.push(FoundNewsDetailActivity.this, params);
//            }
//        });
//        System.out.println("--------url=" + url);
//        mWebview.loadUrl(url);
//
//        DatabaseUtils databaseUtils = new DatabaseUtils(this);
//        MyTaskBean myTaskBean = databaseUtils.getMyTask("查看资讯");
//        if (myTaskBean != null && myTaskBean.getState() == 0) {
//            getCoinTask(databaseUtils, myTaskBean);
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mWebview.removeAllViews();
//        mWebview.destroy();
//    }
//
//    private void getCoinTask(final DatabaseUtils databaseUtils, final MyTaskBean myTaskBean) {
//        JSONObject js = new JSONObject();
//        try {
//            js.put("taskName", myTaskBean.getTaskName());
//            js.put("taskType", myTaskBean.getTaskType() + "");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        new CoinTask(context).start(js.toString(), new HttpResponseListener() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    String ratio = response.getString("ratio");
//                    int coinRatioNum = response.getInt("coinRatioNum");
//                    int coinNum = response.getInt("coinNum");
//                    if (ratio.equals("1.0")) {
//                        Toast.makeText(context, "完成【查看资讯】任务,获得" + coinRatioNum + "个云豆", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(context, "完成【查看资讯】任务,获得" + coinRatioNum + "（" + coinNum + " X " + ratio + "）个云豆", Toast.LENGTH_SHORT).show();
//                    }
//                    MApplication.getUser().setMyPoint(MApplication.getUser().getMyPoint() + coinRatioNum);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                myTaskBean.setState(2);
//                databaseUtils.updataMyTask(myTaskBean);
//                EventBus.getDefault().post(new RefreshUserinfo());
//            }
//
//            @Override
//            public void onErrorResponse(String error, int statueCode) {
//                try {
//                    JSONObject js = new JSONObject(error);
//                    String message = js.getString("message");
//                    if (message.contains("已领取过云豆")) {
//                        myTaskBean.setState(2);
//                        databaseUtils.updataMyTask(myTaskBean);
//                    }
////                    new MToast(context).show(message, 0);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
//
//    private String getTitleWithCategory(int category) {
//        switch (category) {
//            case 4:
//                return "早知道";
//            case 5:
//                return "投条号";
//            case 6:
//                return "大视野";
//            case 7:
//                return "名家谈";
//            default:
//                return "资讯";
//        }
//    }
//
//    @SuppressLint("StringFormatMatches")
//    private void initData() {
//        //showTileMid(newsBean.getTitle());
//        showTileMid(getTitleWithCategory(Integer.valueOf(newsBean.getCategory())));
//        url = Domain.foundNews + newsBean.getInfoId() + "&category=" + newsBean.getCategory();
//        shareurl = Domain.foundNewshare + newsBean.getInfoId() + "&category=" + newsBean.getCategory() + "&share=2";
//        Drawable drawable;
//        if (databaseUtils.hadCollected(CollectionBeen.formatNewsBeanToCollectionBeen(newsBean))) {
//            drawable = ContextCompat.getDrawable(this, R.drawable.shoucang_down);
//        } else {
//            drawable = ContextCompat.getDrawable(this, R.drawable.shoucang_nor);
//        }
//        showCompoundDrawable(shoucangTextView, drawable);
//
//        agreeTextView.setText(String.format(getString(R.string.support_number), newsBean.getLikes()));
//        Drawable supportDrawable;
//        if ("1".equals(newsBean.getIsLike())) {
//            supportDrawable = ContextCompat.getDrawable(this, R.drawable.dianzhan_down);
//        } else {
//            supportDrawable = ContextCompat.getDrawable(this, R.drawable.dianzhan_nor);
//        }
//        showCompoundDrawable(agreeTextView, supportDrawable);
//    }
//
//    public void onEventMainThread(ZixunInfo zixunInfo) {
//        if (newsBean != null && TextUtils.isEmpty(newsBean.getIsLike()) && zixunInfo != null) {
//            newsBean.setIsLike(zixunInfo.getIslike());
//            newsBean.setLikes(Integer.valueOf(zixunInfo.getLikes()));
//            newsBean.setSummary(zixunInfo.getSummary());
//            initData();
//        }
//    }
//
//    private void supportInfo() {
//        String params = ApiParams.requestParamSupport(newsBean.getInfoId());
//        new SupportTask(FoundNewsDetailActivity.this).start(params, new HttpResponseListener() {
//            @SuppressLint("StringFormatMatches")
//            @Override
//            public void onResponse(JSONObject response) {
//                int number = response.optInt("likes");
//                onClickSupportSuccess(String.valueOf(number));
//            }
//
//            @Override
//            public void onErrorResponse(String error, int statueCode) {
//                //new MToast(FoundNewsDetailActivity.this).show(error, 0);
//                if (!TextUtils.isEmpty(error) && error.contains("已赞")) {
//                    String vas = agreeTextView.getText().toString();
//                    vas = vas.substring(vas.length() -1);
//                    onClickSupportSuccess(vas);
//                }
//            }
//        });
//    }
//
//    private void onClickSupportSuccess(String number) {
//        agreeTextView.setText(String.format(getString(R.string.support_number), number));
//        Drawable drawable = ContextCompat.getDrawable(FoundNewsDetailActivity.this, R.drawable.dianzhan_down);
//        showCompoundDrawable(agreeTextView, drawable);
//        newsBean.setIsLike("1");
//        new MToast(FoundNewsDetailActivity.this).show("您已点赞成功!", Toast.LENGTH_SHORT);
//
//        String[] param = new String[]{newsBean.getCategory(), MApplication.getUser().getToB().isColorCloud(), MApplication.getUser().getToB().getOrganizationName(), "点赞"};
//        HashMap<String, String> params = DataStatisticsUtils.getParams("1018", "10091", param);
//        DataStatisticsUtils.push(FoundNewsDetailActivity.this, params);
//    }
//
//    private void showCompoundDrawable(TextView textView, Drawable drawable) {
//        if (textView != null) {
//            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//            textView.setCompoundDrawables(drawable, null, null, null);
//        }
//    }
//
//
//    /**
//     * commont分享
//     */
//    private void MyShare(){
//        String title = newsBean.getTitle();
//        String content = newsBean.getSummary();
//        String category = newsBean.getCategory();
//        int pic = R.drawable.logoshare;
//        if ("1".equals(category)) {
//            pic = R.drawable.sharehangyeziyun;
//        } else if ("2".equals(category)) {
//            pic = R.drawable.shareyunguancha;
//        } else if ("3".equals(category)) {
//            pic = R.drawable.shareyunshidian;
//        } else if ("4".equals(category)) {
//            pic = R.drawable.sharezaozhidao;
//        }
//
//        BShare BShare=new BShare(title, content, pic, shareurl, "链接" + shareurl, title, null, shareurl, "zixun",newsBean);
//        if(null!=commonShareDialog){commonShareDialog=null;}
//        commonShareDialog=new CommonShareDialog(FoundNewsDetailActivity.this, BShare, CommonShareDialog.Tag_Style_Inf_ZiXun, new CommonShareDialog.CommentShareListener() {
//            @Override
//            public void onclick() {
//
//            }
//        });
//        commonShareDialog.show();
//    }
//    /**
//     * 分享的逻辑
//     */
//    protected void share() {
//        ShareYaoqinDialog shareYaoqinDialog = new ShareYaoqinDialog(context, 2, "news");
//        String title = newsBean.getTitle();
//        String content = newsBean.getSummary();
//        String category = newsBean.getCategory();
//
//        int pic = R.drawable.logoshare;
//        if ("1".equals(category)) {
//            pic = R.drawable.sharehangyeziyun;
//        } else if ("2".equals(category)) {
//            pic = R.drawable.shareyunguancha;
//        } else if ("3".equals(category)) {
//            pic = R.drawable.shareyunshidian;
//        } else if ("4".equals(category)) {
//            pic = R.drawable.sharezaozhidao;
//        }
//
//        shareYaoqinDialog.setZixun(newsBean);
//        shareYaoqinDialog.setData(title, content, pic, shareurl, "链接" + shareurl, title, null, shareurl, "zixun");
//        shareYaoqinDialog.show();
//
//    }
//
//    private com.cgbsoft.privatefund.webview.MWebview mWebview;
//
//    private void bindViews() {
//        showTileLeft();
//        titleRight.setVisibility(View.GONE);
//        mShare_layout = (LinearLayout) findViewById(R.id.share_layout);
//        dianzan_layout = (LinearLayout) findViewById(R.id.dianzan_layout);
//        shoucangTextView = (TextView) findViewById(R.id.collect_textview);
//        agreeTextView = (TextView) findViewById(R.id.agree_textview);
//        mShare = (TextView) findViewById(R.id.share);
//        shoucang_layout = (LinearLayout) findViewById(R.id.shoucang_layout);
//        findViewById(R.id.main_main_container).setBackgroundColor(Color.WHITE);
//        mWebview = (com.cgbsoft.privatefund.webview.MWebview) findViewById(R.id.webview);
//    }
//}
