package edu.niit.ydkf.grand_prix.module.user;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;

import butterknife.Bind;
import edu.niit.ydkf.grand_prix.App;
import edu.niit.ydkf.grand_prix.R;
import edu.niit.ydkf.grand_prix.common.utils.RegexValidateUtil;
import edu.niit.ydkf.grand_prix.common.utils.SPUtils;
import edu.niit.ydkf.grand_prix.config.ConString;
import edu.niit.ydkf.grand_prix.module.MainActivity;
import edu.niit.ydkf.grand_prix.module.base.BaseActivity;
import edu.niit.ydkf.grand_prix.module.base.IBaseCallBack;

/**
 * Created by liuhaitian on 16/2/22.
 */
public class LoginActivity extends BaseActivity {
    private static final int REQUEST_SIGNUP = 0;

    @Bind(R.id.input_email)
    EditText _emailText;
    @Bind(R.id.input_password)
    EditText _passwordText;
    @Bind(R.id.btn_login)
    Button _loginButton;
    @Bind(R.id.link_signup)
    TextView _signupLink;
    @Bind(R.id.cb_autologin)
    CheckBox _autologinCheckbox;
    @Bind(R.id.cb_rememberpwd)
    CheckBox _rememberCheckbox;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SIGNUP && resultCode == RESULT_OK) {
            onLoginSuccess();
        }
    }

    public void login() {

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        showProgressDialog("登录中...");

        final String email = _emailText.getText().toString();
        final String password = _passwordText.getText().toString();
        AVUser.loginByMobilePhoneNumberInBackground(email, password, new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                dealData(avUser, e, new IBaseCallBack<AVUser>() {
                    @Override
                    public void onDataSuccess(AVUser value) {
                        dismisProgressDialog();
                        saveInfo(value, email, password);
                        String installationId = AVInstallation.getCurrentInstallation().getInstallationId();
                        value.put("installationId", installationId);
                        value.saveInBackground();
                        onLoginSuccess();
                    }

                    @Override
                    public void onException(JSONObject e) {
                        dismisProgressDialog();
                    }
                });
            }
        });
        _loginButton.setEnabled(true);
    }

    private void saveInfo(AVUser user, String email, String password) {
        String installationId = AVInstallation.getCurrentInstallation().getInstallationId();
        SPUtils.put(getApplicationContext(), "useruid", user.getInt("uid"));
        SPUtils.put(getApplicationContext(), "installationId", installationId);
        SPUtils.put(getApplicationContext(), "username", user.getString("user_name"));
        SPUtils.put(getApplicationContext(), "loginname", email);
        SPUtils.put(getApplicationContext(), "isAutoLogin",
                _autologinCheckbox.isChecked());
        SPUtils.put(getApplicationContext(), "isRememberPwd", _rememberCheckbox
                .isChecked());
        if (_rememberCheckbox.isChecked())
            SPUtils.put(getApplicationContext(), "password", password);
        SPUtils.put(getApplicationContext(), "userid", user.getObjectId());
        if (TextUtils.isEmpty(user.getString("iconurl")))
            SPUtils.put(getApplicationContext(), "iconurl", ConString.DEFAULT_ICON);
        else SPUtils.put(getApplicationContext(), "iconurl", user
                .getString("iconurl"));
    }


    public void onLoginSuccess() {
        App.flushUserInfo(this);
        MainActivity.setReLogin();
        _loginButton.setEnabled(true);
        SPUtils.put(getApplicationContext(), "isLogin", true);
        finish();
    }

    public void onLoginFailed() {
        Snackbar.make(getRootView(), "登录失败", Snackbar.LENGTH_SHORT).show();
//        Toast.makeText(getActivity(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (!email.isEmpty()) {
            if (RegexValidateUtil.checkCellphone(email)) {
                _emailText.setError(null);
            } else {
                _emailText.setError("请输入手机号");
                valid = false;
            }
        } else {
            _emailText.setError("请输入手机号");
            valid = false;
        }
        if (password.isEmpty() || password.length() < 6 || password.length() > 12) {
            _passwordText.setError("请输入6-12个字符");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }


    @Override
    public void init(View view) {
//        viewRoot = findViewById(R.id.view_root);
        Snackbar.make(view, "请先登录!", Snackbar.LENGTH_LONG).show();
        _rememberCheckbox.setChecked((Boolean) SPUtils.get(getApplicationContext(), "isRememberPwd", false));
        _emailText.setText((CharSequence) SPUtils.get(getApplicationContext(), "loginname", ""));
        _passwordText.setText((CharSequence) SPUtils.get(getApplicationContext(), "password", ""));
        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
        _autologinCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    _rememberCheckbox.setChecked(isChecked);
            }
        });
        _rememberCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked)
                    if (_autologinCheckbox.isChecked())
                        _autologinCheckbox.setChecked(false);
            }
        });
    }

    @Override
    public int bindLayout() {
        return R.layout.layout_login;
    }
}