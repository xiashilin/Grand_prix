package edu.niit.ydkf.grand_prix.module.launch;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.Window;

import java.util.ArrayList;
import java.util.List;

import edu.niit.ydkf.grand_prix.R;
import edu.niit.ydkf.grand_prix.common.adapter.ViewPagerAdapter;

/**
 * Created by liuhaitian on 16/3/21.
 * 引导页
 */
public class GuideActivity extends Activity implements ViewPager.OnPageChangeListener {
    private ViewPager mViewPager;
    //    LinearLayout mPointLayout;
    private ViewPagerAdapter pagerAdapter;
    private List<Integer> mPicterList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_guide);
        mViewPager = (ViewPager) findViewById(R.id.picture_show_vp);

        initData();
//        initPoint();
        setViewPager();
    }


    //初始化添加图片资源
    private void initData() {
        mPicterList = new ArrayList<Integer>();
        mPicterList.add(R.drawable.home_page);
        mPicterList.add(R.drawable.friend);
        mPicterList.add(R.drawable.circle);
    }

    //初始化下面的指示器的图片
//    private void initPoint() {
//        // 清除所有的视图
//        mPointLayout.removeAllViews();
//
//        for (int i = 0; i < mPicterList.size(); i++) {
//            ImageView mImageView = new ImageView(this);
//            if (i == 0) {
//                mImageView.setBackgroundResource(R.drawable.page_indicator_focused);
//            } else {
//                mImageView.setBackgroundResource(R.drawable.page_indicator_unfocused);
//            }
//
//            mImageView.setLayoutParams(new LayoutParams(20, 20, Gravity.CENTER));
//            LayoutParams mParams = new LayoutParams(new ViewGroup.LayoutParams(50, 50));
//            mParams.leftMargin = 50;
//            mParams.rightMargin = 50;
//            mPointLayout.addView(mImageView, mParams);
//        }
//    }

    //为viewPager设置相应的参数
    private void setViewPager() {
        pagerAdapter = new ViewPagerAdapter(this, mPicterList);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setOnPageChangeListener(this);
        mViewPager.setCurrentItem(0);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
//        selectPointShow(position);
    }

//    private void selectPointShow(int position) {
//        for (int i = 0; i < mPointLayout.getChildCount(); i++) {
//            ImageView mImageView = (ImageView) mPointLayout.getChildAt(i);
//            if (i == position) {
//                mImageView.setBackgroundResource(R.drawable.page_indicator_focused);
//            } else {
//                mImageView.setBackgroundResource(R.drawable.page_indicator_unfocused);
//            }
//        }
//    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}
