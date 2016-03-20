/*
 Copyright (c) 2012 Roman Truba

 Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 permit persons to whom the Software is furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all copies or substantial
 portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH
 THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.zehao.tripapp.picture;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import ru.truba.touchgallery.GalleryWidget.BasePagerAdapter.OnItemChangeListener;
import ru.truba.touchgallery.GalleryWidget.GalleryViewPager;
import ru.truba.touchgallery.GalleryWidget.UrlPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zehao.constant.CONSTANT;
import com.zehao.tripapp.R;
import com.zehao.util.FileUtil;

public class GalleryUrlActivity extends Activity implements OnClickListener {

    private GalleryViewPager mViewPager;
    
    private List<String> items = null;
    /**
	 * 显示当前图片的页数
	 */
	private TextView pageText;
	private static final String STATE_POSITION = "STATE_POSITION";
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 去掉标题栏以及状态栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags( WindowManager .LayoutParams.FLAG_FULLSCREEN,
				WindowManager .LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_gallery_image);
        
        String temp = readXML(CONSTANT.INFO_DATA, CONSTANT.INFO_PHOTO_URL);
        JsonArray urls = new JsonParser().parse(temp).getAsJsonArray();
        items = new ArrayList<String>();
        for(int i=0;i<urls.size();i++){
        	items.add(CONSTANT.BASE_ROOT_URL + urls.get(i).getAsString().replaceFirst(".", ""));
		}
        
        Bundle bundle = getIntent().getExtras();
		// 当前显示View的位置
		int pagerPosition = bundle.getInt("position", 0);

		// 如果之前有保存用户数据
		if (savedInstanceState != null) {
			pagerPosition = savedInstanceState.getInt(STATE_POSITION);
		}

		pageText = (TextView) findViewById(R.id.page_text);
		// 设定当前的页数和总页数
		pageText.setText((pagerPosition + 1) + "/" + urls.size());

        UrlPagerAdapter pagerAdapter = new UrlPagerAdapter(this, items);
        pagerAdapter.setOnItemChangeListener(new OnItemChangeListener()
		{
			@Override
			public void onItemChange(int currentPosition)
			{
				pageText.setText((currentPosition + 1) + "/" + items.size());
				Toast.makeText(GalleryUrlActivity.this, "Current item is " + currentPosition, Toast.LENGTH_SHORT).show();
			}
		});
        
        mViewPager = (GalleryViewPager)findViewById(R.id.viewer);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setCurrentItem(pagerPosition);
        
    }
    
    @Override
	public void onSaveInstanceState(Bundle outState) {
		// 保存用户数据
		outState.putInt(STATE_POSITION, mViewPager.getCurrentItem());
	}
    
    /**
	 * 获取xml文件键值
	 * 
	 * @param name
	 * @param key
	 * @return
	 */
	public String readXML(String name, String key) {
		SharedPreferences settings = getSharedPreferences(name, Context.MODE_PRIVATE);
		if (settings.contains(key)) {
			return settings.getString(key, null);
		} else {
			return null;
		}
	}
	
	/**
	 * 弹出短时间Toast
	 * 
	 * @param text
	 */
	public void shortToastHandler(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.action_bar_btn_share: {
			shortToastHandler("开始分享");
			showShare();
			break;
		}
		case R.id.action_bar_btn_back:{
			shortToastHandler("返回相册");
			finish();
			break;
			}
		default:
			break;
		}
	}
	
	private void showShare() {
		@SuppressWarnings("deprecation")
		String oldPath = ImageLoader.getInstance().getDiscCache().get(items.get(mViewPager.getCurrentItem())).getPath();
		String newPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + this.getPackageName() + "/download/" + "temp.jpg";
		System.out.println(newPath);
		FileUtil.copy(oldPath, newPath);
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();
		// 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法
		// oks.setNotification(R.drawable.ic_launcher,
		// getString(R.string.app_name));
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		// oks.setTitle(fragment.getViews().getViewName());
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		// oks.setTitleUrl("http://sharesdk.cn");
		// text是分享文本，所有平台都需要这个字段
		oks.setText("说点什么吧... ...");
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		oks.setImagePath(newPath);// 确保SDcard下面存在此张图片/sdcard/test.jpg //fragment.getUrls().get(0)
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
}