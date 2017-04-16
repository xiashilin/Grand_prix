package edu.niit.ydkf.grand_prix.module.event;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import edu.niit.ydkf.grand_prix.R;
import edu.niit.ydkf.grand_prix.common.adapter.TagAdapter;
import edu.niit.ydkf.grand_prix.module.base.BaseActivity;
import edu.niit.ydkf.grand_prix.common.view.FlowTagLayout;
import edu.niit.ydkf.grand_prix.common.view.listener.OnTagSelectListener;

/**
 * Created by xsl on 16/2/26.
 */
public class ChoiseTypeActivity extends BaseActivity {
    protected ActionBar actionBar;
    private Toolbar mToolbar;
    @Bind(R.id.tv_createEvent_title)
    TextView title;
    @Bind(R.id.indoor_flow_layout)
    FlowTagLayout mIndoorFlowTagLayout;
    @Bind(R.id.outdoor_flow_layout)
    FlowTagLayout mOutdoorFlowTagLayout;
    @Bind(R.id.ball_flow_layout)
    FlowTagLayout mBallFlowTagLayout;
    private TagAdapter<String> mIndoorTagAdapter;
    private TagAdapter<String> mOutdoorTagAdapter;
    private TagAdapter<String> mBallTagAdapter;

    private void ballData() {
        List<String> dataBall = new ArrayList<String>();
        dataBall.add("篮球");
        dataBall.add("足球");
        dataBall.add("排球");
        dataBall.add("羽毛球");
        dataBall.add("网球");
        dataBall.add("乒乓球");
        dataBall.add("台球");
        dataBall.add("高尔夫球");
        dataBall.add("棒球");
        dataBall.add("毽球");
        dataBall.add("橄榄球");
        dataBall.add("保龄球");
        dataBall.add("其他");
        mBallTagAdapter.onlyAddAll(dataBall);
    }


    private void initIndoorData() {
        List<String> dataIndoor = new ArrayList<String>();
        dataIndoor.add("游泳");
        dataIndoor.add("健身");
        dataIndoor.add("瑜伽");
        dataIndoor.add("体育舞蹈");
        dataIndoor.add("跆拳道");
        dataIndoor.add("拳击");
        dataIndoor.add("散打");
        dataIndoor.add("截拳道");
        dataIndoor.add("空手道");
        dataIndoor.add("剑术");
        dataIndoor.add("电子竞技");
        dataIndoor.add("书吧");
        dataIndoor.add("茶吧");
        dataIndoor.add("K歌");
        dataIndoor.add("K舞");
        dataIndoor.add("电影");
        dataIndoor.add("棋牌");
        dataIndoor.add("桑拿");
        dataIndoor.add("足道按摩");
        dataIndoor.add("酒吧");
        dataIndoor.add("其他");
        mIndoorTagAdapter.onlyAddAll(dataIndoor);

    }

    private void initOutdoorData() {
        List<String> dataOutdoor = new ArrayList<String>();
        dataOutdoor.add("跑步");
        dataOutdoor.add("射击");
        dataOutdoor.add("赛车");
        dataOutdoor.add("滑雪");
        dataOutdoor.add("攀岩");
        dataOutdoor.add("轮滑");
        dataOutdoor.add("登山");
        dataOutdoor.add("单车");
        dataOutdoor.add("马术");
        dataOutdoor.add("划船");
        dataOutdoor.add("跑酷");
        dataOutdoor.add("潜水");
        dataOutdoor.add("徒步");
        dataOutdoor.add("速降");
        dataOutdoor.add("其他");
        mOutdoorTagAdapter.onlyAddAll(dataOutdoor);
    }

    private void checkBall() {
        mBallTagAdapter = new TagAdapter<>(this);
        mBallFlowTagLayout.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_SINGLE);
        mBallFlowTagLayout.setAdapter(mBallTagAdapter);
        mBallFlowTagLayout.setOnTagSelectListener(new OnTagSelectListener() {
            @Override
            public void onItemSelect(FlowTagLayout parent, List<Integer> selectedList) {
                if (selectedList != null && selectedList.size() > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (int i : selectedList) {
                        sb.append(parent.getAdapter().getItem(i));
                    }
                    Intent intent = new Intent();
                    intent.putExtra("str", sb.toString());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    private void intent() {
        Intent intent = new Intent();
        intent.putExtra("str", "");
        setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(0, 0);
    }

    private void checkIndoor() {
        mIndoorTagAdapter = new TagAdapter<>(this);
        mIndoorFlowTagLayout.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_SINGLE);
        mIndoorFlowTagLayout.setAdapter(mIndoorTagAdapter);
        mIndoorFlowTagLayout.setOnTagSelectListener(new OnTagSelectListener() {
            @Override
            public void onItemSelect(FlowTagLayout parent, List<Integer> selectedList) {
                if (selectedList != null && selectedList.size() > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (int i : selectedList) {
                        sb.append(parent.getAdapter().getItem(i));
                    }
                    Intent intent = new Intent();
                    intent.putExtra("str", sb.toString());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    private void checkOutdoor() {
        mOutdoorTagAdapter = new TagAdapter<>(this);
        mOutdoorFlowTagLayout.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_SINGLE);
        mOutdoorFlowTagLayout.setAdapter(mOutdoorTagAdapter);
        mOutdoorFlowTagLayout.setOnTagSelectListener(new OnTagSelectListener() {
            @Override
            public void onItemSelect(FlowTagLayout parent, List<Integer> selectedList) {
                if (selectedList != null && selectedList.size() > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (int i : selectedList) {
                        sb.append(parent.getAdapter().getItem(i));
                    }
                    Intent intent = new Intent();
                    intent.putExtra("str", sb.toString());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            intent();
//            overridePendingTransition(0, 0);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void init(View view) {
        enableBack();
        checkBall();
        checkIndoor();
        checkOutdoor();

        initIndoorData();
        initOutdoorData();
        ballData();
    }

    @Override
    protected void onBack() {
        super.onBack();
        intent();
    }

    @Override
    public int bindLayout() {
        return R.layout.layout_activityintroduce;
    }
}
