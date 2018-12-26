package com.zspirytus.enjoymusic.base;

import android.os.Bundle;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.engine.FragmentVisibilityManager;
import com.zspirytus.enjoymusic.view.fragment.MusicListDetailFragment;

public class BaseViewPagerItemFragment extends LazyLoadBaseFragment {

    protected void showMusicDetailFragment(String filterAlbum, String filterArtist) {
        MusicListDetailFragment fragment = MusicListDetailFragment.getInstance();
        Bundle bundle = new Bundle();
        bundle.putString("album", filterAlbum);
        bundle.putString("artist", filterArtist);
        fragment.setArguments(bundle);
        FragmentVisibilityManager.getInstance().show(fragment, R.id.full_fragment_container);
    }
}
