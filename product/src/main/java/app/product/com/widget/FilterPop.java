package app.product.com.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.tools.BStrUtils;
import com.cgbsoft.lib.utils.tools.DimensionPixelUtil;
import com.cgbsoft.lib.utils.tools.LogUtils;
import com.cgbsoft.lib.utils.tools.PromptManager;
import com.cgbsoft.lib.utils.tools.ViewHolders;
import com.cgbsoft.lib.widget.FloatView;
import com.cgbsoft.lib.widget.FlowLayoutView;
import com.cgbsoft.lib.widget.taglayout.FlowTagLayout;
import com.cgbsoft.lib.widget.taglayout.OnTagSelectListener;

import java.util.ArrayList;
import java.util.List;

import app.product.com.R;
import app.product.com.adapter.FilteAdapter;
import app.product.com.model.EventFiltBean;
import app.product.com.model.FilterItem;
import app.product.com.model.Series;
import app.product.com.mvp.presenter.ProductPresenter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/10-19:33
 */
public class FilterPop extends PopupWindow implements View.OnClickListener {
    //单选
    private final String RADIO = "radio";
    //区间编辑
    private final String EDIT = "text";
    //多选
    private final String CHECKBOX = "checkbox";

    Button product_filtepop_reset_filter;
    Button product_filtepop_enter_filter;

    LinearLayout product_filte_linear_layout;
    //数据
    private List<FilterItem> filterItemList;
    //重置时候copy的一份出来随意修改
    private List<FilterItem> tempFilterItemList = new ArrayList<>();
    //存储的列表layout
    private List<View> filelaLayouts;
    //上下文
    private Context pContext;
    //    基础view
    private View baseView;
    //inflat
    private LayoutInflater layoutInflater;
    //是否点击了重置按钮
    private boolean isReset;
    //是否点击确定了
    private boolean isCommint;

    public FilterPop(Context context, List<FilterItem> filterItemList) {
        super(context);
        this.filterItemList = filterItemList;
        this.pContext = context;
        baseView = LayoutInflater.from(pContext).inflate(R.layout.product_filter_pop, null);
        layoutInflater = LayoutInflater.from(pContext);
        initConfig();
        initView(filterItemList);
    }

    /**
     * 初始化配置
     */
    private void initConfig() {
        //配置信息
        baseView.setLayoutParams(new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        //开始初始化
        setContentView(baseView);
        setFocusable(true);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);//SOFT_INPUT_ADJUST_RESIZE
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        setBackgroundDrawable(dw);
        setAnimationStyle(R.style.style_product_oderby_dialog_anim);
        setOutsideTouchable(true);
        product_filte_linear_layout = (LinearLayout) baseView.findViewById(R.id.product_filte_linear_layout);
        product_filtepop_reset_filter = (Button) baseView.findViewById(R.id.product_filtepop_reset_filter);
        product_filtepop_enter_filter = (Button) baseView.findViewById(R.id.product_filtepop_enter_filter);
        product_filtepop_reset_filter.setOnClickListener(this);
        product_filtepop_enter_filter.setOnClickListener(this);
    }

