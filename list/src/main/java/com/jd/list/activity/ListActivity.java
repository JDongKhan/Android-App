package com.jd.list.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jd.core.base.BaseActivity;
import com.jd.list.R;
import com.jd.list.R2;

import butterknife.BindView;

@Route(path = "/test/activity1")
public class ListActivity extends BaseActivity {

    @BindView(R2.id.listView1)
    public ListView listView;

    @Override
    protected int getLayoutId() {
        return R.layout.list;
    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected void initView() {
        this.setImmeriveStatuBar();
        // 1.准备集合数据
        String[] strs = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "09",
                "89", "77", "55", "ut", "sd", "gj", "gjk", "qw", "jhk" };
        // 2.准备ArrayAdapter对象
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.item, strs);
        // 3.设置Adapter显示列表
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ListActivity.this,ListMainActivity.class);
                startActivity(intent);
            }
        });
    }


}
