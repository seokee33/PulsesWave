package com.cla.pulsewave.view.history;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cla.pulsewave.R;
import com.cla.pulsewave.databinding.FragmentHistoryBinding;
import com.cla.pulsewave.view.check.Check;

public class History extends Fragment {

    private FragmentHistoryBinding binding;
    public static History newInstance(){
        History history = new History();
        return history;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }
}