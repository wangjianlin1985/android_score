package com.mobileserver.domain;

public class Kejian {
    /*课件id*/
    private int kejianId;
    public int getKejianId() {
        return kejianId;
    }
    public void setKejianId(int kejianId) {
        this.kejianId = kejianId;
    }

    /*标题*/
    private String title;
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    /*描述*/
    private String content;
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    /*课件文件*/
    private String kejianFile;
    public String getKejianFile() {
        return kejianFile;
    }
    public void setKejianFile(String kejianFile) {
        this.kejianFile = kejianFile;
    }

    /*上传老师*/
    private String teacherObj;
    public String getTeacherObj() {
        return teacherObj;
    }
    public void setTeacherObj(String teacherObj) {
        this.teacherObj = teacherObj;
    }

    /*上传时间*/
    private String uploadTime;
    public String getUploadTime() {
        return uploadTime;
    }
    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }

}