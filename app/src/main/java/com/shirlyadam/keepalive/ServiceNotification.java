package com.shirlyadam.keepalive;

import android.app.Notification;
import android.app.Service;
import android.util.Log;

import java.util.LinkedHashMap;
import java.util.Map;

// Code to make a Service stay in the foreground from
public abstract class ServiceNotification {
    public static ServiceNotification getInstance() {
        /*if (Build.VERSION.SDK_INT <= 4)
			return PreEclair.Holder.sInstance;
		else*/
        return EclairAndBeyond.Holder.sInstance;
    }

    public abstract void showNotification(Service context, int id, Notification notification);

    public abstract void hideNotification(Service context, int id);

	/*private static class PreEclair extends ServiceNotification {
		private static class Holder {
			private static final PreEclair sInstance = new PreEclair();
		}
		private NotificationManager getNotificationManager(Context context) {
			return (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		}
		public void showNotification(Service context, int id, Notification n) {
			context.setForeground(true);
			getNotificationManager(context).notify(id, n);
		}
		public void hideNotification(Service context, int id) {
			context.setForeground(false);
			getNotificationManager(context).cancel(id);
		}
	}*/

    private static class EclairAndBeyond extends ServiceNotification {
        private static Map<Integer, Service> serviceMap = new LinkedHashMap<>();

        private static class Holder {
            private static final EclairAndBeyond sInstance = new EclairAndBeyond();
        }

        public void showNotification(Service context, int id, Notification n) {
            Log.d("EclairAndBeyond", "showNotification " + id + " " + n);
            context.startForeground(id, n);
            serviceMap.put(id, context);
        }

        public void hideNotification(Service context, int id) {
            Log.d("EclairAndBeyond", "hideNotification");
            if (context == null) {
                context = serviceMap.get(id);
            }
            if (context != null) {
                context.stopForeground(true);
            }
            serviceMap.remove(id);
        }
    }

}

