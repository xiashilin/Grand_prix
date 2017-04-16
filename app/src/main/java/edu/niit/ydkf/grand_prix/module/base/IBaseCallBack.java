package edu.niit.ydkf.grand_prix.module.base;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by xsl on 16/2/27.
 */
public interface IBaseCallBack<T> {

    void onDataSuccess(T value);

    void onException(JSONObject e);

}