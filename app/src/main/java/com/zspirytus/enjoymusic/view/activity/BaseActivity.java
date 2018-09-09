package com.zspirytus.enjoymusic.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.zspirytus.enjoymusic.interfaces.LayoutIdInject;
import com.zspirytus.enjoymusic.interfaces.ViewInject;
import com.zspirytus.enjoymusic.utils.LogUtil;
import com.zspirytus.enjoymusic.utils.ToastUtil;
import com.zspirytus.zspermission.ZSPermission;

import java.lang.reflect.Field;

/**
 * 所有Activity的基类，负责权限申请回调、view注入以及全局Tool的定义
 * Created by ZSpirytus on 2018/8/2.
 */

public abstract class BaseActivity extends AppCompatActivity
        implements ZSPermission.OnPermissionListener {

    private static Context context;
    private static AppCompatActivity appCompatActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        autoInjectLayoutId();
        autoInjectAllField();
        context = getApplicationContext();
        appCompatActivity = this;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ZSPermission.getInstance().onRequestPermissionResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onGranted() {
        // do nothing, processed by child activity if necessary
    }

    @Override
    public void onDenied() {
        // do nothing, processed by child activity if necessary
    }

    @Override
    public void onNeverAsk() {
        // do nothing, processed by child activity if necessary
    }

    public void autoInjectAllField() {
        try {
            Class<?> clazz = this.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(ViewInject.class)) {
                    ViewInject inject = field.getAnnotation(ViewInject.class);
                    int id = inject.value();
                    if (id > 0) {
                        field.setAccessible(true);
                        field.set(this, this.findViewById(id));
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public void autoInjectLayoutId() {
        Class<?> clazz = this.getClass();
        if (clazz.isAnnotationPresent(LayoutIdInject.class)) {
            LayoutIdInject layoutIdInject = clazz.getAnnotation(LayoutIdInject.class);
            int layoutId = layoutIdInject.value();
            if (layoutId > 0) {
                setContentView(layoutId);
            }
        }
    }

    /**
     * Log or Global Tools
     */

    public static Context getContext() {
        return context;
    }

    public static AppCompatActivity getActivity() {
        return appCompatActivity;
    }

    public final void e(String message) {
        String TAG = this.getClass().getSimpleName();
        LogUtil.e(TAG, message);
    }

    public final void out(Object message) {
        LogUtil.out(message);
    }

    public final void toast(String message) {
        ToastUtil.showToast(this, message);
    }

}
