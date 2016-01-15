package com.zehao.tripapp.map;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.overlayutil.OverlayManager;
import com.baidu.mapapi.overlayutil.TransitRouteOverlay;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.mapapi.search.share.LocationShareURLOption;
import com.baidu.mapapi.search.share.OnGetShareUrlResultListener;
import com.baidu.mapapi.search.share.ShareUrlResult;
import com.baidu.mapapi.search.share.ShareUrlSearch;
import com.zehao.base.BaseActivity;
import com.zehao.constant.CONSTANT;
import com.zehao.tripapp.R;

/**
 * 此demo用来展示如何进行驾车、步行、公交路线搜索并在地图使用RouteOverlay、TransitOverlay绘制
 * 同时展示如何进行节点浏览并弹出泡泡
 */
public class RouteActivity extends BaseActivity implements
		BaiduMap.OnMapClickListener, OnGetRoutePlanResultListener,
		OnGetShareUrlResultListener, OnMarkerClickListener,
		OnGetGeoCoderResultListener{
	// 浏览路线节点相关
	Button mBtnPre = null;// 上一个节点
	Button mBtnNext = null;// 下一个节点
	int nodeIndex = -1;// 节点索引,供浏览节点时使用
	@SuppressWarnings("rawtypes")
	RouteLine route = null;
	OverlayManager routeOverlay = null;
	boolean useDefaultIcon = false;
	private TextView popupText = null;// 泡泡view

	// 地图相关，使用继承MapView的MyRouteMapView目的是重写touch事件实现泡泡处理
	// 如果不处理touch事件，则无需继承，直接使用MapView即可
	MapView mMapView = null; // 地图View
	BaiduMap mBaidumap = null;

	String type, start, end = null;
	int index = 0;
	int size = 0;
	List<WalkingRouteLine> walk_routes;
	List<TransitRouteLine> bus_routes;
	List<DrivingRouteLine> drive_routes;

	// 搜索相关
	RoutePlanSearch mSearch = null; // 搜索模块，也可去掉地图模块独立使用
	
	private ShareUrlSearch mShareUrlSearch = null;
	private GeoCoder mGeoCoder = null;

	@Override
	protected void initContentView(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		addActionBar();
		baseSetContentView(savedInstanceState, R.layout.activity_map_route);
		addLeftMenu(Boolean.TRUE);
		
		CharSequence titleLable = "路线规划功能";
		setTitle(titleLable);
		// 初始化地图
		mMapView = (MapView) findViewById(R.id.map);
		mMapView.showZoomControls(false);
		mBaidumap = mMapView.getMap();
		// 地图移动到中山市南区
		LatLng main = new LatLng(22.4960480000, 113.3772240000);
		mBaidumap.setMapStatus(MapStatusUpdateFactory.newLatLng(main));

		mBtnPre = (Button) findViewById(R.id.pre);
		mBtnNext = (Button) findViewById(R.id.next);
		// 地图点击事件处理
		mBaidumap.setOnMapClickListener(this);

		// 初始化搜索模块，注册事件监听
		mSearch = RoutePlanSearch.newInstance();
		mSearch.setOnGetRoutePlanResultListener(this);

		mShareUrlSearch = ShareUrlSearch.newInstance();
		mShareUrlSearch.setOnGetShareUrlResultListener(this);
		
		// 取出数据
		getDate(getIntent());
		
		//定义Maker坐标点  
//		LatLng point = new LatLng(22.4961990000, 113.3767290000);  
		//构建Marker图标  
//		BitmapDescriptor bitmap = BitmapDescriptorFactory  
//		    .fromResource(R.drawable.icon_st);  
		//构建MarkerOption，用于在地图上添加Marker  
//		OverlayOptions option = new MarkerOptions()  
//		    .position(point)  
//		    .icon(bitmap);  
		//在地图上添加Marker，并显示  
//		@SuppressWarnings("unused")
//		Marker marker = (Marker) mBaidumap.addOverlay(option);
		mBaidumap.setOnMarkerClickListener(this);
		mGeoCoder = GeoCoder.newInstance();
		mGeoCoder.setOnGetGeoCodeResultListener(this);
		
		// 发起反地理编码请求,成功后在地图上增加marker标记并且触发请求分享URL获取，成功后弹出让用户选择
		// （实际上，进入Activity后在handler中发起反地理编码请求，成功后在地图上标记景点，用户点击选择分享）
//		mGeoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(point));
	}

	public void getDate(Intent intent) {
		type = intent.getStringExtra(CONSTANT.TYPE);
		start = intent.getStringExtra(CONSTANT.ROUTE_START);
		end = intent.getStringExtra(CONSTANT.ROUTE_END);

		// 设置起终点信息，对于tranist search 来说，城市名无意义
		PlanNode stNode = PlanNode.withCityNameAndPlaceName("中山市", start);
		PlanNode enNode = PlanNode.withCityNameAndPlaceName("中山市", end);

		if (CONSTANT.ROUTE_WALK.equals(type)) {
			mSearch.walkingSearch((new WalkingRoutePlanOption()).from(stNode)
					.to(enNode));
		} else if (CONSTANT.ROUTE_DRIVE.equals(type)) {
			mSearch.drivingSearch((new DrivingRoutePlanOption()).from(stNode)
					.to(enNode));
		} else if (CONSTANT.ROUTE_BUS.equals(type)) {
			mSearch.transitSearch((new TransitRoutePlanOption()).from(stNode)
					.city("中山市").to(enNode));
		} else {
			Toast.makeText(this, "请选择交通类型！", Toast.LENGTH_LONG).show();
			finish();
		}
	}

	/**
	 * 更换路线
	 * 
	 * @param view
	 */
	public void changeRouteLine(View view) {
		if (size == 1) {
			Toast.makeText(this, "亲，这是最佳路线！", Toast.LENGTH_LONG).show();
		}
		index++;
		if (index >= size) {
			index = 0;
		}
		mBaidumap.clear();
		if (CONSTANT.ROUTE_WALK.equals(type)) {
			route = walk_routes.get(index);
			setWalkRoute();
		} else if (CONSTANT.ROUTE_DRIVE.equals(type)) {
			route = drive_routes.get(index);
			setDriveRoute();
		} else if (CONSTANT.ROUTE_BUS.equals(type)) {
			route = bus_routes.get(index);
			setBusRoute();
		} else {
			Toast.makeText(this, "请选择交通类型！", Toast.LENGTH_LONG).show();
			finish();
		}
	}

	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	boolean isFirstLoc = true;// 是否首次定位
	boolean switchLoc = false;// 定位开关

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			int locType = location.getLocType();
			Toast.makeText(RouteActivity.this, "当前定位的返回值是：" + locType,
					Toast.LENGTH_SHORT).show();
			double longitude = location.getLongitude();
			double latitude = location.getLatitude();
			float radius = 0;
			String addrStr = null;
			if (location.hasRadius()) {// 判断是否有定位精度半径
				radius = location.getRadius();
			}
			if (locType == BDLocation.TypeGpsLocation) {//
				Toast.makeText(
						RouteActivity.this,
						"当前速度是：" + location.getSpeed() + "~~定位使用卫星数量："
								+ location.getSatelliteNumber(),
						Toast.LENGTH_SHORT).show();
			} else if (locType == BDLocation.TypeNetWorkLocation) {
				addrStr = location.getAddrStr();// 获取反地理编码(文字描述的地址)
				Toast.makeText(RouteActivity.this, addrStr, Toast.LENGTH_SHORT)
						.show();
			}
			float direction = location.getDirection();// 获取手机方向，【0~360°】,手机上面正面朝北为0°
			String province = location.getProvince();// 省份
			String city = location.getCity();// 城市
			String district = location.getDistrict();// 区县
			Toast.makeText(RouteActivity.this,
					province + "~" + city + "~" + district, Toast.LENGTH_SHORT)
					.show();
			// 不在景区内
			if (!city.equals("中山市")) {
				Toast.makeText(RouteActivity.this, "当前不在景区内！",
						Toast.LENGTH_SHORT).show();
				return;
			}
			// 构造定位数据
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(radius)//
					.direction(direction)// 方向
					.latitude(latitude)//
					.longitude(longitude)//
					.build();
			// 设置定位数据
			mBaidumap.setMyLocationData(locData);
			LatLng ll = new LatLng(latitude, longitude);
			MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(ll);
			mBaidumap.animateMapStatus(msu);
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	/**
	 * 定位
	 * 
	 * @param view
	 */
	public void forLocation(View view) {
		if (isFirstLoc) {
			// 开启定位图层
			mBaidumap.setMyLocationEnabled(true);
			// 定位初始化
			mLocClient = new LocationClient(getApplicationContext());
			mLocClient.registerLocationListener(myListener);
			LocationClientOption option = new LocationClientOption();
			option.setOpenGps(true);// 打开gps
			option.setCoorType("bd09ll"); // 设置坐标类型
			option.setScanSpan(5000);// 定时请求定位数据的时间间隔
			option.setIsNeedAddress(true);
			option.setNeedDeviceDirect(true);
			option.setLocationMode(com.baidu.location.LocationClientOption.LocationMode.Hight_Accuracy);
			mLocClient.setLocOption(option);
			mLocClient.start();
			isFirstLoc = false;
			switchLoc = true;
			Toast.makeText(RouteActivity.this, "定位已经开启！", Toast.LENGTH_SHORT)
					.show();
		} else if (switchLoc) {
			mLocClient.stop();
			Toast.makeText(RouteActivity.this, "定位已经关闭！", Toast.LENGTH_SHORT)
					.show();
			switchLoc = false;
		} else if (!switchLoc) {
			mLocClient.start();
			Toast.makeText(RouteActivity.this, "定位已经开启！", Toast.LENGTH_SHORT)
					.show();
		}
	}

	float zoom_level = 18;

	/**
	 * 地图放大缩小
	 * 
	 * @param view
	 */
	public void forZoomMap(View view) {
		zoom_level = mBaidumap.getMapStatus().zoom;
		if (view.getId() == R.id.button_zoom_add) {
			if (zoom_level <= 18) {
				mBaidumap.setMapStatus(MapStatusUpdateFactory.zoomIn());
			} else {
				Toast.makeText(RouteActivity.this, "已经放至最大！",
						Toast.LENGTH_SHORT).show();
			}
		} else if (view.getId() == R.id.button_zoom_sub) {
			if (zoom_level > 4) {
				mBaidumap.setMapStatus(MapStatusUpdateFactory.zoomOut());
			} else {
				Toast.makeText(RouteActivity.this, "已经放至最小！",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	public void setWalkRoute() {
		WalkingRouteOverlay overlay = new MyWalkingRouteOverlay(mBaidumap);
		mBaidumap.setOnMarkerClickListener(overlay);
		routeOverlay = overlay;
		overlay.setData((WalkingRouteLine) route);
		overlay.addToMap();
		overlay.zoomToSpan();
	}

	public void setDriveRoute() {
		DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaidumap);
		routeOverlay = overlay;
		mBaidumap.setOnMarkerClickListener(overlay);
		overlay.setData((DrivingRouteLine) route);
		overlay.addToMap();
		overlay.zoomToSpan();
	}

	public void setBusRoute() {
		TransitRouteOverlay overlay = new MyTransitRouteOverlay(mBaidumap);
		mBaidumap.setOnMarkerClickListener(overlay);
		routeOverlay = overlay;
		overlay.setData((TransitRouteLine) route);
		overlay.addToMap();
		overlay.zoomToSpan();
	}

	/**
	 * 节点浏览示例
	 * 
	 * @param v
	 */
	public void nodeClick(View v) {
		if (route == null || route.getAllStep() == null) {
			return;
		}
		if (nodeIndex == -1 && v.getId() == R.id.pre) {
			return;
		}
		// 设置节点索引
		if (v.getId() == R.id.next) {
			if (nodeIndex < route.getAllStep().size() - 1) {
				nodeIndex++;
			} else {
				return;
			}
		} else if (v.getId() == R.id.pre) {
			if (nodeIndex > 0) {
				nodeIndex--;
			} else {
				return;
			}
		}
		// 获取节结果信息
		LatLng nodeLocation = null;
		String nodeTitle = null;
		Object step = route.getAllStep().get(nodeIndex);
		if (step instanceof DrivingRouteLine.DrivingStep) {
			nodeLocation = ((DrivingRouteLine.DrivingStep) step).getEntrance()
					.getLocation();
			nodeTitle = ((DrivingRouteLine.DrivingStep) step).getInstructions();
		} else if (step instanceof WalkingRouteLine.WalkingStep) {
			nodeLocation = ((WalkingRouteLine.WalkingStep) step).getEntrance()
					.getLocation();
			nodeTitle = ((WalkingRouteLine.WalkingStep) step).getInstructions();
		} else if (step instanceof TransitRouteLine.TransitStep) {
			nodeLocation = ((TransitRouteLine.TransitStep) step).getEntrance()
					.getLocation();
			nodeTitle = ((TransitRouteLine.TransitStep) step).getInstructions();
		}

		if (nodeLocation == null || nodeTitle == null) {
			return;
		}
		// 移动节点至中心
		mBaidumap.setMapStatus(MapStatusUpdateFactory.newLatLng(nodeLocation));
		// show popup
		popupText = new TextView(RouteActivity.this);
		popupText.setBackgroundResource(R.drawable.popup);
		popupText.setTextColor(0xFF000000);
		popupText.setText(nodeTitle);
		mBaidumap.showInfoWindow(new InfoWindow(popupText, nodeLocation, 0));

	}

	/**
	 * 切换路线图标，刷新地图使其生效 注意： 起终点图标使用中心对齐.
	 */
	public void changeRouteIcon(View v) {
		if (routeOverlay == null) {
			return;
		}
		if (useDefaultIcon) {
			((Button) v).setText("自定义起终点图标");
			Toast.makeText(this, "将使用系统起终点图标", Toast.LENGTH_SHORT).show();

		} else {
			((Button) v).setText("系统起终点图标");
			Toast.makeText(this, "将使用自定义起终点图标", Toast.LENGTH_SHORT).show();

		}
		useDefaultIcon = !useDefaultIcon;
		routeOverlay.removeFromMap();
		routeOverlay.addToMap();
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}

	// 定制RouteOverly
	private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

		public MyDrivingRouteOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		@Override
		public BitmapDescriptor getStartMarker() {
			if (useDefaultIcon) {
				return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
			}
			return null;
		}

		@Override
		public BitmapDescriptor getTerminalMarker() {
			if (useDefaultIcon) {
				return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
			}
			return null;
		}
	}

	private class MyWalkingRouteOverlay extends WalkingRouteOverlay {

		public MyWalkingRouteOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		@Override
		public BitmapDescriptor getStartMarker() {
			if (useDefaultIcon) {
				return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
			}
			return null;
		}

		@Override
		public BitmapDescriptor getTerminalMarker() {
			if (useDefaultIcon) {
				return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
			}
			return null;
		}
	}

	private class MyTransitRouteOverlay extends TransitRouteOverlay {

		public MyTransitRouteOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		@Override
		public BitmapDescriptor getStartMarker() {
			if (useDefaultIcon) {
				return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
			}
			return null;
		}

		@Override
		public BitmapDescriptor getTerminalMarker() {
			if (useDefaultIcon) {
				return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
			}
			return null;
		}
	}

	@Override
	public void onMapClick(LatLng point) {
		mBaidumap.hideInfoWindow();
	}

	@Override
	public boolean onMapPoiClick(MapPoi poi) {
		return false;
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// 退出时销毁定位
		if (mLocClient != null) {
			mLocClient.stop();
		}
		// 关闭定位图层
		mBaidumap.setMyLocationEnabled(false);
		mShareUrlSearch.destroy();
		mSearch.destroy();
		mMapView.onDestroy();
		super.onDestroy();
	}

	@Override
	public void onGetWalkingRouteResult(WalkingRouteResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(RouteActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
					.show();
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			// result.getSuggestAddrInfo()
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			nodeIndex = -1;
			mBtnPre.setVisibility(View.VISIBLE);
			mBtnNext.setVisibility(View.VISIBLE);
			walk_routes = result.getRouteLines();
			size = walk_routes.size();
			route = walk_routes.get(index);
			WalkingRouteOverlay overlay = new MyWalkingRouteOverlay(mBaidumap);
			mBaidumap.setOnMarkerClickListener(overlay);
			routeOverlay = overlay;
			overlay.setData(walk_routes.get(index));
			overlay.addToMap();
			overlay.zoomToSpan();
		}

	}

	@Override
	public void onGetTransitRouteResult(TransitRouteResult result) {

		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(RouteActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
					.show();
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			// result.getSuggestAddrInfo()
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			nodeIndex = -1;
			mBtnPre.setVisibility(View.VISIBLE);
			mBtnNext.setVisibility(View.VISIBLE);
			bus_routes = result.getRouteLines();
			size = bus_routes.size();
			route = bus_routes.get(index);
			TransitRouteOverlay overlay = new MyTransitRouteOverlay(mBaidumap);
			mBaidumap.setOnMarkerClickListener(overlay);
			routeOverlay = overlay;
			overlay.setData(bus_routes.get(index));
			overlay.addToMap();
			overlay.zoomToSpan();
		}
	}

	@Override
	public void onGetDrivingRouteResult(DrivingRouteResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(RouteActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
					.show();
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			// result.getSuggestAddrInfo()
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			nodeIndex = -1;
			mBtnPre.setVisibility(View.VISIBLE);
			mBtnNext.setVisibility(View.VISIBLE);
			drive_routes = result.getRouteLines();
			size = drive_routes.size();
			route = drive_routes.get(index);
			DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaidumap);
			routeOverlay = overlay;
			mBaidumap.setOnMarkerClickListener(overlay);
			overlay.setData(drive_routes.get(index));
			overlay.addToMap();
			overlay.zoomToSpan();
		}
	}

	@Override
	public void onGetLocationShareUrlResult(ShareUrlResult arg0) {
		// TODO Auto-generated method stub
		// 分享短串结果
		Intent it = new Intent(Intent.ACTION_SEND);
		it.putExtra(Intent.EXTRA_TEXT, "您的朋友通过百度地图SDK与您分享一个位置: " + "中山市南区"
				+ " -- " + arg0.getUrl());
		it.setType("text/plain");
		startActivity(Intent.createChooser(it, "将短串分享到"));
	}

	@Override
	public void onGetPoiDetailShareUrlResult(ShareUrlResult arg0) {
		// TODO Auto-generated method stub
		// 分享短串结果
		Intent it = new Intent(Intent.ACTION_SEND);
		it.putExtra(Intent.EXTRA_TEXT, "您的朋友通过百度地图SDK与您分享一个位置: " + "中山市南区"
				+ " -- " + arg0.getUrl());
		it.setType("text/plain");
		startActivity(Intent.createChooser(it, "将短串分享到"));
	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		// TODO Auto-generated method stub
		Toast.makeText(RouteActivity.this, "click......", Toast.LENGTH_LONG).show();
		return true;
	}

	@Override
	public void onGetGeoCodeResult(GeoCodeResult arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {
		// TODO Auto-generated method stub
		if (arg0 == null || arg0.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(RouteActivity.this, "抱歉，未找到结果",
					Toast.LENGTH_LONG).show();
			return;
		}
		Marker marker = (Marker) mBaidumap.addOverlay(new MarkerOptions()
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.icon_st))
				.title(arg0.getAddress()).position(arg0.getLocation()));
		mShareUrlSearch.requestLocationShareUrl(new LocationShareURLOption()
		.location(marker.getPosition()).snippet("测试分享点")
		.name(marker.getTitle()));
	}

	@Override
	public void setBaseNoTitle() {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	@Override
	protected void handler(Message msg) {
		// TODO Auto-generated method stub
		
	}

}
