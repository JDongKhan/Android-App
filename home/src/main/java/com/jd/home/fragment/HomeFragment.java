package com.jd.home.fragment;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;


import android.content.Intent;
import android.view.View;

import com.jd.core.base.BaseFragment;
import com.jd.home.R;
import com.jd.home.R2;
import com.jd.home.adapter.HomeAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment {


    @BindView(R2.id.recyclerView)
    RecyclerView recyclerView;

    private List<Map<String,Object>> items = new ArrayList<>();


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(View view) {
        this.initList();
    }

    private void initList() {
        this.initData();
        // 设置布局
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        //设置滑动方向：纵向
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        //添加Android自带的分割线
        recyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(),DividerItemDecoration.VERTICAL));

        List<Integer> viewHolders = new ArrayList();
        viewHolders.add(R.layout.banner_pager);
        viewHolders.add(R.layout.home_list_item);

        //设置适配器
        recyclerView.setAdapter(new HomeAdapter(this.getActivity(),this.items,viewHolders));
    }

    private void initData() {
        Map<String,Object> item0 = new HashMap<>();
        items.add(item0);

        Map<String,Object> item1 = new HashMap<>();
        item1.put("title","LIST DEMO");
        items.add(item1);

        Map<String,Object> item2 = new HashMap<>();
        item2.put("title","GRID DEMO");
        items.add(item2);

        Map<String,Object> item3 = new HashMap<>();
        item3.put("title","SECOND_GRID");
        items.add(item3);

        Map<String,Object> item4 = new HashMap<>();
        item4.put("title","自定义List");
        items.add(item4);
    }

    private void push(Class<? extends AppCompatActivity> activityClass) {
        Intent intent = new Intent(this.getActivity(),activityClass);
        this.startActivity(intent);
    }


    @Override
    protected boolean preferredNavigationBarHidden() {
        return true;
    }

}
