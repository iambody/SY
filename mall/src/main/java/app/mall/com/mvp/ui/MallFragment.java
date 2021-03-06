package app.mall.com.mvp.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cgbsoft.lib.base.mvp.ui.BaseFragment;
import com.cgbsoft.lib.base.webview.BaseWebViewActivity;
import com.cgbsoft.lib.base.webview.BaseWebview;
import com.cgbsoft.lib.base.webview.CwebNetConfig;
import com.cgbsoft.lib.base.webview.WebViewConstant;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.constant.RxConstant;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.mall.com.mvp.contract.MallContract;
import app.mall.com.mvp.presenter.MallPresenter;
import butterknife.BindView;
import butterknife.OnClick;
import qcloud.mall.R;
import qcloud.mall.R2;
import rx.Observable;

import static android.view.View.GONE;

/**
 * 商城
 */
public class MallFragment extends BaseFragment<MallPresenter> {

    @BindView(R2.id.mall_rule)
    TextView mallRule;
    @BindView(R2.id.mall_web_view)
    BaseWebview mallWeb;

    @BindView(R2.id.title_layout)
    RelativeLayout title_layout;

    @Override
    protected int layoutID() {
        return R.layout.fragment_mall;
    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {
        mallWeb.loadUrls(CwebNetConfig.clubPage);

    }


    @Override
    protected void after(View view) {
        super.after(view);
        if (SPreference.getBoolean(getActivity(),"ydMallState")){
            title_layout.setVisibility(GONE);
        }
    }


    @Override
    protected MallPresenter createPresenter() {
        return null;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @OnClick(R2.id.mall_rule)
    public void openRulePage() {
        Intent i = new Intent(getContext(), BaseWebViewActivity.class);
        i.putExtra(WebViewConstant.push_message_url, "bank/ydrule.html");
        i.putExtra(WebViewConstant.push_message_title, "云豆使用规则");
        i.putExtra(WebViewConstant.RIGHT_SAVE, false);
        i.putExtra(WebViewConstant.RIGHT_SHARE, false);
        i.putExtra(WebViewConstant.PAGE_INIT, false);
        startActivity(i);

//        Intent intent = new Intent(getContext(),PayActivity.class);
//        startActivity(intent);
    }
}
