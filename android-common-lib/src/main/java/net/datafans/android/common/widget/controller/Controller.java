package net.datafans.android.common.widget.controller;

import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import net.datafans.android.common.AndroidCommon;
import net.datafans.android.common.R;
import net.datafans.android.common.data.service.BaseResponse;
import net.datafans.android.common.data.service.DataService;
import net.datafans.android.common.data.service.DataServiceDelegate;
import net.datafans.android.common.helper.LogHelper;
import net.datafans.android.common.lib.systembar.SystemBarTintManager;

public abstract class Controller extends AppCompatActivity implements DataServiceDelegate {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (enableFullScreen()) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        if (!enableFullScreen())
            initStatusBar();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);
    }

    protected boolean enableFullScreen() {
        return false;
    }

    private void initStatusBar() {

        //if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);

        if (getStatusBarColor() != 0) {
            tintManager.setTintColor(getStatusBarColor());
        } else {
            TypedArray array = getTheme().obtainStyledAttributes(new int[]{
                    R.attr.colorPrimaryDark,
            });

            int primaryColorDark = array.getColor(0, 0xFF11FF);
            array.recycle();

            tintManager.setTintColor(primaryColorDark);
        }
    }


    protected int getStatusBarColor() {
        return 0;
    }

    protected int getToolBarColor() {
        return 0;
    }

    @Override
    public void onStatusOk(BaseResponse response, DataService service) {

    }

    @Override
    public void onStatusError(BaseResponse response, DataService service) {
        LogHelper.error(response.toString());
        Toast toast = Toast.makeText(this, response.getErrorMsg(),
                Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onRequestError(int errorCode, byte[] errorResponse,
                               Throwable throwable, DataService service) {
        if (errorCode == -2) {
            LogHelper.error("network exception");
            Toast toast = Toast.makeText(this, "网络异常", Toast.LENGTH_SHORT);
            toast.show();
        }

        if (errorCode == -1) {
            LogHelper.error("data_parse_exception");
            Toast toast = Toast.makeText(this, "数据解析错误", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
