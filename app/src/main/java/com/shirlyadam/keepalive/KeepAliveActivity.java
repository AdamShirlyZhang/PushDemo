package com.shirlyadam.keepalive;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

public class KeepAliveActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        KeepAliveManager.getInstance().init(this);
        KeepAliveManager.getInstance().addActivityToStack(this);
        Window window = getWindow();

        window.setGravity(Gravity.LEFT | Gravity.TOP);
        WindowManager.LayoutParams params = window.getAttributes();
        params.x = 0;
        params.y = 0;
        params.height = 1;
        params.width = 1;
        window.setAttributes(params);

    }
}
