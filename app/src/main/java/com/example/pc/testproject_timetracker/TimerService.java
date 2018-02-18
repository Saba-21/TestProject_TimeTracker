package com.example.pc.testproject_timetracker;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

public class TimerService extends Service {

    private static final String ACTION_GET_TIME_FROM_SERVICE = "TimeToActivity";
    private static final String ACTION_TELL_SERVICE_TO_STOP = "StopTheService";
    private static final String KEY_TIME_TO_ACTIVITY = "TimeFromIntent";
    private static final String ACTION_TELL_SERVICE_TO_START_TIMER = "StartTheServiceTimer";
    private boolean run;
    private int totalSeconds = 0;

    BroadcastReceiver serviceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(ACTION_TELL_SERVICE_TO_START_TIMER)){
                run = true;
                timer();
            }
            if (intent.getAction().equals(ACTION_TELL_SERVICE_TO_STOP)) {
                run = false;
                Intent broadcastIntent = new Intent();
                broadcastIntent.putExtra(KEY_TIME_TO_ACTIVITY, Integer.toString(totalSeconds));
                broadcastIntent.setAction(ACTION_GET_TIME_FROM_SERVICE);
                sendBroadcast(broadcastIntent);
                stopSelf();
            }
        }
    };

    private void timer(){
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (run) {
                    totalSeconds++;
                    handler.postDelayed(this, 1000);
                }
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

        IntentFilter intentFilter = new IntentFilter(ACTION_TELL_SERVICE_TO_STOP);
        registerReceiver(serviceReceiver, intentFilter);
        intentFilter = new IntentFilter(ACTION_TELL_SERVICE_TO_START_TIMER);
        registerReceiver(serviceReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(serviceReceiver);
    }
}

