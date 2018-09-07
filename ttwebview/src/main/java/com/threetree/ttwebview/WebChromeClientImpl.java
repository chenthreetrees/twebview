package com.threetree.ttwebview;

import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * Created by Administrator on 2018/7/23.
 */

public class WebChromeClientImpl extends WebChromeClient {
    protected final CommonDelegate DELEGATE;
    public WebChromeClientImpl(CommonDelegate DELEGATE) {
        this.DELEGATE = DELEGATE;
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result)
    {
        if(DELEGATE.onJsAlert(url,message,result))
            return true;
        return super.onJsAlert(view, url, message, result);
    }

    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result)
    {
        if(DELEGATE.onJsPrompt(url,message,defaultValue,result))
            return true;
        return super.onJsPrompt(view, url, message, defaultValue, result);
    }

    @Override
    public void onReceivedTitle(WebView view, String title)
    {
        DELEGATE.updateTitle(title);
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress)
    {
        DELEGATE.updateProgress(newProgress);
    }
}
