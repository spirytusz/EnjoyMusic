package com.zspirytus.enjoymusic.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zspirytus.enjoymusic.interfaces.LayoutIdInject;
import com.zspirytus.enjoymusic.interfaces.ViewInject;
import com.zspirytus.enjoymusic.utils.LogUtil;
import com.zspirytus.enjoymusic.utils.ToastUtil;
import com.zspirytus.enjoymusic.view.activity.BaseActivity;

import java.lang.reflect.Field;

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

public class LazyLoadBaseFragment extends Fragment {

    private boolean isViewCreated = false;
    private boolean isVisibleToUser = false;
    private boolean hasLoaded = false;

    private BaseActivity parentActivity;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        lazyLoad();
    }

    @Override
    public void onAttach(Context context) {
        parentActivity = (BaseActivity) context;
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = autoInjectLayoutId(inflater, container);
        autoInjectAllField(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isViewCreated = true;
        lazyLoad();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    protected void initData() {
    }

    protected void initView() {
    }

    private void lazyLoad() {
        if (isViewCreated && isVisibleToUser && !hasLoaded) {
            initData();
            initView();
            hasLoaded = true;
        }
    }

    private void autoInjectAllField(View view) {
        try {
            Class<?> clazz = this.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(ViewInject.class)) {
                    ViewInject inject = field.getAnnotation(ViewInject.class);
                    int id = inject.value();
                    if (id > 0) {
                        field.setAccessible(true);
                        field.set(this, view.findViewById(id));
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private View autoInjectLayoutId(LayoutInflater inflater, @Nullable ViewGroup container) {
        Class<?> clazz = this.getClass();
        if (clazz.isAnnotationPresent(LayoutIdInject.class)) {
            LayoutIdInject layoutIdInject = clazz.getAnnotation(LayoutIdInject.class);
            int layoutId = layoutIdInject.value();
            return inflater.inflate(layoutId, container, false);
        } else {
            return null;
        }
    }

    public final void e(String message) {
        String TAG = this.getClass().getSimpleName();
        LogUtil.e(TAG, message);
    }

    public final void out(Object message) {
        LogUtil.out(message);
    }

    public final void toast(String message) {
        ToastUtil.showToast(parentActivity, message);
    }

    public BaseActivity getParentActivity() {
        return parentActivity;
    }

}
