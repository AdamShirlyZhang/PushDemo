package com.shirlyadam;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.shirlyadam.keepalive.NullIntentService;
import com.shirlyadam.keepalive.PushService;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getApplicationContext().startService(new Intent(getApplicationContext(), PushService.class));
        //开启替身服务，防止通知栏显示 ***正在运行
        NullIntentService.start(getApplicationContext(), PushService.SERVICE_NOTIFICATION);
    }
}
