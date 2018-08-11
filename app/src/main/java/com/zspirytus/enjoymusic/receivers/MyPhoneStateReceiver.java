package com.zspirytus.enjoymusic.receivers;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.zspirytus.enjoymusic.services.MediaPlayHelper;
import com.zspirytus.enjoymusic.utils.ToastUtil;
import com.zspirytus.enjoymusic.view.activity.BaseActivity;

/**
 * Created by ZSpirytus on 2018/8/11.
 */

public class MyPhoneStateReceiver extends BroadcastReceiver {

    final PhoneStateListener phoneStateListener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            MediaPlayHelper mediaPlayHelper = MediaPlayHelper.getInstance();
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    // 响铃
                    ToastUtil.showToast(BaseActivity.getContext(), "响铃");
                    mediaPlayHelper.interruptByEvent();
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    // 接听
                    ToastUtil.showToast(BaseActivity.getContext(), "接听");
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    // 挂断
                    ToastUtil.showToast(BaseActivity.getContext(), "挂断");
                    mediaPlayHelper.resumeAfterEvent();
                    break;
            }
        }
    };

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            // 去电
            ToastUtil.showToast(BaseActivity.getContext(), "去电");
        } else {
            // 非去电，判断电话状态
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
            telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        }
    }
}
