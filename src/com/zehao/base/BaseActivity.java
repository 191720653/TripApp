package com.zehao.base;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.CanvasTransformer;
import com.zehao.data.bean.Domine;
import com.zehao.data.bean.IDataCallback;
import com.zehao.data.bean.MData;
import com.zehao.tripapp.R;
import com.zehao.util.IHandler;
import com.zehao.util.UIHandler;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * < 基本Activity类，包装了跳转Activity方法、弹出Toast方法、读写Xml文件方法 >
 * 
 * @ClassName: BaseActivity
 * @author pc-hao
 * @date 2015年4月27日 上午9:40:04
 * @version V 1.0
 */
public abstract class BaseActivity extends Activity {

	protected static Context context;
	protected SharedPreferences settings;
	protected SlidingMenu menu = null;

	/**
	 * 进入指定Actibity并携带Bundle
	 * 
	 * @param clas
	 * @param bundle
	 */
	public void goActivity(Class<?> clas, Bundle bundle) {
		Intent intent = new Intent(getBaseContext(), clas);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivity(intent);
	}

	/**
	 * 进入指定Activity，不带数据
	 * 
	 * @param clas
	 */
	public void goActivity(Class<?> clas) {
		this.goActivity(clas, null);
	}

	/**
	 * 进入指定Activity，携带Bundle，finish当前
	 * 
	 * @param clas
	 * @param bundle
	 */
	public void goActivityAndFinish(Class<?> clas, Bundle bundle) {
		this.goActivity(clas, bundle);
		finish();
	}

	/**
	 * 进入指定Activity，finish当前
	 * 
	 * @param clas
	 */
	public void goActivityAndFinish(Class<?> clas) {
		this.goActivity(clas, null);
		finish();
	}

	/**
	 * 携带数据进入指定Activity，有返回结果
	 * 
	 * @param clas
	 * @param bundle
	 * @param requestCode
	 */
	public void goActivityForResult(Class<?> clas, Bundle bundle,
			int requestCode) {
		Intent intent = new Intent(this, clas);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivityForResult(intent, requestCode);
	}

