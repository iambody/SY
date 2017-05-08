package app.product.com.mvc.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import app.product.com.R;


/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/8-14:54
 */

public class SearchBaseActivity extends Activity {
    public static final String PRODUCT = "1";
    public static final String ZIXUN = "2";
    public static final String VIDEO = "3";
    public static final String INFOMATION = "4";
    public static final String CUSTOM = "5";
    public static final String ORDER = "6";
    public static final String TYPE_PARAM = "SEARCH_TYPE_PARAMS";
    public static final String SUB_TYPE_PARAM = "SEARCH_TYPE_PARAMS";
    public static final String KEY_NAME_PARAM = "SEARCH_KEY_TEXT";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.acitivity_search_base);
    }
}
