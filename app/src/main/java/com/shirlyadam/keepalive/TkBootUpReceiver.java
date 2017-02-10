package com.shirlyadam.keepalive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @author Adam
 */
public class TkBootUpReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())
                || "android.intent.action.MEDIA_MOUNTED".equals(intent.getAction())
                || "android.intent.action.MEDIA_EJECT".equals(intent.getAction())) {
            context.startService(new Intent(context, PushService.class));
        }
    }
}
