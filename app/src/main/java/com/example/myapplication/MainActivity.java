package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;


import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Fragments.MainFragment;
import com.example.myapplication.Fragments.MineFragment;
import com.example.myapplication.Fragments.WenjuanFragment;
import com.example.myapplication.loginutil.LogActivity;
import com.example.myapplication.loginutil.LogServece;
import com.example.myapplication.loginutil.RetSuccess;
import com.example.myapplication.versionutil.VersionRet;
import com.example.myapplication.versionutil.VersionService;
import com.luck.indicator.EasyIndicator;
import com.safframework.log.L;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends BaseActivity{

    EasyIndicator easy_indicator;
    ViewPager vp;
    List<Fragment> fragmentList;
    int current_fragment_in_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkversion();
        current_fragment_in_list=-1;
        easy_indicator = (EasyIndicator) findViewById(R.id.main_indicator);
        vp=(ViewPager)findViewById(R.id.main_viewpager);
        easy_indicator.setTabTitles(new String[]{"主页", "问卷","我的"});
        easy_indicator.setOnTabClickListener(new EasyIndicator.onTabClickListener() {               //点击tab跳转
            @Override
            public void onTabClick(String s, int i) { }
        });

        fragmentList=new ArrayList<Fragment>();
        fragmentList.add(new MainFragment());
        fragmentList.add(new WenjuanFragment());
        fragmentList.add(new MineFragment());
        // 自定义设置
        easy_indicator.setViewPage( vp,new MyPagerAdapter(getSupportFragmentManager(),fragmentList));
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

            @Override
            public void onPageSelected(int position) {
                current_fragment_in_list=position;
            }

            @Override
            public void onPageScrollStateChanged(int state) { }
        });
    }

    private void checkversion() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(new MyApplication().getBaseurl())//基础URL 建议以 / 结尾
                .addConverterFactory(GsonConverterFactory.create())//设置 Json 转换器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//RxJava 适配器
                .build();

        VersionService versionService =retrofit.create((VersionService.class));

        versionService.checkforupdate()
                .observeOn(AndroidSchedulers.mainThread())			//指定observer的回调方法运行在主线程
                .subscribeOn(Schedulers.io())						    //运行在io线程
                .subscribe(new Observer<VersionRet>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(VersionRet versionRet) {
                        PackageManager pm = getApplication().getPackageManager();
                        try {
                            PackageInfo packageInfo = pm.getPackageInfo(getApplication().getPackageName(), 0);
                            int i=compareVersion(packageInfo.versionName,versionRet.getVersion());   //0代表相等，1代表version1大于version2，-1代表version1小于version2
                           // if(i)
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public static int compareVersion(String version1, String version2) {
        if (version1.equals(version2)) {
            return 0;
        }
        String[] version1Array = version1.split("\\.");
        String[] version2Array = version2.split("\\.");
        int index = 0;
        // 获取最小长度值
        int minLen = Math.min(version1Array.length, version2Array.length);
        int diff = 0;
        // 循环判断每位的大小
        while (index < minLen
                && (diff = Integer.parseInt(version1Array[index])
                - Integer.parseInt(version2Array[index])) == 0) {
            index++;
        }
        if (diff == 0) {
            // 如果位数不一致，比较多余位数
            for (int i = index; i < version1Array.length; i++) {
                if (Integer.parseInt(version1Array[i]) > 0) {
                    return 1;
                }
            }

            for (int i = index; i < version2Array.length; i++) {
                if (Integer.parseInt(version2Array[i]) > 0) {
                    return -1;
                }
            }
            return 0;
        } else {
            return diff > 0 ? 1 : -1;
        }
    }

}


