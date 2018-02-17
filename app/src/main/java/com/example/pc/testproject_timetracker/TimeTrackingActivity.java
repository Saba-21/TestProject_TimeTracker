package com.example.pc.testproject_timetracker;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

public class TimeTrackingActivity extends AppCompatActivity{

    private Dialog firstDialog, secondDialog;
    private EditText taskText, descriptionText, timer;
    private String task, description, time;
    private RVAdapter rvAdapter;
    private DBHelper dbHelper;

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

        taskText = firstDialog.findViewById(R.id.task_text);
        descriptionText = firstDialog.findViewById(R.id.description_text);
        timer = secondDialog.findViewById(R.id.timer);
    }

    public void newTaskButton(View view){
        firstDialog.show();
    }

    public void startButton(View view) {
        firstDialog.dismiss();
        task = taskText.getText().toString();
        description = descriptionText.getText().toString();
        secondDialog.show();
    }

    public void finishButton(View view) {
        time = timer.getText().toString();
        if(empty(task) && empty(description) && empty(time)) {
            dbHelper.addData(new DataModel(task, description, time));
            rvAdapter.addItem(dbHelper.getData());
        }
        secondDialog.dismiss();
    }

    private boolean empty(String text) {
        return text.trim().length() > 0;
    }
}
