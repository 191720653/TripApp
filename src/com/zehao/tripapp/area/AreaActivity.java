package com.zehao.tripapp.area;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zehao.base.BaseActivity;
import com.zehao.data.bean.Domine;
import com.zehao.data.bean.Employee;
import com.zehao.data.bean.IDataCallback;
import com.zehao.data.bean.MData;
import com.zehao.tripapp.MainActivity;
import com.zehao.tripapp.R;
import com.zehao.tripapp.point.PointActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

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
	
	private ListView viewList;
	private ListViewAdapter viewListAdapter;
	private List<Map<String, Object>> listItems = new ArrayList<Map<String,Object>>();
	private String[][] array = {{R.drawable.image_beixi + "", "|良都1", "1681人赞过", "马应彪早年家庭贫苦，为了生计去澳洲悉尼谋生，在澳洲皈依基督教。1900年，筹集了一笔资金在香港筹办先施百货公司自任总监督。1914年，在广州长堤建立先施粤行，并附设东亚大酒店，总投资额为港币100万元，取得巨大成功。1917年，先施公司的业务扩展到上海。1921年，与蔡兴等创办了香港国民商业储蓄银行。"},
								{R.drawable.image_liangdu + "", "|良都2", "1682人赞过", "马应彪早年家庭贫苦，为了生计去澳洲悉尼谋生，在澳洲皈依基督教。1900年，筹集了一笔资金在香港筹办先施百货公司自任总监督。1914年，在广州长堤建立先施粤行，并附设东亚大酒店，总投资额为港币100万元，取得巨大成功。1917年，先施公司的业务扩展到上海。1921年，与蔡兴等创办了香港国民商业储蓄银行。"},
								{R.drawable.image_maling + "", "|良都3", "1683人赞过", "马应彪早年家庭贫苦，为了生计去澳洲悉尼谋生，在澳洲皈依基督教。1900年，筹集了一笔资金在香港筹办先施百货公司自任总监督。1914年，在广州长堤建立先施粤行，并附设东亚大酒店，总投资额为港币100万元，取得巨大成功。1917年，先施公司的业务扩展到上海。1921年，与蔡兴等创办了香港国民商业储蓄银行。"},
								{R.drawable.image_chengnan + "", "|良都4", "1684人赞过", "马应彪早年家庭贫苦，为了生计去澳洲悉尼谋生，在澳洲皈依基督教。1900年，筹集了一笔资金在香港筹办先施百货公司自任总监督。1914年，在广州长堤建立先施粤行，并附设东亚大酒店，总投资额为港币100万元，取得巨大成功。1917年，先施公司的业务扩展到上海。1921年，与蔡兴等创办了香港国民商业储蓄银行。"}};

	@Override
	protected void initContentView(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		baseSetContentView(savedInstanceState, R.layout.activity_view_area);
		
		viewList = (ListView) findViewById(R.id.view_listView);
		
		Map<String, Object> map = null;
		for(int i=0;i<array.length;i++){
			map = new HashMap<String, Object>();
			map.put("image", array[i][0]);
			map.put("title", array[i][1]);
			map.put("likeNum", array[i][2]);
			map.put("info", array[i][3]);
			listItems.add(map);
			System.out.println(map.toString());
		}
		
		viewListAdapter = new ListViewAdapter(this, listItems);
		viewList.setAdapter(viewListAdapter);
		viewList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				goActivity(PointActivity.class);
			}
		});

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.action_bar_btn_like: {
			show("中山市南区点赞");
			break;
		}
		case R.id.action_bar_btn_back:{
			show("返回主界面");
			goActivity(MainActivity.class);
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
