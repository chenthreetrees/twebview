# twebview

该框架将webview的使用进行封装，使用fragment来装载webview页面，将webview生命周期和fragment绑定，提供自定义的titlebar和业务处理。

Android调用JS代码的方法有2种：loadUrl（），evaluateJavascript（）。
本框架两种都采用，主要根据android系统版本，兼容低版本，
通过调用WebLogic的方法：`public void callJs(String js,ValueCallback<String> callback)`

对于JS调用Android代码的方法有3种：
通过WebView的addJavascriptInterface（）进行对象映射。
通过 WebViewClient 的shouldOverrideUrlLoading ()方法回调拦截 url。
通过 WebChromeClient 的onJsAlert()、onJsConfirm()、onJsPrompt（）方法回调拦截JS对话框alert()、confirm()、prompt（） 消息。

本框架采用第一种和第三种，推荐使用第三种。
第一种使用：编写具体业务类继承至WebLogic，重写`getInterfaceNameForJs()`方法，返回相应InterfaceName。
类里面编写相应的func()，方法添加注解'@JavascriptInterface',js调用时候，使用`InterfaceName:func()`。

第三种使用：业务类里重写`public boolean onJsCall(String url, String message, String defaultValue, JsPromptResult result)`,
message为js传过来的字符串，android通过`result.confirm("android返回的result")`返回字符串结果给js。
**注意：**如果没有返回结果，要使用`result.cancel()`，否则会出现web页面阻塞。
js调用的时候，使用`var result=prompt("js://demo?arg1=111&arg2=222")`，传递的参数需要android和js约定好。

## 引用

项目的gradle文件：
```
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```
在app的gradle文件引用依赖：
```
compile 'com.github.chenthreetrees:twebview:1.0.0'
```

## 使用

创建具体的业务处理类，该类继承至WebLogic，重写以下方法:
```
    public abstract View getTitleView()//设置webview的titlebar

    public abstract void updateTitle(String title)//更新标题

    public abstract String getInterfaceNameForJs()//设置提供给js调用的名称

    //js调用android 监听的是prompt接口
    public boolean onJsCall(String url, String message, String defaultValue, JsPromptResult result)

    //页面开始加载
    public void loadStart()

    //页面加载完成,通常在此方法调用js
    public void loadEnd()

    //页面加载出错,框架有默认的处理页面
    public void loadError(String message)

    //加载进度,框架有默认的处理效果
    public void updateProgress(int newProgress)

    //返回
    //true-返回成功 false-返回失败（可能已经是首页）
    public boolean goBack()

    //刷新
    public void refresh()

    //调用js
    public void callJs(String js,ValueCallback<String> callback)

```

创建fragment，传入业务类：
```
    //加载url
    Fragment fragment = DefaultWebDelegate.create(MenuLogic.class, "https://www.zhihu.com");
    //加载asserts目录下的文件
    Fragment fragment = DefaultWebDelegate.create(MenuLogic.class, "js_test.html");
```

自定义：
DefaultWebDelegate采用默认的设置，WebChromeClient和WebViewClient，更改设置可以重写DefaultWebDelegate的`initSettings()`方法。、

其他设置参考demo和源码

