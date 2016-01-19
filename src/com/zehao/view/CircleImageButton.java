package com.zehao.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * < 自定义的圆形ImageButton >
 * @ClassName: CircleImageButton
 * @author pc-hao
 * @date 2015年4月26日 下午1:29:08
 * @version V 1.0
 */

@SuppressLint("DrawAllocation")
public class CircleImageButton extends ImageView {
	
	private Paint paint;

	public CircleImageButton(Context context) {
		this(context, null);
	}

	public CircleImageButton(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CircleImageButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		paint = new Paint();

	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {

		Drawable drawable = getDrawable();
		if (null != drawable) {
			Bitmap original = ((BitmapDrawable) drawable).getBitmap();
			Bitmap change = getCircleBitmap(original, 14);
			final Rect rectSrc = new Rect(0, 0, change.getWidth(), change.getHeight());
			final Rect rectDest = new Rect(0, 0, getWidth(), getHeight());
			paint.reset();
			canvas.drawBitmap(change, rectSrc, rectDest, paint);

		} else {
			super.onDraw(canvas);
		}
	}

	private Bitmap getCircleBitmap(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		int x = bitmap.getWidth();

		canvas.drawCircle(x / 2, x / 2, x / 2, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;

	}
}