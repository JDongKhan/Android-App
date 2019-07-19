package com.jd.other.fragment;



import androidx.fragment.app.Fragment;
import butterknife.BindView;

import android.widget.ListView;

import com.jd.core.base.BaseFragment;
import com.jd.core.base.adapter.LazyAdapter;
import com.jd.other.R;
import com.jd.other.R2;
import com.jd.other.viewholder.OtherViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class OtherFragment extends BaseFragment {

    @BindView(R2.id.simpleListView)
    ListView listView;

    private List<Map<String,String>> items = new ArrayList<>();

    public OtherFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_other;
    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected void initView() {
        this.initData();
        LazyAdapter lazyAdapter = new LazyAdapter(this.getActivity(),this.items, OtherViewHolder.class) {
            @Override
            public int indexOfLayoutsAtPosition(int position) {
                return 0;
            }
        };
        listView.setAdapter(lazyAdapter); //导入
    }

    private void initData() {
        Map<String,String> item1 = new HashMap<>();
        item1.put("title","网络");
        item1.put("router","");
        items.add(item1);

        Map<String,String> item2 = new HashMap<>();
        item2.put("title","列表");
        item1.put("router","");
        items.add(item2);

        Map<String,String> item3 = new HashMap<>();
        item3.put("title","功能菜单");
        item1.put("router","");
        items.add(item3);

        Map<String,String> item4 = new HashMap<>();
        item4.put("title","设置");
        item1.put("router","");
        items.add(item4);
    }

}
