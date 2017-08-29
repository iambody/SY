package app.product.com.mvc.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.contant.RouteConfig;
import com.chenenyu.router.annotation.Route;

import app.product.com.R;
import app.product.com.R2;
import app.product.com.mvp.ui.ProductFragment;
import app.product.com.mvp.ui.ShareSearchActivity;
import butterknife.BindView;

/**
 * @author chenlong
 */
@Route(RouteConfig.GOTO_SELECT_PRODUCT)
public class SelectProductActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener {
//
//    @BindView(R2.id.toolbar)
//    protected Toolbar toolbar;

    @BindView(R2.id.title_mid)
    protected TextView titleMid;
    private FrameLayout contain;

    private ProductFragment productproductragment;

    private String searchProduct;
    private String searchType;

    @Override
    protected int layoutID() {
        return R.layout.activity_select_product;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        searchProduct = intent.getStringExtra(ShareSearchActivity.searchProduct);
        searchType = intent.getStringExtra(ShareSearchActivity.productType);
        System.out.println("-----searchTyp=" + searchType);
        productproductragment.setParamFiLter(searchType);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
//        toolbar.setTitle("");
        titleMid.setText("选择要分享的产品");
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        setSupportActionBar(toolbar);
//        toolbar.setOnMenuItemClickListener(this);
//        toolbar.setNavigationIcon(com.cgbsoft.lib.R.drawable.ic_back_black_24dp);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
        contain = (FrameLayout) findViewById(R.id.contain);
         productproductragment = new ProductFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(ProductFragment.FROM_SEND_PRODUCT, true);
        productproductragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.contain, productproductragment).commit();
    }

    @Override
    protected BasePresenterImpl createPresenter() {
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.page_menu, menu);
//        MenuItem rightItem = menu.findItem(R.id.firstBtn);
//        MenuItem secItem = menu.findItem(R.id.secondBtn);
//        secItem.setVisible(false);
//        rightItem.setIcon(R.drawable.ic_share_search_product);
//        return true;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
//        if (item.getItemId() == com.cgbsoft.lib.R.id.firstBtn) {
//            Intent intent = new Intent(SelectProductActivity.this, ShareSearchActivity.class);
//            intent.putExtra("type", "share");
//            startActivity(intent);
//        }
//        return false;
        return onMenuItemClick(item);
    }
}
