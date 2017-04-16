package edu.niit.ydkf.grand_prix.module.base;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.AVException;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.io.IOException;
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
 * 提供基础功能的Fragment,所有的Fragment都应该继承它
 */
public abstract class BaseFragment extends Fragment implements IBaseFragment {
    private Bundle bundle;
    protected ActionBar actionBar;
    private Toolbar mToolbar;
    protected boolean isVisible = false;
    private boolean isPrepared = false;
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

    public void logi(String str) {
        Log.i(getClass().getSimpleName(), str);
    }

    public void toast(String str) {
        Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
    }

    public void snackbar(String str) {
        Snackbar.make(getView(), str, Snackbar.LENGTH_SHORT).show();
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Fresco.initialize(getActivity());
        isPrepared = true;
        onLazyLoad();
        return inflater.inflate(bindLayout(), null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        super.onViewCreated(view, savedInstanceState);
        initToolbar();
        initView(view);
        onBussiness(view);
    }

    protected void showProgressDialog(String str) {
        progressDialog = new ProgressDialog(getActivity(), R.style
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
     * 这里实现Fragment数据的缓加载.
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        String s = this.getClass().getSimpleName();
        if (getUserVisibleHint()) {
            isVisible = true;
            onLazyLoad();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    protected void onLazyLoad() {
        if (isPrepared && isVisible)
            lazyLoad();
    }

    /**
     * 懒加载，只有当当前Fragment可见时才会执行里面的代码
     */
    protected abstract void lazyLoad();

    protected void onInvisible() {
    }

    /**
     * 初始化Toolbar
     */
    private void initToolbar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager tintManager = new SystemBarTintManager(getActivity());
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.persian_green);
        }
        if (getView().findViewById(R.id.toolbar) != null) {
            mToolbar = (Toolbar) getView().findViewById(R.id.toolbar);
            ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
            actionBar = ((AppCompatActivity) getActivity())
                    .getSupportActionBar();
        }
    }

    public Bundle getBundle() {
        return bundle;
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
                getActivity().runOnUiThread(new Runnable() {
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
                getActivity().runOnUiThread(new Runnable() {
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
