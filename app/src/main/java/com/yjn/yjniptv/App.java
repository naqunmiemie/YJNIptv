package com.yjn.yjniptv;

import android.app.Application;
import android.content.Context;

import com.yjn.common.Common;


public class App extends Application {
    public static App mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        Context context = this;
        init();
    }

    private void init() {
        Common.init(mInstance);
    }

    public static App getInstance() {
        return mInstance;
    }
}
