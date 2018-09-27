package com.threetree.twebview;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.threetree.ttwebview.DefaultWebDelegate;

public class MainActivity extends AppCompatActivity {

    private Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getContentView();
    }

    //加载内容页面
    private void getContentView()
    {
//        mFragment = DefaultWebDelegate.create(MenuLogic.class, "https://www.zhihu.com");
        Bundle bundle = new Bundle();
        bundle.putString("test","test");
        mFragment = DefaultWebDelegate.create(MenuLogic.class, "js_test.html",bundle);
//        mFragment = DefaultWebDelegate.create(MenuLogic.class, "http://pvp.592you.com/");
        FragmentManager tm = getSupportFragmentManager();
        FragmentTransaction t = tm.beginTransaction();
        t.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        t.replace(R.id.fragment, mFragment);
        t.commitAllowingStateLoss();
    }
}
