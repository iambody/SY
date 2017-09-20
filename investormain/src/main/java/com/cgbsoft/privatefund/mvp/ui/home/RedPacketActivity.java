package com.cgbsoft.privatefund.mvp.ui.home;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;

import com.cgbsoft.lib.base.webview.BaseWebview;
import com.cgbsoft.lib.contant.Contant;
import com.cgbsoft.privatefund.R;

public class RedPacketActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_red_packet);
        bindViews();
        String url = getIntent().getStringExtra(Contant.red_packet_url);
        mWebview.loadUrl(url);
    }


    private BaseWebview mWebview;

    private void bindViews() {
        mWebview = (BaseWebview) findViewById(R.id.webview);
        mWebview.setBackgroundColor(Color.TRANSPARENT);
        com.tencent.smtt.sdk.WebSettings settings = mWebview.getSettings();

        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(false);
    }

}
