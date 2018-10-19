package com.funnyAnimal.view.recycle;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;


/**
 * 超级RecyclerView,可实现以下功能
 * 1、滑动到底部自动加载更多
 * 2、可显示数据加载失败提示，网络错误提示，没有数据提示
 * 3、相应的可以点击重新加载，打开网络设置
 *
 * @author brucewuu
 */
public class SuperRecyclerView extends RecyclerView implements ISuper {
    /**
     * 默认滑动到底部还有几个item时加载
     */
    private static final int DEFAULT_THRESHOLD = 3;
    /**
     * 显示状态码
     */
    public static final int STATE_HIDE = 0; // 全部隐藏
    public static final int STATE_PROGRESS = 1; // 显示加载进度
    public static final int STATE_LOAD_END = 2; // 全部加载完毕
    public static final int STATE_NET_ERROR = 3; // 没有网络
    public static final int STATE_LOAD_ERROR = 4; // 加载错误
    public static final int STATE_NO_DATA = 5; // 没有数据
    /**
     * 滑动到底部自动加载更多
     */
    public static final int MODE_AUTO = 0;
    /**
     * 不自动加载
     */
    public static final int MODE_NONE = 2;
    /**
     * 当前模式（自动加载）
     */
    private int mode = MODE_AUTO;
    /**
     * 滑动到底部还有几个item时加载
     */
    private int threshold = DEFAULT_THRESHOLD;

    private SuperRecyclerAdapter mAdapter;
    private OnLoadMoreListener mLoadMoreListener;
    private volatile boolean mLoading;

    /**
     * 加载更多监听
     */
    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
    }

    /**
     * 重新加载监听
     *
     * @author brucewuu Created on 15/11/6.
     */
    public interface OnReloadListener {
        void reload(View view);
    }

    private final OnScrollListener mScrollListener = new OnScrollListener() {
        @Override
        public void onScrollStateChanged(final RecyclerView recyclerView, final int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (mLoading || mode != MODE_AUTO) {
                return;
            }
            int visibleItemCount = recyclerView.getChildCount();
            int totalItemCount = RecyclerViewHelper.getItemCount(recyclerView);
            int firstVisibleItem = RecyclerViewHelper.findFirstVisibleItemPosition(recyclerView);
            if (totalItemCount > 3 && (totalItemCount - visibleItemCount) <= (firstVisibleItem + threshold)) {
                showProgress();
                if (mLoadMoreListener != null) {
                    mLoadMoreListener.onLoadMore();
                }
            }
        }
    };

    public SuperRecyclerView(final Context context) {
        super(context);
    }

    public SuperRecyclerView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public SuperRecyclerView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setLayoutManager(final LayoutManager layout) {
        super.setLayoutManager(layout);
        addOnScrollListener(mScrollListener);
    }

    @Override
    public void setAdapter(final Adapter adapter) {
        if (adapter == null) {
            mAdapter = null;
            super.setAdapter(null);
        } else {
            mAdapter = new SuperRecyclerAdapter(adapter, getContext());
            super.setAdapter(mAdapter);
        }
    }

    private void setMode(final int mode) {
        if (mode < MODE_AUTO || mode > MODE_NONE) {
            throw new IllegalArgumentException("mode must between " + MODE_AUTO
                    + " and " + MODE_NONE);
        }
        this.mode = mode;
    }

    public void enable(boolean enable) {
        setMode(enable ? MODE_AUTO : MODE_NONE);
    }

    @Override
    public void showProgress() {
        if (mode != MODE_NONE && !mLoading) {
            mLoading = true;
            mAdapter.updateState(STATE_PROGRESS);
        }
    }

    @Override
    public void hideProgress() {
        if (mode != MODE_NONE && mLoading) {
            mLoading = false;
            mAdapter.updateState(STATE_HIDE);
        }
    }

    @Override
    public void showLoadEnd() {
        mLoading = false;
        setMode(MODE_NONE);
        mAdapter.updateState(STATE_LOAD_END);
    }

    @Override
    public void showNetError() {
        mLoading = false;
        mAdapter.updateState(STATE_NET_ERROR);
    }

    @Override
    public void showLoadError() {
        mLoading = false;
        if (mAdapter.getCount() <= 1)
            mAdapter.updateState(STATE_LOAD_ERROR);
        else
            hideProgress();
    }

    @Override
    public void showLoadError(@DrawableRes int drawableId, String message) {
        mLoading = false;
        if (mAdapter.getCount() <= 1)
            mAdapter.updateLoadError(drawableId, message, null);
        else
            hideProgress();
    }

    @Override
    public void showLoadError(@DrawableRes int drawableId, String message, String button) {
        mLoading = false;
        if (mAdapter.getCount() <= 1)
            mAdapter.updateLoadError(drawableId, message, button);
        else
            hideProgress();
    }

    @Override
    public void showNoData() {
        mLoading = false;
        if (mAdapter.getCount() <= 1)
            mAdapter.updateState(STATE_NO_DATA);
    }

    @Override
    public void showNoData(@DrawableRes int drawableId, String message) {
        mLoading = false;
        if (mAdapter.getCount() <= 1)
            mAdapter.updateNoData(drawableId, message);
    }

    @Override
    public void showHide() {
        mLoading = false;
        mAdapter.updateState(STATE_HIDE);
    }

    public void setOnLoadMoreListener(final OnLoadMoreListener listener) {
        this.mLoadMoreListener = listener;
    }

    /**
     * 必须在{@link #setAdapter(Adapter)}之后调用
     *
     * @param listener
     */
    public void setOnReloadListener(OnReloadListener listener) {
        if (null == mAdapter)
            throw new IllegalArgumentException("must be setAdapter first");

        mAdapter.setOnReloadListener(listener);
    }

    public void setLoadMoreThreshold(final int threshold) {
        this.threshold = threshold;
    }

    @Override
    protected void onDetachedFromWindow() {
        clearOnScrollListeners();
        mLoadMoreListener = null;
        super.onDetachedFromWindow();
    }
}