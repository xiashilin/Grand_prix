package edu.niit.ydkf.grand_prix.module.im;

import android.content.Context;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVPush;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.facebook.drawee.view.SimpleDraweeView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import edu.niit.ydkf.grand_prix.App;
import edu.niit.ydkf.grand_prix.R;
import edu.niit.ydkf.grand_prix.common.ChatObject;
import edu.niit.ydkf.grand_prix.common.utils.SPUtils;
import edu.niit.ydkf.grand_prix.config.MessageCode;
import edu.niit.ydkf.grand_prix.module.base.BaseActivity;


/**
 * Created by xsl on 16/3/22.
 */
public class MessageBoxActivity extends BaseActivity {
    @Bind(R.id.listview)
    ListView listView;
    @Bind(R.id.listview_nocontent)
    TextView tv_nodata;
    private MessageAdapter adapter;
    private String userid;

    @Override
    public void init(View view) {
        enableBack();
        adapter = new MessageAdapter(this);
        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(this);
        getData();
    }

    @Override
    public int bindLayout() {
        return R.layout.layout_messagebox;
    }

    public void getData() {

        AVQuery<AVObject> list = AVQuery.getQuery("MessageList");
        list.whereEqualTo("receiveUser", App.getCurrentUser());
        list.include("createUser");
        list.include("receiveUser");
        list.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    List<ChatObject> lists = new ArrayList<ChatObject>();
                    for (AVObject avObject : list) {
                        ChatObject chatObject = new ChatObject();
                        AVUser avUser = avObject.getAVUser("createUser");
                        chatObject.setTitle(avUser.getString("user_name") + MessageCode.getMsg(avObject.getInt("type")));
                        chatObject.setName(avUser.getString("user_name"));
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
                        chatObject.setTime(format.format(avObject.getUpdatedAt()));
                        chatObject.setType(avObject.getInt("type"));
                        chatObject.setFlag(avObject.getObjectId());
                        chatObject.setId(avUser.getObjectId());
                        chatObject.setRead(avObject.getBoolean("isDeal"));
                        chatObject.setUser(avUser);
                        lists.add(chatObject);
                    }
                    setListViewData(lists);
                }
            }
        });

    }

    public void setListViewData(List<?> list) {
        if (list.size() == 0) {
            listView.setVisibility(View.GONE);
            tv_nodata.setText("暂无任何消息");
            tv_nodata.setVisibility(View.VISIBLE);
        } else {
            listView.setVisibility(View.VISIBLE);
            tv_nodata.setVisibility(View.GONE);
            adapter.changeData((List<ChatObject>) list);
        }
    }

    public class MessageAdapter extends BaseAdapter {
        private List<ChatObject> list;
        private Context context;

        public MessageAdapter(Context context, List<ChatObject> list) {
            this.context = context;
            this.list = list;
        }

        public MessageAdapter(Context context) {
            list = new ArrayList<>();
            this.context = context;
        }

        public void changeData(List<ChatObject> list) {
            this.list = list;
            this.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_messagebox, null);
                viewHolder = new ViewHolder();
                viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_item_im_username);
                viewHolder.btn_accept = (Button) convertView.findViewById(R.id.btn_messageBox_accept);
                viewHolder.btn_alreadyRead = (Button) convertView.findViewById(R.id.btn_messageBox_alreadyRead);
                viewHolder.btn_refused = (Button) convertView.findViewById(R.id.btn_messageBox_refused);
                viewHolder.tv_item_im_time = (TextView) convertView.findViewById(R.id.tv_item_im_time);
                viewHolder.sdv_icon = (SimpleDraweeView) convertView.findViewById(R.id.sdv_icon);
                convertView.setTag(viewHolder);
            } else viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.tv_name.setText(list.get(position).getName());
            viewHolder.tv_item_im_time.setText(list.get(position).getTime());
            Uri uri = Uri.parse(list.get(position).getUser().getString("iconurl"));
            viewHolder.sdv_icon.setImageURI(uri);
            if (list.get(position).isRead()) {
                viewHolder.btn_alreadyRead.setVisibility(View.GONE);
                viewHolder.btn_accept.setVisibility(View.GONE);
                viewHolder.btn_refused.setVisibility(View.GONE);
            } else if (list.get(position).getType() == MessageCode.ADD_FRIEND) {
                viewHolder.btn_accept.setVisibility(View.VISIBLE);
                viewHolder.btn_refused.setVisibility(View.VISIBLE);
                viewHolder.btn_alreadyRead.setVisibility(View.GONE);
            } else {
                viewHolder.btn_alreadyRead.setVisibility(View.VISIBLE);
                viewHolder.btn_accept.setVisibility(View.GONE);
                viewHolder.btn_refused.setVisibility(View.GONE);
            }
            viewHolder.btn_accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AVObject post = AVObject.createWithoutData("MessageList", list.get(position).getFlag());
                    post.put("isDeal", true);
                    AVObject message = new AVObject("MessageList");
                    message.put("createUser", App.getCurrentUser());
                    message.put("receiveUser", list.get(position).getUser());
                    int type = MessageCode.ACCEPT;
                    message.put("type", type);
                    message.put("isDeal", false);
//                    message.put("receiveIcon", "");
                    message.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null)
                                toast("成功保存");
                        }
                    });
                    AVQuery<AVObject> query = new AVQuery("FriendList");
                    query.whereEqualTo("user", list.get(position).getId());
                    query.whereEqualTo("friendId", userid);
                    query.findInBackground(new FindCallback<AVObject>() {
                        @Override
                        public void done(List<AVObject> list, AVException e) {
                            if (e == null)
                                if (list.size() != 0) {
                                    list.get(0).put("isVerify", true);
                                    list.get(0).saveInBackground();
                                }
                        }
                    });
                    post.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            Snackbar.make(getRootView(), "处理成功", Snackbar.LENGTH_SHORT).show();
                            getData();
                        }
                    });
                    String installationId = list.get(position).getUser().getString("installationId");
                    AVQuery pushQuery = AVInstallation.getQuery();
                    pushQuery.whereEqualTo("installationId", installationId);
                    AVPush.sendMessageInBackground(SPUtils.get(getApplicationContext(), "username", "") + "同意了您的好友请求", pushQuery);
                }
            });
            viewHolder.btn_alreadyRead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AVObject post = AVObject.createWithoutData("MessageList", list.get(position).getFlag());
                    post.put("isDeal", true);
                    post.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            snackbar("处理成功");
                            getData();
                        }
                    });

                }
            });
            viewHolder.btn_refused.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AVObject post = AVObject.createWithoutData("MessageList", list.get(position).getFlag());
                    post.put("isDeal", true);
                    AVObject message = new AVObject("MessageList");
                    message.put("createUser", App.getCurrentUser());
                    message.put("receiveUser", list.get(position).getUser());
                    message.put("type", MessageCode.REFUSE);
                    message.put("isDeal", false);
                    message.saveInBackground();
                    post.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            snackbar("处理成功");
                            getData();
                        }
                    });
                    String installationId = list.get(position).getUser().getString("installationId");
                    AVQuery pushQuery = AVInstallation.getQuery();
                    pushQuery.whereEqualTo("installationId", installationId);
                    AVPush.sendMessageInBackground(SPUtils.get(getApplicationContext(), "username", "") + "同意了您的好友请求", pushQuery);
                }
            });

            return convertView;
        }

        private class ViewHolder {
            TextView tv_name, tv_item_im_time;
            Button btn_alreadyRead, btn_accept, btn_refused;
            SimpleDraweeView sdv_icon;
        }
    }

}
