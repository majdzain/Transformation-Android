package com.transformation.zezoo;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

public class USSDService extends AccessibilityService {
    public static String TAG = "USSDService";
    String type = "";
    boolean isAutomaticRefresh = false;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.d(TAG, "onAccessibilityEvent");
        String text = event.getText().toString();
        if (event.getClassName().equals("android.app.AlertDialog")) {
            performGlobalAction(GLOBAL_ACTION_BACK);
            Log.d(TAG, text);
            Intent intent = new Intent("com.times.ussd.action.REFRESH");
            intent.putExtra("message", text);
            try {
                Intent broadCastIntent = new Intent();
                broadCastIntent.setAction(BasicActivity.BROADCAST_ACTION);
                broadCastIntent.putExtra("text", text);
                broadCastIntent.putExtra("type", type);
                broadCastIntent.putExtra("isAutomaticRefresh",isAutomaticRefresh);
                sendBroadcast(broadCastIntent);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    @Override
    public void onInterrupt() {
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.d(TAG, "onServiceConnected");
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.flags = AccessibilityServiceInfo.DEFAULT;
        info.packageNames = new String[]{"com.android.phone"};
        info.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        setServiceInfo(info);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            if (intent.getExtras().containsKey("type")) {
                type = intent.getStringExtra("type");
            }
        } catch (Exception e) {

        }
        try {
            if (intent.getExtras().containsKey("isAutomaticRefresh")) {
                isAutomaticRefresh = intent.getBooleanExtra("isAutomaticRefresh",false);
            }
        } catch (Exception e) {

        }
        return START_STICKY;
    }

}
