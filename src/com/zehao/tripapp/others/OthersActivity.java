package com.zehao.tripapp.others;

import com.zehao.tripapp.R;
import com.zehao.tripapp.advice.AdviceLineActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;

/**
 * < TODO：中山市南区App其它功能列表 >
 * @ClassName: OthersActivity
 * @author pc-hao
 * @date 2016年2月22日 下午10:06:49
 * @version V 1.0
 */
public class OthersActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 不用系统自带ActionBar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_view_culture);
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
			show("中山市南区经典古建");
			break;
			}
		case R.id.relative_tree:{
			show("中山市南区名木古树");
			break;
			}
		case R.id.relative_hero:{
			show("中山市南区名人典故");
			break;
			}
		case R.id.relative_food:{
			show("中山市南区名店美食");
			break;
			}
		case R.id.relative_custom:{
			show("中山市南区民品民俗");
			break;
			}
		case R.id.relative_advice:{
			// show("中山市南区路线推荐");
			startActivity(new Intent(this, AdviceLineActivity.class));
			break;
			}
		case R.id.action_bar_btn_back:{
			show("返回主界面");
			finish();
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
	
	public void goForActivity(Class<?> clas){
		Intent intent = new Intent(this, clas);
		startActivity(intent);
		this.finish();
	}

}
