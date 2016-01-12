package com.zehao.view;

import java.util.ArrayList;
import java.util.List;

import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.zehao.constant.CONSTANT;
import com.zehao.tripapp.R;
import com.zehao.tripapp.picture.ImagePagerActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

@SuppressLint({ "NewApi", "ClickableViewAccessibility" })
public class PhotoScrollView extends ScrollView implements OnTouchListener {

	/**
	 * 每页要加载的图片数量
	 */
	public static final int PAGE_SIZE = 15;

	/**
	 * 记录当前已加载到第几页
	 */
	private int page;

	/**
	 * 每一列的宽度
	 */
	private int columnWidth;

	/**
	 * 当前第一列的高度
	 */
	private int firstColumnHeight;

	/**
	 * 当前第二列的高度
	 */
	private int secondColumnHeight;

	/**
	 * 当前第三列的高度
	 */
	private int thirdColumnHeight;

	/**
	 * 是否已加载过一次layout，这里onLayout中的初始化只需加载一次
	 */
	private boolean loadOnce = false;

	/**
	 * 第一列的布局
	 */
	private LinearLayout firstColumn;

	/**
	 * 第二列的布局
	 */
	private LinearLayout secondColumn;

	/**
	 * 第三列的布局
	 */
	private LinearLayout thirdColumn;

	/**
	 * MyScrollView下的直接子布局。
	 */
	private static View scrollLayout;

	/**
	 * MyScrollView布局的高度。
	 */
	private static int scrollViewHeight;

	/**
	 * 记录上垂直方向的滚动距离。
	 */
	private static int lastScrollY = -1;

	/**
	 * 记录所有界面上的图片，用以可以随时控制对图片的释放。
	 */
	private List<ImageView> imageViewList = new ArrayList<ImageView>();
	
	private String[] imageUrls = CONSTANT.IMAGES;

