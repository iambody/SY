package com.cgbsoft.lib.utils.damp;

import android.os.Build;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringSystem;

/**
 * 让按钮显示为缩放效果的类
 * Created by alex_xq on 14/12/31.
 */
public class SpringEffect {

    private static final String TAG = "SpringEffect";

    /***
     * down/up时展示spring效果，up成功时出发Runnable操作
     *
     * @param view     动画view对象
     * @param runnable invoke操作
     */
    public static void doEffectSticky(final View view, final Runnable runnable) {
        doEffectSticky(view, 0.2f, runnable);
    }

    public static void doEffectSticky(final ClickRunnable clickRunnable) {
        doEffectSticky(clickRunnable.getView(), 0.2f, clickRunnable);
    }

    public static void doEffectSticky(final View view, float range, final Runnable runnable) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            if (runnable != null)
                runnable.run();
        } else {
            SpringSystem springSystem = SpringSystem.create();
            final Spring spring = springSystem.createSpring();
            final int[] key = new int[]{-1};

            spring.addListener(new SimpleSpringListener() {
                @Override
                public void onSpringUpdate(Spring spring) {
                    float value = (float) spring.getCurrentValue();
                    float scale = 1f - (value * range);
                    view.setScaleX(scale);
                    view.setScaleY(scale);
                }

                @Override
                public void onSpringAtRest(Spring spring) {
                    super.onSpringAtRest(spring);
                    if (key[0] == MotionEvent.ACTION_UP && runnable != null) {
                        runnable.run();
                    }
                }
            });

            view.setOnTouchListener((v, event) -> {
                key[0] = event.getAction();
                switch (key[0]) {

                    case MotionEvent.ACTION_DOWN:
                        spring.setEndValue(1f);
                        break;

                    case MotionEvent.ACTION_UP:
                        spring.setEndValue(0f);
                        view.playSoundEffect(SoundEffectConstants.CLICK);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        spring.setEndValue(0f);
                        break;

                    case MotionEvent.ACTION_OUTSIDE:
                        spring.setEndValue(0f);
                        break;

                    default:
                        break;
                }
                return true;
            });
        }
    }
}
