package edu.niit.ydkf.grand_prix.module.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import edu.niit.ydkf.grand_prix.R;


/**
 * Created by xsl on 16/1/15.
 */
public class BaseFragmentActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        String fragTitle = getIntent().getStringExtra("title");
        try {
            Class<BaseFragment> class1 = (Class<BaseFragment>) Class.forName(fragTitle);
            BaseFragment fragment = class1.newInstance();
            fragment.setBundle(bundle);
            getSupportFragmentManager().beginTransaction().add(R.id.main_content, fragment, fragTitle).commit();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    public static void launch(Context context, String fragmentTitle, Bundle
            bundle) {
        Intent intent = new Intent(context, BaseFragmentActivity.class);
        intent.putExtras(bundle);
        intent.putExtra("title", fragmentTitle);
        context.startActivity(intent);
    }

    @Override
    public void init(View view) {

    }

    @Override
    public int bindLayout() {
        return R.layout.activity_maincontent;
    }
}
