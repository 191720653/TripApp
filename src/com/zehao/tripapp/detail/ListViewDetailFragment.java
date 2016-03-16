package com.zehao.tripapp.detail;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zehao.constant.CONSTANT;
import com.zehao.data.bean.Views;
import com.zehao.http.HttpCLient;
import com.zehao.tripapp.R;
import com.zehao.tripapp.detail.DetailActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;

@SuppressLint("InflateParams")
public class ListViewDetailFragment extends HeaderFragment implements OnSliderClickListener {

	private ListView mListView;
	private TextView viewName, viewInfo, likeNum;

	private ProgressBar progressBar;

	private View mainView;
	private Views views = null;
	public Integer getViewId(){
		return views==null?0:views.getViewId();
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
				((DetailActivity) getActivity()).getFadingActionBarHelper()
						.setActionBarAlpha((int) (255 * progress));
			}
		});
		new Handler().post(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String url = "/AppView_getViewListAction.action";
				JsonObject json = new JsonObject();
				json.addProperty(CONSTANT.APP_GET_DATA, CONSTANT.VIEW_LIST);
				json.addProperty(CONSTANT.VIEW_SIGN, views.getChildSign());
				json.addProperty(CONSTANT.VIEW_ID, views.getViewId());
				RequestParams params = new RequestParams();
				params.add(CONSTANT.DATA, json.toString());
				System.out.println("App发送数据：" + json.toString());
				HttpCLient.post(url, params, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						// TODO Auto-generated method stub
						JsonObject json = (JsonObject) new JsonParser().parse(new String(arg2));
						System.out.println("服务器返回数据：" + json);
						String errorCode = json.get(CONSTANT.ERRCODE).getAsString();
						if (CONSTANT.CODE_168.equals(errorCode)) {
							Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
							List<String> urls = gson.fromJson(
									json.get(CONSTANT.VIEW_PICTURE).getAsJsonArray(),
									new TypeToken<List<String>>() {
									}.getType());
							setListViewTitles(urls);
						} else {
							Toast.makeText(getActivity(), CONSTANT.OTHER_ERROR,
									Toast.LENGTH_SHORT).show();
						}
					}
					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						// TODO Auto-generated method stub
						Toast.makeText(getActivity(), CONSTANT.OTHER_ERROR,
								Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	// private SliderLayout mDemoSlider;

	@Override
	public View onCreateHeaderView(LayoutInflater inflater, ViewGroup container) {
		mainView = inflater.inflate(R.layout.activity_view_detail_header,
				container, false);
		Bundle bundle = getActivity().getIntent().getExtras();
		views = (Views) bundle.getSerializable(CONSTANT.VIEW_INFO);
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
		return new View(getActivity());
	}

	private ListViewDetailAdapter viewListAdapter;
	private SliderLayout sliderLayout;
	
	private void setListViewTitles(List<String> sList) {

		viewName.setText(views.getViewName());
		viewInfo.setText(views.getViewInfo());
		likeNum.setText(views.getLikeNum() + "人赞过");

		System.out.println("setListViewTitles");
		HashMap<String, String> url_maps = new HashMap<String, String>();
		for (int i = 0; i < sList.size(); i++) {
			url_maps.put("" + i, CONSTANT.BASE_ROOT_URL
					+ sList.get(i).replaceFirst(".", ""));
		}
		for (String name : url_maps.keySet()) {
			TextSliderView textSliderView = new TextSliderView(getActivity());
			// initialize a SliderLayout
			textSliderView.description("").image(url_maps.get(name))
					.setScaleType(BaseSliderView.ScaleType.Fit)
					.setOnSliderClickListener(this);
			// add your extra information
			textSliderView.getBundle().putString("extra", name);
			sliderLayout.addSlider(textSliderView);
		}
		System.out.println(url_maps);

		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("title", "人物简介编辑");
		map.put("content", views.getViewPerson());
		listItems.add(map);
		map = new HashMap<String, Object>();
		map.put("title", "社会活动编辑");
		map.put("content", views.getViewAction());
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