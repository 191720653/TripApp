package com.zehao.tripapp.all;

import java.util.List;
import java.util.Map;
import com.zehao.tripapp.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListViewDetailAdapter extends BaseAdapter {
	private List<Map<String, Object>> listItems; // 信息集合
	private LayoutInflater listContainer; // 视图容器

	public final class ListItemView { // 自定义控件集合
		public TextView title;
		public TextView content;
	}

	public ListViewDetailAdapter(Context context, List<Map<String, Object>> listItems) {
		listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
		this.listItems = listItems;
		System.out.println("创建视图容器并设置上下文");
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return listItems.size();
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * ListView Item设置
	 */
	@SuppressLint("InflateParams")
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		// 自定义视图
		ListItemView listItemView = null;
		if (convertView == null) {
			listItemView = new ListItemView();
			// 获取list_item布局文件的视图
			convertView = listContainer.inflate(R.layout.item_list_view_detail, null);
			// 获取控件对象
			listItemView.title = (TextView) convertView
					.findViewById(R.id.item_view_title);
			listItemView.content = (TextView) convertView
					.findViewById(R.id.item_view_content);
			// 设置控件集到convertView
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}

		// 设置文字
		listItemView.title.setText((String) listItems.get(position).get("title"));
		listItemView.content.setText((String) listItems.get(position).get("content"));
		
		return convertView;
	}

}