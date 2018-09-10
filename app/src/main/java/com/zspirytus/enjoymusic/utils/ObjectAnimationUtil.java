package com.zspirytus.enjoymusic.utils;

import android.animation.ObjectAnimator;

import com.zspirytus.enjoymusic.cache.finalvalue.FinalValue;

/**
 * Created by ZSpirytus on 2018/9/10.
 */

public class ObjectAnimationUtil {

    public static void ofFloat(Object object, String property, int duration, float... values) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(object, property, values);
        animator.setDuration(duration);
        animator.start();
    }

    public static void ofFloat(Object object, String property, float... values) {
        ofFloat(object, property, FinalValue.AnimationDuration.SHORT_DURATION, values);
    }
}
