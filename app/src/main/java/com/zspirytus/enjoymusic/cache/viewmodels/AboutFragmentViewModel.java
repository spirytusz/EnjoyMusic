package com.zspirytus.enjoymusic.cache.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.res.Resources;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.entity.listitem.AboutItem;
import com.zspirytus.enjoymusic.global.MainApplication;

import java.util.ArrayList;
import java.util.List;

public class AboutFragmentViewModel extends ViewModel {

    private MutableLiveData<List<AboutItem>> mListItem;

    public void init() {
        mListItem = new MutableLiveData<>();
    }

    public MutableLiveData<List<AboutItem>> getListItem() {
        return mListItem;
    }

    public void obtainListItem() {
        Resources resources = MainApplication.getAppContext().getResources();
        List<AboutItem> aboutItems = new ArrayList<>();

        AboutItem item = new AboutItem();
        item.setIsTitle(true);
        item.setTitle(resources.getString(R.string.about_enjoymusic));
        aboutItems.add(item);

        item = new AboutItem();
        item.setCommonItem(true);
        item.setOnlyMainTitle(false);
        item.setMainTitle(resources.getString(R.string.current_version));
        item.setSubTitle(resources.getString(R.string.current_version_value));
        aboutItems.add(item);

        item = new AboutItem();
        item.setOnlyMainTitle(true);
        item.setCommonItem(true);
        item.setOnlyMainTitle(true);
        item.setMainTitle(resources.getString(R.string.share));
        aboutItems.add(item);

        item = new AboutItem();
        item.setCommonItem(true);
        item.setOnlyMainTitle(false);
        item.setMainTitle(resources.getString(R.string.like));
        item.setSubTitle(resources.getString(R.string.go_to_repo));
        item.setExtraInfo(resources.getString(R.string.repo_url));
        aboutItems.add(item);

        item = new AboutItem();
        item.setDividerLine(true);
        aboutItems.add(item);

        item = new AboutItem();
        item.setIsTitle(true);
        item.setTitle(resources.getString(R.string.about_author));
        aboutItems.add(item);

        item = new AboutItem();
        item.setCommonItem(true);
        item.setOnlyMainTitle(false);
        item.setMainTitle(resources.getString(R.string.github));
        item.setSubTitle(resources.getString(R.string.github_url));
        item.setExtraInfo(resources.getString(R.string.github_url));
        aboutItems.add(item);

        item = new AboutItem();
        item.setCommonItem(true);
        item.setOnlyMainTitle(false);
        item.setMainTitle(resources.getString(R.string.blog));
        item.setSubTitle(resources.getString(R.string.blog_url));
        item.setExtraInfo(resources.getString(R.string.blog_url));
        aboutItems.add(item);

        item = new AboutItem();
        item.setDividerLine(true);
        aboutItems.add(item);

        mListItem.setValue(aboutItems);
    }
}
