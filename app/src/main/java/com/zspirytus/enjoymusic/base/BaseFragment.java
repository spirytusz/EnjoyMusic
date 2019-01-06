package com.zspirytus.enjoymusic.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.interfaces.IBackPressed;
import com.zspirytus.enjoymusic.interfaces.annotations.LayoutIdInject;
import com.zspirytus.enjoymusic.interfaces.annotations.ViewInject;
import com.zspirytus.enjoymusic.utils.LogUtil;
import com.zspirytus.enjoymusic.utils.ToastUtil;

import java.lang.reflect.Field;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Fragment 基类
 * Created by ZSpirytus on 2018/8/2.
 */

public abstract class BaseFragment extends Fragment implements IBackPressed {

    protected long pressedBackLastTime;

    private BaseActivity parentActivity;
    private boolean hasAnim = false;
    private volatile boolean isAnimLoadFinish = false;
    private volatile boolean isLoadSuccess;

    @Override
    public void onAttach(Context context) {
        parentActivity = (BaseActivity) context;
        super.onAttach(context);
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        LogUtil.e(this.getClass().getSimpleName(), "nextAnim = " + nextAnim);
        if (nextAnim != 0) {
            hasAnim = true;
            Animation anim = AnimationUtils.loadAnimation(getContext(), nextAnim);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    isAnimLoadFinish = true;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = autoInjectLayoutId(inflater, container);
        autoInjectAllField(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Schedulers.io().scheduleDirect(() -> {
            try {
                initData();
                isLoadSuccess = true;
            } catch (Throwable e) {
                isLoadSuccess = false;
                e.printStackTrace();
            }
            while (hasAnim && !isAnimLoadFinish) ;
            LogUtil.e(this.getClass().getSimpleName(), "isAnimLoadFinish = " + isAnimLoadFinish);
            AndroidSchedulers.mainThread().scheduleDirect(() -> {
                initView();
                onLoadState(isLoadSuccess);
                registerEvent();
            });
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unregisterEvent();
    }

    @Override
    public void goBack() {
        long now = System.currentTimeMillis();
        if (now - pressedBackLastTime < 2 * 1000) {
            getParentActivity().finish();
        } else {
            toast("Press back again to quit");
            pressedBackLastTime = now;
        }
    }

    protected abstract void initData();

    protected abstract void initView();

    public abstract int getContainerId();

    public int enterAnim() {
        return R.anim.anim_fragment_translate_show_up;
    }

    public int exitAnim() {
        return R.anim.anim_fragment_translate_show_down;
    }

    protected abstract void onLoadState(boolean isSuccess);

    protected void registerEvent() {
    }

    protected void unregisterEvent() {
    }

    private void autoInjectAllField(View view) {
        try {
            Class<?> clazz = this.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(ViewInject.class)) {
                    ViewInject inject = field.getAnnotation(ViewInject.class);
                    int id = inject.value();
                    if (id > 0) {
                        field.setAccessible(true);
                        field.set(this, view.findViewById(id));
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private View autoInjectLayoutId(LayoutInflater inflater, @Nullable ViewGroup container) {
        Class<?> clazz = this.getClass();
        if (clazz.isAnnotationPresent(LayoutIdInject.class)) {
            LayoutIdInject layoutIdInject = clazz.getAnnotation(LayoutIdInject.class);
            int layoutId = layoutIdInject.value();
            return inflater.inflate(layoutId, container, false);
        } else {
            return null;
        }
    }

    public final void e(String message) {
        String TAG = this.getClass().getSimpleName();
        LogUtil.e(TAG, message);
    }

    public final void out(Object message) {
        LogUtil.out(message);
    }

    public final void toast(String message) {
        ToastUtil.showToast(parentActivity, message);
    }

    public BaseActivity getParentActivity() {
        return parentActivity;
    }

}
