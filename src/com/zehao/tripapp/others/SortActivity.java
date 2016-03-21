package com.zehao.tripapp.others;

import java.util.List;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zehao.base.BaseActivity;
import com.zehao.constant.CONSTANT;
import com.zehao.data.bean.Users;
import com.zehao.data.bean.Views;
import com.zehao.http.HttpCLient;
import com.zehao.tripapp.R;
import com.zehao.tripapp.detail.DetailActivity;
import com.zehao.tripapp.login.LoginActivity;
import com.zehao.tripapp.mine.MineActivity;
import com.zehao.util.Tool;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * < TODO：中山市南区App分类列表 >
 * @ClassName: SortActivity
 * @author pc-hao
 * @date 2016年2月22日 下午10:06:49
 * @version V 1.0
 */
public class SortActivity extends BaseActivity implements OnClickListener {
	
	private ListView listView;
	private SortListAdapter sortListAdapter;
	private TextView textViewTitle, textViewAdvice;
	private ProgressBar progressBar;
	
	private String sortType = null;

	@Override
	protected void initContentView(Bundle savedInstanceState) {
		baseSetContentView(savedInstanceState, R.layout.activity_view_sort);
		
		textViewAdvice = (TextView) findViewById(R.id.action_bar_advice);
		textViewAdvice.setVisibility(View.GONE);
		
		Bundle bundle = getIntent().getExtras();
		sortType = bundle.getString(CONSTANT.SORT_VIEW_TYPE);
		textViewTitle = (TextView) findViewById(R.id.action_bar_title);
		textViewTitle.setText(sortType);
		
		progressBar = (ProgressBar) findViewById(R.id.loading);

		new Handler().post(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String url = "/AppSort_getSortListAction.action";
				JsonObject json = new JsonObject();
				json.addProperty(CONSTANT.APP_GET_DATA, CONSTANT.SORT_VIEW);
				json.addProperty(CONSTANT.SORT_VIEW_TYPE, sortType);
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
							Gson gson = new GsonBuilder().create();
							String commonSign = json.get(CONSTANT.COMMON_SIGN).getAsString();
							if(CONSTANT.COMMON_SIGN_HAS.equals(commonSign)){
								List<Views> views = gson.fromJson(json.get(CONSTANT.SORT_VIEW_LIST)
												.getAsJsonArray(),new TypeToken<List<Views>>() {}.getType());
								initData(views);
							}else{
								shortToastHandler("亲，这里暂时没有什么！");
							}
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
	}
	
	public void initData(List<Views> views){
		listView = (ListView) findViewById(R.id.list_view);
		sortListAdapter = new SortListAdapter(this, views);
		listView.setAdapter(sortListAdapter);
		listView.setDividerHeight(0);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Bundle bundle = new Bundle();
				bundle.putSerializable(CONSTANT.VIEW_INFO, (Views)sortListAdapter.getItem(arg2));
				goActivity(DetailActivity.class, bundle);
			}
		});
		progressBar.setVisibility(View.GONE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.action_bar_btn_back: {
			shortToastHandler("返回主界面");
			finish();
			break;
		}
		case R.id.action_bar_btn_share: {
			shortToastHandler("中山市南区分享");
			break;
		}
		case R.id.action_bar_btn_mine:
		case R.id.action_bar_btn_mines: {
			shortToastHandler("中山市南区我的信息");
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
