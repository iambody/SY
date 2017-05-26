package app.product.com.mvc;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.mvc.BaseMvcActivity;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.CollectionUtils;
import com.cgbsoft.lib.utils.tools.Utils;
import com.cgbsoft.lib.widget.MToast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import app.product.com.R;
import app.product.com.model.SearchResultBean;
import app.product.com.mvc.adapter.SearchAdatper;
import app.product.com.mvc.ui.SearchBaseActivity;
import app.product.com.widget.ClearEditText;
import app.product.com.widget.SimpleItemDecoration;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/25-18:00
 */
public class SearchResultListActivity extends BaseMvcActivity implements View.OnClickListener {

    public static final String LIST_PARAM = "LIST_PARAMS";
    public static final String KEY_NAME_PARAM = "KEY_NAME_PARAMS";
    public static final String NOTICE_SEARCH = "notice_search";
    public static final String NOTICE_SEARCH_TITLE = "notice_search_title";
    private List<SearchResultBean.ResultBean> list;
    private LinearLayout emptyLinearLayout;
    private String keyName;
    private String currentType;
    private ClearEditText textEdit;
    private int pageIndex;
    private TextView backText;
//    private DatabaseUtils databaseUtils;
    private SearchAdatper searchAdatper;
    private RecyclerView recycleView;
    private SearchResultBean searchResultBean;
    private String subType;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.acitivity_search_result_list);
//        databaseUtils = new DatabaseUtils(this);
        keyName = getIntent().getStringExtra(SearchBaseActivity.KEY_NAME_PARAM);
        currentType = getIntent().getStringExtra(SearchBaseActivity.TYPE_PARAM);
        subType = getIntent().getStringExtra(SearchBaseActivity.SUB_TYPE_PARAM);
        initView();
        if (getIntent().getSerializableExtra(LIST_PARAM) != null) {
            initData((List<SearchResultBean.ResultBean>) getIntent().getSerializableExtra(LIST_PARAM));
        }

        if (getIntent().getBooleanExtra(NOTICE_SEARCH, false)) {
            String keyName = getIntent().getStringExtra(NOTICE_SEARCH_TITLE);
            searchAdatper.setReturnKeys(Arrays.asList(new String[]{keyName}));
            searchAdatper.setKeyName(keyName);
            currentType = SearchBaseActivity.INFOMATION;
            subType = currentType;
            textEdit.setText(keyName);
        }
    }

    private void initData(List<SearchResultBean.ResultBean> dataList) {
        if (list == null) {
            list = new ArrayList<>();
        }
        if (!CollectionUtils.isEmpty(dataList)) {
            List<String> keyNames = getIntent().getStringArrayListExtra(KEY_NAME_PARAM);
            list.addAll(dataList);
            searchAdatper.setReturnKeys(keyNames);
            searchAdatper.setKeyName(keyName);
            searchAdatper.addDataList(list);
            pageIndex = 1;
        }
    }

    private boolean hasNextData() {
        String total = searchResultBean.getTotal();
        int pageSize = searchResultBean.getPageSize();
        int pageIndex = searchResultBean.getPageIndex();
        return !TextUtils.isEmpty(total) && (pageSize * pageIndex < Integer.parseInt(total));
    }

    @Override
    public void onClick(View v) {
        if (R.id.search_cancel == v.getId()) {
            finish();
            overridePendingTransition(0, R.anim.message_search_out_bottom);
        }
    }

    private void initView() {
        emptyLinearLayout = (LinearLayout) findViewById(R.id.search_empty);
        recycleView = (RecyclerView) findViewById(R.id.result_show);
        textEdit = (ClearEditText) findViewById(R.id.search_title_ed);
        backText = (TextView) findViewById(R.id.search_cancel);
        searchAdatper = new SearchAdatper(this, currentType);
        recycleView.setAdapter(searchAdatper);
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        recycleView.addItemDecoration(new SimpleItemDecoration(this, R.color.c_background, R.dimen.ui_z_dip));
        backText.setOnClickListener(this);
        textEdit.setText(keyName);
        emptyLinearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        recycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                if (visibleItemCount > 0 && visibleItemCount != totalItemCount && lastVisibleItemPosition == totalItemCount - 1) {
                    if (searchResultBean == null || hasNextData()) {
                        pageIndex++;
                        requestSearch(keyName);
//                    } else if (!hasNextData() && pageIndex > 1 ) {
//                        searchAdatper.addDataFooter(new SearchResultBean.ResultBean(SearchResultBean.ResultType.NO_MORE_DATA));
                    }
                }
            }
        });
        textEdit.setTextChangedListener(new ClearEditText.TextChangedListener() {
            @Override
            public void onTextChanged(String value) {
                if (TextUtils.isEmpty(value)) {
                    keyName = "";
                    recycleView.setVisibility(View.GONE);
                    emptyLinearLayout.setVisibility(View.GONE);
                    return;
                }

                if (TextUtils.equals(keyName, value)) {
                    return;
                }

                keyName = value;
                pageIndex = 0;
                searchAdatper.clearData();
                searchAdatper.setKeyName(keyName);
                recycleView.setVisibility(View.VISIBLE);
                requestSearch(value);
            }
        });
        textEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String str = v.getText().toString();
                    if (TextUtils.isEmpty(str)) {
                        return false;
                    }
                    if (TextUtils.equals(keyName, str)) {
                        return false;
                    }
                    keyName = str;
                    pageIndex = 0;
                    searchAdatper.clearData();
                    searchAdatper.setKeyName(str);
                    Toast.makeText(SearchResultListActivity.this, str, Toast.LENGTH_SHORT).show();
                    requestSearch(str);
