package com.zehao.tripapp.point;

import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.zehao.constant.CONSTANT;
import com.zehao.data.bean.Views;
import com.zehao.tripapp.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridViewAdapter extends BaseAdapter {
	private List<Views> listItems; // 信息集合
	private LayoutInflater listContainer; // 视图容器
	private DisplayImageOptions options;

	public final class GridItemView { // 自定义控件集合
		public ImageView image;
		public TextView title;
		public TextView likeNum;
	}

	public GridViewAdapter(Context context, List<Views> listItems) {
		listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
		this.listItems = listItems;
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.load_default)
		.showImageForEmptyUri(R.drawable.load_default)
		.showImageOnFail(R.drawable.load_default)
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
		return listItems.get(arg0);
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
		Log.e("method", "getView");
		// 自定义视图
		GridItemView listItemView = null;
		if (convertView == null) {
			listItemView = new GridItemView();
			// 获取list_item布局文件的视图
			convertView = listContainer.inflate(R.layout.item_grid_view, null);
			// 获取控件对象
			listItemView.image = (ImageView) convertView
					.findViewById(R.id.item_main_image);
			listItemView.title = (TextView) convertView
					.findViewById(R.id.item_main_title);
			listItemView.likeNum = (TextView) convertView
					.findViewById(R.id.item_like_num);
			// 设置控件集到convertView
			convertView.setTag(listItemView);
		} else {
			listItemView = (GridItemView) convertView.getTag();
		}

		// 设置文字和图片
		ImageLoader.getInstance().displayImage(CONSTANT.BASE_ROOT_URL + listItems.get(position).getViewLogo().replaceFirst(".", ""), listItemView.image, options);
		// listItemView.image.setImageResource(Integer.parseInt(listItems.get(position).get("image").toString()));
		listItemView.title.setText((String) listItems.get(position).getViewName());
		listItemView.likeNum.setText(listItems.get(position).getLikeNum() + "");
		
		return convertView;
	}

}