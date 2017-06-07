package com.commui.prompt.mvp.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.webview.CwebNetConfig;
import com.cgbsoft.lib.base.webview.WebViewConstant;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.lib.widget.dialog.BaseDialog;
import com.cgbsoft.privatefund.bean.share.NewsBean;
import com.google.gson.Gson;

import org.json.JSONObject;

import app.privatefund.com.cmmonui.R;

/**
 * desc  签到弹窗
 * Created by yangzonghui on 2017/5/11 16:53
 * Email:yangzonghui@simuyun.com
 *  
 */
public class SignInDialog extends BaseDialog {

    private NewsBean newsBean;

    public SignInDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public SignInDialog(Context context, int theme) {
        super(context, theme);
    }

    public SignInDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_sign);
        bindViews();
        initViews();
    }

    private JSONObject data;

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    public void initViews() {
        try {

            final JSONObject j = data.getJSONObject("result");
            if (null == j) {
                return;
            }
            String signInDate = j.getString("signInDate");
            mTitle.setText(signInDate + "签到");

            String todayPoint = j.getString("todayPoint");
            double Multiple = j.getDouble("Multiple");
            int point = j.getInt("point");
            if (Multiple == 1.0) {
                mContent.setText("获得了" + todayPoint + "个云豆");
            } else {
                mContent.setText("获得了" + todayPoint + "（" + point + " X " + Multiple + "）个云豆");
            }
            final String infoTitle = j.getString("infoTitle");
            title_id.setText(infoTitle);
            title_id.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        JSONObject information = j.getJSONObject("information");
                        Gson g = new Gson();
                        newsBean = g.fromJson(information.toString(), NewsBean.class);
                        openWebPage();

                        SignInDialog.this.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void openWebPage() {
        //TODO 打开早知道
        if (AppManager.isInvestor(getContext())) {
            NavigationUtils.startVideoInformationActivityu(getContext(), CwebNetConfig.baseParentUrl + "/apptie/new_detail_toc.html?id=" + newsBean.getInfoId() + "&category=4", newsBean.getTitle());
            //newsBean.setUrl(Domain.foundNews + newsBean.getInfoId() + "&category=" + newsBean.getCategory());
//                            Intent i = new Intent(getContext(), FoundNewsDetailActivity.class);
//                            Bundle bundle = new Bundle();
//                            bundle.putSerializable(FoundNewsDetailActivity.NEW_PARAM_NAME, newsBean);
//                            i.putExtras(bundle);
//                            getContext().startActivity(i);
        } else {

//                            Intent i = new Intent(getContext(), PushMsgActivity.class);
//                            i.putExtra(Contant.push_message_url, Domain.baseWebsite + "/apptie/new_detail_toc.html?id=" +newsBean.getInfoId()+"&category=4");
//                            i.putExtra(Contant.push_message_title, "早知道");
//                            i.putExtra(Contant.RIGHT_SAVE, false);
//                            i.putExtra(Contant.RIGHT_SHARE, true);
//                            i.putExtra(Contant.PAGE_INIT, false);
//                            getContext().startActivity(i);

        }
    }

    private TextView mTitle;
    private TextView mContent, title_id;
    private TextView mClose;

    // End Of Content View Elements

    private void bindViews() {

        mTitle = (TextView) findViewById(R.id.title);
        mContent = (TextView) findViewById(R.id.content);
        title_id = (TextView) findViewById(R.id.title_id);
        mClose = (TextView) findViewById(R.id.close);
        mClose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                cancel();
            }
        });
    }
}
