package com.zehao.tripapp;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zehao.base.BaseActivity;
import com.zehao.constant.CONSTANT;
import com.zehao.data.bean.Domine;
import com.zehao.data.bean.Employee;
import com.zehao.data.bean.IDataCallback;
import com.zehao.data.bean.MData;
import com.zehao.data.bean.Users;
import com.zehao.tripapp.R;
import com.zehao.tripapp.advice.AdviceLineActivity;
import com.zehao.tripapp.area.AreaActivity;
import com.zehao.tripapp.login.LoginActivity;
import com.zehao.tripapp.mine.MineActivity;
import com.zehao.tripapp.others.SortActivity;
import com.zehao.util.Tool;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

/**
 * < TODO：中山市南区App主界面，显示南区的4大景区 >
 * 
 * @ClassName: MainActivity
 * @author pc-hao
 * @date 2016年2月22日 下午10:21:14
 * @version V 1.0
 */
public class MainActivity extends BaseActivity implements
		IDataCallback<MData<? extends Domine>>, OnClickListener {

	@Override
	protected void initContentView(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		baseSetContentView(savedInstanceState, R.layout.activity_main);
		addLeftMenu(true);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.relative_build:{
			shortToastHandler("中山市南区经典古建");
			Bundle bundle = new Bundle();
			bundle.putString(CONSTANT.SORT_VIEW_TYPE, CONSTANT.SORT_VIEW_TYPE_BUILD);
			goActivity(SortActivity.class, bundle);
			break;
			}
		case R.id.relative_tree:{
			shortToastHandler("中山市南区名木古树");
			Bundle bundle = new Bundle();
			bundle.putString(CONSTANT.SORT_VIEW_TYPE, CONSTANT.SORT_VIEW_TYPE_TREE);
			goActivity(SortActivity.class, bundle);
			break;
			}
		case R.id.relative_hero:{
			shortToastHandler("中山市南区名人典故");
			Bundle bundle = new Bundle();
			bundle.putString(CONSTANT.SORT_VIEW_TYPE, CONSTANT.SORT_VIEW_TYPE_PERSON);
			goActivity(SortActivity.class, bundle);
			break;
			}
		case R.id.relative_food:{
			shortToastHandler("中山市南区名店美食");
			break;
			}
		case R.id.relative_custom:{
			shortToastHandler("中山市南区民品民俗");
			Bundle bundle = new Bundle();
			bundle.putString(CONSTANT.SORT_VIEW_TYPE, CONSTANT.SORT_VIEW_TYPE_CUSTOM);
			goActivity(SortActivity.class, bundle);
			break;
			}
		case R.id.relative_advice:{
			// show("中山市南区路线推荐");
			startActivity(new Intent(this, AdviceLineActivity.class));
			break;
			}
		case R.id.action_bar_btn_mine:
		case R.id.action_bar_btn_mines:{
			shortToastHandler("中山市南区我的信息");
			mine();
			break;
			}
		case R.id.main_culture: {
			// 显示左侧菜单
			menu.showMenu();
			break;
		}
		case R.id.main_liangdu: {
			// 跳往介绍南区的良都
			Bundle bundle = new Bundle();
			bundle.putInt(CONSTANT.MAIN_VIEW_ID, 6);
			goActivity(AreaActivity.class, bundle);
			break;
		}
		case R.id.main_beixi: {
			// 跳往介绍南区的北溪
			Bundle bundle = new Bundle();
			bundle.putInt(CONSTANT.MAIN_VIEW_ID, 9);
			goActivity(AreaActivity.class, bundle);
			break;
		}
		case R.id.main_chengnan: {
			// 跳往介绍南区的城南
			Bundle bundle = new Bundle();
			bundle.putInt(CONSTANT.MAIN_VIEW_ID, 8);
			goActivity(AreaActivity.class, bundle);
			break;
		}
		case R.id.main_maling: {
			// 跳往介绍南区的马岭
			Bundle bundle = new Bundle();
			bundle.putInt(CONSTANT.MAIN_VIEW_ID, 7);
			goActivity(AreaActivity.class, bundle);
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
				goActivity(MineActivity.class);
			}
		} else {
			goActivityAndFinish(LoginActivity.class);
		}
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

}
