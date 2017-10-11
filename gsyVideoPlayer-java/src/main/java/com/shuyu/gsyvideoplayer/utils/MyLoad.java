package com.shuyu.gsyvideoplayer.utils;


import java.util.Map;

import tv.danmaku.ijk.media.player.IjkLibLoader;

/**
 * Created by fei on 2017/9/20.
 */

public class MyLoad implements IjkLibLoader{
    private Map<String,String> path;

    public MyLoad(Map<String,String> path) {
        this.path = path;
    }

    @Override
    public void loadLibrary(String s) throws UnsatisfiedLinkError, SecurityException {
        System.load(path.get(s));
    }
}
