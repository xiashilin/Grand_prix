package edu.niit.ydkf.grand_prix.module.base;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.AVException;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import butterknife.ButterKnife;
import edu.niit.ydkf.grand_prix.App;
import edu.niit.ydkf.grand_prix.R;
import edu.niit.ydkf.grand_prix.common.Params;
import edu.niit.ydkf.grand_prix.common.utils.SystemBarTintManager;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by xsl on 16/1/15.
 * 提供基础功能的Activity,所有的Activity都应该继承它
 */
public abstract class BaseActivity extends AppCompatActivity implements IBaseActivity {
    protected Toolbar mToolbar;
    private View view;
    private ProgressDialog progressDialog;

    public void logd(String str) {
        Log.d(getClass().getSimpleName(), str);
    }

    public void loge(String str) {
        Log.e(getClass().getSimpleName(), str);
    }

    public void logv(String str) {
        Log.v(getClass().getSimpleName(), str);
    }

    public void toast(String str) {
        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
    }

    public void snackbar(String str) {
        Snackbar.make(getRootView(), str, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = this.getLayoutInflater().inflate(bindLayout(), null);
        setContentView(view);
        ButterKnife.bind(this);
        initToolbar();
        init(view);
    }

    protected void showProgressDialog(String str) {
        progressDialog = new ProgressDialog(this, R.style
                .AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(str);
        progressDialog.show();
    }

    protected void dismisProgressDialog() {
        if (progressDialog != null)
            if (progressDialog.isShowing())
                progressDialog.dismiss();
    }

    /**
     * 初始化Toolbar
     */
    private void initToolbar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.primary);
        }
        if (findViewById(R.id.toolbar) != null) {
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(mToolbar);
            ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle("");
        }
    }


    /**
     * 得到根视图
     *
     * @return 根布局视图
     */
    protected View getRootView() {
        return view;
    }

    /**
     * 开启返回键的按钮
     */
    protected void enableBack() {
        if (mToolbar != null) {
            mToolbar.setNavigationIcon(R.drawable.ic_chevron_left_white_36dp);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBack();
                    finish();
                }
            });
        }
    }

    protected void onBack() {
    }

    public static String sha1(String input) {
        MessageDigest mDigest = null;
        try {
            mDigest = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] result = mDigest.digest(input.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

    /**
     * 请求网络连接(无请求头)
     *
     * @param url         请求地址
     * @param requestBody 请求的内容
     * @param listener    数据回调
     *                    2016年03月25日 by:liuhaitian
     */
    public void request(String url, Params requestBody, final RequestListener listener) {
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : requestBody.getMap().entrySet()) {
            builder.add(entry.getKey(), entry.getValue());
        }
        RequestBody body = builder.build();
        final Request request = new Request.Builder().url(url).post(body).build();
        new Thread() {
            @Override
            public void run() {
                super.run();
                String result = "";
                int flag = 0;
                try {
                    Response response = App.client.newCall(request).execute();
                    result = response.body().string();
                    flag = 1;
                } catch (IOException e) {
                    e.printStackTrace();
                    result = e.getMessage();
                    flag = 2;
                }
                final int finalFlag = flag;
                final String finalResult = result;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (finalFlag == 1)
                            listener.onConSuccess(finalResult);
                        if (finalFlag == 2)
                            listener.onConFailure(finalResult);
                    }
                });

            }
        }.start();
    }

    /**
     * 请求网络连接
     *
     * @param url         请求地址
     * @param header      请求头
     * @param requestBody 请求的内容
     * @param listener    数据回调
     *                    2016年03月25日 by:liuhaitian
     */
    public void request(String url, Params header, Params requestBody, final RequestListener listener) {
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        Headers.Builder headerBuilder = new Headers.Builder();
        for (Map.Entry<String, String> entry : requestBody.getMap().entrySet()) {
            bodyBuilder.add(entry.getKey(), entry.getValue());
        }
        for (Map.Entry<String, String> entry : header.getMap().entrySet()) {
            headerBuilder.add(entry.getKey(), entry.getValue());
        }
        Headers headers = headerBuilder.build();
        RequestBody body = bodyBuilder.build();
        final Request request = new Request.Builder().url(url).headers(headers).post(body).build();
        new Thread() {
            @Override
            public void run() {
                super.run();
                String result = "";
                int flag = 0;
                try {
                    Response response = App.client.newCall(request).execute();
                    result = response.body().string();
                    flag = 1;
                } catch (IOException e) {
                    e.printStackTrace();
                    result = e.getMessage();
                    flag = 2;
                }
                final int finalFlag = flag;
                final String finalResult = result;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (finalFlag == 1)
                            listener.onConSuccess(finalResult);
                        if (finalFlag == 2)
                            listener.onConFailure(finalResult);
                    }
                });

            }
        }.start();
    }


    public <T> void dealData(T value, AVException exception, IBaseCallBack<T>
            callBack) {
        if (exception == null)
            callBack.onDataSuccess(value);
        else {
            try {
                JSONObject jsonObject = JSONObject.parseObject(exception.getMessage());
                if (exception.getCode() == 100 || exception.getCode() == 0)
                    snackbar("网络连接失败,请检查网络");
                else {
                    snackbar(jsonObject.getString("error"));
                    callBack.onException(jsonObject);
                }
            } catch (Exception e) {
                snackbar("网络连接失败,请检查网络");
            }
        }

    }

    public <T> void dealData(AVException exception, IBaseCallBack<T>
            callBack) {
        if (exception == null)
            callBack.onDataSuccess(null);
        else {
            try {
                dismisProgressDialog();
                JSONObject jsonObject = JSONObject.parseObject(exception.getMessage());
                if (exception.getCode() == 100 || exception.getCode() == 0)
                    snackbar("网络连接失败,请检查网络");
                else {
                    snackbar(jsonObject.getString("error"));
                    callBack.onException(jsonObject);
                }
            } catch (Exception e) {
                snackbar("网络连接失败,请检查网络");
            }
        }

    }
}