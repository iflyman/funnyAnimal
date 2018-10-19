package com.funnyAnimal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.funnyAnimal.R;

import java.util.List;

public class GetProviceAdapter extends BaseAdapter {
	private Context context;
	private List<String> list;
	private TextView tv_provice;
	private String provice;

	public GetProviceAdapter(Context context, List<String> list) {
		super();
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = LayoutInflater.from(context).inflate(R.layout.item_provie, null);
		tv_provice = (TextView) convertView.findViewById(R.id.tv_provice);
		provice = list.get(position);
		tv_provice.setText(provice);
		return convertView;
	}
}
