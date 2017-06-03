package app.mall.com.mvp.ui;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.tools.DataStatistApiParam;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import app.mall.com.model.PayMethod;
import app.mall.com.mvp.adapter.PayMethodAdapter;
import app.mall.com.mvp.contract.PayMethodContract;
import app.mall.com.mvp.presenter.PayMethodPresenter;
import butterknife.BindView;
import qcloud.mall.R;
import qcloud.mall.R2;

public class PayMethodActivity extends BaseActivity<PayMethodPresenter> implements PayMethodContract.View, Toolbar.OnMenuItemClickListener {

    //返回键
    private ImageView title_left;
    //微信
    private ImageView paymethod_weixin;
    //支付宝
    private ImageView paymethod_zhifubao;
    //银联
    private ImageView paymethod_yinlian;
    //标记
    private TextView paymethod_tag;
    private List<ImageView> paymethods = new ArrayList<>();

    private ArrayList<PayMethod> payMethods = new ArrayList<>();

    private ListView payList;

    private PayMethod payMethod;

    private PayMethodAdapter adapter;

    private int checkPosition;

    @BindView(R2.id.title_mid)
    TextView titleMid;

    @BindView(R2.id.toolbar)
    Toolbar toolbar;

    @Override
    protected int layoutID() {
        return R.layout.activity_pay_method;
    }


    @Override
    protected void after() {
        super.after();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }


    @Override
    protected void init(Bundle savedInstanceState) {
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setNavigationIcon(com.cgbsoft.lib.R.drawable.ic_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        payMethods = (ArrayList<PayMethod>) getIntent().getSerializableExtra("payList");
        paymethod_tag = (TextView) findViewById(R.id.paymethod_tag);
        titleMid = (TextView) findViewById(R.id.title_mid);
        payList = (ListView) findViewById(R.id.pay_method_lv);


        titleMid.setText("支付方式");

//        showTileLeft();
//        showTileMid("支付方式");
//        showTileRight("确认");

        String payConfig = SPreference.getString(this, "payConfig");
        if (!TextUtils.isEmpty(payConfig)) {
            payMethod = new Gson().fromJson(payConfig, PayMethod.class);
            adapter = new PayMethodAdapter(this, payMethods, payMethod.getTypeCode());
            paymethod_tag.setText(payMethod.getName());
            payList.setAdapter(adapter);
        } else {
            adapter = new PayMethodAdapter(this, payMethods, payMethods.get(0).getTypeCode());
            payList.setAdapter(adapter);
            paymethod_tag.setText(payMethods.get(0).getName());
        }

        payList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.check(payMethods.get(position).getTypeCode());
                checkPosition = position;
                paymethod_tag.setText(payMethods.get(position).getName());
                payMethod = payMethods.get(position);
            }
        });
    }

    @Override
    protected PayMethodPresenter createPresenter() {
        return new PayMethodPresenter(this, this);
    }

    private void choiceMethodStasist(String method) {
        if (AppManager.isInvestor(this)) {
            DataStatistApiParam.Pay_C_Method(method);
        } else {
            DataStatistApiParam.Pay_B_Method(method);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(com.cgbsoft.lib.R.menu.page_menu, menu);
        MenuItem firstItem = menu.findItem(com.cgbsoft.lib.R.id.firstBtn);
        MenuItem secItem = menu.findItem(com.cgbsoft.lib.R.id.secondBtn);
        firstItem.setTitle("确认");
        secItem.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        for (int i = 0; i < payMethods.size(); i++) {
            if (payMethods.get(i).getTypeCode() == adapter.getCheck()) {
                payMethod = payMethods.get(i);
            }
        }
        Intent intent = new Intent(PayMethodActivity.this, PayActivity.class);
        JSONObject ja = new JSONObject();
        try {
            ja.put("name", payMethod.getName());
            ja.put("maxLimit", payMethod.getMaxLimit());
            ja.put("typeCode", String.format("%d", payMethod.getTypeCode()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SPreference.putString(PayMethodActivity.this, "payConfig", ja.toString());
        intent.putExtra("paymethod", payMethods.get(checkPosition));
        setResult(RESULT_OK, intent); //intent为A传来的带有Bundle的intent，当然也可以自己定义新的Bundle
        choiceMethodStasist(payMethod.getName());
        finish();
        return false;
    }
}
