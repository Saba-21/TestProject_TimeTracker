package com.example.pc.testproject_timetracker;

/**
 * Created by PC on 17-Feb-18.abc
 */

class Data {

    private String Task;

    private String Description;

    private String Time;

    Data(String task, String description, String time) {
        Task = task;
        Description = description;
        Time = time;
    }

    String getTask() {
        return Task;
    }


    String getDescription() {
        return Description;
    }

    String getTime() {
        return Time;
    }

}
