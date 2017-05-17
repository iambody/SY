package app.privatefund.com.vido.mvc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.BaseApplication;
import com.cgbsoft.lib.base.mvc.BaseMvcActivity;
import com.cgbsoft.lib.widget.MToast;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.http.HttpHandler;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import app.privatefund.com.vido.R;
import app.privatefund.com.vido.mvc.bean.SchoolVideo;
import app.privatefund.com.vido.mvc.services.DownloadService;
import app.privatefund.com.vido.mvc.utils.ToolsUtils;

/**
 * desc 下载记录页面
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/12-20:43
 */
public class LocalVideoActivity extends BaseMvcActivity implements View.OnClickListener {

    private static final String TAG = LocalVideoActivity.class.getSimpleName();
    private ListView listview;
    private ImageView start;
    private TextView startTitle, spaceTitle;
    private LocalVideoAdapter adapter;
    private LinearLayout spaceLayout, selectLayout, stopLayout, deleteLayout, allLayout, startLayout, bottomLayout;
    private RelativeLayout noLayout;
    private TextView allTitle, deleteTitle;
    private ListView listView;
    private List<SchoolVideo> listData = new ArrayList<SchoolVideo>();
    private List<Integer> listSelect = new ArrayList<Integer>();
    private List<Integer> listSelectDel = new ArrayList<Integer>();
//    private DatabaseUtils dbUtils;
    private double sdTotalSize;
    private double sdFreeSize;
    private boolean deleteFlag;
    //    private int deleteCount = 0;
    private PercentReceiver percentReceiver;
    private static final int PERCENT_MSG = 100;
    private static final int COMPLETE_MSG = 101;
    private int percent;
    private String tencentVideoId = null;
    private boolean allFlag = false; //全选标准位
    private float speed;
    private boolean complete = false;
    private static HttpHandler httpHandler;
    private boolean stopFlag = false;
    private boolean startFlag = false;
    private boolean itemStopFlag = false; //点击一行停止现有的下载
    private int downloadtype;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_video);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initRegisterTitleBar();
        showTileLeft();
        titleRight.setVisibility(View.VISIBLE);
        showTileMid(getString(R.string.local_video_title));
        bindView();
        bindData();
    }

    private void bindView() {
        listview = (ListView) findViewById(R.id.local_video_listview);
        start = (ImageView) findViewById(R.id.local_video_start);
        startTitle = (TextView) findViewById(R.id.local_video_start_title);
        spaceTitle = (TextView) findViewById(R.id.local_video_space_title);
        listView = (ListView) findViewById(R.id.local_video_listview);
        spaceLayout = (LinearLayout) findViewById(R.id.local_video_space_layout);
        selectLayout = (LinearLayout) findViewById(R.id.select_delete_layout);
        stopLayout = (LinearLayout) findViewById(R.id.local_video_stop_layout);
        startLayout = (LinearLayout) findViewById(R.id.local_video_start_layout);
        noLayout = (RelativeLayout) findViewById(R.id.local_video_no);
        bottomLayout = (LinearLayout) findViewById(R.id.local_video_bottom_layout);

        allTitle = (TextView) findViewById(R.id.all_title);
        deleteTitle = (TextView) findViewById(R.id.delete_title);
        deleteLayout = (LinearLayout) findViewById(R.id.delete_layout);
        allLayout = (LinearLayout) findViewById(R.id.all_layout);

        adapter = new LocalVideoAdapter(this);
        listView.setAdapter(adapter);

        setDeleteViewDisable();

        deleteLayout.setOnClickListener(this);
        allLayout.setOnClickListener(this);
        titleRight.setOnClickListener(this);
        startLayout.setOnClickListener(this);

        selectLayout.setVisibility(View.GONE);
        titleRight.setBackgroundResource(R.drawable.local_video_delete);

        if (AppManager.isAdViser(baseContext)) {
            deleteTitle.setTextColor(0xffea1202);
        } else {
            deleteTitle.setTextColor(0xfff47900);
        }
    }

    private void bindData() {
//        dbUtils = new DatabaseUtils(this);
        getSDCardSize();
        percentReceiver = new PercentReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_PERCENT);
        registerReceiver(percentReceiver, filter);

        if (null == ToolsUtils.getInstance().handler) {
            setStopLayoutTextShow();
            startFlag = false;
        } else {
            setStartLayoutTextShow();
            startFlag = true;
        }
    }

    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        getAllVideo();
        adapter.notifyDataSetChanged();
        noVideoList();
    }

    public void onClick(View v) {
        if(R.id.title_right==v.getId()){
            if (spaceLayout.getVisibility() == View.VISIBLE) {
                spaceLayout.setVisibility(View.GONE);
                stopLayout.setVisibility(View.GONE);
                selectLayout.setVisibility(View.VISIBLE);
                titleRight.setBackgroundResource(R.color.white);
                setDeleteViewDisable();
                titleRight.setText("取消");
                deleteFlag = true;
                allFlag = false;
                allTitle.setText(R.string.local_video_all);
            } else {
                setSpaceLayoutVisible();
                listSelect.clear();

                Display display = getWindowManager().getDefaultDisplay();
                int width = display.getWidth();
                int height = display.getHeight();

                Rect outRect = new Rect();
                getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);
                int appheight = outRect.height();
                int appwidth = outRect.width();
                Log.i(TAG, "width=" + width + ",height=" + height + ",appwidth=" + appwidth + "appheight=" + appheight + ",high=" + (height - appheight));


                getAllVideo();

                Rect frame = new Rect();
                getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
                int statusBarHeight = frame.top;
                Log.i(TAG, "statusBarHeight=" + statusBarHeight);
            }
            adapter.notifyDataSetChanged();
        }else if(R.id.delete_layout==v.getId()){
            try {
                if (null != listSelect && listSelect.size() > 0) {

                    computerSelectDel();

                    for (final Integer index : listSelectDel) {
                        Log.i(TAG, "delete_layout index=" + index);
                        if (null != tencentVideoId) {
                            if (listData.get(index).getTencentVideoId().equals(tencentVideoId)) {
                                ToolsUtils.setCancelHttpHandler();
                            }
                        }

//                        String currentVideoId = SPSave.getInstance(MApplication.mContext).getString(Contant.currentTencentVideoId);
                        final String videoid = listData.get(index).getTencentVideoId();
                        final int type = listData.get(index).getDownloadtype();
                        int status = listData.get(index).getStatus();
//                        if (videoid.equals(currentVideoId) && status == 2) { //当前播放视频，并且下载完成删除的时候
//                            new iOSDialog(LocalVideoActivity.this, "", "是否要删除正在播放的视频文件？", "取消", "确定") {
//
//                                public void left() {
//                                    this.cancel();
//                                }
//
//                                public void right() {
//                                    ToolsUtils.serviceStop(LocalVideoActivity.this);
//                                    SchoolVideo video = dbUtils.getSchoolVideo(videoid);
//                                    deleteFileAndDB(videoid, type, video);
//                                    refreshAdapter();
//                                    this.dismiss();
//                                }
//                            }.show();
//                        } else {
//                            deleteFileAndDB(videoid, type, listData.get(index));
//                        }
                    }

                    refreshAdapter();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(v.getId()==R.id.all_layout){
            if (!allFlag) {
                addAllSelectList();
                setDeleteViewEnable();
                allTitle.setText(R.string.local_video_cancel_all);
                allFlag = true;
            } else {
                listSelect.clear();
                setDeleteViewDisable();
                Log.i(TAG, "listSelect size=" + listSelect.size());
                allTitle.setText(R.string.local_video_all);
                allFlag = false;
            }
            adapter.notifyDataSetChanged();
        }else if(v.getId()==R.id.local_video_start_layout){
            if (startFlag) {
                ToolsUtils.setCancelHttpHandler();
                updateStopImage();
                setStopLayoutTextShow();
            } else {
                startNewDownload();
                setStartLayoutTextShow();
            }
        }


    }

    public void getSDCardSize() {

        String sDcString = android.os.Environment.getExternalStorageState();
        if (sDcString.equals(android.os.Environment.MEDIA_MOUNTED)) {
            File pathFile = android.os.Environment.getExternalStorageDirectory();
            StatFs statfs = new StatFs(pathFile.getPath());
            long blockSize = statfs.getBlockSize();                         //获取block的size
            float totalBlocks = statfs.getBlockCount();                     //获取block的总数
            float totalGbSize = blockSize * totalBlocks;
            DecimalFormat df = new DecimalFormat("#0.00");
            String totalGbSizeStr = df.format(totalGbSize);                //总共大小
            String[] totalStr = fileSize(totalGbSize);
            long availableBlocks = statfs.getAvailableBlocks();             //获取可用块大小
            String[] avaiStr = fileSize(availableBlocks * blockSize);
//            String usedTotalGbSizeStr = df.format((totalBlocks - availableBlocks) * blockSize /1024/1024/1024);//已用大小
//            String[] usedStr = fileSize((totalBlocks - availableBlocks) * blockSize);
//            spaceTitle.setText(getString(R.string.local_video_all_space)+totalGbSizeStr+"G/"+getString(R.string.local_video_free_space)+usedTotalGbSizeStr+"G");
            spaceTitle.setText(getString(R.string.local_video_all_space) + totalStr[0] + totalStr[1] + "/"
                    + getString(R.string.local_video_free_space) + avaiStr[0] + avaiStr[1]);
        } else {
            getROMSize();
        }
    }

    private void getROMSize() {
        String path = Environment.getDataDirectory().getPath();
        StatFs statFs = new StatFs(path);
        long blockSize = statFs.getBlockSize();
        float totalBlocks = statFs.getBlockCount();
        long availableBlocks = statFs.getAvailableBlocks();
        float usedBlocks = totalBlocks - availableBlocks;

        //处理存储容量格式
        String[] total = fileSize(totalBlocks * blockSize);
        String[] available = fileSize(availableBlocks * blockSize);
        String[] used = fileSize(usedBlocks * blockSize);

        int ss = Integer.parseInt(used[0]);
        int mm = Integer.parseInt(total[0]);
        int tt = ss * 100 / mm;

    }

    private String[] fileSize(float size) {
        String str = "";
        if (size >= 1024) {
            str = "K";
            size /= 1024;
            if (size >= 1024) {
                str = "M";
                size /= 1024;
                if (size >= 1024) {
                    str = "G";
                    size /= 1024;
                }
            }
        }
        DecimalFormat formatter = new DecimalFormat("#0.00");
        formatter.setGroupingSize(3);
        String[] result = new String[2];
        result[0] = formatter.format(size);
        result[1] = str;
        return result;
    }

    public class LocalVideoAdapter extends BaseAdapter {

        private ViewHolder holder;
        //        private static final String TAG = LocalVideoAdapter.class.getSimpleName();
        private Context context;
//        private List<SchoolVideo> list;

        public LocalVideoAdapter(Context context) {
            this.context = context;
        }

        public int getCount() {
            return listData.size();
        }

        public Object getItem(int position) {
            return listData.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = null;
            if (convertView == null) {
                view = LayoutInflater.from(context).inflate(R.layout.local_video_list_item, parent, false);
            } else {
                view = convertView;
            }
            holder = (ViewHolder) view.getTag();
            if (holder == null) {
                holder = new ViewHolder();
                holder.itemLayout = (LinearLayout) view.findViewById(R.id.local_video_list_item_layout);
                holder.imageSelect = (ImageView) view.findViewById(R.id.local_video_list_item_select);
                holder.imageView = (ImageView) view.findViewById(R.id.local_video_list_item_image);
                holder.title = (TextView) view.findViewById(R.id.local_video_list_item_video_title);
                holder.progressBar = (ProgressBar) view.findViewById(R.id.local_video_list_item_progressbar);
                holder.download = (TextView) view.findViewById(R.id.local_video_list_item_download);
                holder.speed = (TextView) view.findViewById(R.id.local_video_list_item_speed);
                holder.pause = (TextView) view.findViewById(R.id.local_video_list_item_pause_title);
                holder.pauseLayout = (LinearLayout) view.findViewById(R.id.local_video_list_item_pause_layout);
                holder.id = (TextView) view.findViewById(R.id.local_video_list_item_id);
                holder.imagePause = (ImageView) view.findViewById(R.id.local_video_list_item_pause);

                view.setTag(holder);
            }

            if (deleteFlag) {
                holder.imageSelect.setVisibility(View.VISIBLE);
                if (allFlag) {
                    holder.imageSelect.setBackgroundResource(R.drawable.checkbox_pressed);
                } else {
                    boolean flag = findSelectList(position);
                    if (flag == false) {
                        holder.imageSelect.setBackgroundResource(R.drawable.checkbox_normal);
                    } else {
                        holder.imageSelect.setBackgroundResource(R.drawable.checkbox_pressed);
                    }
                }
            } else {
                holder.imageSelect.setVisibility(View.GONE);
            }

            final SchoolVideo video = listData.get(position);
            BitmapUtils bu = new BitmapUtils(context);
            bu.display(holder.imageView, video.getCoverImageUrl());
            holder.title.setText(video.getVideoName());
            holder.download.setText(getDownloadedFileSize(video));
            holder.progressBar.setProgress((int) video.getPercent());
            holder.id.setText(video.getTencentVideoId());
            Log.i(TAG, "getView percent=" + video.getPercent() + ",getStatus=" + video.getStatus());
//        holder.speed.setText(list.get(position).getShortName());

            if (video.getPercent() == 100) {
                holder.pauseLayout.setVisibility(View.GONE);
                holder.progressBar.setVisibility(View.INVISIBLE);
                holder.speed.setVisibility(View.GONE);
            } else {
                holder.pauseLayout.setVisibility(View.VISIBLE);
                holder.progressBar.setVisibility(View.VISIBLE);
                holder.speed.setVisibility(View.VISIBLE);
                holder.imagePause.setBackgroundResource(R.drawable.local_video_item_pause);
                holder.pause.setText(R.string.local_video_item_pause);
                holder.speed.setText("");
            }
//            if (dbUtils.getSchoolVideoCount() > 0) {
//                stopLayout.setVisibility(View.VISIBLE);
//            } else {
//                stopLayout.setVisibility(View.GONE);
//            }

            holder.itemLayout.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    SchoolVideo video = listData.get(position);
                    if (deleteFlag) {
                        LinearLayout layout = (LinearLayout) v;
                        ImageView imageview = (ImageView) layout.getChildAt(0);
                        boolean flag = findSelectList(position);
                        if (flag == false) {
                            listSelect.add(position);
                            if (AppManager.isAdViser(baseContext)) {
                                imageview.setBackgroundResource(R.drawable.checkbox_pressed);
                            } else  {
                                imageview.setBackgroundResource(R.drawable.video_check_c);
                            }
                            setDeleteViewEnable();
                        } else {
                            removeSelectList(position);
                            if (listSelect.size() == 0) {
                                setDeleteViewDisable();
                            } else {
                                setDeleteViewEnable();
                            }
                            imageview.setBackgroundResource(R.drawable.checkbox_normal);
                        }
                    } else {
                        LinearLayout layout = (LinearLayout) v;
                        if (null != tencentVideoId) {
                            if (tencentVideoId.equals(video.getTencentVideoId())) {
                                if (video.getStatus() == 2) {
                                    ToolsUtils.getInstance().toPlayVideoActivity(LocalVideoActivity.this, video);
                                } else {
                                    //如果正在下载，点击暂停
                                    httpHandler = ToolsUtils.getInstance().handler;
                                    if (null != httpHandler) {
                                        if (ToolsUtils.setCancelHttpHandler()) {
                                            itemStopFlag = true;
                                        }
                                        setPauseLayout(layout, 0);
                                        setStopLayoutTextShow();
                                    } else {
                                        downloadFile(video, tencentVideoId);
//                                        ToolsUtils.toDownloadService(LocalVideoActivity.this, tencentVideoId, video.getDownloadtype());
                                        setPauseLayout(layout, 1);
                                        setStartLayoutTextShow();
                                    }
//                                    holder.pause.setText(getString(R.string.local_video_free_caching));
                                }
                            } else {
//                                if(video.getStatus() == 1){ //下载完成:2,下载中：1
//                                    holder.pause.setText(getString(R.string.local_video_item_pause));
//                                }else
                                if (video.getStatus() == 2) {//下载完成播放
                                    ToolsUtils.getInstance().toPlayVideoActivity(LocalVideoActivity.this, video);
                                } else {
                                    if (itemStopFlag) {  //已经点击其他下载行停止，
                                        tencentVideoId = video.getTencentVideoId();
                                        downloadFile(video, tencentVideoId);
                                        itemStopFlag = false;
//                                        ToolsUtils.toDownloadService(LocalVideoActivity.this, tencentVideoId, video.getDownloadtype());
                                        setPauseLayout(layout, 1);
                                    } else {
                                        ToolsUtils.setCancelHttpHandler();
                                        updateStopImage();
                                        tencentVideoId = video.getTencentVideoId();
                                        downloadFile(video, tencentVideoId);
                                    }
                                    setStartLayoutTextShow();
                                }
                            }
                        } else {
                            if (video.getStatus() == 2) {//下载完成播放
                                ToolsUtils.getInstance().toPlayVideoActivity(LocalVideoActivity.this, video);
                            } else {
                                httpHandler = ToolsUtils.getInstance().handler;
                                if (null == httpHandler) {
                                    tencentVideoId = video.getTencentVideoId();
                                    downloadFile(video, tencentVideoId);
                                    setPauseLayout(layout, 1);
                                    setStartLayoutTextShow();
                                }
                            }
                        }
                    }
                }
            });

            return view;
        }

        class ViewHolder {
            LinearLayout itemLayout;
            ImageView imageSelect;
            ImageView imageView;
            TextView title;
            ProgressBar progressBar;
            TextView download;
            TextView speed;
            TextView pause;
            LinearLayout pauseLayout;
            TextView id;
            ImageView imagePause;
        }

    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PERCENT_MSG:
