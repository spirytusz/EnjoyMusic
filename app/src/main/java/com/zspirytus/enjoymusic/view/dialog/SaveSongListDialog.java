package com.zspirytus.enjoymusic.view.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.base.BaseDialogFragment;
import com.zspirytus.enjoymusic.interfaces.annotations.LayoutIdInject;
import com.zspirytus.enjoymusic.interfaces.annotations.ViewInject;
import com.zspirytus.enjoymusic.utils.PixelsUtil;

@LayoutIdInject(R.layout.dialog_save_song_list)
public class SaveSongListDialog extends BaseDialogFragment implements View.OnClickListener {

    @ViewInject(R.id.edit_text)
    private EditText mEditText;
    @ViewInject(R.id.ok_btn)
    private TextView mOkBtn;
    @ViewInject(R.id.cancel_btn)
    private TextView mCancleBtn;

    private OnDialogButtonClickListener mListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.SaveSongListDialog);
    }

    @Override
    protected void initView() {
        mOkBtn.setOnClickListener(this);
        mCancleBtn.setOnClickListener(this);
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
    protected void setDialogAttribute(Window window) {
        WindowManager.LayoutParams lp = getDialog().getWindow().getAttributes();
        lp.dimAmount = 0.8f;
        lp.width = PixelsUtil.getPixelsConfig()[0] - 2 * PixelsUtil.dp2px(getContext(), 40);
        lp.height = PixelsUtil.dp2px(getContext(), 120);
        window.setAttributes(lp);
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ok_btn:
                if (mListener != null) {
                    mListener.onPositiveButtonClick(mEditText.getText().toString());
                    dismiss();
                }
                break;
            case R.id.cancel_btn:
                dismiss();
                break;
        }
    }

    public void setOnDialogButtonClickListener(OnDialogButtonClickListener l) {
        mListener = l;
    }

    public interface OnDialogButtonClickListener {
        void onPositiveButtonClick(String content);
    }
}
