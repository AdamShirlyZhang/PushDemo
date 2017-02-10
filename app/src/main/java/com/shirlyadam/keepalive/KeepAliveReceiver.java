package com.shirlyadam.keepalive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.shirlyadam.utils.AppUtils;


/**
 * @author Adam
 */
public class KeepAliveReceiver extends BroadcastReceiver {

    NetworkInfo.State wifiState = null;
    NetworkInfo.State mobileState = null;
    private static final String TAG = KeepAliveReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            String action = intent.getAction();
            //获取手机的连接服务管理器，这里是连接管理器类
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
            mobileState = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();

            if (wifiState != null && mobileState != null && NetworkInfo.State.CONNECTED != wifiState && NetworkInfo.State.CONNECTED == mobileState) {
                Log.d(TAG, "手机移动网络连接");
            } else if (wifiState != null && mobileState != null && NetworkInfo.State.CONNECTED == wifiState && NetworkInfo.State.CONNECTED != mobileState) {
                Log.d(TAG, "手机wifi网络连接");
            } else if (wifiState != null && mobileState != null && NetworkInfo.State.CONNECTED != wifiState && NetworkInfo.State.CONNECTED != mobileState) {
                Log.d(TAG, "其他网络状态连接");
            }
            if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                Log.d(TAG, "ACTION_SCREEN_OFF");
                KeepAliveManager.getInstance().init(context);
                KeepAliveManager.getInstance().startKeepAliveActivity();
            } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
                Log.d(TAG, "ACTION_USER_PRESENT");
                KeepAliveManager.getInstance().init(context);
                KeepAliveManager.getInstance().stopKeepAliveActivity();
            }
            //主进程挂掉才需要启动推送进程
            startPushService(context);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.toString());
        }
    }

    private void startPushService(Context context) {
        if (!AppUtils.isAppAlive(context, context.getApplicationInfo().packageName)) {
            context.startService(new Intent(context, PushService.class));
        }
    }
}
