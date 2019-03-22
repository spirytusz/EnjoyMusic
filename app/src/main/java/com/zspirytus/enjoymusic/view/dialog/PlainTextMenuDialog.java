package com.zspirytus.enjoymusic.view.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.zspirytus.basesdk.annotations.LayoutIdInject;
import com.zspirytus.basesdk.annotations.ViewInject;
import com.zspirytus.basesdk.recyclerview.adapter.CommonRecyclerViewAdapter;
import com.zspirytus.basesdk.recyclerview.listeners.OnItemClickListener;
import com.zspirytus.basesdk.recyclerview.viewholder.CommonViewHolder;
import com.zspirytus.basesdk.utils.PixelsUtil;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.base.BaseDialogFragment;
import com.zspirytus.enjoymusic.factory.LayoutManagerFactory;

import java.util.ArrayList;
import java.util.List;

@LayoutIdInject(R.layout.dialog_menu)
public class PlainTextMenuDialog extends BaseDialogFragment implements OnItemClickListener {

    private static final String TITLE_KEY = "title";
    private static final String MENU_TEXTS_KEY = "menuTexts";

    @ViewInject(R.id.title)
    private TextView mTitle;
    @ViewInject(R.id.recyclerview)
    private RecyclerView mRecyclerView;

    private OnMenuItemClickListener mListener;
    private CommonRecyclerViewAdapter<String> mAdapter;

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
        mRecyclerView.setLayoutManager(LayoutManagerFactory.createLinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        String title = getArguments().getString(TITLE_KEY);
        List<String> menuTexts = getArguments().getStringArrayList(MENU_TEXTS_KEY);
        mTitle.setText(title);
        mAdapter = new CommonRecyclerViewAdapter<String>() {
            @Override
            public int getLayoutId() {
                return R.layout.item_md_menu_item;
            }

            @Override
            public void convert(CommonViewHolder holder, String s, int position) {
                holder.setText(R.id.menu_item, s);
                holder.setOnItemClickListener(PlainTextMenuDialog.this);
            }
        };
        mAdapter.setList(menuTexts);
    }

    @Override
    public void onItemClick(View view, int position) {
        if (mListener != null) {
            mListener.onMenuItemClick(mAdapter.getList().get(position), position);
            dismiss();
        }
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener listener) {
        mListener = listener;
    }

    public void show(FragmentManager manager) {
        super.show(manager, this.getClass().getSimpleName());
    }

    public interface OnMenuItemClickListener {
        void onMenuItemClick(String menuText, int position);
    }

    public static PlainTextMenuDialog create(String title, List<String> menuTexts) {
        Bundle bundle = new Bundle();
        bundle.putString(TITLE_KEY, title);
        bundle.putStringArrayList(MENU_TEXTS_KEY, (ArrayList<String>) menuTexts);
        PlainTextMenuDialog dialog = new PlainTextMenuDialog();
        dialog.setArguments(bundle);
        return dialog;
    }
}
