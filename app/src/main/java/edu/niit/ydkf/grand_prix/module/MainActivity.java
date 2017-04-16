package edu.niit.ydkf.grand_prix.module;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import edu.niit.ydkf.grand_prix.App;
import edu.niit.ydkf.grand_prix.R;
import edu.niit.ydkf.grand_prix.config.ConString;
import edu.niit.ydkf.grand_prix.common.Params;
import edu.niit.ydkf.grand_prix.module.base.BaseActivity;
import edu.niit.ydkf.grand_prix.module.base.RequestListener;
import edu.niit.ydkf.grand_prix.module.im.ImFragment;
import edu.niit.ydkf.grand_prix.module.index.IndexFragment;
import edu.niit.ydkf.grand_prix.module.timeline.TimeLineActivity;
import edu.niit.ydkf.grand_prix.module.user.PersonCenterFragment;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

public class MainActivity extends BaseActivity implements RongIM.UserInfoProvider {

    @Bind(R.id.ll_main_index)
    LinearLayout layout_index;
    @Bind(R.id.ll_main_search)
    LinearLayout layout_search;//活动圈;
    @Bind(R.id.ll_main_center)
    LinearLayout layout_center;//个人中心
    @Bind(R.id.ll_main_im)
    LinearLayout layout_im;//好友

    private LinearLayout lastPage = layout_index;
    //    private HoospHereFragment hoospHereFragment;
    private PersonCenterFragment personCenterFragment;
    private IndexFragment indexFragment;
    private ImFragment imFragment;
    private long exitTime = 0;
    private static boolean isReLogin = true;
    private List<AVObject> list;

    @OnClick({R.id.ll_main_index, R.id.ll_main_search, R.id.ll_main_center, R.id.ll_main_im})
    public void selectUi(View view) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();

        if (!(layout_search == (LinearLayout) view)) {
            selectPage((LinearLayout) view);
            fragmentTransaction.hide(imFragment);
            fragmentTransaction.hide(personCenterFragment);
            fragmentTransaction.hide(indexFragment);
        }
        if (lastPage == (LinearLayout) view) {
            return;
        }
        switch (view.getId()) {
            case R.id.ll_main_index:
                fragmentTransaction.show(indexFragment);
                lastPage = layout_index;
                break;
            case R.id.ll_main_search:
                Intent intent = new Intent(this, TimeLineActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
//                fragmentTransaction.show(hoospHereFragment);
                break;
            case R.id.ll_main_center:
                fragmentTransaction.show(personCenterFragment);
                lastPage = layout_center;
                break;
            case R.id.ll_main_im:
                imFragment.lazyLoad();
                fragmentTransaction.show(imFragment);
                lastPage = layout_im;
            default:
                break;
        }
        fragmentTransaction.commit();
    }

    private void initView() {
        initFragment();
    }

    private void initFragment() {
        indexFragment = new IndexFragment();
//        hoospHereFragment = new HoospHereFragment();
        personCenterFragment = new PersonCenterFragment();
        imFragment = new ImFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.main_content, indexFragment, "index");
        ft.add(R.id.main_content, imFragment, "session");
//        ft.add(R.id.main_content, hoospHereFragment, "hoosp");
        ft.add(R.id.main_content, personCenterFragment, "personcenter");
        ft.hide(imFragment);
//        ft.hide(hoospHereFragment);
        ft.hide(personCenterFragment);
        ft.show(indexFragment);
        ft.commit();
        selectPage(layout_index);
    }

    private void initBussiness() {
        list = new ArrayList<>();
        AVQuery<AVObject> query = new AVQuery<>("_User");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> mlist, AVException e) {
                list = mlist;
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        getToken();
    }

    public void selectPage(LinearLayout position) {
        layout_im.setBackgroundColor(getResources().getColor(R.color.bottom_background));
        layout_index.setBackgroundColor(getResources().getColor(R.color
                .bottom_background));
        layout_search.setBackgroundColor(getResources().getColor(R.color
                .bottom_background));
        layout_center.setBackgroundColor(getResources().getColor(R.color
                .bottom_background));
        position.setBackgroundColor(Color.parseColor("#d3d3d3"));
    }

    public static void setReLogin() {
        isReLogin = true;
    }

    public void getToken() {
        if (App.isLogin(this)) {
            if (!isReLogin) {
                return;
            }
        } else return;
        Params header = Params.newInstance();
        String Nonce = "1";
        header.put("App-Key", ConString.RongCloud_APP_KEY);
        header.put("Nonce", Nonce);
        String time = System.currentTimeMillis() + "";
        header.put("Timestamp", time + "");
        header.put("Signature", sha1(ConString.RongCloud_APP_SECRET + Nonce + time));
        Params body = Params.newInstance();
        body.put("userId", App.getUserid());
        body.put("name", App.getUsername());
        String icon = "";
        try {
            icon = URLEncoder.encode(App.getIconurl(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        body.put("portraitUri", icon);
        request(ConString.RongCloud_GET_TOKEN, header, body, new RequestListener() {
            @Override
            public void onConSuccess(String response) {
                JSONObject result = JSONObject.parseObject(response);
                if (result.getIntValue("code") == 200) {
                    isReLogin = false;
                    connect(result.getString("token"));
                } else {
                    snackbar("融云连接错误，错误码是" + result.getIntValue("code"));
                }
                logd(response);
            }

            @Override
            public void onConFailure(String errormsg) {
                loge(errormsg);
            }
        });
    }

    private void connect(String token) {

        if (getApplicationInfo().packageName.equals(App.getCurProcessName(getApplicationContext()))) {

            /**
             * IMKit SDK调用第二步,建立与服务器的连接
             */
            RongIM.connect(token, new RongIMClient.ConnectCallback() {

                /**
                 * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的 Token
                 */
                @Override
                public void onTokenIncorrect() {
                    getToken();
                    loge("连接融云失败，token错误");
                }

                /**
                 * 连接融云成功
                 * @param userid 当前 token
                 */
                @Override
                public void onSuccess(String userid) {
                    logd("连接融云成功");
                    RongIM.setUserInfoProvider(MainActivity.this, true);
//                    Log.d("LoginActivity", "--onSuccess" + userid);
//                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                    finish();
                }

                /**
                 * 连接融云失败
                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    loge("连接融云失败，错误码是" + errorCode);
                }
            });
        }
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void init(View view) {
        initView();
        initBussiness();
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_main;
    }

    @Override
    public UserInfo getUserInfo(String userid) {
        for (AVObject user : list) {
            if (user.getObjectId().equals(userid))
                return new UserInfo(user.getObjectId(), user.getString("user_name"), Uri.parse(user.getString("iconurl")));
        }
        logd("用户的userid:" + userid);
        return null;
    }
}
