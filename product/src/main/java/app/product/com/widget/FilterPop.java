package app.product.com.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.tools.BStrUtils;
import com.cgbsoft.lib.utils.tools.DimensionPixelUtil;
import com.cgbsoft.lib.utils.tools.ViewHolders;
import com.cgbsoft.lib.widget.FloatView;
import com.cgbsoft.lib.widget.FlowLayoutView;

import java.util.ArrayList;
import java.util.List;

import app.product.com.R;
import app.product.com.R2;
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
public class FilterPop extends PopupWindow {
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
    //存储的列表layout
    private List<View> filelaLayouts = new ArrayList<>();
    //上下文
    private Context pContext;
    //    基础view
    private View baseView;
    //inflat
    private LayoutInflater layoutInflater;

    public FilterPop(Context context, List<FilterItem> filterItemList) {
        super(context);

        this.filterItemList = filterItemList;
        this.pContext = context;
        baseView = LayoutInflater.from(pContext).inflate(R.layout.product_filter_pop, null);
        layoutInflater = LayoutInflater.from(pContext);
        initConfig();
        initView();
    }

    /**
     * 初始化view
     */
    private void initView() {
        product_filtepop_reset_filter= (Button) baseView.findViewById(R.id.product_filtepop_reset_filter);
        product_filtepop_enter_filter= (Button) baseView.findViewById(R.id.product_filtepop_enter_filter);
        product_filte_linear_layout= (LinearLayout) baseView.findViewById(R.id.product_filte_linear_layout);
        for (FilterItem h : filterItemList) {
            switch (h.getType()) {
                case RADIO://单选的
                case CHECKBOX://复选
                    product_filte_linear_layout.addView(drawView(h,h.getType()));
                    filelaLayouts.add(drawView(h,h.getType()));
                    break;
                case EDIT://编辑范围
                    break;

            }

        }

    }

    private View drawView(FilterItem h, String Type) {
        View itemView = null;
        TextView titleView;

        switch (Type) {
            case RADIO://单选的
            case CHECKBOX://复选

                itemView = layoutInflater.inflate(R.layout.view_pop_filter_radio, null);
                titleView = ViewHolders.get(itemView, R.id.view_pop_filter_radio_title);
                FlowLayoutView flaFloatView = (FlowLayoutView) itemView.findViewById(R.id.view_pop_filter_radio_floatview);

                BStrUtils.SetTxt(titleView, h.getName());
                flaterViewData(flaFloatView, h.getItems());

                break;
            case EDIT://编辑范围
                itemView = layoutInflater.inflate(R.layout.view_pop_filter_radio, null);

                break;

        }
        itemView.setTag(h);
        return itemView;
    }


    /**
     * 为流视图加入元素
     */
    private void flaterViewData(FlowLayoutView floatview, List<Series> data) {
        for (Series h : data) {
            TextView txtView = (TextView) layoutInflater.inflate(R.layout.product_flowview_txt, null);
            ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams();
            txtView.setLayoutParams(lp);
            BStrUtils.SetTxt(txtView, h.getName());
            floatview.addView(txtView);
        }
    }

    /**
     * 初始化配置
     */
    private void initConfig() {
        //配置信息
        baseView.setLayoutParams(new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        //开始初始化
        setContentView(baseView);
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        setBackgroundDrawable(dw);
        setAnimationStyle(R.style.style_product_oderby_dialog_anim);
        setOutsideTouchable(true);
    }

    @OnClick(R2.id.product_filtepop_reset_filter)
    public void onProductFiltepopResetFilterClicked() {//重置
    }

    @OnClick(R2.id.product_filtepop_enter_filter)
    public void onProductFiltepopEnterFilterClicked() {//确定
        RxBus.get().post(ProductPresenter.PRODUCT_FILTER_TO_FRAGMENT, filterItemList);
    }

}
