package com.example.coursemanager;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.coursemanager.ui.login.Course;
import com.example.coursemanager.ui.login.Schedule;

import java.util.ArrayList;

public class ThirdFragmentStudent extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ThirdFragmentStudent() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ThirdFragmentStudent.
     */
    // TODO: Rename and change types and number of parameters
    public static ThirdFragmentStudent newInstance(String param1, String param2) {
        ThirdFragmentStudent fragment = new ThirdFragmentStudent();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_third_student, container, false);
    }

    // Edit junk above
    // Needs to get courses taken and courses wanted from previous fragment here, converting them to course lists


    // Then:

    Schedule useless = new Schedule ();
    ArrayList<Course> TotalCourses = new ArrayList<Course>();
    TotalCourses = useless.CreateTotalCoursesArray(CoursesTaken, CoursesWanted, TotalCourses);
    ArrayList<Schedule> DegreeSchedule = useless.CreateSchedule(CoursesTaken, TotalCourses);

    /* Last: with the schedule list use year and semester field to display courses -
             field year + 2021 for fall, year + 2022 for winter & summer
             e.g. 2nd year winter course means, Winter 2024

             Display this using a table I guess

     */


}