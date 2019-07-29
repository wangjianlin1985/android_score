package com.mobileclient.domain;

import java.io.Serializable;

public class Course implements Serializable {
    /*课程编号*/
    private String courseNo;
    public String getCourseNo() {
        return courseNo;
    }
    public void setCourseNo(String courseNo) {
        this.courseNo = courseNo;
    }

    /*课程名称*/
    private String courseName;
    public String getCourseName() {
        return courseName;
    }
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    /*上课老师*/
    private String teacherObj;
    public String getTeacherObj() {
        return teacherObj;
    }
    public void setTeacherObj(String teacherObj) {
        this.teacherObj = teacherObj;
    }

    /*上课地点*/
    private String coursePlace;
    public String getCoursePlace() {
        return coursePlace;
    }
    public void setCoursePlace(String coursePlace) {
        this.coursePlace = coursePlace;
    }

    /*上课时间*/
    private String courseTime;
    public String getCourseTime() {
        return courseTime;
    }
    public void setCourseTime(String courseTime) {
        this.courseTime = courseTime;
    }

    /*总学时*/
    private int courseHours;
    public int getCourseHours() {
        return courseHours;
    }
    public void setCourseHours(int courseHours) {
        this.courseHours = courseHours;
    }

    /*课程学分*/
    private float courseScore;
    public float getCourseScore() {
        return courseScore;
    }
    public void setCourseScore(float courseScore) {
        this.courseScore = courseScore;
    }

    /*附加信息*/
    private String memo;
    public String getMemo() {
        return memo;
    }
    public void setMemo(String memo) {
        this.memo = memo;
    }

}