package com.zspirytus.enjoymusic.view.fragment;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zspirytus.basesdk.annotations.LayoutIdInject;
import com.zspirytus.basesdk.annotations.ViewInject;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.adapter.SearchResultListAdapter;
import com.zspirytus.enjoymusic.base.BaseFragment;
import com.zspirytus.enjoymusic.cache.viewmodels.SearchFragmentViewModel;
import com.zspirytus.enjoymusic.engine.FragmentVisibilityManager;
import com.zspirytus.enjoymusic.factory.LayoutManagerFactory;

@LayoutIdInject(R.layout.fragment_search_layout)
public class SearchFragment extends BaseFragment {

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
    }

    @Override
    protected void initView() {
        mEditText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getParentActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

        mClearTextBtn.setOnClickListener(v -> {
            mEditText.setText("");
            mInfoText.setVisibility(View.GONE);
        });
        mSearchBtn.setOnClickListener(v -> {
            if (mEditText.getText().length() > 0) {
                mViewModel.applyToSearch(mEditText.getText().toString());
            }
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
    public void goBack() {
        mEditText.clearFocus();
        InputMethodManager imm = (InputMethodManager) getParentActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);

        FragmentVisibilityManager.getInstance().remove(this);
    }
}
