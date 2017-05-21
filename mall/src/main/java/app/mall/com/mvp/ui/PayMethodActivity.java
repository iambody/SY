package app.mall.com.mvp.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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
import qcloud.mall.R;

public class PayMethodActivity extends BaseActivity<PayMethodPresenter> implements PayMethodContract.View, View.OnClickListener {

    //返回键
    private ImageView title_left;
    //完成按钮
    private TextView title_right;
    private TextView titleMid;
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

    @Override
    protected int layoutID() {
        return R.layout.activity_pay_method;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

        payMethods = (ArrayList<PayMethod>) getIntent().getSerializableExtra("payList");
        paymethod_tag = (TextView) findViewById(R.id.paymethod_tag);
        title_left = (ImageView) findViewById(R.id.title_left);
        title_right = (TextView) findViewById(R.id.title_right);
        titleMid = (TextView) findViewById(R.id.title_mid);
        payList = (ListView) findViewById(R.id.pay_method_lv);
        title_left.setOnClickListener(this);


        titleMid.setText("支付方式");
        title_right.setText("确认");

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

        title_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.title_right) {
            Intent intent = new Intent(PayMethodActivity.this, PayActivity.class);
            JSONObject ja = new JSONObject();
            try {
                ja.put("name", payMethod.getName());
                ja.put("maxLimit", payMethod.getMaxLimit());
                ja.put("typeCode", String.format("%d", payMethod.getTypeCode()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            SPreference.putString(this, "payConfig", ja.toString());
            intent.putExtra("paymethod", payMethods.get(checkPosition));
            setResult(RESULT_OK, intent); //intent为A传来的带有Bundle的intent，当然也可以自己定义新的Bundle
            finish();

        } else if (i == R.id.title_left) {
            finish();
        }
    }

}
