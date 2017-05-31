package qcloud.liveold.mvp.views;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cgbsoft.lib.widget.RoundImageView;
import com.cgbsoft.lib.widget.dialog.BaseDialog;
import com.lidroid.xutils.BitmapUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import qcloud.liveold.R;

/**
 * desc
 * Created by yangzonghui on 2017/5/27 11:32
 * Email:yangzonghui@simuyun.com
 *  
 */
public abstract class LiveUserInfoDialog extends BaseDialog {
    public LiveUserInfoDialog(Context context, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public LiveUserInfoDialog(Context context, int theme) {
        super(context, theme);
    }

    public LiveUserInfoDialog(Context context) {
        super(context, R.style.dialog_baobei);
    }

    private String url;
    private String name;
    private Context context;

    public LiveUserInfoDialog(Context context, String url, String name) {
        this(context, R.style.dialog_baobei);
        this.context = context;
        this.url = url;
        this.name = name;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.live_user_dialog);
        bindViews();
        init();
    }

    private void init() {
        BitmapUtils bu = new BitmapUtils(context);
        bu.display(title_view, url);
        if (!TextUtils.isEmpty(name)) {
            Pattern p = Pattern.compile("^((13[0-9])|(14[5-7])|(15[0-9])|(17[0-8])|(18[0-9]))\\d{8}$");
            Matcher m = p.matcher(name);
            boolean b = m.matches();
            if (name.equals("null")) {
                name = "私募云用户";
            } else if (b) {
                name = "私募云用户";
            }
            name1.setText(name);
        } else {
            name1.setText("私募云用户");
        }

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                left();
            }
        });
    }

    private RoundImageView title_view;
    private TextView name1;
    private ImageButton close;

    private void bindViews() {

        title_view = (RoundImageView) findViewById(R.id.title_view);
        name1 = (TextView) findViewById(R.id.name);
        close = (ImageButton) findViewById(R.id.close);
    }

    public abstract void left();
}
