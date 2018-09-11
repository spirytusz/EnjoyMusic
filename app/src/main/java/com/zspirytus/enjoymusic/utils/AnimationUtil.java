package com.zspirytus.enjoymusic.utils;

import android.animation.ObjectAnimator;

import com.zspirytus.enjoymusic.cache.finalvalue.FinalValue;

/**
 * Created by ZSpirytus on 2018/9/10.
 */

public class AnimationUtil {

    public static ObjectAnimator ofFloat(Object object, String property, int duration, float... values) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(object, property, values);
        animator.setDuration(duration);
        return animator;
    }

    public static ObjectAnimator ofFloat(Object object, String property, float... values) {
        return ofFloat(object, property, FinalValue.AnimationDuration.SHORT_DURATION, values);
    }

    public static ObjectAnimator ofInt(Object object, String property, int... values) {
        return ObjectAnimator.ofInt(object, property, values);
    }
}
