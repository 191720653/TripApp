package com.zehao.tripapp;

import com.baidu.mapapi.SDKInitializer;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import android.app.Application;

public class TripApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		
		SDKInitializer.initialize(this);

		ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(
				getApplicationContext())
				.memoryCacheExtraOptions(480, 800) // max width, max height
				.threadPoolSize(3) // 线程池内加载的数量
				.threadPriority(Thread.NORM_PRIORITY - 2) // 降低线程的优先级保证主UI线程不受太大影响
				.denyCacheImageMultipleSizesInMemory()
				.tasksProcessingOrder(QueueProcessingType.FIFO)
				.memoryCache(new LruMemoryCache(8 * 1024 * 1024)) // 建议内存设在5-10M,可以有比较好的表现
				.memoryCacheSize(8 * 1024 * 1024)
				.diskCacheSize(512 * 1024 * 1024)
				.diskCacheFileCount(100) // 缓存的文件数量
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				.imageDownloader( new BaseImageDownloader(getApplicationContext(),
						5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)
				.writeDebugLogs() // Remove for release app
				.build();

		ImageLoader.getInstance().init(configuration);
	}

}
