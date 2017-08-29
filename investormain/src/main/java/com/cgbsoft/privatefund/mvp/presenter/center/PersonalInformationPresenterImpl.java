package com.cgbsoft.privatefund.mvp.presenter.center;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.utils.constant.RxConstant;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.tools.DownloadUtils;
import com.cgbsoft.lib.utils.tools.LogUtils;
import com.cgbsoft.privatefund.model.PersonalInformationModelListener;
import com.cgbsoft.privatefund.model.impl.PersonalInformationModelImpl;
import com.cgbsoft.privatefund.mvp.contract.center.PersonalInformationContract;

/**
 * Created by sunfei on 2017/7/8 0008.
 */

public class PersonalInformationPresenterImpl extends BasePresenterImpl<PersonalInformationContract.PersonalInformationView> implements PersonalInformationContract.PersonalInformationPresenter ,PersonalInformationModelListener{


    private final PersonalInformationContract.PersonalInformationView personalInformationView;
    private final PersonalInformationModelImpl personalInformationModel;
    private String imageId;

    public PersonalInformationPresenterImpl(@NonNull Context context, @NonNull PersonalInformationContract.PersonalInformationView view) {
        super(context, view);
        this.personalInformationView=view;
        this.personalInformationModel=new PersonalInformationModelImpl();
    }

    /**
     * 更新用户信息
     * @param userName 新用户名
     * @param gender 新用户性别
     * @param birthday 新用户生日
     */
    @Override
    public void updateUserInfoToServer(String userName, String gender, String birthday) {
        personalInformationView.showLoadDialog();
        personalInformationModel.updateUserInfoToServer(getCompositeSubscription(),this,userName,gender,birthday);
    }

    @Override
    public void updateSuccess() {
        personalInformationView.hideLoadDialog();
        personalInformationView.updateSuccess();
    }

    @Override
    public void updateError(Throwable error) {
        personalInformationView.hideLoadDialog();
        personalInformationView.updateError(error);
    }

    @Override
    public void uploadImgSuccess() {
        personalInformationView.hideLoadDialog();
        personalInformationView.uploadImgSuccess(imageId);
    }

    @Override
    public void uploadImgError(Throwable error) {
        personalInformationView.hideLoadDialog();
        personalInformationView.uploadImgError(error);
    }

    @Override
    public void verifyIndentitySuccess(String result, String hasIdCard,String title,String credentialCode,String status,String statusCode,String customerName,String credentialNumber,String credentialTitle,String existStatus) {
        personalInformationView.hideLoadDialog();
        if (TextUtils.isEmpty(result)) {//无身份
            personalInformationView.verifyIndentitySuccess(false,false,null,null,null,status,statusCode);
        } else {//有身份
            if ("1001".equals(result) && "0".equals(hasIdCard)) {
                personalInformationView.verifyIndentitySuccess(true, false,result,title,credentialCode,status,statusCode);
            } else {
                personalInformationView.verifyIndentitySuccess(true,true,result,null,null,status,statusCode);
            }
        }
    }

    @Override
    public void verifyIndentityError(Throwable error) {
        personalInformationView.hideLoadDialog();
        personalInformationView.verifyIndentityError(error);
    }

    /**
     * 上传头像的远程路径给服务端
     */
    @Override
    public void uploadRemotePath(String adviserId) {
        personalInformationView.showLoadDialog();
        personalInformationModel.uploadRemotePath(getCompositeSubscription(),this,adviserId,imageId);
    }

    public void uploadIcon(String mPhotoPath) {
        personalInformationView.showLoadDialog();
        new Thread(() -> {
            String newTargetFile = com.cgbsoft.lib.utils.dm.Utils.helper.FileUtils.compressFileToUpload(mPhotoPath, false);
            imageId = DownloadUtils.postObject(newTargetFile, Constant.UPLOAD_USERICONNEWC_TYPE);
            com.cgbsoft.lib.utils.dm.Utils.helper.FileUtils.deleteFile(newTargetFile);
            if (!TextUtils.isEmpty(imageId)) {
                LogUtils.Log("aaa","imageId==="+imageId);
                RxBus.get().post(RxConstant.GOTO_PERSONAL_INFORMATION,1);
            } else {
                imageId = "";
                RxBus.get().post(RxConstant.GOTO_PERSONAL_INFORMATION,2);
//                iconImg.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        hideLoadDialog();
//                        Toast.makeText(getApplicationContext(), "上传头像失败", Toast.LENGTH_SHORT).show();
//                    }
//                });
            }
        }).start();
    }

    /**
     * 验证身份
     */
    public void verifyIndentity(){
        personalInformationView.showLoadDialog();
        personalInformationModel.verifyIndentity(getCompositeSubscription(),this);
    }
}
