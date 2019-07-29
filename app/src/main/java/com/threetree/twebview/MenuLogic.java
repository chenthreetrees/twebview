package com.threetree.twebview;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.ValueCallback;
import android.widget.TextView;
import android.widget.Toast;

import com.threetree.ttwebview.WebDelegate;
import com.threetree.ttwebview.WebLogic;

import java.util.Set;

/**
 * Created by Administrator on 2018/7/24.
 */

public class MenuLogic extends WebLogic {

    TextView mBackTv;
    TextView mTitleTv;
    TextView mMenuTv;

    public MenuLogic(WebDelegate webDelegate,Bundle args)
    {
        super(webDelegate,args);
    }

    @Override
    public String getInterfaceNameForJs()
    {
        return "android";
    }

    @Override
    public boolean isShowProgressbar()
    {
        return false;
    }

    @Override
    public View getTitleView()
    {
        View view = View.inflate(mActivity,R.layout.include_common_title_bar,null);
        mBackTv = (TextView) view.findViewById(R.id.back_tv);
        mBackTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(!goBack())
                {
                    mActivity.finish();
                }
            }
        });
        mTitleTv = (TextView) view.findViewById(R.id.title_tv);
        mMenuTv = (TextView) view.findViewById(R.id.menu_tv);
        mMenuTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                callJs("callJS()", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value)
                    {
//                        String result = value.substring(1,value.length()-1);//去掉js返回字符串带的双引号
                        Toast.makeText(mActivity,value,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        return view;
    }

    @Override
    public void loadEnd()
    {

    }

    @Override
    public void updateTitle(String title)
    {
        mTitleTv.setText(title);
    }

    @Override
    public boolean onJsCall(String url, String message, String defaultValue, JsPromptResult result)
    {
        Uri uri = Uri.parse(message);
        // 如果url的协议 = 预先约定的 js 协议
        // 就解析往下解析参数
        if ( uri.getScheme().equals("js")) {

            // 如果 authority  = 预先约定协议里的 webview，即代表都符合约定的协议
            // 所以拦截url,下面JS开始调用Android需要的方法
            if (uri.getAuthority().equals("webview")) {

                // 可以在协议上带有参数并传递到Android上
                String arg1="",arg2="";
                Set<String> collection = uri.getQueryParameterNames();
                if(collection.contains("arg1"))
                {
                    arg1 = uri.getQueryParameter("arg1");
                }
                if(collection.contains("arg2"))
                {
                    arg2 = uri.getQueryParameter("arg2");
                }
                Toast.makeText(mActivity,"js调用了Android的方法成功啦,arg1="+arg1+" arg2="+arg2,Toast.LENGTH_SHORT).show();
                //参数result:代表消息框的返回值(输入值)
                result.confirm("android返回的result");
            }
            return true;
        }
        return false;
    }

    @JavascriptInterface
    public void callAndroid(String arg)
    {
        Toast.makeText(mActivity,"js调用了Android的方法成功啦,arg=" + arg,Toast.LENGTH_SHORT).show();
    }
}
