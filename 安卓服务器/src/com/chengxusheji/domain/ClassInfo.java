package com.chengxusheji.domain;

import java.sql.Timestamp;
public class ClassInfo {
    /*�༶���*/
    private String classNo;
    public String getClassNo() {
        return classNo;
    }
    public void setClassNo(String classNo) {
        this.classNo = classNo;
    }

    /*�༶����*/
    private String className;
    public String getClassName() {
        return className;
    }
    public void setClassName(String className) {
        this.className = className;
    }

    /*��������*/
    private Timestamp bornDate;
    public Timestamp getBornDate() {
        return bornDate;
    }
    public void setBornDate(Timestamp bornDate) {
        this.bornDate = bornDate;
    }

    /*������*/
    private String headerName;
    public String getHeaderName() {
        return headerName;
    }
    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

}