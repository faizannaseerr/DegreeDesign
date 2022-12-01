package com.example.coursemanager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.coursemanager.databinding.FragmentSecondAdminBinding;
import com.example.coursemanager.databinding.FragmentThirdAdminBinding;
import com.example.coursemanager.ui.login.Course;
import com.example.coursemanager.ui.login.LoginActivity;
import com.example.coursemanager.ui.login.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ThirdFragmentAdmin extends Fragment {
    private FragmentThirdAdminBinding binding;
    Course course = new Course();
    static int i;
    static ArrayList<String> prereqList = new ArrayList<>();
    static String req;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentThirdAdminBinding.inflate(inflater, container, false);


        MainActivityAdmin activity = (MainActivityAdmin) getActivity();
        binding.editingCourse.setText("Edit " + activity.getCourseCode());

        setPrereqTable(binding.addTable, activity.getCourseCode());

        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.editBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ThirdFragmentAdmin.this)
                        .navigate(R.id.action_thirdFragmentAdmin_to_FirstFragment);
            }
        });

        MainActivityAdmin activity = (MainActivityAdmin) getActivity();
        String editCourseCode = activity.getCourseCode();

        final EditText CourseName = binding.editcourseName;
        final EditText CourseCode = binding.editCourseCode;
        final EditText Prereq = binding.editprereq;

        DatabaseReference courseRef = FirebaseDatabase
                .getInstance("https://course-manager-b07-default-rtdb.firebaseio.com/")
                .getReference().child("Courses");

        courseRef.child(editCourseCode).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("courseName").exists()){
                    CourseName.setText(snapshot.child("courseName").getValue().toString());
                }
                else{
                    CourseName.setText("");
                }


                CourseCode.setText(snapshot.child("courseCode").getValue().toString());


                CheckBox editfallbox = getView().findViewById(R.id.editfall);
                editfallbox.setChecked(snapshot.child("fall").getValue().toString().compareTo("true") == 0);

                CheckBox editwinterbox = getView().findViewById(R.id.editwinter);
                editwinterbox.setChecked(snapshot.child("winter").getValue().toString().compareTo("true") == 0);

                CheckBox editsummerbox = getView().findViewById(R.id.editsummer);
                editsummerbox.setChecked(snapshot.child("summer").getValue().toString().compareTo("true") == 0);


                getView().findViewById(R.id.editadd_prereq).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String s = Prereq.getText().toString();
                        courseRef.child(s).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot presnapshot) {
                                Course edit_prereq_course = snapshot.getValue(Course.class);
                                if(presnapshot.exists()){
                                    if(edit_prereq_course.getPrereqs().contains(s)){
                                        String warningMsg = "This course is already a prerequisite";
                                        Toast.makeText(getActivity(), warningMsg, Toast.LENGTH_LONG).show();
                                    }
                                    else if(s.compareTo("")==0){
                                        String warningMsg = "Please specify the prerequisite";
                                        Toast.makeText(getActivity(), warningMsg, Toast.LENGTH_LONG).show();
                                    }
                                    else if(s.compareTo(editCourseCode)==0){
                                        String warningMsg = "The course cannot be a prerequisite\nfor itself";
                                        Toast.makeText(getActivity(), warningMsg, Toast.LENGTH_LONG).show();
                                    }
                                    else{
                                        edit_prereq_course.addPrereqs(s);

                                        courseRef.child(editCourseCode).setValue(edit_prereq_course);

                                        String warningMsg = "Prerequisite Added";
                                        Toast.makeText(getActivity(), warningMsg, Toast.LENGTH_LONG).show();

                                        NavHostFragment.findNavController(ThirdFragmentAdmin.this)
                                                .navigate(R.id.action_thirdFragmentAdmin_self);

                                    }
                                }
                                else{
                                    String warningMsg = "This course does not yet exist.";
                                    Toast.makeText(getActivity(), warningMsg, Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        Prereq.setText("");

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



//
//
//        // Gets the text from the Course Code and sets the field courseCode of our Course object to the input
//        TextWatcher course_code_watcher = new TextWatcher() {
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                course.setCourseCode(s.toString());
//            }
//        };
//
//        CourseCode.addTextChangedListener(course_code_watcher);
//
//
//        TextWatcher course_name_watcher = new TextWatcher() {
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                course.setCourseName(s.toString());
//            }
//        };
//
//        CourseName.addTextChangedListener(course_name_watcher);
//
//
//        // Checks if the session checkbox is checked or not and assigns the corresponding boolean to our Course object
//        CheckBox fallbox = getView().findViewById(R.id.fall);
//        fallbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                course.setFall(isChecked);
//            }
//        });
//
//        // Checks if the session checkbox is checked or not and assigns the corresponding boolean to our Course object
//        CheckBox winterbox = getView().findViewById(R.id.winter);
//        winterbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                course.setWinter(isChecked);
//            }
//        });
//
//        // Checks if the session checkbox is checked or not and assigns the corresponding boolean to our Course object
//        CheckBox summerbox = getView().findViewById(R.id.summer);
//        summerbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                course.setSummer(isChecked);
//            }
//        });


//        getView().findViewById(R.id.add_prereq).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String s = Prereq.getText().toString();
//                ref.child(s).addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if (snapshot.exists()) {
//                            if (course.getPrereqs().contains(s)) {
//                                String warningMsg = "This course is already a prerequisite";
//                                Toast.makeText(getActivity(), warningMsg, Toast.LENGTH_LONG).show();
//                            } else {
//                                course.addPrereqs(s);
//
//                                String warningMsg = "Prerequisite Added";
//                                Toast.makeText(getActivity(), warningMsg, Toast.LENGTH_LONG).show();
//                            }
//                        } else {
//                            String warningMsg = "This course does not yet exist.";
//                            Toast.makeText(getActivity(), warningMsg, Toast.LENGTH_LONG).show();
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//
//                Prereq.setText("");
//            }
//        });


        // Use the "Create Course" button to add a course to the database with the input information
//        getView().findViewById(R.id.generate).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                // Use this listener on the course code that is being enetered to check if exists in the database already
//                ref.child(course.getCourseCode()).addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if (snapshot.exists()) {
//
//                            // If the course already exists tell the user
//                            String warningMsg = "The course already exists.\nTry going back and editing the course";
//                            Toast.makeText(getActivity(), warningMsg, Toast.LENGTH_LONG).show();
//                        } else {
//
//                            String courseCode = course.getCourseCode();
//                            // Block course creation if no sections are selected
//                            if (!course.isFall() && !course.isWinter() && !course.isSummer()) {
//                                String warningMsg = "Please select offering sessions";
//                                Toast.makeText(getActivity(), warningMsg, Toast.LENGTH_LONG).show();
//                            }
//                            // Block course creation if course code is invalid
//                            else if (courseCode.length() != 6 || !courseCode.substring(0, 3).matches("[A-Z]+") || !courseCode.substring(3, 4).matches("^[A-D]") || !courseCode.substring(4).matches("^[0-9]*$")) {
//                                String warningMsg = "Please enter a valid course code";
//                                Toast.makeText(getActivity(), warningMsg, Toast.LENGTH_LONG).show();
//                            }
//                            // Block course creation if the user does not enter a course name
//                            else if (course.getCourseName().compareTo("") == 0) {
//                                String warningMsg = "Please enter a course name";
//                                Toast.makeText(getActivity(), warningMsg, Toast.LENGTH_LONG).show();
//                            } else {
//                                ref.child(course.getCourseCode()).setValue(course);
//
//                                String warningMsg = "Course Added";
//                                Toast.makeText(getActivity(), warningMsg, Toast.LENGTH_LONG).show();
//
//                                NavHostFragment.findNavController(SecondFragmentAdmin.this)
//                                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//
//            }
//        });

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;


    }

    protected void setPrereqTable(TableLayout table, String child_name) {
        prereqList.clear();
        DatabaseReference courseRef = FirebaseDatabase
                .getInstance("https://course-manager-b07-default-rtdb.firebaseio.com/")
                .getReference().child("Courses").child(child_name);
        courseRef.child("prereqs").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    i = 0;
                    for (DataSnapshot datasnapshot : snapshot.getChildren()) {
                        req = (String) datasnapshot.getValue();
                        prereqList.add(req);
                    }
                    for (String req : prereqList){
                        final String currReq = req;

                        courseRef.child("prereqs").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                TableRow row = new TableRow(getActivity());
                                TableRow.LayoutParams params =
                                        new TableRow.LayoutParams(TableRow
                                                .LayoutParams.WRAP_CONTENT);
                                row.setLayoutParams(params);
                                row.setId(1000 + i);
                                TextView course = new TextView(getActivity());
                                course.setId(2000 + i);
                                row.addView(course);
                                TextView space = new TextView(getActivity());
                                space.setId(3000+i);
                                row.addView(space);

                                Button delete = new Button(getActivity());
                                delete.setId(4000 + i);
                                row.addView(delete);
                                delete.setText("Remove");
                                delete.setTextColor(Color.rgb(0,0,0));
                                delete.setBackgroundColor(Color.rgb(0,239,239));

                                space.setText("     ");
                                course.setText(req);
                                table.addView(row, i);
                                i++;

                                delete.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        courseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                Course a = snapshot.getValue(Course.class);
                                                a.getPrereqs().remove(req);
                                                courseRef.setValue(a);

                                                NavHostFragment.findNavController(ThirdFragmentAdmin.this)
                                                        .navigate(R.id.action_thirdFragmentAdmin_self);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                                    }
                                });

                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

}