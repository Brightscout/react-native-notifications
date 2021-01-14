package com.wix.reactnativenotifications.pushy;

public interface IPushyToken {
    /**
     * Handle an event where application is ready; typically used for sending token to JS.
     */
    void onAppReady();

}
