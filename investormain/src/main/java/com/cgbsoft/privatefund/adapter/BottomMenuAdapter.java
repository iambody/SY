package com.cgbsoft.privatefund.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cgbsoft.privatefund.R;


/**
 * 
 * 为自定义Dialog提供的适配器（客服热线，理财师电话，再投产品详情的更多按钮有使用）
 * @author liu
 *
 */
public class BottomMenuAdapter extends BaseAdapter {
	
	
	private Context mContext;
	
	private String[] mString;
	
	private int mTextColor;
	
	private boolean mUseTextColor;
	
	private boolean isWeixin = false;// 是否微信分享
	
	public BottomMenuAdapter(Context context){
		mContext = context;
	}
	
	/**
	 * 微信页面过来
	 * @param context
	 * @param string
	 */
	public BottomMenuAdapter(Context context, String string) {
		mContext = context;
		if("微信".equals(string)) {
			isWeixin = true;
		}
	}

	public void setData(String[] array){
		mString = array;
		notifyDataSetChanged();
	}
	
	public void setTextColor(int color,boolean useOrNot){
		mUseTextColor = useOrNot;
		mTextColor = color;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mString != null ? mString.length : 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = View.inflate(mContext, R.layout.bm_menu_item_layout, null);
		}
		TextView tv = (TextView)convertView.findViewById(R.id.item);
		tv.setText(mString[position]);
		
		return convertView;
	}

}
