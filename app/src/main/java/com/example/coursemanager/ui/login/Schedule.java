package com.example.coursemanager.ui.login;

import java.util.ArrayList;
import java.util.List;

public class Schedule {
    Course course;
    int year;
    String semester;

    // This constructor is used for using the whole algorithm
    public Schedule() {
        this.course = null;
        this.year = 0;
        this.semester = "";
    }

    // This one is used while creating the schedule
    public Schedule(Course course, int year, String semester) {
        this.course = course;
        this.year = year;
        this.semester = semester;
    }

    // Uses recursion to create a total list for all the courses needed
    public ArrayList<Course> CreateTotalCoursesArray(ArrayList<Course> CoursesTaken, ArrayList<Course> CoursesWanted, ArrayList<Course> TotalCourses) {


        for (int i = 0; i < CoursesWanted.size(); i++) {
            if (CoursesWanted.get(i).prereqs.isEmpty() && !CoursesTaken.contains(CoursesWanted.get(i)) && !TotalCourses.contains(CoursesWanted.get(i))) {
                TotalCourses.add(CoursesWanted.get(i));
                return TotalCourses;
            } else if (!CoursesTaken.contains(CoursesWanted.get(i)) && !TotalCourses.contains(CoursesWanted.get(i))) {
                // need line here to convert string prereqs list to course prereqs list
                TotalCourses = CreateTotalCoursesArray(CoursesTaken, CoursesWanted.get(i).prereqs, TotalCourses);
                TotalCourses.add(CoursesWanted.get(i));
            }

        }
        return TotalCourses;

    }

    /* Functions goes through total courses thrice, checking for fall winter and summer courses and if prereqs are satisfied then it is added to a Schedule List
       -- this is repeated until all courses have been added and the degree year is also incremented */

    public ArrayList<Schedule> CreateSchedule(ArrayList<Course> CoursesTaken, ArrayList<Course> TotalCourses){
        ArrayList<Schedule> DegreeSchedule = new ArrayList<Schedule>();
        int DegreeYear = 1;
        ArrayList<Course> PresentSemCourses = new ArrayList<Course>();


        while (!TotalCourses.isEmpty()) {

            PresentSemCourses.clear();
            for (int i = 0; i < TotalCourses.size(); i++) {
                if (TotalCourses.get(i).fall) {
                    if (TotalCourses.get(i).prereqs.isEmpty()) {
                        DegreeSchedule.add(new Schedule(TotalCourses.get(i), DegreeYear, "fall"));
                        PresentSemCourses.add(TotalCourses.get(i));
                    }

                    else {
                        boolean PreReqsSatisfied = true;
                        for (int j = 0; j < TotalCourses.get(i).prereqs.size(); j++) {
                            if (!CoursesTaken.contains(TotalCourses.get(i).prereqs.get(j))) {
                                PreReqsSatisfied = false;
                                break;
                            }
                        }

                        if (PreReqsSatisfied) {
                            DegreeSchedule.add(new Schedule(TotalCourses.get(i), DegreeYear, "fall"));
                            PresentSemCourses.add(TotalCourses.get(i));
                        }
                    }
                }

                if (PresentSemCourses.size() == 6) {
                    break;
                }

            }
            CoursesTaken.addAll(PresentSemCourses);
            TotalCourses.removeAll(PresentSemCourses);


            PresentSemCourses.clear();
            for (int i = 0; i < TotalCourses.size(); i++) {
                if (TotalCourses.get(i).winter) {
                    if (TotalCourses.get(i).prereqs.isEmpty()) {
                        DegreeSchedule.add(new Schedule(TotalCourses.get(i), DegreeYear, "winter"));
                        PresentSemCourses.add(TotalCourses.get(i));
                    }

                    else {
                        boolean PreReqsSatisfied = true;
                        for (int j = 0; j < TotalCourses.get(i).prereqs.size(); j++) {
                            if (!CoursesTaken.contains(TotalCourses.get(i).prereqs.get(j))) {
                                PreReqsSatisfied = false;
                                break;
                            }
                        }

                        if (PreReqsSatisfied) {
                            DegreeSchedule.add(new Schedule(TotalCourses.get(i), DegreeYear, "winter"));
                            PresentSemCourses.add(TotalCourses.get(i));
                        }
                    }
                }

                if (PresentSemCourses.size() == 6) {
                    break;
                }

            }
            CoursesTaken.addAll(PresentSemCourses);
            TotalCourses.removeAll(PresentSemCourses);

            PresentSemCourses.clear();
            for (int i = 0; i < TotalCourses.size(); i++) {
                if (TotalCourses.get(i).summer) {
                    if (TotalCourses.get(i).prereqs.isEmpty()) {
                        DegreeSchedule.add(new Schedule(TotalCourses.get(i), DegreeYear, "summer"));
                        PresentSemCourses.add(TotalCourses.get(i));
                    }

                    else {
                        boolean PreReqsSatisfied = true;
                        for (int j = 0; j < TotalCourses.get(i).prereqs.size(); j++) {
                            if (!CoursesTaken.contains(TotalCourses.get(i).prereqs.get(j))) {
                                PreReqsSatisfied = false;
                                break;
                            }
                        }

                        if (PreReqsSatisfied) {
                            DegreeSchedule.add(new Schedule(TotalCourses.get(i), DegreeYear, "summer"));
                            PresentSemCourses.add(TotalCourses.get(i));
                        }
                    }
                }

                if (PresentSemCourses.size() == 6) {
                    break;
                }

            }
            CoursesTaken.addAll(PresentSemCourses);
            TotalCourses.removeAll(PresentSemCourses);

            DegreeYear += 1;
        }


        return DegreeSchedule;

    }

    public void PrintSchedule (ArrayList <Schedule> DegreeSchedule) {
        for (int i = 0; i < DegreeSchedule.size(); i++) {
            System.out.println(DegreeSchedule.get(i).course.courseCode);
            System.out.println(DegreeSchedule.get(i).semester);
            System.out.println(DegreeSchedule.get(i).year);
        }
    }
}

