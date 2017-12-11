package com.cgbsoft.lib.widget;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cgbsoft.lib.R;
import com.cgbsoft.lib.base.model.bean.MemberDegrade;
import com.cgbsoft.lib.base.model.bean.MemeberInfo;
import com.cgbsoft.lib.utils.tools.CollectionUtils;
import com.cgbsoft.lib.widget.dialog.BaseDialog;

import java.util.List;

/**
 * @author chenlong
 */
public abstract class MemberDegradeDialog extends BaseDialog {
    private TextView memberDesc;
    private LinearLayout memberContent;
    private TextView lookMember;
    private LayoutInflater layoutInflater;
    private ImageView closeImage;

    public MemberDegradeDialog(Context context) {
        this(context, R.style.ios_dialog_alpha);
        layoutInflater = LayoutInflater.from(context);
    }

    public MemberDegradeDialog(Context context, int theme) {
        super(context, theme);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_degrade_prompt_dialog);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        init();
    }

    public void updateDialogUi(MemberDegrade memeberDegrade) {
        if (memeberDegrade == null) {
            return;
        }
        memberDesc.setText(memeberDegrade.getContent());
        for (String item : memeberDegrade.getMemberProfit()) {
            View itemView = layoutInflater.inflate(R.layout.item_dialog_member_degrade, null);
            TextView nameText = (TextView) itemView.findViewById(R.id.title_name);
            nameText.setText(item);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.topMargin = 10;
            itemView.setLayoutParams(layoutParams);
            memberContent.addView(itemView);
        }
    }

    public void init() {
        memberDesc = (TextView)findViewById(R.id.memberDesc);
        memberContent = (LinearLayout) findViewById(R.id.show_content_ll );
        lookMember = (TextView)findViewById(R.id.lookMember);
        lookMember.setOnClickListener(v -> buttonClick());
        closeImage = (ImageView) findViewById(R.id.close);
        closeImage.setOnClickListener(v -> dismiss());
    }

    public abstract void buttonClick();
}
