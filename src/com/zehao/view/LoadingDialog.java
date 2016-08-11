package com.zehao.view;

import com.zehao.tripapp.R;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

public class LoadingDialog extends Dialog {
	
	public LoadingDialog(Context context) {
		super(context, R.style.DialogLoading);
		init();
	}

	private void init() {
		View contentView = View.inflate(getContext(), R.layout.loding_dialog_layout, null);
		setContentView(contentView);
		
		contentView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		getWindow().setWindowAnimations(R.anim.unzoom_in);
	}
	
	@Override
	public void dismiss() {
		getWindow().setWindowAnimations(R.anim.unzoom_out);
		super.dismiss();
	}
	
	@Override
	public void setTitle(int titleId) {
		setTitle(getContext().getString(titleId));
	}
}
