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

@LayoutIdInject(R.layout.dialog_progress)
public class ProgressDialog extends BaseDialogFragment {

    private static final String PROGRESS_TEXT_KEY = "progressTextKey";

    @ViewInject(R.id.progress_text)
    private TextView mProgressText;

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
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);
    }

    @Override
    protected void initView() {
        Bundle bundle = getArguments();
        if (bundle != null && bundle.getString(PROGRESS_TEXT_KEY) != null) {
            String progressText = getArguments().getString(PROGRESS_TEXT_KEY);
            mProgressText.setText(progressText);
        } else {
            mProgressText.setText(R.string.default_progress_text);
        }
    }

    @Override
    protected void setDialogAttribute(Window window) {
        WindowManager.LayoutParams lp = getDialog().getWindow().getAttributes();
        lp.dimAmount = 0.8f;
        lp.width = PixelsUtil.getPixelsConfig()[0] - 2 * PixelsUtil.dp2px(getContext(), 40);
        lp.height = PixelsUtil.dp2px(getContext(), 84);
        window.setAttributes(lp);
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    public static ProgressDialog getInstance() {
        return getInstance(null);
    }

    public static ProgressDialog getInstance(String progressText) {
        ProgressDialog dialog = new ProgressDialog();
        if (progressText != null) {
            Bundle bundle = new Bundle();
            bundle.putString(PROGRESS_TEXT_KEY, progressText);
            dialog.setArguments(bundle);
        }
        return dialog;
    }
}
