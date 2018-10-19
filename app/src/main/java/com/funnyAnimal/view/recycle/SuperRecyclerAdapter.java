package com.funnyAnimal.view.recycle;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.funnyAnimal.R;


/**
 * {@link SuperRecyclerView}
 *
 * @author brucewuu Created by brucewuu on 15/11/5.
 */
class SuperRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_FOOTER = Integer.MAX_VALUE;
    private static final int VIEW_TYPE_BODY = Integer.MAX_VALUE - 1;

    private RecyclerView.Adapter mWrapped;
    private int viewState = SuperRecyclerView.STATE_HIDE;
    private LayoutInflater mInflater;

    private SuperRecyclerView.OnReloadListener onReloadListener;

    private
    @DrawableRes
    int noDataDrawableId = 0;
    private String noDataMessage;
    private
    @DrawableRes
    int loadErrorDrawableId = 0;
    private String loadErrorMessage;
    private String loadErrorButton;

    SuperRecyclerAdapter(@NonNull RecyclerView.Adapter mWrapped, Context context) {
        this.mWrapped = mWrapped;
        this.mWrapped.registerAdapterDataObserver(mAdapterDataObserver);
        this.mInflater = LayoutInflater.from(context);
    }

    void updateState(final int state) {
        if (this.viewState == state)
            return;
        this.viewState = state;
        this.noDataDrawableId = 0;
        this.noDataMessage = null;
        this.loadErrorDrawableId = 0;
        this.loadErrorMessage = null;
        this.loadErrorButton = null;
        notifyItemChanged(mWrapped.getItemCount());
    }

    void updateNoData(@DrawableRes int drawableId, String message) {
        this.noDataDrawableId = drawableId;
        this.noDataMessage = message;
        if (this.viewState == SuperRecyclerView.STATE_NO_DATA)
            return;
        this.viewState = SuperRecyclerView.STATE_NO_DATA;
        notifyItemChanged(mWrapped.getItemCount());
    }

    void updateLoadError(@DrawableRes int drawableId, String message, String button) {
        this.loadErrorDrawableId = drawableId;
        this.loadErrorMessage = message;
        this.loadErrorButton = button;
        if (this.viewState == SuperRecyclerView.STATE_LOAD_ERROR)
            return;
        this.viewState = SuperRecyclerView.STATE_LOAD_ERROR;
        notifyItemChanged(mWrapped.getItemCount());
    }

    public void setOnReloadListener(SuperRecyclerView.OnReloadListener listener) {
        this.onReloadListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (VIEW_TYPE_FOOTER == viewType) {
            return createFooterViewHolder(parent);
        } else if (VIEW_TYPE_BODY == viewType) {
            return createBodyViewHolder(parent);
        } else {
            return mWrapped.onCreateViewHolder(parent, viewType);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final int type = getItemViewType(position);
        if (VIEW_TYPE_FOOTER == type) {
            bindFooterViewHolder(holder);
        } else if (VIEW_TYPE_BODY == type) {
            bindBodyViewHolder(holder);
        } else {
            mWrapped.onBindViewHolder(holder, position);
        }
    }

    private RecyclerView.ViewHolder createFooterViewHolder(final ViewGroup parent) {
        return new FooterViewHolder(mInflater.inflate(R.layout.layout_recycler_footer, parent, false));
    }

    private RecyclerView.ViewHolder createBodyViewHolder(final ViewGroup parent) {
        return new BodyViewHolder(mInflater.inflate(R.layout.layout_recycler_body, parent, false));
    }

    private void bindFooterViewHolder(final RecyclerView.ViewHolder holder) {
        final SimpleProgressView view = (SimpleProgressView) holder.itemView;
        switch (viewState) {
            case SuperRecyclerView.STATE_PROGRESS:
                view.showProgress();
                break;
            case SuperRecyclerView.STATE_LOAD_END:
                view.showText();
                break;
            case SuperRecyclerView.STATE_HIDE:
                view.hide();
                break;
            default:
                break;
        }
    }

    private void bindBodyViewHolder(RecyclerView.ViewHolder holder) {
        final BodyViewHolder viewHolder = (BodyViewHolder) holder;
        switch (viewState) {
            case SuperRecyclerView.STATE_NET_ERROR:
                viewHolder.imageView.setImageResource(R.mipmap.ic_net_error);
                viewHolder.textView.setText("没有网络连接，请检查网络设置~");
                viewHolder.button.setVisibility(View.VISIBLE);
                viewHolder.button.setText("去设置");
                viewHolder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Settings.ACTION_SETTINGS);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        v.getContext().startActivity(intent);
                    }
                });
                break;
            case SuperRecyclerView.STATE_LOAD_ERROR:
                viewHolder.itemView.setVisibility(View.VISIBLE);
                if (loadErrorDrawableId == 0)
                    viewHolder.imageView.setImageResource(R.mipmap.ic_net_error);
                else
                    viewHolder.imageView.setImageResource(loadErrorDrawableId);
                if (null == loadErrorMessage)
                    viewHolder.textView.setText("加载失败，请重试~");
                else
                    viewHolder.textView.setText(loadErrorMessage);
                viewHolder.button.setVisibility(View.VISIBLE);
                if (null == loadErrorButton)
                    viewHolder.button.setText("重新加载");
                else
                    viewHolder.button.setText(loadErrorButton);
                if (onReloadListener != null) {
                    viewHolder.button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            viewHolder.itemView.setVisibility(View.GONE);
                            viewState = SuperRecyclerView.STATE_HIDE;
                            notifyDataSetChanged();
                            onReloadListener.reload(v);
                        }
                    });
                }
                break;
            case SuperRecyclerView.STATE_NO_DATA:
                viewHolder.itemView.setVisibility(View.VISIBLE);
                if (noDataDrawableId == 0)
                    viewHolder.imageView.setImageResource(R.mipmap.icon_no_data);
                else
                    viewHolder.imageView.setImageResource(noDataDrawableId);
                if (null == noDataMessage)
                    viewHolder.textView.setText("暂无数据~");
                else
                    viewHolder.textView.setText(noDataMessage);
                viewHolder.button.setVisibility(View.INVISIBLE);
                viewHolder.button.setOnClickListener(null);
                break;
            case SuperRecyclerView.STATE_HIDE:
                viewHolder.imageView.setImageDrawable(null);
                holder.itemView.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    private static class FooterViewHolder extends RecyclerView.ViewHolder {
        FooterViewHolder(final View itemView) {
            super(itemView);
        }
    }

    private static class BodyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        Button button;

        BodyViewHolder(final View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.iv_load_state);
            this.textView = (TextView) itemView.findViewById(R.id.tv_load_state);
            this.button = (Button) itemView.findViewById(R.id.btn_do_next);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mWrapped.getItemCount()) {
            if (SuperRecyclerView.STATE_NO_DATA == viewState ||
                    SuperRecyclerView.STATE_NET_ERROR == viewState ||
                    SuperRecyclerView.STATE_LOAD_ERROR == viewState) {
                return VIEW_TYPE_BODY;
            } else if (SuperRecyclerView.STATE_PROGRESS == viewState ||
                    SuperRecyclerView.STATE_LOAD_END == viewState) {
                return VIEW_TYPE_FOOTER;
            } else {
                return mWrapped.getItemViewType(position);
            }
        } else {
            return mWrapped.getItemViewType(position);
        }
    }

    public int getCount() {
        return mWrapped.getItemCount();
    }

    @Override
    public int getItemCount() {
        if (SuperRecyclerView.STATE_NO_DATA == viewState ||
                SuperRecyclerView.STATE_NET_ERROR == viewState ||
                SuperRecyclerView.STATE_LOAD_ERROR == viewState ||
                SuperRecyclerView.STATE_PROGRESS == viewState ||
                SuperRecyclerView.STATE_LOAD_END == viewState)
            return mWrapped.getItemCount() + 1;
        else
            return mWrapped.getItemCount();
    }

    @Override
    public long getItemId(int position) {
        final int type = getItemViewType(position);
        if (VIEW_TYPE_FOOTER == type || VIEW_TYPE_BODY == type)
            return position;
        else
            return mWrapped.getItemId(position);
    }

    private boolean isFullSpanType(int type) {
        return type == VIEW_TYPE_BODY || type == VIEW_TYPE_FOOTER;
    }

    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mWrapped.onAttachedToRecyclerView(recyclerView);
        //Log.e("------:", "onAttachedToRecyclerView");
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    SuperRecyclerAdapter adapter = (SuperRecyclerAdapter) recyclerView.getAdapter();
                    if (isFullSpanType(adapter.getItemViewType(position))) {
                        return gridLayoutManager.getSpanCount();
                    }
                    return 1;
                }
            });
        }
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        mWrapped.onDetachedFromRecyclerView(recyclerView);
        mWrapped.unregisterAdapterDataObserver(mAdapterDataObserver);
        //Log.e("------:", "onDetachedFromRecyclerView");
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        //super.setHasStableIds(hasStableIds);
        mWrapped.setHasStableIds(hasStableIds);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        mWrapped.onViewRecycled(holder);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean onFailedToRecycleView(RecyclerView.ViewHolder holder) {
        mWrapped.onFailedToRecycleView(holder);
        return super.onFailedToRecycleView(holder);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        mWrapped.onViewAttachedToWindow(holder);
        int position = holder.getLayoutPosition();
        int type = getItemViewType(position);
        if (isFullSpanType(type)) {
            ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
            if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams lp = (StaggeredGridLayoutManager.LayoutParams) layoutParams;
                lp.setFullSpan(true);
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        mWrapped.onViewDetachedFromWindow(holder);
    }

    private final RecyclerView.AdapterDataObserver mAdapterDataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onItemRangeRemoved(final int positionStart, final int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(final int fromPosition, final int toPosition, final int itemCount) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
            notifyItemRangeChanged(fromPosition, itemCount);
        }

        @Override
        public void onItemRangeInserted(final int positionStart, final int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(final int positionStart, final int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
            notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onChanged() {
            super.onChanged();
            notifyDataSetChanged();
        }
    };
}
