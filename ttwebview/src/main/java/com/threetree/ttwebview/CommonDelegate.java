package com.threetree.ttwebview;

import android.webkit.JsPromptResult;
import android.webkit.JsResult;

/**
 * Created by Administrator on 2018/7/24.
 */

public abstract class CommonDelegate extends WebDelegate {

    public void loadStart()
    {

    }

    public void loadEnd()
    {

    }

    public void loadError(String message)
    {

    }

    public void updateTitle(String title)
    {

    }

    public void updateProgress(int newProgress)
    {

    }

    public boolean onJsPrompt(String url, String message, String defaultValue, JsPromptResult result)
    {
        return false;
    }

    public boolean onJsAlert(String url, String message, JsResult result)
    {
        return false;
    }
}
