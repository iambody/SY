package app.product.com.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.utils.tools.DimensionPixelUtil;

import java.util.List;

import app.product.com.R;
import app.product.com.model.Series;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/8-18:12
 */
public class ProductSeriesLayout extends ViewGroup {
    private final static String TAG = "LineBreakLayout";
    //    private List<Series> lables;
//    private List<Series> lableSelected = new ArrayList<>();
    private int mCellWidth;
    private View currentView;
    private View moreView;
    private boolean isInit = true;
    private Context context;

    public boolean isInit() {
        return isInit;
    }

    public void setInit(boolean init) {
        isInit = init;
    }

    private OnClickTextCallBack onClickTextCallBack;

    public interface OnClickTextCallBack {

        void onTextClick(TextView textView);
    }

    public OnClickTextCallBack getOnClickTextCallBack() {
        return onClickTextCallBack;
    }

    public void setOnClickTextCallBack(OnClickTextCallBack onClickTextCallBack) {
        this.onClickTextCallBack = onClickTextCallBack;
    }

    private void showCompoundDrawable(TextView textView, Drawable drawable) {
        if (textView != null) {
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            textView.setCompoundDrawables(null, null, drawable, null);
        }
    }

    //自定义属性
    private int LEFT_RIGHT_SPACE; //dip
    private int ROW_SPACE;

    public ProductSeriesLayout(Context context) {
        this(context, null);
    }

    public ProductSeriesLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProductSeriesLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LineBreakLayout);
        LEFT_RIGHT_SPACE = ta.getDimensionPixelSize(R.styleable.LineBreakLayout_leftAndRightSpace, 15);
        ROW_SPACE = ta.getDimensionPixelSize(R.styleable.LineBreakLayout_rowSpace, 20);
        mCellWidth = (DimensionPixelUtil.getDisplayWidth(getContext()) - DimensionPixelUtil.dp2px(context,7) * 2 + LEFT_RIGHT_SPACE) / 5 - LEFT_RIGHT_SPACE;
        ta.recycle(); //回收
        // ROW_SPACE=20   LEFT_RIGHT_SPACE=40
        Log.v(TAG, "ROW_SPACE=" + ROW_SPACE + "   LEFT_RIGHT_SPACE=" + LEFT_RIGHT_SPACE);
    }

    /**
     * 添加标签
     *
     * @param target 标签集合
     */
    public void setLables(final List<Series> target, boolean isExtend) {
//        if (this.lables == null) {
//            this.lables = new ArrayList<>();
//        }
        // this.lables.clear();
//        this.lables = target;
        removeAllViews();
        requestLayout();
        if (target != null && target.size() > 0) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            for (final Series lable : target) {
                //获取标签布局
                final TextView tv = (TextView) inflater.inflate(R.layout.item_product_label, null);
                tv.setWidth(mCellWidth);
                tv.setText(lable.getName());
                tv.setGravity(Gravity.CENTER);
                tv.setTag(lable);

                boolean lastValue = isExtendLine(target);

                if (!isExtend && lastValue && (TextUtils.equals("更多", lable.getName()) || TextUtils.equals("收起", lable.getName()))) {
                    tv.setTextColor(AppManager.isInvestor(getContext()) ? getResources().getColor(R.color.app_golden) : getResources().getColor(R.color.color5));
                    moreView = tv;
                }

                if ((TextUtils.equals("更多", lable.getName()) || TextUtils.equals("收起", lable.getName()))) {
                    tv.setText(isExtend ? "收起" : "更多");
                    tv.setPadding(DimensionPixelUtil.dp2px(context,7), DimensionPixelUtil.dp2px(context,5), DimensionPixelUtil.dp2px(context,7), DimensionPixelUtil.dp2px(context,5));
                    showCompoundDrawable(tv, ContextCompat.getDrawable(getContext(), isExtend ? R.drawable.open_up :
                            (!lastValue ? R.drawable.open : AppManager.isInvestor(getContext()) ? R.drawable.open_c : R.drawable.open_b)));
                }

                if (currentView != null && TextUtils.equals(((TextView) currentView).getText().toString(), lable.getName())) {
                    tv.setTextColor(AppManager.isInvestor(getContext()) ? getResources().getColor(R.color.app_golden) : getResources().getColor(R.color.color5));
                    currentView = tv;
                }

                if (isInit && TextUtils.equals(getResources().getString(R.string.product_series_all), lable.getName())) {
                    tv.setTextColor(AppManager.isInvestor(getContext()) ? getResources().getColor(R.color.app_golden) : getResources().getColor(R.color.color5));
                    currentView = tv;
                }

                tv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(AppManager.getUserInfo(context).getToC().getCustomerType())){
                            return;
                        }
                        if (currentView != null && !(TextUtils.equals(((TextView) v).getText().toString(), "更多") || TextUtils.equals(((TextView) v).getText().toString(), "收起"))) {
                            Series series = (Series) v.getTag();
                            if (series.getName().equals(((TextView) currentView).getText().toString())) {
                                return;
                            }
                            currentView.setSelected(false);
                            ((TextView) currentView).setTextColor(getResources().getColor(R.color.tv_gray));
                        }

                        if (moreView != null) {
                            ((TextView) moreView).setTextColor(getResources().getColor(R.color.tv_gray));
                            showCompoundDrawable((TextView) moreView, ContextCompat.getDrawable(getContext(), R.drawable.open));
                        }

                        tv.setSelected(true);
                        if (tv.isSelected()) {
                            tv.setTextColor(AppManager.isInvestor(getContext()) ? getResources().getColor(R.color.app_golden) : getResources().getColor(R.color.color5));
                        } else {
                            tv.setTextColor(getResources().getColor(R.color.tv_gray));
                        }

                        if (!(TextUtils.equals(((TextView) v).getText().toString(), "更多") || TextUtils.equals(((TextView) v).getText().toString(), "收起"))) {
                            currentView = tv;
                        }

                        if (onClickTextCallBack != null) {
                            onClickTextCallBack.onTextClick(tv);
                        }

                    }
                });
                //将标签添加到容器中
                addView(tv);
            }
        }
    }

    private boolean isExtendLine(List<Series> target) {
        boolean isLastLine = true;
        for (Series series : target) {
            if (currentView != null) {
                if (series.getName().equals(((TextView) currentView).getText().toString())) {
                    isLastLine = false;
                }
            }
        }
        return isLastLine;
    }

    /**
     * 获取选中标签
     */
