package edu.niit.ydkf.grand_prix.module.index;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.CountCallback;
import com.avos.avoscloud.FindCallback;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import edu.niit.ydkf.grand_prix.R;
import edu.niit.ydkf.grand_prix.common.adapter.SimpleAnimationAdapter;
import edu.niit.ydkf.grand_prix.module.base.BaseFragment;
import edu.niit.ydkf.grand_prix.module.event.CreateEventActivity;
import edu.niit.ydkf.grand_prix.module.maps.info;

/**
 * Created by xsl on 16/1/19.
 */
public class IndexFragment extends BaseFragment {
    @Bind(R.id.action_button)
    FloatingActionButton actionButton;
    @Bind(R.id.ultimate_recycler_view)
    UltimateRecyclerView customUltimateRecyclerview;
    @Bind(R.id.ll_index_progress)
    LinearLayout linearLayout;
    private SimpleAnimationAdapter simpleAnimationAdapter = null;
    private List<AVObject> lists;
    private LinearLayoutManager linearLayoutManager;
//    private List<info> infos;


    @OnClick(R.id.iv_index_map)
    public void gotoMap() {
//        startActivity(new Intent(getActivity(), MapActivity.class));
    }


    @Override
    public int bindLayout() {
        return R.layout.layout_index;
    }

    @Override
    public void initView(View view) {
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CreateEventActivity.class));
            }
        });
        linearLayoutManager = new LinearLayoutManager(getActivity());
        customUltimateRecyclerview.setLayoutManager(linearLayoutManager);
        customUltimateRecyclerview.setHasFixedSize(false);
        lists = new ArrayList<>();
//        infos = new ArrayList<>();
        simpleAnimationAdapter = new SimpleAnimationAdapter(lists, getActivity());
        customUltimateRecyclerview.enableLoadmore();
        simpleAnimationAdapter.setCustomLoadMoreView(LayoutInflater.from(getActivity())
                .inflate(R.layout.custom_bottom_progressbar, null));

        customUltimateRecyclerview.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(final int itemsCount, int maxLastVisiblePosition) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AVQuery<AVObject> event = new AVQuery<>("EventInfo");
                        event.setLimit(5);
                        event.setSkip(itemsCount);
                        event.orderByDescending("createdAt");
                        event.findInBackground(new FindCallback<AVObject>() {
                            @Override
                            public void done(List<AVObject> list, AVException e) {
                                if (e == null) {
                                    for (int i = 0; i < list.size(); i++) {
                                        lists.add(simpleAnimationAdapter
                                                .getAdapterItemCount(), list.get(i));
                                        simpleAnimationAdapter.insert(list.get(i), simpleAnimationAdapter
                                                .getAdapterItemCount());
                                    }
                                }
                            }
                        });
                        customUltimateRecyclerview.setRefreshing(false);
                    }
                }, 1000);

            }
        });
        customUltimateRecyclerview.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
                customUltimateRecyclerview.setRefreshing(false);
            }
        });
        customUltimateRecyclerview.setAdapter(simpleAnimationAdapter);
    }


    public void getData() {
        AVQuery<AVObject> event = new AVQuery<>("EventInfo");
        event.setLimit(50);
        event.orderByDescending("createdAt");
        event.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(final List<AVObject> list, AVException e) {
                if (e == null) {
                    for (int i = 0; i < list.size(); i++) {
//                        AVGeoPoint avGeoPoint = list.get(i).getAVGeoPoint("location");
//                        info minfo = new info(avGeoPoint.getLatitude(), avGeoPoint.getLongitude(),
//                                list.get(i).getString("eventname"),
//                                list.get(i).getString("locantion"), "0", list.get(i).getObjectId());
//                        infos.add(minfo);
//                        AVGeoPoint avGeoPoint = list.get(i).getAVGeoPoint("location");
//                        double lat;
//                        double lon;
//                        if (SPUtils.get(getActivity(),
//                                "latitude", 32.03) == null)
//                            lat = 32.03;
//                        else
//                            lat = (double) SPUtils.get(getActivity(),
//                                    "latitude", 32.03);
//                        if (SPUtils.get(getActivity(),
//                                "lontitude", 118.46) == null)
//                            lon = 118.46;
//                        else lon = (double) SPUtils.get(getActivity(),
//                                "lontitude", 118.46);
//
//                        AVGeoPoint avGeoPoint1 = new AVGeoPoint(lat, lon);
//                        String distance = String.valueOf(avGeoPoint.distanceInMilesTo(avGeoPoint1));
//                        list.get(i).put("distance", distance);
                        AVQuery<AVObject> query = AVQuery.getQuery("TeamSituation");
                        query.whereEqualTo("eventobj", list.get(i).getObjectId());
                        final int finalI = i;
                        query.countInBackground(new CountCallback() {
                            public void done(int count, AVException e) {
                                if (e == null) {
                                    list.get(finalI).put("number", count + 1 + "");
                                    linearLayout.setVisibility(View.GONE);
                                    simpleAnimationAdapter.setData(list);
                                    simpleAnimationAdapter.notifyDataSetChanged();
                                    lists = list;
                                } else {
                                }
                            }
                        });
                    }
                    lists = list;
                    linearLayout.setVisibility(View.GONE);
                    simpleAnimationAdapter.setData(list);
                    simpleAnimationAdapter.notifyDataSetChanged();

                } else {
                    JSONObject jsonObject = JSONObject.parseObject(e.getMessage());
                    if (e.getCode() == 100 || e.getCode() == 0)
                        Snackbar.make(getView(), "网络连接失败,请检查网络", Snackbar
                                .LENGTH_SHORT).show();
                    else
                        Snackbar.make(getView(), jsonObject.getString("error"), Snackbar
                                .LENGTH_SHORT).show();
                }
            }
        });
//        changeDistance();
    }

    @Override
    public void onBussiness(View view) {
        actionBar.setTitle("");
        getData();
    }

    @Override
    protected void lazyLoad() {

    }
//
//    public void changeDistance() {
//        if (lists != null)
//            for (int i = 0; i < lists.size(); i++) {
//                AVGeoPoint avGeoPoint = lists.get(i).getAVGeoPoint("location");
//                String distance = DistanceConversion.GetDistance((Double) SPUtils.get
//                                (getActivity(), "lontitude",
//                                        39.9)
//                        , (Double) SPUtils.get(getActivity(), "latitude", 116.4), avGeoPoint
//                                .getLongitude(), avGeoPoint.getLatitude());
//                lists.get(i).put("distance", distance);
//                simpleAnimationAdapter.setData(lists);
//                simpleAnimationAdapter.notifyDataSetChanged();
//            }
//    }


}
