package org.yjn.common;

import android.app.Application;

import com.hjq.toast.ToastUtils;

public class Common {
    public static void init(Application application){
        ToastUtils.init(application);
    }
}
