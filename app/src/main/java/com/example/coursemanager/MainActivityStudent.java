package com.example.coursemanager;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.coursemanager.databinding.ActivityMainStudentBinding;

public class MainActivityStudent extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainStudentBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainStudentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public String getUsername() {
        String name = "";
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            name = bundle.getString("username");
        }
        return name;
    }

    public String getTableName(){
        String name = "";
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            name = bundle.getString("Table Name");
        }
        return name;
    }
}