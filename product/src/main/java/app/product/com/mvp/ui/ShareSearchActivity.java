package app.product.com.mvp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

import app.product.com.R;
import app.product.com.R2;
import app.product.com.mvc.ui.SelectProductActivity;
import app.product.com.mvp.ui.adapter.SearchResultListAdapter;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author chenlong
 */
public class ShareSearchActivity extends BaseActivity implements OnItemClickListener, OnEditorActionListener {

    public static final String productID = "productID";
    public static final String productCatagory = "category";
    public static final String productType = "productType";
    public static final String searchProduct = "searchProduct";

    @BindView(R2.id.search_cancel)
    TextView cancel;

    @BindView(R2.id.search_title_ed)
    EditText edit;

    @BindView(R2.id.search_result_list)
    ListView search_Result;

    @BindView(R2.id.search_taishan)
    TextView taishan;

    @BindView(R2.id.search_henshan)
    TextView hengshan;

    @BindView(R2.id.search_songshan)
    TextView search_songshan;

    @BindView(R2.id.search_kunlunshan)
    TextView kunlunshan;

    @BindView(R2.id.search_huanghe)
    TextView huanghe;

    @BindView(R2.id.search_changjiang)
    TextView changjiang;

    @BindView(R2.id.search_lanchangjiang)
    TextView lanchangjiang;

    @BindView(R2.id.search_yamaxun)
    TextView yamaxun;

    private final ArrayList<String> datas = new ArrayList<String>();
    private SearchResultListAdapter adapter;
    private String isShare;

    @Override
    protected int layoutID() {
        return R.layout.activity_search;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        bindView();
        startTask();
    }

    @Override
    protected BasePresenterImpl createPresenter() {
        return null;
    }

    private void bindView() {
        adapter = new SearchResultListAdapter(this, datas);
        search_Result.setAdapter(adapter);
        isShare = getIntent().getStringExtra("type");
    }

    int type = 0; // 搜索类型 0 产品类型 1产品关键字

    @OnClick(R2.id.search_cancel)
    void backCancelOnclick() {
        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        finish();
    }

    @OnClick(R2.id.search_taishan)
    void searchTaiShan() {
        Intent i = new Intent(this, SelectProductActivity.class);
        HashMap<String, String> umengMap = new HashMap<String, String>();
        HashMap<String, String> umengMap2 = new HashMap<String, String>();
//        umengMap.put("类型", "泰山");
//        umengMap.put("机构", SPSave.getInstance(MApplication.mContext).getString("organizationName"));
//        MobclickAgent.onEvent(MApplication.mContext, "proRecommand_click", umengMap);
//        umengMap2.put("类型", "固收");
//        umengMap2.put("机构", SPSave.getInstance(MApplication.mContext).getString("organizationName"));
//        MobclickAgent.onEvent(MApplication.mContext, "productList_click", umengMap2);
        i.putExtra(searchProduct, "泰山");
        i.putExtra(productType, 1);
        if (!TextUtils.isEmpty(isShare)) {
            i.putExtra("isShare", isShare);
        }
        startActivity(i);
        finish();
    }

    @OnClick(R2.id.search_henshan)
    void searchHenShan() {
        Intent i = new Intent(this, SelectProductActivity.class);
        HashMap<String, String> umengMap = new HashMap<String, String>();
        HashMap<String, String> umengMap2 = new HashMap<String, String>();
//        umengMap.put("类型", "恒山");
//        umengMap.put("机构", SPSave.getInstance(MApplication.mContext).getString("organizationName"));
//        MobclickAgent.onEvent(MApplication.mContext, "proRecommand_click", umengMap);
//        umengMap2.put("类型", "固收");
//        umengMap2.put("机构", SPSave.getInstance(MApplication.mContext).getString("organizationName"));
//        MobclickAgent.onEvent(MApplication.mContext, "productList_click", umengMap2);
        i.putExtra(productType, 3);
        i.putExtra(searchProduct, "恒山");
        if (!TextUtils.isEmpty(isShare)) {
            i.putExtra("isShare", isShare);
        }
        startActivity(i);
        finish();
    }

