package com.cgbsoft.lib.utils.imgNetLoad;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.io.File;


/**
 * 图片加载库（持续更新）
 * Created by user on 2016/11/4.
 */

public class Imageload {

    /**
     * 开始创建图片加载
     *
     * @param context context
     * @return 图片库
     */
    private static RequestManager imageWith(Context context) {
        return Glide.with(context.getApplicationContext());
    }


    /**
     * 创建DrawableTypeRequest
     *
     * @param requestManager requestManager
     * @param url            资源
     * @return DrawableTypeRequest
     */
    private static DrawableTypeRequest getDrawableTypeRequest(RequestManager requestManager, Object url) {
        DrawableTypeRequest mDrawableTypeRequest = null;
        if (url instanceof String) {
            mDrawableTypeRequest = requestManager.load(url.toString());
        } else if (url instanceof byte[]) {
            mDrawableTypeRequest = requestManager.load((byte[]) url);
        } else if (url instanceof File) {
            mDrawableTypeRequest = requestManager.load((File) url);
        } else if (url instanceof Uri) {
            mDrawableTypeRequest = requestManager.load((Uri) url);
        } else if (url instanceof Integer) {
            mDrawableTypeRequest = requestManager.load((Integer) url);
        }
        return mDrawableTypeRequest;
    }

    private static void base(DrawableTypeRequest drawableTypeRequest, ImageView imageView) {
        if (drawableTypeRequest != null) {
            drawableTypeRequest.into(imageView);
        }
    }

    /**
     * 显示图片
     *
     * @param context   context
     * @param res       资源：Uri， File， String， integer
     * @param imageView imageView
     */
    public static void display(@NonNull Context context, @NonNull Object res, @NonNull ImageView imageView) {
        base(getDrawableTypeRequest(imageWith(context), res), imageView);
    }

    /**
     * 显示图片
     *
     * @param context   context
     * @param res       资源：Uri， File， String， integer
     * @param imageView imageView
     * @param errorId   可以为Drawable、int、null（不设置error）
     * @param holdId    可以为Drawable、int、null（不设置placeHolder）
     */
    public static void display(@NonNull Context context, @NonNull Object res, @NonNull ImageView imageView, Object errorId, Object holdId) {
        DrawableTypeRequest requestCreator = getDrawableTypeRequest(imageWith(context), res);
        if (requestCreator == null) {
            return;
        }

        if (errorId != null)
            if (errorId instanceof Drawable) {
                requestCreator.error((Drawable) errorId);
            } else if (errorId instanceof Integer) {
                requestCreator.error((Integer) errorId);
            }
        if (holdId != null)
            if (holdId instanceof Drawable) {
                requestCreator.placeholder((Drawable) holdId);
            } else if (errorId instanceof Integer) {
                requestCreator.placeholder((Integer) holdId);
            }
        base(requestCreator, imageView);
    }
}
