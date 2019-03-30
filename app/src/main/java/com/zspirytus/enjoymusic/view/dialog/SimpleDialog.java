package com.zspirytus.enjoymusic.view.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
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

@LayoutIdInject(R.layout.dialog_simple)
public class SimpleDialog extends BaseDialogFragment {

    private static final String CONTENT_KEY = "content";

    @ViewInject(R.id.title)
    private TextView mTitle;
    @ViewInject(R.id.tip_text)
    private TextView mTip;
    @ViewInject(R.id.ok_btn)
    private TextView mOkBtn;
    @ViewInject(R.id.cancel_btn)
    private TextView mCancelBtn;

    private OnButtonClickListener mListener;

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
        getDialog().setCanceledOnTouchOutside(true);
    }


    @Override
    protected void setDialogAttribute(Window window) {
        WindowManager.LayoutParams lp = getDialog().getWindow().getAttributes();
        lp.dimAmount = 0.8f;
        lp.width = PixelsUtil.getPixelsConfig()[0] - 2 * PixelsUtil.dp2px(getContext(), 40);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    @Override
    protected void initView() {
        int contentResId = getArguments().getInt(CONTENT_KEY);
        mTip.setText(contentResId);
        if (mListener != null) {
            mOkBtn.setOnClickListener(v -> mListener.onPositiveBtnClick());
            mCancelBtn.setOnClickListener(v -> mListener.onNegativeBtnClick());
        }
    }

    public void setOnButtonClickListener(OnButtonClickListener listener) {
        mListener = listener;
    }

    public static SimpleDialog getInstance(@StringRes int content) {
        SimpleDialog dialog = new SimpleDialog();
        Bundle bundle = new Bundle();
        bundle.putInt(CONTENT_KEY, content);
        dialog.setArguments(bundle);
        return dialog;
    }

    public interface OnButtonClickListener {
        void onPositiveBtnClick();

        void onNegativeBtnClick();
    }
}
