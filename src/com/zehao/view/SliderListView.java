package com.zehao.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class SliderListView extends ListView {

	public SliderListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public SliderListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public SliderListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected boolean overScrollBy(int deltaX, int deltaY, int scrollX,
			int scrollY, int scrollRangeX, int scrollRangeY,
			int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
		// TODO Auto-generated method stub
		return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX,
				scrollRangeY, maxOverScrollX, 300, isTouchEvent);
	}

}