    /**
     * 根据数据初始化view
     */
    public void initView(List<FilterItem> datas) {
        if (product_filte_linear_layout.getChildCount() > 0) {
            product_filte_linear_layout.removeAllViews();
        }
        filelaLayouts = new ArrayList<>();

        for (FilterItem h : datas) {
            product_filte_linear_layout.addView(drawView(h, h.getType()));
            filelaLayouts.add(drawView(h, h.getType()));
            if (datas.indexOf(h) != datas.size() - 1)
                product_filte_linear_layout.addView(getLineview());
        }

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.product_filtepop_reset_filter) //重置按钮 需要假死状态
        {
            isReset = true;
            tempFilterItemList = new ArrayList<>();
            tempFilterItemList = resetView(filterItemList);
            initView(tempFilterItemList);
        }
        if (v.getId() == R.id.product_filtepop_enter_filter) //确定按钮
        {

            List<FilterItem> getData = new ArrayList<>();
            for (int i = 0; i < filelaLayouts.size(); i++) {
                getData.add((FilterItem) filelaLayouts.get(i).getTag());
            }
            RxBus.get().post(ProductPresenter.PRODUCT_FILTER_TO_FRAGMENT, new EventFiltBean(getData));
            this.dismiss();
        }

    }


    private View drawView(final FilterItem h, String Type) {
        View itemView = null;
        TextView titleView;

        switch (Type) {
            case RADIO://单选的
            case CHECKBOX://复选
                itemView = layoutInflater.inflate(R.layout.view_pop_filter_radio, null);
                titleView = ViewHolders.get(itemView, R.id.view_pop_filter_radio_title);
                FlowTagLayout flaFloatView = (FlowTagLayout) itemView.findViewById(R.id.view_pop_filter_radio_floatview);
                //画标题**************************************
                BStrUtils.SetTxt(titleView, h.getName());
                //画tag**************************************
                FilteAdapter filteAdapter = new FilteAdapter(pContext);
                flaFloatView.setTagCheckedMode(RADIO.equals(Type) ? FlowTagLayout.FLOW_TAG_CHECKED_SINGLE : FlowTagLayout.FLOW_TAG_CHECKED_MULTI);
                flaFloatView.setAdapter(filteAdapter);
                flaFloatView.setOnTagSelectListener(new OnTagSelectListener() {
                    @Override
                    public void onItemSelect(FlowTagLayout parent, List<Integer> selectedList) {
                        setviewData(h, selectedList);
                    }
                });
                filteAdapter.onlyAddAll(h.getItems());
                break;
            case EDIT://编辑范围
                itemView = layoutInflater.inflate(R.layout.view_pop_filter_ed, null);
                titleView = ViewHolders.get(itemView, R.id.view_pop_filter_ed_title);
                EditText editTextleft = ViewHolders.get(itemView, R.id.view_pop_filter_ed_edleft);
                EditText editTextright = ViewHolders.get(itemView, R.id.view_pop_filter_ed_edright);
                BStrUtils.SetTxt1(editTextleft, h.getMinNumber());
                BStrUtils.SetTxt1(editTextright, h.getMaxNumber());
                //画标题**************************************
                BStrUtils.SetTxt(titleView, h.getName());
                editTextleft.addTextChangedListener(new EditChangeListene(h, 0));
                editTextright.addTextChangedListener(new EditChangeListene(h, 1));


                break;

        }
        itemView.setTag(h);
        return itemView;
    }

    /**
     * 设置view内部date的标识
     */
    private void setviewData(FilterItem data, List<Integer> selectedLists) {
        //先暴力清楚所有的标识
        for (int i = 0; i < data.getItems().size(); i++) {
            data.getItems().get(i).setChecked(false);
        }
        //再根据记录设置已存的标识
        if (null == selectedLists || selectedLists.size() == 0) return;
        for (int j = 0; j < selectedLists.size(); j++) {
            data.getItems().get(selectedLists.get(j)).setChecked(true);
        }
    }


    /**
     * 画分割线
     *
     * @return
     */
    private View getLineview() {
        View imagview = layoutInflater.inflate(R.layout.product_filter_line, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, DimensionPixelUtil.dip2px(pContext, 0.5f));
        params.setMargins(DimensionPixelUtil.dip2px(pContext, 10), 0, DimensionPixelUtil.dip2px(pContext, 10), 0);
        imagview.setLayoutParams(params);
        return imagview;
    }


    /**
     * 监听输入框后的结果
     */
    public class EditChangeListene implements TextWatcher {
        private FilterItem filterItem;
        //        0标识是最小的  1标识是最大的
        private int editType;

        public EditChangeListene(FilterItem filterItem, int editType) {
            this.filterItem = filterItem;
            this.editType = editType;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
                 filterItemList.get(0);
            if (0 == editType) filterItem.setMinNumber(s.toString());
            if (1 == editType) filterItem.setMaxNumber(s.toString());
        }
    }

    /**
     * 重置时候copy一份数据 保存在另外的指针里面
     * 并且要重置
     *
     * @param
     * @param originItems
     */

    private List<FilterItem> resetView(List<FilterItem> originItems) {
        List<FilterItem> resetFilterDate = new ArrayList<>();
        for (FilterItem h : originItems) {
            FilterItem newFilterItem = new FilterItem();
            newFilterItem.setMaxNumber(h.getMaxNumber());
            newFilterItem.setMinNumber(h.getMaxNumber());

            List<Series> sers = new ArrayList<>();
            for (int i = 0; i < h.getItems().size(); i++) {
                sers.add(new Series(h.getItems().get(i).getName(), h.getItems().get(i).getKey(), h.getItems().get(i).isChecked()));
            }
            newFilterItem.setItems(sers);
            newFilterItem.setKey(h.getKey());
            newFilterItem.setName(h.getName());
            newFilterItem.setType(h.getType());
            resetFilterDate.add(newFilterItem);
        }


        for (FilterItem h : resetFilterDate) {
            for (int i = 0; i < h.getItems().size(); i++) {
                h.getItems().get(i).setChecked(false);
            }
        }
        return resetFilterDate;

    }

    public void showAsDropDown(View anchor, int xoff, int yoff, List<FilterItem> da) {
        super.showAsDropDown(anchor, xoff, yoff);
        filterItemList = da;
        initView(da);
    }
}