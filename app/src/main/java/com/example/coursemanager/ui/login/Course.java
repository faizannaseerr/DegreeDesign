package com.example.coursemanager.ui.login;

import java.util.ArrayList;

public class Course {
    String courseCode;
    ArrayList<String> prereqs;
    boolean fall;
    boolean winter;
    boolean summer;

    public Course(){
        courseCode = "";
        prereqs = new ArrayList<String>();
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public ArrayList<String> getPrereqs() {
        return prereqs;
    }

    public void setPrereqs(ArrayList<String> prereqs) {
        this.prereqs = prereqs;
    }

    public boolean isFall() {
        return fall;
    }

    public void setFall(boolean fall) {
        this.fall = fall;
    }

    public boolean isWinter() {
        return winter;
    }

    public void setWinter(boolean winter) {
        this.winter = winter;
    }

    public boolean isSummer() {
        return summer;
    }

    public void setSummer(boolean summer) {
        this.summer = summer;
    }

}
