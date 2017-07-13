package app.product.com.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.tools.BStrUtils;
import com.cgbsoft.lib.utils.tools.ViewHolders;

import java.util.ArrayList;
import java.util.List;

import app.product.com.R;
import app.product.com.model.Series;
import app.product.com.mvp.presenter.ProductPresenter;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/10-15:53
 */
public class OrderbyPop extends PopupWindow {
    //系类数据
    private List<Series> prductoderby;
    //上下文
    private Context dcContext;

    private View baseView;
    private ListView lPopListview;
    private OrderbyAp orderbyAp;

    private String OderBy;
    public OrderbyPop(Context context, List<Series> data,String CurrentOderBy) {
//        super(context,R.style.style_product_oderby_dialog);
        super(context);
        this.dcContext = context;
        this.prductoderby = data;
        this.OderBy=CurrentOderBy;
        baseView = LayoutInflater.from(dcContext).inflate(R.layout.product_orderbydialog, null);
        initConfig();
        initview();
    }



    /**
     * 进行配置
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
//        setAnimationStyle(R.style.style_product_oderby_dialog_anim);
        setOutsideTouchable(true);
        baseView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int lsBottomY=lPopListview.getBottom();
                int touchY= (int) event.getY();
                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    if(touchY>lsBottomY){
                        dismiss();
                    }
                }
                return true;
            }
        });


    }

    /**
     * 初始化veiw
     */
    private void initview() {
        lPopListview= (ListView) baseView.findViewById(R.id.product_productfragment_orderbyls);
        orderbyAp=new OrderbyAp(dcContext,prductoderby);
        lPopListview.setAdapter(orderbyAp);
        lPopListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RxBus.get().post(ProductPresenter.PRODUCT_ORDERBY_TO_FRAGMENT,prductoderby.get(position));//register(ProductPresenter.PRODUCT_ORDERBY_TO_FRAGMEN
                OrderbyPop.this.dismiss();
            }
        });
    }
    private class OrderbyAp extends BaseAdapter {
        private Context mContext;
        private List<Series> mBeans = new ArrayList<Series>();

        public OrderbyAp(Context mContext,List<Series> lsx) {
            super();
            this.mContext = mContext;
            this.mBeans = lsx;
        }



        @Override
        public int getCount() {
            return mBeans.size();
        }

        @Override
        public Object getItem(int position) {
            return mBeans.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Item mItem = null;
            if (null == convertView) {
                mItem = new Item();
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.product_item_orderbypop, null);
                mItem.product_item_orderby_orderbytxt = ViewHolders.get(convertView,
                        R.id.product_item_orderby_orderbytxt);
                convertView.setTag(mItem);
            } else {
                mItem = (Item) convertView.getTag();
            }
            Series da = mBeans.get(position);
            BStrUtils.SetTxt(mItem.product_item_orderby_orderbytxt, da.getName());

                mItem.product_item_orderby_orderbytxt.setTextColor(dcContext.getResources().getColor(!BStrUtils.isEmpty(OderBy)&&OderBy.equals(da.getKey())?R.color.app_golden:R.color.black));

            return convertView;
        }

        private class Item {
            TextView product_item_orderby_orderbytxt;
        }

    }


}
