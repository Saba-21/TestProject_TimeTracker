package com.example.pc.testproject_timetracker;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by PC on 17-Feb-18.abc
 */

class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder>{


    private List<DataModel> listData;

    RVAdapter(List<DataModel> listData) {
        this.listData = listData;
    }

    void addItem(List<DataModel> listData) {
        this.listData = listData;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position % 2 == 0)
            holder.itemLayout.setBackgroundResource(R.color.itemGrey);
        holder.Task.setText(listData.get(position).getTask());
        holder.Description.setText(listData.get(position).getDescription());
        holder.Time.setText(listData.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView Task, Description, Time;
        private LinearLayout itemLayout;

        ViewHolder(View itemView) {
            super(itemView);
            Task = itemView.findViewById(R.id.task);
            Description = itemView.findViewById(R.id.description);
            Time = itemView.findViewById(R.id.time);
            itemLayout = itemView.findViewById(R.id.item_layout);
        }
    }

}