//                    getAllVideo();
                    updateProgress();
                    break;
                case COMPLETE_MSG:
                    getAllVideo();
                    if (startFlag) {//当确定是开始下载，开始新的下载
                        startNewDownload();
                    }
                    adapter.notifyDataSetChanged();
//                    handler.post(runnable);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void getAllVideo() {
        List<SchoolVideo> list = new ArrayList<>();//dbUtils.getSchoolVideoList();
        Log.i(TAG, "getAllVideo videolist size=" + list.size());

        //判断SD卡已经下载文件是否存在，修改数据库
        for (int i = 0; i < list.size(); i++) {
            SchoolVideo video = list.get(i);
            video = ToolsUtils.isFileExist(this, video);
            if (video.getStatus() == 0 && video.getSize() == 0) {
                //如果正在下载文件被外部删除停止下载
                if (null != tencentVideoId) {
                    if (video.getTencentVideoId().equals(tencentVideoId)) {
                        ToolsUtils.setCancelHttpHandler();
                    }
                }
            }
        }

        List<SchoolVideo> videos = new ArrayList<>();//dbUtils.getSchoolVideoListNoDownloadTime();
        if (null != videos) {
            listData.clear();
            listData.addAll(videos);

//            videos = dbUtils.getSchoolVideoListDownloadTime();
            listData.addAll(videos);
            Log.i(TAG, "getAllVideo listDate size= " + listData.size());
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
        try {
            if (null != percentReceiver) {
                unregisterReceiver(percentReceiver);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class PercentReceiver extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            long total = bundle.getLong("total");
            long current = bundle.getLong("current");
            downloadtype = bundle.getInt("downloadtype");
            tencentVideoId = bundle.getString("tencentVideoId");
            speed = bundle.getFloat("speed");
            complete = bundle.getBoolean("complete");

            if (total > 0) {
                percent = (int) (current * 100 / total);
            }
            if (percent != 0) {
                SchoolVideo video =new SchoolVideo();// dbUtils.getSchoolVideo(tencentVideoId);
                if (null != tencentVideoId) {
                    Message msg = new Message();
                    if (complete) {
//                        video.setStatus(2);
//                        boolean ret = ToolsUtils.encryptFile(tencentVideoId,downloadtype);
//                        if(ret) {
//                            video.setEncrypt(2);
//                        }else{
//                            video.setEncrypt(1);
//                        }
                        msg.what = COMPLETE_MSG;
                        Log.i(TAG, "complete ok");

//                        List<SchoolVideo> list = dbUtils.getSchoolVideoList();
//                        Log.i(TAG, "getAllVideo videolist size=" + list.size());
                    } else {
//                        video.setSize(total);
//                        video.setPercent(current * 100 / total);
//                        video.setStatus(1);
//                        video.setDownloadtype(downloadtype);
                        msg.what = PERCENT_MSG;

                        int index = findIndexFromList(tencentVideoId);
                        if (index != -1) {
                            listData.set(index, video);
                        }
                    }
//                    dbUtils.saveSchoolVideo(video);
                    handler.sendMessage(msg);
                }

            }
            Log.i(TAG, "PercentReceiver percent=" + percent + ",tencentVideoId=" + tencentVideoId);
        }
    }

    private void downloadServiceStart(int videotype) {
        Intent intent = new Intent(LocalVideoActivity.this, DownloadService.class);
        intent.setAction(Contant.ACTION_DOWNLOAD);
//        intent.putExtra("videotype", videotype);
//        intent.putExtra("tencentVideoId", tencentVideoId);
        startService(intent);
    }

    private boolean findSelectList(int position) {
        for (int i = 0; i < listSelect.size(); i++) {
            int id = (Integer) listSelect.get(i);
            Log.i(TAG, "findSelectList id=" + id + ",position=" + position);
            if (id == position) {
                return true;
            }
        }
        return false;
    }

    private void removeSelectList(int position) {
        int i = 0;
        for (Integer index : listSelect) {
            Log.i(TAG, "removeSelectList index=" + index + ",position=" + position);
            if (index == position) {
                listSelect.remove(i);
                return;
            }
            i++;
        }
    }

    private void addAllSelectList() {
        Log.i(TAG, "addAllSelectList size=" + listSelect.size());

        for (int i = 0; i < listData.size(); i++) {
            boolean findFlag = false;
            Log.i(TAG, "addAllSelectList index=" + i);
            for (int j = 0; j < listSelect.size(); j++) {
                if (i == listSelect.get(j)) {
                    findFlag = true;
                }
            }
            if (!findFlag) {
                listSelect.add(i);
            }
        }
    }

    private int findIndexFromWidget(String tencentVideoId) {
        int index = -1;
        for (int i = 0; i < listView.getChildCount(); i++) {
            View view = listView.getChildAt(i);
            TextView tv_id = (TextView) view.findViewById(R.id.local_video_list_item_id);
            if (tv_id != null && TextUtils.equals(tencentVideoId, tv_id.getText().toString())) {
                index = i;
            }
        }
        return index;
    }

    private int findIndexFromList(String tencentVideoId) {
        int i = 0;
        int nofind = -1;
        for (SchoolVideo video : listData) {
            if (video.getTencentVideoId().equals(tencentVideoId)) {
                return i;
            }
            i++;
        }
        return nofind;
    }

    private void updateProgress() {
        try {
            int indexFromWidget = findIndexFromWidget(tencentVideoId);
            int indexFromList = findIndexFromList(tencentVideoId);
            if (indexFromWidget != -1) {
//                Log.i(TAG, "updateProgress listview child count=" + listView.getChildCount() + ",data count=" + listData.size() + ",index=" + index + ",last=" + listView.getLastVisiblePosition());
                LinearLayout layout = (LinearLayout) listView.getChildAt(indexFromWidget);
                if (null != layout) {
                    if (judgeViewVideoId(layout)) {
                        setPauseLayout(layout, 1);
                        LinearLayout linearlayout = (LinearLayout) layout.getChildAt(2);
                        Log.i(TAG, "updateProgress count=" + linearlayout.getChildCount());
                        ProgressBar pro = (ProgressBar) linearlayout.getChildAt(1);

                        RelativeLayout relativelayout = (RelativeLayout) linearlayout.getChildAt(2);
                        TextView textviewSize = (TextView) relativelayout.getChildAt(0);
                        TextView textviewSpeed = (TextView) relativelayout.getChildAt(1);

                        pro.setProgress(percent);
                        textviewSize.setText(getDownloadedFileSize(listData.get(indexFromList)));
                        textviewSpeed.setText((int) speed + "K/s");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getDownloadedFileSize(SchoolVideo video) {
        DecimalFormat df = new DecimalFormat("#0.0");
        String totalStr = df.format(video.getSize() / 1024 / 1024) + "M";                //总共大小
        String downStr = video.getSize() / 1024 / 1024 * video.getPercent() / 100 + "M";
        return downStr + "/" + totalStr;
    }

    private void setDeleteViewEnable() {
        deleteTitle.setEnabled(true);
        if (AppManager.isAdViser(baseContext)) {
            deleteTitle.setTextColor(0xffea1202);
        } else  {
            deleteTitle.setTextColor(0xfff47900);
        }

        deleteTitle.setText(getString(R.string.local_video_delete) + "( " + listSelect.size() + " )");
    }

    private void setDeleteViewDisable() {
        deleteTitle.setEnabled(false);
        deleteTitle.setTextColor(0x99999999);
        deleteTitle.setText(getString(R.string.local_video_delete));
    }

    private void setSpaceLayoutVisible() {
        spaceLayout.setVisibility(View.VISIBLE);
        stopLayout.setVisibility(View.VISIBLE);
        selectLayout.setVisibility(View.GONE);
        titleRight.setBackgroundResource(R.drawable.local_video_delete);
        titleRight.setText("");
        deleteFlag = false;
    }

    //pauseflag 0:stop 1:caching
    private void setPauseLayout(LinearLayout layout, int pauseflag) {

        try {
            if (null != layout) {
                Log.i(TAG, "childcount=" + layout.getChildCount());
                if (layout.getChildCount() > 0) {
                    RelativeLayout pauseRelativelayout = (RelativeLayout) layout.getChildAt(1);  //获取暂停Relativelayout
                    LinearLayout pauseLinearlayout = (LinearLayout) pauseRelativelayout.getChildAt(1);  //获取暂停layout

                    ImageView pauseImage = (ImageView) pauseLinearlayout.getChildAt(0);
                    TextView pauseText = (TextView) pauseLinearlayout.getChildAt(1);
                    TextView speedTv = (TextView) layout.findViewById(R.id.local_video_list_item_speed);

                    if (pauseflag == 1) {
                        pauseImage.setBackgroundResource(R.drawable.local_video_item_download);
                        pauseText.setText(R.string.local_video_free_caching);
                    } else {
                        pauseImage.setBackgroundResource(R.drawable.local_video_item_pause);
                        pauseText.setText(R.string.local_video_item_pause);
                        if (speedTv != null)
                            speedTv.setText("");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //没有数据缓存的时候
    private void noVideoList() {
        if (listData.size() == 0) {
            noLayout.setVisibility(View.VISIBLE);
            stopLayout.setVisibility(View.GONE);
            bottomLayout.setVisibility(View.GONE);
            titleRight.setVisibility(View.GONE);
        }
    }

    private void startNewDownload() {
        toastWifi();
        if (listData.size() > 0) {
            SchoolVideo video = listData.get(0);
            tencentVideoId = video.getTencentVideoId();
            if (video.getStatus() != 2) {
                updateImage();
                ToolsUtils.getInstance().toDownloadService(LocalVideoActivity.this, tencentVideoId, video.getDownloadtype());
            }
        }
    }

    private void toastWifi() {
        int netStatus = ToolsUtils.getWifiStatus(this);
        if (netStatus == Contant.NET_STAUS_MOBILE) { //手机流量
            new MToast(this).showLoginFailure(this.getString(R.string.local_video_net_mobile));
        }
    }

    private void downloadFile(SchoolVideo video, String tencentvideoid) {
        toastWifi();
        ToolsUtils.getInstance().toDownloadService(LocalVideoActivity.this, tencentvideoid, video.getDownloadtype());
    }

    private void updateImage() {
        int index = findIndexFromWidget(tencentVideoId);
        if (index != -1) {
            LinearLayout layout = (LinearLayout) listView.getChildAt(index);
            if (judgeViewVideoId(layout)) {
                setPauseLayout(layout, 1);
            }
        }
    }

    private void updateStopImage() {
        int index = findIndexFromWidget(tencentVideoId);
        if (index != -1) {
            LinearLayout layout = (LinearLayout) listView.getChildAt(index);
            if (judgeViewVideoId(layout)) {
                setPauseLayout(layout, 0);
            }
        }
    }

    private void setStopLayoutTextShow() {
        startFlag = false;
        if (AppManager.isAdViser(baseContext)) {
            start.setBackgroundResource(R.drawable.local_video_start);
        } else   {
            start.setBackgroundResource(R.drawable.play_c);
        }
        startTitle.setText(R.string.local_video_all_start);
        itemStopFlag = false;
    }

    private void setStartLayoutTextShow() {
        startFlag = true;
        if (AppManager.isAdViser(baseContext)) {
            start.setBackgroundResource(R.drawable.local_video_stop);
        } else  {
            start.setBackgroundResource(R.drawable.pause_c);
        }
        startTitle.setText(R.string.local_video_all_pause);
    }

    private boolean judgeViewVideoId(LinearLayout layout) {

        TextView textviewid = (TextView) layout.getChildAt(3);
        String id = textviewid.getText().toString();
        Log.i(TAG, "updateProgress id=" + id);
        if (id.equals(tencentVideoId)) {
            return true;
        }
        return false;
    }

    private void encryptFile() {

//        SchoolVideo video = dbUtils.getSchoolVideo(tencentVideoId);
//        if (null != video) {
//            boolean ret = ToolsUtils.encryptFile(video.getTencentVideoId(), downloadtype);
//            if (ret) {
//                video.setEncrypt(2);
//            } else {
//                video.setEncrypt(1);
//            }
//            dbUtils.saveSchoolVideo(video);
//        }
    }

    Runnable runnable = new Runnable() {
        public void run() {
            try {
                Thread.sleep(3000);
                encryptFile();
                getAllVideo();
                if (startFlag) {//当确定是开始下载，开始新的下载
                    startNewDownload();
                }
                adapter.notifyDataSetChanged();
                handler.removeCallbacks(runnable);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    //删除的视频有可能在播放中
    private void computerSelectDel() {
//        int remIndex = -1;
//        boolean flag = false;
//        for (int index = 0; index < listSelect.size(); index++) {
//            String currentVideoId = SPSave.getInstance(MApplication.mContext).getString(Contant.currentTencentVideoId);
//            if (listData.get(index).getTencentVideoId().equals(currentVideoId)) {
//                remIndex = index;
//                flag = true;
//            } else {
//                listSelectDel.add(index);
//            }
//        }
//
//        if (flag) {
//            listSelectDel.add(listSelect.get(remIndex));
//        }
    }

    private void refreshAdapter() {
        listSelect.clear();
        listSelectDel.clear();
        getAllVideo();
        if (listData.size() == 0) {
            noVideoList();
        } else {
            setDeleteViewEnable();
            setSpaceLayoutVisible();
        }
        adapter.notifyDataSetChanged();
    }

    private void deleteFileAndDB(String videoid, int type, SchoolVideo video) {
        ToolsUtils.deleteDownloadFile(videoid, type);
//        dbUtils.deleteSchoolVideo(video);
    }


//    private downloadComplete(String tencentVideoId,int downloadtype){
//        //1.加密文件 2.修改数据库 3.开始下个下载
//        boolean ret = ToolsUtils.encryptFile(tencentVideoId,downloadtype);
//
//    }

}
