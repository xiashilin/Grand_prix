package edu.niit.ydkf.grand_prix.module.launch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.Window;
import android.content.SharedPreferences.Editor;

import edu.niit.ydkf.grand_prix.R;
import edu.niit.ydkf.grand_prix.module.MainActivity;


/**
 * Created by liuhaitian on 16/3/21.
 * 欢迎页
 */
public class WelcomeActivity extends Activity {
    private static final int ENTER_HOME = 0x00;
    private static final int ENTER_SPLASH = 0x01;
    private static final String FILE_NAME = "first";
    private int waitTime = 3 * 1000;
    //    @Bind(R.id.software_version_tv)
//    TextView mSoftwareVersionTV;
    private SharedPreferences mPreferences;
    private Handler mHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_welcome);

        // 先得到SharedPreferences
        getPreference();
        // 再进行消息处理
        handleMessage();
//        // 初始化TextView
//        initView();
//        // 给TextView设置值
//        setText();
        // 控制跳转
        into();
    }


    private void handleMessage() {
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                if (message.what == ENTER_HOME) {
                    //进入主界面
                    startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                } else {
                    //将APP启动值设置为false
                    Editor editor = mPreferences.edit();
                    editor.putBoolean("isFirst", false);
                    editor.commit();
                    //进入引导界面
                    startActivity(new Intent(WelcomeActivity.this, GuideActivity.class));
                }
                finish();
                return false;
            }
        });
    }

    //跳转
    private void into() {
        if (isFirstRun()) {
            mHandler.sendEmptyMessageDelayed(ENTER_SPLASH, waitTime);
        } else {
            mHandler.sendEmptyMessageDelayed(ENTER_HOME, waitTime);
        }

    }

    //给TextView设置
//    private void setText() {
//        mSoftwareVersionTV.setText(getString(R.string.software_version) + getVersion());
//    }
//
//    private void initView() {
//        mSoftwareVersionTV = (TextView) findViewById(R.id.software_version_tv);
//    }

    private void getPreference() {
        mPreferences = getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

    //判断是不是第一次进入
    private Boolean isFirstRun() {
        return mPreferences.getBoolean("isFirst", true);
    }

    //程序版本号
//    private String getVersion() {
//        String versionString = null;
//        try {
//            PackageManager manager = getPackageManager();
//            PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
//            versionString = info.versionName;
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        return versionString;
//    }


}
