package com.threetree.ttwebview;

import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Administrator on 2018/7/23.
 */

public interface IWebViewInitListener {
    void initWebViewSettings(WebView webView);
    //针对浏览器本身行为的控制,如前进后退的回调
    WebViewClient initWebViewClient();
    //针对页面的控制,如js交互
    WebChromeClient initWebChromeClient();
}
