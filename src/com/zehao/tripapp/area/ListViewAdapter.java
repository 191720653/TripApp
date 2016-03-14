package com.zehao.tripapp.area;

import java.util.List;
import java.util.Map;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.zehao.tripapp.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("ResourceAsColor")
public class ListViewAdapter extends BaseAdapter {
	private List<Map<String, Object>> listItems; // 信息集合
	private LayoutInflater listContainer; // 视图容器
	private DisplayImageOptions options;

	public final class ListItemView { // 自定义控件集合
		public ImageView image;
		public TextView title;
		public TextView likeNum;
		public TextView info;
	}

	public ListViewAdapter(Context context, List<Map<String, Object>> listItems) {
		listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
		this.listItems = listItems;
		System.out.println("创建视图容器并设置上下文");
		options = new DisplayImageOptions.Builder()
		.showImageForEmptyUri(R.drawable.ic_empty)
		.showImageOnFail(R.drawable.ic_error)
		.resetViewBeforeLoading(true)
		.cacheOnDisk(true)
		.cacheInMemory(true)
		.imageScaleType(ImageScaleType.EXACTLY)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.displayer(new FadeInBitmapDisplayer(50))
		.build();
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
		System.out.println("getview");
		// 自定义视图
		ListItemView listItemView = null;
		if (convertView == null) {
			listItemView = new ListItemView();
			// 获取list_item布局文件的视图
			convertView = listContainer.inflate(R.layout.item_list_view, null);
			// 获取控件对象
			listItemView.image = (ImageView) convertView
					.findViewById(R.id.item_main_image);
			listItemView.title = (TextView) convertView
					.findViewById(R.id.item_main_title);
			listItemView.info = (TextView) convertView
					.findViewById(R.id.item_main_info);
			listItemView.likeNum = (TextView) convertView
					.findViewById(R.id.item_like_num);
			// 设置控件集到convertView
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}
		System.out.println("设置文字和图片");

		// 设置文字和图片
		ImageLoader.getInstance().displayImage(listItems.get(position).get("image").toString(), listItemView.image, options);
		listItemView.title.setText((String) listItems.get(position).get("title"));
		listItemView.info.setText((String) listItems.get(position).get("info"));
		listItemView.likeNum.setText(listItems.get(position).get("likeNum").toString());
		
		return convertView;
	}

}