	/**
	 * 在Handler中进行图片可见性检查的判断，以及加载更多图片的操作。
	 */
	private static Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			PhotoScrollView myScrollView = (PhotoScrollView) msg.obj;
			int scrollY = myScrollView.getScrollY();
			// 如果当前的滚动位置和上次相同，表示已停止滚动
			if (scrollY == lastScrollY) {
				// 当滚动的最底部，并且当前没有正在下载的任务时，开始加载下一页的图片
				if (scrollViewHeight + scrollY >= scrollLayout.getHeight()) {
					myScrollView.loadMoreImages();
				}
				myScrollView.checkVisibility();
			} else {
				lastScrollY = scrollY;
				Message message = new Message();
				message.obj = myScrollView;
				// 5毫秒后再次对滚动位置进行判断
				handler.sendMessageDelayed(message, 5);
			}
		};
	};

	/**
	 * MyScrollView的构造函数。
	 * 
	 * @param context
	 * @param attrs
	 */
	public PhotoScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		Toast.makeText(context, "MyScrollView的构造函数", Toast.LENGTH_LONG).show();
		this.context = context;
		setOnTouchListener(this);
		init();
	}

	/**
	 * 进行一些关键性的初始化操作，获取MyScrollView的高度，以及得到第一列的宽度值。并在这里开始加载第一页的图片。
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		Toast.makeText(context, changed + "进行一些关键性的初始化操作" + loadOnce, Toast.LENGTH_LONG).show();
		if (changed && !loadOnce) {
			scrollViewHeight = getHeight();
			scrollLayout = getChildAt(0);
			firstColumn = (LinearLayout) findViewById(R.id.first_column);
			secondColumn = (LinearLayout) findViewById(R.id.second_column);
			thirdColumn = (LinearLayout) findViewById(R.id.third_column);
			columnWidth = firstColumn.getWidth();
			loadOnce = true;
			loadMoreImages();
			Toast.makeText(context, "loadMoreImages", Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * 监听用户的触屏事件，如果用户手指离开屏幕则开始进行滚动检测。
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			Message message = new Message();
			message.obj = this;
			handler.sendMessageDelayed(message, 5);
		}
		return false;
	}
	
	/** 
     * 最小的滑动距离 
     */  
    private static final int SCROLLLIMIT = 40; 

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		// TODO Auto-generated method stub
		super.onScrollChanged(l, t, oldl, oldt);
		if (oldt > t && oldt - t > SCROLLLIMIT) {// 向下
			
        } else if (oldt < t && t - oldt > SCROLLLIMIT) {// 向上
        	
        }
	}

	/**
	 * 开始加载下一页的图片。
	 */
	public void loadMoreImages() {
		if (hasSDCard()) {
			int startIndex = page * PAGE_SIZE;
			int endIndex = page * PAGE_SIZE + PAGE_SIZE;
			if (startIndex < imageUrls.length) {
				Toast.makeText(getContext(), "正在加载...", Toast.LENGTH_SHORT)
						.show();
				if (endIndex > imageUrls.length) {
					endIndex = imageUrls.length;
				}
				for (int i = startIndex; i < endIndex; i++) {
					ImageView  view = new ImageView(getContext());
					view.setTag(R.string.image_position, i);
					imageViewList.add(view);
					show(i, view);
				}
				page++;
			} else {
				Toast.makeText(getContext(), "已没有更多图片", Toast.LENGTH_SHORT)
						.show();
			}
		} else {
			Toast.makeText(getContext(), "未发现SD卡", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 遍历imageViewList中的每张图片，对图片的可见性进行检查，如果图片已经离开屏幕可见范围，则将图片替换成一张空图。
	 */
	public void checkVisibility() {
		for (int i = 0; i < imageViewList.size(); i++) {
			ImageView imageView = imageViewList.get(i);
			if(imageView.getTag(R.string.border_top)==null){break;}
			int borderTop = (Integer) imageView.getTag(R.string.border_top);
			int borderBottom = (Integer) imageView.getTag(R.string.border_bottom);
			if (borderBottom > getScrollY() && borderTop < getScrollY() + scrollViewHeight) {
				String imageUrl = (String) imageView.getTag(R.string.image_url);
				ImageLoader.getInstance().displayImage(imageUrl, imageView, options);
			} else {
				imageView.setImageResource(R.drawable.ic_launcher);
			}
		}
	}
	
	public void show(int position, ImageView view){
		ImageLoader.getInstance().displayImage(imageUrls[position], view, options, new ImageLoadingListener() {
			@Override
			public void onLoadingCancelled(String arg0, View arg1) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
				// TODO Auto-generated method stub
				double ratio = arg2.getWidth() / (columnWidth * 1.0);
				int scaledHeight = (int) (arg2.getHeight() / ratio);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(columnWidth,  
						scaledHeight);
				ImageView  imageView = (ImageView) arg1;
                imageView.setLayoutParams(params);
                imageView.setImageBitmap(arg2);
                imageView.setScaleType(ScaleType.FIT_XY);  
                imageView.setPadding(5, 5, 5, 5);
                
                imageView.setTag(R.string.image_url, imageUrls[(Integer) imageView.getTag(R.string.image_position)]); 

                imageView.setOnClickListener(new OnClickListener() {  
                    @Override  
                    public void onClick(View v) {  
                        Intent intent = new Intent(getContext(), ImagePagerActivity.class);  
                        intent.putExtra("image_position", (Integer) v.getTag(R.string.image_position));  
                        getContext().startActivity(intent);  
                    }  
                });
                findColumnToAdd(imageView, scaledHeight).addView(imageView);
                imageViewList.add((Integer) imageView.getTag(R.string.image_position), imageView);
			}
			@Override
			public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void onLoadingStarted(String arg0, View arg1) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	/**
	 * 判断手机是否有SD卡。
	 * 
	 * @return 有SD卡返回true，没有返回false。
	 */
	private boolean hasSDCard() {
		return Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState());
	}

//	/**
//	 * 异步下载图片的任务。
//	 * 
//	 * @author guolin
//	 */
//	class LoadImageTask extends AsyncTask<Integer, Void, Bitmap> {
//		/**
//		 * 图片的URL地址
//		 */
//		private String mImageUrl;
//		/** 
//         * 记录每个图片对应的位置 
//         */  
//        private int mItemPosition;
//		public LoadImageTask() {
//		}
//		@Override
//		protected Bitmap doInBackground(Integer... params) {
//			mItemPosition = params[0];
//			mImageUrl = Images.imageUrls[mItemPosition];  
//            Bitmap imageBitmap = imageLoader.getBitmapFromMemoryCache(mImageUrl);  
//            if (imageBitmap == null) {  
//                imageBitmap = loadImage(mImageUrl);  
//            }  
//			return imageBitmap;
//		}
//		@Override
//		protected void onPostExecute(Bitmap bitmap) {
//			if (bitmap != null) {
//				double ratio = bitmap.getWidth() / (columnWidth * 1.0);
//				int scaledHeight = (int) (bitmap.getHeight() / ratio);
//				addImage(bitmap, columnWidth, scaledHeight);
//			}
//			taskCollection.remove(this);
//		}
//		/** 
//         * 向ImageView中添加一张图片 
//         */  
//        private void addImage(Bitmap bitmap, int imageWidth, int imageHeight) {  
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(imageWidth,  
//                    imageHeight);  
//            if (mImageView != null) {  
//                mImageView.setImageBitmap(bitmap);  
//            } else {  
//                ImageView imageView = new ImageView(getContext());  
//                imageView.setLayoutParams(params);  
//                imageView.setImageBitmap(bitmap);  
//                imageView.setScaleType(ScaleType.FIT_XY);  
//                imageView.setPadding(5, 5, 5, 5);  
//                imageView.setTag(R.string.image_url, mImageUrl);  
//                imageView.setOnClickListener(new OnClickListener() {  
//                    @Override  
//                    public void onClick(View v) {  
//                        Intent intent = new Intent(getContext(), ImageDetailActivity.class);  
//                        intent.putExtra("image_position", mItemPosition);  
//                        getContext().startActivity(intent);  
//                    }  
//                });  
//                findColumnToAdd(imageView, imageHeight).addView(imageView);  
//                imageViewList.add(imageView);  
//            }  
//        }
//	}
	
	/**
	 * 找到此时应该添加图片的一列。原则就是对三列的高度进行判断，当前高度最小的一列就是应该添加的一列。
	 * 
	 * @param imageView
	 * @param imageHeight
	 * @return 应该添加图片的一列
	 */
	private LinearLayout findColumnToAdd(ImageView imageView,
			int imageHeight) {
		if (firstColumnHeight <= secondColumnHeight) {
			if (firstColumnHeight <= thirdColumnHeight) {
				imageView.setTag(R.string.border_top, firstColumnHeight);
				firstColumnHeight += imageHeight;
				imageView.setTag(R.string.border_bottom, firstColumnHeight);
				return firstColumn;
			}
			imageView.setTag(R.string.border_top, thirdColumnHeight);
			thirdColumnHeight += imageHeight;
			imageView.setTag(R.string.border_bottom, thirdColumnHeight);
			return thirdColumn;
		} else {
			if (secondColumnHeight <= thirdColumnHeight) {
				imageView.setTag(R.string.border_top, secondColumnHeight);
				secondColumnHeight += imageHeight;
				imageView
						.setTag(R.string.border_bottom, secondColumnHeight);
				return secondColumn;
			}
			imageView.setTag(R.string.border_top, thirdColumnHeight);
			thirdColumnHeight += imageHeight;
			imageView.setTag(R.string.border_bottom, thirdColumnHeight);
			return thirdColumn;
		}
	}
	
	private Context context;
	DisplayImageOptions options; // 显示图片的设置
	@SuppressWarnings("deprecation")
	public void init(){
		
		ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(getContext())
				.memoryCacheExtraOptions(480, 800) // max width, max height
				.threadPoolSize(3) // 线程池内加载的数量
				.threadPriority(Thread.NORM_PRIORITY - 2) // 降低线程的优先级保证主UI线程不受太大影响
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new LruMemoryCache(5 * 1024 * 1024)) // 建议内存设在5-10M,可以有比较好的表现
				.memoryCacheSize(5 * 1024 * 1024)
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.diskCacheSize(50 * 1024 * 1024)
				.diskCacheFileCount(100) // 缓存的文件数量
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				.imageDownloader( new BaseImageDownloader(getContext(),
						5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)
				.writeDebugLogs() // Remove for release app
				.build();

		ImageLoader.getInstance().init(configuration);
		
		options = new DisplayImageOptions.Builder()
			//	.showStubImage(R.drawable.ic_stub)
				.showImageForEmptyUri(R.drawable.ic_empty)
				.showImageOnFail(R.drawable.ic_error)
				.cacheInMemory(true)
				.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565) // 设置图片的解码类型
				.build();
	}

}