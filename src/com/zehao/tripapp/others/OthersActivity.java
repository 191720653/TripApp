package com.zehao.tripapp.others;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zehao.base.BaseActivity;
import com.zehao.constant.CONSTANT;
import com.zehao.data.bean.Users;
import com.zehao.tripapp.R;
import com.zehao.tripapp.advice.AdviceLineActivity;
import com.zehao.tripapp.login.LoginActivity;
import com.zehao.tripapp.mine.MineActivity;
import com.zehao.util.Tool;

import android.os.Bundle;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.content.Intent;

/**
 * < TODO：中山市南区App其它功能列表 >
 * @ClassName: OthersActivity
 * @author pc-hao
 * @date 2016年2月22日 下午10:06:49
 * @version V 1.0
 */
public class OthersActivity extends BaseActivity implements OnClickListener {

	@Override
	protected void initContentView(Bundle savedInstanceState) {
		baseSetContentView(savedInstanceState, R.layout.activity_view_culture);
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
		case R.id.relative_build:{
			shortToastHandler("中山市南区经典古建");
			break;
			}
		case R.id.relative_tree:{
			shortToastHandler("中山市南区名木古树");
			break;
			}
		case R.id.relative_hero:{
			shortToastHandler("中山市南区名人典故");
			break;
			}
		case R.id.relative_food:{
			shortToastHandler("中山市南区名店美食");
			break;
			}
		case R.id.relative_custom:{
			shortToastHandler("中山市南区民品民俗");
			break;
			}
		case R.id.relative_advice:{
			// show("中山市南区路线推荐");
			startActivity(new Intent(this, AdviceLineActivity.class));
			break;
			}
		case R.id.action_bar_btn_back:{
			shortToastHandler("返回主界面");
			finish();
			break;
			}
		case R.id.action_bar_btn_share:{
			shortToastHandler("中山市南区分享");
			break;
			}
		case R.id.action_bar_btn_mine:
		case R.id.action_bar_btn_mines:{
			shortToastHandler("中山市南区我的信息");
			mine();
			break;
			}
		default:
			break;
		}
	}
	
	public void goForActivity(Class<?> clas){
		Intent intent = new Intent(this, clas);
		startActivity(intent);
		this.finish();
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
