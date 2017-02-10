package com.shirlyadam.keepalive;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;


/**
 * @author Adam
 */
public class PushService extends Service {

    private static final String TAG = PushService.class.getSimpleName();
    public static final int SERVICE_NOTIFICATION = 1;
    private ServiceNotification mServiceNotification;
    private KeepAliveReceiver mReceiver;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        init();
    }

    private void init() {

        registerPushReleatedReceiver();

        // 使用替身服务在前台展示
        Notification notification = new Notification();
        notification.flags = Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR | Notification.FLAG_ONLY_ALERT_ONCE;
        Intent notificationIntent = new Intent("");
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        notification.contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        String title = "推送服务";
        String message = "正在后台运行";
        notification.setLatestEventInfo(getApplicationContext(), title, message, notification.contentIntent);
        mServiceNotification = ServiceNotification.getInstance();
        mServiceNotification.showNotification(this, SERVICE_NOTIFICATION, notification);
    }


    /**
     * 注册与推送进程相关的广播
     */
    private void registerPushReleatedReceiver() {
        IntentFilter filter = new IntentFilter();
        mReceiver = new KeepAliveReceiver();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(mReceiver, filter);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //判断连接是否建立如果没有则建立连接
        Log.d(TAG, "onStartCommand" + android.os.Process.myPid());


        //开启替身服务，让替身服务把相同id 的Foreground通知关闭
        NullIntentService.start(this, SERVICE_NOTIFICATION);
        return Service.START_STICKY;

    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        mServiceNotification.hideNotification(this, SERVICE_NOTIFICATION);
        super.onDestroy();
        startService(new Intent(getApplicationContext(), PushService.class));
        unregisterReceiver(mReceiver);
    }

}
