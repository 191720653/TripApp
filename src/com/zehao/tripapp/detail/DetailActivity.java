package com.zehao.tripapp.detail;

import java.util.List;

import com.achep.header2actionbar.FadingActionBarHelper;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.BaseSliderView.OnSliderClickListener;
import com.zehao.base.BaseActivity;
import com.zehao.data.bean.Domine;
import com.zehao.data.bean.Employee;
import com.zehao.data.bean.IDataCallback;
import com.zehao.data.bean.MData;
import com.zehao.tripapp.R;
import com.zehao.tripapp.area.AreaActivity;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * < TODO：中山市南区App景区的各个景点介绍界面，显示南区的各个景点的详细介绍 >
 * 
 * @ClassName: DetailActivity
 * @author pc-hao
 * @date 2016年2月23日 下午09:23:14
 * @version V 1.0
 */
public class DetailActivity extends BaseActivity implements
		IDataCallback<MData<? extends Domine>>, OnClickListener, OnSliderClickListener {
	
	private FadingActionBarHelper mFadingActionBarHelper;

	public FadingActionBarHelper getFadingActionBarHelper() {
		return mFadingActionBarHelper;
	}
	
	private ImageButton mineImageButton;
	private int imageButtonY = 0;
	
//	private SliderLayout mDemoSlider;
	
	@Override
	protected void initContentView(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		baseSetContentView(savedInstanceState, R.layout.activity_view_detail);
		
		setActionBarLayout(R.layout.action_bar_like);
		mFadingActionBarHelper = new FadingActionBarHelper(getActionBar(),
				getResources().getDrawable(R.drawable.actionbar_bg));

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new ListViewDetailFragment()).commit();
		}
		
		mineImageButton = (ImageButton) findViewById(R.id.action_bar_btn_mines);
		
//		mDemoSlider = (SliderLayout)findViewById(R.id.slider);
//
//        HashMap<String,String> url_maps = new HashMap<String, String>();
//        url_maps.put("Hannibal", "http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg");
//        url_maps.put("Big Bang Theory", "http://tvfiles.alphacoders.com/100/hdclearart-10.png");
//        url_maps.put("House of Cards House of Cards", "http://cdn3.nflximg.net/images/3093/2043093.jpg");
//        url_maps.put("Game of Thrones", "http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg");
//
//        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
//        file_maps.put("Hannibal",R.drawable.image_nanqu);
//        file_maps.put("Big Bang Theory",R.drawable.image_nanqu);
//        file_maps.put("House of Cards House of Cards",R.drawable.image_nanqu);
//        file_maps.put("Game of Thrones", R.drawable.image_nanqu);
//
//        for(String name : url_maps.keySet()){
//            TextSliderView textSliderView = new TextSliderView(this);
//            // initialize a SliderLayout
//            textSliderView
//                    .description(name)
//                    .image(url_maps.get(name))
//                    .setScaleType(BaseSliderView.ScaleType.Fit)
//                    .setOnSliderClickListener(this);
//
//            //add your extra information
//            textSliderView.getBundle()
//                    .putString("extra",name);
//
//           mDemoSlider.addSlider(textSliderView);
//        }
//        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
//        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
//        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
//        mDemoSlider.setDuration(4000);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.action_bar_btn_like: {
			show("中山市南区点赞");
			break;
		}
		case R.id.action_bar_btn_back:{
			show("返回主界面");
			goActivity(AreaActivity.class);
			break;
			}
		case R.id.action_bar_btn_share:{
			show("中山市南区分享");
			break;
			}
		case R.id.action_bar_btn_mine:
		case R.id.action_bar_btn_mines:{
			show("中山市南区我的信息");
			break;
			}
		default:
			break;
		}
	}
	
	public void show(String contents){
		Toast.makeText(this, contents, Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	protected void loadData() {
		// TODO Auto-generated method stub
		setDataCallback(this);// 设置回调函数
		// 我们还可以把这个Callback传给其它获取数据的类，比如HTTP获取数据
		// 比如 HttClient.get(url,this);
	}

	@Override
	public void onNewData(Object data) {
		// TODO Auto-generated method stub
		// update UI 更新UI数据
		@SuppressWarnings({ "unchecked", "unused" })
		final List<Employee> list = (List<Employee>) ((MData<? extends Domine>) data).dataList;
		handler.post(new Runnable() {
			public void run() {
				// 更新UI
			}
		});
		// 或者
		handler.sendEmptyMessage(0);// 通知Handler
	}

	@Override
	public void onError(String msg, int code) {
		// TODO Auto-generated method stub
		handler.post(new Runnable() {
			public void run() {
				// 通知错误消息
			}
		});
		// 或者
		handler.sendEmptyMessage(0);// 通知Handler
	}

	@Override
	protected void handler(Message msg) { // 我们可以处理数据消息了
		switch (msg.what) {
		case 0:
			break;
		case -1:
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

	}

	@Override
	public void setBaseNoTitle() {
		// TODO Auto-generated method stub
		// 不用系统自带ActionBar
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	@Override
	public void onSliderClick(BaseSliderView slider) {
		// TODO Auto-generated method stub
		Toast.makeText(this,slider.getBundle().get("extra") + "",Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * 设置ActionBar的布局
	 * 
	 * @param layoutId
	 */
	public void setActionBarLayout(int layoutId) {
		ActionBar actionBar = getActionBar();
		if (null != actionBar) {
			// 去掉空白
			actionBar.setTitle("");
			actionBar.setDisplayShowHomeEnabled(false);
			actionBar.setDisplayShowCustomEnabled(true);
			LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View v = inflator.inflate(layoutId, null);
			ActionBar.LayoutParams layout = new ActionBar.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			actionBar.setCustomView(v, layout);
		}
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		if(imageButtonY<=0){
			int[] location = new int[2];
			mineImageButton.getLocationOnScreen(location);
			imageButtonY = location[1];
		}
		if(ev.getY()>=imageButtonY){
			System.out.println("拦截事件......");
			return mineImageButton.dispatchTouchEvent(ev);
		}else
			return super.dispatchTouchEvent(ev);
	}

}
