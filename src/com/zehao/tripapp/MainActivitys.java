package com.zehao.tripapp;

import java.util.List;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.zehao.base.BaseActivity;
import com.zehao.data.bean.Domine;
import com.zehao.data.bean.Employee;
import com.zehao.data.bean.IDataCallback;
import com.zehao.data.bean.MData;
import com.zehao.tripapp.R;
import com.zehao.tripapp.login.LoginActivity;
import com.zehao.tripapp.map.RouteBeforeActivity;
import com.zehao.tripapp.picture.WaterfallActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

public class MainActivitys extends BaseActivity implements
		IDataCallback<MData<? extends Domine>> {

	private Button route, share, image, picture, photo;
	private String image_path;

	@Override
	protected void initContentView(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		addActionBar();
		baseSetContentView(savedInstanceState, R.layout.activity_main);
		addLeftMenu(Boolean.TRUE);

		route = (Button) findViewById(R.id.route);
		share = (Button) findViewById(R.id.share);
		image = (Button) findViewById(R.id.image);
		picture = (Button) findViewById(R.id.picture);
		photo = (Button) findViewById(R.id.photo);
		route.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				goActivity(RouteBeforeActivity.class);
			}
		});
		share.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showShare();
			}
		});
		image.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.setType("image/*");
				intent.putExtra("crop", true);
				intent.putExtra("return-data", true);
				startActivityForResult(intent, 888888);
			}
		});
		picture.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				goActivity(PictureActivity.class);
				goActivity(WaterfallActivity.class);
			}
		});
		photo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				goActivity(TakePhotoActivity.class);
				goActivity(LoginActivity.class);
			}
		});
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

	private void showShare() {
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();

		// 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法
		// oks.setNotification(R.drawable.ic_launcher,
		// getString(R.string.app_name));
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle("Android程序测试分享");
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		// oks.setTitleUrl("http://sharesdk.cn");
		// text是分享文本，所有平台都需要这个字段
		oks.setText("我是程序测试图文分享！");
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		oks.setImagePath(image_path);// 确保SDcard下面存在此张图片/sdcard/test.jpg
		// url仅在微信（包括好友和朋友圈）中使用
		// oks.setUrl("http://sharesdk.cn");
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		// oks.setComment("我是测试评论文本");
		// site是分享此内容的网站名称，仅在QQ空间使用
		// oks.setSite(getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		// oks.setSiteUrl("http://sharesdk.cn");

		// 启动分享GUI
		oks.show(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			System.out.println("requestCode" + requestCode);
			if (requestCode == 888888) {
				Uri uri = data.getData();
				// ContentResolver contentResolver = this.getContentResolver();
				// Bitmap bitmap = BitmapFactory.decodeStream(contentResolver
				// .openInputStream(uri));
				String[] proj = { MediaStore.Images.Media.DATA };
				// 好像是android多媒体数据库的封装接口，具体的看Android文档
				@SuppressWarnings("deprecation")
				Cursor cursor = managedQuery(uri, proj, null, null, null);
				// 按我个人理解 这个是获得用户选择的图片的索引值
				int column_index = cursor
						.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				// 将光标移至开头 ，这个很重要，不小心很容易引起越界
				cursor.moveToFirst();
				// 最后根据索引值获取图片路径
				image_path = cursor.getString(column_index);
				System.out.println(image_path);
				shortToastHandler(image_path);
			}
		}
	}

	@Override
	public void setBaseNoTitle() {
		// TODO Auto-generated method stub
		// 不用系统自带ActionBar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

}
