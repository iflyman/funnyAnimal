package com.funnyAnimal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.funnyAnimal.BuildConfig;
import com.funnyAnimal.R;

import java.util.List;

/**
 * Created by 青峰 on 2017/9/9.
 */

public class WrapperAdapter extends BaseAdapter {

    private Context context;
    private List<String> items;

    public WrapperAdapter(Context context, List<String> items) {
        super();
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (null == convertView) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_wrapper, null);
            holder = new ViewHolder();
            holder.item_img = (ImageView) convertView.findViewById(R.id.wrapper_img);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.item_img.setBackgroundResource(context.getResources().getIdentifier(items.get(position),"mipmap", BuildConfig.APPLICATION_ID));
        return convertView;
    }

    public class ViewHolder {
        public ImageView item_img;
    }
}
