package edu.niit.ydkf.grand_prix.module.im;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVPush;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import edu.niit.ydkf.grand_prix.App;
import edu.niit.ydkf.grand_prix.R;
import edu.niit.ydkf.grand_prix.config.MessageCode;
import edu.niit.ydkf.grand_prix.common.ChatObject;
import edu.niit.ydkf.grand_prix.common.adapter.ChatAdapter;
import edu.niit.ydkf.grand_prix.module.base.BaseActivity;
import edu.niit.ydkf.grand_prix.module.user.LoginActivity;
import edu.niit.ydkf.grand_prix.common.utils.SPUtils;

/**
 * Created by xsl on 16/2/26.
 */
public class AddFriends extends BaseActivity implements AdapterView.OnItemClickListener {
    @Bind(R.id.et_addfriend_number)
    EditText editText;
    @Bind(R.id.listview)
    ListView listView;
    private ChatAdapter adapter;

    @OnClick(R.id.btn_addfriend)
    public void searchFriend() {
        if (TextUtils.isEmpty(editText.getText().toString())) {
            Snackbar.make(getRootView(), "搜索不能为空", Snackbar.LENGTH_SHORT).show();
            return;
        }
        if (validationLogin()) {
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }
        String inputString = editText.getText().toString();

        AVQuery<AVObject> email = AVQuery.getQuery("_User");
        email.whereContains("email", inputString);

        AVQuery<AVObject> phone = AVQuery.getQuery("_User");
        phone.whereContains("mobilePhoneNumber", inputString);

        AVQuery<AVObject> username = AVQuery.getQuery("_User");
        username.whereContains("user_name", inputString);

        List<AVQuery<AVObject>> queries = new ArrayList<AVQuery<AVObject>>();
        queries.add(email);
        queries.add(phone);
        queries.add(username);

        AVQuery<AVObject> mainQuery = AVQuery.or(queries);
        mainQuery.whereNotEqualTo("objectId", App.getCurrentUser().getObjectId());
        mainQuery.findInBackground(new FindCallback<AVObject>() {
            public void done(List<AVObject> results, AVException e) {
                if (e == null) {
                    List<ChatObject> list = new ArrayList<ChatObject>();
                    for (int i = 0; i < results.size(); i++) {
                        ChatObject chatObject = new ChatObject();
                        chatObject.setName(results.get(i).getString("user_name"));
                        chatObject.setId(results.get(i).getObjectId());
                        chatObject.setUser((AVUser) results.get(i));
                        list.add(chatObject);
                    }
                    adapter.changeData(list);
                }

            }
        });
    }

    public boolean validationLogin() {
        //如果不是登录状态,跳到登录界面
        return !App.isLogin(this);
    }

    @Override
    public void init(View view) {
        enableBack();
        adapter = new ChatAdapter(this, "添加好友");
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public int bindLayout() {
        return R.layout.layout_addfriend;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ChatObject chatObject = (ChatObject) adapter.getItem(position);
        isFriend(chatObject.getUser());
    }

    public void isFriend(final AVUser friendUser) {
        AVQuery<AVObject> email = AVQuery.getQuery("FriendList");
        email.whereEqualTo("user", friendUser);

        AVQuery<AVObject> phone = AVQuery.getQuery("FriendList");
        phone.whereEqualTo("friend", friendUser);


        List<AVQuery<AVObject>> queries = new ArrayList<AVQuery<AVObject>>();
        queries.add(email);
        queries.add(phone);

        final AVQuery<AVObject> mainQuery = AVQuery.or(queries);
        mainQuery.include("user");
        mainQuery.include("friend");
        mainQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    for (AVObject avObject : list) {
                        if (avObject.getAVUser("friend").equals(App.getCurrentUser()) || avObject.getAVUser("user").equals(App.getCurrentUser())) {
                            if (avObject.getBoolean("isVerify"))
                                Toast.makeText(getApplicationContext(), "对方已经是您的好友,无需添加", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(getApplicationContext(), "申请已经提交,请勿重复提交请求", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    AVObject post = new AVObject("FriendList");
                    post.put("user", App.getCurrentUser());
                    post.put("friend", friendUser);
                    post.put("isVerify", false);
                    AVObject message = new AVObject("MessageList");
                    message.put("createUser", App.getCurrentUser());
                    message.put("receiveUser", friendUser);
                    message.put("type", MessageCode.ADD_FRIEND);
                    message.put("isDeal", false);
                    message.saveInBackground();
                    post.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                Toast.makeText(getApplicationContext(), "提交好友申请成功", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    AVQuery pushQuery = AVInstallation.getQuery();
                    pushQuery.whereEqualTo("installationId", friendUser.getString("installationId"));
                    AVPush.sendMessageInBackground(SPUtils.get(AddFriends.this, "username", "") + "请求添加您为好友", pushQuery);
                }
            }
        });
    }
}
