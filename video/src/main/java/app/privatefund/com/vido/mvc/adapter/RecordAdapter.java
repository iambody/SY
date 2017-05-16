package app.privatefund.com.vido.mvc.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cgbsoft.lib.AppManager;
import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;
import java.util.HashMap;

import app.privatefund.com.vido.R;
import app.privatefund.com.vido.mvc.bean.SchoolVideo;
import app.privatefund.com.vido.mvc.utils.ToolsUtils;
import io.rong.eventbus.EventBus;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/15-14:12
 */
public class RecordAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<SchoolVideo> list;
    private boolean isShowCheck = false;
    private static HashMap<String, Boolean> isSelected;

    public RecordAdapter(Context context, ArrayList<SchoolVideo> list) {
        this.context = context;
        this.list = list;
        isSelected = new HashMap<String, Boolean>();
        initDate();
    }

    public void initDate() {
        for (int i = 0; i < list.size(); i++) {
            getIsSelected().put(list.get(i).getTencentVideoId(), false);
        }
    }

    public static HashMap<String, Boolean> getIsSelected() {
        return isSelected;
    }

    public static void setIsSelected(HashMap<String, Boolean> isSelected) {
        RecordAdapter.isSelected = isSelected;
    }

    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView != null) {
            view = convertView;
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.playrecord_item, parent, false);
        }
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        if (viewHolder == null) {
            viewHolder = new ViewHolder();
            viewHolder.linearLayout = (LinearLayout) view.findViewById(R.id.play_record_layout);
            viewHolder.checkBox = (CheckBox) view.findViewById(R.id.play_record_select);
            viewHolder.history = (TextView) view.findViewById(R.id.play_location);
            viewHolder.title = (TextView) view.findViewById(R.id.play_record_title);
            viewHolder.imageView = (ImageView) view.findViewById(R.id.play_record_image);
            view.setTag(viewHolder);
        }

        if (isShowCheck) {
            viewHolder.checkBox.setVisibility(View.VISIBLE);
        } else {
            viewHolder.checkBox.setVisibility(View.GONE);
        }
//TODO 历史记录展示 已播放
//        viewHolder.history.setText(String.format(" 已播放 %s", DataUtil.long2Data(list.get(position).getCurrentTime() * 1000)));
        BitmapUtils bu = new BitmapUtils(context);
        bu.display(viewHolder.imageView, list.get(position).getCoverImageUrl());
        viewHolder.title.setText(list.get(position).getVideoName());
        Log.e("Record", isSelected.toString());
        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isSelected.put(list.get(position).getTencentVideoId(), true);
                    Log.e("Record", isSelected.toString());
//                    EventBus.getDefault().post(new RecordEvent(getCheckCount()));
                    buttonView.setButtonDrawable(ContextCompat.getDrawable(context, AppManager.isInvestor(context) ? R.drawable.video_check_c : R.drawable.checkbox_pressed));
                } else {
                    isSelected.put(list.get(position).getTencentVideoId(), false);
                    Log.e("Record", isSelected.toString());
                    //todo  播放历史发送eventbus
//                    EventBus.getDefault().post(new RecordEvent(getCheckCount()));
                    buttonView.setButtonDrawable(ContextCompat.getDrawable(context,R.drawable.checkbox_normal));
                }
            }
        });
        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShowCheck) {
                    if (finalViewHolder.checkBox.isChecked()) {
                        isSelected.put(list.get(position).getTencentVideoId(), false);
                        Log.e("Record", isSelected.toString());
                        finalViewHolder.checkBox.setChecked(false);
                        //todo  播放历史发送eventbus
//                        EventBus.getDefault().post(new RecordEvent(getCheckCount()));
                    } else {
                        isSelected.put(list.get(position).getTencentVideoId(), true);
                        Log.e("Record", isSelected.toString());
                        finalViewHolder.checkBox.setChecked(true);
//                        EventBus.getDefault().post(new RecordEvent(getCheckCount()));
                    }
                }else {
                    ToolsUtils.toPlayVideoActivity(context, list.get(position));

                }
            }
        });
        if (isSelected.get(list.get(position).getTencentVideoId())) {
            viewHolder.checkBox.setChecked(true);
        } else {
            viewHolder.checkBox.setChecked(false);
        }
        return view;
    }


    private int getCheckCount() {
        int i = 0;
        for (String id : isSelected.keySet()) {
            if (isSelected.get(id)) {
                i++;
            }
        }
        return i;
    }

    public HashMap<String, Boolean> getDelete() {
        return isSelected;
    }

    public void showCheck() {
        isShowCheck = true;
    }

    public void hideCheck() {
        isShowCheck = false;
    }

    public void choiceAll() {
        for (int i = 0; i < isSelected.size(); i++) {
            isSelected.put(list.get(i).getTencentVideoId(), true);
        }
        notifyDataSetChanged();
    }

    public void cancelChoiceAll() {
        for (int i = 0; i < isSelected.size(); i++) {
            isSelected.put(list.get(i).getTencentVideoId(), false);
        }
        notifyDataSetChanged();
    }

    public void removeCheck(){
        isSelected.clear();
        initDate();
    }

    class ViewHolder {
        LinearLayout linearLayout;
        CheckBox checkBox;
        ImageView imageView;
        TextView title;
        TextView history;
    }
}
