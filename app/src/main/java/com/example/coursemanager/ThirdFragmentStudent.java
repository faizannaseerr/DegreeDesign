package com.example.coursemanager;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.coursemanager.databinding.FragmentFirstStudentBinding;
import com.example.coursemanager.databinding.FragmentThirdStudentBinding;
import com.example.coursemanager.ui.login.Course;
import com.example.coursemanager.ui.login.Schedule;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Console;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class ThirdFragmentStudent extends Fragment {
    private FragmentThirdStudentBinding binding;


/*
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    } */


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentThirdStudentBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ThirdFragmentStudent.this)
                        .navigate(R.id.action_thirdFragmentStudent_to_FirstFragment);
            }
        });

        ArrayList<Course> CoursesWanted = new ArrayList<Course>();
        ArrayList<Course> CoursesTaken = new ArrayList<Course>();

        DatabaseReference dReferenceTaken = FirebaseDatabase
                .getInstance("https://course-manager-b07-default-rtdb.firebaseio.com/")
                .getReference().child("students")
                .child(((MainActivityStudent) getActivity()).getUsername())
                .child("coursesTaken");

        dReferenceTaken.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Course TakenCourse = new Course();
                    TakenCourse.setCourseCode(snapshot.getKey());
                    CoursesTaken.add(TakenCourse);
                    // Don't need to add more details for Courses Taken I believe, other than course codes
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference dReferenceWanted = FirebaseDatabase
                .getInstance("https://course-manager-b07-default-rtdb.firebaseio.com/")
                .getReference();
        dReferenceWanted.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                DataSnapshot data = dataSnapshot.child("students")
                        .child(((MainActivityStudent) getActivity()).getUsername())
                        .child("coursesWanted");
                for (DataSnapshot snapshot: data.getChildren()){
                    Course WantedCourse = new Course();
                    WantedCourse.setCourseCode(snapshot.getKey());
                    DataSnapshot dataSnapshot1 = dataSnapshot.child("Courses")
                            .child(snapshot.getKey());
                    WantedCourse.setCourseName(dataSnapshot1.child("courseName").getValue(String.class));
                    WantedCourse.setSummer(dataSnapshot1.child("summer").getValue(boolean.class));
                    WantedCourse.setWinter(dataSnapshot1.child("winter").getValue(boolean.class));
                    WantedCourse.setFall(dataSnapshot1.child("fall").getValue(boolean.class));
                    DataSnapshot dataSnapshot2 = dataSnapshot.child("Courses")
                            .child(snapshot.getKey()).child("prereqs");
                    for (DataSnapshot snapshot2: dataSnapshot2.getChildren()){
                        WantedCourse.addPrereqs(snapshot2.getValue(String.class));
                    }
                    CoursesWanted.add(WantedCourse);
                }

                // Keep ur code to display inside this block ***


                ArrayList<String> print = new ArrayList<>();
                for (int i = 0; i < CoursesWanted.size(); i++){
                    print.add(CoursesWanted.get(i).getCourseCode());
                }
                Log.d("Debug",CoursesWanted.toString());
                Log.d("Debug",CoursesTaken.toString());

                /* Courses Taken isn't showing up on the screen ugh */

                Schedule useless = new Schedule ();
                ArrayList<Course> TotalCourses = new ArrayList<Course>();

                TotalCourses = useless.CreateTotalCoursesArray(CoursesTaken, CoursesWanted, TotalCourses);
                ArrayList<Schedule> DegreeSchedule = useless.CreateSchedule(CoursesTaken, TotalCourses);

                /* Last: with the schedule list use year and semester field to display courses -
                     degree year 1 means 2022 and so on

                     Display this using a table I guess, or list view
                     (list view seems nicer & cleaner tbh)
                 */


                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, print);
                binding.listview.setAdapter(adapter);

                // Currently using this to test schedule algorithm & generation of Courses Wanted & Taken Arrays ^^


                //end of block***
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

        });

    }
}

// Remove back arrow on header