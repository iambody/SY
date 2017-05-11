//package com.cgbsoft.privatefund.widget.mvc;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RadioButton;
//import android.widget.TextView;
//
//import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
//import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
//import com.cgbsoft.lib.utils.tools.NavigationUtils;
//import com.cgbsoft.lib.utils.tools.ViewUtils;
//import com.cgbsoft.lib.widget.LoadingDialog;
//import com.cgbsoft.privatefund.R;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.io.FileInputStream;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * 资产证明
// * @author chenlong
// */
//public class AssetProveActivity extends BaseActivity implements View.OnClickListener {
//
//    private static final int SMOTH_CODE = 2;
//    private TextView description;
//    private FixGridLayout frameLayout;
//    private Button commit;
//    private ViewGroup viewGroup;
//    private TextView checkResult;
//    private int width;
//    private List<String> imagePaths = new ArrayList<>();
//    private List<String> remoteParams = new ArrayList<>();
//    private LoadingDialog loading;
//    private TextView identView;
//    private static int state;
//
//
//
//    @Override
//    protected void onCreate(Bundle arg0) {
//        super.onCreate(arg0);
//        setContentView(R.layout.);
//        width = ViewUtils.getDisplayWidth(this);
//        showTileLeft();
//        showTileMid("资产证明");
//        bindViews();
//        initData();
//    }
//
//    @Override
//    protected int layoutID() {
//        return R.layout.acitivity_asset_prove;
//    }
//
//    @Override
//    protected void init(Bundle savedInstanceState) {
//
//    }
//
//    @Override
//    protected BasePresenterImpl createPresenter() {
//        return null;
//    }
//
//    private void bindViews() {
//        frameLayout = (FixGridLayout) findViewById(R.id.fix_grid_layout);
//        commit = (Button)findViewById(R.id.commit);
//        checkResult = (TextView)findViewById(R.id.check_result);
//        identView = (TextView)findViewById(R.id.choose_identify);
//        description = (TextView) findViewById(R.id.asset_prove_des);
//        ViewUtils.setTextColorAndLink(context, description, R.string.hotline, getResources().getColor(R.color.orange), new ViewUtils.OnClickHyperlinkListener() {
//            @Override
//            public void onClick(View v, String linkText) {
//                NavigationUtils.startDialgTelephone(AssetProveActivity.this, "4001888848");
//            }
//        });
//        viewGroup = (ViewGroup) findViewById(R.id.radio_group);
//        frameLayout.setmCellWidth(width / 4);
//        frameLayout.setmCellHeight(width / 4);
//        //添加按钮
//        commit.setOnClickListener(this);
//    }
//
//    public ImageView addImg(){
//        ImageView addImage = new ImageView(getApplicationContext());
//        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(width/4,width/4);
//        addImage.setLayoutParams(layoutParams);
//        addImage.setImageResource(R.drawable.asset_prove_upload);
//        addImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (imagePaths.size() >= 10) {
//                    new MToast(AssetProveActivity.this).show("最多上传10张图片", 0);
//                    return;
//                }
//                NavigationUtils.startSystemImageForResult(AssetProveActivity.this, Contant.REQUEST_IMAGE);
//            }
//        });
//        return addImage;
//    }
//
//    private void initData() {
//        UserInfoC userInfoC = MApplication.getUser().getToC();
//        String vas = userInfoC.getAssetsCertificationImage();
//        int status = userInfoC.getAssetsCertificationStatus();
//
//        boolean hasStatus = false;
//        switch (status) {
//            case 1:
//                checkResult.setVisibility(View.VISIBLE);
//                checkResult.setText("审核状态：待审核");
//                commit.setVisibility(View.GONE);
//                hasStatus = true;
//                break;
//            case 2:
//                checkResult.setVisibility(View.VISIBLE);
//                checkResult.setText("审核状态：已通过");
//                commit.setVisibility(View.GONE);
//                hasStatus = true;
//                break;
//            case 3:
//                checkResult.setVisibility(View.VISIBLE);
//                checkResult.setText("审核状态：被驳回");
//                if (TextUtils.isEmpty(vas)) {
//                    frameLayout.addView(addImg());
//                }
//                break;
//            default:
//                checkResult.setVisibility(View.GONE);
//                frameLayout.addView(addImg());
//                identView.setText("选择投资者身份");
//                break;
//        }
//
//        if (!TextUtils.isEmpty(vas)) {
//            try {
//                JSONArray jsonArray = new JSONArray(vas);
//                initImageListData(jsonArray);
//                updateImageViewLayout();
//            } catch (Exception e) {
//                e.printStackTrace();
//                Log.e(AssetProveActivity.class.getName(), e.getMessage());
//            }
//        }
//
//        if (hasStatus) {
//            if ("1".equals(userInfoC.getInvestmentType())) {
//                ((RadioButton)viewGroup.getChildAt(0)).setChecked(true);
//                viewGroup.getChildAt(1).setVisibility(View.GONE);
//            } else if ("2".equals(userInfoC.getInvestmentType())) {
//                ((RadioButton)viewGroup.getChildAt(1)).setChecked(true);
//                viewGroup.getChildAt(0).setVisibility(View.GONE);
//            }
//        }
//    }
//
//    private void initImageListData(JSONArray jsonArray) {
//        try {
//            imagePaths.clear();
//            for (int i = 0; i < jsonArray.length(); i++) {
//                imagePaths.add((String)jsonArray.get(i));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void updateImageViewLayout() {
//        frameLayout.removeAllViews();
//        for(int i=0; i < imagePaths.size(); i++){
//            String bean= imagePaths.get(i);
//            ImageView view = new ImageView(this);
//            setImgLayoutParams(view);
//            BitmapUtils bu = new BitmapUtils(this);
//            if (!TextUtils.isEmpty(bean) && !bean.startsWith("http") && bean.contains(Contant.UPLOAD_CERTIFICATE_TYPE)) {
//                bean = Domain.urlStr + bean;
//                bu.display(view, bean);
//            } else if (bean.startsWith("http")) {
//                bu.display(view, bean);
//            } else {
//                setImageBitmap(view, bean);
//            }
//            view.setTag(bean);
//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    String url =  (String)v.getTag();
//                    Intent intent = new Intent(AssetProveActivity.this, SmoothImageActivity.class);
//                    intent.putExtra(SmoothImageActivity.IMAGE_SAVE_PATH_LOCAL, url);
//                    intent.putExtra(SmoothImageActivity.IMAGE_RIGHT_DELETE, TextUtils.isEmpty(MApplication.getUser().getToC().getAssetsCertificationImage()));
//                    startActivityForResult(intent, SMOTH_CODE);
//                }
//            });
//            frameLayout.addView(view);
//        }
//
//        if (0 == MApplication.getUser().getToC().getAssetsCertificationStatus() ||
//                3 == MApplication.getUser().getToC().getAssetsCertificationStatus()) {
//            frameLayout.addView(addImg());
//        }
//    }
//
////    //更新布局数据
////    public void upDateLayout(){
////        if (TextUtils.isEmpty(MApplication.getUser().getToC().getAssetsCertificationImage())) {
////            frameLayout.removeAllViews();
////        } else {
////            frameLayout.removeViewAt(frameLayout.getChildCount() - 1);
////        }
////        for(int i=0;i<imagePaths.size();i++){
////            String bean= imagePaths.get(i);
////            ImageView view = new ImageView(this);
////            setImgLayoutParams(view);
////            setImageBitmap(view, bean);
////            view.setTag(bean);
////            view.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View v) {
////                    String url = (String) v.getTag();
////                    Intent intent = new Intent(AssetProveActivity.this, SmoothImageActivity.class);
////                    intent.putExtra(SmoothImageActivity.IMAGE_SAVE_PATH_LOCAL, url);
////                    startActivity(intent);
////                }
////            });
////            frameLayout.addView(view);
////        }
////        frameLayout.addView(addImg());
////    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.title_left:
//                finish();
//                break;
//            case R.id.relative_add:
//                /**
//                 * 调用图片选择和拍照页面 会在@{MainActivity}页面的onActivityResult方法回掉
//                 */
//                Intent ii = new Intent(this, MultiImageSelectorActivity.class);
//                // 是否显示拍摄图片
//                ii.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
//                ii.putExtra("clear", 1);
//                // 最大可选择图片数量
//                // ii.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 5);
//                // 选择模式
//                ii.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_SINGLE);
//                ii.putExtra("camerasensortype", 2);
//                // 默认选择
//                // if (mSelectPath != null && mSelectPath.size() > 0) {
//                // ii.putExtra(
//                // MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST,
//                // mSelectPath);
//                // }
//                startActivityForResult(ii, Contant.REQUEST_IMAGE);
//                break;
//            case R.id.commit:
//                commitProveInfo();
//                break;
//        }
//    }
//
//    private boolean isSelectRole() {
//        int value =  viewGroup.getChildCount();
//        for (int i = 0;i < value; i++) {
//            RadioButton radioButton = (RadioButton)viewGroup.getChildAt(i);
//            if (radioButton.isChecked()) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public void commitProveInfo() {
//        if (!isSelectRole()) {
//            new MToast(AssetProveActivity.this).show("请选择投资者身份", 0);
//            return;
//        }
//        if (imagePaths.size() == 0) {
//            new MToast(AssetProveActivity.this).show("请先上传资料证明", 0);
//            return;
//        }
//        uploadFileProve();
//    }
//
//    private void uploadFileProve() {
//        if (loading == null) {
//            loading = new LoadingDialog(this);
//        }
//        loading.show();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                remoteParams.clear();
//                for (final String localPath : imagePaths) {
//                    if (localPath.contains(Contant.UPLOAD_CERTIFICATE_TYPE) || localPath.startsWith("http")) {
//                        continue;
//                    }
//                    String newTargetFile = com.cgbsoft.privatefund.utils.FileUtils.compressFileToUpload(localPath, true);
//                    String paths = DownloadUtils.postObject(newTargetFile, Contant.UPLOAD_CERTIFICATE_TYPE);
//                    com.cgbsoft.privatefund.utils.FileUtils.deleteFile(newTargetFile);
//                    if (!TextUtils.isEmpty(paths)) {
//                        remoteParams.add(paths);
//                    } else {
//                        ThreadUtils.runOnMainThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                new MToast(AssetProveActivity.this).show("证明文件上传失败，请重新上传", 0);
//                            }
//                        });
//                        loading.dismiss();
//                        return;
//                    }
//                }
//                uploadInfo();
//            }
//        }).start();
//    }
//
//    private JSONArray getArrayParams(List<String> lists) {
//        JSONArray jsonArray = new JSONArray();
//        for (int i = 0; i < lists.size() ; i++) {
//            jsonArray.put(lists.get(i).startsWith("http") ? lists.get(i) : Domain.urlStr + lists.get(i));
//        }
//        return jsonArray;
//    }
//
//    private void uploadInfo() {
//        String params = ApiParams.requestParamAssetProve(MApplication.getUserid(), getArrayParams(remoteParams) , getSelectType());
//        new AssetProveTask(context).start(params, new HttpResponseListener() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    loading.dismiss();
//                    String results = response.get("result").toString();
//                    if ("suc".equals(results)) {
//                        MApplication.getUser().getToC().setAssetsCertificationStatus(1);
//                        MApplication.getUser().getToC().setInvestmentType(((RadioButton)viewGroup.getChildAt(0)).isChecked() ? "1" : "2");
//                        new MToast(AssetProveActivity.this).show("提交成功", 0);
//                        EventBus.getDefault().post(new RefushUser());
//                        finish();
//                    } else {
//                        MApplication.getUser().getToC().setAssetsCertificationStatus(0);
//                        new MToast(AssetProveActivity.this).show("资产证明失败", 0);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onErrorResponse(String error, int statueCode) {
//                loading.dismiss();
//                new MToast(AssetProveActivity.this).show(error, 0);
//            }
//        });
//    }
//
//    private String getSelectType() {
//        if (((RadioButton)viewGroup.getChildAt(0)).isChecked()) {
//            return "1";
//        }
//        return "2";
//    }
//
//    /**
//     * 设置imageview的尺寸
//     */
//    private void setImgLayoutParams(ImageView img) {
//        //ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) img.getLayoutParams();
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, 0);
//        lp.width = (int) (ViewUtils.getDisplayWidth(this)/3);
//        lp.height = lp.width;
//        img.setLayoutParams(lp);
//        img.setScaleType(ImageView.ScaleType.CENTER_CROP);
//    }
//    /**
//     * 为imageview设置图片
//     */
//    private void setImageBitmap(ImageView img, String url){
//        FileInputStream fis = null;
//        img.setImageBitmap(MyBitmapUtils.getLoacalBitmap(url));
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == Contant.REQUEST_IMAGE) {
//            if (resultCode == RESULT_OK) {
//                List<String> mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
//                if (mSelectPath != null && mSelectPath.size() > 0) {
//                    imagePaths.add( mSelectPath.get(0));
//                    updateImageViewLayout();
//                }
//            }
//        } else if (requestCode == SMOTH_CODE) {
//            if (resultCode == RESULT_OK) {
//                String url = data.getStringExtra("deletPath");
//                String needVas = null;
//                for (String vas : imagePaths) {
//                    if (url.contains(vas)) {
//                        needVas = vas;
//                        break;
//                    }
//                }
//                if (!TextUtils.isEmpty(needVas)) {
//                    imagePaths.remove(needVas);
//                    updateImageViewLayout();
//                }
//            }
//        }
//    }
//}
