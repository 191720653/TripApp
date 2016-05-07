package com.zehao.tripapp.tripinfo;

import java.util.List;

import org.apache.http.Header;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zehao.base.BaseActivity;
import com.zehao.constant.CONSTANT;
import com.zehao.data.bean.TripInfo;
import com.zehao.data.bean.Users;
import com.zehao.http.HttpCLient;
import com.zehao.tripapp.R;
import com.zehao.tripapp.login.LoginActivity;
import com.zehao.tripapp.mine.MineActivity;
import com.zehao.util.Tool;

public class TripInfoActivity extends BaseActivity implements OnClickListener {
	
	private ListView listView;
	private TripListAdapter adviceListAdapter;
	private TextView textViewTitle, textViewAdvice;
	private ProgressDialog progressDialog;
	
	private int type = -1;

	@Override
	protected void initContentView(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		baseSetContentView(savedInstanceState, R.layout.activity_view_advice);
		
		textViewTitle = (TextView) findViewById(R.id.action_bar_title);
		textViewAdvice = (TextView) findViewById(R.id.action_bar_advice);
		
		Bundle bundle = getIntent().getExtras();
		if(bundle!=null){
			type = bundle.getInt(CONSTANT.TRIP_TYPE_ID);
		}
		textViewTitle.setVisibility(View.GONE);
		textViewAdvice.setTextColor(this.getResources().getColor(R.color.main_yellow));

		handler.sendEmptyMessage(CONSTANT.ACTION_SHOW_DIALOG);
		getData();
		
	}

	@Override
	public void setBaseNoTitle() {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	@Override
	protected void handler(Message msg) {
		// TODO Auto-generated method stub
		switch (msg.what) {
		case CONSTANT.ACTION_SHOW_DIALOG:
			progressDialog = ProgressDialog.show(this, "获取数据", "正在联网,请稍候......");
			break;
		case CONSTANT.ACTION_DISMISS_DIALOG:
			progressDialog.dismiss();
			break;
		default:
			break;
		}
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

	public void getData(){
		String url = "/AppTripInfo_getTripInfoListAction.action";
		JsonObject json = new JsonObject();
		json.addProperty(CONSTANT.APP_GET_DATA, CONSTANT.TRIP_INFO_ORDER);
		json.addProperty(CONSTANT.TRIP_TYPE_ID, type);
		
		RequestParams params = new RequestParams();
		params.put(CONSTANT.DATA, json.toString());
		
		System.out.println("App发送数据：" + json.toString());
		HttpCLient.post(url, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				JsonObject json = (JsonObject) new JsonParser()
				.parse(new String(arg2));
				System.out.println("服务器返回数据：" + json);
				String errorCode = json.get(CONSTANT.ERRCODE).getAsString();
				if (CONSTANT.CODE_168.equals(errorCode)) {
					Gson gson = new GsonBuilder().create();
					List<TripInfo> tripTypes = gson.fromJson(json.get(CONSTANT.TRIP_INFO_DATA)
							.getAsJsonArray(),new TypeToken<List<TripInfo>>() {}.getType());
					
					listView = (ListView) findViewById(R.id.list_view);
					adviceListAdapter = new TripListAdapter(TripInfoActivity.this, tripTypes);
					listView.setAdapter(adviceListAdapter);
					listView.setDividerHeight(0);
					listView.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
								long arg3) {
							// TODO Auto-generated method stub
							shortToastHandler("点击了推荐路线：" + arg2);
						}
					});
					handler.sendEmptyMessage(CONSTANT.ACTION_DISMISS_DIALOG);
				} else {
					handler.sendEmptyMessage(CONSTANT.ACTION_DISMISS_DIALOG);
					shortToastHandler(CONSTANT.OTHER_ERROR);
				}
			}
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				handler.sendEmptyMessage(CONSTANT.ACTION_DISMISS_DIALOG);
				shortToastHandler(CONSTANT.OTHER_ERROR);
			}
		});
	}

}