//    public List<Series> getSelectedLables() {
//        return lableSelected;
//    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //为所有的标签childView计算宽和高
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        //获取高的模式
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        //建议的高度
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        //布局的宽度采用建议宽度（match_parent或者size），如果设置wrap_content也是match_parent的效果
        int width = MeasureSpec.getSize(widthMeasureSpec);

        int height;
        if (heightMode == MeasureSpec.EXACTLY) {
            //如果高度模式为EXACTLY（match_perent或者size），则使用建议高度
            height = heightSize;
        } else {
            //其他情况下（AT_MOST、UNSPECIFIED）需要计算计算高度
            int childCount = getChildCount();
            if (childCount <= 0) {
                height = 0;   //没有标签时，高度为0
            } else {
                int row = 1;  // 标签行数
                int widthSpace = width;// 当前行右侧剩余的宽度
                for (int i = 0; i < childCount; i++) {
                    View view = getChildAt(i);
                    //获取标签宽度
                    int childW = view.getMeasuredWidth();
                    Log.v(TAG, "标签宽度:" + childW + " 行数：" + row + "  剩余宽度：" + widthSpace);
                    if (widthSpace >= childW) {
                        //如果剩余的宽度大于此标签的宽度，那就将此标签放到本行
                        widthSpace -= childW;
                    } else {
                        row++;    //增加一行
                        //如果剩余的宽度不能摆放此标签，那就将此标签放入一行
                        widthSpace = width - childW;
                    }
                    //减去标签左右间距
                    widthSpace -= LEFT_RIGHT_SPACE;
                }
                //由于每个标签的高度是相同的，所以直接获取第一个标签的高度即可
                int childH = getChildAt(0).getMeasuredHeight();
                //最终布局的高度=标签高度*行数+行距*(行数-1)
                height = (childH * row) + ROW_SPACE * (row - 1);

                Log.v(TAG, "总高度:" + height + " 行数：" + row + "  标签高度：" + childH);
            }
        }
        System.out.println("--------left=" + getPaddingLeft() + "---TOP=" + getPaddingTop() + "---RIGHT=" + getPaddingRight() + "----BOTTOM=" + getPaddingBottom());
        //设置测量宽度和测量高度
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int row = 0;
        int right = 0;   // 标签相对于布局的右侧位置
        int botom;       // 标签相对于布局的底部位置
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            int childW = childView.getMeasuredWidth();
            int childH = childView.getMeasuredHeight();
            //右侧位置=本行已经占有的位置+当前标签的宽度
            right += childW;
            //底部位置=已经摆放的行数*（标签高度+行距）+当前标签高度
            botom = row * (childH + ROW_SPACE) + childH;
            // 如果右侧位置已经超出布局右边缘，跳到下一行
            // if it can't drawing on a same line , skip to next line
            int lineCount = ((TextView) childView).getLineCount();

            System.out.println("------lineCount=" + lineCount + "-------childH=" + childH);
            if (right > (r - LEFT_RIGHT_SPACE)) {
                row++;
                right = childW;
                botom = row * (childH + ROW_SPACE) + childH;
            }
            Log.d(TAG, "left = " + (right - childW) + " top = " + (botom - childH) +
                    " right = " + right + " botom = " + botom);
            childView.layout(right - childW, botom - childH, right, botom);
            right += LEFT_RIGHT_SPACE;
        }
    }
}
