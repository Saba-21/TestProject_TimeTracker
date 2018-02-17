package com.example.pc.testproject_timetracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TimeTrackingActivity extends AppCompatActivity {


    private List<Data> listData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_tracking);

        RecyclerView recyclerView;
        RVAdapter rvAdapter;

        listData.add(new Data("a", "b", "c"));

        recyclerView = findViewById(R.id.recycler_view_id);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(llm);
        rvAdapter = new RVAdapter(listData);
        recyclerView.setAdapter(rvAdapter);

        rvAdapter.addItem(new Data("aa", "bb", "cc"));

    }
}
