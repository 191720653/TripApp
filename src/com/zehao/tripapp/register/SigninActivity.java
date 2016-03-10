package com.zehao.tripapp.register;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.regex.Pattern;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zehao.base.BaseActivity;
import com.zehao.constant.CONSTANT;
import com.zehao.data.bean.Users;
import com.zehao.http.HttpCLient;
import com.zehao.tripapp.R;
import com.zehao.util.Tool;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;

public class SigninActivity extends BaseActivity implements OnClickListener {

	/** 用户信息对话框*/
	private enum ChangeUserType {USER_ACCOUNT, USER_PASSWORD, USER_NOTE, USER_NAME};
	/**加载用户头像*/
	private static final int LOAD_USER_ICON = 2;
	/**Toast 提升*/
	private static final int MSG_SHOW_TOAST = 3;
	/**打开相册，并截图*/
	private static final int INTENT_ACTION_PICTURE = 0;
	/**打开相机照相*/
	private static final int INTENT_ACTION_CAREMA = 1;
	/**照相后，截图*/
	private static final int INTENT_ACTION_CROP = 2;
	/**图片名字*/
	private static final String PICTURE_NAME = "userIcon.jpg";
	
	private ImageView ivUserIcon;
	private TextView tvUserName, tvUserGender, tvUserNote, tvUserAccount, tvUserPassword;
	private Platform platform;
	private String pname = null;
	
	private String picturePath;
	// private UserInfo userInfo = new UserInfo();
	private Users users = new Users();

	private ProgressDialog progressDialog;
	
//	private Button back,register;
	
