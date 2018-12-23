package com.zspirytus.enjoymusic.base;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.view.fragment.MusicListDetailFragment;

public class BaseViewPagerItemFragment extends LazyLoadBaseFragment {

    protected void showMusicDetailFragment(FragmentManager fragmentManager, String filterAlbum, String filterArtist) {
        MusicListDetailFragment fragment = MusicListDetailFragment.getInstance();
        Bundle bundle = new Bundle();
        bundle.putString("album", filterAlbum);
        bundle.putString("artist", filterArtist);
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction()
                .add(R.id.full_fragment_container, fragment, "MusicListDetailFragment")
                .show(fragment)
                .commitAllowingStateLoss();
    }
}
