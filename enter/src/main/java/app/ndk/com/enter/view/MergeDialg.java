package app.ndk.com.enter.view;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.cgbsoft.lib.utils.tools.BStrUtils;
import com.cgbsoft.lib.widget.dialog.BaseDialog;

import app.ndk.com.enter.R;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/7/12-17:23
 */
public class MergeDialg extends BaseDialog implements View.OnClickListener {
    private MergeDialogListener mergeDialogListener;

    private Context pContext;
    private View baseView;
    private String phoneNumber;
    private TextView dialog_merge_content;

    private TextView dialog_merge_continue;
    private TextView dialog_merge_merge;
    private ImageView dialog_merge_back;

    String vas;
    String getVas="尊敬的投资人：您的手机号%s在此之前已注册过私享云APP，为了保障您的信息安全，建议您将两个账号进行合并处理。\n若要合并请您选择“我要合并“，客服人员会在工作日48小时内与您沟通相关事宜，在此期间您的APP仍可继续使用。若不需要合并请选择“继续使用“ 。 \n如有疑问，请拨打400-188-8848咨询";

    public MergeDialg(Context pContext, String phonenumber, MergeDialogListener dialogListener) {
        super(pContext, com.cgbsoft.lib.R.style.dialog_comment_style);
        this.pContext = pContext;
        this.phoneNumber = phonenumber;
//        vas = String.format(getContext().getResources().getString(R.string.account_merge_str), phoneNumber);
        vas = String.format(getVas, phoneNumber);
        mergeDialogListener = dialogListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseView = LayoutInflater.from(pContext).inflate(R.layout.dialog_merge_account, null);
        setContentView(baseView);
        initView();
    }

    private void initView() {
        initConfig();
        dialog_merge_back = (ImageView) findViewById(R.id.dialog_merge_back);
        dialog_merge_continue = (TextView) findViewById(R.id.dialog_merge_continue);
        dialog_merge_merge = (TextView) findViewById(R.id.dialog_merge_merge);

        dialog_merge_content = (TextView) findViewById(R.id.dialog_merge_content);
        BStrUtils.SetTxt(dialog_merge_content, vas);
        dialog_merge_continue.setOnClickListener(this);
        dialog_merge_merge.setOnClickListener(this);
        dialog_merge_back.setOnClickListener(this);
    }

    private void initConfig() {
        //配置信息
        WindowManager.LayoutParams wparams = getWindow().getAttributes();
        wparams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wparams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        wparams.gravity = Gravity.BOTTOM;
        getWindow().setAttributes(wparams);
        //开始初始化
        getWindow().setWindowAnimations(com.cgbsoft.lib.R.style.dialog_commont_anims_style);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.dialog_merge_back) {
            MergeDialg.this.dismiss();
        } else if (v.getId() == R.id.dialog_merge_continue) {
            if (null != mergeDialogListener) mergeDialogListener.left();
            MergeDialg.this.dismiss();
        } else if (v.getId() == R.id.dialog_merge_merge) {
            if (null != mergeDialogListener) mergeDialogListener.right();
            MergeDialg.this.dismiss();

        }

    }

    public interface MergeDialogListener {
        public void left();

        public void right();
    }
}
