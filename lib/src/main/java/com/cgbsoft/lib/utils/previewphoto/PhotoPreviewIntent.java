package com.cgbsoft.lib.utils.previewphoto;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 类       名:图片预览
 */
public class PhotoPreviewIntent extends Intent {

    private Context mContext;
    private List<PrePhotoInfo> paths;

    public PhotoPreviewIntent(Context packageContext) {
        super(packageContext, PhotoPreviewActivity.class);
        mContext = packageContext;
    }

    /**
     * 照片地址
     *
     * @param
     */
    public void setPhotoPaths(List<String> pathss) {
        if (null == pathss) return  ;

        paths = new ArrayList<>();
        for (int i = 0; i < pathss.size(); i++) {
            PrePhotoInfo prePhotoInfo = new PrePhotoInfo();
            prePhotoInfo.setPath(pathss.get(i));
            paths.add(prePhotoInfo);
        }
//        this.paths = paths;

    }


    /**
     * 当前照片的下标
     *
     * @param currentItem
     */
    public PhotoPreviewIntent setCurrentItem(int currentItem) {
        this.putExtra(PhotoPreviewActivity.EXTRA_CURRENT_ITEM, currentItem);
        return this;
    }

    /**
     * 设置默认图片
     *
     * @param res
     */
    public void setDefluatDrawble(int res) {
        this.putExtra(PhotoPreviewActivity.EXTRA_DF_DRAWBLE, res);

    }

    /**
     * 设置默认图片
     *
     * @param skip
     */
    public PhotoPreviewIntent skipMemory(boolean skip) {
        this.putExtra(PhotoPreviewActivity.EXTRA_SKIP_MEMORY, skip);
        return this;
    }


    public void launch() {


        if (paths == null) {
            throw new NullPointerException("paths is null");
        }
        if (paths.size() == 0) {
            throw new RuntimeException("paths size must > 0");
        }

        //获取本地图片
        final List<PrePhotoInfo> prePhotoInfos = new ArrayList<>();

        Observable.from(paths)
                .subscribeOn(Schedulers.io())
                .doOnNext(new Action1<PrePhotoInfo>() {
                    @Override
                    public void call(PrePhotoInfo photoInfo) {
                        prePhotoInfos.add(photoInfo);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        setFlags(FLAG_ACTIVITY_NEW_TASK);
                        putParcelableArrayListExtra(PhotoPreviewActivity.EXTRA_PHOTOS, (ArrayList<? extends Parcelable>) prePhotoInfos);
                        mContext.startActivity(PhotoPreviewIntent.this);
                    }
                })
                .subscribe();


    }
}
