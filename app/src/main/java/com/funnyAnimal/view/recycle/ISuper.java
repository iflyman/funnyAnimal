package com.funnyAnimal.view.recycle;

import android.support.annotation.DrawableRes;

/**
 * {@link SuperRecyclerView} interface
 *
 * @author brucewuu Created by brucewuu on 16/4/8.
 */
interface ISuper {
    void showProgress();

    void hideProgress();

    void showLoadEnd();

    void showNetError();

    void showLoadError();

    void showLoadError(@DrawableRes int drawableId, String message);

    void showLoadError(@DrawableRes int drawableId, String message, String button);

    void showNoData();

    void showNoData(@DrawableRes int drawableId, String message);

    void showHide();
}
