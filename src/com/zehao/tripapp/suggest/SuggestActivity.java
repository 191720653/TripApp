package com.zehao.tripapp.suggest;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zehao.constant.CONSTANT;
import com.zehao.data.bean.Users;
import com.zehao.http.HttpCLient;
import com.zehao.tripapp.R;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SuggestActivity extends Activity implements OnClickListener {
	
	private TextView textViewTitle;
	private EditText editText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_view_give_advice);
		
		textViewTitle = (TextView) findViewById(R.id.action_bar_title);
		textViewTitle.setText(getResources().getText(R.string.info_advice));
		
		editText = (EditText) findViewById(R.id.suggest);

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				InputMethodManager inputManager = (InputMethodManager)editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(editText, 0);
			}
		}, 200);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.action_bar_btn_back: {
			shortToastHandler("返回");
			finish();
			break;
		}
		case R.id.action_bar_register: {
			shortToastHandler("提交");
			suggest();
			break;
		}
		default:
			break;
		}
	}
	
	/**
	 * 弹出短时间Toast
	 * 
	 * @param text
	 */
	public void shortToastHandler(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * 获取xml文件键值
	 * 
	 * @param name
	 * @param key
	 * @return
	 */
	public String readXML(String name, String key) {
		SharedPreferences settings = getSharedPreferences(name, Context.MODE_PRIVATE);
		if (settings.contains(key)) {
			return settings.getString(key, null);
		} else {
			return null;
		}
	}
	
	public void suggest(){
		String content = editText.getText().toString();
		if(content!=null&&content.length()>0){
			if(content.length()>200) content = content.substring(200);
			String temp = readXML(CONSTANT.INFO_DATA, CONSTANT.INFO_DATA_USERS);
			JsonObject json = new JsonParser().parse(temp).getAsJsonObject();
			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
			Users users = gson.fromJson(json, Users.class);
			
			String url = "/AppSuggest_suggestAction.action";
			json = new JsonObject();
			json.addProperty(CONSTANT.APP_GET_DATA, CONSTANT.USER_SUGGEST);
			json.addProperty(CONSTANT.USER_SUGGEST_CONTENT, content);
			json.addProperty(CONSTANT.TOKEN, users.getToken());
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
						shortToastHandler("亲，您的宝贵意见已经反馈给客服了！");
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
		} else {
			shortToastHandler("亲，请先输入您宝贵的意见吧！");
		}
	}

}
