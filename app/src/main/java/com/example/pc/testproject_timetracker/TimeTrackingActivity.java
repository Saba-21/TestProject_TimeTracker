package com.example.pc.testproject_timetracker;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

public class TimeTrackingActivity extends AppCompatActivity{
    private static final String MAIL_INTENT_TYPE = "message/rfc822";
    private static final String MAIL_SAMPLE_RECIPIENT = "recipient@example.com";
    private static final String MAIL_SAMPLE_TITLE = "Tracked time for your tasks";
    private static final String MAIL_ACTIVITY_NAME = "Send mail...";
    private static final String ACTION_GET_TIME_FROM_SERVICE = "TimeToActivity";
    private static final String ACTION_TELL_SERVICE_TO_STOP = "StopTheService";
    private static final String ACTION_ASK_SERVICE_TIME = "AskServiceTime";
    private static final String KEY_TIME_TO_ACTIVITY = "TimeFromIntent";
    private static final String KEY_TIMER = "KeyTimer", KEY_TASK = "KeyTask", KEY_DESC = "KeyDesc";
    private static final String KEY_SHARED_PREFERENCE = "SharedPreferenceKey";
    private Dialog firstDialog, secondDialog;
    private EditText taskView, descriptionView;
    private TextView timeView;
    private String task, description;
    private RVAdapter rvAdapter;
    private DBHelper dbHelper;
    private int totalSeconds = 0;
    private boolean running = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_tracking);
        initView();
        setupDialog();
        setupRecyclerView();
        getTimerState();                           //timer state and input data is saved in shared preferences

        if (running) {                             //activity gets timer data from service only after its destruction while timer was running
            sendTimeBroadcast();                   //service will listen it and send broadcast with timer data
            timer();                               //run timer
            secondDialog.show();
        }
        IntentFilter intentFilter = new IntentFilter(ACTION_GET_TIME_FROM_SERVICE);     //registering broadcast that service will send
        registerReceiver(activityReceiver, intentFilter);

    }

    BroadcastReceiver activityReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction()!=null)
                if (intent.getAction().equals(ACTION_GET_TIME_FROM_SERVICE))                 //listening broadcast that service will send with its timer data
                    totalSeconds = intent.getIntExtra(KEY_TIME_TO_ACTIVITY, 0);
        }
    };

    @Override
    protected void onStop() {                                       //save timer state and input data when activity is invisible
        super.onStop();
        saveTimerState();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(activityReceiver);
    }

    private void setupRecyclerView(){
        dbHelper = new DBHelper(this);
        RecyclerView recyclerView;
        recyclerView = findViewById(R.id.recycler_view_id);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        rvAdapter = new RVAdapter(dbHelper.getData());
        recyclerView.setAdapter(rvAdapter);
    }

    private void setupDialog(){                                     //dialog will space 2/3 of screen size in width and half in height
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width =(metrics.widthPixels / 3)*2;
        int height = metrics.heightPixels / 2;

        WindowManager.LayoutParams params;

        Window dialogWindow = firstDialog.getWindow();
        if (dialogWindow != null) {
            params = dialogWindow.getAttributes();
            params.width = width;
            params.height = height;
            dialogWindow.setAttributes(params);
        }
        dialogWindow = secondDialog.getWindow();
        if (dialogWindow != null) {
            params = dialogWindow.getAttributes();
            params.width = width;
            params.height = height;
            dialogWindow.setAttributes(params);
        }
    }

    private void initView(){                                         //dialog can't be canceled until user clicks finish button
        firstDialog = new Dialog(this);
        firstDialog.setContentView(R.layout.start_dialog_layout);
        firstDialog.setCancelable(false);
        firstDialog.setCanceledOnTouchOutside(false);

        secondDialog = new Dialog(this);
        secondDialog.setContentView(R.layout.timer_dialog_layout);
        secondDialog.setCancelable(false);
        secondDialog.setCanceledOnTouchOutside(false);

        taskView = firstDialog.findViewById(R.id.task_text);
        descriptionView = firstDialog.findViewById(R.id.description_text);
        timeView = secondDialog.findViewById(R.id.timer);
    }

    public void newTaskButton(View view){       //first dialog pop-ups
        firstDialog.show();
    }

    public void share(View view){                                       //sharing via mail
        StringBuilder stringBuilder = new StringBuilder();
        for (DataModel data : dbHelper.getData()){
            stringBuilder.append(String.format("%s - %s - %s\n",data.getTask(),data.getDescription(),data.getTime()));
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType(MAIL_INTENT_TYPE);
        intent.putExtra(Intent.EXTRA_EMAIL  , new String[]{MAIL_SAMPLE_RECIPIENT});
        intent.putExtra(Intent.EXTRA_SUBJECT, MAIL_SAMPLE_TITLE);
        intent.putExtra(Intent.EXTRA_TEXT   , stringBuilder.toString());
        startActivity(Intent.createChooser(intent,MAIL_ACTIVITY_NAME ));
    }

    public void startButton(View view) {
        getViewData();                                  //get input data
        if (!task.isEmpty() && !description.isEmpty()) {
            firstDialog.cancel();
            secondDialog.show();
            running = true;                          //timer state
            timer();                                 //start timer
            startTimerInBackground();                //start service
        }
    }

    public void finishButton(View view) {
        sendStopBroadcast();                                             //broadcast that service listens and will call stop after receiving
        running = false;
        String time = timeView.getText().toString();
        dbHelper.addData(new DataModel(task, description, time));       //save data in database
        rvAdapter.addItem(dbHelper.getData());                             //add item to list
        totalSeconds = 0;                                               //reset timer dialog
        secondDialog.cancel();
        saveTimerState();
    }

    public void startTimerInBackground(){                               //starting service
        Intent intent = new Intent(this, TimerService.class);
        startService(intent);
    }

    public void saveTimerState(){                                       //saving timer state and input text
        SharedPreferences.Editor editor = getSharedPreferences(KEY_SHARED_PREFERENCE, MODE_PRIVATE).edit();
        editor.putBoolean(KEY_TIMER, running);
        editor.putString(KEY_TASK,task);
        editor.putString(KEY_DESC,description);
        editor.apply();
    }

    public void getTimerState(){                                        //getting timer state and input text
        SharedPreferences prefs = getSharedPreferences(KEY_SHARED_PREFERENCE, MODE_PRIVATE);
        running = prefs.getBoolean(KEY_TIMER, false);
        task = prefs.getString(KEY_TASK,"");
        description = prefs.getString(KEY_DESC,"");
    }

    private void sendStopBroadcast(){                                   //service will send back timer data
        Intent intent = new Intent();
        intent.setAction(ACTION_TELL_SERVICE_TO_STOP);
        sendBroadcast(intent);
    }

    private void sendTimeBroadcast(){                                    //service will stop itself
        Intent intent = new Intent();
        intent.setAction(ACTION_ASK_SERVICE_TIME);
        sendBroadcast(intent);
    }

    private void timer(){                                               // handler will execute incrementing value on every one second
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (running) {
                    int hours = totalSeconds / 3600;
                    int minutes = (totalSeconds % 3600) / 60;
                    int seconds = totalSeconds % 60;
                    String timeText = String.format(Locale.ENGLISH,"%d:%02d:%02d", hours, minutes, seconds);
                    timeView.setText(timeText);
                    totalSeconds++;
                    handler.postDelayed(this, 1000);
                }
            }
        });
    }

    private void getViewData(){                                         //resets dialog window
        task = taskView.getText().toString();
        taskView.getText().clear();
        description = descriptionView.getText().toString();
        descriptionView.getText().clear();
    }
}
