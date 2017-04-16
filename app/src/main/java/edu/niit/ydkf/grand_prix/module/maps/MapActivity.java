//
//import android.app.Activity;
//import android.content.Intent;
//import android.graphics.Color;
//import android.graphics.Point;
//import android.os.Build;
//import android.os.Bundle;
//import android.support.v7.widget.Toolbar;
//import android.view.View;
//import android.widget.Button;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.baidu.location.BDLocation;
//import com.baidu.location.BDLocationListener;
//import com.baidu.location.LocationClient;
//import com.baidu.location.LocationClientOption;
//import com.baidu.mapapi.SDKInitializer;
//import com.baidu.mapapi.map.BaiduMap;
//import com.baidu.mapapi.map.BitmapDescriptor;
//import com.baidu.mapapi.map.BitmapDescriptorFactory;
//import com.baidu.mapapi.map.InfoWindow;
//import com.baidu.mapapi.map.MapPoi;
//import com.baidu.mapapi.map.MapStatusUpdate;
//import com.baidu.mapapi.map.MapStatusUpdateFactory;
//import com.baidu.mapapi.map.MapView;
//import com.baidu.mapapi.map.Marker;
//import com.baidu.mapapi.map.MarkerOptions;
//import com.baidu.mapapi.map.MyLocationConfiguration;
//import com.baidu.mapapi.map.MyLocationData;
//import com.baidu.mapapi.map.OverlayOptions;
//import com.baidu.mapapi.model.LatLng;
//
//import java.util.List;
//
//import edu.niit.ydkf.grand_prix.R;
//import edu.niit.ydkf.grand_prix.utils.SystemBarTintManager;
//
//
///**
// * Created by zhangdk on 2016/1/20.
// */
//public class MapActivity extends Activity {
//    private MapView mapView;
//    private BaiduMap mBaiduMap;
//    //阿萨德
//
//
//    //定位相关
//    private LocationClient mLocationClient;
//    private MyLocationListener myLocationListener;
//    private boolean isFirstIn = true;
//    private double mLatitude;
//    private double mLongtitude;
//    //图标相关
//    private MyOrientationListener myOrientationListener;
//    private MyLocationConfiguration.LocationMode mLcationMode;
//    private float mCrrentX;
//
//    //搜索相关
//    private Button btn_place;
//    //覆盖物相关
//    private BitmapDescriptor mMakers;
//    private LinearLayout mMakerly;
////    private PoiSearch mPoiSearch;
////    private List<PoiInfo> nearList;
////
////    //半径范围
////    private PoiNearbySearchOption option;
////    private int radius;
////    private String keyWords;
////    private boolean isLoc;
//
//    private BDLocation location;
//
//    //    private Handler handler = new Handler() {
////        @Override
////        public void handleMessage(Message msg) {
////            switch (msg.what) {
////                case 0:
////                    mapView.
////                    break;
////            }
////        }
////    };
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        // 注意该方法是在setContentView之前调用
//        SDKInitializer.initialize(getApplicationContext());
//        setContentView(R.layout.mapview_activity);
//
//        initView();
//        //初始化定位
//        initLocation();
//
//        //初始化覆盖物
//        initMaker();
//
//        //进入之后先定位一下我的位置
//        LatLng latlng = new LatLng(mLatitude, mLongtitude);
//        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latlng);
//        mBaiduMap.setMapStatus(msu);
//
//        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
//            @Override
//            public boolean onMarkerClick(Marker marker) {
//                //获取maker中的数据
//                Bundle extraInfo = marker.getExtraInfo();
//                final info infos = (info) extraInfo.getSerializable("info");
//                mMakerly.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(MapActivity.this, EventDetailedActivity.class);
//                        intent.putExtra("id", infos.getObjid());
//                        startActivity(intent);
//                    }
//                });
//                TextView name = (TextView) mMakerly.findViewById(R.id.id_info_name);
//                TextView distance = (TextView) mMakerly.findViewById(R.id.id_info_distance);
//                TextView place = (TextView) findViewById(R.id.id_info_place);
//                name.setText(infos.getName());
//                place.setText(infos.getPlace());
//                distance.setText(infos.getDistance() + "米");
//
//                //添加位置文本
//                InfoWindow infoWindow;
//                TextView tv = new TextView(getApplicationContext());
//                tv.setBackgroundResource(R.drawable.location_tips);
//                tv.setPadding(30, 20, 30, 50);
//                tv.setText(infos.getName());
//                tv.setTextColor(Color.parseColor("#ffffff"));
//                LatLng latLng = marker.getPosition();
//                //将经纬度转化为屏幕上的坐标
//                Point p = mBaiduMap.getProjection().toScreenLocation(latLng);
//                p.y -= 40;//设置偏移量
//                //将屏幕上的坐标转化为实际经纬度
//                LatLng ll = mBaiduMap.getProjection().fromScreenLocation(p);
//                infoWindow = new InfoWindow(tv, ll, 0);
//                mBaiduMap.showInfoWindow(infoWindow);
//                mMakerly.setVisibility(View.VISIBLE);
//
//                return true;
//            }
//        });
//
//        //地图上的点击事件
//        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(LatLng latLng) {
//                mMakerly.setVisibility(View.GONE);
//                mBaiduMap.hideInfoWindow();
//            }
//
//            @Override
//            public boolean onMapPoiClick(MapPoi mapPoi) {
//                return false;
//            }
//        });
//    }
//
//    private void initMaker() {
//        mMakers = BitmapDescriptorFactory.fromResource(R.drawable.maker);
//        mMakerly = (LinearLayout) findViewById(R.id.id_maker_ly);
//    }
//
//    private void initLocation() {
//        mLcationMode = MyLocationConfiguration.LocationMode.NORMAL;
//        mLocationClient = new LocationClient(this);
//        myLocationListener = new MyLocationListener();
//        mLocationClient.registerLocationListener(myLocationListener);
//
//        LocationClientOption option = new LocationClientOption();
//        option.setCoorType("bd09ll");//坐标类型
//        option.setIsNeedAddress(true);
//        option.setOpenGps(true);
//        option.setScanSpan(1000);
//        mLocationClient.setLocOption(option);
//
//        //初始化传感器
//        myOrientationListener = new MyOrientationListener(this);
//        myOrientationListener.setOnOrientationListener(new MyOrientationListener.OnOrientationListener() {
//            @Override
//            public void onOrientationChanged(float x) {
//                mCrrentX = x;
//            }
//        });
//    }
//
//    private void initView() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            SystemBarTintManager tintManager = new SystemBarTintManager(this);
//            tintManager.setStatusBarTintEnabled(true);
//            tintManager.setStatusBarTintResource(R.color.persian_green);
//        }
//        if (findViewById(R.id.toolbar) != null) {
//            Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
//            mToolbar.setTitle("");
//            mToolbar.setNavigationIcon(R.drawable.ic_chevron_left_white_36dp);
//            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    finish();
//                }
//            });
//        }
//        mapView = (MapView) findViewById(R.id.id_bmapView);
//        btn_place = (Button) findViewById(R.id.btn_Myplace);
////        mPoiSearch = PoiSearch.newInstance();
//        location = new BDLocation();
////        nearList = new ArrayList<>();
//        mBaiduMap = mapView.getMap();
//        //设置地图的放大比例
//        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(12.0f);
//        mBaiduMap.setMapStatus(msu);
//        btn_place.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                centerToMyLocation();
//            }
//        });
//        addOverlays(App.infos);
////        btn_overlay.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                option = new PoiNearbySearchOption();
////                String keyWord = et_search.getText().toString().trim();
////                option.sortType(PoiSortType.distance_from_near_to_far);
////                option.location(new LatLng(mLatitude, mLongtitude));
////                if (radius != 0) {
////                    option.radius(radius);
////                } else {
////                    option.radius(5000);//范围
////                }
////                option.pageCapacity(20);//默认每页20
////                mPoiSearch.searchNearby(option);
////                option.keyword(keyWord);
////                mPoiSearch.searchNearby(option);
////                addOverlays(info.infos);
////            }
////        });
////        //接受周围
////        mPoiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
////            @Override
////            public void onGetPoiResult(PoiResult poiResult) {
////                if (poiResult != null) {
////                    if (poiResult.getAllPoi() != null && poiResult.getAllPoi().size() > 0) {
////                        nearList.addAll(poiResult.getAllPoi());
////                    }
////                }
////            }
////
////            @Override
////            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
////
////            }
////        });
//
//    }
//
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        mBaiduMap.setBuildingsEnabled(true);
//        if (!mLocationClient.isStarted()) {
//            //开启定位
//            mLocationClient.start();
//            //开启方向传感器
//            myOrientationListener.start();
//        }
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        //停止定位
//        mBaiduMap.setBuildingsEnabled(false);
//        mLocationClient.stop();
//        //停止方向传感器
//        myOrientationListener.stop();
//
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (mapView != null)
//            mapView.onResume();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (mapView != null)
//            mapView.onPause();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (mapView != null)
//            mapView.onDestroy();
//    }
//
//    //添加覆盖物
//
//    private void addOverlays(List<info> infos) {
//        mBaiduMap.clear();
//        LatLng latLng = null;
//        Marker marker = null;
//        for (info info : infos) {
//            //经纬度
//            latLng = new LatLng(info.getLatitude(), info.getLngtitude());
//            //图标
//            OverlayOptions options = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.maker)).zIndex(5);
//            marker = (Marker) (mBaiduMap.addOverlay(options));
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("info", info);
//            marker.setExtraInfo(bundle);
//        }
//        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
//        mBaiduMap.setMapStatus(msu);
//    }
//
//    //定位到我的位置
//    private void centerToMyLocation() {
//        LatLng latlng = new LatLng(mLatitude, mLongtitude);
//        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latlng);
//        mBaiduMap.setMapStatus(msu);
//    }
//
//    class MyLocationListener implements BDLocationListener {
//        @Override
//        public void onReceiveLocation(BDLocation bdLocation) {
//            MyLocationData data = new MyLocationData.Builder()
//                    .direction(mCrrentX)
//                    .accuracy(bdLocation.getRadius())
//                    .latitude(bdLocation.getLatitude())
//                    .longitude(bdLocation.getLongitude()).build();
//            mBaiduMap.setMyLocationData(data);
//            //每次定位成功之后更新一下经纬度
//            mLatitude = bdLocation.getLatitude();
//            mLongtitude = bdLocation.getLongitude();
//            //设置图标
//            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.navi_map_gps_locked);
//            MyLocationConfiguration config = new MyLocationConfiguration(mLcationMode, true, bitmapDescriptor);
//            mBaiduMap.setMyLocationConfigeration(config);
//
//
//            if (isFirstIn) {
//                //定位到我的位置
//                LatLng latlng = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
//                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latlng);
//                mBaiduMap.setMapStatus(msu);
//                isFirstIn = false;
//            }
//        }
//    }
//
//}
