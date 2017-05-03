package com.cgbsoft.privatefund.widget.navigation;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;

/**
 * User: Axl_Jacobs(Axl.Jacobs@gmail.com)
 * Date: 2016-04-05
 * Time: 19:52
 * FIXME
 */
public class ScrollingBehavior extends CoordinatorLayout.Behavior<View> {

    long systemCurrentTime;
    boolean mIsAnimatingOut = false;
    Interpolator INTERPOLATOR;

    float y = 0;

    public ScrollingBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        systemCurrentTime = System.nanoTime();
        INTERPOLATOR = new FastOutSlowInInterpolator();
    }


    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
        if (dy > 100 && child.getVisibility() == View.VISIBLE && System.nanoTime() - systemCurrentTime > 500 * 1000000) {
//            Log.e("behavior", "向上滑动");
            systemCurrentTime = System.nanoTime();
            animOut(child);
        } else if (dy < -50 && child.getVisibility() == View.GONE && System.nanoTime() - systemCurrentTime > 500 * 1000000) {
//            Log.e("behavior", "向下滑动");
            systemCurrentTime = System.nanoTime();
            animIn(child);
        }
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
    }


    private void animOut(View child) {
        ViewCompat.animate(child).y(child.getY() + 500)
                .setInterpolator(INTERPOLATOR).withLayer()
                .setListener(new ViewPropertyAnimatorListener() {
                    public void onAnimationStart(View view) {
                        ScrollingBehavior.this.mIsAnimatingOut = true;
                    }

                    public void onAnimationCancel(View view) {
                        ScrollingBehavior.this.mIsAnimatingOut = false;
                    }

                    public void onAnimationEnd(View view) {
                        ScrollingBehavior.this.mIsAnimatingOut = false;
                        view.setVisibility(View.GONE);
                    }
                }).start();
    }

    private void animIn(View child) {
        child.setVisibility(View.VISIBLE);
        ViewCompat.animate(child).y(child.getY() - 500)
                .setInterpolator(INTERPOLATOR).withLayer().setListener(null)
                .start();
    }
}