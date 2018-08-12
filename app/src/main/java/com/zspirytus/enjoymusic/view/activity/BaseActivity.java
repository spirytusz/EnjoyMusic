package com.zspirytus.enjoymusic.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.zspirytus.enjoymusic.Interface.ViewInject;
import com.zspirytus.enjoymusic.receivers.MyHeadPhoneStateReceiver;
import com.zspirytus.enjoymusic.receivers.MyPhoneStateReceiver;
import com.zspirytus.enjoymusic.utils.LogUtil;
import com.zspirytus.enjoymusic.utils.ToastUtil;
import com.zspirytus.zspermission.ZSPermission;

import java.lang.reflect.Field;

/**
 * Created by ZSpirytus on 2018/8/2.
 */

public abstract class BaseActivity extends RxAppCompatActivity {

    private MyPhoneStateReceiver myPhoneStateReceiver = new MyPhoneStateReceiver();
    private MyHeadPhoneStateReceiver myHeadPhoneStateReceiver = new MyHeadPhoneStateReceiver();

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
    protected void onStart() {
        super.onStart();
        registerBroadCast();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterBroadCast();
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

    /**
     * Register or Unregister Broadcast
     */

    private void registerBroadCast() {
        // register phone state broadcast
        IntentFilter phoneStateFilter = new IntentFilter();
        phoneStateFilter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
        phoneStateFilter.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
        registerReceiver(myPhoneStateReceiver, phoneStateFilter);

        // register head phone state broadcast
        IntentFilter headPhoneFilter = new IntentFilter();
        headPhoneFilter.addAction(Intent.ACTION_HEADSET_PLUG);
        registerReceiver(myHeadPhoneStateReceiver, phoneStateFilter);
    }

    private void unregisterBroadCast() {
        // unregister phone state broadcast
        unregisterReceiver(myPhoneStateReceiver);

        // unregister head phone state broadcast
        unregisterReceiver(myHeadPhoneStateReceiver);
    }

    /**
     * Log or Global Tools
     */

    public static Context getContext() {
        return context;
    }

    public final void e(String message) {
        String TAG = this.getClass().getSimpleName();
        LogUtil.e(TAG, message);
    }

    public final void toast(String message) {
        ToastUtil.showToast(this, message);
    }

}