	@Override
	protected void initContentView(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		// addActionBar();
		// ((TextView)findViewById(R.id.action_bar_title)).setText("用户信息");
		baseSetContentView(savedInstanceState, R.layout.activity_signup);
//		back = (Button) findViewById(R.id.action_bar_btn_first);
//		back.setText("返回");
//		register = (Button) findViewById(R.id.action_bar_btn_second);
//		register.setText(this.getString(R.string.register_userinfo_sign_up));
//		back.setOnClickListener(this);
//		register.setOnClickListener(this);

		tvUserName = (TextView) findViewById(R.id.tv_user_name);
		tvUserGender = (TextView) findViewById(R.id.tv_user_gender);
		tvUserNote = (TextView) findViewById(R.id.tv_user_note);
		tvUserAccount = (TextView) findViewById(R.id.tv_user_account);
		tvUserPassword = (TextView) findViewById(R.id.tv_user_password);
		ivUserIcon = (ImageView) findViewById(R.id.iv_user_icon);

		ivUserIcon.setOnClickListener(this);
		findViewById(R.id.rl_icon).setOnClickListener(this);
		findViewById(R.id.rl_name).setOnClickListener(this);
		findViewById(R.id.rl_gender).setOnClickListener(this);
		findViewById(R.id.rl_note).setOnClickListener(this);
		findViewById(R.id.rl_account).setOnClickListener(this);
		findViewById(R.id.rl_password).setOnClickListener(this);
		
		Bundle bundle = getIntent().getExtras();
		if(bundle!=null){
			pname = bundle.getString("platform");
			setRegisterUserinfoatform(pname);
			initData();
		}else{
			users.setType(CONSTANT.USER_TYPE_COMMON);
		}
		
		//初始化照片保存地址
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			String thumPicture = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+this.getPackageName()+"/download";
			File pictureParent =new File(thumPicture);	
			File pictureFile =new File(pictureParent, PICTURE_NAME);	
			
			if(!pictureParent.exists()){
				pictureParent.mkdirs();
			}
			try{
				if (!pictureFile.exists()) {
					pictureFile.createNewFile();
				}	
			}catch (Exception e) {
				e.printStackTrace();
			}
			picturePath = pictureFile.getAbsolutePath();
			Log.e("picturePath ==>>", picturePath);
		}else{
			Log.e("change user icon ==>>", "there is not sdcard!");
		}
			
	}

	public void setRegisterUserinfoatform(String platName) {
		platform = ShareSDK.getPlatform(platName);
		System.out.println(platName);
		System.out.println("platform.getName(): "+platform.getName());
	}
	
	/**初始化数据*/
	private void initData(){
		System.out.println(platform.getDb().exportData());
		if(platform != null){
			String gender = platform.getDb().getUserGender();
			if(gender.equals("0")){
				users.setSexs("男");
			}else{
				users.setSexs("女");
			}
			users.setIcon(platform.getDb().getUserIcon());
			users.setNickName(platform.getDb().getUserName());
			users.setTypeId(platform.getDb().getUserId());
			users.setType(pname);
			String acount = System.currentTimeMillis() + "";
			users.setAccount(acount);
			users.setPassword(acount);
		}
		
		tvUserName.setText(users.getNickName());
		tvUserGender.setText(users.getSexs());
		tvUserAccount.setText(users.getAccount());
		tvUserPassword.setText(users.getPassword());
		tvUserNote.setText(users.getRemark());
		// 加载头像
		if(!TextUtils.isEmpty(users.getIcon())){
			loadIcon();
		}
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.action_bar_btn_back:// 返回主界面
			this.finish();
			break;
		case R.id.action_bar_register:// 调用注册方法
			signIn();
			// handler.sendEmptyMessage(MSG_SHOW_TOAST);
			break;
		case R.id.rl_icon:
			showChangeIconDialog();
			// getPicture();
			break;
		case R.id.rl_name:
			showChangeInfo(ChangeUserType.USER_NAME);
			break;
		case R.id.rl_gender:
			showGerderDialog();
			break;
		case R.id.rl_note:
			showChangeInfo(ChangeUserType.USER_NOTE);
			break;
		case R.id.rl_account:
			showChangeInfo(ChangeUserType.USER_ACCOUNT);
			break;
		case R.id.rl_password:
			showChangeInfo(ChangeUserType.USER_PASSWORD);
			break;
		case R.id.iv_user_icon:
			Bundle bundle = new Bundle();
			bundle.putString("icon_path", picturePath);
			goActivity(IconActivity.class, bundle);
			break;
		default:
			break;
		}		
	}

	/**
	 * 加载头像
	 */
	public void loadIcon() {
		final String imageUrl = platform.getDb().getUserIcon();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					URL picUrl = new URL(imageUrl);
					Bitmap userIcon = BitmapFactory.decodeStream(picUrl.openStream());
			        FileOutputStream b = null;  	  
			        try {  
			        	b = new FileOutputStream(picturePath);  
			        	userIcon.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件  
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
			        users.setIcon(picturePath);

			        Message msg = new Message();
					msg.what = LOAD_USER_ICON;
					handler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == INTENT_ACTION_PICTURE && resultCode == Activity.RESULT_OK && null != data){
			Cursor c = getContentResolver().query(data.getData(), null, null, null, null);
			c.moveToNext();
			picturePath = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));
			c.close();
			System.out.println("onActivityResult == " + picturePath);
			if(new File(picturePath).exists()){
				System.out.println("onActivityResult == " + picturePath +" == exist");
				users.setIcon(picturePath);
				ivUserIcon.setImageBitmap(compressImageFromFile(picturePath));
				//ivUserIcon.setImageURI(Uri.parse(path));
				//ivUserIcon.setImageDrawable(Drawable.createFromPath(path));
			}
		}else if(requestCode == INTENT_ACTION_CAREMA && resultCode == Activity.RESULT_OK){
			System.out.println("INTENT_ACTION_CAREMA == " + picturePath );
			users.setIcon(picturePath);
			//ivUserIcon.setImageURI(Uri.parse(picturePath));
			ivUserIcon.setImageBitmap(compressImageFromFile(picturePath));
			//ivUserIcon.setImageDrawable(Drawable.createFromPath(picturePath));
		}else if(requestCode == INTENT_ACTION_CROP && resultCode == Activity.RESULT_OK && null != data){
			System.out.println("INTENT_ACTION_CROP == " + picturePath );
			ivUserIcon.setImageURI(Uri.parse(picturePath));
			// ivUserIcon.setImageDrawable(Drawable.createFromPath(picturePath));
			//ivUserIcon.setImageBitmap(compressImageFromFile(picturePath));
		}
	}
	
	/**修改图片*/
	private void showChangeIconDialog(){		
		final Dialog dialog = new Dialog(this, R.style.WhiteDialog);
		dialog.setContentView(R.layout.activity_signup_icon_select_dialog);
		dialog.findViewById(R.id.dialog_camera).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				openCamera();
			}
		});
		dialog.findViewById(R.id.dialog_picture).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				getPicture();
			}
		});
		dialog.findViewById(R.id.dialog_crop).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				Uri uri = Uri.fromFile(new File(picturePath));
				openCrop(uri);
			}
		});
		dialog.show();
	}
	
	/**从相册获取图片*/
	private void getPicture(){
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(intent, INTENT_ACTION_PICTURE); 
		//TODO
	}
	
	/**打开相机照相*/
	private void openCamera(){
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(picturePath)));
		intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
		startActivityForResult(intent, INTENT_ACTION_CAREMA);
	}
	
	/**剪裁方法*/
	private void openCrop(Uri uri){
		//TODO 裁剪方法，自己做
		Intent intent = new Intent("com.android.camera.action.CROP");
		   intent.setDataAndType(uri, "image/*");
		   intent.putExtra("crop", "true");//可裁剪
		   intent.putExtra("aspectX", 1);
		   intent.putExtra("aspectY", 1);
		   intent.putExtra("outputX", 100);
		   intent.putExtra("outputY", 100);
		   intent.putExtra("scale", true);
		   intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		   intent.putExtra("return-data", false);//若为false则表示不返回数据
		   intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		   intent.putExtra("noFaceDetection", true); 
		   startActivityForResult(intent, INTENT_ACTION_CROP);
	}
	
	/**
	 * gender select dialog,性别选择对话框
	 */
	private void showGerderDialog(){
		final Dialog dialog = new Dialog(this, R.style.WhiteDialog);
		dialog.setContentView(R.layout.activity_signup_gender_select_dialog);
		final ImageView ivBoy = (ImageView) dialog.findViewById(R.id.dialog_iv_boy);
		final ImageView ivGirl = (ImageView) dialog.findViewById(R.id.dialog_iv_girl);
		if(users.getSexs() == "男"){
			ivGirl.setVisibility(View.GONE);
			ivBoy.setVisibility(View.VISIBLE);
		}else{
			ivBoy.setVisibility(View.GONE);
			ivGirl.setVisibility(View.VISIBLE);
		}
		dialog.findViewById(R.id.rl_boy).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ivGirl.setVisibility(View.GONE);
				ivBoy.setVisibility(View.VISIBLE);
				tvUserGender.setText(R.string.register_userinfo_boy);
				users.setSexs("男");
				dialog.dismiss();
			}
		});
		dialog.findViewById(R.id.rl_girl).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				ivGirl.setVisibility(View.VISIBLE);
				ivBoy.setVisibility(View.GONE);
				tvUserGender.setText(R.string.register_userinfo_girl);
				users.setSexs("女");
				dialog.dismiss();
			}
		});
		dialog.show();
	}
	
	/**改变用户信息*/
	private void showChangeInfo(final ChangeUserType type){
		String title;
		String content;
		String hint;
		if(type == ChangeUserType.USER_NAME){
			content = tvUserName.getText().toString();
			title = getApplicationContext().getString(R.string.register_userinfo_change_user_name_title);
			hint = getApplicationContext().getString(R.string.register_userinfo_input_user_name_hint);
		}else if(type==ChangeUserType.USER_ACCOUNT){
			content = tvUserAccount.getText().toString();
			title = getApplicationContext().getString(R.string.register_userinfo_change_user_account_title);
			hint = getApplicationContext().getString(R.string.register_userinfo_input_user_account_hint);
		}else if(type==ChangeUserType.USER_PASSWORD){
			content = tvUserPassword.getText().toString();
			title = getApplicationContext().getString(R.string.register_userinfo_change_user_password_title);
			hint = getApplicationContext().getString(R.string.register_userinfo_input_user_password_hint);
		}else{
			content = tvUserNote.getText().toString();
			title = getApplicationContext().getString(R.string.register_userinfo_change_user_note_title);
			hint = getApplicationContext().getString(R.string.register_userinfo_input_user_note_hint);
		}

		View dlgView = View.inflate(this, R.layout.activity_signup_change_userinfo_dialog, null);
		LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
		final Dialog dialog = new Dialog(this, R.style.WhiteDialog);
		dialog.setContentView(dlgView, layoutParams);
		final TextView tvTitle = (TextView) dialog.findViewById(R.id.dialog_tv_title);
		final EditText etInfo = (EditText) dialog.findViewById(R.id.dialog_ev_info);
		final TextView tvHint = (TextView) dialog.findViewById(R.id.dialog_tv_hint);
		tvTitle.setText(title);
		etInfo.setHint(content);
		tvHint.setText(hint);
		dialog.findViewById(R.id.dialog_btn_save).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {	
				String content = etInfo.getText().toString();
				if(type == ChangeUserType.USER_NAME){
					tvUserName.setText(content);
					users.setNickName(content);
				}else if(type == ChangeUserType.USER_ACCOUNT){
					tvUserAccount.setText(content);
					users.setAccount(content);
				}else if(type == ChangeUserType.USER_PASSWORD){
					tvUserPassword.setText(content);
					users.setPassword(content);
				}else{
					tvUserNote.setText(content);
					users.setRemark(content);
				}
				dialog.dismiss();
			}
		});
		dialog.show();
	}
	
	//图片压缩
	private Bitmap compressImageFromFile(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true;//只读边,不读内容
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		float hh = 800f;//
		float ww = 480f;//
		int be = 1;
		if (w > h && w > ww) {
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;//设置采样率
		
		newOpts.inPreferredConfig = Config.ARGB_8888;//该模式是默认的,可不设
		newOpts.inPurgeable = true;// 同时设置才会有效
		newOpts.inInputShareable = true;//。当系统内存不够时候图片自动被回收
		
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
//		return compressBmpFromBmp(bitmap);//原来的方法调用了这个方法企图进行二次压缩
									//其实是无效的,大家尽管尝试
		return bitmap;
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
		switch (msg.what) {
		case CONSTANT.ACTION_SHOW_DIALOG: 
			progressDialog = ProgressDialog.show(this, "注册", "正在联网注册,请稍候......");
			break;
		case CONSTANT.ACTION_DISMISS_DIALOG: 
			progressDialog.dismiss();
			break;
		case LOAD_USER_ICON:
			ivUserIcon.setImageURI(Uri.parse(picturePath));
			break;
		case MSG_SHOW_TOAST:
			// 执行注册
			System.out.println(users.toString());
			shortToastHandler("注册成功！");
			finish();
			break;
		default:
			break;
		}
	}
	
	public void signIn(){
		if(CONSTANT.NULL_STRING.equals(Tool.NVL(users.getAccount()))
				||CONSTANT.NULL_STRING.equals(Tool.NVL(users.getPassword()))){
			shortToastHandler("请输入账号、密码！");
		}else{
			if(!Pattern.matches("^[0-9a-zA-Z]{8,12}", users.getAccount())
					||!Pattern.matches("^[0-9a-zA-Z]{8,12}", users.getPassword())){
				shortToastHandler("请填写8到12位的纯数字、字母或数字字母组合的账号、密码！");
			}else{
				if(pname==null) users.setTypeId(users.getAccount());
				String url = "/AppSignIn_registerAction.action";
				JsonObject json = new JsonObject();
				json.addProperty(CONSTANT.ACCOUNT, users.getAccount());
				json.addProperty(CONSTANT.PASSWORD, users.getPassword());
				json.addProperty(CONSTANT.ICON, CONSTANT.NULL_STRING);
				json.addProperty(CONSTANT.NICK_NAME, users.getNickName());
				json.addProperty(CONSTANT.SEX, users.getSexs());
				json.addProperty(CONSTANT.REMARK, users.getRemark());
				json.addProperty(CONSTANT.TYPE, users.getType());
				json.addProperty(CONSTANT.USER_TYPE_ID, users.getTypeId());
				
				RequestParams params = new RequestParams();
				params.put(CONSTANT.DATA, json);
				
				if(!CONSTANT.NULL_STRING.equals(users.getIcon())){
					System.out.println("用户头像路径: " + users.getIcon());
					try {
						params.put("image", new File(picturePath));
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						System.out.println("用户头像不存在！");
					}
				}
				System.out.println("App发送数据：" + json.toString());
				handler.sendEmptyMessage(CONSTANT.ACTION_SHOW_DIALOG);
				HttpCLient.post(url, params, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						// TODO Auto-generated method stub
						JsonObject json = (JsonObject) new JsonParser().parse(new String(arg2));
						System.out.println("服务器返回数据：" + json);
						String errorCode = json.get(CONSTANT.ERRCODE).getAsString();
						if(CONSTANT.CODE_168.equals(errorCode)){
							Users user = new Gson().fromJson(json, Users.class);
							System.out.println("返回结果转Uers：" + user.toString());
							writeXML(CONSTANT.INFO_DATA, CONSTANT.INFO_DATA_USERS, json.toString());
							// 注册成功，跳到登录界面
							finish();
						}else if(CONSTANT.CODE_176.equals(errorCode)){
							shortToastHandler(CONSTANT.CODE_176_TEXT);
						}else{
							shortToastHandler(CONSTANT.OTHER_ERROR);
						}
						handler.sendEmptyMessage(CONSTANT.ACTION_DISMISS_DIALOG);
					}
					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						// TODO Auto-generated method stub
						shortToastHandler(CONSTANT.OTHER_ERROR);
						handler.sendEmptyMessage(CONSTANT.ACTION_DISMISS_DIALOG);
					}
				});
			}
		}
	}
}
