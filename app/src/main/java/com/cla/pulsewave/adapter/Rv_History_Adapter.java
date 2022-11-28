package com.cla.pulsewave.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cla.pulsewave.R;
import com.cla.pulsewave.datatype.HistoryData;
import com.google.android.material.divider.MaterialDivider;

import java.util.List;

public class Rv_History_Adapter extends RecyclerView.Adapter<Rv_History_Adapter.CustomViewHolder> {
    private List<HistoryData> arrayList;

    public Rv_History_Adapter(List<HistoryData> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public Rv_History_Adapter.CustomViewHolder onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_list_item, parent, false);
        Rv_History_Adapter.CustomViewHolder holder = new Rv_History_Adapter.CustomViewHolder(view);
        return holder;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull Rv_History_Adapter.CustomViewHolder holder, int position) {
        holder.tv_User_State.setText(arrayList.get(position).getUserState());
        holder.tv_AVG_BPM.setText(arrayList.get(position).getAvgBPM());
        holder.tv_date.setText(arrayList.get(position).getDate());
        switch (arrayList.get(position).getUserState()){
            case "Good":
                holder.divider_UserState.setDividerColor(Color.GREEN);
                break;
            case "Bad":
                holder.divider_UserState.setDividerColor(Color.RED);
                break;
            case "So":
                holder.divider_UserState.setDividerColor(Color.YELLOW);
                break;

        }
    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_User_State;
        private TextView tv_AVG_BPM;
        private TextView tv_date;
        private MaterialDivider divider_UserState;

        public CustomViewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
            super(itemView);
            this.tv_User_State = (TextView) itemView.findViewById(R.id.tv_User_State);
            this.tv_AVG_BPM = (TextView) itemView.findViewById(R.id.tv_AVG_BPM);
            this.tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            this.divider_UserState = (MaterialDivider)itemView.findViewById(R.id.divider_UserSate);
        }
    }
}