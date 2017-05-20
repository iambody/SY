package app.privatefund.com.im.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.cgbsoft.lib.widget.dialog.BaseDialog;

import app.privatefund.com.im.R;
import io.rong.imkit.model.UIConversation;

/**
 * Created by lee on 2016/8/24.
 */
public abstract class ConversationMenuDialog extends BaseDialog {
    public ConversationMenuDialog(Context context, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public ConversationMenuDialog(Context context, int theme) {
        super(context, theme);
    }

    private UIConversation uiConversation;

    public ConversationMenuDialog(Context context, UIConversation uiConversation) {
        this(context, R.style.dialog_baobei);
        this.uiConversation = uiConversation;

    }

    private String targetId;

    public ConversationMenuDialog(Context context, UIConversation uiConversation, String targetId) {
        this(context, R.style.dialog_baobei);
        this.uiConversation = uiConversation;
        this.targetId = targetId;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_mid);
        bindViews();
        init();
    }

    private void init() {

        linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConversationMenuDialog.this.dismiss();
            }
        });

        set_nor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu1();
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu2();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu3();
            }
        });
    }

    private TextView set_nor;
    private TextView edit;
    private TextView delete;
    private LinearLayout linear;

    private void bindViews() {

        View line1 = (View) findViewById(R.id.line1);
        set_nor = (TextView) findViewById(R.id.set_nor);
        edit = (TextView) findViewById(R.id.edit);
        delete = (TextView) findViewById(R.id.delete);
        linear = (LinearLayout) findViewById(R.id.linear);

        if ("98331278d8e54370b68544fd7994de22".equals(targetId)) {
            set_nor.setVisibility(View.GONE);
            line1.setVisibility(View.GONE);
        }
        if (uiConversation.isTop()) {
            set_nor.setText("取消置顶");
        } else {
            set_nor.setText("置顶会话");
        }
    }

    public abstract void menu1();

    public abstract void menu2();

    public abstract void menu3();

}
