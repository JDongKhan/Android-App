package com.jd.list.fragment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jd.core.base.BaseFragment;
import com.jd.core.base.adapter.BaseListViewAdapter;
import com.jd.list.R;
import com.jd.list.activity.DemoActivity;
import com.jd.list.activity.ListActivity;
import com.jd.list.utils.BaseUtils;
import com.jd.list.viewholders.ListViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ListFragment extends BaseFragment {


    private List<Map<String,Object>> items = new ArrayList<>();

    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_list;
    }

    @Override
    protected void initView(View view) {
        ListView simpleListView = view.findViewById(R.id.simpleListView);

        this.getNavigationBar().setBackViewHidden(true);
        this.getNavigationBar().setTitle("功能");

        this.initData();
        BaseListViewAdapter lazyAdapter = new BaseListViewAdapter(this.getActivity(),this.items, ListViewHolder.class) {
            @Override
            public int indexOfLayoutsAtPosition(int position) {
                return 0;
            }
        };
        simpleListView.setAdapter(lazyAdapter); //导入

        //点击事件
        simpleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String,Object> item = ListFragment.this.items.get(position);
                OnListClick click = (OnListClick) item.get("action");
                if (click != null) {
                    click.onClick();
                }
            }
        });
    }


    @Override
    protected boolean preferredNavigationBarHidden() {
        return false;
    }

    private void initData() {
        Map<String,Object> item1 = new HashMap<>();
        item1.put("title","LIST DEMO");
        item1.put("action",  new OnListClick(){
            @Override
            public void onClick() {
                startDemo(BaseUtils.TYPE_LIST);
            }
        });
        items.add(item1);

        Map<String,Object> item2 = new HashMap<>();
        item2.put("title","GRID DEMO");
        item2.put("action",  new OnListClick(){
            @Override
            public void onClick() {
                startDemo(BaseUtils.TYPE_GRID);
            }
        });
        items.add(item2);

        Map<String,Object> item3 = new HashMap<>();
        item3.put("title","SECOND_GRID");
        item3.put("action",  new OnListClick(){
            @Override
            public void onClick() {
                startDemo(BaseUtils.TYPE_SECOND_GRID);
            }
        });
        items.add(item3);

        Map<String,Object> item4 = new HashMap<>();
        item4.put("title","自定义List");
        item4.put("action",  new OnListClick(){
            @Override
            public void onClick() {
                push(ListActivity.class);
            }
        });
        items.add(item4);
    }

    private void push(Class<? extends AppCompatActivity> activityClass) {
        Intent intent = new Intent(this.getActivity(),activityClass);
        this.startActivity(intent);
    }

    private void startDemo(int demoType) {
        Intent intent = new Intent(this.getActivity(), DemoActivity.class);
        intent.putExtra(DemoActivity.Companion.getEXTRA_TYPE(), demoType);
        startActivity(intent);
    }

    ///////////////////////////////////////
    public interface  OnListClick {
        public void onClick();
    }

}
