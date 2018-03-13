package com.example.pc.testproject_timetracker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by saba on 3/13/2018.abc
 */

public class PresenterImpl implements Presenter {

    private TimerView timerView;
    private List<DataModel> listData;

    private String task, desc;


    PresenterImpl(TimerView timerView) {
        this.timerView = timerView;
        listData = new ArrayList<>();
    }

    @Override
    public int getRowsCount() {
        return listData.size();
    }

    @Override
    public void rowViewAtPosition(int pos, ViewHolderItem item) {
        item.setTaskText(listData.get(pos).getTask());
        item.setDescText(listData.get(0).getDescription());
        item.setTimeText(listData.get(pos).getTime());
        item.setBackColor(pos);
    }

    @Override
    public void validateInput(String task, String desc) {
        if (!task.isEmpty() && !desc.isEmpty()){
            this.task = task;
            this.desc = desc;
            timerView.showSecondDialog();
            timerView.hideFirstDialog();
        }
    }

    @Override
    public void saveData(String time) {
        listData.add(new DataModel(task, desc, time));
        timerView.updateList();
    }

    @Override
    public void onDestroy() {
        timerView = null;
    }
}
