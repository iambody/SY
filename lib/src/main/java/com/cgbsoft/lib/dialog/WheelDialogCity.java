package com.cgbsoft.lib.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.cgbsoft.lib.R;
import com.cgbsoft.lib.base.model.SalonsEntity;
import com.cgbsoft.lib.widget.OnWheelChangedListener;
import com.cgbsoft.lib.widget.WheelAdapter;
import com.cgbsoft.lib.widget.WheelView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WheelDialogCity extends Dialog implements View.OnClickListener {
	WheelView parent;
	/**
	 * 取消
	 */
	private TextView mTvCancel;
	/**
	 * 确认
	 */
	private TextView mTvConfirm;
	/**
	 * 确认监听
	 */
	private ConfirmListenerInteface confirmCallback;
	/**
	 * 数据源
	 */
//	private List<Map<String, Object>> mList = new ArrayList<Map<String, Object>>();
    private List<SalonsEntity.CityBean> mList = new ArrayList<>();
    /**
     * 选择的基金
     */
    private Map<String, String> result;
    private String titleStr;
    private int currentPosition;
    private SalonsEntity.CityBean parentSelect;

    public void setList(List<SalonsEntity.CityBean> list) {
        mList.clear();
		this.mList.addAll(list);
	}
    public void setTitle(String titleStr){
        this.titleStr=titleStr;
    }
	public WheelDialogCity(Context context, int theme) {
		super(context, theme);
	}

	/**
	 * 设置确认监听回调
	 *
	 * @param confirmCallback
	 */
	public void setConfirmCallback(ConfirmListenerInteface confirmCallback) {
		this.confirmCallback = confirmCallback;
	}

	public WheelDialogCity(Context context) {
		this(context, R.style.address_style);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wheel_dialog_city);
		getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		getWindow().setGravity(Gravity.BOTTOM);
		setCanceledOnTouchOutside(true);
		initView();
	}

	private void initView() {
        TextView title = (TextView) findViewById(R.id.tv_title);
        title.setText(titleStr);
        mTvCancel = (TextView) this.findViewById(R.id.tv_cancel);
        mTvCancel.setOnClickListener(this);
        mTvConfirm = (TextView) this.findViewById(R.id.tv_confirm);
        mTvConfirm.setOnClickListener(this);
        parent = (WheelView) this.findViewById(R.id.parent);
        parent.setVisibleItems(5);
        parent.setCyclic(false);
        parent.setAdapter(new WheelAdapter() {
            @Override
            public int getMaximumLength() {
                return mList.size();
            }

            @Override
            public int getItemsCount() {
                return mList.size();
            }

            @Override
            public String getItem(int index) {
                SalonsEntity.CityBean cityBean = mList.get(index);
                return cityBean.getText();
            }
        });
        parent.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                currentPosition = newValue;
                parentSelect = mList.get(newValue);
            }
        });
    }

    public interface ConfirmListenerInteface {
		void confirm(SalonsEntity.CityBean result);
	}
	@Override
	public void onClick(View v) {
        if (v.getId() == R.id.tv_cancel) {// 取消
			cancel();
        } else if (v.getId() == R.id.tv_confirm) {// 确认
			if (confirmCallback != null) {
                SalonsEntity.CityBean result;
                if (parentSelect != null) {
                    result=parentSelect;
				} else {
                    result = mList.get(0);
                }
                confirmCallback.confirm(result);
            }
			cancel();
        }
//        switch (v.getId()) {
//		case R.id.tv_cancel: // 取消
//			break;
//		case R.id.tv_confirm:// 确认
//			break;
//		default:
//			break;
//		}
	}
}
