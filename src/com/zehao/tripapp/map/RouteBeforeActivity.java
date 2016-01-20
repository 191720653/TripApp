package com.zehao.tripapp.map;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.zehao.base.BaseActivity;
import com.zehao.constant.CONSTANT;
import com.zehao.tripapp.R;
import com.zehao.view.HistoryViewGroup;

/**
 * 跳转到地图导航界面 用Gson存储用户的输入历史，然后放进sharepreference
 */
public class RouteBeforeActivity extends BaseActivity {

	/**
	 * 用户输入历史
	 */
	private HistoryViewGroup historyViewGroup;
	private TextView tagView;
	/*ListView route_history;*/
	String[] history_arr = null;

	// private Button searchbtn;
	private ArrayAdapter<String> arr_adapter;
	private AutoCompleteTextView route_end, route_start;

	@Override
	protected void initContentView(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		addActionBar();
		baseSetContentView(savedInstanceState,
				R.layout.activity_map_routebefore);
		CharSequence titleLable = "游览路线规划";
		setTitle(titleLable);

		// 初始化
		route_start = (AutoCompleteTextView) findViewById(R.id.start);
		route_end = (AutoCompleteTextView) findViewById(R.id.end);
		/*route_history = (ListView) findViewById(R.id.listView_route_history);*/

		// 获取搜索记录文件内容
		SharedPreferences sp = getSharedPreferences("search_history", 0);
		String history = sp.getString("history", "");

		// 用逗号分割内容返回数组
		history_arr = history.split(",");

		// 新建适配器，适配器数据为搜索历史文件内容
		arr_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, history_arr);

		// 保留前30条数据
		if (history_arr.length > 30) {
			String[] newArrays = new String[30];
			// 实现数组之间的复制
			System.arraycopy(history_arr, 0, newArrays, 0, 30);
			arr_adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_dropdown_item_1line, history_arr);
		}
		route_start.setAdapter(arr_adapter);
		route_end.setAdapter(arr_adapter);

		historyViewGroup = (HistoryViewGroup) findViewById(R.id.flow_layout);
		WindowManager wm = this.getWindowManager();
		@SuppressWarnings("deprecation")
		int width = (wm.getDefaultDisplay().getWidth() - 88)/3;
		for (int i = 0; i < history_arr.length; i++) {
			tagView = (TextView) getLayoutInflater().inflate(
					R.layout.activity_map_routebefore_textview,
					historyViewGroup, false);
			ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
					width*5/6, ViewGroup.MarginLayoutParams.WRAP_CONTENT);
			if(i%3==0){
				lp.leftMargin = 0;
			}else{
				lp.leftMargin = 10;
			}
			if(i%3==2){
				lp.rightMargin = 0;
			}else{
				lp.rightMargin = 10;
			}
			lp.topMargin = 10;
			tagView.setLayoutParams(lp);
			tagView.setText(history_arr[i]);
			tagView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					System.out.println("click：" + ((TextView)view).getText());
					shortToastHandler("click：" + ((TextView)view).getText());
					if (route_start.getText() == null
							|| route_start.getText().length() == 0) {
						route_start.setText(((TextView)view).getText());
					} else if (route_end.getText() == null
							|| route_end.getText().length() == 0) {
						route_end.setText(((TextView)view).getText());
					}
				}
			});
			historyViewGroup.addView(tagView);
		}

	}

	public void save(String text) {
		SharedPreferences mysp = getSharedPreferences("search_history", 0);
		String old_text = mysp.getString("history", "");
		if (old_text.length() > 500) {
			old_text = old_text.substring(old_text.indexOf(","));
		}

		// 利用StringBuilder.append新增内容，逗号便于读取内容时用逗号拆分开
		StringBuilder builder = new StringBuilder(old_text);
		builder.append(text + ",");

		// 判断搜索内容是否已经存在于历史文件，已存在则不重复添加
		if (!old_text.contains(text + ",")) {
			SharedPreferences.Editor myeditor = mysp.edit();
			myeditor.putString("history", builder.toString());
			myeditor.commit();
			Toast.makeText(this, text + "添加成功", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this, text + "已存在", Toast.LENGTH_SHORT).show();
		}

	}

	// 清除搜索记录
	public void cleanHistory(View v) {
		SharedPreferences sp = getSharedPreferences("search_history", 0);
		SharedPreferences.Editor editor = sp.edit();
		editor.clear();
		editor.commit();
		Toast.makeText(this, "清除成功", Toast.LENGTH_SHORT).show();
		super.onDestroy();
	}

	/**
	 * 发起路线规划搜索
	 * 
	 * @param v
	 */
	public void searchButton(View view) {
		// 处理搜索按钮响应

		Intent intent = new Intent(RouteBeforeActivity.this,
				RouteActivity.class);
		// 实际使用中请对起点终点城市进行正确的设定
		if (view.getId() == R.id.drive) {
			intent.putExtra(CONSTANT.TYPE, CONSTANT.ROUTE_DRIVE);
		} else if (view.getId() == R.id.transit) {
			intent.putExtra(CONSTANT.TYPE, CONSTANT.ROUTE_BUS);
		} else if (view.getId() == R.id.walk) {
			intent.putExtra(CONSTANT.TYPE, CONSTANT.ROUTE_WALK);
		}
		String start = route_start.getText().toString();
		String end = route_end.getText().toString();
		intent.putExtra(CONSTANT.ROUTE_START, start);
		intent.putExtra(CONSTANT.ROUTE_END, end);
		save(start);
		save(end);
		startActivity(intent);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void setBaseNoTitle() {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	@Override
	protected void handler(Message msg) {
		// TODO Auto-generated method stub

	}

}
