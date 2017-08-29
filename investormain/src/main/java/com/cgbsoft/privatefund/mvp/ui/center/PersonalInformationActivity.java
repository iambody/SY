package com.cgbsoft.privatefund.mvp.ui.center;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cgbsoft.lib.AppInfStore;
import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.model.UserInfoDataEntity;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.base.webview.BaseWebViewActivity;
import com.cgbsoft.lib.base.webview.CwebNetConfig;
import com.cgbsoft.lib.base.webview.WebViewConstant;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.dialog.WheelDialogAddress;
import com.cgbsoft.lib.listener.listener.GestureManager;
import com.cgbsoft.lib.permission.MyPermissionsActivity;
import com.cgbsoft.lib.permission.MyPermissionsChecker;
import com.cgbsoft.lib.utils.constant.RxConstant;
import com.cgbsoft.lib.utils.imgNetLoad.Imageload;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.widget.RoundImageView;
import com.cgbsoft.lib.widget.dialog.LoadingDialog;
import com.cgbsoft.privatefund.BuildConfig;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.adapter.BottomMenuAdapter;
import com.cgbsoft.privatefund.mvp.contract.center.PersonalInformationContract;
import com.cgbsoft.privatefund.mvp.presenter.center.PersonalInformationPresenterImpl;
import com.cgbsoft.privatefund.mvp.ui.home.MineFragment;
import com.cgbsoft.privatefund.utils.Bimp;
import com.cgbsoft.privatefund.utils.StorageKit;
import com.chenenyu.router.annotation.Route;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import app.ndk.com.enter.mvp.ui.start.PermissionsActivity;
import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;

/**
 * 个人信息页面
 * Created by sunfei on 2017/7/8 0008.
 */
