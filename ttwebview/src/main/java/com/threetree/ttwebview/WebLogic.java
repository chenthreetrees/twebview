package com.threetree.ttwebview;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.ValueCallback;

/**
 * Created by Administrator on 2018/7/24.
 */

public abstract class WebLogic {
    private WebDelegate mWebDelegate;
    protected FragmentActivity mActivity;

    public WebLogic(WebDelegate webDelegate,Bundle args)
    {
        mWebDelegate = webDelegate;
        mActivity = mWebDelegate.getWebActivity();
    }

    /**
     * 设置webview的titlebar
     * @return
     */
    public abstract View getTitleView();

    /**
     * 更新标题
     * @param title
     */
    public abstract void updateTitle(String title);

    /**
     * 设置提供给js调用的名称
     * @return
     */
    public abstract String getInterfaceNameForJs();

    /**
     * js调用android
     * 监听的是prompt接口
     * @param url
     * @param message
     * @param defaultValue
     * @param result
     * @return
     */
    public boolean onJsCall(String url, String message, String defaultValue, JsPromptResult result){
        return false;
    }

    /**
     * 页面开始加载
     */
    public void loadStart()
    {

    }

    /**
     * 页面加载完成
     * 在此方法调用js
     */
    public void loadEnd()
    {

    }

    /**
     * 页面加载出错
     * @param message
     */
    public void loadError(String message)
    {

    }

    /**
     * 加载进度
     * @param newProgress
     */
    public void updateProgress(int newProgress)
    {

    }

    /**
     * 返回
     * @return
     */
    public boolean goBack()
    {
        return mWebDelegate.goBack();
    }

    /**
     * 刷新
     */
    public void refresh()
    {
        mWebDelegate.reloadUrl();
    }

    /**
     * 调用js
     * @param js
     * @param callback
     */
    public void callJs(String js,ValueCallback<String> callback)
    {
        mWebDelegate.callJs(js,callback);
    }

    public boolean isShowProgressbar()
    {
        return true;
    }

}
