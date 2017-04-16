package edu.niit.ydkf.grand_prix.common.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.List;

import edu.niit.ydkf.grand_prix.R;
import edu.niit.ydkf.grand_prix.module.MainActivity;
import edu.niit.ydkf.grand_prix.module.launch.GuideActivity;

/**
 * Created by 张典孔 on 2016/3/24.
 */
public class ViewPagerAdapter extends PagerAdapter {
    private Context mContext;
    private List<Integer> mList;
    private LayoutInflater mInflater;

    public ViewPagerAdapter(Context mContext, List<Integer> mList) {
        this.mContext = mContext;
        this.mList = mList;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals((View) object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    //获得item的长度
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mInflater.inflate(R.layout.guide_item, null);
        RelativeLayout mLayout = (RelativeLayout) view.findViewById(R.id.picter);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView_btn);
        mLayout.setBackgroundResource(mList.get(position));
        //设置跳转按钮
        if (position == 2) {
            imageView.setVisibility(View.VISIBLE);
        } else {
            imageView.setVisibility(View.GONE);
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GuideActivity guideActivity = (GuideActivity) mContext;
                mContext.startActivity(new Intent(mContext, MainActivity.class));
                guideActivity.finish();
            }
        });
        ((ViewPager) container).addView(view, 0);
        return view;
    }
}
