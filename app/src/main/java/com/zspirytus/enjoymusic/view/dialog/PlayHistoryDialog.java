package com.zspirytus.enjoymusic.view.dialog;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.zspirytus.basesdk.annotations.LayoutIdInject;
import com.zspirytus.basesdk.annotations.ViewInject;
import com.zspirytus.basesdk.recyclerview.adapter.CommonRecyclerViewAdapter;
import com.zspirytus.basesdk.recyclerview.viewholder.CommonViewHolder;
import com.zspirytus.basesdk.utils.PixelsUtil;
import com.zspirytus.basesdk.utils.TimeUtil;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.base.BaseDialogFragment;
import com.zspirytus.enjoymusic.cache.viewmodels.MainActivityViewModel;
import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.entity.listitem.PlayHistoryItem;
import com.zspirytus.enjoymusic.factory.LayoutManagerFactory;
import com.zspirytus.enjoymusic.impl.binder.aidlobserver.PlayHistoryObserverManager;
import com.zspirytus.enjoymusic.receivers.observer.PlayHistoryObserver;

import java.util.ArrayList;
import java.util.List;

@LayoutIdInject(R.layout.dialog_play_history)
public class PlayHistoryDialog extends BaseDialogFragment implements PlayHistoryObserver {

    @ViewInject(R.id.close_btn)
    private ImageView mCloseBtn;
    @ViewInject(R.id.recyclerview)
    private RecyclerView mRecyclerView;

    private CommonRecyclerViewAdapter<PlayHistoryItem> mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.PlayHistoryDialogStyle);
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = getDialog().getWindow().getAttributes();
        lp.dimAmount = 0.8f;
        getDialog().getWindow().setAttributes(lp);
        getDialog().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getDialog().setCanceledOnTouchOutside(true);
    }

    @Override
    protected void initData() {
        mAdapter = new CommonRecyclerViewAdapter<PlayHistoryItem>() {
            @Override
            public int getLayoutId() {
                return R.layout.item_play_history;
            }

            @Override
            public void convert(CommonViewHolder holder, PlayHistoryItem playHistoryItem, int position) {
                TextView no = holder.getView(R.id.item_no);
                TextView title = holder.getView(R.id.item_title);
                TextView info = holder.getView(R.id.item_info);
                holder.setText(R.id.item_no, String.valueOf(position + 1));
                holder.setText(R.id.item_title, playHistoryItem.getMusicName());
                holder.setText(R.id.item_info, TimeUtil.timestamp2Time(playHistoryItem.getMusicDuration()));
                if (playHistoryItem.isSelected()) {
                    no.setTextColor(getResources().getColor(R.color.colorPrimary));
                    title.setTextColor(getResources().getColor(R.color.colorPrimary));
                    info.setTextColor(getResources().getColor(R.color.colorPrimary));

                    no.setTypeface(Typeface.DEFAULT_BOLD);
                    title.setTypeface(Typeface.DEFAULT_BOLD);
                    info.setTypeface(Typeface.DEFAULT_BOLD);
                } else {
                    no.setTextColor(getResources().getColor(android.R.color.darker_gray));
                    title.setTextColor(getResources().getColor(android.R.color.darker_gray));
                    info.setTextColor(getResources().getColor(android.R.color.darker_gray));

                    no.setTypeface(Typeface.DEFAULT);
                    title.setTypeface(Typeface.DEFAULT);
                    info.setTypeface(Typeface.DEFAULT);
                }
            }
        };
        PlayHistoryObserverManager.getInstance().register(this);
    }

    @Override
    protected void initView() {
        mRecyclerView.setLayoutManager(LayoutManagerFactory.createLinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mCloseBtn.setOnClickListener(v -> dismiss());
    }

    @Override
    public void onPlayHistoryChange(List<Music> playHistory) {
        Music currentMusic = ViewModelProviders.of(getParentActivity()).get(MainActivityViewModel.class)
                .getCurrentPlayingMusic().getValue();
        List<PlayHistoryItem> playHistoryItems = new ArrayList<>();
        for (Music music : playHistory) {
            PlayHistoryItem item = new PlayHistoryItem(music);
            if (music != null && music.getMusicName().equals(currentMusic.getMusicName())) {
                item.setSelected(true);
            }
            playHistoryItems.add(item);
        }
        mAdapter.setList(playHistoryItems);
    }

    @Override
    protected void setDialogAttribute(Window window) {
        WindowManager.LayoutParams lp = getDialog().getWindow().getAttributes();
        lp.dimAmount = 0.8f;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = (int) (PixelsUtil.getPixelsConfig()[1] * 0.7f);
        window.setAttributes(lp);
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    @Override
    public void onDestroy() {
        PlayHistoryObserverManager.getInstance().unregister(this);
        super.onDestroy();
    }
}
