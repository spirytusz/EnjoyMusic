package com.zspirytus.enjoymusic.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.zspirytus.enjoymusic.Interface.ViewInject;
import com.zspirytus.enjoymusic.utils.ToastUtil;

import java.lang.reflect.Field;

/**
 * Created by ZSpirytus on 2018/8/2.
 */

public abstract class BaseActivity extends RxAppCompatActivity {

    private static Context context;

    public abstract Integer getLayoutId();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        autoInjectAllField();
        context = getApplicationContext();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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

    public final void e(String message) {
        String TAG = this.getClass().getSimpleName();
        Log.e(TAG, message);
    }

    public final void toast(String message) {
        ToastUtil.showToast(this, message);
    }

    public static Context getContext(){
        return context;
    }

}
