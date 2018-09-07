package com.threetree.ttwebview;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Administrator on 2018/7/23.
 */

public class WebViewClientImpl extends WebViewClient {
    protected final CommonDelegate DELEGATE;
    public WebViewClientImpl(CommonDelegate DELEGATE) {
        this.DELEGATE = DELEGATE;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url)
    {
        return false;
    }

    //页面开始加载时回调(页面后退也会回调)
    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon)
    {
        super.onPageStarted(view, url, favicon);
        DELEGATE.loadStart();
    }

    @Override
    public void onPageFinished(WebView view, String url)
    {
        super.onPageFinished(view, url);
        DELEGATE.loadEnd();
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error)
    {
        super.onReceivedSslError(view, handler, error);
        DELEGATE.loadError(error.toString());
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error)
    {
        super.onReceivedError(view, request, error);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            DELEGATE.loadError(error.getDescription().toString());
        }else
        {
            DELEGATE.loadError(error.toString());
        }

    }
}
