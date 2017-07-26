package com.cgbsoft.privatefund.widget;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.cgbsoft.lib.AppInfStore;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.net.NetConfig;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.adapter.AddressSelectAdapter;
import com.chenenyu.router.annotation.Route;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 地址选择dialog
 *
 * @author chenlong
 */
@Route(RouteConfig.SELECT_ADDRESS)
public class SelectAddressActivity extends Activity {

    public TextView txtDialogTitle;

    public EditText txtDialogContent;

    public ListView listView;

    public Button btnOk;

    private AddressSelectAdapter addressSelectAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_select_address);
        txtDialogTitle = (TextView)findViewById(R.id.txt_dialog_title);
        txtDialogContent = (EditText)findViewById(R.id.txt_dialog_content);
        listView = (ListView) findViewById(R.id.list_view);
        btnOk = (Button) findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(txtDialogContent.getText().toString())) {
                AppInfStore.saveSelectAddress(this, txtDialogContent.getText().toString());
                NetConfig.updateRequestUrl();
                finish();
            }
        });
        bindView();
        initData();
        findViewById(R.id.contain).setOnTouchListener((v, event) -> {
            finish();
            overridePendingTransition(0, R.anim.fade_out);
            return false;
        });
    }


    private void bindView() {
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String addressChoose = (String) addressSelectAdapter.getItem(position);
            txtDialogContent.setText(addressChoose);
        });
    }

    private void initData() {
        String[] arrayList = getResources().getStringArray(R.array.address_select);
        List<String> list = Arrays.asList(arrayList);
        addressSelectAdapter = new AddressSelectAdapter(SelectAddressActivity.this, list);
        listView.setAdapter(addressSelectAdapter);
        addressSelectAdapter.notifyDataSetChanged();
    }
}
