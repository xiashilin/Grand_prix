package edu.niit.ydkf.grand_prix.module.im;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.CountCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import edu.niit.ydkf.grand_prix.App;
import edu.niit.ydkf.grand_prix.R;
import edu.niit.ydkf.grand_prix.common.adapter.FragmentAdapter;
import edu.niit.ydkf.grand_prix.module.base.BaseFragment;
import edu.niit.ydkf.grand_prix.module.base.IBaseCallBack;

/**
 * Created by xsl on 16/3/21.
 */
public class ImFragment extends BaseFragment {
    @Bind(R.id.viewpager)
    ViewPager mViewPager;
    @Bind(R.id.tabs)
    TabLayout mTabLayout;
    @Bind(R.id.iv_im_haveNewMessage)
    ImageView iv_isNewMessage;


    @OnClick(R.id.iv_im_addfriend)
    public void addFriend() {
        startActivity(new Intent(getActivity(), AddFriends.class));
    }

    @OnClick(R.id.iv_im_messagebox)
    public void showMessage() {
        startActivity(new Intent(getActivity(), MessageBoxActivity.class));
    }

    @Override
    public int bindLayout() {
        return R.layout.layout_im;
    }

    @Override
    public void initView(View view) {
        actionBar.setTitle("");
        setupViewPager();
    }

    @Override
    public void onBussiness(View view) {
    }


    private void setupViewPager() {
        List<String> titles = new ArrayList<>();
        titles.add("消息");
        titles.add("联系人");
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(0)));
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(1)));
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new SessionFragment());
        fragments.add(new FriendFragment());
        FragmentAdapter adapter =
                new FragmentAdapter(getActivity().getSupportFragmentManager(), fragments, titles);
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void lazyLoad() {
        AVQuery<AVObject> query = new AVQuery<>("MessageList");
        query.whereEqualTo("receiveUser", App.getCurrentUser());
        query.whereEqualTo("isDeal", false);
        query.countInBackground(new CountCallback() {
            @Override
            public void done(final int i, AVException e) {
                dealData(e, new IBaseCallBack<Object>() {
                    @Override
                    public void onDataSuccess(Object value) {
                        if (i == 0)
                            iv_isNewMessage.setVisibility(View.GONE);
                        else iv_isNewMessage.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onException(JSONObject e) {

                    }
                });
            }
        });
    }
}
