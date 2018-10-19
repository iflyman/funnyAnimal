package com.funnyAnimal.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * 取消viewpager预加载
 *
 * @author brucewuu Created on 16/6/21.
 */
public abstract class DelayLoadFragment extends BaseFragment {
    /**
     * Fragment当前状态是否可见
     */
    protected boolean isVisible;
    /**
     * View是否已创建
     **/
    protected boolean isViewCreated;
    /**
     * 是否已被加载过一次，第二次就不再去请求数据了
     **/
    protected boolean mHasLoadedOnce;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = isVisibleToUser;
        //L.ee(TAG + isVisibleToUser);
        if (isVisible) {
            if (isViewCreated) {
                lazyLoad();
            }
        } else {
            onInvisible();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //L.ee(TAG + "--onActivityCreated-");
        isViewCreated = true;
        if (isVisible) {
            lazyLoad();
        }
    }

    @Override
    public void onDestroyView() {
        isViewCreated = false;
        super.onDestroyView();
    }

    /**
     * 不可见
     */
    protected void onInvisible() {
    }

    /**
     * 延迟加载
     * 子类必须重写此方法
     */
    protected abstract void lazyLoad();
}
