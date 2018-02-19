package com.example.pc.testproject_timetracker;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;

public class TimerService extends Service {

    private static final String ACTION_GET_TIME_FROM_SERVICE = "TimeToActivity";
    private static final String ACTION_TELL_SERVICE_TO_STOP = "StopTheService";
    private static final String ACTION_ASK_SERVICE_TIME = "AskServiceTime";
    private static final String KEY_TIME_TO_ACTIVITY = "TimeFromIntent";
    private int totalSeconds;

    BroadcastReceiver serviceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction()!=null) {
                if (intent.getAction().equals(ACTION_ASK_SERVICE_TIME)) {       //listen broadcast to send timer data
                    Intent broadcastIntent = new Intent();
                    broadcastIntent.putExtra(KEY_TIME_TO_ACTIVITY, totalSeconds);
                    broadcastIntent.setAction(ACTION_GET_TIME_FROM_SERVICE);
                    sendBroadcast(broadcastIntent);
                }
                if (intent.getAction().equals(ACTION_TELL_SERVICE_TO_STOP)) {       //listen broadcast to call stop
                    stopSelf();
                }
            }
        }
    };

    private void timer(){
        totalSeconds = 0;
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                totalSeconds++;
                handler.postDelayed(this, 1000);
            }
        });
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startForeground(1, new Notification());     //prevent service from being killed after activity destruction
        timer();
        IntentFilter intentFilter = new IntentFilter(ACTION_TELL_SERVICE_TO_STOP);
        registerReceiver(serviceReceiver, intentFilter);
        intentFilter = new IntentFilter(ACTION_ASK_SERVICE_TIME);
        registerReceiver(serviceReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(serviceReceiver);
    }
}

