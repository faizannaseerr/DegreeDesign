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
            }

            else if (!CoursesTaken.contains(CoursesWanted.get(i)) && !TotalCourses.contains(CoursesWanted.get(i))) {
                // need line here to convert string prereqs list to course prereqs list
                TotalCourses = CreateTotalCoursesArray(CoursesTaken, CoursesWanted.get(i).prereqs, TotalCourses);
                TotalCourses.add(CoursesWanted.get(i));
            }

        }
        return TotalCourses;

    }

	/* Things to do later to decrease redundancy/inefficiency:
			Swap maxCourseCounter with PresentSemCourses.size()
			Remove three variations of PresentSemCourses and just use .clear() with one declaration
			Remove all the system.out lines (was added for testing) */

    /* Functions goes through total courses thrice, checking for fall winter and summer courses and if prereqs are satisfied then it is added to a Schedule List
       -- this is repeated until all courses have been added and the degree year is also incremented */

    public ArrayList<Schedule> CreateSchedule(ArrayList<Course> CoursesTaken, ArrayList<Course> TotalCourses){
        ArrayList<Schedule> DegreeSchedule = new ArrayList<Schedule>();
        int DegreeYear = 1;



        while (!TotalCourses.isEmpty()) {
            int maxCourseCounter = 0;
            ArrayList<Course> PresentSemCoursesFall = new ArrayList<Course>();
            System.out.println();
            for (int i = 0; i < TotalCourses.size(); i++) {
                System.out.println(TotalCourses.get(i).getCourseCode());
                if (TotalCourses.get(i).fall) {
                    if (TotalCourses.get(i).prereqs.isEmpty()) {
                        DegreeSchedule.add(new Schedule(TotalCourses.get(i), DegreeYear, "fall"));
                        PresentSemCoursesFall.add(TotalCourses.get(i));
                        maxCourseCounter += 1;
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
                            PresentSemCoursesFall.add(TotalCourses.get(i));
                            maxCourseCounter += 1;
                        }
                    }
                }

                if (maxCourseCounter == 6) {
                    break;
                }

            }
            CoursesTaken.addAll(PresentSemCoursesFall);
            TotalCourses.removeAll(PresentSemCoursesFall);

            maxCourseCounter = 0;
            ArrayList<Course> PresentSemCoursesWinter = new ArrayList<Course>();
            System.out.println();
            for (int i = 0; i < TotalCourses.size(); i++) {
                System.out.println(TotalCourses.get(i).getCourseCode());
                if (TotalCourses.get(i).winter) {
                    if (TotalCourses.get(i).prereqs.isEmpty()) {
                        DegreeSchedule.add(new Schedule(TotalCourses.get(i), DegreeYear, "winter"));
                        PresentSemCoursesWinter.add(TotalCourses.get(i));
                        maxCourseCounter += 1;
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
                            PresentSemCoursesWinter.add(TotalCourses.get(i));
                            maxCourseCounter += 1;
                        }
                    }
                }

                if (maxCourseCounter == 6) {
                    break;
                }

            }
            CoursesTaken.addAll(PresentSemCoursesWinter);
            TotalCourses.removeAll(PresentSemCoursesWinter);



            maxCourseCounter = 0;
            ArrayList<Course> PresentSemCoursesSummer = new ArrayList<Course>();
            System.out.println();
            for (int i = 0; i < TotalCourses.size(); i++) {
                System.out.println(TotalCourses.get(i).getCourseCode());

                if (TotalCourses.get(i).summer) {
                    if (TotalCourses.get(i).prereqs.isEmpty()) {
                        DegreeSchedule.add(new Schedule(TotalCourses.get(i), DegreeYear, "summer"));
                        PresentSemCoursesSummer.add(TotalCourses.get(i));
                        maxCourseCounter += 1;
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
                            PresentSemCoursesSummer.add(TotalCourses.get(i));
                            maxCourseCounter += 1;
                        }
                    }
                }

                if (maxCourseCounter == 6) {
                    break;
                }

            }
            CoursesTaken.addAll(PresentSemCoursesSummer);
            TotalCourses.removeAll(PresentSemCoursesSummer);

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

