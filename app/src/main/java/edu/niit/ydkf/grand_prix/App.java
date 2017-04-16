package edu.niit.ydkf.grand_prix;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.PushService;

import edu.niit.ydkf.grand_prix.common.utils.SPUtils;
import edu.niit.ydkf.grand_prix.config.ConString;
import edu.niit.ydkf.grand_prix.module.im.MessageBoxActivity;
import io.rong.imkit.RongIM;
import okhttp3.OkHttpClient;

/**
 * Created by xsl on 16/1/14.
 */
public class App extends Application {
    public static OkHttpClient client;
    private static String userid;
    private static String username;
    private static String iconurl;
    private static int useruid;
    private static AVUser currentUser;
//    public static LocationClient mLocationClient = null;
//    public BDLocationListener myListener = new MyLocationListener();

    public static int getUseruid() {
        return useruid;
    }


    //判断客户端是否为登录状态
    public static boolean isLogin(Context context) {
        return (boolean) SPUtils.get(context, "isLogin", false);
    }

    public static String getUserid() {
        return userid;
    }


    public static String getUsername() {
        return username;
    }


    public static String getIconurl() {
        return iconurl;
    }

    public static AVUser getCurrentUser() {
        return currentUser;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
        getLocation();
        client = new OkHttpClient();
//        myListener = new MyLocationListener(getApplicationContext());
//        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
//        mLocationClient.registerLocationListener(myListener);    //注册监听函数
//        initLocation();
//        mLocationClient.start();
        flushUserInfo(getApplicationContext());
    }

    private void init() {
        AVOSCloud.initialize(this, "YqEwNrMmrPe5Kj52vKwLnRnH-gzGzoHsz", "UqnYj2IawNKFlN0L6MIwHi8j");
        AVInstallation.getCurrentInstallation().saveInBackground();
//        SDKInitializer.initialize(getApplicationContext());
        PushService.setDefaultPushCallback(this, MessageBoxActivity.class);
        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext())) ||
                "io.rong.push".equals(getCurProcessName(getApplicationContext())))
            RongIM.init(this);
    }

    public static void flushUserInfo(Context context) {
        currentUser = AVUser.getCurrentUser();
        userid = (String) SPUtils.get(context, "userid", "");
        username = (String) SPUtils.get(context, "username", "");
        iconurl = (String) SPUtils.get(context, "iconurl", ConString.DEFAULT_ICON);
        useruid = (int) SPUtils.get(context, "useruid", 0);
    }


    //当内存不足/应用程序关闭时被调用
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (!(boolean) SPUtils.get(getBaseContext(), "isAutoLogin", false)) {
            SPUtils.put(getBaseContext(), "isLogin", false);
        }
    }

    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    //    private void initLocation() {
//        LocationClientOption option = new LocationClientOption();
//        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
//        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
//        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
//        option.setIsNeedAddress(false);//可选，设置是否需要地址信息，默认不需要
//        option.setOpenGps(true);//可选，默认false,设置是否使用gps
//        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
//        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
//        mLocationClient.setLocOption(option);
//    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void getLocation() {
//        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
//        initLocation();
//        mLocationClient.registerLocationListener(myListener);    //注册监听函数
//        mLocationClient.start();
    }

    private void initLocation() {
//        LocationClientOption option = new LocationClientOption();
//        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
//        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
//        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
//        int span = 1000;
//        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
//        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
//        option.setOpenGps(true);//可选，默认false,设置是否使用gps
//        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
//        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
//        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
//        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
//        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
//        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
//        mLocationClient.setLocOption(option);
    }
}