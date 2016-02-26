/*
 * Copyright (C) 2013 AChep@xda <artemchep@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zehao.tripapp.area;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.achep.header2actionbar.HeaderFragment;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderTypes.BaseSliderView.OnSliderClickListener;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.zehao.tripapp.R;
import com.zehao.tripapp.point.PointActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListViewFragment extends HeaderFragment implements OnSliderClickListener {

    private ListView mListView;
    private String[] mListViewTitles;
    private boolean mLoaded;

    private AsyncLoadSomething mAsyncLoadSomething;
    private FrameLayout mContentOverlay;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        setHeaderBackgroundScrollMode(HEADER_BACKGROUND_SCROLL_PARALLAX);
        setOnHeaderScrollChangedListener(new OnHeaderScrollChangedListener() {
            @Override
            public void onHeaderScrollChanged(float progress, int height, int scroll) {
                height -= getActivity().getActionBar().getHeight();

                progress = (float) scroll / height;
                if (progress > 1f) progress = 1f;

                // *
                // `*
                // ```*
                // ``````*
                // ````````*
                // `````````*
                progress = (1 - (float) Math.cos(progress * Math.PI)) * 0.5f;

                ((AreaActivity) getActivity())
                        .getFadingActionBarHelper()
                        .setActionBarAlpha((int) (255 * progress));
            }
        });

        cancelAsyncTask(mAsyncLoadSomething);
        mAsyncLoadSomething = new AsyncLoadSomething(this);
        mAsyncLoadSomething.execute();
        
        Map<String, Object> map = null;
		for (int i = 0; i < array.length; i++) {
			map = new HashMap<String, Object>();
			map.put("image", array[i][0]);
			map.put("title", array[i][1]);
			map.put("likeNum", array[i][2]);
			map.put("info", array[i][3]);
			listItems.add(map);
			System.out.println(map.toString());
		}

		viewListAdapter = new ListViewAdapter(activity, listItems);
		
    }

    @Override
    public void onDetach() {
        cancelAsyncTask(mAsyncLoadSomething);
        super.onDetach();
    }

    private SliderLayout mDemoSlider;
    @Override
    public View onCreateHeaderView(LayoutInflater inflater, ViewGroup container) {
    	
    	View view = inflater.inflate(R.layout.activity_view_area_header, container, false);
    	
    	mDemoSlider = (SliderLayout) view.findViewById(R.id.slider);
    	
    			HashMap<String, String> url_maps = new HashMap<String, String>();
    			url_maps.put(
    					"Hannibal",
    					"http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg");
    			url_maps.put("Big Bang Theory",
    					"http://tvfiles.alphacoders.com/100/hdclearart-10.png");
    			url_maps.put("House of Cards House of Cards",
    					"http://cdn3.nflximg.net/images/3093/2043093.jpg");
    			url_maps.put(
    					"Game of Thrones",
    					"http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg");
    	
    			HashMap<String, Integer> file_maps = new HashMap<String, Integer>();
    			file_maps.put("Hannibal", R.drawable.image_nanqu);
    			file_maps.put("Big Bang Theory", R.drawable.image_nanqu);
    			file_maps.put("House of Cards House of Cards", R.drawable.image_nanqu);
    			file_maps.put("Game of Thrones", R.drawable.image_nanqu);
    	
    			for (String name : url_maps.keySet()) {
    				TextSliderView textSliderView = new TextSliderView(view.getContext());
    				// initialize a SliderLayout
    				textSliderView.description(name).image(url_maps.get(name))
    						.setScaleType(BaseSliderView.ScaleType.Fit)
    						.setOnSliderClickListener(this);
    	
    				// add your extra information
    				textSliderView.getBundle().putString("extra", name);
    	
    				mDemoSlider.addSlider(textSliderView);
    			}
    			mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
    			mDemoSlider
    					.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
    			mDemoSlider.setCustomAnimation(new DescriptionAnimation());
    			mDemoSlider.setDuration(4000);
        return view;
    }

    @Override
    public View onCreateContentView(LayoutInflater inflater, ViewGroup container) {
        mListView = (ListView) inflater.inflate(R.layout.fragment_listview, container, false);
        if (mLoaded) setListViewTitles(mListViewTitles);
        return mListView;
    }

    @Override
    public View onCreateContentOverlayView(LayoutInflater inflater, ViewGroup container) {
        ProgressBar progressBar = new ProgressBar(getActivity());
        mContentOverlay = new FrameLayout(getActivity());
        mContentOverlay.addView(progressBar, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
        if (mLoaded) mContentOverlay.setVisibility(View.GONE);
        return mContentOverlay;
    }

    private ListViewAdapter viewListAdapter;
	private List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
	private String[][] array = {
			{
					R.drawable.image_beixi + "",
					"|良都1",
					"1681人赞过",
					"马应彪早年家庭贫苦，为了生计去澳洲悉尼谋生，在澳洲皈依基督教。1900年，筹集了一笔资金在香港筹办先施百货公司自任总监督。1914年，在广州长堤建立先施粤行，并附设东亚大酒店，总投资额为港币100万元，取得巨大成功。1917年，先施公司的业务扩展到上海。1921年，与蔡兴等创办了香港国民商业储蓄银行。" },
			{
					R.drawable.image_liangdu + "",
					"|良都2",
					"1682人赞过",
					"马应彪早年家庭贫苦，为了生计去澳洲悉尼谋生，在澳洲皈依基督教。1900年，筹集了一笔资金在香港筹办先施百货公司自任总监督。1914年，在广州长堤建立先施粤行，并附设东亚大酒店，总投资额为港币100万元，取得巨大成功。1917年，先施公司的业务扩展到上海。1921年，与蔡兴等创办了香港国民商业储蓄银行。" },
			{
					R.drawable.image_maling + "",
					"|良都3",
					"1683人赞过",
					"马应彪早年家庭贫苦，为了生计去澳洲悉尼谋生，在澳洲皈依基督教。1900年，筹集了一笔资金在香港筹办先施百货公司自任总监督。1914年，在广州长堤建立先施粤行，并附设东亚大酒店，总投资额为港币100万元，取得巨大成功。1917年，先施公司的业务扩展到上海。1921年，与蔡兴等创办了香港国民商业储蓄银行。" },
			{
					R.drawable.image_chengnan + "",
					"|良都4",
					"1684人赞过",
					"马应彪早年家庭贫苦，为了生计去澳洲悉尼谋生，在澳洲皈依基督教。1900年，筹集了一笔资金在香港筹办先施百货公司自任总监督。1914年，在广州长堤建立先施粤行，并附设东亚大酒店，总投资额为港币100万元，取得巨大成功。1917年，先施公司的业务扩展到上海。1921年，与蔡兴等创办了香港国民商业储蓄银行。" } };
    private void setListViewTitles(String[] titles) {
        mLoaded = true;
        mListViewTitles = titles;
        if (mListView == null) return;

        mListView.setVisibility(View.VISIBLE);
//        setListViewAdapter(mListView, new ArrayAdapter<String>(
//                getActivity(), android.R.layout.simple_list_item_1,
//                mListViewTitles));
        setListViewAdapter(mListView, viewListAdapter);
        mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				System.out.println("jump");
			}
		});
    }

    private void cancelAsyncTask(AsyncTask task) {
        if (task != null) task.cancel(false);
    }

    // //////////////////////////////////////////
    // ///////////// -- LOADER -- ///////////////
    // //////////////////////////////////////////

    private static class AsyncLoadSomething extends AsyncTask<Void, Void, String[]> {

        private static final String TAG = "AsyncLoadSomething";

        final WeakReference<ListViewFragment> weakFragment;

        public AsyncLoadSomething(ListViewFragment fragment) {
            this.weakFragment = new WeakReference<ListViewFragment>(fragment);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            final ListViewFragment audioFragment = weakFragment.get();
            if (audioFragment.mListView != null) audioFragment.mListView.setVisibility(View.INVISIBLE);
            if (audioFragment.mContentOverlay != null) audioFragment.mContentOverlay.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[] doInBackground(Void... voids) {

            try {
                // Emulate long downloading
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return new String[]{"Placeholder", "Placeholder", "Placeholder", "Placeholder",
                    "Placeholder", "Placeholder", "Placeholder", "Placeholder",
                    "Placeholder", "Placeholder", "Placeholder", "Placeholder",
                    "Placeholder", "Placeholder", "Placeholder", "Placeholder",
                    "Placeholder", "Placeholder", "Placeholder", "Placeholder",
                    "Placeholder", "Placeholder", "Placeholder", "Placeholder"};
        }

        @Override
        protected void onPostExecute(String[] titles) {
            super.onPostExecute(titles);
            final ListViewFragment audioFragment = weakFragment.get();
            if (audioFragment == null) {
                Log.d(TAG, "Skipping.., because there is no fragment anymore.");
                return;
            }

            if (audioFragment.mContentOverlay != null) audioFragment.mContentOverlay.setVisibility(View.GONE);
            audioFragment.setListViewTitles(titles);
        }
    }

	@Override
	public void onSliderClick(BaseSliderView slider) {
		// TODO Auto-generated method stub
		System.out.println(slider.getBundle().get("extra") + "");
	}
}
