package com.chengxusheji.domain;

import java.sql.Timestamp;
public class Score {
    /*�ɼ�id*/
    private int scoreId;
    public int getScoreId() {
        return scoreId;
    }
    public void setScoreId(int scoreId) {
        this.scoreId = scoreId;
    }

    /*ѧ��*/
    private Student studentObj;
    public Student getStudentObj() {
        return studentObj;
    }
    public void setStudentObj(Student studentObj) {
        this.studentObj = studentObj;
    }

    /*�γ�*/
    private Course courseObj;
    public Course getCourseObj() {
        return courseObj;
    }
    public void setCourseObj(Course courseObj) {
        this.courseObj = courseObj;
    }

    /*�γ̳ɼ�*/
    private float courseScore;
    public float getCourseScore() {
        return courseScore;
    }
    public void setCourseScore(float courseScore) {
        this.courseScore = courseScore;
    }

    /*ѧ������*/
    private String evaluate;
    public String getEvaluate() {
        return evaluate;
    }
    public void setEvaluate(String evaluate) {
        this.evaluate = evaluate;
    }

    /*���ʱ��*/
    private String addTime;
    public String getAddTime() {
        return addTime;
    }
    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

}