package com.zehao.tripapp.map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.zehao.constant.CONSTANT;
import com.zehao.tripapp.R;

/**
 * 跳转到地图导航界面
 * 用Gson存储用户的输入历史，然后放进sharepreference
 */
public class RouteBeforeActivity extends Activity {
    
	/**
	 * 用户输入历史
	 */
    ListView route_history;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_routebefore);
        CharSequence titleLable = "游览路线规划";
        setTitle(titleLable);
        
        route_history = (ListView) findViewById(R.id.listView_route_history);
    }

    /**
     * 发起路线规划搜索
     *
     * @param v
     */
    public void searchButton(View view) {
        // 处理搜索按钮响应
        EditText editSt = (EditText) findViewById(R.id.start);
        EditText editEn = (EditText) findViewById(R.id.end);

        Intent intent = new Intent(RouteBeforeActivity.this, RouteActivity.class);
        // 实际使用中请对起点终点城市进行正确的设定
        if (view.getId() == R.id.drive) {
        	intent.putExtra(CONSTANT.TYPE, CONSTANT.ROUTE_DRIVE);
        } else if (view.getId() == R.id.transit) {
        	intent.putExtra(CONSTANT.TYPE, CONSTANT.ROUTE_BUS);
        } else if (view.getId() == R.id.walk) {
        	intent.putExtra(CONSTANT.TYPE, CONSTANT.ROUTE_WALK);
        }
        intent.putExtra(CONSTANT.ROUTE_START, editSt.getText().toString());
    	intent.putExtra(CONSTANT.ROUTE_END, editEn.getText().toString());
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

}
