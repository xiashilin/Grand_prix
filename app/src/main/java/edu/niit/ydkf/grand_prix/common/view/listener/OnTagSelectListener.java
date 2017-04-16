package edu.niit.ydkf.grand_prix.common.view.listener;

import java.util.List;

import edu.niit.ydkf.grand_prix.common.view.FlowTagLayout;

/**
 * Created by HanHailong on 15/10/20.
 */
public interface OnTagSelectListener {
    void onItemSelect(FlowTagLayout parent, List<Integer> selectedList);
}
