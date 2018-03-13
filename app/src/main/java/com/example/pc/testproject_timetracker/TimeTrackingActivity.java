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
import android.widget.TextView;

public class TimeTrackingActivity extends AppCompatActivity implements TimerView{
    private Dialog firstDialog, secondDialog;
    private EditText taskView, descriptionView;
    private TextView timeView;
    private PresenterImpl presenter;
    private RVAdapter rvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_tracking);

        setupDialog();

        taskView = firstDialog.findViewById(R.id.task_text);
        descriptionView = firstDialog.findViewById(R.id.description_text);
        timeView = secondDialog.findViewById(R.id.timer);
        RecyclerView recyclerView = findViewById(R.id.recycler_view_id);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        presenter = new PresenterImpl(this);

        rvAdapter = new RVAdapter(presenter);

        recyclerView.setAdapter(rvAdapter);

    }

    private void setupDialog(){

        firstDialog = new Dialog(this);
        firstDialog.setContentView(R.layout.start_dialog_layout);

        secondDialog = new Dialog(this);
        secondDialog.setContentView(R.layout.timer_dialog_layout);
        secondDialog.setCancelable(false);
        secondDialog.setCanceledOnTouchOutside(false);

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

    public void newTaskButton(View view){
        firstDialog.show();
    }

    public void startButton(View view) {
        presenter.validateInput(taskView.getText().toString(),descriptionView.getText().toString());
    }

    public void finishButton(View view) {
        presenter.saveData(timeView.getText().toString());
        secondDialog.cancel();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void showSecondDialog() {
        firstDialog.cancel();
    }

    @Override
    public void hideFirstDialog() {
        secondDialog.show();
        taskView.getText().clear();
        descriptionView.getText().clear();
    }

    @Override
    public void updateList() {
        rvAdapter.notifyDataSetChanged();
    }

}
