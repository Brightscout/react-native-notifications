package com.wix.reactnativenotifications.pushy;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import android.content.Context;
import android.content.Intent;

public class PushyTokenHandlerService extends JobIntentService {

    public static String EXTRA_IS_APP_INIT = "isAppInit";
    public static final int JOB_ID = 2400;

    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, PushyTokenHandlerService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        IPushyToken pushyToken = PushyToken.get(this);
        if (pushyToken == null) {
            return;
        }

        if (intent.getBooleanExtra(EXTRA_IS_APP_INIT, false)) {
            pushyToken.onAppReady();
        }
    }
}
