package com.example.pc.testproject_timetracker;

/**
 * Created by PC on 17-Feb-18.abc
 */

class DataModel {

    private String Task;

    private String Description;

    private String Time;

    DataModel(){}

    DataModel(String task, String description, String time) {
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

    void setTask(String task) {
        Task = task;
    }

    void setDescription(String description) {
        Description = description;
    }

    void setTime(String time) {
        Time = time;
    }

}
