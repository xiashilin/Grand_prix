package edu.niit.ydkf.grand_prix.module.user;


import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;
import edu.niit.ydkf.grand_prix.R;
import edu.niit.ydkf.grand_prix.module.base.BaseActivity;

/**
 * Created by hp on 2016/3/18.
 */
public class AboutActivity extends BaseActivity {
    @Bind(R.id.tv_toolbar_title)
    TextView tv_toolbartitle;
    private RelativeLayout rl_feedback, rl_function_introduction, rl_revisions;

    @OnClick(R.id.rl_feedback)
    public void feedback() {
        Intent intent = new Intent(AboutActivity.this, FeedbackActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.rl_function_introduction)
    public void introduction() {
        Intent intent = new Intent(AboutActivity.this, FunctionIntroductionActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.rl_revisions)
    public void revisions() {
        Intent intent = new Intent(AboutActivity.this, RevisionsActivity.class);
        startActivity(intent);

    }


    @Override
    public void init(View view) {
        enableBack();
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_about;
    }


}
