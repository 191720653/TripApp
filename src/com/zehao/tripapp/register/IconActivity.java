/*
 * 官网地站:http://www.mob.com
 * 技术支持QQ: 4006852216
 * 官方微信:ShareSDK   （如果发布新版本的话，我们将会第一时间通过微信将版本更新内容推送给您。如果使用过程中有任何问题，也可以通过微信与我们取得联系，我们将会在24小时内给予回复）
 * 
 * Copyright (c) 2013年 mob.com. All rights reserved.
 */

package com.zehao.tripapp.register;

import com.zehao.base.BaseActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

/** 查看编辑页面中图片的例子 */
public class IconActivity extends BaseActivity implements OnClickListener {
	private ImageView imageView;
	private Bitmap bitmap;
	private Uri pictureUri;
	private String picturePath;

	/** 设置图片用于浏览 */
	@Deprecated
	public void setImageBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
		if (imageView != null) {
			imageView.setImageBitmap(bitmap);
		}
	}

	/** 设置图片用于浏览 */
	public void setImagePath(String path) {
		if (!TextUtils.isEmpty(path)) {
			picturePath = path;
			pictureUri = Uri.parse(path);
			if (imageView != null) {
				//ivViewer.setImageURI(pictureUri);
				imageView.setImageBitmap(compressImageFromFile(picturePath));
			}
		}
	}
	
	@Override
	protected void initContentView(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		imageView = new ImageView(this);
		imageView.setScaleType(ScaleType.FIT_CENTER);
		imageView.setBackgroundColor(0xc0000000);
		setImagePath(getIntent().getExtras().getString("icon_path"));
		imageView.setOnClickListener(this);
		setContentView(imageView);
		if (bitmap != null && !bitmap.isRecycled()) {
			imageView.setImageBitmap(bitmap);
		} else if(!TextUtils.isEmpty(picturePath)){
			imageView.setImageBitmap(compressImageFromFile(picturePath));
		} else if (pictureUri != null && !TextUtils.isEmpty(pictureUri.getPath())) {
			//ivViewer.setImageURI(pictureUri);
			imageView.setImageBitmap(compressImageFromFile(pictureUri.getPath()));
		}
	}

	public void onClick(View v) {
		finish();
	}

	// 图片压缩
	private Bitmap compressImageFromFile(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true;// 只读边,不读内容
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
		newOpts.inSampleSize = be;// 设置采样率

		newOpts.inPreferredConfig = Config.ARGB_8888;// 该模式是默认的,可不设
		newOpts.inPurgeable = true;// 同时设置才会有效
		newOpts.inInputShareable = true;// 。当系统内存不够时候图片自动被回收

		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		// return compressBmpFromBmp(bitmap);//原来的方法调用了这个方法企图进行二次压缩
		// 其实是无效的,大家尽管尝试
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
		
	}
}
