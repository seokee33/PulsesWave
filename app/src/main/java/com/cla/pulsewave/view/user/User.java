package com.cla.pulsewave.view.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.cla.pulsewave.databinding.FragmentUserBinding;


public class User extends Fragment {
    private FragmentUserBinding binding;

    public static User newInstance() {
        User user = new User();
        return user;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        return view;
    }


}