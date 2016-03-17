package com.zehao.tripapp.advice;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.zehao.base.BaseActivity;
import com.zehao.tripapp.R;
import com.zehao.view.RefreshableView;
import com.zehao.view.SelfListView;
import com.zehao.view.RefreshableView.PullToRefreshListener;
import com.zehao.view.SelfListView.OnLoadListener;

public class AdviceLineActivity extends BaseActivity implements OnClickListener, OnLoadListener {
	
	private RefreshableView refreshableView;
	private SelfListView selfListView;
	private AdviceListAdapter adviceListAdapter;
	private TextView textViewTitle, textViewAdvice;

	@Override
	protected void initContentView(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		baseSetContentView(savedInstanceState, R.layout.activity_view_advice);
		
		textViewTitle = (TextView) findViewById(R.id.action_bar_title);
		textViewAdvice = (TextView) findViewById(R.id.action_bar_advice);
		
		Bundle bundle = getIntent().getExtras();
		if(bundle==null){
			textViewTitle.setVisibility(View.GONE);
		} else  textViewTitle.setTextColor(this.getResources().getColor(R.color.gray));
		textViewAdvice.setTextColor(this.getResources().getColor(R.color.main_yellow));
		
		refreshableView = (RefreshableView) findViewById(R.id.refreshable_view);
		refreshableView.setOnRefreshListener(new PullToRefreshListener() {
			@Override
			public void onRefresh() {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				refreshableView.finishRefreshing();
			}
		}, 0);

		
		selfListView = (SelfListView) findViewById(R.id.list_view);
		adviceListAdapter = new AdviceListAdapter(this);
		selfListView.setAdapter(adviceListAdapter);
		selfListView.setDividerHeight(0);
		selfListView.setOnLoadListener(this);
		selfListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				shortToastHandler("点击了推荐路线：" + arg2);
			}
		});
		
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
			break;
		}
		default:
			break;
		}
	}

	@Override
	public void onLoad() {
		// TODO Auto-generated method stub
		// 为了显示效果，采用延迟加载
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				selfListView.loadComplete();
			}
		}, 3000);
	}

}
