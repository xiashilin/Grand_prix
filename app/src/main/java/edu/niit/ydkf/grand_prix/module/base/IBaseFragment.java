package edu.niit.ydkf.grand_prix.module.base;

import android.view.View;

/**
 * Created by xsl on 16/1/15.
 */
public interface IBaseFragment {
    int bindLayout();

    void initView(View view);

    void onBussiness(View view);

}
