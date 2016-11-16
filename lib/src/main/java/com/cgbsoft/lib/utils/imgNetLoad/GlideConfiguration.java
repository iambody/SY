package com.cgbsoft.lib.utils.imgNetLoad;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper;
import com.bumptech.glide.module.GlideModule;
import com.cgbsoft.lib.utils.cache.CacheManager;

import java.io.File;

/**
 * Created by xiaoyu.zhang on 2016/1/13.
 */
public class GlideConfiguration implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setDiskCache(() -> {
            File file = new File(CacheManager.getCachePath(context, CacheManager.GLIDE));
            return DiskLruCacheWrapper.get(file, 1024 * 1024 * 100);
        });
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
