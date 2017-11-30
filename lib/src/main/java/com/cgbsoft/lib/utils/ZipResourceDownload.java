package com.cgbsoft.lib.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;

import com.cgbsoft.lib.AppInfStore;
import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.BaseApplication;
import com.cgbsoft.lib.R;
import com.cgbsoft.lib.bodys.FileDownloadCallback;
import com.cgbsoft.lib.bodys.FileDownloadTask;
import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.utils.dm.Utils.helper.FileUtils;
import com.cgbsoft.lib.utils.net.NetConfig;
import com.cgbsoft.lib.utils.tools.LogUtils;

import java.io.File;

/**
 * @author chenlong
 */
public class ZipResourceDownload {

    private static final int BEGIN_UNZIP = 0;
    private static final int UPDATE_PROGRESS = 1;
    private static final int END_UNZIP = 2;
    private static final int FAILED_UNZIP = 3;

    private Context context;
    private int lastProgress = 0;
    private ProgressBar progressBar;
    private AlertDialog downloadDialog;
    private static final int UPDATE_RESOURCE_ZIP_DIALOG = 2;

    public ZipResourceDownload(Context context) {
        this.context = context;
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int type= (int) msg.obj;
            switch (type) {
                case BEGIN_UNZIP:
                    LogUtils.Log("aaa","begin_unzip");
                    downloadDialog.show();
                    progressBar.setProgress(1);
                    progressBar.invalidate();
                    break;
                case UPDATE_PROGRESS:
                    LogUtils.Log("aaa","unzip_progress"+msg.arg1);
                    progressBar.setProgress(msg.arg1);
                    progressBar.invalidate();
                    break;
                case END_UNZIP:
                    LogUtils.Log("aaa","END_UNZIP");
                    downloadDialog.dismiss();
                    if (msg.arg2 == UPDATE_RESOURCE_ZIP_DIALOG) {
                        initSoFile();
                    }
                    break;
                case FAILED_UNZIP:
                    LogUtils.Log("aaa","FAILED_UNZIP");
                    downloadDialog.dismiss();
                    if (msg.arg2 == UPDATE_RESOURCE_ZIP_DIALOG) {
                        initSoFile();
                    }
                    break;
            }
        }
    };

    public void initZipResource() {
        if (!TextUtils.isEmpty(AppManager.getResouceZipServerFileName(context)) &&
                !TextUtils.equals(AppManager.getResouceZipOldFileName(context), AppManager.getResouceZipServerFileName(context))) {
            downloadResourceZipFile();
        } else {
            initSoFile();
        }
    }

    private void initSoFile() {
        if (!SoFileUtils.isLoadSoFile(context)) {
            downloadSoFileOnce();
        }
    }

    public void initDownDialog() {
        if (null == downloadDialog) {
            View view = View.inflate(context, R.layout.download_dialog_layout, null);
            progressBar = (ProgressBar) view.findViewById(R.id.pb_download);
            downloadDialog = new AlertDialog.Builder(context).setView(view).create();
            Window window = downloadDialog.getWindow();
            window.setGravity(Gravity.CENTER);
            downloadDialog.setCancelable(false);
            downloadDialog.setCanceledOnTouchOutside(false);
        }
    }

    /**
     * 下载需要动态加载的so文件
     */
    public void downloadSoFileOnce() {
        initDownDialog();
        File saveFile = FileUtils.createTempFile(Constant.SO_ZIP_NAME);
        if (saveFile != null) {
            FileDownloadTask task = new FileDownloadTask(NetConfig.SoDown.DOWN_RUL, saveFile, new FileDownloadCallback() {
                @Override
                public void onStart() {
                    super.onStart();
                    LogUtils.Log("aaa","startdown");
                    downloadDialog.show();
                    progressBar.setProgress(1);
                    progressBar.invalidate();
                }

                @Override
                public void onProgress(int progress, long networkSpeed) {
                    super.onProgress(progress, networkSpeed);
                    /** 因为会频繁的刷新,这里我只是进度>1%的时候才去显示 */
                    if (progress > lastProgress) {
                        LogUtils.Log("aaa","down---lastProgress==="+lastProgress);
                        lastProgress = progress;
                        progressBar.setProgress(lastProgress);
                        progressBar.invalidate();
                    }
                }

                @Override
                public void onFailure() {
                    super.onFailure();
                    downloadDialog.dismiss();
                    LogUtils.Log("aaa","onFailure");
                }

                @Override
                public void onDone() {
                    super.onDone();
                    downloadDialog.dismiss();
                    LogUtils.Log("aaa","onDone");
                    unZipSoFileToApp();
                }
            });
            task.execute();
        }
    }

    /**
     * 解压so库的zip文件
     */
    private void unZipSoFileToApp() {
        File tempFile = FileUtils.getTempFile(Constant.SO_ZIP_NAME);
        File targetDir = BaseApplication.getContext().getDir("dynamic", Context.MODE_PRIVATE);
        FileUtils.doUnzip(tempFile, targetDir, Constant.SO_ZIP_NAME, "dynamic", new FileUtils.UnZipCallback() {
            @Override
            public void success() {
            }

            @Override
            public void beginUnZip() {
                Message msg = Message.obtain();
                msg.obj=BEGIN_UNZIP;
                handler.sendMessage(msg);
            }

            @Override
            public void updateProgress(int progress) {
                Message msg = Message.obtain();
                msg.obj=UPDATE_PROGRESS;
                msg.arg1=progress;
                handler.sendMessage(msg);
            }

            @Override
            public void endUnZip() {
                Message msg = Message.obtain();
                msg.obj=END_UNZIP;
                handler.sendMessage(msg);
                System.out.println("--------down  upSoResourceFile success");
            }

            @Override
            public void failed() {
                Message msg = Message.obtain();
                msg.obj=FAILED_UNZIP;
                handler.sendMessage(msg);
                System.out.println("--------down  upSoResourceFile failure");
            }
        });
    }

    /**
     * 下载H5资源文件
     */
    private void downloadResourceZipFile() {
        initDownDialog();
        String apkUrl = AppManager.getResouceDownloadAddress(context);
        File saveZipFile = FileUtils.createResourceLocalTempFile(Constant.HEALTH_ZIP_DIR, Constant.RESOURCE_ZIP_NAME);
        if (!TextUtils.isEmpty(apkUrl) && saveZipFile != null) {
            FileDownloadTask task = new FileDownloadTask(apkUrl, saveZipFile, new FileDownloadCallback() {
                @Override
                public void onStart() {
                    super.onStart();
                    downloadDialog.show();
                    progressBar.setProgress(1);
                    progressBar.invalidate();
                }

                @Override
                public void onProgress(int progress, long networkSpeed) {
                    super.onProgress(progress, networkSpeed);
                    if (progressBar.getProgress() < progress) {
                        progressBar.setProgress(progress);
                        progressBar.invalidate();
                    }
                }

                @Override
                public void onFailure() {
                    super.onFailure();
                    downloadDialog.dismiss();
                    AppInfStore.saveResourceOldFileName(context, "");
                    saveZipFile.delete();
                    initSoFile();
                }

                @Override
                public void onDone() {
                    super.onDone();
                    unZipResourceFile();
                }
            });
            task.execute();
        }
    }

    /**
     * 解压H5zip资源文件
     */
    private void unZipResourceFile() {
        File upZipFile = FileUtils.getResourceLocalTempFile(Constant.HEALTH_ZIP_DIR, Constant.RESOURCE_ZIP_NAME);
        System.out.println("--------down  upZipResourceFile start=" + upZipFile.getPath());
        if (upZipFile != null || upZipFile.exists()) {
            File targetDir = BaseApplication.getContext().getDir(Constant.HEALTH_ZIP_DIR, Context.MODE_PRIVATE);
            FileUtils.doUnzip(upZipFile, targetDir, Constant.RESOURCE_ZIP_NAME, Constant.HEALTH_ZIP_DIR, new FileUtils.UnZipCallback() {
                @Override
                public void success() {
                }

                @Override
                public void beginUnZip() {
                    Message msg = Message.obtain();
                    msg.obj=BEGIN_UNZIP;
                    msg.arg2 = UPDATE_RESOURCE_ZIP_DIALOG;
                    handler.sendMessage(msg);
                }

                @Override
                public void updateProgress(int progress) {
                    Message msg = Message.obtain();
                    msg.obj=UPDATE_PROGRESS;
                    msg.arg2 = UPDATE_RESOURCE_ZIP_DIALOG;
                    msg.arg1=progress;
                    handler.sendMessage(msg);
                }

                @Override
                public void endUnZip() {
                    Message msg = Message.obtain();
                    msg.obj = END_UNZIP;
                    msg.arg2 = UPDATE_RESOURCE_ZIP_DIALOG;
                    handler.sendMessage(msg);
                    upZipFile.delete();
                    AppInfStore.saveResourceOldFileName(context, AppManager.getResouceZipServerFileName(context));
                    System.out.println("--------down  upZipResourceFile success");
                }

                @Override
                public void failed() {
                    Message msg = Message.obtain();
                    msg.obj = FAILED_UNZIP;
                    msg.arg2 = UPDATE_RESOURCE_ZIP_DIALOG;
                    handler.sendMessage(msg);
                    upZipFile.delete();
                    AppInfStore.saveResourceOldFileName(context, "");
                    System.out.println("--------down  upZipResourceFile error");
                }
            });
        }
    }

    public void closeDilaog() {
        if (downloadDialog != null && downloadDialog.isShowing() && !((Activity)context).isFinishing()) {
            downloadDialog.dismiss();
        }
    }
}
