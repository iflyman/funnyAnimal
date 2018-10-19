package com.funnyAnimal.view.swipeback;

/**
 * Created by brucewuu on 2014/7/28.
 */
public interface SwipeBackDao {
    /**
     * @return the SwipeBackLayout associated with this activity.
     */
    SwipeBackLayout getSwipeBackLayout();
    /**
     * set enable swipe back or not.
     * @param enable
     */
    void setSwipeBackEnable(boolean enable);
    /**
     * Scroll out contentView and finish the activity
     */
    void scrollToFinishActivity();
}
