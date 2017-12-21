package com.example.alex.myselfexposeeffect.detail;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alex.myselfexposeeffect.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * TODO
 *
 * @author YinGen Chu
 * @date 2017/12/6 11:36.
 * @email: youguyilin@126.com
 */

public class DetailActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener{

    @BindView(R.id.layout_header)
    LinearLayout mLayoutHeader;
    @BindView(R.id.tab_layout_container)
    ImageView mTabLayoutContainer;
    @BindView(R.id.indicator)
    TabLayoutIndicator mIndicator;
    @BindView(R.id.tab_content)
    TabLayout mTabContent;
    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    @BindView(R.id.btn_download)
    Button mBtnDownload;
    @BindView(R.id.title_container)
    ImageView mTitleContainer;
    @BindView(R.id.btn_left)
    ImageView mBtnLeft;
    @BindView(R.id.tv_title)
    TextView mTvTitle;

    private FragmentTab introTab,commentTab,recommendTab;
    private FragmentAdapter mFragmentAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        ButterKnife.bind(this);

        mTvTitle.setText("紫石榴");
        mTitleContainer.setBackgroundColor(Color.parseColor("#ffffffff"));
        mTabLayoutContainer.setBackgroundColor(Color.parseColor("#ffffffff"));
        initFragments();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAppBarLayout.addOnOffsetChangedListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAppBarLayout.removeOnOffsetChangedListener(this);
    }

    private void initFragments() {
        List<FragmentTab> fragments = new ArrayList<>(3);
        introTab = new FragmentTab(IntroductionFrag.class).setTitle("介绍");
        commentTab = new FragmentTab(CommentFrag.class).setTitle("评论");
        recommendTab = new FragmentTab(RecommendFrag.class).setTitle("推荐");
        fragments.add(introTab);
        fragments.add(commentTab);
        fragments.add(recommendTab);
        mFragmentAdapter = new FragmentAdapter(DetailActivity.this, getSupportFragmentManager(),fragments);
        mViewPager.setAdapter(mFragmentAdapter);
        mViewPager.setOffscreenPageLimit(fragments.size());
        mTabContent.setupWithViewPager(mViewPager);

        mIndicator.setupWithTabLayout(mTabContent);
        mIndicator.setupWithViewPager(mViewPager);

        for (int i = 0;i< fragments.size();i++){
            mTabContent.getTabAt(i).setCustomView(R.layout.tab_sub);
        }
    }


    @OnClick({R.id.btn_download, R.id.btn_left})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_download:
                Toast.makeText(DetailActivity.this,"安装",Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_left:
                finish();
                break;
        }
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        Log.e("verticalOffset",verticalOffset + "------");
        if (Math.abs(verticalOffset) < 500){
            mTvTitle.setVisibility(View.GONE);
            mTitleContainer.setBackgroundColor(Color.parseColor("#ffffffff"));
            mTabLayoutContainer.setBackgroundColor(Color.parseColor("#ffffffff"));
        }else {
            mTvTitle.setVisibility(View.VISIBLE);
            mTitleContainer.setBackgroundColor(Color.parseColor("#F9F9F9"));
            mTabLayoutContainer.setBackgroundColor(Color.parseColor("#F9F9F9"));
        }
    }
}
