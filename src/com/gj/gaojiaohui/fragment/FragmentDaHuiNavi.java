package com.gj.gaojiaohui.fragment;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
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
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MarkerOptions.MarkerAnimateType;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.route.BikingRouteLine;
import com.baidu.mapapi.search.route.BikingRouteResult;
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
import com.baidu.mapapi.utils.OpenClientUtil;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.view.support.DrivingRouteOverlay;
import com.gheng.exhibit.view.support.OverlayManager;
import com.gheng.exhibit.view.support.TransitRouteOverlay;
import com.gheng.exhibit.view.support.WalkingRouteOverlay;
import com.gj.gaojiaohui.abconstant.GloableConfig;
import com.gj.gaojiaohui.bean.BDLocations;
import com.gj.gaojiaohui.bean.DaHuiNavisLocations;
import com.gj.gaojiaohui.utils.CommonUtil;
import com.gj.gaojiaohui.utils.ProgressUtil;
import com.gj.gaojiaohui.utils.SharePreferenceUtils;
import com.gj.gaojiaohui.view.MDMRadioButton;
import com.smartdot.wenbo.huiyi.R;

//大会导航界面
public class FragmentDaHuiNavi extends Fragment implements
		BaiduMap.OnMapClickListener, OnGetGeoCoderResultListener,
		OnGetRoutePlanResultListener, OnClickListener {

	OverlayManager routeOverlay = null;
	RouteLine routeline = null;
	MapView mMapView = null; // 地图View
	// 搜索相关
	BaiduMap mBaidumap = null;
	GeoCoder gSearch = null; // 搜索模块，也可去掉地图模块独立使用
	RoutePlanSearch rSearch = null; // 搜索模块，也可去掉地图模块独立使用
	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	boolean isFirstLoc = true; // 是否首次定位
	private Marker mMarkerA;
	private Marker mMarkerB;
	private InfoWindow mInfoWindow;
	boolean isrroute = false;
	ArrayList<BDLocations> hotels = new ArrayList<>();
	ArrayList<BDLocations> zhanguans = new ArrayList<>();
	ArrayList<Marker> markers = new ArrayList<>();
	// BitmapDescriptor bdhotel = BitmapDescriptorFactory
	// .fromResource(R.drawable.marker_hotel);
	BitmapDescriptor dahuinavi_marker = BitmapDescriptorFactory
			.fromResource(R.drawable.dahuinavi_marker);
	// BitmapDescriptor bdmyhotel = BitmapDescriptorFactory
	// .fromResource(R.drawable.marker_huichang);
	// BitmapDescriptor bdhuichang = BitmapDescriptorFactory
	// .fromResource(R.drawable.marker_huichang);
	// BitmapDescriptor bdjingdian = BitmapDescriptorFactory
	// .fromResource(R.drawable.marker_jingdian);
	Context mcontext;
	Button jiudian, zhanguan;
	RadioGroup dahuinavi_routetype_rg;
	MDMRadioButton radiobutton_drive;
	ImageView openbdmap_btn;
	// l1是定位的坐标，l2是点击的搜索地点或者常用的酒店类地点
	LatLng ml1 = null;
	LatLng ml2 = null;
	String latitude = null;
	public Handler ml2_handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				ProgressUtil.dismissProgressDialog();
				parseData(msg);
				break;
			case 5:
				if (ml1 != null && ml2 != null) {
					dahuinavi_routetype_rg.setVisibility(View.VISIBLE);
				} else {
					dahuinavi_routetype_rg.setVisibility(View.GONE);
				}
				break;
			case 6:

				break;
			}
		};
	};
	String muser_id = "";

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_dahuinavi, container,
				false);
		mcontext = getActivity();
		SharePreferenceUtils.getAppConfig(getActivity());
		// 初始化地图
		mMapView = (MapView) view.findViewById(R.id.dahuinavi_map);
		mBaidumap = mMapView.getMap();
		mMapView.showZoomControls(false);
		mBaidumap.setMyLocationConfigeration(new MyLocationConfiguration(
				LocationMode.NORMAL, true, null));
		// 百度地图定位----开启定位图层
		mBaidumap.setMyLocationEnabled(true);
		// 地图点击事件处理
		mBaidumap.setOnMapClickListener(this);
		// 定位初始化
		mLocClient = new LocationClient(getActivity().getApplicationContext());
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); // 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(10);
		mLocClient.setLocOption(option);
		//
		initView(view);
		process();
		setAllClick();
		//
		mLocClient.start();
		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (getActivity().getIntent().getStringExtra("latitude") != null) {
			try {
				latitude = getActivity().getIntent().getStringExtra("latitude");
				String longitude = getActivity().getIntent().getStringExtra(
						"longitude");
				String address = getActivity().getIntent().getStringExtra(
						"address");
				ml2 = new LatLng(Double.parseDouble(latitude),
						Double.parseDouble(longitude));
				ml2_handler.sendEmptyMessage(5);
				//
				mBaidumap.clear();
				mMarkerA = (Marker) mBaidumap.addOverlay(new MarkerOptions()
						.position(ml2).title(address).icon(dahuinavi_marker));
				MapStatusUpdate u = MapStatusUpdateFactory
						.newMapStatus(new MapStatus.Builder().target(ml2)
								.zoom(15).build());
				mBaidumap.setMapStatus(u);
				//

			} catch (Exception e) {
				// 防止转换崩溃
			}
		}
	}

	/** 解析json数据 */
	private void parseData(Message msg) {
		String json = msg.obj.toString();
		// 通过gson将json映射成实体类
		DaHuiNavisLocations daHuiNavisLocations = CommonUtil.gson.fromJson(
				json, DaHuiNavisLocations.class);// 这段崩溃有3种原因
		// 1/字段名缺失,2/格式类型不对,3/字段null了
		switch (daHuiNavisLocations.resultCode) {
		case "200":
			for (int i = 0; i < daHuiNavisLocations.exhibition_list.size(); i++) {
				daHuiNavisLocations.exhibition_list
						.get(i)
						.setLl(new LatLng(
								Double.parseDouble(daHuiNavisLocations.exhibition_list
										.get(i).latitude),
								Double.parseDouble(daHuiNavisLocations.exhibition_list
										.get(i).longitude)));
			}
			for (int i = 0; i < daHuiNavisLocations.hotel_list.size(); i++) {
				daHuiNavisLocations.hotel_list.get(i).setLl(
						new LatLng(Double
								.parseDouble(daHuiNavisLocations.hotel_list
										.get(i).latitude), Double
								.parseDouble(daHuiNavisLocations.hotel_list
										.get(i).longitude)));
			}
			hotels.addAll(daHuiNavisLocations.hotel_list);
			zhanguans.addAll(daHuiNavisLocations.exhibition_list);
			break;

		default:
			break;
		}
	}

	private void initView(View view) {
		jiudian = (Button) view.findViewById(R.id.jiudian);
		zhanguan = (Button) view.findViewById(R.id.zhanguan);
		openbdmap_btn = (ImageView) view.findViewById(R.id.openbdmap_btn);
		dahuinavi_routetype_rg = (RadioGroup) view
				.findViewById(R.id.dahuinavi_routetype_rg);
		radiobutton_drive = (MDMRadioButton) view
				.findViewById(R.id.radiobutton_drive);
		mMapView.showZoomControls(false);
		jiudian.setOnClickListener(this);
		zhanguan.setOnClickListener(this);
		openbdmap_btn.setOnClickListener(this);
		// 设置透明度
		// jiudian.getBackground().setAlpha(80);
		// zhanguan.getBackground().setAlpha(80);

		// 初始化搜索模块，注册事件监听
		gSearch = GeoCoder.newInstance();
		gSearch.setOnGetGeoCodeResultListener(this);
		rSearch = RoutePlanSearch.newInstance();
		rSearch.setOnGetRoutePlanResultListener(this);
		//
	}

	private void process() {
		try {
			muser_id = Constant.decode(Constant.key,
					(String) SharePreferenceUtils.getParam("user_id", ""));
		} catch (Exception e) {
			e.printStackTrace();
		}
		String murl = String.format(GloableConfig.DAHUINAVI_URL, muser_id);
		Constant.getDataByVolley(mcontext, murl, ml2_handler, 0);
	}

	private void setAllClick() {
		mBaidumap.setOnMarkerClickListener(new OnMarkerClickListener() {
			public boolean onMarkerClick(final Marker marker) {
				Button button = new Button(mcontext.getApplicationContext());
				button.setBackgroundResource(R.drawable.popup);
				button.setTextColor(Color.parseColor("#3a3b3b"));
				button.setTextSize(12);
				if (!isrroute) {
					button.setText(marker.getTitle());
					// TextView nametv = new TextView(getApplicationContext());
					// nametv.setBackgroundResource(R.drawable.popup);
					// nametv.setTextColor(Color.parseColor("#3a3b3b"));
					// nametv.setTextSize(12);
					// nametv.setText(marker.getTitle());
					ml2 = marker.getPosition();
					ml2_handler.sendEmptyMessage(5);
					radiobutton_drive.setChecked(true);
					if (ml1 != null && ml2 != null) {
						if (routeOverlay != null)
							routeOverlay.removeFromMap();
						PlanNode stNode1 = PlanNode.withLocation(ml1);
						PlanNode enNode1 = PlanNode.withLocation(ml2);
						rSearch.drivingSearch((new DrivingRoutePlanOption())
								.from(stNode1).to(enNode1));
					} else {
						Toast.makeText(
								mcontext,
								getResources()
										.getString(
												R.string.please_confirm_start_destination),
								Toast.LENGTH_SHORT).show();
					}
				} else {
					LatLng nodeLocation = null;
					String nodeTitle = "";
					if (marker.getPosition() == routeline.getStarting()
							.getLocation()) {
						nodeTitle = getResources().getString(
								R.string.start_point);
					} else if (marker.getPosition() == routeline.getTerminal()
							.getLocation()) {
						nodeTitle = getResources().getString(
								R.string.destination);
					} else {
						for (int i = 0; i < routeline.getAllStep().size(); i++) {
							Object step = routeline.getAllStep().get(i);
							if (step instanceof DrivingRouteLine.DrivingStep) {
								nodeLocation = ((DrivingRouteLine.DrivingStep) step)
										.getEntrance().getLocation();
								nodeTitle = ((DrivingRouteLine.DrivingStep) step)
										.getInstructions();
							} else if (step instanceof WalkingRouteLine.WalkingStep) {
								nodeLocation = ((WalkingRouteLine.WalkingStep) step)
										.getEntrance().getLocation();
								nodeTitle = ((WalkingRouteLine.WalkingStep) step)
										.getInstructions();
							} else if (step instanceof TransitRouteLine.TransitStep) {
								nodeLocation = ((TransitRouteLine.TransitStep) step)
										.getEntrance().getLocation();
								nodeTitle = ((TransitRouteLine.TransitStep) step)
										.getInstructions();
							} else if (step instanceof BikingRouteLine.BikingStep) {
								nodeLocation = ((BikingRouteLine.BikingStep) step)
										.getEntrance().getLocation();
								nodeTitle = ((BikingRouteLine.BikingStep) step)
										.getInstructions();
							}
							if (i == routeline.getAllStep().size() - 1) {
								if (nodeLocation != marker.getPosition()) {
									nodeTitle = "";
								}
							} else {
								if (nodeLocation == marker.getPosition()) {
									break;
								}
							}
						}
					}
					if (!nodeTitle.equals(""))
						button.setText(nodeTitle);

				}
				mInfoWindow = new InfoWindow(BitmapDescriptorFactory
						.fromView(button), marker.getPosition(), -47, null);
				mBaidumap.showInfoWindow(mInfoWindow);
				return true;
			}
		});
		dahuinavi_routetype_rg
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {
						case R.id.radiobutton_drive:
							// guest_vp.setCurrentItem(0);
							if (ml1 != null && ml2 != null) {
								if (routeOverlay != null)
									routeOverlay.removeFromMap();
								PlanNode stNode1 = PlanNode.withLocation(ml1);
								PlanNode enNode1 = PlanNode.withLocation(ml2);
								rSearch.drivingSearch((new DrivingRoutePlanOption())
										.from(stNode1).to(enNode1));
							} else {
								Toast.makeText(
										mcontext,
										getResources()
												.getString(
														R.string.please_confirm_destination),
										Toast.LENGTH_SHORT).show();
							}
							break;
						case R.id.radiobutton_transit:
							// guest_vp.setCurrentItem(1);
							if (ml1 != null && ml2 != null) {
								if (routeOverlay != null)
									routeOverlay.removeFromMap();
								PlanNode stNode1 = PlanNode.withLocation(ml1);
								PlanNode enNode1 = PlanNode.withLocation(ml2);
								rSearch.transitSearch((new TransitRoutePlanOption())
										.from(stNode1).city("北京").to(enNode1));
							} else {
								Toast.makeText(
										mcontext,
										getResources()
												.getString(
														R.string.please_confirm_destination),
										Toast.LENGTH_SHORT).show();
							}
							break;
						case R.id.radiobutton_walk:
							// guest_vp.setCurrentItem(2);
							if (ml1 != null && ml2 != null) {
								if (routeOverlay != null)
									routeOverlay.removeFromMap();
								PlanNode stNode1 = PlanNode.withLocation(ml1);
								PlanNode enNode1 = PlanNode.withLocation(ml2);
								rSearch.walkingSearch((new WalkingRoutePlanOption())
										.from(stNode1).to(enNode1));
							} else {
								Toast.makeText(
										mcontext,
										getResources()
												.getString(
														R.string.please_confirm_destination),
										Toast.LENGTH_SHORT).show();
							}
							break;
						}
					}
				});
	}

	@Override
	public void onClick(View v) {
		if (v.equals(jiudian)) {
			mBaidumap.clear();
			markers.clear();
			isrroute = false;
			for (int i = 0; i < hotels.size(); i++) {
				if (hotels.get(i).ll != null) {
					MarkerOptions oob = new MarkerOptions()
							.title(hotels.get(i).name)
							.position(hotels.get(i).ll).icon(dahuinavi_marker)
							.zIndex(i).draggable(true);
					oob.animateType(MarkerAnimateType.grow);
					mMarkerB = (Marker) (mBaidumap.addOverlay(oob));
					markers.add(mMarkerB);
				}
			}
			// MapStatusUpdate u = MapStatusUpdateFactory
			// .newLatLng(hotels.get(0).ll);
			if (hotels.size() > 0) {
				MapStatusUpdate u = MapStatusUpdateFactory
						.newMapStatus(new MapStatus.Builder()
								.target(hotels.get(0).ll).zoom(15).build());
				mBaidumap.setMapStatus(u);
			}
		} else if (v.equals(zhanguan)) {
			mBaidumap.clear();
			markers.clear();
			isrroute = false;
			for (int i = 0; i < zhanguans.size(); i++) {
				MarkerOptions oob = new MarkerOptions()
						.title(zhanguans.get(i).name)
						.position(zhanguans.get(i).ll).icon(dahuinavi_marker)
						.zIndex(i).draggable(true);
				oob.animateType(MarkerAnimateType.grow);
				mMarkerB = (Marker) (mBaidumap.addOverlay(oob));
				markers.add(mMarkerB);
			}
			if (zhanguans.size() > 0) {
				MapStatusUpdate u = MapStatusUpdateFactory
						.newMapStatus(new MapStatus.Builder()
								.target(zhanguans.get(0).ll).zoom(15).build());
				mBaidumap.setMapStatus(u);
			}
		} else if (v.equals(openbdmap_btn)) {
			if (ml1 == null || ml2 == null) {
				Toast.makeText(
						mcontext,
						getResources().getString(
								R.string.please_confirm_start_destination),
						Toast.LENGTH_SHORT).show();
			} else if (ml1 != null && ml2 != null) {
				Toast.makeText(mcontext,
						getResources().getString(R.string.opening_baidumap),
						Toast.LENGTH_SHORT).show();
				// 构建 导航参数
				NaviParaOption para = new NaviParaOption().startPoint(ml1)
						.endPoint(ml2).startName("天安门").endName("百度大厦");
				try {
					BaiduMapNavigation.openBaiduMapNavi(para, mcontext);
				} catch (BaiduMapAppNotSupportNaviException e) {
					e.printStackTrace();
					showDialog();
				}
			} else {
				Toast.makeText(
						mcontext,
						getResources().getString(
								R.string.please_confirm_destination),
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null) {
				return;
			}
			// 这里一般不会走，走到了ifFrirstLoc里面
			ml1 = new LatLng(location.getLatitude(), location.getLongitude());
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaidumap.setMyLocationData(locData);
			if (isFirstLoc && latitude == null) {
				isFirstLoc = false;
				MapStatus.Builder builder = new MapStatus.Builder();
				builder.target(ml1).zoom(15.0f);
				mBaidumap.animateMapStatus(MapStatusUpdateFactory
						.newMapStatus(builder.build()));
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
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
	public void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	public void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	public void onDestroy() {
		// 回收 bitmap 资源
		// bdhotel.recycle();
		// bdmyhotel.recycle();
		// bdhuichang.recycle();
		// bdjingdian.recycle();
		dahuinavi_marker.recycle();
		gSearch.destroy();
		rSearch.destroy();
		mMapView.onDestroy();
		mLocClient.stop();
		BaiduMapNavigation.finish(mcontext);
		super.onDestroy();
	}

	// 获取到经纬度的方法
	@Override
	public void onGetGeoCodeResult(GeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(mcontext,
					getResources().getString(R.string.not_find_result),
					Toast.LENGTH_LONG).show();
			return;
		}
		ml2 = result.getLocation();
		ml2_handler.sendEmptyMessage(1);
		mBaidumap.clear();
		mMarkerA = (Marker) mBaidumap.addOverlay(new MarkerOptions()
				.position(result.getLocation()).title(result.getAddress())
				.icon(dahuinavi_marker).animateType(MarkerAnimateType.grow));
		mBaidumap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
				.getLocation()));
		String strInfo = String.format("纬度：%f 经度：%f",
				result.getLocation().latitude, result.getLocation().longitude);
		// Toast.makeText(mcontext, strInfo, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {

	}

	@Override
	public void onGetDrivingRouteResult(DrivingRouteResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(mcontext,
					getResources().getString(R.string.not_find_result),
					Toast.LENGTH_SHORT).show();
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			// result.getSuggestAddrInfo()
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			DrivingRouteOverlay overlay = new DrivingRouteOverlay(mBaidumap);
			routeOverlay = overlay;
			isrroute = true;
			mBaidumap.setOnMarkerClickListener(overlay);
			routeline = result.getRouteLines().get(0);
			overlay.setData(result.getRouteLines().get(0));
			overlay.addToMap();
			overlay.zoomToSpan();
		}
	}

	@Override
	public void onGetBikingRouteResult(BikingRouteResult arg0) {

	}

	@Override
	public void onGetTransitRouteResult(TransitRouteResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(mcontext,
					getResources().getString(R.string.not_find_result),
					Toast.LENGTH_SHORT).show();
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			// result.getSuggestAddrInfo()
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			TransitRouteOverlay overlay = new TransitRouteOverlay(mBaidumap);
			routeOverlay = overlay;
			isrroute = true;
			mBaidumap.setOnMarkerClickListener(overlay);
			routeline = result.getRouteLines().get(0);
			overlay.setData(result.getRouteLines().get(0));
			overlay.addToMap();
			overlay.zoomToSpan();
		}
	}

	@Override
	public void onGetWalkingRouteResult(WalkingRouteResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(mcontext,
					getResources().getString(R.string.not_find_result),
					Toast.LENGTH_SHORT).show();
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			// result.getSuggestAddrInfo()
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			WalkingRouteOverlay overlay = new WalkingRouteOverlay(mBaidumap);
			routeOverlay = overlay;
			isrroute = true;
			mBaidumap.setOnMarkerClickListener(overlay);
			routeline = result.getRouteLines().get(0);
			overlay.setData(result.getRouteLines().get(0));
			overlay.addToMap();
			overlay.zoomToSpan();
		}
	}

	/**
	 * 提示未安装百度地图app或app版本过低
	 */
	public void showDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
		builder.setMessage(getResources().getString(
				R.string.dahuinavi_openbd_toast));
		builder.setTitle(getResources().getString(
				R.string.dahuinavi_openbd_toast));
		builder.setPositiveButton(getResources().getString(R.string.alert_ok),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						OpenClientUtil.getLatestBaiduMapApp(mcontext);
					}
				});

		builder.setNegativeButton(
				getResources().getString(R.string.alert_cancel),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

		builder.create().show();

	}
}
