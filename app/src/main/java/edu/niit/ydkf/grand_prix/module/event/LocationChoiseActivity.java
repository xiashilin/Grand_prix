//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Message;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.AdapterView;
//import android.widget.BaseAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.baidu.location.BDLocation;
//import com.baidu.location.BDLocationListener;
//import com.baidu.location.LocationClient;
//import com.baidu.location.LocationClientOption;
//import com.baidu.mapapi.SDKInitializer;
//import com.baidu.mapapi.map.BaiduMap;
//import com.baidu.mapapi.map.BitmapDescriptorFactory;
//import com.baidu.mapapi.map.MapStatusUpdate;
//import com.baidu.mapapi.map.MapStatusUpdateFactory;
//import com.baidu.mapapi.map.MapView;
//import com.baidu.mapapi.map.Marker;
//import com.baidu.mapapi.map.MarkerOptions;
//import com.baidu.mapapi.map.MyLocationConfiguration;
//import com.baidu.mapapi.map.MyLocationData;
//import com.baidu.mapapi.map.OverlayOptions;
//import com.baidu.mapapi.model.LatLng;
//import com.baidu.mapapi.search.core.PoiInfo;
//import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
//import com.baidu.mapapi.search.poi.PoiDetailResult;
//import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
//import com.baidu.mapapi.search.poi.PoiResult;
//import com.baidu.mapapi.search.poi.PoiSearch;
//import com.baidu.mapapi.search.poi.PoiSortType;
//import com.baidu.mapapi.utils.CoordinateConverter;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//
///**
// * Created by zhangdk on 16/2/24.
// */
//public class LocationChoiseActivity extends BaseActivity {
//    private MapView mMapView;
//    private BaiduMap mBaidumap;
//    private Button btn_MyLcation, btn_ok;
//    private EditText searchEt;
//    private ListView mListView;
//    private MyAdapter mAdapter;
//    private List<PoiInfo> nearList;
//    public static Map<Integer, Boolean> isSelected;
//    //定位相关
//    //定位的客户端
//    private LocationClient mLocationClient;
//    //定位的监听器
//    private MyLocationListener myLocationListener;
//    //当前定位的模式
//    private MyLocationConfiguration.LocationMode mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
//    //是否是第一次定位
//    private boolean isFirstIn = true;
//    private double mLatitude;
//    private double mLongtitude;
//    private String address;
//
//
//    //Maker表示为标记
//    private Marker mCurrentMaker;
//    private Marker markerA;
//
//    private PoiSearch mPoiSearch;
//
//    //半径范围
//    private PoiNearbySearchOption option;
//    //    private int radius;
//    private String keyWords;
//    private boolean isLoc;
//
////    private Handler handler = new Handler() {
////        @Override
////        public void handleMessage(Message msg) {
////            switch (msg.what) {
////                case 0:
////                    mAdapter.notifyDataSetChanged();
////                    break;
////            }
////        }
////    };
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        SDKInitializer.initialize(getApplicationContext());
//        enableBack();
//        initView();
//        //初始化定位
//        initLocation();
//    }
//
//    private void initLocation() {
//        mLocationClient = new LocationClient(this);
//        myLocationListener = new MyLocationListener();
//        mLocationClient.registerLocationListener(myLocationListener);
//
//        LocationClientOption option = new LocationClientOption();
//        option.setCoorType("bd09ll");//设置坐标类型
//        option.setIsNeedAddress(true);
//        option.setOpenGps(true);//打开GPS
//        option.setScanSpan(1000);
//        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
//        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
//        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
//        mLocationClient.setLocOption(option);
//    }
//
//    private void initView() {
//        mMapView = (MapView) findViewById(R.id.id_bmapView);
//        btn_MyLcation = (Button) findViewById(R.id.btn_MyLocation);
//        btn_ok = (Button) findViewById(R.id.btn_ok);
//        searchEt = (EditText) findViewById(R.id.et_input);
//        mListView = (ListView) findViewById(R.id.mListView);
//        nearList = new ArrayList<PoiInfo>();
//        mAdapter = new MyAdapter();
//        mListView.setAdapter(mAdapter);
//        mBaidumap = mMapView.getMap();
//        isSelected = new HashMap<Integer, Boolean>();
//        mPoiSearch = PoiSearch.newInstance();
//        mMapView.showZoomControls(false);
//        //设置地图的放大比例
//        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
//        mBaidumap.setMapStatus(msu);
////        btn_release.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                //回调显示
////
////            }
////        });
//        btn_MyLcation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                LatLng latlng = new LatLng(mLatitude, mLongtitude);
//                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latlng);
//                mBaidumap.animateMapStatus(msu);
//            }
//        });
//        //周边地理位置列表的点击事件
//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
////                mAdapter.setSelected(i);
////                mAdapter.notifyDataSetChanged();
//                PoiInfo ad = (PoiInfo) mAdapter.getItem(i);
//                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ad.location);
//                mBaidumap.animateMapStatus(u);
////                if (!isLoc) {
////                    mCurrentMaker.setPosition(ad.location);
////                } else {
////                    markerA.setPosition(ad.location);
////                }
//                Intent intent = new Intent();
//                intent.putExtra("location", ad);
//                setResult(RESULT_OK, intent);
//                finish();
//            }
//        });
//
//        //接受周边地理位置
//        mPoiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
//            @Override
//            public void onGetPoiResult(PoiResult poiResult) {
//                Message msg = new Message();
//                if (poiResult != null) {
//                    if (poiResult.getAllPoi() != null && poiResult.getAllPoi().size() > 0) {
//                        if (nearList.size() != 0)
//                            nearList.clear();
//                        nearList.addAll(poiResult.getAllPoi());
//                        mAdapter.notifyDataSetChanged();
//                        if (nearList != null && nearList.size() > 0) {
//                            for (int i = 0; i < nearList.size(); i++) {
//                                isSelected.put(i, false);
//                            }
//                        } else {
//                            msg.what = 0;
//                        }
////                        msg.what = 0;
////                        handler.sendMessage(msg);
//                    }
//
//                }
//            }
//
//            @Override
//            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
//
//            }
//        });
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        //开启图层定位
//        mBaidumap.setMyLocationEnabled(true);
//        if (!mLocationClient.isStarted()) {
//            mLocationClient.start();
//        }
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        //关闭图层定位
//        mBaidumap.setMyLocationEnabled(false);
//        mLocationClient.stop();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mMapView.onDestroy();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        mMapView.onResume();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        mMapView.onPause();
//    }
//
//    @Override
//    public void init(View view) {
//
//    }
//
//    @Override
//    public int bindLayout() {
//        return R.layout.layout_choise;
//    }
//
//    class MyLocationListener implements BDLocationListener {
//        @Override
//        public void onReceiveLocation(BDLocation bdLocation) {
//            //map view 销毁后不再处理新接受的位置
//            if (bdLocation == null || mMapView == null)
//                return;
//            mBaidumap.clear();
//            //构造定位数据
//            MyLocationData data = new MyLocationData.Builder()
//                    .accuracy(bdLocation.getRadius())
//                    .direction(100)
//                    .latitude(bdLocation.getLatitude())
//                    .longitude(bdLocation.getLongitude()).build();
//
//            mBaidumap.setMyLocationData(data);
//            address = bdLocation.getAddrStr();
//            //每次定位成功之后更新一下经纬度
//            mLatitude = bdLocation.getLatitude();
//            mLongtitude = bdLocation.getLongitude();
//
//            if (isFirstIn) {
//                //定位到我的位置
//                isFirstIn = false;
//                LatLng llA = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
//                CoordinateConverter converter = new CoordinateConverter();
//                converter.coord(llA);
//                converter.from(CoordinateConverter.CoordType.COMMON);
//                LatLng convertLatLng = converter.convert();
////                OverlayOptions ooA = new MarkerOptions().position(convertLatLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.maker))
////                        .zIndex(4).draggable(true);
////                mCurrentMaker = (Marker) mBaidumap.addOverlay(ooA);
//                MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(convertLatLng, 12.0f);
//                mBaidumap.animateMapStatus(u);
//                toast(address);
//            }
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    searchNeayBy();
//                }
//            }).start();
//        }
//
//        public void onReceivePoi(BDLocation poiLocation) {
//            if (poiLocation == null) {
//                return;
//            }
//        }
//    }
//
//    /*
//  * 显示经纬度的位置
//  * */
//    private void showMap(double latitude, double longtitude, String address) {
//        LatLng llA = new LatLng(latitude, longtitude);
//        CoordinateConverter converter = new CoordinateConverter();
//        converter.coord(llA);
//        converter.from(CoordinateConverter.CoordType.COMMON);
//        LatLng convertLatLng = converter.convert();
//        OverlayOptions ooA = new MarkerOptions().position(convertLatLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.maker))
//                .zIndex(6).draggable(true);
//        markerA = (Marker) (mBaidumap.addOverlay(ooA));
//        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(convertLatLng, 16.0f);
//        mBaidumap.animateMapStatus(u);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                searchNeayBy();
//            }
//        }).start();
//    }
//
//    /**
//     * 搜索周边地理位置
//     */
//    private void searchNeayBy() {
//        option = new PoiNearbySearchOption();
//        option.sortType(PoiSortType.distance_from_near_to_far);
//        option.location(new LatLng(mLatitude, mLongtitude));
////        if (radius != 0) {
////            option.radius(radius);
////        } else {
//        option.radius(100000);//范围
////        }
//        option.pageCapacity(20);//默认每页20
//        keyWords = searchEt.getText().toString();
//        if (keyWords.length() == 0) {
//            option.keyword("球场");
//            mPoiSearch.searchNearby(option);
//        } else {
//            btn_ok.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
//                            .hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager
//                                    .HIDE_NOT_ALWAYS);
////                    mListView.clearAnimation();
//                    option.keyword(keyWords);
//                    mPoiSearch.searchNearby(option);
//                }
//            });
//        }
//
//
//    }
//
//
//    public class MyAdapter extends BaseAdapter {
////        private LayoutInflater mInflater;
////        private int mSelected;
////        private List<PoiInfo> nearList;
////        private Map<Integer, Boolean> isSelected;
////
////        public MyAdapter(Context context, List<PoiInfo> nearList, Map<Integer, Boolean> isSelected) {
////            this.mInflater = LayoutInflater.from(context);
////            this.nearList = nearList;
////            this.isSelected = isSelected;
////        }
//
//        @Override
//        public int getCount() {
//            return nearList.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return nearList.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(int postion, View convertView, ViewGroup parent) {
//            ViewHolder viewHolder;
//            if (convertView == null) {
//                viewHolder = new ViewHolder();
//                convertView = getLayoutInflater().inflate(R.layout.choise_item, null);
//                viewHolder.tv_palce1 = (TextView) convertView.findViewById(R.id.place1);
//                viewHolder.tv_place2 = (TextView) convertView.findViewById(R.id.place2);
//                convertView.setTag(viewHolder);
//            } else {
//                viewHolder = (ViewHolder) convertView.getTag();
//            }
//            PoiInfo info = nearList.get(postion);
//            viewHolder.tv_palce1.setText(info.name);
//            viewHolder.tv_place2.setText(info.address);
//            return convertView;
//        }
//
//
////        public void setSelected(int selected) {
////            this.mSelected = selected;
////            notifyDataSetChanged();
////        }
//    }
//
//    class ViewHolder {
//        public TextView tv_palce1, tv_place2;
//    }
//}
