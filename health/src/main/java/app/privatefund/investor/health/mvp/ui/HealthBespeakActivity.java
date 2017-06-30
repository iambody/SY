package app.privatefund.investor.health.mvp.ui;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.utils.damp.ClickRunnable;
import com.cgbsoft.lib.utils.imgNetLoad.Imageload;

import app.privatefund.investor.health.R;
import app.privatefund.investor.health.R2;
import app.privatefund.investor.health.mvp.contract.HealthBespeakContract;
import app.privatefund.investor.health.mvp.model.HealthListModel;
import app.privatefund.investor.health.mvp.presenter.HealthBespeakPresenter;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author chenlong
 */
public class HealthBespeakActivity extends BaseActivity<HealthBespeakPresenter> implements HealthBespeakContract.View {

    @BindView(R2.id.health_bespeak_content_container)
    LinearLayout contentContainer;

    @BindView(R2.id.health_bespeak_result_container)
    LinearLayout resultContainer;

    @BindView(R2.id.health_product_image)
    ImageView imageView;

    @BindView(R2.id.health_bespeak_title)
    TextView healthTitle;

    @BindView(R2.id.health_bespeak_name)
    EditText healthBespeakName;

    @BindView(R2.id.health_bespeak_phone)
    EditText healthBespeakPhone;

    @BindView(R2.id.health_bespeak_input_validate)
    EditText healthBespeakInputCode;

    @BindView(R2.id.health_bespeak_send)
    TextView healthBespeakSendCode;

    @BindView(R2.id.commit_bespeak_health)
    Button button;

    public static final String HEALTH_ITME_PARAMS = "health_product_params";
    public static final int TIMER_TOTAL = 60 * 1000;
    public static final int TIMER_DELAYT = 1000;
    private HealthListModel healthListModel;
    private CountDownTimer timer;

    @Override
    protected int layoutID() {
        return R.layout.activity_health_bespeak;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initTitle();
        healthListModel = (HealthListModel)getIntent().getSerializableExtra(HEALTH_ITME_PARAMS);
        Imageload.display(this, healthListModel.getImageUrl(), imageView);
        healthTitle.setText(healthListModel.getTitle());
        timer = new CountDownTimer(TIMER_TOTAL, TIMER_DELAYT) {
            @Override
            public void onTick(long millisUntilFinished) {
                healthBespeakSendCode.setText(getString(R.string.health_bespeak_validate_had_send).concat("(").concat(String.valueOf(millisUntilFinished / 1000).concat("s)")));
                healthBespeakSendCode.setBackgroundResource(R.color.app_golden_disable);
                healthBespeakSendCode.setEnabled(false);
            }

            @Override
            public void onFinish() {
                healthBespeakSendCode.setText(R.string.health_bespeak_validate_send);
                healthBespeakSendCode.setBackgroundResource(R.color.app_golden);
                healthBespeakSendCode.setEnabled(true);
            }
        };
    }

    private void initTitle() {
        ((TextView)findViewById(R.id.title_mid)).setText(R.string.health_bespeak_title);
        ImageView imageView = (ImageView)findViewById(R.id.title_left);
        imageView.setVisibility(View.VISIBLE);
        imageView.setOnClickListener(v -> finish());
    }

    @Override
    protected HealthBespeakPresenter createPresenter() {
        return new HealthBespeakPresenter(this, this);
    }

    @Override
    public void bespeakSuccess() {
        button.setEnabled(true);
        button.setBackgroundResource(R.drawable.button_golden_bg);
        contentContainer.setVisibility(View.GONE);
        resultContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void bespeakFailure() {
        button.setEnabled(true);
        button.setBackgroundResource(R.drawable.button_golden_bg);
    }

    @Override
    public void bespeakError(String error) {
        button.setEnabled(true);
        button.setBackgroundResource(R.drawable.button_golden_bg);
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R2.id.health_bespeak_send)
    void sendValidatecode() {
        if (TextUtils.isEmpty(healthBespeakPhone.getText().toString())) {
            Toast.makeText(this, R.string.please_input_phone, Toast.LENGTH_SHORT).show();
            return;
        }
        timer.start();
        getPresenter().getValidateCode(healthBespeakPhone.getText().toString());
    }

    @OnClick(R2.id.commit_bespeak_health)
    void commitBespeakHealthClick() {
        commitBespeakHealth();
    }

    private void commitBespeakHealth() {
        if (validateEditView()) {
            button.setEnabled(false);
            button.setBackgroundResource(R.color.app_golden_disable);
            getPresenter().commitHealthBespeak(healthListModel.getId(),
                    healthBespeakName.getText().toString(), healthBespeakPhone.getText().toString(), healthBespeakInputCode.getText().toString());
        }
    }

    private boolean validateEditView() {
        if (TextUtils.isEmpty(healthBespeakName.getText().toString())) {
            Toast.makeText(this, R.string.please_input_name, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(healthBespeakPhone.getText().toString())) {
            Toast.makeText(this, R.string.please_input_phone, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(healthBespeakInputCode.getText().toString())) {
            Toast.makeText(this, R.string.please_input_validate, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }
}
