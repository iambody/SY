package com.cgbsoft.privatefund.mvp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cgbsoft.lib.base.mvc.BaseMvcActivity;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.lib.base.model.bean.CredentialStateMedel;
import com.cgbsoft.privatefund.mvp.ui.center.UploadIndentityCradActivity;
import com.chenenyu.router.annotation.Route;

@Route(RouteConfig.CrenditralGuideActivity)
public class CrenditralGuideActivity extends BaseMvcActivity {


    Button jumpCrenditralDetial;
    private CredentialStateMedel credentialStateMedel;
    private TextView title;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crenditral_guide);
        title = (TextView) findViewById(R.id.title_mid);
        title.setText("证件夹");
        back = (ImageView) findViewById(R.id.iv_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        jumpCrenditralDetial = (Button) findViewById(R.id.jump_crenditral_detial);
        ImageView crenditralGuideImage = (ImageView) findViewById(R.id.crenditral_guide_image);

        credentialStateMedel = (CredentialStateMedel) getIntent().getSerializableExtra("credentialStateMedel");
        if ("10".equals(credentialStateMedel.getCustomerType())) {
            if ("1001".equals(credentialStateMedel.getCustomerIdentity())) {
                crenditralGuideImage.setBackgroundResource(R.drawable.guide100101);
            } else {
                crenditralGuideImage.setBackgroundResource(R.drawable.guide10010x);
            }
        } else {
            crenditralGuideImage.setBackgroundResource(R.drawable.guide20);
        }

        jumpCrenditralDetial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != credentialStateMedel) {
                    Intent intent = new Intent(CrenditralGuideActivity.this, UploadIndentityCradActivity.class);
                    intent.putExtra("credentialStateMedel", credentialStateMedel);
                    startActivity(intent);
                }
                finish();
            }
        });
    }
}
