package com.cgbsoft.lib.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
 * 下载apkdialog
 * Created by xiaoyu.zhang on 2016/11/24 13:59
 * Email:zhangxyfs@126.com
 *  
 */
public class DownloadDialog implements View.OnClickListener, Constant {
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


    public DownloadDialog(Context context, boolean isOpenWindow) {
        _isOpenWindow = isOpenWindow;
        _context = context;
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
        dialog.setContentView(R.layout.view_custom_dialog);
        dialog.setCanceledOnTouchOutside(true);
        window = dialog.getWindow();
        window.setGravity(Gravity.CENTER | Gravity.CENTER);
        window.setWindowAnimations(R.style.AnimBottom);

        tv_vcd_title = (TextView) dialog.findViewById(R.id.tv_vcd_title);
        pb_vcd = (ProgressBar) dialog.findViewById(R.id.pb_vcd);
        tv_vcd_message = (TextView) dialog.findViewById(R.id.tv_vcd_message);
        btn_vcd_sure = (Button) dialog.findViewById(R.id.btn_vcd_sure);
        iv_vcd_cancel = (ImageView) dialog.findViewById(R.id.iv_vcd_cancel);

        btn_vcd_sure.setOnClickListener(this);
        iv_vcd_cancel.setOnClickListener(this);

        tv_vcd_title.setText("升级提示");
        btn_vcd_sure.setText("现在升级");
        pb_vcd.setMax(100);
        pb_vcd.setVisibility(View.VISIBLE);

        checkVersion();
    }

    private void checkVersion() {
        OtherInfo otherInfo = daoUtils.getOtherInfo(DBConstant.APP_UPDATE_INFO);
        if (otherInfo != null) {
            String json = otherInfo.getContent();
            AppResourcesEntity.Result result = new Gson().fromJson(json, AppResourcesEntity.Result.class);
            if (result != null && !TextUtils.equals(result.version, _verName)) {
                if (TextUtils.isEmpty(result.adverts)) {
                    return;
                }

                if (TextUtils.equals(result.isMustUpdate, "y")) {//强制更新
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setCancelable(false);
                    iv_vcd_cancel.setVisibility(View.GONE);
                }
                tv_vcd_message.setText(result.adverts);
                downloadUrl = result.downUrl;

                OtherInfo info = daoUtils.getOtherInfo(APP_DOWNLOAD_PATH);
                if (info != null) {
                    downloadApkPath = info.getContent();
                    btn_vcd_sure.setText("现在安装");
                }

                if (_isOpenWindow)
                    dialog.show();
            }
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_vcd_sure) {
            toDownload();
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
        Uri uri = Uri.fromFile(file);
        Intent install = new Intent(Intent.ACTION_VIEW);
        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        install.setDataAndType(uri, "application/vnd.android.package-archive");
        // 执行意图进行安装
        _context.startActivity(install);
    }

    private void destory() {
        daoUtils.destory();
        daoUtils = null;
    }
}
