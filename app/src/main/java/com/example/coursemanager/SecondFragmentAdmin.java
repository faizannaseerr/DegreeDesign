package com.example.coursemanager;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.coursemanager.databinding.FragmentSecondAdminBinding;
import com.example.coursemanager.ui.login.Course;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SecondFragmentAdmin extends Fragment {

    private FragmentSecondAdminBinding binding;
    final Course course = new Course();

    DatabaseReference ref = FirebaseDatabase.getInstance("https://course-manager-b07-default-rtdb.firebaseio.com/").getReference("Courses");

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondAdminBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragmentAdmin.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });

        final EditText CourseCode = binding.CourseCode;


        // Gets the text from the Course Code and sets the field coursecode of our Course object to the input
        TextWatcher course_code_watcher = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                course.setCourseCode(s.toString());
            }
        };

        CourseCode.addTextChangedListener(course_code_watcher);

        CheckBox fallbox = getView().findViewById(R.id.fall);
        fallbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                course.setFall(isChecked);
            }
        });

        CheckBox winterbox = getView().findViewById(R.id.winter);
        winterbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                course.setWinter(isChecked);
            }
        });

        CheckBox summerbox = getView().findViewById(R.id.summer);
        summerbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                course.setSummer(isChecked);
            }
        });


        getView().findViewById(R.id.generate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ref.child(course.getCourseCode()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            String warningMsg = "The course already exists.\nTry going back and editing the course";
                            Toast.makeText(getActivity(), warningMsg, Toast.LENGTH_LONG).show();
                        }
                        else{
                            ref.child(course.getCourseCode()).setValue(course);
                            NavHostFragment.findNavController(SecondFragmentAdmin.this)
                                    .navigate(R.id.action_SecondFragment_to_FirstFragment);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}