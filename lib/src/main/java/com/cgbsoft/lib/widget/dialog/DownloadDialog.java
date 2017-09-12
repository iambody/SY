package com.cgbsoft.lib.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
import com.cgbsoft.lib.utils.tools.LogUtils;
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

    private ProgressBar pb_vcd;
    private TextView tv_vcd_message;
    private TextView btn_vcd_sure;
    private ImageView iv_vcd_cancel;

    private DaoUtils daoUtils;
    private String downloadUrl;
    private DownloadManagerPro downloadManagerPro;
    private int downloadApkToken;
    private String downloadApkPath;
    private boolean formSetting;
    private TextView new_version_code;


    public DownloadDialog(Context context, boolean isOpenWindow, boolean fromSetting) {
        _isOpenWindow = isOpenWindow;
        _context = context;
        this.formSetting = fromSetting;
        init();
    }

    public void init() {
        _verName = Utils.getVersionName(_context);
        _newVerName=_verName;
        _verCode = Utils.getVersionCode(_context);
        _appName = _context.getResources().getString(R.string.app_name);
        daoUtils = new DaoUtils(_context, DaoUtils.W_OTHER);

        resources = _context.getResources();
        screenWidth = Utils.getScreenWidth(_context);

        dialog = new BaseDialog(_context, R.style.dialog_comment_style);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        View inflate = LayoutInflater.from(_context).inflate(R.layout.dialog_download_c, null);
        dialog.setContentView(inflate);
        window = dialog.getWindow();
//配置信息
        WindowManager.LayoutParams wparams = window.getAttributes();
        wparams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wparams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        wparams.gravity = Gravity.BOTTOM;
        window.setAttributes(wparams);
//        window.setGravity(Gravity.CENTER);
        window.setWindowAnimations(R.style.AnimBottom);

        pb_vcd = (ProgressBar) dialog.findViewById(R.id.pb_vcd);
        tv_vcd_message = (TextView) dialog.findViewById(R.id.tv_vcd_message);
        tv_vcd_message.setMovementMethod(ScrollingMovementMethod.getInstance());

        btn_vcd_sure = (TextView) dialog.findViewById(R.id.btn_vcd_sure);
        iv_vcd_cancel = (ImageView) dialog.findViewById(R.id.iv_vcd_cancel);
        new_version_code = (TextView) dialog.findViewById(R.id.new_version_code);

        btn_vcd_sure.setOnClickListener(this);
        iv_vcd_cancel.setOnClickListener(this);
        if (AppManager.isInvestor(_context)) {
//            btn_vcd_sure.setBackgroundResource(R.drawable.btn_orange_bg_sel);
            pb_vcd.setProgressDrawable(_context.getResources().getDrawable(R.drawable.orange_progress_bar));
        }
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
                if (TextUtils.isEmpty(result.adverts) || _verName.equals(result.version)) {
                    return;
                }
                if (TextUtils.isEmpty(result.version)) {
                    return;
                }
                if (result.upgradeType == 0) {
                    return;
                }
                if ((!formSetting) && result.upgradeType == 2) {
                    return;
                }

                if (TextUtils.equals(result.isMustUpdate, "y")) {//强制更新
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setCancelable(false);
                    iv_vcd_cancel.setVisibility(View.GONE);
                }
                tv_vcd_message.setText(result.adverts);
                new_version_code.setText(result.version);

                downloadUrl = result.downUrl;

                OtherInfo info = daoUtils.getOtherInfo(APP_DOWNLOAD_PATH);
                if (info != null) {
                    downloadApkPath = info.getContent();
                    File file = new File(downloadApkPath);
                    if (file.isFile() && file.exists()&&getUninatllApkInfo(_context,file.getAbsolutePath())) {
                        btn_vcd_sure.setText("现在安装");
                    }
                }

                if (_isOpenWindow) {
                    _newVerName=result.version;
                    dialog.show();
                }
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

    /**
     * 判断apk文件是否可以执行
     * @param context
     * @param filePath
     * @return
     */
    public boolean getUninatllApkInfo(Context context,String filePath) {
        boolean result = false;
        try {
            PackageManager pm = context.getPackageManager();
            LogUtils.Log("archiveFilePath", filePath);
            PackageInfo info = pm.getPackageArchiveInfo(filePath,PackageManager.GET_ACTIVITIES);
            if (info != null) {
                result = true;//完整
            }
        } catch (Exception e) {
            result = false;//不完整
        }
        return result;
    }
    public void onresume(){
        btn_vcd_sure.setEnabled(true);
    }
    private void toDownload() {
        btn_vcd_sure.setEnabled(false);
        if (!TextUtils.isEmpty(downloadApkPath)) {
            File file = new File(downloadApkPath);
            if (file.isFile() && file.exists()&&getUninatllApkInfo(_context,file.getAbsolutePath())) {
                btn_vcd_sure.setEnabled(true);
                installApk(file);
                return;
            }
        }

        String fileRoot = CacheManager.getCachePath(_context, CacheManager.APK);
        delAllFile(fileRoot);
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
                    LogUtils.Log("aaa","strs=================="+strs);
                    btn_vcd_sure.setText(strs);
                }, error -> {
                });
            }
        });

        downloadApkToken = downloadManagerPro.addTask("POF_Cloud_V" + _newVerName, downloadUrl, 12, fileRoot, true, true);
        try {
            downloadManagerPro.startDownload(downloadApkToken);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i]);// 再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }

    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); // 删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            myFilePath.delete(); // 删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
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
