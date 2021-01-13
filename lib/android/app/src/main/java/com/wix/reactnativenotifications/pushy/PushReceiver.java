package com.wix.reactnativenotifications.pushy;

import android.os.Bundle;
import me.pushy.sdk.Pushy;

import android.app.Notification;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.app.PendingIntent;

import com.wix.reactnativenotifications.BuildConfig;
import com.wix.reactnativenotifications.core.notification.IPushNotification;
import com.wix.reactnativenotifications.core.notification.PushNotification;
import static com.wix.reactnativenotifications.Defs.LOGTAG;

/**
 * Instance-ID + token refreshing handling service. Contacts the FCM to fetch the updated token.
 *
 * @author amitd
 */
public class PushReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d(LOGTAG, "New message from Pushy: " + bundle);
         try {
            final IPushNotification notification = PushNotification.get(context, bundle);
            notification.onReceived();
        } catch (IPushNotification.InvalidNotificationException e) {
            if(BuildConfig.DEBUG) Log.v(LOGTAG, "Pushy message handling aborted", e);
        }
    }
}
