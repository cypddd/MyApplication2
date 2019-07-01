package com.example.myapplication.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.myapplication.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class WenjuanFragment extends Fragment {



    public WenjuanFragment() {
        // Required empty public constructor
    }


    WebView webview;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_wenjuan, null);
        webview = view.findViewById(R.id.wv_wenjuan);
        webview.setWebViewClient(new WebViewClient() {
            //设置在webView点击打开的新网页在当前界面显示,而不跳转到新的浏览器中
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                view.canGoBack();
                return true;
            }
        });
        webview.setWebChromeClient(new WebChromeClient());
        webview.getSettings().setJavaScriptEnabled(true);  //设置WebView属性,运行执行js脚本
        webview.loadUrl("http://cyp.natapp1.cc/wenjuan");          //调用loadUrl方法为WebView加入链接
        webview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK ) {
                        //这里处理返回键事件
                        if (webview.canGoBack()){
                            webview.goBack();
                            //Toast.makeText(getActivity(), "ok", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }
                }
                return false;
            }
        });
        return view;

    }
    private void initView() {
        WebSettings setting = webview.getSettings();
        setting.setJavaScriptEnabled(true);//支持Js
        setting.setCacheMode(WebSettings.LOAD_DEFAULT);//缓存模式
        //是否支持画面缩放，默认不支持
        setting.setBuiltInZoomControls(true);
        setting.setSupportZoom(true);
        //是否显示缩放图标，默认显示
        setting.setDisplayZoomControls(false);
        //设置网页内容自适应屏幕大小
        setting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);//注意网上例程很多的是.SINGLE_COLUMN，但调试时发现移动版网站会错位，所以改成
        //SINGLE_COLUMN

        setting.setUseWideViewPort(true);
        setting.setLoadWithOverviewMode(true);

    }

}
