package com.zehao.tripapp.advice;

import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.zehao.constant.CONSTANT;
import com.zehao.data.bean.TripType;
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

@SuppressLint("InflateParams")
public class AdviceListAdapter extends BaseAdapter {
	
	private LayoutInflater listContainer;
	
	private List<TripType> tripTypes;
	
	private DisplayImageOptions options;
	
	public final class ListItemView { // 自定义控件集合
		public ImageView image;
		public TextView title;
	}
	
	public AdviceListAdapter(Context context, List<TripType> tripTypes){
		listContainer = LayoutInflater.from(context);
		this.tripTypes = tripTypes;
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

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return tripTypes.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return tripTypes.get(position);
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
		listItemView.title.setText(tripTypes.get(position).getTypeTitle());
		ImageLoader.getInstance().displayImage(CONSTANT.BASE_ROOT_URL + tripTypes.get(position).getTypeLogo().replaceFirst(".", ""), listItemView.image, options);
		
		return convertView;
	}

}
