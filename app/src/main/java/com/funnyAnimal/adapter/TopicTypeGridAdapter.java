package com.funnyAnimal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;

import com.funnyAnimal.R;
import com.funnyAnimal.utils.ImageLoadFactory;
import com.funnyAnimal.utils.UiHelper;
import com.funnyAnimal.view.MLImageView;

import java.util.List;

/**
 * Created by 青峰 on 2017/9/17.
 */

public class TopicTypeGridAdapter extends BaseAdapter {

    private Context context;
    private List<String> items;

    public TopicTypeGridAdapter(Context context, List<String> items) {
        super();
        this.context = context;
        this.items = items;
    }

    public void update(List<String> newItems){
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_topic_grid, null);
            holder = new ViewHolder();

            holder.item_img = (MLImageView) convertView.findViewById(R.id.topic_pic);
            ViewGroup.LayoutParams params = holder.item_img.getLayoutParams();
            int screenWidth = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
            params.height = (screenWidth - UiHelper.dp2px(context, 96)) / 3;
            holder.item_img.setLayoutParams(params);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        ImageLoadFactory.display2(context, items.get(position).replace("https", "http"), holder.item_img);
        return convertView;
    }

    public class ViewHolder {
        public MLImageView item_img;
    }
}
