package com.zspirytus.enjoymusic.view.fragment;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zspirytus.basesdk.annotations.LayoutIdInject;
import com.zspirytus.basesdk.annotations.ViewInject;
import com.zspirytus.basesdk.recyclerview.listeners.OnItemClickListener;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.adapter.SearchResultListAdapter;
import com.zspirytus.enjoymusic.base.BaseFragment;
import com.zspirytus.enjoymusic.cache.viewmodels.SearchFragmentViewModel;
import com.zspirytus.enjoymusic.db.QueryExecutor;
import com.zspirytus.enjoymusic.db.table.Album;
import com.zspirytus.enjoymusic.db.table.Artist;
import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.engine.ForegroundMusicController;
import com.zspirytus.enjoymusic.engine.FragmentVisibilityManager;
import com.zspirytus.enjoymusic.entity.listitem.SearchResult;
import com.zspirytus.enjoymusic.factory.LayoutManagerFactory;
import com.zspirytus.enjoymusic.utils.ToastUtil;

@LayoutIdInject(R.layout.fragment_search_layout)
public class SearchFragment extends BaseFragment implements OnItemClickListener {

    @ViewInject(R.id.back_btn)
    private ImageView mBackBtn;
    @ViewInject(R.id.edit_text)
    private EditText mEditText;
    @ViewInject(R.id.clear_text_btn)
    private ImageView mClearTextBtn;
    @ViewInject(R.id.search_btn)
    private ImageView mSearchBtn;
    @ViewInject(R.id.recyclerview)
    private RecyclerView mRecyclerView;
    @ViewInject(R.id.info_text)
    private TextView mInfoText;

    private SearchFragmentViewModel mViewModel;
    private SearchResultListAdapter mAdapter;

    @Override
    protected void initData() {
        mViewModel = ViewModelProviders.of(this).get(SearchFragmentViewModel.class);
        mViewModel.init(getParentActivity());
        mAdapter = new SearchResultListAdapter();
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    protected void initView() {
        mEditText.requestFocus();
        openSoftKeyBoard();

        mEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch();
                return true;
            }
            return false;
        });
        mClearTextBtn.setOnClickListener(v -> {
            mEditText.setText("");
            mInfoText.setVisibility(View.GONE);
        });
        mSearchBtn.setOnClickListener(v -> {
            performSearch();
        });
        mBackBtn.setOnClickListener(v -> goBack());
        mRecyclerView.setLayoutManager(LayoutManagerFactory.createLinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public int enterAnim() {
        return R.anim.anim_fragment_translate_show_up;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel.getSearchResultList().observe(this, values -> {
            if (values != null && !values.isEmpty()) {
                mAdapter.setData(values);
                mInfoText.setVisibility(View.GONE);
            } else {
                mAdapter.clearData();
                mInfoText.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public int getContainerId() {
        return R.id.full_fragment_container;
    }

    @Override
    public void onItemClick(View view, int position) {
        SearchResult result = mAdapter.getData().get(position);
        if (result.isMusic()) {
            Music music = result.getMusic();
            ForegroundMusicController.getInstance().play(music);
            ForegroundMusicController.getInstance().addToPlayList(music);
        } else if (result.isAlbum()) {
            Album album = result.getAlbum();
            FilterMusicListFragment fragment = FilterMusicListFragment.getInstance(album.getAlbumName(), QueryExecutor.findMusicList(album), FilterMusicListFragment.ALBUM_FLAG);
            FragmentVisibilityManager.getInstance().addCurrentFragmentToBackStack();
            FragmentVisibilityManager.getInstance().show(fragment);
        } else if (result.isArtist()) {
            Artist artist = result.getArtist();
            FilterMusicListFragment fragment = FilterMusicListFragment.getInstance(artist.getArtistName(), QueryExecutor.findMusicList(artist), FilterMusicListFragment.ARTIST_FLAG);
            FragmentVisibilityManager.getInstance().addCurrentFragmentToBackStack();
            FragmentVisibilityManager.getInstance().show(fragment);
        }
    }

    @Override
    public void goBack() {
        mEditText.clearFocus();
        closeSoftKeyBoard();

        FragmentVisibilityManager.getInstance().remove(this);
    }

    private void performSearch() {
        if (mEditText.getText().length() > 0) {
            mViewModel.applyToSearch(mEditText.getText().toString());
            closeSoftKeyBoard();
        } else {
            ToastUtil.showToast(getContext(), R.string.no_search_text);
        }
    }

    private void openSoftKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getParentActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    private void closeSoftKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getParentActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }
}
