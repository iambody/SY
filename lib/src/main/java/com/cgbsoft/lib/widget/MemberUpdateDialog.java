package com.cgbsoft.lib.widget;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cgbsoft.lib.R;
import com.cgbsoft.lib.base.model.bean.MemeberInfo;
import com.cgbsoft.lib.utils.tools.CollectionUtils;
import com.cgbsoft.lib.utils.tools.DimensionPixelUtil;
import com.cgbsoft.lib.utils.tools.ViewUtils;
import com.cgbsoft.lib.widget.dialog.BaseDialog;

import java.util.List;

/**
 * @author chenlong
 */
public abstract class MemberUpdateDialog extends BaseDialog {
    private Context _context;
    private Resources resources;
    private int screenWidth;
    private TextView memberLevel;
    private TextView memberValue;
    private LinearLayout memberContent;
    private TextView lookMember;
    private LayoutInflater layoutInflater;
    private ImageView closeImage;

    public MemberUpdateDialog(Context context) {
        this(context, R.style.ios_dialog_alpha);
        layoutInflater = LayoutInflater.from(context);
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
    }

    public MemberUpdateDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_update_prompt_dialog);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        init();
    }

    public void updateDialogUi(MemeberInfo memeberInfo) {
        if (memeberInfo == null) {
            return;
        }
        if (!TextUtils.isEmpty(memeberInfo.getLevel()) && memeberInfo.getLevel().length() >2) {
            ViewUtils.scaleUserAchievment(memberLevel, memeberInfo.getLevel(), 0 , 2, 1.5f);
        }
        memberValue.setText(memeberInfo.getCurrentWealthNumber());
        for (MemeberInfo.MemeberProject memeberProject : memeberInfo.getItem()) {
            View itemView = layoutInflater.inflate(R.layout.item_dialog_member_update, null);
            TextView nameText = (TextView) itemView.findViewById(R.id.name);
            LinearLayout linearLayout = (LinearLayout) itemView.findViewById(R.id.content_ll);
            nameText.setText(memeberProject.getProjectType());
            createContentView(memeberProject.getProjectList(), linearLayout);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.topMargin = 20;
            itemView.setLayoutParams(layoutParams);
            memberContent.addView(itemView);
        }
    }

    private void createContentView(List<MemeberInfo.MemeberItemProject> list, LinearLayout contentLayout) {
        if (!CollectionUtils.isEmpty(list)) {
            for (int i = 0; i < list.size(); i++) {
                MemeberInfo.MemeberItemProject memeberItemProject = list.get(i);
                View itemView = layoutInflater.inflate(R.layout.memeber_project_item_flag, null);
                TextView nameText = (TextView) itemView.findViewById(R.id.project_name);
                TextView stepText = (TextView) itemView.findViewById(R.id.project_step);
                nameText.setText(memeberItemProject.getProjectName());
                if (TextUtils.isEmpty(memeberItemProject.getFrequencyInfo())) {
                    stepText.setVisibility(View.GONE);
                } else {
                    stepText.setText(memeberItemProject.getFrequencyInfo());
                }
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                if (i != list.size() -1) {
                    layoutParams.rightMargin = 30;
                }
                layoutParams.height = DimensionPixelUtil.dp2px(getContext(), 58);
//                int parentWidth = contentLayout.getWidth();
//                System.out.println("-----parentWidth=" + parentWidth);
//                layoutParams.width =  (parentWidth - 60)/3;
                itemView.setLayoutParams(layoutParams);
                contentLayout.addView(itemView);
            }
        }
    }

    public void init() {
        memberLevel = (TextView)findViewById(R.id.member_level);
        memberValue = (TextView)findViewById(R.id.member_value);
        memberContent = (LinearLayout) findViewById(R.id.show_content_ll );
        lookMember = (TextView)findViewById(R.id.lookMember);
        lookMember.setOnClickListener(v -> buttonClick());
        closeImage = (ImageView) findViewById(R.id.close);
        closeImage.setOnClickListener(v -> dismiss());
    }

    public abstract void buttonClick();
}
