package com.zehao.tripapp.area;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

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
import com.zehao.data.bean.Village;
import com.zehao.http.HttpCLient;
import com.zehao.tripapp.R;
import com.zehao.tripapp.detail.DetailActivity;
import com.zehao.tripapp.point.PointActivity;

import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;

@SuppressLint("InflateParams")
public class ListViewFragment extends HeaderFragment implements OnSliderClickListener {

	private ListView mListView;
	private TextView viewName, viewInfo;

	private ProgressBar progressBar;

	private View mainView;
	private Village village = null;
	public Integer getVillageId(){
		return village==null?0:village.getVillageId();
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
				((AreaActivity) getActivity()).getFadingActionBarHelper()
						.setActionBarAlpha((int) (255 * progress));
			}
		});
		new Handler().post(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Bundle bundle = getActivity().getIntent().getExtras();
				String url = "/AppVillage_getMainListAction.action";
				JsonObject json = new JsonObject();
				json.addProperty(CONSTANT.APP_GET_DATA, CONSTANT.MAIN_VIEW);
				json.addProperty(CONSTANT.MAIN_VIEW_ID,
						bundle.getInt(CONSTANT.MAIN_VIEW_ID));
				RequestParams params = new RequestParams();
				params.add(CONSTANT.DATA, json.toString());
				System.out.println("App发送数据：" + json.toString());
				HttpCLient.post(url, params, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						// TODO Auto-generated method stub
						JsonObject json = (JsonObject) new JsonParser()
								.parse(new String(arg2));
						System.out.println("服务器返回数据：" + json);
						String errorCode = json.get(CONSTANT.ERRCODE)
								.getAsString();
						if (CONSTANT.CODE_168.equals(errorCode)) {
							Gson gson = new GsonBuilder().setDateFormat(
									"yyyy-MM-dd HH:mm:ss").create();
							List<String> urls = gson.fromJson(
									json.get(CONSTANT.MAIN_VIEW_PICTURE)
											.getAsJsonArray(),
									new TypeToken<List<String>>() {
									}.getType());
							village = gson.fromJson(
									json.get(CONSTANT.MAIN_VIEW_INFO)
											.getAsJsonObject(), Village.class);
							List<Views> views = gson.fromJson(
									json.get(CONSTANT.MAIN_VIEW_CHILDVIEW)
											.getAsJsonArray(),
									new TypeToken<List<Views>>() {
									}.getType());
							setListViewTitles(urls, views);
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
		mainView = inflater.inflate(R.layout.activity_view_area_header,
				container, false);
		viewName = (TextView) mainView.findViewById(R.id.viewa_viewarea_name);
		viewInfo = (TextView) mainView.findViewById(R.id.viewa_viewarea_info);
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

	private ListViewAdapter viewListAdapter;
	private SliderLayout sliderLayout;
	
	private void setListViewTitles(List<String> sList, final List<Views> vList) {

		viewName.setText(village.getVillageName());
		viewInfo.setText(village.getVillageInfo());

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

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				viewListAdapter = new ListViewAdapter(getActivity(), vList);

				// mListView.setVisibility(View.VISIBLE);
				setListViewAdapter(mListView, viewListAdapter);
				mListView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
							long arg3) {
						// TODO Auto-generated method stub
						Views view = (Views) viewListAdapter.getItem(arg2-1);
						if(CONSTANT.VIEW_SIGN_Y.equals(view.getChildSign())){
							Intent intent = new Intent(getActivity(), PointActivity.class);
							intent.putExtra(CONSTANT.VIEW_INFO, view);
							startActivity(intent);
						}else{
							Intent intent = new Intent(getActivity(), DetailActivity.class);
							Bundle bundle = new Bundle();
							bundle.putSerializable(CONSTANT.VIEW_INFO, view);
							intent.putExtras(bundle);
							startActivity(intent);
						}
					}
				});
				
				progressBar.setVisibility(View.GONE);
				
				LayoutInflater inflater = getActivity().getLayoutInflater();
				RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.action_bar_bottom, null, true);
				getActivity().addContentView(relativeLayout, new FrameLayout.LayoutParams(
						ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.BOTTOM));
			}
		}, 1500);
		
	}

	@Override
	public void onSliderClick(BaseSliderView slider) {
		// TODO Auto-generated method stub
		Toast.makeText(getActivity(),slider.getBundle().get("extra") + "",Toast.LENGTH_SHORT).show();
	}

}
