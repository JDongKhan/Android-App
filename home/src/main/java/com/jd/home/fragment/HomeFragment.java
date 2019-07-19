package com.jd.home.fragment;


import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;
import android.util.Log;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.jd.core.base.BaseFragment;
import com.jd.core.view.BannerPager;
import com.jd.home.R;
import com.jd.home.R2;
import com.jd.home.service.BookService;
import com.jd.home.utils.ServiceGenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment {

    @BindView(R2.id.textView)
    public TextView textView;

    @BindView(R2.id.router_test)
    public Button routerTest;

    @BindView(R2.id.network)
    public Button network;

    @BindView(R2.id.imageView)
    public ImageView imageView;

    @BindView(R2.id.banner)
    BannerPager bannerPager;

    public HomeFragment() {
        // Required empty public constructor
    }


    @OnClick(R2.id.router_test)
    public void click() {
        ARouter.getInstance().build("/test/activity1").navigation();
    }


    @OnClick(R2.id.network)
    public void click2() {
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



    @Override
    protected int getLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected void initView() {
        textView.setText("hello");
        // 加载应用资源
        int resource = R.drawable.default_icon;
        Glide.with(this).load(resource).into(imageView);

        List<Integer> list = new ArrayList<>();
        list.add(R.mipmap.ic_banner_1);
        list.add(R.mipmap.ic_banner_2);
        list.add(R.mipmap.ic_banner_3);
        list.add(R.mipmap.ic_banner_4);
        list.add(R.mipmap.ic_banner_5);
        bannerPager.setAdapter(new IndexBannderAdapter(this.getContext(), list));

        bannerPager.setOnPageClickListener(new BannerPager.OnPageClickListener() {
            @Override
            public void onPageClick(int position) {

            }
        });
    }


    private class IndexBannderAdapter extends BannerPager.BannerAdapter {

        public IndexBannderAdapter(Context context, List list) {
            super(context, list);
        }

        @Override
        public int getCount() {
            return super.getCount();
        }
    }

}
