package com.zehao.base;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

/**
 * < 基本FragmentActivity类，包装了跳转Activity方法、弹出Toast方法、读写Xml文件方法 >
 * 
 * @ClassName: BaseFragmentActivity
 * @author pc-hao
 * @date 2015年4月27日 上午9:40:04
 * @version V 1.0
 */
public abstract class BaseFragmentActivity extends FragmentActivity {

	protected static Context context;
	protected SharedPreferences settings;

	// Toast处理器
	private static Handler toastHandler = new Handler() {

		// 处理信息
		public void handleMessage(android.os.Message message) {
			// 弹出Toast
			switch (message.what) {
			case 0: {
				Toast.makeText(context, message.obj.toString(),
						Toast.LENGTH_SHORT).show();
				break;
			}
			case 1: {
				Toast.makeText(context, message.obj.toString(),
						Toast.LENGTH_LONG).show();
				break;
			}
			default: {
				break;
			}
			}

		};
	};

	public BaseFragmentActivity() {

		context = this;

	}

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
	 * 调用toastHandler弹出短时间Toast
	 * 
	 * @param text
	 */
	public void shortToastHandler(String text) {
		toastHandler.obtainMessage(0, text).sendToTarget();
	}

	/**
	 * 调用toastHandler弹出长时间Toast
	 * 
	 * @param text
	 */
	public void longToastHandler(String text) {
		toastHandler.obtainMessage(1, text).sendToTarget();
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
	public Object readXMLBoolean(String name, String key) {
		settings = getSharedPreferences(name, Context.MODE_PRIVATE);
		if (settings.contains(key)) {
			return settings.getBoolean(key, false);
		} else {
			return null;
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

}
