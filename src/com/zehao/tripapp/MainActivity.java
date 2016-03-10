package com.zehao.tripapp;

import java.util.List;

import com.zehao.base.BaseActivity;
import com.zehao.data.bean.Domine;
import com.zehao.data.bean.Employee;
import com.zehao.data.bean.IDataCallback;
import com.zehao.data.bean.MData;
import com.zehao.tripapp.R;
import com.zehao.tripapp.area.AreaActivity;
import com.zehao.tripapp.others.OthersActivity;

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

	// private ImageButton cultureButton;

	@Override
	protected void initContentView(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		baseSetContentView(savedInstanceState, R.layout.activity_main);

		// cultureButton = (ImageButton) findViewById(R.id.main_culture);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.main_culture: {
			// 跳往介绍南区的其它情况
			goActivity(OthersActivity.class);
			break;
		}
		case R.id.main_liangdu: {
			// 跳往介绍南区的良都
			goActivity(AreaActivity.class);
			break;
		}
		case R.id.main_beixi: {
			// 跳往介绍南区的北溪
			goActivity(AreaActivity.class);
			break;
		}
		case R.id.main_chengnan: {
			// 跳往介绍南区的城南
			goActivity(AreaActivity.class);
			break;
		}
		case R.id.main_maling: {
			// 跳往介绍南区的马岭
			goActivity(AreaActivity.class);
			break;
		}
		default:
			break;
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
