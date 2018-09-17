package com.zspirytus.enjoymusic.view.fragment;

import android.support.v7.widget.RecyclerView;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.interfaces.LayoutIdInject;
import com.zspirytus.enjoymusic.interfaces.ViewInject;

/**
 * Created by ZSpirytus on 2018/9/17.
 */
@LayoutIdInject(R.layout.fragment_play_list)
public class PlayListFragment extends BaseFragment {

    @ViewInject(R.id.play_list_rv)
    private RecyclerView mPlayListRecyclerView;

    public static PlayListFragment getInstance() {
        return new PlayListFragment();
    }
}
