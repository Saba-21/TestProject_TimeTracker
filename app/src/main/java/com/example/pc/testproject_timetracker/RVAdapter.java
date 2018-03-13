package com.example.pc.testproject_timetracker;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by PC on 17-Feb-18.abc
 */

class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder>{

    RVAdapter(PresenterImpl presenterImp) {
        this.presenterImp = presenterImp;
    }

    private PresenterImpl presenterImp;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        presenterImp.rowViewAtPosition(position, holder);
    }

    @Override
    public int getItemCount() {
        return presenterImp.getRowsCount();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements ViewHolderItem {

        private TextView Task, Description, Time;
        private LinearLayout itemLayout;

        ViewHolder(View itemView) {
            super(itemView);
            Task = itemView.findViewById(R.id.task);
            Description = itemView.findViewById(R.id.description);
            Time = itemView.findViewById(R.id.time);
            itemLayout = itemView.findViewById(R.id.item_layout);
        }

        @Override
        public void setTaskText(String task) {
            Task.setText(task);
        }

        @Override
        public void setDescText(String desc) {
            Description.setText(desc);
        }

        @Override
        public void setTimeText(String time) {
            Time.setText(time);
        }

        @Override
        public void setBackColor( int pos) {
            if (pos % 2 == 0)
                itemLayout.setBackgroundResource(R.color.itemGrey);
        }
    }

}
