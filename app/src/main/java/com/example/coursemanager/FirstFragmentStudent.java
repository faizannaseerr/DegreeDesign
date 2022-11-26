package com.example.coursemanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.coursemanager.databinding.FragmentFirstStudentBinding;
import com.example.coursemanager.ui.login.LoginActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirstFragmentStudent extends Fragment {

    private FragmentFirstStudentBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstStudentBinding.inflate(inflater, container, false);
        MainActivityStudent activity = (MainActivityStudent) getActivity();
        binding.textviewFirst.setText("Welcome, " + activity.getUsername() + "!");
        setTable(binding.takenTable, "Courses Taken");
        setTable(binding.wantedTable,"Courses Wanted");
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

        binding.addButton1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = getActivity().getIntent();
                intent.putExtra("Table Name", "Courses Taken");
                NavHostFragment.findNavController(FirstFragmentStudent.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });

        binding.addButton2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = getActivity().getIntent();
                intent.putExtra("Table Name", "Courses Wanted");
                NavHostFragment.findNavController(FirstFragmentStudent.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    protected void setTable(TableLayout table, String child_name){
        DatabaseReference ref = FirebaseDatabase
                .getInstance("https://course-manager-b07-default-rtdb.firebaseio.com/")
                .getReference().child("students")
                .child(((MainActivityStudent) getActivity()).getUsername())
                .child(child_name);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i = 0;
                for (DataSnapshot datasnapshot : snapshot.getChildren()) {
                    TableRow row = new TableRow(getActivity());
                    TableRow.LayoutParams params =
                            new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                    row.setLayoutParams(params);
                    row.setId(1000+i);
                    TextView course = new TextView(getActivity());
                    course.setId(2000+i);
                    row.addView(course);
                    Button edit = new Button(getActivity());
                    edit.setId(3000+i);
                    row.addView(edit);
                    course.setText(datasnapshot.getKey());
                    edit.setText("Delete");
                    table.addView(row, i);
                    i++;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

}