@Route(RouteConfig.GOTOC_PERSONAL_INFORMATION_ACTIVITY)
public class PersonalInformationActivity extends BaseActivity<PersonalInformationPresenterImpl> implements PersonalInformationContract
        .PersonalInformationView {
    private static final int REQUEST_CODE_TO_CHANGE_ANME = 1002;
    private static final int REQUEST_CODE_TO_CHANGE_GENDER = 1003;
//    @BindView(R.id.toolbar)
//    protected Toolbar toolbar;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.title_mid)
    TextView titleTV;
    @BindView(R.id.civ_personal_information_icon)
    RoundImageView iconImg;
    @BindView(R.id.tv_user_date)
    TextView userDate;
    @BindView(R.id.rl_show_datepicker)
    RelativeLayout changeUserDate;
    @BindView(R.id.tv_address_str)
    TextView userAddress;
    private Dialog mHeadIconDialog;
    private String mPhotoPath;
    @BindView(R.id.tv_personal_num)
    TextView userNum;
    @BindView(R.id.tv_personal_name)
    TextView userName;
    @BindView(R.id.tv_personal_gender)
    TextView userGender;
    @BindView(R.id.tv_member_level)
    TextView memberLevel;
    @BindView(R.id.ll_my_qr)
    LinearLayout myQrAll;
    @BindView(R.id.tv_identity)
    TextView identityStatus;

    private boolean showAssert;

    private static String levelName;

    /**
     * 更新头像，拍照REQUEST_CODE
     */
    private static final int REQUEST_HEAD_CAMERA = 1400;

    /**
     * 更新头像，从手机相册选择REQUEST_CODE
     */
    private static final int RESULT_PIC_SELECT = 1402;
    /**
     * 裁剪图片REQUEST_CODE
     */
    private static final int REQUEST_CROP = 1403;
    private static final int HEAD_WIDTH = 128;//头像宽度
    private static final int HEAD_HEIGHT = 128;//头像高度
    private int REQUEST_CODE = 2000; // 请求码
    private String[] PERMISSIONS = new String[]{Manifest.permission.CAMERA};
    private SimpleDateFormat simpleDateFormat;
    private int mYear;
    private int mMonth;
    private int mDay;
    private Calendar calendar;
    private UserInfoDataEntity.UserInfo userInfo;
    private LoadingDialog mLoadingDialog;
    private Observable<Integer> uploadIcon;
    private Observable<Boolean> swtichCentifyObservable;
    private android.os.Handler handler = new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    hideLoadDialog();
                    //上传头像成功
                    uploadRemotePath();
                    break;
                case 2:
                    //上传头像失败
                    hideLoadDialog();
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.upload_icon_fail), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private MyPermissionsChecker mPermissionsChecker;
    private boolean isClickBack;
    private boolean hasIndentity;
    private boolean hasUpload;
    private String indentityCode;
    private String title;
    private String credentialCode;
    private String status;

    @Override
    protected void before() {
        super.before();
        showAssert = AppManager.isShowAssert(this);
    }

    private void startPermissionsActivity() {
        MyPermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);
    }
    @OnClick(R.id.rl_personal_information_qr)
    public void gotoMyQr(){
        Intent intent = new Intent(this, BaseWebViewActivity.class);
        intent.putExtra(WebViewConstant.push_message_url, CwebNetConfig.myqr);
        intent.putExtra(WebViewConstant.push_message_title, getResources().getString(R.string.myqr));
        startActivity(intent);
    }

    /**
     * 点击证件夹
     */
    @OnClick(R.id.rl_personal_information_card_collect)
    public void gotoCardCollect(){
        if (showAssert) {
            goToCardCollect();
        } else {
            GestureManager.showGroupGestureManage(this, GestureManager.CENTIFY_DIR);
        }
    }

    private void goToCardCollect() {
        if (null == status) {
            isClickBack=true;
            getPresenter().verifyIndentity();
        } else {
            isClickBack=false;
            if (hasIndentity) {
                if (hasUpload) {//去证件列表
                    Intent intent = new Intent(this, CardCollectActivity.class);
                    intent.putExtra("indentityCode",indentityCode);
                    startActivity(intent);
                } else {//去上传证件照
                    Intent intent = new Intent(this, UploadIndentityCradActivity.class);
                    intent.putExtra("credentialCode",credentialCode);
                    intent.putExtra("indentityCode",indentityCode);
                    intent.putExtra("title", title);
                    startActivity(intent);
                }
            } else {//无身份
                Intent intent = new Intent(this, SelectIndentityActivity.class);
                startActivity(intent);
            }
        }
    }

    /**
     * 点击我的会员
     */
    @OnClick(R.id.rl_goto_member_center)
    public void gotoMemberCenter(){
        Intent intent = new Intent(this, BaseWebViewActivity.class);
        intent.putExtra(WebViewConstant.push_message_title, getResources().getString(R.string.mymember));
        intent.putExtra(WebViewConstant.push_message_url, CwebNetConfig.membercenter);
        startActivity(intent);
    }

    /**
     * 点击姓名
     */
    @OnClick(R.id.rl_username_all)
    public void changeUserName(){
        Intent intent = new Intent(this, ChangeNameActivity.class);
        String nickName = userName.getText().toString();
        if (!TextUtils.isEmpty(nickName)) {
            intent.putExtra("name", nickName);
        }
        startActivityForResult(intent,REQUEST_CODE_TO_CHANGE_ANME);
//        NavigationUtils.startActivityByRouterForResult(baseContext,RouteConfig.GOTO_CHANGE_USERNAME_ACTIVITY,REQUEST_CODE_TO_CHANGE_ANME);
    }

    /**
     * 点击性别
     */
    @OnClick(R.id.rl_usergender_all)
    public void changeUserGender(){
        Intent intent = new Intent(this, ChangeGenderActivity.class);
        startActivityForResult(intent,REQUEST_CODE_TO_CHANGE_GENDER);
//        NavigationUtils.startActivityByRouterForResult(baseContext,RouteConfig.GOTO_CHANGE_USERGENDER_ACTIVITY,REQUEST_CODE_TO_CHANGE_GENDER);
    }

    /**
     * 更换头像
     */
    @OnClick(R.id.rl_personal_information_icon_all)
    public void changeIcon() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (null == mPermissionsChecker) {
                mPermissionsChecker = new MyPermissionsChecker(this);
            }
            // 缺少权限时, 进入权限配置页面
            if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
                startPermissionsActivity();
                return;
            }
        }
        mHeadIconDialog.show();
    }

    @Override
    protected int layoutID() {
        return R.layout.activity_personal_information;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        userInfo = AppManager.getUserInfo(baseContext);
        levelName = getIntent().getStringExtra(MineFragment.LEVER_NAME);
        initView(savedInstanceState);
        initHeadIconDialog();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getPresenter().verifyIndentity();
    }

    private void initView(Bundle savedInstanceState) {
        uploadIcon = RxBus.get().register(RxConstant.GOTO_PERSONAL_INFORMATION, Integer.class);
        uploadIcon.subscribe(new RxSubscriber<Integer>() {
            @Override
            protected void onEvent(Integer integer) {
                handler.sendEmptyMessage(integer);
                /*switch (integer) {
                    case 1:
                        //上传头像成功
                        uploadRemotePath();
                        break;
                    case 2:
                        //上传头像失败
                        hideLoadDialog();
                        Toast.makeText(getApplicationContext(), "上传头像失败", Toast.LENGTH_SHORT).show();
                        break;
                }*/
            }

            @Override
            protected void onRxError(Throwable error) {

            }
        });

        swtichCentifyObservable = RxBus.get().register(RxConstant.GOTO_SWITCH_CENTIFY_DIR, Boolean.class);
        swtichCentifyObservable.subscribe(new RxSubscriber<Boolean>() {
            @Override
            protected void onEvent(Boolean boovalue) {
//                getPresenter().verifyIndentity();
                goToCardCollect();
            }

            @Override
            protected void onRxError(Throwable error) {

            }
        });

        mLoadingDialog = LoadingDialog.getLoadingDialog(baseContext, "", false, false);
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        calendar = Calendar.getInstance();
//        String format = simpleDateFormat.format(calendar.getTime());
//        userDate.setText(format);
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        titleTV.setText(getResources().getString(R.string.personal_information_title));
//        setSupportActionBar(toolbar);
//        toolbar.setNavigationIcon(com.cgbsoft.lib.R.drawable.ic_back_black_24dp);
//        toolbar.setNavigationOnClickListener(v -> finish());
        userInfo = AppManager.getUserInfo(baseContext);
        if (null != userInfo) {
            String bandingAdviserId = userInfo.getToC().getBandingAdviserId();
            if (!TextUtils.isEmpty(bandingAdviserId)) {
                myQrAll.setVisibility(View.VISIBLE);
            } else {
                myQrAll.setVisibility(View.GONE);
            }
            String phoneNum = userInfo.getPhoneNum();
            if (!TextUtils.isEmpty(phoneNum)) {
                phoneNum = phoneNum.substring(0, 3).concat("****").concat(phoneNum.substring(7));
            } else {
                phoneNum="未绑定手机号";
            }
            userNum.setText(phoneNum);

            Imageload.display(PersonalInformationActivity.this,userInfo.getHeadImageUrl(),iconImg, R.drawable.logo, null);

            userName.setText(TextUtils.isEmpty(userInfo.getNickName())?"":userInfo.getNickName());

            userGender.setText(TextUtils.isEmpty(userInfo.getSex())?"":userInfo.getSex());

            userDate.setText(TextUtils.isEmpty(userInfo.getBirthday())?"":userInfo.getBirthday());

//            memberLevel.setText(TextUtils.isEmpty(userInfo.getToC().getMemberLevel())?"无":userInfo.getToC().getMemberLevel());
            memberLevel.setText(TextUtils.isEmpty(levelName)?"无":levelName);
        }


    }

    @Override
    protected PersonalInformationPresenterImpl createPresenter() {
        return new PersonalInformationPresenterImpl(baseContext,this);
    }

    /**
     * 初始化更换头像对话框
     */
    private void initHeadIconDialog() {
        if (null != mHeadIconDialog) {
            return;
        }
        mHeadIconDialog = new Dialog(this, R.style.bottomMenuDialog);
        mHeadIconDialog.setContentView(R.layout.bm_menu_dialog);

        ListView menu = (ListView) mHeadIconDialog.findViewById(R.id.lv_items);
        TextView tvCancel = (TextView) mHeadIconDialog.findViewById(R.id.tv_cancel);


        String[] items = getResources().getStringArray(R.array.dialog_upload_head_items);
        BottomMenuAdapter menuAdapter = new BottomMenuAdapter(this);
        menuAdapter.setTextColor(0xffe96d48, true);
        menuAdapter.setData(items);

        menu.setAdapter(menuAdapter);
        menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mHeadIconDialog.dismiss();
                switch (position) {
                    case 0:    // 拍照
                        takePhotoByCamera();
                        break;
                    case 1:    // 相册
                        selectPhotoFromAlbum();
                        break;

                    default:
                        break;
                }
            }
        });

        tvCancel.setOnClickListener(v -> mHeadIconDialog.dismiss());
    }

    /**
     * 相机拍摄图片
     */
    private void takePhotoByCamera() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            // 缺少权限时, 进入权限配置页面
