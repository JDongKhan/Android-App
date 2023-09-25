package com.jd.other.fragment;



import androidx.fragment.app.Fragment;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.jd.core.base.BaseFragment;
import com.jd.core.base.adapter.BaseListViewAdapter;
import com.jd.core.network.ServiceGenerator;
import com.jd.other.R;
import com.jd.other.network.BookService;
import com.jd.other.viewholder.OtherViewHolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class OtherFragment extends BaseFragment {

    ListView listView;

    private List<Map<String,Object>> items = new ArrayList<>();

    public OtherFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_other;
    }

    @Override
    protected void initView(View view) {
        listView = view.findViewById(R.id.simpleListView);
        this.navigationBar.setBackViewHidden(true);
        this.navigationBar.setTitle("功能");

        this.initData();
        BaseListViewAdapter lazyAdapter = new BaseListViewAdapter(this.getActivity(),this.items, OtherViewHolder.class) {
            @Override
            public int indexOfLayoutsAtPosition(int position) {
                return 0;
            }
        };
        listView.setAdapter(lazyAdapter); //导入

        //点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String,Object> item = OtherFragment.this.items.get(position);
                OnOtherClick click = (OnOtherClick) item.get("action");
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
        item1.put("title","网络");
        item1.put("action",  new OnOtherClick(){
            @Override
            public void onClick() {
                testNetwork();
            }
        });
        items.add(item1);

        Map<String,Object> item2 = new HashMap<>();
        item2.put("title","路由");
        item2.put("action",  new OnOtherClick(){
            @Override
            public void onClick() {
                ARouter.getInstance().build("/test/activity1").navigation();
            }
        });
        items.add(item2);

        Map<String,Object> item3 = new HashMap<>();
        item3.put("title","功能菜单");
        item3.put("action",  new OnOtherClick(){
            @Override
            public void onClick() {

            }
        });
        items.add(item3);

        Map<String,Object> item4 = new HashMap<>();
        item4.put("title","设置");
        item4.put("action",  new OnOtherClick(){
            @Override
            public void onClick() {

            }
        });
        items.add(item4);
    }


    private void testNetwork() {
        Call<ResponseBody> call = ServiceGenerator.createService(BookService.class).getShop("63.223.108.42");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    ResponseBody  body = response.body();
                    if (body != null) {
                        String s = body.string();
                        Log.e("network", s);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("network",t.toString());
            }
        });
    }


    ///////////////////////////////////////
    public interface  OnOtherClick {
        public void onClick();
    }
}
