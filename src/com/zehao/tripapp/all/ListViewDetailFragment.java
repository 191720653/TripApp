package com.zehao.tripapp.all;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.achep.header2actionbar.HeaderFragment;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.BaseSliderView.OnSliderClickListener;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.zehao.constant.CONSTANT;
import com.zehao.data.bean.Other;
import com.zehao.tripapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint("InflateParams")
public class ListViewDetailFragment extends HeaderFragment implements OnSliderClickListener {

	private ListView mListView;
	private TextView viewName, viewInfo, likeNum;

	private ProgressBar progressBar;

	private View mainView;
	private Other other = null;
	public Other getOther(){return other;}
	public Integer getOtherId(){
		return other==null?0:other.getId();
	}
	public void setLikeNum(String num){
		likeNum.setText(num + "人赞过");
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		setHeaderBackgroundScrollMode(HEADER_BACKGROUND_SCROLL_PARALLAX);
		setOnHeaderScrollChangedListener(new OnHeaderScrollChangedListener() {
			@Override
			public void onHeaderScrollChanged(float progress, int height,
					int scroll) {
				height -= getActivity().getActionBar().getHeight();
				progress = (float) scroll / height;
				if (progress > 1f)
					progress = 1f;
				progress = (1 - (float) Math.cos(progress * Math.PI)) * 0.5f;
				((AllDetailActivity) getActivity()).getFadingActionBarHelper()
						.setActionBarAlpha((int) (255 * progress));
			}
		});

	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	@Override
	public View onCreateHeaderView(LayoutInflater inflater, ViewGroup container) {
		mainView = inflater.inflate(R.layout.activity_view_alldetail_header,
				container, false);
		Bundle bundle = getActivity().getIntent().getExtras();
		other = (Other) bundle.getSerializable(CONSTANT.OTHER_INFO);
		viewName = (TextView) mainView.findViewById(R.id.view_name);
		viewInfo = (TextView) mainView.findViewById(R.id.view_info);
		likeNum = (TextView) mainView.findViewById(R.id.item_like_num);
		sliderLayout =  (SliderLayout) mainView.findViewById(R.id.slider);
		sliderLayout.setPresetTransformer(SliderLayout.Transformer.Stack);
		sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
		sliderLayout.setCustomAnimation(new DescriptionAnimation());
		sliderLayout.setDuration(4000);
		System.out.println("onCreateHeaderView");
		
		return mainView;
	}

	@Override
	public View onCreateContentView(LayoutInflater inflater, ViewGroup container) {
		mListView = (ListView) inflater.inflate(R.layout.fragment_listview,
				container, false);
		mListView.setDividerHeight(0);
		System.out.println("onCreateContentView mListView = (ListView) inflater.inflate(R)");
		// mListView.setVisibility(View.GONE);
		return mListView;
	}

	@Override
	public View onCreateContentOverlayView(LayoutInflater inflater,
			ViewGroup container) {
		progressBar = new ProgressBar(getActivity());
		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
		layoutParams.setMargins(0, 120, 0, 0);
		getActivity().addContentView(progressBar, layoutParams);
		System.out.println("onCreateContentOverlayView ProgressBar progressBar = new ProgressBar()");

		setListViewTitles();
		return new View(getActivity());
	}

	private ListViewDetailAdapter viewListAdapter;
	private SliderLayout sliderLayout;
	private List<String> imageUrls = null;
	public List<String> getUrls(){return imageUrls;}
	
	private void setListViewTitles() {

		viewName.setText(other.getName());
		viewInfo.setText(other.getInfo());
		likeNum.setText(other.getLikeNum() + "人赞过");

		System.out.println("setListViewTitles......");
		imageUrls = other.getUrls();
		for (int i = 0; i < imageUrls.size(); i++) {
			StringBuffer temp = new StringBuffer(CONSTANT.BASE_ROOT_URL)
			.append(imageUrls.get(i).replaceFirst(".", ""));
			TextSliderView textSliderView = new TextSliderView(getActivity());
			// initialize a SliderLayout
			textSliderView.description("").image(temp.toString())
					.setScaleType(BaseSliderView.ScaleType.Fit)
					.setOnSliderClickListener(this);
			// add your extra information
			textSliderView.getBundle().putString("extra", i+"");
			sliderLayout.addSlider(textSliderView);
			System.out.println(temp.toString());
		}

		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("title", "人物简介编辑");
		map.put("content", other.getRecord());
		listItems.add(map);
		map = new HashMap<String, Object>();
		map.put("title", "社会活动编辑");
		map.put("content", other.getText());
		listItems.add(map);
		viewListAdapter = new ListViewDetailAdapter(getActivity(), listItems);
		mListView.setAdapter(viewListAdapter);
		
		progressBar.setVisibility(View.GONE);
		
		LayoutInflater inflater = getActivity().getLayoutInflater();
		RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.action_bar_bottom, null, true);
		getActivity().addContentView(relativeLayout, new FrameLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.BOTTOM));
		
	}

	@Override
	public void onSliderClick(BaseSliderView slider) {
		// TODO Auto-generated method stub
		Toast.makeText(getActivity(),slider.getBundle().get("extra") + "",Toast.LENGTH_SHORT).show();
	}

}