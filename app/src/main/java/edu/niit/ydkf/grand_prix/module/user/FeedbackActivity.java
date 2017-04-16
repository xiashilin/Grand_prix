package edu.niit.ydkf.grand_prix.module.user;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;

import butterknife.Bind;
import butterknife.OnClick;
import edu.niit.ydkf.grand_prix.App;
import edu.niit.ydkf.grand_prix.R;
import edu.niit.ydkf.grand_prix.module.base.BaseActivity;

/**
 * Created by hp on 2016/3/21.
 */
public class FeedbackActivity extends BaseActivity {
    @Bind(R.id.feedback_content)
    EditText et_feedback;

    @OnClick(R.id.btn_ok)
    public void sendFeedback() {
        if (TextUtils.isEmpty(et_feedback.getText().toString())) {
            snackbar("内容不能为空");
            return;
        }
        AVObject post = new AVObject("FeedBack");
        post.put("content", et_feedback.getText().toString());
        post.put("user", App.getCurrentUser());
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    toast("反馈提交成功");
                    finish();
                }
            }
        });
    }

    @Override
    public void init(View view) {
        enableBack();
    }

    @Override
    public int bindLayout() {
        return R.layout.feedback;
    }
}
