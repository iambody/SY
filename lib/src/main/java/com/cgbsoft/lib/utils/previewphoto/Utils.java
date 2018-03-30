package com.cgbsoft.lib.utils.previewphoto;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;

import java.io.File;
import java.util.concurrent.ExecutionException;

/**
 */


public class Utils {

    public static String getImagePathFromCache(Context context, String url, int expectW, int expectH){

        FutureTarget<File> future  = Glide.with(context.getApplicationContext()).load(url).downloadOnly(expectW,expectH);

        try {
            File cacheFile = future.get();
            String path = cacheFile.getAbsolutePath();
            return path;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

}
