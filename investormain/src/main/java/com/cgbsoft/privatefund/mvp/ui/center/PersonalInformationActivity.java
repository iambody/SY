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
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.dialog.WheelDialogAddress;
import com.cgbsoft.lib.utils.tools.LogUtils;
import com.cgbsoft.lib.widget.CircleImageView;
import com.cgbsoft.privatefund.BuildConfig;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.adapter.BottomMenuAdapter;
import com.cgbsoft.privatefund.mvp.contract.center.PersonalInformationContract;
import com.cgbsoft.privatefund.mvp.presenter.center.PersonalInformationPresenterImpl;
import com.cgbsoft.privatefund.utils.Bimp;
import com.cgbsoft.privatefund.utils.PermissionsChecker;
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
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 个人信息页面
 * Created by sunfei on 2017/7/8 0008.
 */
@Route(RouteConfig.GOTOC_PERSONAL_INFORMATION_ACTIVITY)
public class PersonalInformationActivity extends BaseActivity<PersonalInformationPresenterImpl> implements PersonalInformationContract
        .PersonalInformationView {
    @BindView(R.id.title_left)
    ImageView back;
    @BindView(R.id.title_mid)
    TextView titleTV;
    @BindView(R.id.civ_personal_information_icon)
    CircleImageView iconImg;
    @BindView(R.id.tv_user_date)
    TextView userDate;
    @BindView(R.id.rl_show_datepicker)
    RelativeLayout changeUserDate;
    @BindView(R.id.tv_address_str)
    TextView userAddress;
    private Dialog mHeadIconDialog;
    private String mPhotoPath;
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
    private PermissionsChecker mPermissionsChecker;
    private int REQUEST_CODE = 2000; // 请求码
    private String[] PERMISSIONS = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    private SimpleDateFormat simpleDateFormat;
    private int mYear;
    private int mMonth;
    private int mDay;
    private Calendar calendar;

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);
    }

    @OnClick(R.id.title_left)
    public void clickBack() {
        this.finish();
    }

    @OnClick(R.id.rl_personal_information_icon_all)
    public void changeIcon() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (null == mPermissionsChecker) {
                mPermissionsChecker = new PermissionsChecker(this);
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
        initView(savedInstanceState);
        initHeadIconDialog();
    }

    private void initView(Bundle savedInstanceState) {
        simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.CHINA);
        calendar = Calendar.getInstance();
        String format = simpleDateFormat.format(calendar.getTime());
        userDate.setText(format);
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        back.setVisibility(View.VISIBLE);
        titleTV.setText(getResources().getString(R.string.personal_information_title));
    }

    @Override
    protected PersonalInformationPresenterImpl createPresenter() {
        return null;
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

        tvCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mHeadIconDialog.dismiss();
            }
        });
    }

    /**
     * 相机拍摄图片
     */
    private void takePhotoByCamera() {
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
            }
        } else if (requestCode == REQUEST_CODE) {
            mHeadIconDialog.show();
        }
    }

    private void deletePhoto() {    // 删除相机拍摄的照片
        StorageKit.deleteFile(getPhotoFile());
    }

    private void handlePhotoResult(int requestCode, Intent resultIntent) {
        if (requestCode == REQUEST_HEAD_CAMERA) {
            dispatchCropImage();    // 照片拍摄需要裁剪
        } else if (requestCode == RESULT_PIC_SELECT) {
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

        new DatePickerDialog(baseContext, android.R.style.Theme_Material_Light_Dialog_Alert, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                String format = simpleDateFormat.format(calendar.getTime());
                userDate.setText(format);
            }
        }, mYear, mMonth, mDay).show();
    }

    @OnClick(R.id.rl_show_address)
    public void showAddressDialog() {
        List<Map<String, Object>> parentList=null;
        try {
            StringBuilder sb = new StringBuilder();
            InputStream open = getResources().getAssets().open("address.json");
            BufferedReader bis = new BufferedReader(new InputStreamReader(open));
            String line = "";
            while ((line = bis.readLine()) != null) {
                sb.append(line);
            }
            String sbs = sb.toString();
            parentList = new Gson().fromJson(sbs, new TypeToken<List<Map<String, Object>>>() {
            }.getType());
        } catch (IOException e) {
            LogUtils.Log("aaa", "printStackTrace===");
            e.printStackTrace();
        }

        WheelDialogAddress dialogAddress = new WheelDialogAddress(this);
        dialogAddress.setList(parentList);
        dialogAddress.setTitle("选择地区");
        dialogAddress.setConfirmCallback(new WheelDialogAddress.ConfirmListenerInteface() {

            @Override
            public void confirm(Map<String, Object> map) {
                if (map != null) {
                    Iterator<Map.Entry<String, Object>> iterators = map.entrySet().iterator();
                    Map.Entry<String, Object> entry = iterators.next();
                    String key = entry.getKey();
                    if (!TextUtils.isEmpty(key) && key.equals("sub") && iterators.hasNext()) {
                        entry = iterators.next();
                    }
                    String nameChinese = entry.getKey();
                    String nameEnglish = (String) entry.getValue();
                    List<Map<String, Object>> child = (List<Map<String, Object>>) map.get("sub");
                    String childNameChinese = "";
                    String childNameEnglish = "";
                    String districtName = "";
                    String districtNum = "";
                    if (null != child) {
                        String childPositionStr = (String) map.get("child_position");
                        String grandSonPositionStr = (String) map.get("grandson_position");
                        int childPositionInt = Integer.parseInt(childPositionStr);
                        int grandSonPositionInt = Integer.parseInt(grandSonPositionStr);
                        Map<String, Object> childMap = child.get(childPositionInt);
                        Iterator<Map.Entry<String, Object>> iteratorsChild = childMap.entrySet().iterator();
                        Map.Entry<String, Object> childEntry = iteratorsChild.next();
                        String childKey = childEntry.getKey();
                        if (!TextUtils.isEmpty(childKey) && childKey.equals("sub") && iteratorsChild.hasNext()) {
                            childEntry = iteratorsChild.next();
                        }
                        childNameChinese = childEntry.getKey();
                        childNameEnglish = (String) childEntry.getValue();
                        List<Map<String, Object>> grandson = (List<Map<String, Object>>) childMap.get("sub");
                        if (null != grandson) {
                            Map<String, Object> grandSonMap = grandson.get(grandSonPositionInt);
                            Map.Entry<String, Object> grandSonEntry = grandSonMap.entrySet().iterator().next();
                            districtName = grandSonEntry.getKey();
                            districtNum = (String) grandSonEntry.getValue();
                        }
                    }
                    userAddress.setText(nameChinese.concat(childNameChinese).concat(districtName));
                    Toast.makeText(getApplicationContext(), "nameChinese==="+nameChinese+"---childNameChinese==="+childNameChinese+"---districtName==="+districtName, Toast.LENGTH_SHORT).show();
//                    callback2(function, nameChinese, nameEnglish, childNameChinese, childNameEnglish);
                }
            }
        });
        dialogAddress.show();
    }
}
