package com.cla.pulsewave.view.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cla.pulsewave.adapter.Rv_History_Adapter;
import com.cla.pulsewave.database.AppDatabase;
import com.cla.pulsewave.databinding.FragmentHistoryBinding;
import com.cla.pulsewave.datatype.HistoryData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class History extends Fragment {

    private FragmentHistoryBinding binding;

    //RecyclerView
    private Rv_History_Adapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private List<HistoryData> arrayList = new ArrayList<>();

    //DataBase
    AppDatabase database;


    public static History newInstance() {
        History history = new History();
        return history;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        ///RecyclerView 초기화
        linearLayoutManager = new LinearLayoutManager(requireContext());
        binding.rvHistory.setLayoutManager(linearLayoutManager);

        initDataBase();


        return view;
    }

    private void initDataBase(){
        database = AppDatabase.getInstance(getContext());
        for(int i = 1; i<20; ++i){
            if(i%2==0){
                HistoryData data = new HistoryData();
                data.setDate(getDate());
                data.setAvgBPM("88");
                data.setUserState("Good");
                database.historyDao().insert(data);
            }else{
                HistoryData data = new HistoryData();
                data.setDate(getDate());
                data.setAvgBPM("130");
                data.setUserState("Bad");
                database.historyDao().insert(data);
            }
        }
        arrayList = database.historyDao().getAll();
        adapter = new Rv_History_Adapter(arrayList);
        binding.rvHistory.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private String getDate(){
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd\nhh:mm");
        return sdf.format(date);
    }
}