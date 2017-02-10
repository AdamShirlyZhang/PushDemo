package com.shirlyadam;

import android.app.Application;

/**
 * Created by Adam on 2017/2/8.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        /**
         * 注意开启推送服务最好在splashActivity里边，因为如果在Application开启会导致onCreate方法执行两次
         */


    }
}
