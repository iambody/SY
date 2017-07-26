package com.cgbsoft.lib.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.cgbsoft.lib.R;
import com.cgbsoft.lib.widget.OnWheelChangedListener;
import com.cgbsoft.lib.widget.WheelAdapter;
import com.cgbsoft.lib.widget.WheelView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WheelDialogAddress extends Dialog implements View.OnClickListener {
	WheelView parent,child,grandson;
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
	private List<Map<String, Object>> mList = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> childList = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> grandSonList = new ArrayList<Map<String, Object>>();
    /**
     * 选择的基金
     */
    private Map<String, String> result;
    private String titleStr;
    private int currentPosition;
    private int childCurrentPosition=0;
    private int grandSonCurrentPosition=0;
    private Map<String, Object> parentSelect;

    public void setList(List<Map<String, Object>> list) {
        mList.clear();
		this.mList.addAll(list);
	}
    public void setTitle(String titleStr){
        this.titleStr=titleStr;
    }
	public WheelDialogAddress(Context context, int theme) {
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

	public WheelDialogAddress(Context context) {
		this(context, R.style.address_style);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wheel_dialog_address);
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
        child = (WheelView) findViewById(R.id.child);
        child.setVisibleItems(5);
        grandson = (WheelView) findViewById(R.id.grandson);
        grandson.setVisibleItems(5);
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
                Map<String, Object> map = mList.get(index);
                Set<Map.Entry<String, Object>> entries = map.entrySet();
                Iterator<Map.Entry<String, Object>> iterators = entries.iterator();
                Map.Entry<String, Object> entry = iterators.next();
                String key = entry.getKey();
                if (!TextUtils.isEmpty(key) && key.equals("sub") && iterators.hasNext()) {
                    entry = iterators.next();
                }
                return entry.getKey();
            }
        });
        setChildAdapter(0);
        parent.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                currentPosition = newValue;
                parentSelect = mList.get(newValue);
                setChildAdapter(newValue);
            }
        });
        child.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                childCurrentPosition =newValue;
            }
        });
        grandson.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                grandSonCurrentPosition=newValue;
            }
        });
    }

    private void setChildAdapter(int newValue) {
        Map<String, Object> map = mList.get(newValue);
        List<Map<String, Object>> childListTemp = (List<Map<String, Object>>) map.get("sub");
        childList.clear();
        if (null == childListTemp) {
            childListTemp=new ArrayList<Map<String, Object>>();
            HashMap<String, Object> tempMap = new HashMap<>();
            tempMap.put("暂无", "000000");
            ArrayList<Map<String, Object>> tempList = new ArrayList<>();
            HashMap<String, Object> tempMap2 = new HashMap<>();
            tempMap2.put("暂无", "000000");
            tempList.add(tempMap2);
            tempMap.put("sub", tempList);
            childListTemp.add(tempMap);
        }
        childList.addAll(childListTemp);
        if (childList != null && childList.size() > 0) {
            child.setAdapter(new ChildAdapter());
            child.setCurrentItem(0);
            childCurrentPosition=0;
            setGrandSonAdapter(0);
        }
    }
    private void setGrandSonAdapter(int newValue) {
        Map<String, Object> map = childList.get(newValue);
        List<Map<String, Object>> grandSonListTemp = (List<Map<String, Object>>) map.get("sub");
        grandSonList.clear();
        if (null == grandSonListTemp) {
            grandSonListTemp=new ArrayList<Map<String, Object>>();
            HashMap<String, Object> tempMap = new HashMap<>();
            tempMap.put("暂无", "000000");
            grandSonListTemp.add(tempMap);
        }
        grandSonList.addAll(grandSonListTemp);
        if (grandSonList != null && grandSonList.size() > 0) {
            grandson.setAdapter(new GrandSonAdapter());
            grandson.setCurrentItem(0);
            grandSonCurrentPosition=0;
        }
    }



    public interface ConfirmListenerInteface {
		void confirm(Map<String, Object> result);
	}
    class ChildAdapter implements WheelAdapter{

        @Override
        public int getItemsCount() {
            return childList.size();
        }

        @Override
        public String getItem(int index) {
            Map<String, Object> map = childList.get(index);
            Set<Map.Entry<String, Object>> entries = map.entrySet();
            Map.Entry<String, Object> entry = entries.iterator().next();
            return entry.getKey();
        }

        @Override
        public int getMaximumLength() {
            return childList.size();
        }
    }
    class GrandSonAdapter implements WheelAdapter{

        @Override
        public int getItemsCount() {
            return grandSonList.size();
        }

        @Override
        public String getItem(int index) {
            Map<String, Object> map = grandSonList.get(index);
            Set<Map.Entry<String, Object>> entries = map.entrySet();
            Map.Entry<String, Object> entry = entries.iterator().next();
            return entry.getKey();
        }

        @Override
        public int getMaximumLength() {
            return grandSonList.size();
        }
    }
	@Override
	public void onClick(View v) {
        if (v.getId() == R.id.tv_cancel) {// 取消
			cancel();
        } else if (v.getId() == R.id.tv_confirm) {// 确认
			if (confirmCallback != null) {
                Map<String,Object> result;
                if (parentSelect != null) {
                    result=parentSelect;
				} else {
                    result = mList.get(0);
                }
                result.put("child_position",childCurrentPosition+"");
                result.put("grandson_position",grandSonCurrentPosition+"");
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
