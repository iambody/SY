package app.product.com.mvc.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.mvc.BaseMvcActivity;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.db.DaoUtils;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.BStrUtils;
import com.cgbsoft.lib.utils.tools.LogUtils;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.lib.utils.tools.ViewUtils;
import com.cgbsoft.privatefund.bean.product.HistorySearchBean;
import com.chenenyu.router.Router;
import com.chenenyu.router.annotation.Route;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import app.product.com.R;
import app.product.com.model.SearchResultBean;
import app.product.com.mvc.adapter.SearchAdatper;
import app.product.com.utils.BUtils;
import app.product.com.utils.ProductNavigationUtils;
import app.product.com.widget.ClearEditText;
import app.product.com.widget.HotSearchAdapter;
import app.product.com.widget.LineBreakLayout;
import app.product.com.widget.SimpleItemDecoration;


/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/8-14:54
 */
@Route("product_sousouactivity")
public class SearchBaseActivity extends BaseMvcActivity implements View.OnClickListener {
    public static final String PRODUCT = "1";
    public static final String ZIXUN = "2";
    public static final String VIDEO = "3";
    public static final String INFOMATION = "4";
    public static final String CUSTOM = "5";
    public static final String ORDER = "6";
    public static final String TYPE_PARAM = "SEARCH_TYPE_PARAMS";
    public static final String SUB_TYPE_PARAM = "SEARCH_TYPE_PARAMS";
    public static final String KEY_NAME_PARAM = "SEARCH_KEY_TEXT";
    private ListView listView;
    private LinearLayout historySearch;
    private LineBreakLayout flagHistorySearch;
    private HotSearchAdapter listAdapter;
    private ImageView delHistory;
    private ClearEditText textEdit;
    private TextView backText;
    private View header;
    //    private DatabaseUtils databaseUtils;
    private String currentType;
    private SearchAdatper searchAdatper;
    private RecyclerView recycleView;
    private LinearLayout resultLinearLayout;
    private LinearLayout emptyLinearLayout;
    private String currentKey;
    private DaoUtils daoUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.acitivity_search_base);
        daoUtils = new DaoUtils(baseContext, DaoUtils.W_SousouHistory);
        currentType = getIntent().getStringExtra(TYPE_PARAM);
        initListView();
        initHistory();
        initHotSearch();
        ViewUtils.showInputMethod(textEdit);
        textEdit.requestFocus();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.search_cancel) {
            finish();
            overridePendingTransition(0, R.anim.message_search_out_bottom);
        } else if (v.getId() == R.id.search_title_ed) {
        } else if (R.id.product_search_history_del == v.getId()) {
//            daoUtils.clearnHistorySearch();
            daoUtils.clearnHistoryByID(currentType, AppManager.getUserId(baseContext));
            initHistory();
        }
    }

    private void initListView() {
        resultLinearLayout = (LinearLayout) findViewById(R.id.product_search_search_result);
        emptyLinearLayout = (LinearLayout) findViewById(R.id.product_search_search_empty);
        delHistory = (ImageView) findViewById(R.id.product_search_history_del);
        initRecycleView();
        listView = (ListView) findViewById(R.id.product_search_list);
        historySearch = (LinearLayout) findViewById(R.id.product_search_history_search);
        flagHistorySearch = (LineBreakLayout) findViewById(R.id.product_search_history_search_flag);
        backText = (TextView) findViewById(R.id.search_cancel);
        backText.setOnClickListener(this);
        delHistory.setOnClickListener(this);
        textEdit = (ClearEditText) findViewById(R.id.search_title_ed);
        listAdapter = new HotSearchAdapter(this, currentType);
        listView.setAdapter(listAdapter);
        header = LayoutInflater.from(this).inflate(R.layout.hot_search_head, null);
        ((ImageView) header.findViewById(R.id.hot_search_title_img)).setImageResource(AppManager.isInvestor(this) ? R.drawable.hot_c : R.drawable.hot_b);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SearchResultBean.ResultBean hotSearch = (SearchResultBean.ResultBean) parent.getAdapter().getItem(position);
                if (hotSearch != null) {
                    jumpActivityFromHotSearch(hotSearch);
                }
            }
        });
        textEdit.setTextChangedListener(new ClearEditText.TextChangedListener() {
            @Override
            public void onTextChanged(String value) {
                if (TextUtils.isEmpty(value)) {
                    currentKey = "";
                    resultLinearLayout.setVisibility(View.GONE);
                    emptyLinearLayout.setVisibility(View.GONE);
                    return;
                }
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
                    //todo 保存记录
                    saveSearchText(str);
                    requestSearch(str);
//                    if (AppManager.isInvestor(SearchBaseActivity.this)) {
//                        DataStatistApiParam.searchBaseToC(textEdit.getText().toString(), Utils.getNameByName(SearchBaseActivity.this, currentType));
//                    } else {
//                        DataStatistApiParam.searchBaseToB(textEdit.getText().toString(), Utils.getNameByName(SearchBaseActivity.this, currentType));
//                    }
                    return false;
                }
                return false;
            }
        });
        emptyLinearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    private void initRecycleView() {
        recycleView = (RecyclerView) findViewById(R.id.result_show);
        searchAdatper = new SearchAdatper(this, currentType);
        recycleView.setAdapter(searchAdatper);
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        recycleView.addItemDecoration(new SimpleItemDecoration(this, R.color.c_background, R.dimen.ui_z_dip));
    }

    private void requestSearch(String str) {
        ViewUtils.hideInputMethod(textEdit);
        searchAdatper.setKeyName(str);
        if (TextUtils.equals(currentKey, str)) {
            return;
        }
        this.currentKey = str;
        String name = BUtils.replaceSpeialStr(str);

        HashMap<String, String> map = new HashMap<>();
        map.put("category", AppManager.isInvestor(baseContext) ? "c" : "b");
        map.put("infoType", formateType());
        map.put("keywords", name);
        map.put("userId", AppManager.getUserId(baseContext));
        addSubscription(ApiClient.getSousouData(map).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                Gson gson = new Gson();
                try {
                    JSONObject ja = new JSONObject(s);
                    JSONArray keys = ja.getJSONArray("keywords");
                    JSONArray jsonArray = ja.getJSONArray("items");
                    List<SearchResultBean> list = gson.fromJson(jsonArray.toString(), new TypeToken<List<SearchResultBean>>() {
                    }.getType());
                    List<String> keysList = gson.fromJson(keys.toString(), new TypeToken<List<String>>() {
                    }.getType());
                    resultLinearLayout.setVisibility(View.VISIBLE);
                    searchAdatper.setReturnKeys(keysList);
                    if (!BUtils.isEmpty(list)) {
                        searchAdatper.setData(list);
                        changeViewDisplay(false);
                    } else {
                        changeViewDisplay(true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void onRxError(Throwable error) {
                LogUtils.Log("s", "s");
            }
        }));

    }

    private void changeViewDisplay(boolean empty) {
        emptyLinearLayout.setVisibility(empty ? View.VISIBLE : View.GONE);
    }

    private void jumpActivityFromHotSearch(SearchResultBean.ResultBean resultBean) {
        switch (resultBean.getInfoType()) {
            case INFOMATION:
//                NavigationUtils.startMessageActivity(context, resultBean, currentKey);
                break;
            case PRODUCT:
                ProductNavigationUtils.startProductDetailActivity(baseContext,resultBean.getTargetId(),resultBean.getTitle(),200);
                break;
            case ZIXUN:
                String informationUrl =    "https://app.simuyun.com/app5.0/discover/details.html?id=" + resultBean.getTargetId()+ "&category=" + resultBean.getCategoryId();
                NavigationUtils.startVideoInformationActivityu(baseContext,informationUrl,resultBean.getTitle());
                break;
            case VIDEO:
                Router.build(RouteConfig.GOTOVIDEOPLAY).with("videoId", resultBean.getTargetId()).go(baseContext);
                break;
        }
    }

    private String formateType() {
        String values = currentType;
        if (AppManager.isInvestor(SearchBaseActivity.this)) {
            if (TextUtils.equals(currentType, ZIXUN)) {
                values += "," + VIDEO;
            } else if (TextUtils.equals(currentType, VIDEO)) {
                values += "," + ZIXUN;
            }
        }
        return values;
    }

    private void initHotSearch() {
        HashMap<String, String> map = new HashMap<>();
        map.put("infoType", formateType());
        map.put("category", AppManager.isInvestor(baseContext) ? "c" : "b");
        addSubscription(ApiClient.getHotSousouData(map).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {

                try {
//                    JSONObject response = new JSONObject(s);
//                    Log.i("hot search task ", "-------search hot=" + response.toString());
//                    JSONArray jsonArray = response.getJSONArray("result");
                    Gson g = new Gson();
                    if (!BStrUtils.isEmpty(s)) {
                        List<SearchResultBean.ResultBean> list = g.fromJson(s, new TypeToken<List<SearchResultBean.ResultBean>>() {
                        }.getType());
                        if (!BUtils.isEmpty(list)) {
                            listView.setHeaderDividersEnabled(false);
                            listView.addHeaderView(header);
                            listAdapter.setData(list);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void onRxError(Throwable error) {

            }
        }));


    }

    private void initHistory() {

        List<HistorySearchBean> historySearches = daoUtils.getHistorysByType(currentType, AppManager.getUserId(baseContext));
        Log.i("----search history", "----search histor=" + historySearches.size());
        historySearch.setVisibility(BUtils.isEmpty(historySearches) ? View.GONE : View.VISIBLE);

        List<String> historyList = new ArrayList<>();
        for (HistorySearchBean historySearch : historySearches) {
            historyList.add(historySearch.getName());
        }
        flagHistorySearch.setOnClickFlagText(new LineBreakLayout.OnClickFlagText() {
            @Override
            public void onclick(String values) {
                textEdit.setText(values);
                requestSearch(values);
            }
        });
        flagHistorySearch.setLables(historyList, true);
    }

    //todo 更新搜搜记录
    private void saveSearchText(String name) {
        if (!TextUtils.isEmpty(name)) {
            HistorySearchBean historySearch = new HistorySearchBean(String.valueOf(System.currentTimeMillis()), name, currentType, System.currentTimeMillis(), AppManager.getUserId(baseContext));
            daoUtils.insertHistorySearch(historySearch);

        }
    }
}
