package com.threetree.ttwebview;

import android.webkit.URLUtil;
import android.webkit.WebView;

/**
 * 路由截断, 线程安全的惰性单例模式
 * Created by Administrator on 2018/7/23.
 */

public class Router {
    private Router() {
    }
    private static class Holder {
        private static final Router INSTANCE = new Router();
    }
    public static Router getInstance() {
        return Holder.INSTANCE;
    }
    public final void loadPage(WebDelegate delegate, String url) {
        loadPage(delegate.getWebView(), url);
    }
    private void loadWebPage(WebView webView, String url) {
        if (webView != null) {
            webView.loadUrl(url);
        } else {
            throw new NullPointerException("WebView is null!");
        }
    }
    //在assets文件夹中的本地页面(和res文件夹同级)
    private void loadLocalPage(WebView webView, String url) {
        loadWebPage(webView, "file:///android_asset/" + url);
    }
    private void loadPage(WebView webView, String url) {
        if (URLUtil.isNetworkUrl(url) || URLUtil.isAssetUrl(url)) {
            loadWebPage(webView, url);
        } else {
            loadLocalPage(webView, url);
        }
    }
}
