package com.jd.app;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.android.material.tabs.TabLayout;
import com.jd.core.base.BaseActivity;
import com.jd.home.fragment.HomeFragment;
import com.jd.list.fragment.ListFragment;
import com.jd.other.fragment.OtherFragment;
import com.jd.setting.fragment.SettingFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    ViewPager viewPager;


    TabLayout tabLayout;

    List<Map<String,Object>> items = new ArrayList<>();


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tab_layout);
        //初始化数据
        this.initData();
        //viewpager

        viewPager.addOnPageChangeListener(this);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));

        tabLayout.setupWithViewPager(viewPager);

        setTabLayout();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectTab(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                selectTab(tab);
            }
        });

    }

    private void setTabLayout() {
        for (int i = 0; i < items.size(); i ++) {
            Map<String,Object> item = items.get(i);
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(R.layout.view_main_tab);
            View tabView = tab.getCustomView();

            ImageView iconView = tabView.findViewById(R.id.tab_icon_iv);
            TextView titleView = tabView.findViewById(R.id.tab_title_tv);

            int  selectedImage = Integer.valueOf(item.get("selectedImage").toString());
            int image = Integer.valueOf(item.get("image").toString());

            String titleString = item.get("title").toString();

            titleView.setText(titleString);
            titleView.setTextColor(ContextCompat.getColor(this, R.color.black));

            if (i == 0) {
                iconView.setImageResource(selectedImage);
            } else {
                iconView.setImageResource(image);
            }

        }
    }

    private void selectTab(TabLayout.Tab tab) {
        for (int i = 0; i < items.size(); i ++) {
            Map<String,Object> item = items.get(i);
            TabLayout.Tab tmpTab = tabLayout.getTabAt(i);


            ImageView iconView = tmpTab.getCustomView().findViewById(R.id.tab_icon_iv);
            TextView titleView = tmpTab.getCustomView().findViewById(R.id.tab_title_tv);
            int image = Integer.valueOf(item.get("image").toString());
            int  selectedImage = Integer.valueOf(item.get("selectedImage").toString());

            if (tab == tmpTab) {
                iconView.setImageResource(selectedImage);
            } else {
                iconView.setImageResource(image);
            }
        }
    }


    private void initData() {
        Map<String,Object> item1 = new HashMap<>();
        item1.put("title","首页");
        item1.put("image",R.drawable.icon_one_selected);
        item1.put("selectedImage",R.mipmap.tab_home_selected);
        item1.put("fragment",new HomeFragment());
        items.add(item1);


        Map<String,Object> item2 = new HashMap<>();
        item2.put("title","列表");
        item2.put("image",R.drawable.icon_two_selected);
        item2.put("selectedImage",R.mipmap.tab_home_selected);
        item2.put("fragment",new ListFragment());
        items.add(item2);

        Map<String,Object> item3 = new HashMap<>();
        item3.put("title","功能菜单");
        item3.put("image",R.drawable.icon_three_selected);
        item3.put("selectedImage",R.mipmap.tab_home_selected);
        item3.put("fragment",new OtherFragment());
        items.add(item3);

        Map<String,Object> item4 = new HashMap<>();
        item4.put("title","设置");
        item4.put("image",R.drawable.icon_four_selected);
        item4.put("selectedImage",R.mipmap.tab_home_selected);
        item4.put("fragment",new SettingFragment());
        items.add(item4);
    }
    /******************************* viewpage  **********************************/
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = (Fragment) MainActivity.this.items.get(position).get("fragment");
            return fragment;
        }

        @Override
        public int getCount() {
            return 4;
        }
    }

    /**
     * 隐藏导航
     */
    protected boolean preferredNavigationBarHidden() {
        return true;
    }


}
