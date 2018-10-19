package com.funnyAnimal.view.recycle;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
/**
 * base RecyclerView.Adapter
 *
 * @author brucewuu Created on 16/3/29.
 */
public abstract class RecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerViewHolder> implements DataIO<T> {

    private final ArrayList<T> items;
    private final int layoutResId;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;

    public RecyclerAdapter(Context context, int layoutResId) {
        this.items = new ArrayList<>();
        this.layoutResId = layoutResId;
        this.mInflater = LayoutInflater.from(context);
    }

    public RecyclerAdapter(Context context, int layoutResId, List<T> items) {
        this.items = new ArrayList<>(items);
        this.layoutResId = layoutResId;
        this.mInflater = LayoutInflater.from(context);
    }

    public RecyclerAdapter(Context context, int layoutResId, T[] items) {
        this.items = new ArrayList<>(Arrays.asList(items));
        this.layoutResId = layoutResId;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final RecyclerViewHolder holder = new RecyclerViewHolder(mInflater.inflate(layoutResId, parent, false));
        if (null != mOnItemClickListener) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(v, holder.getAdapterPosition());
                }
            });
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        bindData(holder, items.get(position), position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void add(T item) {
        final int position = items.size();
        items.add(item);
        notifyItemInserted(position);
    }

    @Override
    public void addAll(Collection<T> collection) {
        final int positionStart = items.size();
        items.addAll(collection);
        notifyItemRangeInserted(positionStart, collection.size());
    }

    @Override
    public void addAll(@NonNull T[] t) {
        final int positionStart = items.size();
        Collections.addAll(items, t);
        notifyItemRangeInserted(positionStart, t.length);
    }

    @Override
    public void insertAll(Collection<T> collection) {
        items.addAll(0, collection);
        notifyItemRangeInserted(0, collection.size());
    }

    public void insert(T item) {
        items.add(0,item);
        notifyItemInserted(0);
    }

    @Override
    public void remove(T item) {
        final int position = items.indexOf(item);
        if (-1 == position)
            return;
        items.remove(item);
        notifyItemRemoved(position);
    }

    @Override
    public void remove(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void clear() {
        if (items.isEmpty())
            return;
        items.clear();
        notifyDataSetChanged();
    }

    @Override
    public T getItem(int position) {
        return items.get(position);
    }

    @Override
    public ArrayList<T> getAll() {
        return items;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    protected abstract void bindData(RecyclerViewHolder holder, T t, int position);

    public RecyclerAdapter setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
        return this;
    }
}
