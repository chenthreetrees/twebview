package com.threetree.ttwebview;

import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Administrator on 2018/7/23.
 */

public class DefaultWebDelegate extends CommonDelegate implements IWebViewInitListener{


    /**
     * 标题布局
     */
    private FrameLayout mTitleFlyt;

    /**
     * webview布局
     */
    private FrameLayout mWebFlyt;

    /**
     * 加载进度
     */
    private ProgressBar mProgressbar;

    /**
     * 加载失败view
     */
    private View mLoadFailView;
    private TextView mErrorTv;

    /**
     * 重新加载按钮
     */
    private Button mReloadBtn;
    private WebView mWebView;

    //必须用这种方式创建WebDelegateDefault 类
    public static <T extends WebLogic>DefaultWebDelegate create(Class<T> webLogic,String url)
    {
        final Bundle bundle = new Bundle();
        bundle.putString(WebDelegate.URL_KEY, url);
        bundle.putString(WebDelegate.LOGIC_CLASS_NAME,webLogic.getName());
        final DefaultWebDelegate delegate = new DefaultWebDelegate();
        delegate.setArguments(bundle);
        return delegate;
    }

    @Override
    public void initWebViewSettings(WebView webView)
    {
        //开启调试模式
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            if (0 != (mActivity.getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE))
            {
                WebView.setWebContentsDebuggingEnabled(true);
            }
        }
        initSettings(webView);
    }

    protected void initSettings(final WebView webView)
    {
        //不能横向滚动
        webView.setHorizontalScrollBarEnabled(false);
        //不能纵向滚动
        webView.setVerticalScrollBarEnabled(false);
        //允许截图
        webView.setDrawingCacheEnabled(true);
        //屏蔽长按事件
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        //重写返回按键
        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (webView.canGoBack()) { //表示按返回键时的操作
                    webView.goBack(); //后退
                    return true; //已处理
                }
                return false;
            }
        });
        //初始化WebSettings
        final WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        // 设置允许JS弹窗
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        //隐藏缩放控件
        settings.setBuiltInZoomControls(false);
        settings.setDisplayZoomControls(false);
        //是否支持使用屏幕控件或手势进行缩放，默认是true
        settings.setSupportZoom(false);
        //文件权限
        settings.setAllowFileAccess(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setAllowContentAccess(true);
        //缓存相关
        settings.setAppCacheEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        //从Android5.0开始，WebView默认不支持同时加载Https和Http混合模式
        //需要打开这个设置来支持
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
    }

    @Override
    public WebViewClient initWebViewClient()
    {
        return new WebViewClientImpl(this);
    }

    @Override
    public WebChromeClient initWebChromeClient()
    {
        return new WebChromeClientImpl(this);
    }

    @Override
    public IWebViewInitListener getWebViewInit()
    {
        return this;
    }

    //基类Delegate中封装的方法,Fragment会加载这个方法返回的view或者layout布局
    @Override
    public Object setLayout()
    {
        return R.layout.default_web;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView)
    {
        mTitleFlyt = (FrameLayout) rootView.findViewById(R.id.titleLayout);
        View view = mWebLogic.getTitleView();
        if(view != null)
        {
            mTitleFlyt.addView(mWebLogic.getTitleView());
        }

        mWebFlyt = (FrameLayout) rootView.findViewById(R.id.web_flyt);
        mWebView = getWebView();
        if(mWebView != null)
        {
            mWebFlyt.addView(mWebView);
        }
        addJsInterface(mWebLogic,mWebLogic.getInterfaceNameForJs());

        mLoadFailView = rootView.findViewById(R.id.web_load_error_view);
        mErrorTv = (TextView)rootView.findViewById(R.id.error_tv);
        mProgressbar = (ProgressBar) rootView.findViewById(R.id.webview_loadprogress);
        mReloadBtn = (Button) rootView.findViewById(R.id.web_load_error_reload_btn);
        mReloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                // 重新加载
                reloadUrl();
            }
        });

        if (getUrl() != null) {
            //进行页面加载
            Router.getInstance().loadPage(this, getUrl());
        }
    }


    @Override
    public void loadStart()
    {
        mLoadFailView.setVisibility(View.GONE);
        mWebLogic.loadStart();
    }

    @Override
    public void loadEnd()
    {
        mWebFlyt.setVisibility(View.VISIBLE);
        mWebView.setVisibility(View.VISIBLE);
        mWebLogic.loadEnd();
    }

    @Override
    public void loadError(String message)
    {
        mLoadFailView.setVisibility(View.VISIBLE);
        mWebFlyt.setVisibility(View.GONE);
        mWebView.setVisibility(View.GONE);
        if(!TextUtils.isEmpty(message))
        {
            mErrorTv.setText(message);
        }
        mWebLogic.loadError(message);
    }

    @Override
    public void updateTitle(String title)
    {
        mWebLogic.updateTitle(title);
    }

    @Override
    public void updateProgress(int newProgress)
    {
        if (newProgress == 100) {
            mProgressbar.setVisibility(View.GONE);
            mProgressbar.setProgress(0);
        } else {
            mProgressbar.setVisibility(View.VISIBLE);
            mProgressbar.setProgress(newProgress);
        }
        mWebLogic.updateProgress(newProgress);
    }

    @Override
    public boolean onJsPrompt(String url, String message, String defaultValue, JsPromptResult result)
    {
        if(mWebLogic.onJsCall(url,message,defaultValue,result))
        {
            return true;
        }
        return super.onJsPrompt(url, message, defaultValue, result);
    }

    @Override
    public boolean onJsAlert(String url, String message, JsResult result)
    {
        Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
        result.cancel();
        return true;
    }
}
