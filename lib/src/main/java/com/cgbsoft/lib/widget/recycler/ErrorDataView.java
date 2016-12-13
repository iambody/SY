package com.cgbsoft.lib.widget.recycler;


import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cgbsoft.lib.R;
import com.cgbsoft.lib.utils.tools.Utils;

import butterknife.ButterKnife;

/**
 * Created by win8 -1 on 2015/9/1.
 */
public class ErrorDataView {
    private final String TAG = "ErrorDataView";
    private View parent;
    private Context mContext;
    private int size;
    private LinearLayout noDataLinearLayout;
    private ImageView noDataIv;
    private TextView noDataTv;

    private OnErrorDataListener mOnErrorDataListener;

    public static final int ERROR_NET = 0x010;
    public static final int ERROR_NODATA = 0x020;
    public static final int ERROR_NODRAFT = 0x030;
    public static final int ERROR_NONE = 0x0100;


    public ErrorDataView(Context context) {
        mContext = context;
        parent = LayoutInflater.from(context).inflate(R.layout.layout_no_data, null);
        init();
        data();
    }

    public ErrorDataView(Context context, View view) {
        mContext = context;
        parent = view;
        init();
        data();
    }

    private void init() {
        noDataLinearLayout = ButterKnife.findById(parent, R.id.noDataLinearLayout);
        noDataIv = ButterKnife.findById(parent, R.id.noDataIv);
        noDataTv = ButterKnife.findById(parent, R.id.noDataTv);
        size = Utils.convertDipOrPx(mContext, 150);
    }

    private void data() {
        noDataLinearLayout.setOnClickListener(v -> {
            if (mOnErrorDataListener == null) {
                Log.e(TAG, "请实现OnErrorDataListener");
                return;
            }
            mOnErrorDataListener.errorDataClickListener();
        });
        noDataIv.setOnClickListener(v -> {
            if (mOnErrorDataListener == null) {
                Log.e(TAG, "请实现OnErrorDataListener");
                return;
            }
            mOnErrorDataListener.errorDataClickListener();
        });
    }

    public void setErrorImgSize(int size) {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) noDataIv.getLayoutParams();
        lp.width = size;
        lp.height = size;
        noDataIv.setLayoutParams(lp);
    }

    public void setData(int which) {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) noDataIv.getLayoutParams();
        lp.width = size;
        lp.height = size;
        noDataIv.setLayoutParams(lp);
        noDataIv.setVisibility(View.VISIBLE);

        switch (which) {
            case ERROR_NET:
                noDataIv.setImageResource(R.drawable.no_network);
                noDataTv.setText("网络错误，点击重新加载~");
                break;
            case ERROR_NODATA:
                noDataIv.setVisibility(View.GONE);
                noDataTv.setText("暂时没有数据");
                break;
            case ERROR_NODRAFT:
                noDataIv.setVisibility(View.GONE);
                noDataTv.setText("没准备好的时候，可以存到这里~");
                break;


        }
    }

    public void setData(int which, int px) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) noDataLinearLayout.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, 0);
        noDataLinearLayout.setLayoutParams(layoutParams);

        if (px > 0) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) noDataIv.getLayoutParams();
            lp.width = px;
            lp.height = px;
            noDataIv.setLayoutParams(lp);
        } else {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) noDataIv.getLayoutParams();
            lp.width = Utils.convertDipOrPx(mContext, 250);
            lp.height = Utils.convertDipOrPx(mContext, 250);
            noDataIv.setLayoutParams(lp);
        }
        switch (which) {
            case ERROR_NET:
                noDataIv.setVisibility(View.VISIBLE);
                noDataIv.setImageResource(R.drawable.no_network);
                noDataTv.setText("网络错误，点击重新加载~");
                break;
            case ERROR_NODATA:
                noDataIv.setVisibility(View.GONE);
                noDataTv.setText("暂时没有数据");
                break;

        }
    }

    public void setVideoNoData(String text) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) noDataLinearLayout.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, 0);
        noDataLinearLayout.setLayoutParams(layoutParams);

        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) noDataIv.getLayoutParams();
        lp.width = size;
        lp.height = size;
        noDataIv.setLayoutParams(lp);
        noDataIv.setVisibility(View.GONE);
        noDataTv.setText(text);
    }

    public void setData(int pxSize, int imageResId, String text, String btnText, int btnWidth, int btnHeight) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) noDataLinearLayout.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, 0);
        noDataLinearLayout.setLayoutParams(layoutParams);

        if (pxSize > 0) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) noDataIv.getLayoutParams();
            lp.width = pxSize;
            lp.height = pxSize;
            noDataIv.setLayoutParams(lp);
        } else {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) noDataIv.getLayoutParams();
            lp.width = Utils.convertDipOrPx(mContext, 250);
            lp.height = Utils.convertDipOrPx(mContext, 250);
            noDataIv.setLayoutParams(lp);
        }
        if (imageResId > 0) {
            noDataIv.setImageResource(imageResId);
        }
        if (imageResId == 0) {
            noDataIv.setVisibility(View.GONE);
        }
        if (text != null) {
            noDataTv.setText(text);
        }
    }

    public void setData(int pxSize, int imageResId, String text) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) noDataLinearLayout.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, 0);
        noDataLinearLayout.setLayoutParams(layoutParams);

        if (pxSize > 0) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) noDataIv.getLayoutParams();
            lp.width = pxSize;
            lp.height = pxSize;
            noDataIv.setLayoutParams(lp);
        } else {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) noDataIv.getLayoutParams();
            lp.width = Utils.convertDipOrPx(mContext, 250);
            lp.height = Utils.convertDipOrPx(mContext, 250);
            noDataIv.setLayoutParams(lp);
        }
        if (imageResId > 0) {
            noDataIv.setImageResource(imageResId);
        } else {
            noDataIv.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(text)) {
            noDataTv.setText(text);
            noDataTv.setVisibility(View.VISIBLE);
        }

    }

    public void setData(int marginTop, int pxSize, int imageResId, String text) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) noDataLinearLayout.getLayoutParams();
        layoutParams.setMargins(0, Utils.convertDipOrPx(mContext, marginTop), 0, 0);
        noDataLinearLayout.setLayoutParams(layoutParams);

        if (pxSize > 0) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) noDataIv.getLayoutParams();
            lp.width = pxSize;
            lp.height = pxSize;
            noDataIv.setLayoutParams(lp);
        } else {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) noDataIv.getLayoutParams();
            lp.width = Utils.convertDipOrPx(mContext, 250);
            lp.height = Utils.convertDipOrPx(mContext, 250);
            noDataIv.setLayoutParams(lp);
        }
        if (imageResId > 0) {
            noDataIv.setImageResource(imageResId);
        } else {
            noDataIv.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(text)) {
            noDataTv.setText(text);
        }
    }

    public View getErrorDataView() {
        return parent;
    }


    public void destory() {
        mContext = null;
        parent = null;
    }

    public void setOnErrorDataListener(OnErrorDataListener onErrorDataListener) {
        mOnErrorDataListener = onErrorDataListener;
    }

    public interface OnErrorDataListener {
        void errorDataClickListener();
    }

}






















