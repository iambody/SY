package com.cgbsoft.lib.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.R;
import com.cgbsoft.lib.base.model.AppResourcesEntity;
import com.cgbsoft.lib.base.model.bean.OtherInfo;
import com.cgbsoft.lib.utils.cache.CacheManager;
import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.utils.db.DBConstant;
import com.cgbsoft.lib.utils.db.DaoUtils;
import com.cgbsoft.lib.utils.dm.core.DownloadManagerPro;
import com.cgbsoft.lib.utils.dm.core.DownloadManagerProListener;
import com.cgbsoft.lib.utils.dm.database.elements.Task;
import com.cgbsoft.lib.utils.rxjava.RxSchedulersHelper;
import com.cgbsoft.lib.utils.tools.Utils;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;

import rx.Observable;

import static com.cgbsoft.lib.utils.db.DBConstant.APP_DOWNLOAD_PATH;

/**
 * desc
 * Created by yangzonghui on 2017/6/15 22:52
 * Email:yangzonghui@simuyun.com
 *  
 */
public class DownloadAdviserDialog implements View.OnClickListener, Constant {
    private Context _context;
    private boolean _isOpenWindow, _isCompel, _isCouldClickBack = true;
    private String _verName, _appName, _newVerUrl, _newVerPath, _newVerName;
    private int _verCode, _newVerCode, OK_STATUS = -1;

    private Dialog dialog;
    private Window window;

    private Resources resources;
    private int screenWidth;

    private TextView tv_vcd_title;
    private ProgressBar pb_vcd;
    private TextView tv_vcd_message;
    private Button btn_vcd_sure;
    private ImageView iv_vcd_cancel;

    private DaoUtils daoUtils;
    private String downloadUrl;
    private DownloadManagerPro downloadManagerPro;
    private int downloadApkToken;
    private String downloadApkPath;
    private ImageView bg_dialog;
    private boolean formSetting;


    public DownloadAdviserDialog(Context context, boolean isOpenWindow, boolean fromSetting) {
        _isOpenWindow = isOpenWindow;
        _context = context;
        this.formSetting = fromSetting;
        init();
    }

    public void init() {
        _verName = Utils.getVersionName(_context);
        _verCode = Utils.getVersionCode(_context);
        _appName = _context.getResources().getString(R.string.app_name);
        daoUtils = new DaoUtils(_context, DaoUtils.W_OTHER);

        resources = _context.getResources();
        screenWidth = Utils.getScreenWidth(_context);

        dialog = new Dialog(_context, R.style.CenterCompatDialogTheme);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.view_custom_dialog);
        window = dialog.getWindow();
        window.setGravity(Gravity.CENTER | Gravity.CENTER);
        window.setWindowAnimations(R.style.AnimBottom);

        tv_vcd_title = (TextView) dialog.findViewById(R.id.tv_vcd_title);
        bg_dialog = (ImageView) dialog.findViewById(R.id.bg_download);
        pb_vcd = (ProgressBar) dialog.findViewById(R.id.pb_vcd);
        tv_vcd_message = (TextView) dialog.findViewById(R.id.tv_vcd_message);
        btn_vcd_sure = (Button) dialog.findViewById(R.id.btn_vcd_sure);
        iv_vcd_cancel = (ImageView) dialog.findViewById(R.id.iv_vcd_cancel);

        btn_vcd_sure.setOnClickListener(this);
        iv_vcd_cancel.setOnClickListener(this);
        if (AppManager.isInvestor(_context)) {
            bg_dialog.setImageResource(R.drawable.bg_investor_dialog);
            btn_vcd_sure.setBackgroundResource(R.drawable.btn_orange_bg_sel);
            pb_vcd.setProgressDrawable(_context.getResources().getDrawable(R.drawable.orange_progress_bar));
        }
        tv_vcd_title.setText("升级提示");
        btn_vcd_sure.setText("现在升级");
        pb_vcd.setMax(100);
        pb_vcd.setVisibility(View.VISIBLE);

        checkVersion();
    }

    private void checkVersion() {

        tv_vcd_message.setText("升级内容");
        downloadUrl = "https://upload.simuyun.com/android/POF_Cloud_Adviser_V5.8.0.apk";

        downloadApkPath = "";
        btn_vcd_sure.setText("现在安装");

        dialog.show();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_vcd_sure) {
            toDownload();
            btn_vcd_sure.setEnabled(false);
        } else if (v.getId() == R.id.iv_vcd_cancel) {
            dialog.dismiss();
        }
    }


    private void toDownload() {
        if (!TextUtils.isEmpty(downloadApkPath)) {
            File file = new File(downloadApkPath);
            if (file.isFile() && file.exists()) {
                installApk(file);
                return;
            }
        }

        String fileRoot = CacheManager.getCachePath(_context, CacheManager.APK);

        String[] strs = downloadUrl.split("/");
        String fileName = strs[strs.length - 1];

        if (downloadManagerPro == null) {
            downloadManagerPro = new DownloadManagerPro(_context);
        }
        downloadManagerPro.init(fileRoot, 12, new DownloadManagerProListener() {
            @Override
            public void OnDownloadCompleted(long taskId) {
                Task task = downloadManagerPro.getTask(downloadApkToken);
                downloadApkPath = task.save_address + task.name + ".apk";
                installApk(new File(downloadApkPath));
                daoUtils.saveOrUpdataOther(APP_DOWNLOAD_PATH, downloadApkPath);

                Observable.just("现在安装").compose(RxSchedulersHelper.io_main()).subscribe(strs -> {
                    btn_vcd_sure.setText(strs);
                }, error -> {
                });
            }

            @Override
            public void onDownloadProcess(long taskId, double percent, long downloadedLength) {
                pb_vcd.setProgress((int) percent);
                Observable.just("已下载(" + (int) percent + "%)").compose(RxSchedulersHelper.io_main()).subscribe(strs -> {
                    btn_vcd_sure.setText(strs);
                }, error -> {
                });
            }
        });

        downloadApkToken = downloadManagerPro.addTask(fileName.split("\\.")[0], downloadUrl, 12, fileRoot, false, true);
        try {
            downloadManagerPro.startDownload(downloadApkToken);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * 安装软件
     *
     * @param file
     */
    private void installApk(File file) {
        Intent install = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
//            install.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            Uri uri = FileProvider.getUriForFile(_context, "com.cgbsoft.privatefund.fileProvider", file);
//            install.setDataAndType(uri, "application/vnd.android.package-archive");
        }
//        else {
        Uri uri = Uri.fromFile(file);
        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        install.setDataAndType(uri, "application/vnd.android.package-archive");
//        }
        // 执行意图进行安装
        _context.startActivity(install);
    }

    private void destory() {
        daoUtils.destory();
        daoUtils = null;
    }
}

