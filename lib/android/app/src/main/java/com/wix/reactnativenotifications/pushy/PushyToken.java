package com.wix.reactnativenotifications.pushy;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import me.pushy.sdk.Pushy;

import com.facebook.react.ReactApplication;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.wix.reactnativenotifications.BuildConfig;
import com.wix.reactnativenotifications.core.JsIOHelper;

import static com.wix.reactnativenotifications.Defs.LOGTAG;
import static com.wix.reactnativenotifications.Defs.TOKEN_RECEIVED_EVENT_NAME;

public class PushyToken implements IPushyToken {

    final protected Context mAppContext;
    final protected JsIOHelper mJsIOHelper;

    protected static String sToken;

    protected PushyToken(Context appContext) {
        if (!(appContext instanceof ReactApplication)) {
            throw new IllegalStateException("Application instance isn't a react-application");
        }
        mJsIOHelper = new JsIOHelper();
        mAppContext = appContext;
    }

    public static IPushyToken get(Context context) {
        Context appContext = context.getApplicationContext();
        return new PushyToken(appContext);
    }


    @Override
    public void onAppReady() {
        synchronized (mAppContext) {
            if (sToken == null) {
                if(BuildConfig.DEBUG) Log.i(LOGTAG, "App initialized => asking for new token");
                refreshToken();
            } else {
                // Except for first run, this should be the case.
                if(BuildConfig.DEBUG) Log.i(LOGTAG, "App initialized => publishing existing token ("+sToken+")");
                sendTokenToJS();
            }
        }
    }

    protected void refreshToken() {
        try {
            sToken = Pushy.register(mAppContext);
            Log.i(LOGTAG, "Pushy token" + "=" + sToken);
            sendTokenToJS();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    protected void sendTokenToJS() {
        final ReactInstanceManager instanceManager = ((ReactApplication) mAppContext).getReactNativeHost().getReactInstanceManager();
        final ReactContext reactContext = instanceManager.getCurrentReactContext();

        // Note: Cannot assume react-context exists cause this is an async dispatched service.
        if (reactContext != null && reactContext.hasActiveCatalystInstance()) {
            Bundle tokenMap = new Bundle();
            tokenMap.putString("deviceToken", sToken);
            mJsIOHelper.sendEventToJS(TOKEN_RECEIVED_EVENT_NAME, tokenMap, reactContext);
        }
    }
}
