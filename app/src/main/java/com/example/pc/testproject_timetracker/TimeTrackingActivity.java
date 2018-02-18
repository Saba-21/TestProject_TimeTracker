package com.example.pc.testproject_timetracker;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.Toast;

public class TimeTrackingActivity extends AppCompatActivity{

    private static final String ACTION_GET_TIME_FROM_SERVICE = "TimeToActivity";
    private static final String ACTION_TELL_SERVICE_TO_STOP = "StopTheService";
    private static final String KEY_TIME_TO_ACTIVITY = "TimeFromIntent";
    private static final String ACTION_TELL_SERVICE_TO_START_TIMER = "StartTheServiceTimer";
    private Dialog firstDialog, secondDialog;
    private EditText taskView, descriptionView;
    private TextView timeView;
    private String task, description;
    private RVAdapter rvAdapter;
    private DBHelper dbHelper;
    private int totalSeconds = 0;
    private boolean run;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_tracking);
        initView();
        setupDialog();

        dbHelper = new DBHelper(this);

        RecyclerView recyclerView;
        recyclerView = findViewById(R.id.recycler_view_id);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        rvAdapter = new RVAdapter(dbHelper.getData());
        recyclerView.setAdapter(rvAdapter);

        IntentFilter intentFilter = new IntentFilter(ACTION_GET_TIME_FROM_SERVICE);
        registerReceiver(activityReceiver, intentFilter);

    }

    BroadcastReceiver activityReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_GET_TIME_FROM_SERVICE))
                Toast.makeText(context, intent.getStringExtra(KEY_TIME_TO_ACTIVITY), Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(activityReceiver);
    }

    private void setupDialog(){
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels / 2;
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

    private void initView(){
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

    public void newTaskButton(View view){
        startService(new Intent(this,TimerService.class));
        firstDialog.show();
    }

    public void startButton(View view) {
        saveDataResetDialog();
        if (empty(task) && empty(description)) {
            firstDialog.cancel();
            secondDialog.show();
            run = true;
            timer();
            sendStartBroadcast();
        }
    }

    public void finishButton(View view) {
        sendStopBroadcast();
        run = false;
        String time = timeView.getText().toString();
        totalSeconds = 0;
        dbHelper.addData(new DataModel(task, description, time));
        rvAdapter.addItem(dbHelper.getData());
        secondDialog.cancel();
    }

    private void saveDataResetDialog(){
        task = taskView.getText().toString();
        taskView.getText().clear();
        description = descriptionView.getText().toString();
        descriptionView.getText().clear();
    }

    private void sendStartBroadcast(){
        Intent intent = new Intent();
        intent.setAction(ACTION_TELL_SERVICE_TO_START_TIMER);
        sendBroadcast(intent);
    }

    private void sendStopBroadcast(){
        Intent intent = new Intent();
        intent.setAction(ACTION_TELL_SERVICE_TO_STOP);
        sendBroadcast(intent);
    }

    private void timer(){
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (run) {
                    int hours = totalSeconds / 3600;
                    int minutes = (totalSeconds % 3600) / 60;
                    int seconds = totalSeconds % 60;
                    String timeText = String.format("%d:%02d:%02d", hours, minutes, seconds);
                    timeView.setText(timeText);
                    totalSeconds++;
                    handler.postDelayed(this, 1000);
                }
            }
        });
    }

    private boolean empty(String text) {
        return text.trim().length() > 0;
    }
}
