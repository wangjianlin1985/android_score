package com.mobileserver.domain;

public class Kejian {
    /*�μ�id*/
    private int kejianId;
    public int getKejianId() {
        return kejianId;
    }
    public void setKejianId(int kejianId) {
        this.kejianId = kejianId;
    }

    /*����*/
    private String title;
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    /*����*/
    private String content;
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    /*�μ��ļ�*/
    private String kejianFile;
    public String getKejianFile() {
        return kejianFile;
    }
    public void setKejianFile(String kejianFile) {
        this.kejianFile = kejianFile;
    }

    /*�ϴ���ʦ*/
    private String teacherObj;
    public String getTeacherObj() {
        return teacherObj;
    }
    public void setTeacherObj(String teacherObj) {
        this.teacherObj = teacherObj;
    }

    /*�ϴ�ʱ��*/
    private String uploadTime;
    public String getUploadTime() {
        return uploadTime;
    }
    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }

}