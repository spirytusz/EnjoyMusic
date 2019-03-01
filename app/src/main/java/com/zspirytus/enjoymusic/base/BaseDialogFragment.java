package com.zspirytus.enjoymusic.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.zspirytus.basesdk.annotations.LayoutIdInject;
import com.zspirytus.basesdk.annotations.ViewInject;
import com.zspirytus.enjoymusic.utils.LogUtil;
import com.zspirytus.enjoymusic.utils.ToastUtil;

import java.lang.reflect.Field;

public abstract class BaseDialogFragment extends DialogFragment {

    private BaseActivity parentActivity;

    protected void initData() {
    }

    protected abstract void initView();

    protected void setDialogAttribute(Window window) {
    }

    @Override
    public void onAttach(Context context) {
        parentActivity = (BaseActivity) context;
        super.onAttach(context);
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
        initData();
        initView();
    }

    @Override
    public void onStart() {
        super.onStart();
        setDialogAttribute(getDialog().getWindow());
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
