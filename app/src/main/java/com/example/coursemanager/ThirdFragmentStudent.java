package com.example.coursemanager;

import android.graphics.Color;
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
import android.widget.TableRow;
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
        ArrayList<String> CoursesTaken = new ArrayList<String>();
        ArrayList<Course> CoursesScheduled = new ArrayList<Course>();

        DatabaseReference dReference = FirebaseDatabase
                .getInstance("https://course-manager-b07-default-rtdb.firebaseio.com/")
                .getReference();
        dReference.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                DataSnapshot snap = dataSnapshot.child("students").
                        child(((MainActivityStudent) getActivity()).getUsername())
                        .child("coursesTaken");
                for (DataSnapshot snapshot: snap.getChildren()){
                    CoursesTaken.add(snapshot.getKey());
                    // Don't need to add more details for Courses Taken I believe, other than course codes
                }


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

                // creating list of things to schedule
                while (!CoursesWanted.isEmpty()) {
                    Course a = CoursesWanted.get(0);
                    CoursesScheduled.add(a);
                    for (String b: a.getPrereqs()){
                        Course c = dataSnapshot.child("Courses").child(b).getValue(Course.class);
                        boolean found = false;
                        for (String d: CoursesTaken){
                            if (d.compareTo(b) == 0){
                                found = true;
                            }
                        }
                        for (Course d: CoursesWanted){
                            if (d.getCourseCode().compareTo(b) == 0){
                                found = true;
                            }
                        }
                        for (Course d: CoursesScheduled){
                            if (d.getCourseCode().compareTo(b) == 0){
                                found = true;
                            }
                        }
                        if (!found){
                            CoursesWanted.add(c);
                        }
                    }
                    CoursesWanted.remove(0);
                }

                // creating schedule
                int i = 0;
                int j = 0;
                boolean morethanfive = false;
                ArrayList<Course> presentSemester = new ArrayList<Course>();
                while(!CoursesScheduled.isEmpty()) {
                    for (Course a: CoursesScheduled){
                        boolean takeable = true;
                        if (j == 0 && !a.isFall()){
                            takeable = false;
                        }
                        if (j == 1 && !a.isWinter()){
                            takeable = false;
                        }
                        if (j == 2 && !a.isSummer()){
                            takeable = false;
                        }
                        for (String b: a.getPrereqs()){
                            boolean pretaken = false;
                            for (String c: CoursesTaken){
                                if (b.compareTo(c) == 0){
                                    pretaken = true;
                                }
                            }
                            if (!pretaken) {
                                takeable = false;
                            }
                        }
                        if (takeable) {
                            presentSemester.add(a);
                        }
                    }

                    //adding row to table
                    int k = 0;
                    TableRow row = new TableRow(getActivity());
                    TableRow.LayoutParams params =
                            new TableRow.LayoutParams(TableRow
                                    .LayoutParams.WRAP_CONTENT);
                    row.setLayoutParams(params);
                    row.setId(1000 + (i * 3 + j) * 1000);
                    TextView semester = new TextView(getActivity());
                    semester.setId(1001 + (i * 3 + j) * 1000);
                    semester.setText("");
                    int year = i + 2022;
                    if (j == 0){
                        semester.setText(year + " Fall: ");
                    }
                    if (j == 1){
                        year ++;
                        semester.setText(year + " Winter: ");
                    }
                    if (j == 2){
                        year ++;
                        semester.setText(year + " Summer: ");
                    }
                    row.addView(semester);
                    TextView courseList = new TextView(getActivity());
                    String longName = "";
                    while (!presentSemester.isEmpty()){
                        Course a = presentSemester.get(0);
                        if (k != 0){
                            longName = longName + ", ";
                        }
                        longName = longName + a.getCourseCode();
                        for (Course b: CoursesScheduled){
                            if (b.getCourseCode().compareTo(a.getCourseCode()) == 0){
                                CoursesScheduled.remove(b);
                                break;
                            }
                        }
                        CoursesTaken.add(a.getCourseCode());
                        presentSemester.remove(0);
                        k ++;
                    }
                    if (k > 4){
                        morethanfive = true;
                    }
                    courseList.setText(longName);
                    courseList.setId(1002 + (i * 3 + j) * 1000);
                    row.addView(courseList);
                    binding.table.addView(row, i * 3 + j);
                    j ++;
                    if (j == 3){
                        i ++;
                        j = 0;
                    }
                }

                if (morethanfive){
                    binding.textView.setText("Please consult with your program coordinator");
                    binding.textView2.setText("before taking more than 5 courses in a semester");
                }


                //ArrayList<String> print = new ArrayList<>();
                //for (int i = 0; i < CoursesWanted.size(); i++){
                    //print.add(CoursesWanted.get(i).getCourseCode());
                //}
                //Log.d("Debug",CoursesWanted.toString());
                //Log.d("Debug",CoursesTaken.toString());

                /* Courses Taken isn't showing up on the screen ugh */

                //Schedule useless = new Schedule ();
                //ArrayList<Course> TotalCourses = new ArrayList<Course>();

                //TotalCourses = useless.CreateTotalCoursesArray(CoursesTaken, CoursesWanted, TotalCourses);
                //ArrayList<Schedule> DegreeSchedule = useless.CreateSchedule(CoursesTaken, TotalCourses);

                /* Last: with the schedule list use year and semester field to display courses -
                     degree year 1 means 2022 and so on

                     Display this using a table I guess, or list view
                     (list view seems nicer & cleaner tbh)
                 */


                //ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, print);
                //binding.listview.setAdapter(adapter);

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