	/**
	 * 弹出短时间Toast
	 * 
	 * @param text
	 */
	public void shortToastHandler(String text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 弹出长时间Toast
	 * 
	 * @param text
	 */
	public void longToastHandler(String text) {
		Toast.makeText(context, text, Toast.LENGTH_LONG).show();
	}

	/**
	 * 写入xml文件
	 * 
	 * @param name
	 * @param key
	 * @param value
	 */
	public void writeXML(String name, String key, String value) {
		settings = getSharedPreferences(name, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(key, value);
		editor.commit();
	}

	/**
	 * 将Boolean值写入xml文件中
	 * 
	 * @param name
	 * @param key
	 * @param value
	 */
	public void writeXMLBoolean(String name, String key, Boolean value) {
		settings = getSharedPreferences(name, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	/**
	 * 获取xml文件键值
	 * 
	 * @param name
	 * @param key
	 * @return
	 */
	public String readXML(String name, String key) {
		settings = getSharedPreferences(name, Context.MODE_PRIVATE);
		if (settings.contains(key)) {
			return settings.getString(key, null);
		} else {
			return null;
		}
	}

	/**
	 * 读取xml文件中的Boolean值
	 * 
	 * @param name
	 * @param key
	 * @return
	 */
	public Boolean readXMLBoolean(String name, String key) {
		settings = getSharedPreferences(name, Context.MODE_PRIVATE);
		if (settings.contains(key)) {
			return settings.getBoolean(key, false);
		} else {
			return false;
		}
	}

	/**
	 * 删除xml文件里面的键值
	 * 
	 * @param name
	 * @param key
	 */
	public void removeXML(String name, String key) {
		settings = getSharedPreferences(name, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.remove(key);
		editor.commit();
	}

	protected UIHandler handler = new UIHandler(Looper.getMainLooper());

	// 数据回调接口，都传递Domine的子类实体
	protected IDataCallback<MData<? extends Domine>> dataCallback;

	public BaseActivity() {
		context = this;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBaseNoTitle();
		setContentView(R.layout.activity_base);

		setHandler();
		initContentView(savedInstanceState);

	}

	/**
	 * 子类添加布局
	 * @param savedInstanceState
	 * @param layoutResId
	 */
	public void baseSetContentView(Bundle savedInstanceState, int layoutResId) {
		LinearLayout content = (LinearLayout) findViewById(R.id.contents);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(layoutResId, null);
		content.addView(view);
	}

	/**
	 * 子类添加布局
	 * @param savedInstanceState
	 * @param view
	 */
	public void baseSetContentView(Bundle savedInstanceState, View view) {
		LinearLayout content = (LinearLayout) findViewById(R.id.contents);
		content.addView(view);
	}

	private void setHandler() {
		handler.setHandler(new IHandler() {
			public void handleMessage(Message msg) {
				handler(msg);// 有消息就提交给子类实现的方法
			}
		});
	}

	/**
	 * 初始化UI，setContentView等
	 * @param savedInstanceState
	 */
	protected abstract void initContentView(Bundle savedInstanceState);

	/**
	 *  可能全屏或者没有ActionBar等，有子类决定
	 */
	public abstract void setBaseNoTitle();

	/**
	 *  增加自定义的ActionBar
	 */
	@SuppressLint("InflateParams")
	protected void addActionBar() {
		LinearLayout content = (LinearLayout) findViewById(R.id.contents);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.action_bar, null);
		content.addView(view);
	}
	
	/**
	 * 是否能有侧滑栏
	 * @param enable
	 */
	protected void addLeftMenu(boolean enable) {
		// 如果你的项目有侧滑栏可以处理此方法
		if (enable) { // 是否能有侧滑栏
			// configure the SlidingMenu
			menu = new SlidingMenu(this);
			menu.setMode(SlidingMenu.LEFT);
			// 设置触摸屏幕的模式
			menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
			menu.setShadowWidthRes(R.dimen.shadow_width);
			menu.setShadowDrawable(R.drawable.shadow);

			// 设置滑动菜单视图的宽度
			menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
			// 设置渐入渐出效果的值
			menu.setFadeDegree(0.35f);
			/**
			 * SLIDING_WINDOW will include the Title/ActionBar in the content
			 * section of the SlidingMenu, while SLIDING_CONTENT does not.
			 */
			menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
			// 为侧滑菜单设置布局
			menu.setMenu(R.layout.menu_left);
			menu.setBehindCanvasTransformer(new CanvasTransformer() {
				@Override
				public void transformCanvas(Canvas canvas, float percentOpen) {// percentOpen 滑动菜单栏打开时的百分比值
					// TODO Auto-generated method stub
					float scale = (float) (percentOpen * 0.25 + 0.75);
	        		canvas.scale(scale, scale, canvas.getWidth() / 2, canvas.getHeight() / 2);
				}
			});
		} else {

		}
	}
	
	/**
	 * 让子类处理消息
	 * @param msg
	 */
	protected abstract void handler(Message msg);

	/**
	 *  横竖屏切换，键盘等
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		try {
			super.onRestoreInstanceState(savedInstanceState);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final String ACTION_NETWORK_CHANGE = "android.net.change.action";
	public static final String ACTION_PUSH_DATA = "android.data.bean.push.action";
	public static final String ACTION_NEW_VERSION = "android.apk.update.action";

	@Override
	protected void onResume() {
		super.onResume();
		// 你可以添加多个Action捕获
		IntentFilter filter = new IntentFilter();
		filter.addAction(ACTION_NETWORK_CHANGE);
		filter.addAction(ACTION_PUSH_DATA);
		filter.addAction(ACTION_NEW_VERSION);
		registerReceiver(receiver, filter);
		// 还可能发送统计数据，比如第三方的SDK 做统计需求
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(receiver);
		// 还可能发送统计数据，比如第三方的SDK 做统计需求
	}

	BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// 处理各种情况
			String action = intent.getAction();
			if (ACTION_NETWORK_CHANGE.equals(action)) { // 网络发生变化
				// 处理网络问题
			} else if (ACTION_PUSH_DATA.equals(action)) { // 可能有新数据
				Bundle b = intent.getExtras();
				Object mdata = (Object) b.get("data");
				if (dataCallback != null) { // 数据通知
					dataCallback.onNewData(mdata);
				}
			} else if (ACTION_NEW_VERSION.equals(action)) { // 可能发现新版本
				// VersionDialog 可能是版本提示是否需要下载的对话框
			}
		}
	};

	public void setDataCallback(
			IDataCallback<MData<? extends Domine>> dataCallback) {
		this.dataCallback = dataCallback;
	}

}
