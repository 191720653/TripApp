package com.zehao.tripapp.area;

import java.util.List;

import com.achep.header2actionbar.FadingActionBarHelper;
import com.zehao.base.BaseActivity;
import com.zehao.data.bean.Domine;
import com.zehao.data.bean.Employee;
import com.zehao.data.bean.IDataCallback;
import com.zehao.data.bean.MData;
import com.zehao.tripapp.MainActivity;
import com.zehao.tripapp.R;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

/**
 * < TODO：中山市南区App景区介绍界面，显示南区的4大景区的详细介绍 >
 * 
 * @ClassName: AreaActivity
 * @author pc-hao
 * @date 2016年2月23日 下午09:23:14
 * @version V 1.0
 */
public class AreaActivity extends BaseActivity implements
		IDataCallback<MData<? extends Domine>>, OnClickListener {

	private FadingActionBarHelper mFadingActionBarHelper;

	public FadingActionBarHelper getFadingActionBarHelper() {
		return mFadingActionBarHelper;
	}
	
	@Override
	protected void initContentView(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		baseSetContentView(savedInstanceState, R.layout.activity_view_area);
		setActionBarLayout(R.layout.action_bar_like);
		mFadingActionBarHelper = new FadingActionBarHelper(getActionBar(),
				getResources().getDrawable(R.drawable.actionbar_bg));

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new ListViewFragment()).commit();
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.action_bar_btn_like: {
			show("中山市南区点赞");
			break;
		}
		case R.id.action_bar_btn_back: {
			show("返回主界面");
			goActivity(MainActivity.class);
			break;
		}
		case R.id.action_bar_btn_share: {
			show("中山市南区分享");
			break;
		}
		case R.id.action_bar_btn_mine:
		case R.id.action_bar_btn_mines: {
			show("中山市南区我的信息");
			break;
		}
		default:
			break;
		}
	}

	public void show(String contents) {
		Toast.makeText(this, contents, Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
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
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
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

}
