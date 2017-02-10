package com.shirlyadam.keepalive;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * 替身服务
 * <br/>
 * 替代别服务服务把相同id 的Foreground通知关闭
 */
public class NullIntentService extends IntentService {

    private static int SERVICE_NOTIFICATION;
    private ServiceNotification mServiceNotification;

    public NullIntentService() {
        super("NullIntentService");
    }

    public static void start(Context context, int notification_id) {
        SERVICE_NOTIFICATION = notification_id;
        Intent intent = new Intent(context, NullIntentService.class);
        context.startService(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
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

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mServiceNotification.hideNotification(this, SERVICE_NOTIFICATION);
    }
}
