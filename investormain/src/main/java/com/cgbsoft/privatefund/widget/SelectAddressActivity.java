package com.cgbsoft.privatefund.widget;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.cgbsoft.lib.AppInfStore;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.contant.RouteConfig;
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
public class SelectAddressActivity extends BaseActivity {

    @BindView(R.id.txt_dialog_title)
    public TextView txtDialogTitle;

    @BindView(R.id.txt_dialog_content)
    public EditText txtDialogContent;

    @BindView(R.id.list_view)
    public ListView listView;

    @BindView(R.id.btn_ok)
    public Button btnOk;

    private AddressSelectAdapter addressSelectAdapter;

    @Override
    protected void after() {
        super.after();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    protected int layoutID() {
        return R.layout.dialog_select_address;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        bindView();
        initData();
        findViewById(R.id.contain).setOnTouchListener((v, event) -> {
            finish();
            overridePendingTransition(0, R.anim.fade_out);
            return false;
        });
    }

    @Override
    protected BasePresenterImpl createPresenter() {
        return null;
    }

    @OnClick(R.id.btn_ok)
    public void confirmButton() {
        if (!TextUtils.isEmpty(txtDialogContent.getText().toString())) {
            AppInfStore.saveSelectAddress(this, txtDialogContent.getText().toString());
//            NetConfig.updateRequestUrl();
            finish();
        }
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
