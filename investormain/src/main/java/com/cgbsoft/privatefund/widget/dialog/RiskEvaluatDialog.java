package com.cgbsoft.privatefund.widget.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cgbsoft.lib.utils.tools.PromptManager;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.utils.MainNavigationUtils;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/22-21:23
 */
public class RiskEvaluatDialog extends BaseDialog {
    private TextView wenjuan;
    private LinearLayout ding;

    public RiskEvaluatDialog(Context dActivity) {
        super(dActivity, R.style.dialog_comment_style);
        this.baseContext = dActivity;
    }


    @Override
    protected int getViewResourceId() {
        return R.layout.dialog_riskevalust;
    }

    @Override
    protected void initView() {
        ding = (LinearLayout) baseDialogView.findViewById(R.id.ding);
        wenjuan = (TextView) baseDialogView.findViewById(R.id.wenjuan);
        RiskEvaluatDialog.this.setCanceledOnTouchOutside(true);
        wenjuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainNavigationUtils.startCommonWebActivity(baseContext);
                RiskEvaluatDialog.this.dismiss();
            }
        });
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        int x = (int) event.getX();
        final int y = (int) event.getY();

        int viewTop = ding.getTop();
        int viewBottom = ding.getBottom();
        int viewLeft = ding.getLeft();
        int viewRight = ding.getRight();

        if (x < viewLeft || x > viewRight || y < viewTop || y > viewBottom) {

            RiskEvaluatDialog.this.dismiss();
        }
        return true;
    }
}
