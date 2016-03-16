package com.zehao.tripapp.point;

import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.BaseSliderView.OnSliderClickListener;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zehao.base.BaseActivity;
import com.zehao.constant.CONSTANT;
import com.zehao.data.bean.Domine;
import com.zehao.data.bean.Employee;
import com.zehao.data.bean.IDataCallback;
import com.zehao.data.bean.MData;
import com.zehao.data.bean.Views;
import com.zehao.http.HttpCLient;
import com.zehao.tripapp.R;
import com.zehao.tripapp.detail.DetailActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

/**
 * < TODO：中山市南区App景点(带不同区域)的详情介绍界面，显示南区的各个景点(带不同区域) 的详细介绍 >
 * 
 * @ClassName: PointActivity
 * @author pc-hao
 * @date 2016年2月24日 下午14:23:14
 * @version V 1.0
 */
public class CopyOfPointActivity extends BaseActivity implements
		IDataCallback<MData<? extends Domine>>, OnClickListener, OnSliderClickListener {

	private SliderLayout mDemoSlider;
	
	private GridView gridView;
	private GridViewAdapter gridViewAdapter;
	
	private TextView viewName, viewInfo;

	@Override
	protected void initContentView(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		baseSetContentView(savedInstanceState, R.layout.activity_view_point);
		
		gridView = (GridView) findViewById(R.id.view_point_gridview);
		viewInfo = (TextView) findViewById(R.id.view_point_info);
		viewName = (TextView) findViewById(R.id.view_point_name);
		
		mDemoSlider = (SliderLayout)findViewById(R.id.slider);
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Stack);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        
        final Views view = (Views) getIntent().getSerializableExtra(CONSTANT.VIEW_INFO);
        
        new Handler().post(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String url = "/AppView_getViewListAction.action";
				JsonObject json = new JsonObject();
				json.addProperty(CONSTANT.APP_GET_DATA, CONSTANT.VIEW_LIST);
				json.addProperty(CONSTANT.VIEW_SIGN, view.getChildSign());
				json.addProperty(CONSTANT.VIEW_ID, view.getViewId());
				RequestParams params = new RequestParams();
				params.add(CONSTANT.DATA, json.toString());
				System.out.println("App发送数据：" + json.toString());
				HttpCLient.post(url, params, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						// TODO Auto-generated method stub
						JsonObject json = (JsonObject) new JsonParser().parse(new String(arg2));
						System.out.println("服务器返回数据：" + json);
						String errorCode = json.get(CONSTANT.ERRCODE).getAsString();
						if (CONSTANT.CODE_168.equals(errorCode)) {
							Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
							List<String> urls = gson.fromJson(json.get(CONSTANT.VIEW_PICTURE)
									.getAsJsonArray(),new TypeToken<List<String>>() {}.getType());
							List<Views> views = gson.fromJson(json.get(CONSTANT.VIEW_CHILD)
									.getAsJsonArray(),new TypeToken<List<Views>>() {}.getType());
							setGridViewTitles(urls, views);
						} else {
							shortToastHandler(CONSTANT.OTHER_ERROR);
						}
					}
					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						// TODO Auto-generated method stub
						shortToastHandler(CONSTANT.OTHER_ERROR);
					}
				});
			}
		});
        viewInfo.setText(view.getViewInfo());
        viewName.setText(view.getViewName());
	}
	
	private void setGridViewTitles(List<String> sList, final List<Views> vList) {
		
		System.out.println("setListViewTitles");

		HashMap<String, String> url_maps = new HashMap<String, String>();
		for (int i = 0; i < sList.size(); i++) {
			url_maps.put("" + i, CONSTANT.BASE_ROOT_URL
					+ sList.get(i).replaceFirst(".", ""));
		}
		for (String name : url_maps.keySet()) {
			TextSliderView textSliderView = new TextSliderView(this);
			// initialize a SliderLayout
			textSliderView.description("").image(url_maps.get(name))
					.setScaleType(BaseSliderView.ScaleType.Fit)
					.setOnSliderClickListener(this);
			// add your extra information
			textSliderView.getBundle().putString("extra", name);
			mDemoSlider.addSlider(textSliderView);
		}
		System.out.println(url_maps);

//		new Handler().postDelayed(new Runnable() {
//			@Override
//			public void run() {
				// TODO Auto-generated method stub
				gridViewAdapter = new GridViewAdapter(CopyOfPointActivity.this, vList);

				gridView.setVisibility(View.VISIBLE);
				gridView.setAdapter(gridViewAdapter);
				gridView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
							long arg3) {
						// TODO Auto-generated method stub
						Views view = (Views) gridViewAdapter.getItem(arg2);
						Bundle bundle = new Bundle();
						bundle.putSerializable(CONSTANT.VIEW_INFO, view);
						goActivity(DetailActivity.class, bundle);
					}
				});
//			}
//		}, 5000);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.action_bar_advice: {
			show("中山市南区推荐路线");
			break;
		}
		case R.id.action_bar_btn_back:{
			show("返回主界面");
			finish();
			// goActivity(AreaActivity.class);
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
		getMenuInflater().inflate(R.menu.main, menu);
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
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	@Override
	public void onSliderClick(BaseSliderView slider) {
		// TODO Auto-generated method stub
		Toast.makeText(this,slider.getBundle().get("extra") + "",Toast.LENGTH_SHORT).show();
	}

}
