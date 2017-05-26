package app.product.com.mvc.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;


import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.contant.RouteConfig;
import com.chenenyu.router.annotation.Route;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app.product.com.R;
import app.product.com.R2;
import app.product.com.mvp.ui.ProductFragment;
import butterknife.BindView;

@Route(RouteConfig.GOTO_SELECT_PRODUCT)
public class SelectProductActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener {

    @BindView(R2.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R2.id.title_mid)
    protected TextView titleMid;

    private FrameLayout contain;



    @Override
    protected int layoutID() {
        return R.layout.activity_select_product;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        titleMid.setText("选择要分享的产品");
        contain = (FrameLayout) findViewById(R.id.contain);
        ProductFragment productproductragment = new ProductFragment();
//        productproductragment.setSearchValue("");
//        productproductragment.setFrom("share");
        JSONObject filterParam = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            filterParam.put("series", jsonArray.put("0"));
//            productproductragment.setParams(filterParam);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.contain, productproductragment).commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected BasePresenterImpl createPresenter() {
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.page_menu, menu);
        MenuItem rightItem = menu.findItem(R.id.firstBtn);
        MenuItem secItem = menu.findItem(R.id.secondBtn);
        secItem.setVisible(false);
//        rightItem.setTitle(hasRightShare ? R.string.umeng_socialize_share : R.string.save);
        rightItem.setIcon(R.drawable.ic_share_search_product);
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == com.cgbsoft.lib.R.id.firstBtn) {
//            Intent intent = new Intent(SelectProductActivity.this, SearchActivity.class);
//            intent.putExtra("type", "share");
//            startActivity(intent);
        }
        return false;
    }
}
