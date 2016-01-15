package com.zehao.tripapp.welcome;

import java.util.ArrayList;

import com.zehao.base.BaseActivity;
import com.zehao.tripapp.MainActivity;
import com.zehao.tripapp.R;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

public class GuideActivity extends BaseActivity {
	private ViewPager viewPager;

	/** 装分页显示的view的数组 */
	private ArrayList<View> pageViews;
	private ImageView imageView;

	/** 将小圆点的图片用数组表示 */
	private ImageView[] imageViews;

	// 包裹滑动图片的LinearLayout
	private ViewGroup viewPics;

	// 包裹小圆点的LinearLayout
	private ViewGroup viewPoints;

	@Override
	protected void initContentView(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		// 将要分页显示的View装入数组中
		LayoutInflater inflater = getLayoutInflater();
		pageViews = new ArrayList<View>();
		pageViews.add(inflater.inflate(R.layout.viewpager_welcome_begin, null));
		pageViews.add(inflater.inflate(R.layout.viewpager_welcome_then, null));
		pageViews.add(inflater.inflate(R.layout.viewpager_welcome_end, null));

		// 创建imageviews数组，大小是要显示的图片的数量
		imageViews = new ImageView[pageViews.size()];
		// 从指定的XML文件加载视图
		viewPics = (ViewGroup) inflater.inflate(
				R.layout.activity_welcome_guide, null);

		// 实例化小圆点的linearLayout和viewpager
		viewPoints = (ViewGroup) viewPics.findViewById(R.id.viewGroup);
		viewPager = (ViewPager) viewPics.findViewById(R.id.guidePages);

		// 添加小圆点的图片
		for (int i = 0; i < pageViews.size(); i++) {
			imageView = new ImageView(GuideActivity.this);
			// 设置小圆点imageview的参数
			LayoutParams layoutParams = new LayoutParams(18, 18);
			layoutParams.setMargins(5, 0, 5, 0);
			imageView.setLayoutParams(layoutParams);// 创建一个宽高均为20 的布局
			// 将小圆点layout添加到数组中
			imageViews[i] = imageView;

			// 默认选中的是第一张图片，此时第一个小圆点是选中状态，其他不是
			if (i == 0) {
				imageViews[i]
						.setBackgroundResource(R.drawable.page_indicator_focused);
			} else {
				imageViews[i].setBackgroundResource(R.drawable.page_indicator);
			}
			// 将imageviews添加到小圆点视图组
			viewPoints.addView(imageViews[i]);
		}

		// 显示滑动图片的视图
		baseSetContentView(savedInstanceState, viewPics);

		// 设置viewpager的适配器和监听事件
		viewPager.setAdapter(new GuidePageAdapter());
		viewPager.setOnPageChangeListener(new GuidePageChangeListener());
	}

	private Button.OnClickListener Button_OnClickListener = new Button.OnClickListener() {
		public void onClick(View v) {
			// 设置已经引导
			writeXML(SHAREDPREFERENCES_NAME, KEY_GUIDE_ACTIVITY, "true");// "false"

			// 跳转
			Intent mIntent = new Intent();
			mIntent.setClass(GuideActivity.this, MainActivity.class);
			GuideActivity.this.startActivity(mIntent);
			GuideActivity.this.finish();
		}
	};

	private static final String SHAREDPREFERENCES_NAME = "my_pref";
	private static final String KEY_GUIDE_ACTIVITY = "guide_activity";

	class GuidePageAdapter extends PagerAdapter {

		// 销毁position位置的界面
		@Override
		public void destroyItem(View v, int position, Object arg2) {
			// TODO Auto-generated method stub
			((ViewPager) v).removeView(pageViews.get(position));
		}

		@Override
		public void finishUpdate(View arg0) {
			// TODO Auto-generated method stub

		}

		// 获取当前窗体界面数
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return pageViews.size();
		}

		// 初始化position位置的界面
		@Override
		public Object instantiateItem(View v, int position) {
			// TODO Auto-generated method stub
			((ViewPager) v).addView(pageViews.get(position));

			// 测试页卡2内的按钮事件
			if (position == 2) {
				Button action = (Button) v.findViewById(R.id.btn_close_guide);
				action.setOnClickListener(Button_OnClickListener);
			}

			return pageViews.get(position);
		}

		// 判断是否由对象生成界面
		@Override
		public boolean isViewFromObject(View v, Object arg1) {
			// TODO Auto-generated method stub
			return v == arg1;
		}

		@Override
		public void startUpdate(View arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public int getItemPosition(Object object) {
			// TODO Auto-generated method stub
			return super.getItemPosition(object);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public Parcelable saveState() {
			// TODO Auto-generated method stub
			return null;
		}
	}

	class GuidePageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageSelected(int position) {
			// TODO Auto-generated method stub
			for (int i = 0; i < imageViews.length; i++) {
				imageViews[position]
						.setBackgroundResource(R.drawable.page_indicator_focused);
				// 不是当前选中的page，其小圆点设置为未选中的状态
				if (position != i) {
					imageViews[i]
							.setBackgroundResource(R.drawable.page_indicator);
				}
				// AnimationSet animationSet = new AnimationSet(true);
				// TranslateAnimation swing = new TranslateAnimation(
				// -6f, 6f, 0, 0);
				// swing.setDuration(500);
				// swing.setRepeatCount(Animation.INFINITE);
				// swing.setRepeatMode(Animation.REVERSE);
				// swing.setRepeatCount(2);
				// animationSet.addAnimation(swing);
				// imageViews[position].startAnimation(animationSet);
			}
		}
	}

	@Override
	protected void handler(Message msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setBaseNoTitle() {
		// TODO Auto-generated method stub
		// 不用系统自带ActionBar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

}