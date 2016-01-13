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
package ru.truba.touchgallery.TouchView;
/**
 * 优化，在原来基础上集成ImageLoder加载网络图片
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.zehao.tripapp.R;

import ru.truba.touchgallery.TouchView.InputStreamWrapper.InputStreamProgressListener;

public class UrlTouchImageView extends RelativeLayout {
	protected ProgressBar mProgressBar;
	protected TouchImageView mImageView;

	protected Context mContext;
	// 初始化图片加载库
	DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
			.cacheOnDisk(true)
			// 图片存本地
			.cacheInMemory(true).displayer(new FadeInBitmapDisplayer(10))
			.bitmapConfig(Bitmap.Config.RGB_565).build();

	public UrlTouchImageView(Context ctx) {
		super(ctx);
		mContext = ctx;
		init();

	}

	public UrlTouchImageView(Context ctx, AttributeSet attrs) {
		super(ctx, attrs);
		mContext = ctx;
		init();
	}

	public TouchImageView getImageView() {
		return mImageView;
	}

	@SuppressWarnings("deprecation")
	protected void init() {
		mImageView = new TouchImageView(mContext);
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		mImageView.setLayoutParams(params);
		this.addView(mImageView);
		mImageView.setVisibility(GONE);

		mProgressBar = new ProgressBar(mContext);// , null,
													// android.R.attr.progressBarStyleHorizontal
		params = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_VERTICAL);
		params.setMargins(30, 0, 30, 0);
		mProgressBar.setLayoutParams(params);
		// mProgressBar.setIndeterminate(false);
		// mProgressBar.setMax(100);
		this.addView(mProgressBar);
	}

	public void setUrl(String imageUrl) {
		// new ImageLoadTask().execute(imageUrl);
		ImageLoader.getInstance().displayImage(imageUrl, mImageView,
				defaultOptions, new ImageLoadingListener() {
					@Override
					public void onLoadingCancelled(String arg0, View arg1) {
						// TODO Auto-generated method stub
						// mImageView.setScaleType(ScaleType.CENTER);
						// Bitmap bitmap =
						// BitmapFactory.decodeResource(getResources(),
						// R.drawable.no_photo);
						// mImageView.setImageBitmap(bitmap);
						// mImageView.setVisibility(VISIBLE);
						// mProgressBar.setVisibility(GONE);
					}

					@Override
					public void onLoadingComplete(String arg0, View arg1,
							Bitmap arg2) {
						// TODO Auto-generated method stub
						mImageView.setScaleType(ScaleType.MATRIX);
						mImageView.setImageBitmap(arg2);
						mImageView.setVisibility(VISIBLE);
						mProgressBar.setVisibility(GONE);
					}

					@Override
					public void onLoadingFailed(String arg0, View arg1,
							FailReason arg2) {
						// TODO Auto-generated method stub
						mImageView.setScaleType(ScaleType.CENTER);
						Bitmap bitmap = BitmapFactory.decodeResource(
								getResources(), R.drawable.no_photo);
						mImageView.setImageBitmap(bitmap);
						mImageView.setVisibility(VISIBLE);
						mProgressBar.setVisibility(GONE);
					}

					@Override
					public void onLoadingStarted(String arg0, View arg1) {
						// TODO Auto-generated method stub

					}
				});
	}

	public void setScaleType(ScaleType scaleType) {
		mImageView.setScaleType(scaleType);
	}

	// No caching load
	public class ImageLoadTask extends AsyncTask<String, Integer, Bitmap> {
		@Override
		protected Bitmap doInBackground(String... strings) {
			String url = strings[0];
			Bitmap bm = null;
			try {
				URL aURL = new URL(url);
				URLConnection conn = aURL.openConnection();
				conn.connect();
				InputStream is = conn.getInputStream();
				int totalLen = conn.getContentLength();
				InputStreamWrapper bis = new InputStreamWrapper(is, 8192,
						totalLen);
				bis.setProgressListener(new InputStreamProgressListener() {
					@Override
					public void onProgress(float progressValue,
							long bytesLoaded, long bytesTotal) {
						publishProgress((int) (progressValue * 100));
					}
				});
				bm = BitmapFactory.decodeStream(bis);
				bis.close();
				is.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return bm;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (bitmap == null) {
				mImageView.setScaleType(ScaleType.CENTER);
				bitmap = BitmapFactory.decodeResource(getResources(),
						R.drawable.no_photo);
				mImageView.setImageBitmap(bitmap);
			} else {
				mImageView.setScaleType(ScaleType.MATRIX);
				mImageView.setImageBitmap(bitmap);
			}
			mImageView.setVisibility(VISIBLE);
			mProgressBar.setVisibility(GONE);
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			mProgressBar.setProgress(values[0]);
		}
	}
}
