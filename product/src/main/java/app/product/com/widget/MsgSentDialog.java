package app.product.com.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cgbsoft.lib.widget.dialog.BaseDialog;

import app.product.com.R;

/**
 * Created by lee on 2016/6/15.
 */
public abstract class MsgSentDialog extends BaseDialog {

    private TextView personName;
    private TextView productName;
    private EditText shareExtra;
    private ImageView shareImg;
    private Button cancel;
    private Button send;

    public MsgSentDialog(Context context, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public MsgSentDialog(Context context, int theme) {
        super(context, theme);
    }

    public MsgSentDialog(Context context) {
        this(context, R.style.dialog_baobei);
    }

    private String product_name,person_name,product_type,product_id;

    public MsgSentDialog(Context context, String product_name, String person_name, String product_type, String product_id){
        this(context, R.style.dialog_baobei);
        this.product_name = product_name;
        this.product_type = product_type;
        this.person_name = person_name;
        this.product_id = product_id;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.msg_sent_dialog);
        bindViews();
        init();
    }

    private void init() {
        personName.setText(String.format("分享给：%s", person_name));
        productName.setText(product_name);
        if (product_type.equals("1")){
            shareImg.setBackgroundResource(R.drawable.mountain_large);
        }else if(product_type.equals("2")){
            shareImg.setBackgroundResource(R.drawable.rivers_large);
        }else {
            shareImg.setBackgroundResource(R.drawable.moren_large);
        }
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                left();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(shareExtra.getWindowToken(), 0);
                right(shareExtra.getText().toString());
            }
        });
    }

    private void bindViews() {
        personName = (TextView) findViewById(R.id.share_person_name);
        productName = (TextView) findViewById(R.id.share_product_name);
        shareExtra = (EditText) findViewById(R.id.share_extra);
        shareImg = (ImageView) findViewById(R.id.share_product_img);
        cancel = (Button) findViewById(R.id.quxiao);
        send = (Button) findViewById(R.id.send);
    }

    public abstract void left();

    public abstract void right(String extra);
}