//                    if (Utils.isVisteRole(SearchResultListActivity.this)) {
//                        DataStatistApiParam.searchBaseToC(textEdit.getText().toString(), Utils.getNameByName(SearchResultListActivity.this, currentType));
//                    } else {
//                        DataStatistApiParam.searchBaseToB(textEdit.getText().toString(), Utils.getNameByName(SearchResultListActivity.this, currentType));
//                }
                    return false;
                }
                return false;
            }
        });
    }

    private String formateType() {
        String values = currentType;
        if (AppManager.isInvestor(baseContext)) {
            if (TextUtils.equals(currentType, SearchBaseActivity.ZIXUN)) {
                values += "," + SearchBaseActivity.VIDEO;
            } else if (TextUtils.equals(currentType, SearchBaseActivity.VIDEO)) {
                values += "," + SearchBaseActivity.ZIXUN;
            }
        }
        return values;
    }

    private void requestSearch(String str) {
        String replaceSpeialStr = Utils.replaceSpeialStr(str);
        HashMap<String, String> map = new HashMap<>();
        map.put("category", AppManager.isInvestor(baseContext) ? "c" : "b");
        map.put("infoType", formateType());
        map.put("keywords", replaceSpeialStr);
        map.put("userId", AppManager.getUserId(baseContext));
        if (!TextUtils.isEmpty(String.valueOf(pageIndex))) {
            map.put("pageIndex", String.valueOf(pageIndex));
        }
        if (!TextUtils.isEmpty(subType)) {
            map.put("subType", subType);
        }
        addSubscription(ApiClient.getSousouData(map).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                Gson gson = new Gson();
                try {
                    JSONObject ja = new JSONObject(s);
                    JSONArray jsonArray = ja.getJSONArray("items");
                    JSONArray keys = ja.getJSONArray("keywords");
                    List<String> keysList = gson.fromJson(keys.toString(), new TypeToken<List<String>>() {
                    }.getType());
                    List<SearchResultBean> list = gson.fromJson(jsonArray.toString(), new TypeToken<List<SearchResultBean>>() {
                    }.getType());
                    if (!CollectionUtils.isEmpty(keysList)) {
                        searchAdatper.setReturnKeys(keysList);
                    }
                    if (!CollectionUtils.isEmpty(list) && list.get(0) != null && !CollectionUtils.isEmpty(list.get(0).getResults())) {
                        searchResultBean = list.get(0);
                        List<SearchResultBean.ResultBean> resutList = list.get(0).getResults();
                        insertViewType(resutList);
                        searchAdatper.addDataList(resutList);
                        changeViewDisplay(false);
                    } else {
                        if (pageIndex < 1) {
                            changeViewDisplay(true);
                        } else {
                            searchAdatper.addDataFooter(new SearchResultBean.ResultBean(SearchResultBean.ResultType.NO_MORE_DATA));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void onRxError(Throwable error) {

                MToast.makeText(SearchResultListActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }));


    }

    private void insertViewType(List<SearchResultBean.ResultBean> arrayList) throws Exception {
        switch (currentType) {
            case SearchBaseActivity.PRODUCT:
                searchAdatper.insertTypeData(arrayList, SearchResultBean.ResultType.PRODUCT_ITEM);
                break;
            case SearchBaseActivity.ZIXUN:
                searchAdatper.insertTypeData(arrayList, SearchResultBean.ResultType.XUN_ITEM);
                break;
            case SearchBaseActivity.VIDEO:
                searchAdatper.insertTypeData(arrayList, SearchResultBean.ResultType.VIDEO_ITEM);
                break;
            case SearchBaseActivity.INFOMATION:
                searchAdatper.insertTypeData(arrayList, SearchResultBean.ResultType.INFO_ITEM);
                break;
            case SearchBaseActivity.CUSTOM:
                searchAdatper.insertTypeData(arrayList, SearchResultBean.ResultType.CUSTOM_ITEM);
                break;
            case SearchBaseActivity.ORDER:
                searchAdatper.insertTypeData(arrayList, SearchResultBean.ResultType.ORDER_ITEM);
                break;
            default:
                throw new Exception("error info type exception");
        }
    }

    private void changeViewDisplay(boolean empty) {
        emptyLinearLayout.setVisibility(empty ? View.VISIBLE : View.GONE);
    }


}
