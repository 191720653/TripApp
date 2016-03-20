package com.zehao.tripapp.picture;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import org.apache.http.Header;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.huewu.pla.lib.MultiColumnPullToRefreshListView;
import com.huewu.pla.lib.MultiColumnPullToRefreshListView.OnRefreshListener;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zehao.base.BaseActivity;
import com.zehao.constant.CONSTANT;
import com.zehao.data.bean.Users;
import com.zehao.http.HttpCLient;
import com.zehao.tripapp.R;

public class WaterfallActivity extends BaseActivity implements OnClickListener {
	
	// 可以把它当成一个listView，如果不想用下拉刷新这个特性，只是瀑布流，可以用这个：MultiColumnListView 一样的用法
	private MultiColumnPullToRefreshListView waterfallView;
	
	private WaterfallAdapter adapter = null;

	private JsonArray urls = null;

	@Override
	protected void initContentView(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		baseSetContentView(savedInstanceState, R.layout.activity_waterfall_image_view);
		
		waterfallView = (MultiColumnPullToRefreshListView) findViewById(R.id.list);
		
		String temp = readXML(CONSTANT.INFO_DATA, CONSTANT.INFO_PHOTO_URL);
		if(temp!=null&&temp.length()!=0){
			urls = new JsonParser().parse(temp).getAsJsonArray();
			setData();
		}
		
		getUrl();
		
	}
	
	public void setData(){
		ArrayList<String> imageList = new ArrayList<String>();
		for(int i=0;i<urls.size();i++){
			imageList.add(urls.get(i).getAsString());
		}
		
		if(adapter!=null){
			adapter.setList(imageList);
			adapter.notifyDataSetChanged();
		} else {
			adapter = new WaterfallAdapter(imageList, this);
			waterfallView.setAdapter(adapter);
			waterfallView.setOnRefreshListener(new OnRefreshListener() {
				@Override
				public void onRefresh() {
					// TODO Auto-generated method stub
					//下拉刷新要做的事
					getUrl();
					//刷新完成后记得调用这个
					waterfallView.onRefreshComplete();
				}
			});
		}
	}
	
	public void getUrl(){
		String temp = readXML(CONSTANT.INFO_DATA, CONSTANT.INFO_DATA_USERS);
		JsonObject json = new JsonParser().parse(temp).getAsJsonObject();
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		Users user = gson.fromJson(json, Users.class);
		String url = "/AppPicture_getPicturesAction.action";
		json = new JsonObject();
		json.addProperty(CONSTANT.TOKEN, user.getToken());
		json.addProperty(CONSTANT.PICTURE_COUNT, urls==null?0:urls.size());
		
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
					String commonSign = json.get(CONSTANT.COMMON_SIGN).getAsString();
					if(CONSTANT.COMMON_SIGN_HAS.equals(commonSign)){
						urls = json.getAsJsonArray(CONSTANT.PICTURES);
						setData();
						writeXML(CONSTANT.INFO_DATA, CONSTANT.INFO_PHOTO_URL, urls.toString());
					}else{
						longToastHandler("亲，拍张景区的照片上传到您的云相册吧！");
					}
				} else {
					shortToastHandler(CONSTANT.OTHER_ERROR);
				}
			}
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				shortToastHandler(CONSTANT.OTHER_ERROR);
			}
		});
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.action_bar_add: {
			shortToastHandler("开始拍照");
			// goActivity(TakePhotoActivity.class);
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(intent, 1);
			break;
		}
		case R.id.action_bar_btn_back:{
			shortToastHandler("返回主界面");
			finish();
			break;
			}
		default:
			break;
		}
	}
	
	@SuppressLint("SdCardPath")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			String sdStatus = Environment.getExternalStorageState();
			if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
				Log.i("TestFile",
						"SD card is not avaiable/writeable right now.");
				return;
			}
			new DateFormat();
			String name = DateFormat.format("yyyyMMdd_hhmmss",Calendar.getInstance(Locale.CHINA)) + ".jpg";	
			Toast.makeText(this, name, Toast.LENGTH_LONG).show();
			Bundle bundle = data.getExtras();
			Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
		
			FileOutputStream b = null;		   
			File file = new File("/sdcard/Image/");
			if(!file.exists()){
				file.mkdirs();// 创建文件夹
			}
			String fileName = "/sdcard/Image/"+name;

			try {
				b = new FileOutputStream(fileName);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				try {
					b.flush();
					b.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			upload(fileName);
		}
	}
	
	public void upload(String path){
		String temp = readXML(CONSTANT.INFO_DATA, CONSTANT.INFO_DATA_USERS);
		JsonObject json = new JsonParser().parse(temp).getAsJsonObject();
		Gson gson = new GsonBuilder().create();
		Users user = gson.fromJson(json, Users.class);
		String url = "/AppPicture_uploadPictureAction.action";
		json = new JsonObject();
		json.addProperty(CONSTANT.TOKEN, user.getToken());
		
		RequestParams params = new RequestParams();
		params.put(CONSTANT.DATA, json.toString());
		try {
			params.put("image", new File(path));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
					shortToastHandler("亲，照片已经上传到您的云相册！");
					getUrl();
				} else {
					shortToastHandler(CONSTANT.OTHER_ERROR);
				}
			}
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				shortToastHandler(CONSTANT.OTHER_ERROR);
			}
		});
	}

}
