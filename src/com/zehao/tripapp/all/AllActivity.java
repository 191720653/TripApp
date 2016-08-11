package com.zehao.tripapp.all;

import java.util.List;

import org.apache.http.Header;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

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
import com.zehao.data.bean.Other;
import com.zehao.data.bean.Users;
import com.zehao.http.HttpCLient;
import com.zehao.tripapp.R;
import com.zehao.tripapp.advice.AdviceLineActivity;
import com.zehao.tripapp.login.LoginActivity;
import com.zehao.tripapp.mine.MineActivity;
import com.zehao.util.Tool;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

/**
 * < TODO：中山市南区App名人典故、名点美食、民品民俗的主界面介绍 >
 * 
 * @ClassName: AllActivity
 * @author pc-hao
 * @date 2016年2月24日 下午14:23:14
 * @version V 1.0
 */
public class AllActivity extends BaseActivity implements
		IDataCallback<MData<? extends Domine>>, OnClickListener, OnSliderClickListener {
	
	private String[] titles = {"名人典故","名点美食","民品民俗"};
	
	private String[] infos = {"名人典故的介绍！","名点美食的介绍！","民品民俗的介绍！"};
	
	private String[] urls = {CONSTANT.BASE_ROOT_URL+"/photoes/image_other_1_",
			CONSTANT.BASE_ROOT_URL+"/photoes/image_other_2_",
			CONSTANT.BASE_ROOT_URL+"/photoes/image_other_3_"};

	private SliderLayout mDemoSlider;
	
	private GridView gridView;
	private GridViewAdapter gridViewAdapter;
	
	private TextView viewName, viewInfo, viewTitle;
	
	private ProgressBar progressBar;
	
	private String type;

	@Override
	protected void initContentView(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		baseSetContentView(savedInstanceState, R.layout.activity_view_other);
		
		gridView = (GridView) findViewById(R.id.view_point_gridview);
		viewInfo = (TextView) findViewById(R.id.view_point_info);
		viewName = (TextView) findViewById(R.id.view_point_name);
		viewTitle = (TextView) findViewById(R.id.all_view_title);
		
		mDemoSlider = (SliderLayout)findViewById(R.id.slider);
		mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Stack);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        
        progressBar = new ProgressBar(this);
		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
		layoutParams.setMargins(0, 120, 0, 0);
		this.addContentView(progressBar, layoutParams);
        
		Bundle bundle = getIntent().getExtras();
		type = bundle.getString(CONSTANT.OTHER_TYPE);
		System.out.println(type);
		viewTitle.setText(titles[Integer.parseInt(type)-1]);
		
        new Handler().post(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String url = "/AppOther_getOtherInfoListAction.action";
				JsonObject json = new JsonObject();
				json.addProperty(CONSTANT.APP_GET_DATA, CONSTANT.OTHER_INFO);
				json.addProperty(CONSTANT.OTHER_TYPE, type);
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
							List<Other> others = gson.fromJson(json.get(CONSTANT.OTHER_LIST)
									.getAsJsonArray(),new TypeToken<List<Other>>() {}.getType());
							setGridViewData(others);
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
        viewInfo.setText(infos[Integer.parseInt(type)-1]);
        viewName.setText(titles[Integer.parseInt(type)-1]);
        
        for (int i=1;i<=3;i++) {
			TextSliderView textSliderView = new TextSliderView(this);
			// initialize a SliderLayout
			textSliderView.description("").image(urls[Integer.parseInt(type)-1]+i+".png")
					.setScaleType(BaseSliderView.ScaleType.Fit)
					.setOnSliderClickListener(this);
			// add your extra information
			textSliderView.getBundle().putString("extra", i+CONSTANT.NULL_STRING);
			mDemoSlider.addSlider(textSliderView);
		}
	}
	
	private void setGridViewData(final List<Other> vList) {
		
		System.out.println("setGridViewData");

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				gridViewAdapter = new GridViewAdapter(AllActivity.this, vList);

				gridView.setVisibility(View.VISIBLE);
				gridView.setAdapter(gridViewAdapter);
				gridView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
							long arg3) {
						// TODO Auto-generated method stub
						Other temp = (Other) gridViewAdapter.getItem(arg2);
						Bundle bundle = new Bundle();
						bundle.putSerializable(CONSTANT.OTHER_INFO, temp);
						goActivity(AllDetailActivity.class, bundle);
					}
				});
				progressBar.setVisibility(View.GONE);
			}
		}, 2000);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.action_bar_advice: {
			// show("中山市南区推荐路线");
			Bundle bundle = new Bundle();
			bundle.putString("advice", "view");
			goActivity(AdviceLineActivity.class, bundle);
			break;
		}
		case R.id.action_bar_btn_back:{
			show("返回主界面");
			finish();
			// goActivity(AreaActivity.class);
			break;
			}
		case R.id.action_bar_btn_share:{
			// show("中山市南区分享");
			showShare();
			break;
			}
		case R.id.action_bar_btn_mine:
		case R.id.action_bar_btn_mines:{
			show("中山市南区我的信息");
			mine();
			break;
			}
		default:
			break;
		}
	}
	
	public void mine(){
		String temp = readXML(CONSTANT.INFO_DATA, CONSTANT.INFO_DATA_USERS);
		if(!CONSTANT.NULL_STRING.equals(Tool.NVL(temp))){
			JsonObject json = new JsonParser().parse(temp).getAsJsonObject();
			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
			Users user = gson.fromJson(json, Users.class);
			if(CONSTANT.LOGIN_SIGN_OFF.equals(user.getLoginSign())){
				goActivityAndFinish(LoginActivity.class);
			} else {
				goActivityAndFinish(MineActivity.class);
			}
		} else {
			goActivityAndFinish(LoginActivity.class);
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
	
	private void showShare() {
		String icon = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + this.getPackageName() + "/download/" + "userIcon.jpg";
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();
		// 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法
		// oks.setNotification(R.drawable.ic_launcher,
		// getString(R.string.app_name));
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		// oks.setTitle(fragment.getViews().getViewName());
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		// oks.setTitleUrl("http://sharesdk.cn");
		// text是分享文本，所有平台都需要这个字段
		oks.setText(infos[Integer.parseInt(type)-1]);
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		oks.setImagePath(icon);// 确保SDcard下面存在此张图片/sdcard/test.jpg //fragment.getUrls().get(0)
		// url仅在微信（包括好友和朋友圈）中使用
		// oks.setUrl("http://sharesdk.cn");
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		// oks.setComment("我是测试评论文本");
		// site是分享此内容的网站名称，仅在QQ空间使用
		// oks.setSite(getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		// oks.setSiteUrl("http://sharesdk.cn");

		// 启动分享GUI
		oks.show(this);
	}
}
