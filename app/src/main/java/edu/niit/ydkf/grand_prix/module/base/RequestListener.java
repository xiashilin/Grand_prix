package edu.niit.ydkf.grand_prix.module.base;

/**
 * Created by xsl on 16/1/19.
 */
public interface RequestListener {
    void onConSuccess(String response);

    void onConFailure(String errormsg);
}
