package com.cgbsoft.privatefund.mvp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.base.webview.BaseWebViewActivity;
import com.cgbsoft.lib.listener.listener.FeedbackListener;
import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.utils.dm.Utils.helper.FileUtils;
import com.cgbsoft.lib.utils.net.NetConfig;
import com.cgbsoft.lib.utils.tools.DownloadUtils;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.lib.utils.tools.ThreadUtils;
import com.cgbsoft.lib.widget.dialog.LoadingDialog;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.mvp.contract.home.FeedBackUserContract;
import com.cgbsoft.privatefund.mvp.presenter.home.FeedBackUserPresenter;
import com.cgbsoft.privatefund.widget.mvc.adapter.FeedbackAdapter;
import com.chenenyu.router.annotation.Route;
import com.jhworks.library.ImageSelector;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 意见反馈
 * Created by xiaoyu.zhang on 2016/11/14 14:08
 * Email:zhangxyfs@126.com
 *  
 */
@Route("investornmain_feedbackctivity")
public class FeedbackActivity extends BaseActivity<FeedBackUserPresenter> implements FeedbackListener, FeedBackUserContract.View {

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.title_mid)
    protected TextView titleMid;

    @BindView(R.id.afb_et)
    protected EditText afb_et;

    @BindView(R.id.recyclerView)
    protected RecyclerView recyclerView;

    @BindView(R.id.commit)
    protected Button commit;

    private List<String> imagePaths = new ArrayList<>();
    private List<String> remoteParams = new ArrayList<>();
    private FeedbackAdapter feedbackAdapter;
    private GridLayoutManager gridLayoutManager;
    private LoadingDialog loading;
    private static final int SMOTH_CODE = 2;
    private int picClickPosition = -1;

    @Override
    protected int layoutID() {
        return R.layout.activity_feedback;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        titleMid.setText("意见反馈");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(com.cgbsoft.lib.R.drawable.ic_back_black_24dp);
        toolbar.setNavigationOnClickListener(v -> finish());

        feedbackAdapter = new FeedbackAdapter(this);
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 4);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(feedbackAdapter);
        recyclerView.setHasFixedSize(true);
//        recyclerView.addItemDecoration(new DividerGridItemDecoration(getApplicationContext()));
    }

    @Override
    protected FeedBackUserPresenter createPresenter() {
        return new FeedBackUserPresenter(getBaseContext(), this);
    }

    @OnClick(R.id.commit)
    void commitButtonClick() {
        String editStr = afb_et.getText().toString();
        if (editStr.length() >= 10 && editStr.length() <= 200) {
            uploadFile(editStr);
        } else {
            if (editStr.length() < 10) {
                Toast.makeText(FeedbackActivity.this, "您输入的文字不能少于10个", Toast.LENGTH_SHORT).show();
            } else if (editStr.length() > 200) {
                Toast.makeText(FeedbackActivity.this, "您输入的文字不能大于200个", Toast.LENGTH_SHORT).show();
            }/* else if (imagePaths.size() == 0) {
                        new MToast(FeedbackActivity.this).show("您输入的文字不能大于200个", 0);
                    }*/
        }
    }

    @Override
    public void picClickListener(int pos, String path) {
        if (TextUtils.equals(path, "+")) {
            if (imagePaths.size() > 12) {
                Toast.makeText(FeedbackActivity.this, "最多上传10张图片", Toast.LENGTH_SHORT).show();
                return;
            }
            NavigationUtils.startSystemImageForResult(this, BaseWebViewActivity.REQUEST_IMAGE);
        } else {
            picClickPosition = pos;
            Intent intent = new Intent(this, SmoothImageActivity.class);
            intent.putExtra(SmoothImageActivity.IMAGE_SAVE_PATH_LOCAL, path);
            intent.putExtra(SmoothImageActivity.IMAGE_RIGHT_DELETE, true);
            startActivityForResult(intent, SMOTH_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BaseWebViewActivity.REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                ArrayList<String> mSelectPath = data.getStringArrayListExtra(ImageSelector.EXTRA_RESULT);
                if (mSelectPath != null && mSelectPath.size() > 0) {
                    imagePaths.add(mSelectPath.get(0));
                    feedbackAdapter.addPic(mSelectPath.get(0));
                }
            }
        } else if (requestCode == SMOTH_CODE) {
            if (resultCode == RESULT_OK) {
                if (picClickPosition > -1) {
                    imagePaths.remove(picClickPosition);
                    feedbackAdapter.removeOne(picClickPosition);
                }
            }
        }
    }

    private void uploadFile(final String str) {
        if (loading == null) {
            loading = new LoadingDialog(this);
        }
        loading.show();
        new Thread() {
            @Override
            public void run() {
                super.run();
                remoteParams.clear();
                for (final String localPath : imagePaths) {
                    if (localPath.contains(Constant.UPLOAD_FEEDBACK_TYPE) || localPath.startsWith("http")) {
                        continue;
                    }
                    String newTargetFile = FileUtils.compressFileToUpload(localPath, true);
                    String paths = DownloadUtils.postObject(newTargetFile, Constant.UPLOAD_FEEDBACK_TYPE);
                    FileUtils.deleteFile(newTargetFile);
                    if (!TextUtils.isEmpty(paths)) {
                        remoteParams.add(paths);
                    } else {
                        ThreadUtils.runOnMainThread(() -> Toast.makeText(FeedbackActivity.this, "证明文件上传失败，请重新上传", Toast.LENGTH_SHORT).show());
                        loading.dismiss();
                        return;
                    }
                }
                uploadInfo(str);
            }
        }.start();
    }

    private JSONArray getArrayParams(List<String> lists) {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < lists.size(); i++) {
            jsonArray.put(lists.get(i).contains("http") ? lists.get(i) : NetConfig.UPLOAD_FILE + lists.get(i));
        }
        return jsonArray;
    }

    private void uploadInfo(String str) {
        getPresenter().feedBack(str, getArrayParams(remoteParams));
    }

    @Override
    public void requestSuccess() {
        if (loading != null && loading.isShowing()) {
            loading.dismiss();
        }
        Toast.makeText(FeedbackActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void requestFailure(String errorMsg) {
        Toast.makeText(FeedbackActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
    }
}
