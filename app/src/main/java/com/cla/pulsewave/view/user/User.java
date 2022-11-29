package com.cla.pulsewave.view.user;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.cla.pulsewave.databinding.FragmentUserBinding;
import com.cla.pulsewave.dialog.BluetoothSettingDialog;
import com.cla.pulsewave.view.login.Login;
import com.google.firebase.auth.FirebaseAuth;


public class User extends Fragment {
    private FragmentUserBinding binding;
    private FirebaseAuth mAuth;

    public static User newInstance() {
        User user = new User();
        return user;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        mAuth = FirebaseAuth.getInstance();

        binding.layoutUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        binding.tvBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BluetoothSettingDialog dialog = new BluetoothSettingDialog(getActivity());
                dialog.show();
            }
        });

        binding.tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
                Intent intent = new Intent(getContext(), Login.class);
                getActivity().finish();
                startActivity(intent);
            }
        });
        return view;
    }


    private void logout() {
        mAuth.signOut();
    }


}