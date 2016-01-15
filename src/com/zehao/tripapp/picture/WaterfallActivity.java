package com.zehao.tripapp.picture;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Message;
import android.view.Window;

import com.huewu.pla.lib.MultiColumnPullToRefreshListView;
import com.huewu.pla.lib.MultiColumnPullToRefreshListView.OnRefreshListener;
import com.zehao.base.BaseActivity;
import com.zehao.constant.CONSTANT;
import com.zehao.tripapp.R;

public class WaterfallActivity extends BaseActivity {
	
	private MultiColumnPullToRefreshListView waterfallView;//可以把它当成一个listView
	//如果不想用下拉刷新这个特性，只是瀑布流，可以用这个：MultiColumnListView 一样的用法

	@Override
	protected void initContentView(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		addActionBar();
		baseSetContentView(savedInstanceState, R.layout.activity_waterfall_image_view);
		addLeftMenu(Boolean.TRUE);
		
		waterfallView = (MultiColumnPullToRefreshListView) findViewById(R.id.list);
		
		ArrayList<String> imageList = new ArrayList<String>();
//		imageList.add("http://www.yjz9.com/uploadfile/2012/1219/20121219043531502.jpg");
//		imageList.add("http://www.yjz9.com/uploadfile/2012/1219/20121219043532264.jpg");
//		imageList.add("http://www.yjz9.com/uploadfile/2012/1219/20121219043533581.jpg");
//		imageList.add("http://www.yjz9.com/uploadfile/2012/1219/20121219043533571.jpg");
//		imageList.add("http://www.yjz9.com/uploadfile/2012/1219/20121219043534672.jpg");
//		imageList.add("http://www.yjz9.com/uploadfile/2012/1219/20121219043534854.jpg");
//		imageList.add("http://www.yjz9.com/uploadfile/2012/1219/20121219043535929.jpg");
//		imageList.add("http://www.yjz9.com/uploadfile/2012/1219/20121219043535784.jpg");
//		imageList.add("http://www.yjz9.com/uploadfile/2012/1219/20121219043536626.jpg");
//		imageList.add("http://www.yjz9.com/uploadfile/2012/1219/20121219043536244.jpg");
		for(int i=0;i<CONSTANT.IMAGES.length;i++){
			imageList.add(CONSTANT.IMAGES[i]);
		}
		
		WaterfallAdapter adapter = new WaterfallAdapter(imageList, this);
		waterfallView.setAdapter(adapter);
		waterfallView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				//下拉刷新要做的事
				
				//刷新完成后记得调用这个
				waterfallView.onRefreshComplete();
			}
		});
		
	}

	@Override
	public void setBaseNoTitle() {
		// TODO Auto-generated method stub
		// 不用系统自带ActionBar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	@Override
	protected void handler(Message msg) {
		// TODO Auto-generated method stub
	}

}
