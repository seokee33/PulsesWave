package com.cla.pulsewave.view;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.cla.pulsewave.R;
import com.cla.pulsewave.databinding.ActivityMainBinding;
import com.cla.pulsewave.view.check.Check;
import com.cla.pulsewave.view.history.History;
import com.cla.pulsewave.view.user.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    //바텀네비&각 페이지
    private FragmentManager fm;
    private FragmentTransaction ft;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //권한 체크( >= marshMellow)
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            showPermissionDialog();
        }

        binding.bottomNaviMain.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_check:
                        setFrag(0);
                        break;
                    case R.id.menu_history:

                        setFrag(1);
                        break;
                    case R.id.menu_User:
                        setFrag(2);
                        break;
                }
                return true;
            }
        });

        setFrag(0);
    }

    private void setFrag(int i) {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        switch (i) {
            case 0:
                ft.replace(binding.fragMain.getId(), Check.newInstance()).commit();
                break;
            case 1:
                ft.replace(binding.fragMain.getId(), History.newInstance()).commit();
                break;
            case 2:
                ft.replace(binding.fragMain.getId(), User.newInstance()).commit();
                break;
        }
    }


    //권한 체크( >= marshMellow)
    private void showPermissionDialog() {
        if (!MainActivity.checkPermission(this)) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                    100);
        }
    }

    //권한 체크( >= marshMellow)
    public static boolean checkPermission(final Context context) {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }
}