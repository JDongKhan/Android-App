package com.jd.other.fragment;



import androidx.fragment.app.Fragment;

import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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
        //导入
        listView.setAdapter(lazyAdapter);

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
        ServiceGenerator.getInstance().createService(BookService.class).getShop("63.223.108.42")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.i("1111","onSubscribe");
            }

            @Override
            public void onNext(String s) {
                Log.i("1111","onNext:"+s);
                Toast.makeText(OtherFragment.this.getContext(),s,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Throwable e) {
                Log.i("1111","onError"+e.getMessage());
                Toast.makeText(OtherFragment.this.getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onComplete() {
                Log.i("1111","onComplete");
            }
        });
    }


    ///////////////////////////////////////
    public interface  OnOtherClick {
        public void onClick();
    }
}
