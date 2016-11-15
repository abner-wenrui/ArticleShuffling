package com.abner.widgetdemo.Utils;

import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.RelativeLayout;

/**
 * Project   com.abner.widgetdemo.Utils
 *
 * @Author Abner
 * Time   2016/9/27.16:36
 */
public class AnimationUtils {
    public static int runningAnimationCount = 0;

    //转出去的动画
    public static void rotateOutAnim(RelativeLayout layout, long delay) {
        RotateAnimation ra = new RotateAnimation(0, -180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.0f);
        ra.setAnimationListener(new MyAnimationListenner());
        ra.setDuration(500);
        for (int i = 0; i < layout.getChildCount(); i++) {
            layout.getChildAt(i).setEnabled(false);
        }
        ra.setFillAfter(true);
        ra.setStartOffset(delay);
        layout.startAnimation(ra);
    }

    //转进来的动画
    public static void rotateInAnim(RelativeLayout layout, long delay) {
        RotateAnimation ra = new RotateAnimation(-180f, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.0f);
        ra.setDuration(500);
        for (int i = 0; i < layout.getChildCount(); i++) {
            layout.getChildAt(i).setEnabled(true);
        }
        ra.setAnimationListener(new MyAnimationListenner());
        ra.setStartOffset(delay);
        ra.setFillAfter(true);
        layout.startAnimation(ra);
    }

    static class MyAnimationListenner implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {
            runningAnimationCount++;
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            runningAnimationCount--;

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}



