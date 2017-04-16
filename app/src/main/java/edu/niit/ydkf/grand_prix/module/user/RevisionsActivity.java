package edu.niit.ydkf.grand_prix.module.user;

import android.view.View;

import edu.niit.ydkf.grand_prix.R;
import edu.niit.ydkf.grand_prix.module.base.BaseActivity;


/**
 * Created by hp on 2016/3/22.
 */
public class RevisionsActivity extends BaseActivity {

    @Override
    public void init(View view) {
        enableBack();
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_revisions;
    }
}
