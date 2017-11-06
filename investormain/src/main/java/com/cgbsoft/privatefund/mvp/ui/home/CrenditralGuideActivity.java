package com.cgbsoft.privatefund.mvp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cgbsoft.lib.base.mvc.BaseMvcActivity;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.model.CredentialStateMedel;
import com.cgbsoft.privatefund.mvp.ui.center.UploadIndentityCradActivity;



public class CrenditralGuideActivity extends BaseMvcActivity {


    Button jumpCrenditralDetial;
    private CredentialStateMedel credentialStateMedel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crenditral_guide);
        jumpCrenditralDetial = (Button) findViewById(R.id.jump_crenditral_detial);
        credentialStateMedel = (CredentialStateMedel) getIntent().getSerializableExtra("credentialStateMedel");

        jumpCrenditralDetial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != credentialStateMedel) {
                    Intent intent = new Intent(CrenditralGuideActivity.this, UploadIndentityCradActivity.class);
                    intent.putExtra("credentialStateMedel", credentialStateMedel);
                    startActivity(intent);
                }
            }
        });
    }
}