//            if (needPermissions(PERMISSIONS)) {
//                startPermissionsActivity();
//                return;
//            }
//        }
        String action = MediaStore.ACTION_IMAGE_CAPTURE;
        if (!isIntentAvailable(this, action)) {
            Toast.makeText(getApplicationContext(), "您的手机不支持相机拍摄", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent takePictureIntent = new Intent(action);
        File f = getPhotoFile();
        mPhotoPath = f.getAbsolutePath();
        Uri contentUri;
        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            contentUri = FileProvider.getUriForFile(baseContext, BuildConfig.APPLICATION_ID, f);
        } else {
            contentUri = Uri.fromFile(f);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
//            install.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            Uri uri = FileProvider.getUriForFile(_context, "com.cgbsoft.privatefund.fileProvider", file);
//            install.setDataAndType(uri, "application/vnd.android.package-archive");
        }
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        startActivityForResult(takePictureIntent, REQUEST_HEAD_CAMERA);
    }

    /**
     * 检查是否有对应ACTION的intent
     *
     * @param context
     * @param action
     * @return
     */
    public static boolean isIntentAvailable(Context context, String action) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    private static File getPhotoFile() {
        return new File(StorageKit.getTempFilePath(), "head_photo.jpg");
    }

    private static File getCroppedHeadFile() {
        return new File(StorageKit.getTempFilePath(), "head_upload.jpg");
    }

    /**
     * 从相册选择照片
     */
    private void selectPhotoFromAlbum() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_PIC_SELECT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_HEAD_CAMERA || requestCode == RESULT_PIC_SELECT) {    // 修改头像
            if (resultCode == Activity.RESULT_OK) {
                handlePhotoResult(requestCode, data);
            }
        } else if (requestCode == REQUEST_CROP) {    // 裁剪图片
            if (resultCode == Activity.RESULT_OK) {
                deletePhoto();
                Bitmap bitmap = BitmapFactory.decodeFile(mPhotoPath);//Bimp.revitionImageSizeHalf(filepath);
                bitmap = Bimp.rotateBitmap(bitmap, mPhotoPath);// 处理某些手机图片旋转问题
                iconImg.setImageBitmap(bitmap);
                updateLoadIcon();
            }
        } else if (requestCode == REQUEST_CODE&&resultCode== PermissionsActivity.PERMISSIONS_GRANTED) {
            changeIcon();
//            mHeadIconDialog.show();
        } else if (requestCode==REQUEST_CODE_TO_CHANGE_ANME) {
            userInfo = AppManager.getUserInfo(baseContext);
            if (null != userInfo) {
                userName.setText(userInfo.getNickName());
            }
        } else if (requestCode==REQUEST_CODE_TO_CHANGE_GENDER) {
            if (null == data) {
                return;
            }
            String gender = data.getStringExtra("gender");
            userGender.setText(gender);
            getPresenter().updateUserInfoToServer(null!=userInfo?userInfo.getRealName():"",gender,null!=userInfo?userInfo.getBirthday():"");
        }
    }

    /**
     * 上传头像
     */
    private void updateLoadIcon() {

        getPresenter().uploadIcon(mPhotoPath);
//        showLoadDialog();
//        new Thread(() -> {
//            String newTargetFile = com.cgbsoft.lib.utils.dm.Utils.helper.FileUtils.compressFileToUpload(mPhotoPath, false);
//            imageId = DownloadUtils.postObject(newTargetFile, Constant.UPLOAD_USERICONNEWC_TYPE);
//            com.cgbsoft.lib.utils.dm.Utils.helper.FileUtils.deleteFile(newTargetFile);
//            if (!TextUtils.isEmpty(imageId)) {
//                uploadRemotePath(imageId);
//            } else {
//                iconImg.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        hideLoadDialog();
//                        Toast.makeText(getApplicationContext(), "上传头像失败", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        }).start();
    }

    /**
     * 上传头像的远程路径给服务端
     */
    private void uploadRemotePath() {
        String userId = AppManager.getUserId(baseContext);
        getPresenter().uploadRemotePath(userId);
    }

    private void deletePhoto() {    // 删除相机拍摄的照片
        StorageKit.deleteFile(getPhotoFile());
    }

    private void handlePhotoResult(int requestCode, Intent resultIntent) {
        if (requestCode == REQUEST_HEAD_CAMERA) {
            dispatchCropImage();    // 照片拍摄需要裁剪
        } else if (requestCode == RESULT_PIC_SELECT) {
            if (null == resultIntent) {
                return;
            }
            Uri selectedImage = resultIntent.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            mPhotoPath = picturePath;
            dispatchCropImage();
        }
    }

    /**
     * 进行裁剪图片操作
     */
    private void dispatchCropImage() {
        if (mPhotoPath == null) {
            return;
        }
        try {
            Intent intent = new Intent("com.android.camera.action.CROP");
            Uri contentUri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
//            install.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            Uri uri = FileProvider.getUriForFile(_context, "com.cgbsoft.privatefund.fileProvider", file);
//            install.setDataAndType(uri, "application/vnd.android.package-archive");
            }
            //判断是否是AndroidN以及更高的版本
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                contentUri = FileProvider.getUriForFile(baseContext, BuildConfig.APPLICATION_ID, new File(mPhotoPath));
            } else {
                contentUri = Uri.fromFile(new File(mPhotoPath));
            }
            intent.setDataAndType(Uri.fromFile(new File(mPhotoPath)), "image/*");
            putCropIntentExtras(intent);
            File f = getCroppedHeadFile();
            mPhotoPath = f.getAbsolutePath();
            Uri contentUriF;
            //判断是否是AndroidN以及更高的版本
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                contentUriF = FileProvider.getUriForFile(baseContext, BuildConfig.APPLICATION_ID, f);
            } else {
                contentUriF = Uri.fromFile(f);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
//            install.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            Uri uri = FileProvider.getUriForFile(_context, "com.cgbsoft.privatefund.fileProvider", file);
//            install.setDataAndType(uri, "application/vnd.android.package-archive");
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
            startActivityForResult(intent, REQUEST_CROP);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "您的手机不支持裁剪图片", Toast.LENGTH_SHORT).show();
        }
    }

    private static void putCropIntentExtras(Intent intent) {
        if (intent == null) {
            return;
        }
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", HEAD_WIDTH);
        intent.putExtra("outputY", HEAD_HEIGHT);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
    }

    @OnClick(R.id.rl_show_datepicker)
    public void showDatePicker() {

        DatePickerDialog datePickerDialog = new DatePickerDialog(baseContext, android.R.style.Theme_Material_Light_Dialog_Alert, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                String format = simpleDateFormat.format(calendar.getTime());
                userDate.setText(format);
                updateServerDate(format);
            }
        }, mYear, mMonth, mDay);
        DatePicker datePicker = datePickerDialog.getDatePicker();
        datePicker.setMaxDate(new Date().getTime());
        datePickerDialog.show();
    }

    /**
     * 向服务端更新用户生日
     * @param format
     */
    private void updateServerDate(String format) {
        getPresenter().updateUserInfoToServer(null!=userInfo?userInfo.getRealName():"",null!=userInfo?userInfo.getSex():"",format);
    }

    @OnClick(R.id.rl_show_address)
    public void showAddressDialog() {
        List<Map<String, Object>> parentList=null;
        try {
            StringBuilder sb = new StringBuilder();
            InputStream open = getResources().getAssets().open("city.json");
            BufferedReader bis = new BufferedReader(new InputStreamReader(open));
            String line = "";
            while ((line = bis.readLine()) != null) {
                sb.append(line);
            }
            String sbs = sb.toString();
            parentList = new Gson().fromJson(sbs, new TypeToken<List<Map<String, Object>>>() {
            }.getType());
        } catch (IOException e) {
            e.printStackTrace();
        }

        WheelDialogAddress dialogAddress = new WheelDialogAddress(this);
        dialogAddress.setList(parentList);
        dialogAddress.setTitle("选择地区");
        dialogAddress.setConfirmCallback(new WheelDialogAddress.ConfirmListenerInteface() {

            @Override
            public void confirm(Map<String, Object> map) {
                if (map != null) {
                    String province = (String) map.get("province");
                    List<Map<String,Object>> cityList = (List<Map<String, Object>>) map.get("city");
                    String childPositionStr = (String) map.get("child_position");
                    String grandSonPositionStr = (String) map.get("grandson_position");
                    int childPositionInt = Integer.parseInt(childPositionStr);
                    int grandSonPositionInt = Integer.parseInt(grandSonPositionStr);
                    Map<String, Object> cityObj = cityList.get(childPositionInt);
                    String cityName = (String) cityObj.get("n");
                    List<Map<String,Object>> districtList = (List<Map<String, Object>>) cityObj.get("areas");
                    Map<String, Object> districtObj = districtList.get(grandSonPositionInt);
                    String districtName = (String) districtObj.get("s");
                    userAddress.setText(province.concat(cityName).concat(districtName));
                    Toast.makeText(getApplicationContext(), "province==="+province+"---cityName==="+cityName+"---districtName==="+districtName, Toast.LENGTH_SHORT).show();

//                    Iterator<Map.Entry<String, Object>> iterators = map.entrySet().iterator();
//                    Map.Entry<String, Object> entry = iterators.next();
//                    String key = entry.getKey();
//                    if (!TextUtils.isEmpty(key) && key.equals("sub") && iterators.hasNext()) {
//                        entry = iterators.next();
//                    }
//                    String nameChinese = entry.getKey();
//                    String nameEnglish = (String) entry.getValue();
//                    List<Map<String, Object>> child = (List<Map<String, Object>>) map.get("sub");
//                    String childNameChinese = "";
//                    String childNameEnglish = "";
//                    String districtName = "";
//                    String districtNum = "";
//                    if (null != child) {
//                        String childPositionStr = (String) map.get("child_position");
//                        String grandSonPositionStr = (String) map.get("grandson_position");
//                        int childPositionInt = Integer.parseInt(childPositionStr);
//                        int grandSonPositionInt = Integer.parseInt(grandSonPositionStr);
//                        Map<String, Object> childMap = child.get(childPositionInt);
//                        Iterator<Map.Entry<String, Object>> iteratorsChild = childMap.entrySet().iterator();
//                        Map.Entry<String, Object> childEntry = iteratorsChild.next();
//                        String childKey = childEntry.getKey();
//                        if (!TextUtils.isEmpty(childKey) && childKey.equals("sub") && iteratorsChild.hasNext()) {
//                            childEntry = iteratorsChild.next();
//                        }
//                        childNameChinese = childEntry.getKey();
//                        childNameEnglish = (String) childEntry.getValue();
//                        List<Map<String, Object>> grandson = (List<Map<String, Object>>) childMap.get("sub");
//                        if (null != grandson) {
//                            Map<String, Object> grandSonMap = grandson.get(grandSonPositionInt);
//                            Map.Entry<String, Object> grandSonEntry = grandSonMap.entrySet().iterator().next();
//                            districtName = grandSonEntry.getKey();
//                            districtNum = (String) grandSonEntry.getValue();
//                        }
//                    }
//                    userAddress.setText(nameChinese.concat(childNameChinese).concat(districtName));
//                    callback2(function, nameChinese, nameEnglish, childNameChinese, childNameEnglish);
                }
            }
        });
        dialogAddress.show();
    }

    @Override
    public void showLoadDialog() {
        try {
            if (mLoadingDialog.isShowing()) {
                return;
            }
            mLoadingDialog.show();
        } catch (Exception e) {
        }
    }

    @Override
    public void hideLoadDialog() {
        mLoadingDialog.dismiss();
    }

    @Override
    public void updateSuccess() {
        if (null != userInfo) {
            String newUserDate = userDate.getText().toString();
            String newUserGender = userGender.getText().toString();
            userInfo.setBirthday(newUserDate);
            userInfo.setSex(newUserGender);
            AppInfStore.saveUserInfo(baseContext,userInfo);
        }
    }

    @Override
    public void updateError(Throwable error) {
        userDate.setText(null!=userInfo?userInfo.getBirthday():"");
        userDate.setText(null!=userInfo?userInfo.getSex():"");

    }

    @Override
    public void uploadImgSuccess(String imageId) {
        if (null != userInfo) {
            userInfo.setHeadImageUrl(imageId);
            AppInfStore.saveUserInfo(baseContext,userInfo);
        }
    }

    @Override
    public void uploadImgError(Throwable error) {
        Toast.makeText(baseContext.getApplicationContext(),null!=error?error.getMessage():getResources().getString(R.string.upload_icon_fail),Toast.LENGTH_SHORT).show();
        if (null != userInfo) {
            Imageload.display(baseContext,userInfo.getHeadImageUrl(),iconImg, R.drawable.logo, null);
        }
    }

    @Override
    public void verifyIndentitySuccess(boolean hasIndentity, boolean hasUpload,String indentityCode,String title,String credentialCode,String status,String statusCode) {
        this.hasIndentity=hasIndentity;
        this.hasUpload=hasUpload;
        this.indentityCode=indentityCode;
        this.title=title;
        this.credentialCode=credentialCode;
        this.status=status;
        identityStatus.setText(status);
        if (isClickBack) {
            isClickBack=false;
            if (hasIndentity) {
                if (hasUpload) {//去证件列表
                    Intent intent = new Intent(this, CardCollectActivity.class);
                    intent.putExtra("indentityCode",indentityCode);
                    startActivity(intent);
                } else {//去上传证件照
                    Intent intent = new Intent(this, UploadIndentityCradActivity.class);
                    intent.putExtra("credentialCode",credentialCode);
                    intent.putExtra("indentityCode",indentityCode);
                    intent.putExtra("title", title);
                    startActivity(intent);
                }
            } else {//无身份
                Intent intent = new Intent(this, SelectIndentityActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    public void verifyIndentityError(Throwable error) {
        if (isClickBack) {
            isClickBack=false;
            Toast.makeText(getApplicationContext(),"服务器忙,请稍后再试!",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (swtichCentifyObservable != null) {
            RxBus.get().unregister(RxConstant.GOTO_SWITCH_CENTIFY_DIR, swtichCentifyObservable);
        }
    }
}
