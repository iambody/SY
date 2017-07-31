package qcloud.liveold.mvp.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import qcloud.liveold.R;
import qcloud.liveold.mvp.model.PDFLiveBean;

/**
 * desc
 * Created by yangzonghui on 2017/7/30 14:40
 * Email:yangzonghui@simuyun.com
 *  
 */
public class PDFListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<PDFLiveBean> pdfBeans;

    public PDFListAdapter(Context context, ArrayList<PDFLiveBean> pdfBeans) {
        this.context = context;
        this.pdfBeans = pdfBeans;
    }

    @Override
    public int getCount() {
        if (pdfBeans == null) {
            return 0;
        } else {
            return pdfBeans.size();
        }
    }

    @Override
    public Object getItem(int position) {
        return pdfBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.pdf_item, parent, false);
        } else {
            view = convertView;
        }
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        if (viewHolder == null) {
            viewHolder = new ViewHolder();
            viewHolder.pdfName = (TextView) view.findViewById(R.id.pdf_name);
            view.setTag(viewHolder);
        }
        viewHolder.pdfName.setText(pdfBeans.get(position).getName());
        return view;
    }

    class ViewHolder {
        private TextView pdfName;
    }
}