    @OnClick(R2.id.search_songshan)
    void searchSongShan() {
        Intent i = new Intent(this, SelectProductActivity.class);
        HashMap<String, String> umengMap = new HashMap<String, String>();
        HashMap<String, String> umengMap2 = new HashMap<String, String>();
//        umengMap.put("类型", "嵩山");
//        umengMap.put("机构", SPSave.getInstance(MApplication.mContext).getString("organizationName"));
//        MobclickAgent.onEvent(MApplication.mContext, "proRecommand_click", umengMap);
//        umengMap2.put("类型", "固收");
//        umengMap2.put("机构", SPSave.getInstance(MApplication.mContext).getString("organizationName"));
//        MobclickAgent.onEvent(MApplication.mContext, "productList_click", umengMap2);
        i.putExtra(searchProduct, "嵩山");
        i.putExtra(productType, 4);
        if (!TextUtils.isEmpty(isShare)) {
            i.putExtra("isShare", isShare);
        }
        startActivity(i);
        finish();
    }

    @OnClick(R2.id.search_kunlunshan)
    void searchKunLunShan() {
        Intent i = new Intent(this, SelectProductActivity.class);
        HashMap<String, String> umengMap = new HashMap<String, String>();
        HashMap<String, String> umengMap2 = new HashMap<String, String>();
//        umengMap.put("类型", "昆仑山");
//        umengMap.put("机构", SPSave.getInstance(MApplication.mContext).getString("organizationName"));
//        MobclickAgent.onEvent(MApplication.mContext, "proRecommand_click", umengMap);
//        umengMap2.put("类型", "固收");
//        umengMap2.put("机构", SPSave.getInstance(MApplication.mContext).getString("organizationName"));
//        MobclickAgent.onEvent(MApplication.mContext, "productList_click", umengMap2);
        i.putExtra(productType, 9);
        i.putExtra(searchProduct, "昆仑山");
        if (!TextUtils.isEmpty(isShare)) {
            i.putExtra("isShare", isShare);
        }
        startActivity(i);
        finish();
    }

    @OnClick(R2.id.search_huanghe)
    void searchHuangHe() {
        Intent i = new Intent(this, SelectProductActivity.class);
        HashMap<String, String> umengMap = new HashMap<String, String>();
        HashMap<String, String> umengMap2 = new HashMap<String, String>();
//        umengMap.put("类型", "黄河");
//        umengMap.put("机构", SPSave.getInstance(MApplication.mContext).getString("organizationName"));
//        MobclickAgent.onEvent(MApplication.mContext, "proRecommand_click", umengMap);
//        umengMap2.put("类型", "浮收");
//        umengMap2.put("机构", SPSave.getInstance(MApplication.mContext).getString("organizationName"));
//        MobclickAgent.onEvent(MApplication.mContext, "productList_click", umengMap2);
        i.putExtra(searchProduct, "黄河");
        i.putExtra(productType, 6);
        if (!TextUtils.isEmpty(isShare)) {
            i.putExtra("isShare", isShare);
        }
        startActivity(i);
        finish();
    }

    @OnClick(R2.id.search_changjiang)
    void searchChangeJiang() {
        Intent i = new Intent(this, SelectProductActivity.class);
        HashMap<String, String> umengMap = new HashMap<String, String>();
        HashMap<String, String> umengMap2 = new HashMap<String, String>();
//        umengMap.put("类型", "长江");
//        umengMap.put("机构", SPSave.getInstance(MApplication.mContext).getString("organizationName"));
//        MobclickAgent.onEvent(MApplication.mContext, "proRecommand_click", umengMap);
//        umengMap2.put("类型", "浮收");
//        umengMap2.put("机构", SPSave.getInstance(MApplication.mContext).getString("organizationName"));
//        MobclickAgent.onEvent(MApplication.mContext, "productList_click", umengMap2);
        i.putExtra(productType, 5);
        i.putExtra(searchProduct, "长江");
        if (!TextUtils.isEmpty(isShare)) {
            i.putExtra("isShare", isShare);
        }
        startActivity(i);
        finish();
    }

