package com.cla.pulsewave.view.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cla.pulsewave.adapter.Rv_History_Adapter;
import com.cla.pulsewave.databinding.FragmentHistoryBinding;
import com.cla.pulsewave.dataType.HistoryData;

import java.util.ArrayList;

public class History extends Fragment {

    private FragmentHistoryBinding binding;

    //RecyclerView
    private Rv_History_Adapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<HistoryData> arrayList;

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

//        getItemList();
        adapter = new Rv_History_Adapter(arrayList);
        binding.rvHistory.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        return view;
    }

//    private void getItemList(){
//        arrayList = new ArrayList<>();
//        arrayList.add(new HistoryData("12/1","Good","80"));
//        arrayList.add(new HistoryData("12/2","Bad","180"));
//        arrayList.add(new HistoryData("12/3","Good","80"));
//        arrayList.add(new HistoryData("12/4","Bad","280"));
//        arrayList.add(new HistoryData("12/5","Good","80"));
//        arrayList.add(new HistoryData("12/6","Bad","380"));
//        arrayList.add(new HistoryData("12/7","Good","80"));
//        arrayList.add(new HistoryData("12/8","Good","80"));
//        arrayList.add(new HistoryData("12/9","Bad","480"));
//        arrayList.add(new HistoryData("12/10","Good","80"));
//        arrayList.add(new HistoryData("12/11","Bad","580"));
//        arrayList.add(new HistoryData("12/12","Good","80"));
//    }
}