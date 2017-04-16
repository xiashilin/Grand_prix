package edu.niit.ydkf.grand_prix.module.im;

import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import edu.niit.ydkf.grand_prix.App;
import edu.niit.ydkf.grand_prix.R;
import edu.niit.ydkf.grand_prix.common.ChatObject;
import edu.niit.ydkf.grand_prix.common.adapter.ChatAdapter;
import edu.niit.ydkf.grand_prix.module.base.BaseFragment;
import io.rong.imkit.RongIM;


/**
 * Created by xsl on 16/3/21.
 */
public class FriendFragment extends BaseFragment {
    @Bind(R.id.listview)
    ListView listview;
    private ChatAdapter adapter;
    @Bind(R.id.ll_index_progress)
    LinearLayout layout_progress;
    @Bind(R.id.ll_nodata)
    LinearLayout ll_nodata;

    @Override
    public int bindLayout() {
        return R.layout.base_listview_without_toolbar;
    }

    @Override
    public void initView(View view) {
    }

    private void flushListView() {
        AVQuery<AVObject> query = new AVQuery<>("FriendList");
        query.whereEqualTo("user", App.getCurrentUser());
        AVQuery<AVObject> query1 = new AVQuery<>("FriendList");
        query1.whereEqualTo("friend", App.getCurrentUser());
        List<AVQuery<AVObject>> queries = new ArrayList<AVQuery<AVObject>>();
        queries.add(query);
        queries.add(query1);
        AVQuery<AVObject> mainQuery = AVQuery.or(queries);
        mainQuery.whereEqualTo("isVerify", true);
        mainQuery.include("user");
        mainQuery.include("friend");
        mainQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                List<ChatObject> lists = new ArrayList<ChatObject>();
                if (list.size() == 0)
                    ll_nodata.setVisibility(View.VISIBLE);
                else ll_nodata.setVisibility(View.GONE);
                for (AVObject avObject : list) {
                    ChatObject chatObject = new ChatObject();
                    if (avObject.getAVUser("friend").equals(App
                            .getCurrentUser())) {
                        chatObject.setName(avObject.getAVUser("user").getString("user_name"));
                        chatObject.setId(avObject.getAVUser("user").getObjectId());
                        chatObject.setUser(avObject.getAVUser("user"));
//                        chatObject.setIconurl(avObject.getString("userIcon"));
                    } else if (avObject.getAVUser("user").equals(App.getCurrentUser())) {
                        chatObject.setName(avObject.getAVUser("friend").getString("user_name"));
                        chatObject.setId(avObject.getAVUser("friend").getObjectId());
                        chatObject.setUser(avObject.getAVUser("friend"));
//                        chatObject.setIconurl(avObject.getString("receiveIcon"));
                    }
                    lists.add(chatObject);
                }
                adapter.changeData(lists);
                adapter.notifyDataSetChanged();
                layout_progress.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void onBussiness(View view) {
        adapter = new ChatAdapter(getActivity(), "点击进入聊天");
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String oppositeId = ((ChatObject) adapter.getItem(position))
                        .getId();
                String oppositeName = ((ChatObject) adapter.getItem(position))
                        .getName();
                RongIM.getInstance().startPrivateChat(getActivity(), oppositeId,
                        "与" + oppositeName + "的会话");
            }
        });
    }

    @Override
    protected void lazyLoad() {
        flushListView();
    }
}
