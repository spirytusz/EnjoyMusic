package com.zspirytus.enjoymusic.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * LazyLoadBaseFragment 懒加载Fragment基类
 * 懒加载的条件:
 * 1. View已被初始化
 * 2. isVisibleToUser == true (当Fragment对用户可见性改变时调用setUserVisibleHint(boolean))
 * 因此，设置三个flag:
 * 1. isViewCreated 初值为false，在onViewCreated(View, Bundle)中令其为true;
 * 2. isVisibleToUser 初值为false，在setUserVisibleHint(boolean)中将参数赋值给isVisibleToUser;
 * 3. hasLoaded 初值为false，当已经加载布局和数据后，令其为true.
 * 从而，懒加载的条件为：
 * isViewCreated && isVisibleToUser && !hasLoaded == true
 * <p>
 * 关于懒加载的时机，由于setUserVisibleHint(boolean)是独立于Fragment的生命周期的，所以分情况讨论
 * 当懒加载Fragment为初始Fragment时，在onViewCreated(View, Bundle)中调用lazyLoad()
 * 当懒加载Fragment不为初始Fragment时，此时在onViewCreated(View, Bundle)调用时，Fragment是不可见的，
 * 我们需要Fragment可见时再懒加载，因此可以在setUserVisibleHint(boolean)调用。
 * 因此，懒加载的调用时机是setUserVisibleHint(boolean)或onViewCreated(View, Bundle)，调用前加个条件判断就ok了。
 * Created by ZSpirytus on 2018/9/12.
 */

public abstract class LazyLoadBaseFragment extends BaseFragment {

    protected boolean isViewCreated = false;
    protected boolean isVisibleToUser = false;
    protected boolean hasLoaded = false;

    private volatile boolean isSuccess;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        lazyLoad();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        isViewCreated = true;
        lazyLoad();
    }

    protected void lazyLoad() {
        if (isViewCreated && isVisibleToUser && !hasLoaded) {
            Schedulers.io().scheduleDirect(() -> {
                try {
                    initData();
                    isSuccess = true;
                } catch (Throwable e) {
                    isSuccess = false;
                    e.printStackTrace();
                }
                AndroidSchedulers.mainThread().scheduleDirect(() -> {
                    initView();
                    onLoadState(isSuccess);
                });
            });
            hasLoaded = true;
        }
    }

}
