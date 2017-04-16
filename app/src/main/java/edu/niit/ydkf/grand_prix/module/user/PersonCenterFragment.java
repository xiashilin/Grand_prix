package edu.niit.ydkf.grand_prix.module.user;


import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.Bind;
import butterknife.OnClick;
import edu.niit.ydkf.grand_prix.App;
import edu.niit.ydkf.grand_prix.R;
import edu.niit.ydkf.grand_prix.module.base.BaseFragment;
import edu.niit.ydkf.grand_prix.common.utils.SPUtils;

/**
 * Created by liuhaitian on 16/2/2.
 * 个人中心界面
 */
public class PersonCenterFragment extends BaseFragment {
    @Bind(R.id.sdv_userinfo_icon)
    SimpleDraweeView sdv_icon;
    @Bind(R.id.tv_usercenter_username)
    TextView tv_username;

    @Override
    public int bindLayout() {
        return R.layout.layout_usercenter;
    }

    @OnClick(R.id.btn_usercenter_logout)
    public void logout() {
        AVQuery<AVObject> query = new AVQuery<AVObject>("_User");
        query.getInBackground(App.getUserid(), new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                avObject.put("installationId", "");
                avObject.saveInBackground();
            }
        });
        SPUtils.put(getActivity(), "isLogin", false);
        SPUtils.put(getActivity(), "isAutoLogin", false);
        AVUser.logOut();
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }

    @Override
    public void initView(View view) {
    }

    @Override
    public void onResume() {
        super.onResume();
        Uri uri = Uri.parse(App.getIconurl());
        sdv_icon.setImageURI(uri);
        validationLogin();
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        tv_icon.setText(SPUtils.get(getActivity(), "username", "").toString());
                        tv_username.setText(SPUtils.get(getActivity(), "username", "").toString());
                    }
                });
            }
        }.start();


    }

    public boolean validationLogin() {
        LinearLayout ll_not_login = (LinearLayout) getView().findViewById(R.id.ll_im_notLogin);
        Button btn_login = (Button) getView().findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });
        //如果不是登录状态,跳到登录界面
        if (!App.isLogin(getActivity())) {
            ll_not_login.setVisibility(View.VISIBLE);
            return true;
        } else {
            ll_not_login.setVisibility(View.GONE);
            return false;
        }
    }

    @Override
    public void onBussiness(View view) {
        actionBar.setTitle("");
//        if (validationLogin())
//            startActivity(new Intent(getActivity(), LoginActivity.class));
    }


    @OnClick(R.id.rl_usercenter_user)
    public void edituserinfo() {
        startActivity(new Intent(getActivity(), UserInfoActivity.class));
    }

    @OnClick(R.id.rl_usercenter_creater)
    public void creater() {
        Intent intent = new Intent(getActivity(), ListActivity.class);
        intent.putExtra("toolbartitle", "我创建的活动");
        intent.putExtra("isJoin", false);
        startActivity(intent);
    }

    @OnClick(R.id.rl_usercenter_join)
    public void join() {
        Intent intent = new Intent(getActivity(), ListActivity.class);
        intent.putExtra("toolbartitle", "我报名的活动");
        intent.putExtra("isJoin", true);
        startActivity(intent);
    }

    @OnClick(R.id.rl_team)
    public void team() {
    }

    @OnClick(R.id.rl_software_adout)
    public void about() {
        Intent intent = new Intent(getActivity(), AboutActivity.class);
        startActivity(intent);
    }

    @Override
    protected void lazyLoad() {
    }
}