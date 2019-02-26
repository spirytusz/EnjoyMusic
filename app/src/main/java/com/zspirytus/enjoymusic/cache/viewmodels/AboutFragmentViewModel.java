package com.zspirytus.enjoymusic.cache.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.zspirytus.enjoymusic.entity.listitem.AboutItem;

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
        List<AboutItem> aboutItems = new ArrayList<>();

        AboutItem item = new AboutItem();
        item.setIsTitle(true);
        item.setTitle("关于享乐");
        aboutItems.add(item);

        item = new AboutItem();
        item.setCommonItem(true);
        item.setOnlyMainTitle(false);
        item.setMainTitle("当前版本");
        item.setSubTitle("v1.0");
        aboutItems.add(item);

        item = new AboutItem();
        item.setOnlyMainTitle(true);
        item.setCommonItem(true);
        item.setOnlyMainTitle(true);
        item.setMainTitle("分享");
        aboutItems.add(item);

        item = new AboutItem();
        item.setCommonItem(true);
        item.setOnlyMainTitle(false);
        item.setMainTitle("点个赞呗");
        item.setSubTitle("前往项目Star或者Fork鼓励作者");
        item.setExtraInfo("https://github.com/zkw012300/EnjoyMusic");
        aboutItems.add(item);

        item = new AboutItem();
        item.setIsTitle(true);
        item.setTitle("关于作者");
        aboutItems.add(item);

        item = new AboutItem();
        item.setCommonItem(true);
        item.setOnlyMainTitle(false);
        item.setMainTitle("GitHub");
        item.setSubTitle("https://github.com/zkw012300");
        item.setExtraInfo("https://github.com/zkw012300");
        aboutItems.add(item);

        item = new AboutItem();
        item.setCommonItem(true);
        item.setOnlyMainTitle(false);
        item.setMainTitle("博客");
        item.setSubTitle("http://www.zspirytus.com/");
        item.setExtraInfo("http://www.zspirytus.com/");
        aboutItems.add(item);
        mListItem.setValue(aboutItems);
    }
}
