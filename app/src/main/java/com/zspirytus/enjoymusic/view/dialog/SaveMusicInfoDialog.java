package com.zspirytus.enjoymusic.view.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.zspirytus.basesdk.annotations.LayoutIdInject;
import com.zspirytus.basesdk.annotations.ViewInject;
import com.zspirytus.basesdk.utils.PixelsUtil;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.base.BaseDialogFragment;

@LayoutIdInject(R.layout.dialog_save_music_info)
public class SaveMusicInfoDialog extends BaseDialogFragment {

    @ViewInject(R.id.ok_btn)
    private TextView mOkBtn;
    @ViewInject(R.id.cancel_btn)
    private TextView mCancelBtn;

    private OnDialogButtonClickListener mListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.SaveMusicInfoDialog);
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = getDialog().getWindow().getAttributes();
        lp.dimAmount = 0.8f;
        getDialog().getWindow().setAttributes(lp);
        getDialog().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    @Override
    protected void initView() {
        getDialog().setCanceledOnTouchOutside(false);
        mOkBtn.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onPositiveBtnClick();
            }
            dismiss();
        });
        mCancelBtn.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onNegativeBtnClick();
            }
            dismiss();
        });
    }

    @Override
    protected void setDialogAttribute(Window window) {
        WindowManager.LayoutParams lp = getDialog().getWindow().getAttributes();
        lp.dimAmount = 0.8f;
        lp.width = PixelsUtil.getPixelsConfig()[0] - 2 * PixelsUtil.dp2px(getContext(), 40);
        lp.height = PixelsUtil.dp2px(getContext(), 96);
        window.setAttributes(lp);
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    public void setOnDialogButtonClickListener(OnDialogButtonClickListener listener) {
        mListener = listener;
    }

    public interface OnDialogButtonClickListener {
        void onPositiveBtnClick();

        void onNegativeBtnClick();
    }
}
