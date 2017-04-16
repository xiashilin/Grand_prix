package edu.niit.ydkf.grand_prix.module.user;

import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.CountCallback;
import com.avos.avoscloud.FindCallback;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import edu.niit.ydkf.grand_prix.R;
import edu.niit.ydkf.grand_prix.common.adapter.SimpleAnimationAdapter;
import edu.niit.ydkf.grand_prix.module.base.BaseActivity;
import edu.niit.ydkf.grand_prix.common.utils.SPUtils;

/**
 * Created by liuhaitian on 16/2/29.
 */
public class ListActivity extends BaseActivity {
    @Bind(R.id.tv_toolbar_title)
    TextView tv_toolbartitle;
    @Bind(R.id.ll_index_progress)
    LinearLayout linearlayout;
    @Bind(R.id.ultimate_recycler_view)
    UltimateRecyclerView customUltimateRecyclerview;
    @Bind(R.id.ll_nodata)
    LinearLayout nodata;

    private SimpleAnimationAdapter simpleAnimationAdapter = null;
    private List<AVObject> lists;
    private LinearLayoutManager linearLayoutManager;


    public void getCreateData() {
        AVQuery<AVObject> event = new AVQuery<>("EventInfo");
        event.whereEqualTo("createUser", SPUtils.get(this, "userid", ""));
        event.orderByDescending("createdAt");
        event.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(final List<AVObject> list, AVException e) {
                if (e == null) {
                    if (list.size() == 0) {
                        linearlayout.setVisibility(View.GONE);
                        nodata.setVisibility(View.VISIBLE);
                        return;
                    }
                    lists = list;
                    nodata.setVisibility(View.GONE);
                    for (int i = 0; i < list.size(); i++) {
                        AVQuery<AVObject> query = AVQuery.getQuery("TeamSituation");
                        query.whereEqualTo("eventobj", list.get(i).getObjectId());
                        final int finalI = i;
                        query.countInBackground(new CountCallback() {
                            public void done(int count, AVException e) {
                                if (e == null) {
                                    list.get(finalI).put("number", count + 1 + "");
                                    linearlayout.setVisibility(View.GONE);
                                    simpleAnimationAdapter.setData(list);
                                    simpleAnimationAdapter.notifyDataSetChanged();
                                } else {
                                }
                            }
                        });
                    }
                    linearlayout.setVisibility(View.GONE);
                    simpleAnimationAdapter.setData(list);
                    simpleAnimationAdapter.notifyDataSetChanged();

                } else {
                    JSONObject jsonObject = JSONObject.parseObject(e.getMessage());
                    if (e.getCode() == 100 || e.getCode() == 0)
                        Snackbar.make(getCurrentFocus(), "网络连接失败,请检查网络", Snackbar
                                .LENGTH_SHORT).show();
                    else
                        Snackbar.make(getCurrentFocus(), jsonObject.getString("error"), Snackbar
                                .LENGTH_SHORT).show();
                }
            }
        });
    }

    public void getJoinData() {
        AVQuery<AVObject> event = new AVQuery<>("TeamSituation");
        event.whereEqualTo("memberid", SPUtils.get(this, "userid", ""));
        event.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(final List<AVObject> list, AVException e) {
                if (e == null) {
                    if (list.size() == 0) {
                        nodata.setVisibility(View.VISIBLE);
                        Snackbar.make(getCurrentFocus(), "当前没有加入任何活动", Snackbar.LENGTH_SHORT).show();
                        return;
                    }
                    nodata.setVisibility(View.GONE);
                    String[] objs = new String[list.size()];
                    for (int i = 0; i < list.size(); i++) {
                        objs[i] = list.get(i).getString("eventobj");
                    }
                    AVQuery<AVObject> query = new AVQuery<AVObject>("EventInfo");
                    query.whereContainedIn("objectId", Arrays.asList(objs));
                    query.findInBackground(new FindCallback<AVObject>() {
                        @Override
                        public void done(final List<AVObject> list, AVException e) {
                            if (e == null) {
                                lists = list;
                                for (int i = 0; i < list.size(); i++) {
                                    AVQuery<AVObject> query = AVQuery.getQuery("TeamSituation");
                                    query.whereEqualTo("eventobj", list.get(i).getObjectId());
                                    final int finalI = i;
                                    query.countInBackground(new CountCallback() {
                                        public void done(int count, AVException e) {
                                            if (e == null) {
                                                list.get(finalI).put("number", count + 1 + "");
                                                linearlayout.setVisibility(View.GONE);
                                                simpleAnimationAdapter.setData(list);
                                                simpleAnimationAdapter.notifyDataSetChanged();
                                            } else {
                                            }
                                        }
                                    });
                                }
                                linearlayout.setVisibility(View.GONE);
                                simpleAnimationAdapter.setData(list);
                                simpleAnimationAdapter.notifyDataSetChanged();

                            } else {
                                JSONObject jsonObject = JSONObject.parseObject(e.getMessage());
                                if (e.getCode() == 100 || e.getCode() == 0)
                                    Snackbar.make(getCurrentFocus(), "网络连接失败,请检查网络", Snackbar
                                            .LENGTH_SHORT).show();
                                else
                                    Snackbar.make(getCurrentFocus(), jsonObject.getString("error"), Snackbar
                                            .LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    JSONObject jsonObject = JSONObject.parseObject(e.getMessage());
                    if (e.getCode() == 100 || e.getCode() == 0)
                        Snackbar.make(getCurrentFocus(), "网络连接失败,请检查网络", Snackbar
                                .LENGTH_SHORT).show();
                    else
                        Snackbar.make(getCurrentFocus(), jsonObject.getString("error"), Snackbar
                                .LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void init(View view) {
        enableBack();
        linearLayoutManager = new LinearLayoutManager(this);
        customUltimateRecyclerview.setLayoutManager(linearLayoutManager);
        customUltimateRecyclerview.setHasFixedSize(false);
        lists = new ArrayList<>();
        simpleAnimationAdapter = new SimpleAnimationAdapter(lists, this);
        tv_toolbartitle.setText(getIntent().getStringExtra("toolbartitle"));
        if (getIntent().getBooleanExtra("isJoin", false)) { //真则是加入,假则是创建
            getJoinData();
        } else {
            getCreateData();
        }
        customUltimateRecyclerview.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (getIntent().getBooleanExtra("isJoin", false)) { //真则是加入,假则是创建
                    getJoinData();
                } else {
                    getCreateData();
                }
                customUltimateRecyclerview.setRefreshing(false);
            }
        });
        customUltimateRecyclerview.setAdapter(simpleAnimationAdapter);
    }

    @Override
    public int bindLayout() {
        return R.layout.base_listview;
    }
}
