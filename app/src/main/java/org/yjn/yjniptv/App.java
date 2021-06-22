package org.yjn.yjniptv;

import android.app.Application;

import org.yjn.common.Common;


public class App extends Application {
    public static App mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        init();
    }

    private void init() {
        Common.init(mInstance);
    }

    public static App getInstance() {
        return mInstance;
    }
}
