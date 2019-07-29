package com.mobileserver.domain;

public class Score {
    /*成绩id*/
    private int scoreId;
    public int getScoreId() {
        return scoreId;
    }
    public void setScoreId(int scoreId) {
        this.scoreId = scoreId;
    }

    /*学生*/
    private String studentObj;
    public String getStudentObj() {
        return studentObj;
    }
    public void setStudentObj(String studentObj) {
        this.studentObj = studentObj;
    }

    /*课程*/
    private String courseObj;
    public String getCourseObj() {
        return courseObj;
    }
    public void setCourseObj(String courseObj) {
        this.courseObj = courseObj;
    }

    /*课程成绩*/
    private float courseScore;
    public float getCourseScore() {
        return courseScore;
    }
    public void setCourseScore(float courseScore) {
        this.courseScore = courseScore;
    }

    /*学生评价*/
    private String evaluate;
    public String getEvaluate() {
        return evaluate;
    }
    public void setEvaluate(String evaluate) {
        this.evaluate = evaluate;
    }

    /*添加时间*/
    private String addTime;
    public String getAddTime() {
        return addTime;
    }
    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

}