package com.example.coursemanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.coursemanager.databinding.FragmentFirstStudentBinding;
import com.example.coursemanager.ui.login.LoginActivity;

public class FirstFragmentStudent extends Fragment {

    private FragmentFirstStudentBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstStudentBinding.inflate(inflater, container, false);
        MainActivityStudent activity = (MainActivityStudent) getActivity();
        String username = activity.getUsername();
        binding.textviewFirst.setText("Welcome, " + activity.getUsername() + "!");
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragmentStudent.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });

        binding.studentBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}