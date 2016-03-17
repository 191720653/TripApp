package com.zehao.tripapp.advice;

import com.zehao.tripapp.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class AdviceListAdapter extends BaseAdapter {
	
	private LayoutInflater listContainer;
	
	public final class ListItemView { // 自定义控件集合
		public ImageView image;
		public TextView title;
	}
	
	public String[] title = {"孝道文化游","一日游","亲子一日游","节假游"};
	
	public AdviceListAdapter(Context context){
		listContainer = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return title.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ListItemView listItemView = null;
		if (convertView == null) {
			listItemView = new ListItemView();
			// 获取list_item布局文件的视图
			convertView = listContainer.inflate(R.layout.item_list_view_advice, null);
			// 获取控件对象
			listItemView.image = (ImageView) convertView.findViewById(R.id.item_advice_image);
			listItemView.title = (TextView) convertView.findViewById(R.id.item_advice_title);
			// 设置控件集到convertView
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}
		System.out.println("设置文字和图片");

		// 设置文字和图片
		listItemView.title.setText(title[position]);
		listItemView.image.setImageResource(R.drawable.image_nanqu);
		
		return convertView;
	}

}
