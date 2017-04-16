package edu.niit.ydkf.grand_prix.module.index;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import edu.niit.ydkf.grand_prix.App;
import edu.niit.ydkf.grand_prix.R;
import edu.niit.ydkf.grand_prix.module.base.BaseActivity;
import edu.niit.ydkf.grand_prix.module.user.LoginActivity;
import edu.niit.ydkf.grand_prix.common.utils.SPUtils;

/**
 * Created by xsl on 16/2/28.
 */
public class EventDetailedActivity extends BaseActivity {
    @Bind(R.id.sdv_detail_front)
    SimpleDraweeView sdv_detail_front;
    @Bind(R.id.sdv_detail_background)
    SimpleDraweeView sdv_detail_background;
    @Bind(R.id.tv_detailed_title)
    TextView tv_title;
    @Bind(R.id.tv_detailed_location)
    TextView tv_location;
    @Bind(R.id.tv_detailed_time)
    TextView tv_time;
    @Bind(R.id.tv_detailed_type)
    TextView tv_type;
    @Bind(R.id.tv_detailed_content)
    TextView tv_content;
    @Bind(R.id.tv_detailed_creator)
    TextView tv_creator;
    @Bind(R.id.btn_baoming)
    Button btn_baoming;
    @Bind(R.id.rl_detailed)
    RelativeLayout rl;
    @Bind(R.id.ll_detailed)
    LinearLayout ll_detailed;
    @Bind(R.id.tv_renshu)
    TextView renshu;
    private String objid;
    private String creatorid;


    private void onBussiness() {
        objid = getIntent().getStringExtra("id");
        if (getIntent().getStringExtra("number") != null) {
            renshu.setText(getIntent().getStringExtra("number") + "人报名");
            renshu.setVisibility(View.VISIBLE);
        }
        AVQuery<AVObject> query = new AVQuery<AVObject>("EventInfo");
        query.getInBackground(objid, new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if (e == null) {
                    tv_title.setText(avObject.getString("eventname"));
                    tv_location.setText(avObject.getString("locantion"));
                    tv_time.setText(avObject.getString("starttime") + " — " + avObject.getString
                            ("endtime"));
                    tv_type.setText(avObject.getString("eventtype"));
                    tv_content.setText(avObject.getString("introduce"));
                    creatorid = avObject.getString("createUser");
                    Uri uri = Uri.parse(avObject.getString("ThumbnailUrl"));
                    sdv_detail_front.setImageURI(uri);
                    sdv_detail_background.setImageURI(uri);
                    AVQuery<AVObject> query1 = new AVQuery<AVObject>("TeamSituation");
                    query1.whereEqualTo("eventobj", objid);
                    query1.findInBackground(new FindCallback<AVObject>() {
                        @Override
                        public void done(List<AVObject> list, AVException e) {
                            if (e == null) {
                                for (int i = 0; i < list.size(); i++) {
                                    if (list.get(i).getString("memberid").equals(SPUtils.get
                                            (getApplicationContext(),
                                                    "userid",
                                                    ""))) {
                                        btn_baoming.setEnabled(false);
                                        Snackbar.make(rl, "您已报名,无需重新报名!", Snackbar.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    });
                    AVQuery<AVObject> query2 = new AVQuery<AVObject>("_User");
                    query2.getInBackground(creatorid, new GetCallback<AVObject>() {
                        @Override
                        public void done(AVObject avObject, AVException e) {
                            if (e == null) {
                                tv_creator.setText(avObject.getString("user_name"));
                            }
                        }
                    });
                }
            }
        });

    }

    @OnClick(R.id.btn_baoming)
    public void baoming() {
        if (validationLogin()) {
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }
        if (SPUtils.get(this, "userid", "").toString().equals(creatorid)) {
            Snackbar.make(rl, "无法报名自己创建的活动!", Snackbar.LENGTH_SHORT).show();
            return;
        }
        AVObject post = new AVObject("TeamSituation");
        post.put("eventobj", objid);
        post.put("creatorid", creatorid);
        post.put("memberid", SPUtils.get(this, "userid", ""));
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    btn_baoming.setEnabled(false);
                    getYibaoming();
                    Snackbar.make(rl, "报名成功!", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean validationLogin() {
        //如果不是登录状态,跳到登录界面
        if (!App.isLogin(this)) {
            return true;
        } else {
            return false;
        }
    }

    public void getYibaoming() {
        ll_detailed.removeAllViews();
        final AVQuery<AVObject> query = AVQuery.getQuery("TeamSituation");
        query.whereEqualTo("eventobj", objid);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    for (int i = 0; i < list.size(); i++) {
                        AVQuery<AVObject> query = new AVQuery<AVObject>("_User");
                        query.getInBackground(list.get(i).getString("memberid"), new GetCallback<AVObject>() {
                            @Override
                            public void done(AVObject avObject, AVException e) {
                                if (e == null) {
                                    View view = getLayoutInflater().inflate(R.layout
                                            .item_yibaoming, null);
                                    ((TextView) view.findViewById(R.id.tv_item_im_icon)).setText
                                            (avObject.getString("user_name"));
                                    ((TextView) view.findViewById(R.id.tv_item_im_username)).setText(
                                            avObject.getString("user_name"));
                                    ll_detailed.addView(view);
                                }
                            }
                        });

                    }
                }
            }
        });
    }

    @Override
    public void init(View view) {
        enableBack();
        onBussiness();
        getYibaoming();
    }

    @Override
    public int bindLayout() {
        return R.layout.layout_detailedevent;
    }
}
