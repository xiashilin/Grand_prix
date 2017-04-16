package edu.niit.ydkf.grand_prix.module.event;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import butterknife.Bind;
import edu.niit.ydkf.grand_prix.R;
import edu.niit.ydkf.grand_prix.module.base.BaseActivity;

/**
 * Created by xsl on 16/3/30.
 */
public class IntroduceActivity extends BaseActivity {
    @Bind(R.id.et_introduce)
    EditText et_introduce;

    @Override
    public void init(View view) {
        enableBack();
        et_introduce.setText(getIntent().getStringExtra("str"));
    }

    @Override
    protected void onBack() {
        super.onBack();
        Intent intent = new Intent();
        intent.putExtra("str", et_introduce.getText().toString());
        setResult(RESULT_OK, intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBack();
            finish();
            overridePendingTransition(0, 0);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public int bindLayout() {
        return R.layout.layout_introduce;
    }
}