    @OnClick(R2.id.search_lanchangjiang)
    void searchLanChangeJiang() {
        Intent i = new Intent(this, SelectProductActivity.class);
        HashMap<String, String> umengMap = new HashMap<String, String>();
        HashMap<String, String> umengMap2 = new HashMap<String, String>();
//        umengMap.put("类型", "澜沧江");
//        umengMap.put("机构", SPSave.getInstance(MApplication.mContext).getString("organizationName"));
//        MobclickAgent.onEvent(MApplication.mContext, "proRecommand_click", umengMap);
//        umengMap2.put("类型", "浮收");
//        umengMap2.put("机构", SPSave.getInstance(MApplication.mContext).getString("organizationName"));
//        MobclickAgent.onEvent(MApplication.mContext, "productList_click", umengMap2);
        i.putExtra(productType, 7);
        i.putExtra(searchProduct, "澜沧江");
        if (!TextUtils.isEmpty(isShare)) {
            i.putExtra("isShare", isShare);
        }
        startActivity(i);
        finish();
    }

    @OnClick(R2.id.search_yamaxun)
    void searchYaMaXun() {
        Intent i = new Intent(this, SelectProductActivity.class);
        HashMap<String, String> umengMap = new HashMap<String, String>();
        HashMap<String, String> umengMap2 = new HashMap<String, String>();
//        umengMap.put("类型", "亚马逊");
//        umengMap.put("机构", SPSave.getInstance(MApplication.mContext).getString("organizationName"));
//        MobclickAgent.onEvent(MApplication.mContext, "proRecommand_click", umengMap);
//        umengMap2.put("类型", "浮收");
//        umengMap2.put("机构", SPSave.getInstance(MApplication.mContext).getString("organizationName"));
//        MobclickAgent.onEvent(MApplication.mContext, "productList_click", umengMap2);
        i.putExtra(productType, 8);
        i.putExtra(searchProduct, "亚马逊");
        if (!TextUtils.isEmpty(isShare)) {
            i.putExtra("isShare", isShare);
        }
        startActivity(i);
        finish();
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        edit.setText("");
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    public void startTask() {
        ApiClient.getTestGetHotProduct().subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                try {
                    JSONArray result = new JSONArray(s);
                    for (int i = 0; i < result.length(); i++) {
                        datas.add(result.getString(i));
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void onRxError(Throwable error) {

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // TODO Auto-generated method stub
        // VolleySingleton.getInstance(this).getRequestQueue().add(request);
        String searchText = getSearchText(position, null);
        type = 1;
        SearchProduct(searchText, type);
    }

    private String getSearchText(int index, String string) {
        if (string != null) {
            return string;
        } else {
            return datas.get(index);
        }
        // return null;
    }

    private void SearchProduct(String searchValue, int type) {
        Intent i = new Intent(this, SelectProductActivity.class);
        i.putExtra(searchProduct, searchValue);
        i.putExtra("productType", type);
        if (!TextUtils.isEmpty(isShare)) {
            i.putExtra("isShare", isShare);
        }
        startActivity(i);
        finish();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        // TODO Auto-generated method stub
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            // if (keyCode == KeyEvent.KEYCODE_ENTER) {// 修改回车键功能
            // 先隐藏键盘
            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                    this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            String content = edit.getText().toString().trim();
            SearchProduct(content, 1);
//            String[] param = new String[]{content, MApplication.getUser().getToB().isColorCloud(), MApplication.getUser().getToB().getOrganizationName()};
//            HashMap<String, String> params = DataStatisticsUtils.getParams("1010", "10066", param);
//            DataStatisticsUtils.push(this, params);
            return true;
        }
        return false;
    }
}
