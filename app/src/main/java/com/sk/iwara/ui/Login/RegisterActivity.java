package com.sk.iwara.ui.Login;

import android.webkit.WebSettings;
import android.webkit.WebViewClient;

import com.sk.iwara.base.BaseActivity;
import com.sk.iwara.databinding.ActivityRegisterBinding;

public class RegisterActivity extends BaseActivity<ActivityRegisterBinding> {
    @Override
    protected void init() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initUI() {
        WebSettings webSettings = binding.registerWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // 获取默认的 User-Agent
        String defaultUserAgent = webSettings.getUserAgentString();

        // 设置自定义 User-Agent
        String customUserAgent = defaultUserAgent + " MyCustomApp/1.0";
        webSettings.setUserAgentString(customUserAgent);

        // 设置 WebViewClient，防止打开浏览器
        binding.registerWebView.setWebViewClient(new WebViewClient());


        // 加载网址
        binding.registerWebView.loadUrl("https://www.iwara.tv/register");
    }
}
