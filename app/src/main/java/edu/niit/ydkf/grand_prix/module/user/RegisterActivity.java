package edu.niit.ydkf.grand_prix.module.user;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SignUpCallback;

import butterknife.Bind;
import edu.niit.ydkf.grand_prix.R;
import edu.niit.ydkf.grand_prix.config.ConString;
import edu.niit.ydkf.grand_prix.module.base.BaseActivity;
import edu.niit.ydkf.grand_prix.module.base.IBaseCallBack;
import edu.niit.ydkf.grand_prix.common.utils.RegexValidateUtil;
import edu.niit.ydkf.grand_prix.common.utils.SPUtils;

/**
 * Created by liuhaitian on 16/2/23.
 */
public class RegisterActivity extends BaseActivity {
    @Bind(R.id.input_username)
    EditText _userText;
    @Bind(R.id.input_phonenumber)
    EditText _phoneText;
    @Bind(R.id.input_password)
    EditText _passwordText;
    @Bind(R.id.btn_signup)
    Button _signupButton;
    @Bind(R.id.link_login)
    TextView _loginLink;


    public void signup() {

        if (!validate()) {
            onSignupFailed();
            return;
        }
        final String phone = _phoneText.getText().toString();
        final String password = _passwordText.getText().toString();
        final String username = _userText.getText().toString();
        showProgressDialog("正在注册...");
        final AVUser user = new AVUser();
        user.setUsername(phone);
        user.setPassword(password);
        user.setMobilePhoneNumber(phone);
        user.put("user_name", username);
        String installationId = AVInstallation.getCurrentInstallation().getInstallationId();
        user.put("installationId", installationId);
        user.put("iconurl", ConString.DEFAULT_ICON);
        user.signUpInBackground(new SignUpCallback() {
            public void done(AVException e) {
                dealData(e, new IBaseCallBack<Object>() {
                    @Override
                    public void onDataSuccess(Object value) {
                        dismisProgressDialog();
                        String installationId = AVInstallation.getCurrentInstallation().getInstallationId();
                        SPUtils.put(getApplicationContext(), "useruid", user.getInt("uid"));
                        SPUtils.put(getApplicationContext(), "installationId", installationId);
                        SPUtils.put(getApplicationContext(), "username", username);
                        SPUtils.put(getApplicationContext(), "phone", phone);
                        SPUtils.put(getApplicationContext(), "userid", user.getObjectId());
                        SPUtils.put(getApplicationContext(), "iconurl", ConString
                                .DEFAULT_ICON);
                        onSignupSuccess();
                    }

                    @Override
                    public void onException(JSONObject e) {
                        dismisProgressDialog();
                    }
                });
            }
        });
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        snackbar("注册失败");

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _phoneText.getText().toString();
        String password = _passwordText.getText().toString();

        if (name.isEmpty() || !RegexValidateUtil.checkCellphone(name)) {
            _phoneText.setError("请输入正确的手机号码");
            valid = false;
        } else {
            _phoneText.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 10) {
            _passwordText.setError("6-10个字符");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    @Override
    public void init(View view) {
        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public int bindLayout() {
        return R.layout.layout_register;
    }
}
