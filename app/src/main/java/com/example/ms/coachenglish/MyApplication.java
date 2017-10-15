package com.example.ms.coachenglish;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;

/**
 * 全局获取context
 * Created by MS on 2017/10/10.
 */

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate(){
        super.onCreate();
        context = getApplicationContext();
        LitePal.initialize(context);
    }

    public static Context getContext(){
        return context;
    }
}
