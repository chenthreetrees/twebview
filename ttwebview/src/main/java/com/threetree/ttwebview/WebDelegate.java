package com.threetree.ttwebview;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.webkit.ValueCallback;
import android.webkit.WebView;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;

/**
 * Created by Administrator on 2018/7/23.
 */

public abstract class WebDelegate extends BaseDelegate {

    /**
     * logic 类名
     */
    public static final String LOGIC_CLASS_NAME = "logic_class_name";
    /**
     * 初始url
     */
    public static final String URL_KEY = "url";

    /**
     * 是否从sd卡读取html等文件
     */
    public static final String IS_SDCARD = "is_sdcard";

    public static final String WEB_SETTINGS = "web_settings";

    private WebView mWebView = null;
    //使用弱引用防止内存泄漏
    private final ReferenceQueue<WebView> WEB_VIEW_QUEUE = new ReferenceQueue<>();
    private String mUrl = null;
    private boolean isSdCard = false;
    private boolean mIsWebViewAvailable = false;

    protected WebLogic mWebLogic;

    public abstract IWebViewInitListener getWebViewInit();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        mUrl = args.getString(URL_KEY);
        isSdCard = args.getBoolean(IS_SDCARD,false);
        initWebView();
        initLogic(args);
    }

    private <T extends WebLogic> void initLogic(Bundle args)
    {
        try {
            Class<T> tempTaskClassName = (Class<T>) Class.forName(args.getString(LOGIC_CLASS_NAME));
            Constructor<T> con = tempTaskClassName.getConstructor(WebDelegate.class,Bundle.class);
            mWebLogic = (T) con.newInstance(this,args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (mWebView != null)
            mWebView.onPause();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (mWebView != null)
            mWebView.onResume();
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        mIsWebViewAvailable = false;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.removeAllViews();
            mWebView.destroy();
            mWebView = null;
        }
    }


    private void initWebView()
    {
        if (mWebView != null) {
            mWebView.removeAllViews();
            mWebView.destroy();
        } else {
            //获取子类回调传回来的接口实例
            final IWebViewInitListener listener = getWebViewInit();
            if (listener != null) {
                final WeakReference<WebView> webViewWeakReference = new WeakReference<WebView>(new WebView(mActivity), WEB_VIEW_QUEUE);
                mWebView = webViewWeakReference.get();
                listener.initWebViewSettings(mWebView);
                mWebView.setWebViewClient(listener.initWebViewClient());
                mWebView.setWebChromeClient(listener.initWebChromeClient());
                //webview可用了
                mIsWebViewAvailable = true;
            } else {
                throw new NullPointerException("InitListener is null");
            }
        }
    }

    public FragmentActivity getWebActivity()
    {
        return mActivity;
    }

    public WebView getWebView()
    {
        if (mWebView == null) {
            throw new NullPointerException("WebView is null!");
        }
        return mIsWebViewAvailable ? mWebView : null;
    }

    public String getUrl()
    {
        if (mUrl == null) {
            throw new NullPointerException("WebView is null!");
        }
        return mUrl;
    }

    public boolean isSdCard(){
        return isSdCard;
    }

    /**
     * 重新加载
     */
    public void reloadUrl()
    {
        if (mWebView != null)
            mWebView.reload();
    }

    /**
     * 返回
     * @return true-返回成功 false-返回失败（可能已经是首页）
     */
    public boolean goBack()
    {
        if (mWebView != null && mWebView.canGoBack())
        {
            mWebView.goBack();
            return true;
        }
        return false;
    }

    /**
     * 调用js
     *
     * @param jsFunction
     */
    public void callJs(String jsFunction,ValueCallback<String> callback)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mWebView.evaluateJavascript("javascript:" + jsFunction, callback);
        } else {
            mWebView.loadUrl("javascript:" + jsFunction);
        }
    }

    @SuppressLint("JavascriptInterface")
    public void addJsInterface(Object object, String name)
    {
        mWebView.addJavascriptInterface(object, name);
    }


}
