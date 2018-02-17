package com.example.pc.testproject_timetracker;

import android.app.Dialog;
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

public class TimeTrackingActivity extends AppCompatActivity{

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
        firstDialog.show();
    }

    public void startButton(View view) {
        task = taskView.getText().toString();
        taskView.getText().clear();
        description = descriptionView.getText().toString();
        descriptionView.getText().clear();
        if (empty(task) && empty(description)) {
            firstDialog.cancel();
            secondDialog.show();
            run = true;
            timer();
        }
    }

    public void finishButton(View view) {
        run = false;
        String time = timeView.getText().toString();
        totalSeconds = 0;
        dbHelper.addData(new DataModel(task, description, time));
        rvAdapter.addItem(dbHelper.getData());
        secondDialog.cancel();
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
