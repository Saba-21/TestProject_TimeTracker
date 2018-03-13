package com.example.pc.testproject_timetracker;

/**
 * Created by saba on 3/13/2018.abc
 */

public interface Presenter {

    //activity

    void validateInput(String task, String desc);

    void saveData(String time);

    void onDestroy();

    //rvadapter

    int getRowsCount();

    void rowViewAtPosition(int pos, ViewHolderItem item);

}
