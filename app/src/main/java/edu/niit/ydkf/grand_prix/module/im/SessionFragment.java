package edu.niit.ydkf.grand_prix.module.im;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import edu.niit.ydkf.grand_prix.App;
import edu.niit.ydkf.grand_prix.R;
import edu.niit.ydkf.grand_prix.module.base.BaseFragment;
import edu.niit.ydkf.grand_prix.module.user.LoginActivity;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;

/**
 * Created by xsl on 16/2/22.
 * 会话列表
 */
public class SessionFragment extends BaseFragment {
//    private List<AVObject> lists;
//    @Bind(R.id.ll_im_empty_friend)
//    LinearLayout ll_empty_friend;
//    @Bind(R.id.ll_im_notLogin)
//    LinearLayout ll_not_login;
//    @Bind(R.id.ll_index_progress)
//    LinearLayout ll_progress;
//    @Bind(R.id.listview)
//    ListView listView;
//    private CustomAdapter adapter;

//    @OnClick(R.id.tv_im_release)
//    public void addFriends() {
//        startActivity(new Intent(getActivity(), AddFriends.class));
//    }


    @Override
    public int bindLayout() {
        return R.layout.layout_session_rongcloud;
    }

    @Override
    public void initView(View view) {
        if (!App.isLogin(getActivity()))
            return;
        ConversationListFragment fragment = (ConversationListFragment) getChildFragmentManager().findFragmentById(R.id.conversationlist);

        Uri uri = Uri.parse("rong://" + getActivity().getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话非聚合显示
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")//设置群组会话聚合显示
                .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")//设置讨论组会话非聚合显示
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")//设置系统会话非聚合显示
                .build();

        fragment.setUri(uri);
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
//        actionBar.setTitle("");
//        if (validationLogin())
//            ;
//            startActivity(new Intent(getActivity(), LoginActivity.class));
//        lists = new ArrayList<>();
//        adapter = new CustomAdapter();
//        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (RongIM.getInstance() != null) {
//                    App.duifangname = ((TextView) view.findViewById(R.id
//                            .tv_item_im_username)).getText().toString();
//                    RongIM.getInstance().startPrivateChat(getActivity(), lists.get(position)
//                            .getString("hisfriend"), "");
//                }
//
//            }
//        });
    }


    @Override
    public void onResume() {
        super.onResume();
        validationLogin();
//        getList();
    }

    @Override
    protected void lazyLoad() {

    }
//
//    public void getList() {
//        if (validationLogin()) {
//            return;
//        }
//        AVQuery<AVObject> query = new AVQuery<AVObject>("FriendList");
//        query.whereEqualTo("user", SPUtils.get(getActivity(),
//                "userid", ""));
//        logd(SPUtils.get(getActivity(),
//                "userid", "").toString());
//        query.findInBackground(new FindCallback<AVObject>() {
//            public void done(List<AVObject> avObjects, AVException e) {
//                if (e == null) {
//                    if (avObjects.size() != 0) {
//                        lists = avObjects;
//                        ll_progress.setVisibility(View.GONE);
//                        adapter.notifyDataSetChanged();
//                        ll_empty_friend.setVisibility(View.GONE);
//                    } else {
//                        ll_empty_friend.setVisibility(View.VISIBLE);
//                    }
//                } else {
//                }
//            }
//        });
//    }


//    public class CustomAdapter extends BaseAdapter {
//
//        @Override
//        public int getCount() {
//            return lists.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return null;
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return 0;
//        }
//
//        @Override
//        public View getView(final int position, View convertView, ViewGroup parent) {
//            final ViewHolder viewHolder;
//            if (convertView == null) {
//                convertView = getActivity().getLayoutInflater().inflate(R.layout.item_listview, null);
//                viewHolder = new ViewHolder();
//                viewHolder.tv_icon = (TextView) convertView.findViewById(R.id.tv_item_im_icon);
//                viewHolder.tv_username = (TextView) convertView.findViewById(R.id.tv_item_im_username);
//                viewHolder.tv_item_userid = (TextView) convertView.findViewById(R.id.tv_item_userid);
//                convertView.setTag(viewHolder);
//            } else viewHolder = (ViewHolder) convertView.getTag();
//            AVQuery<AVObject> query = new AVQuery<AVObject>("_User");
//            query.getInBackground(lists.get(position).getString("hisfriend"), new
//                    GetCallback<AVObject>() {
//                        @Override
//                        public void done(AVObject avObject, AVException e) {
//                            if (e == null) {
//                                viewHolder.tv_icon.setText(avObject.getString("user_name"));
//                                viewHolder.tv_username.setText(avObject.getString("user_name"));
//                            }
//                        }
//                    });
//
//            return convertView;
//        }
//
//        public class ViewHolder {
//            TextView tv_icon;
//            TextView tv_username;
//            TextView tv_item_userid;
//        }
//    }
}