package com.mobileserver.domain;

public class ClassInfo {
    /*班级编号*/
    private String classNo;
    public String getClassNo() {
        return classNo;
    }
    public void setClassNo(String classNo) {
        this.classNo = classNo;
    }

    /*班级名称*/
    private String className;
    public String getClassName() {
        return className;
    }
    public void setClassName(String className) {
        this.className = className;
    }

    /*开办日期*/
    private java.sql.Timestamp bornDate;
    public java.sql.Timestamp getBornDate() {
        return bornDate;
    }
    public void setBornDate(java.sql.Timestamp bornDate) {
        this.bornDate = bornDate;
    }

    /*班主任*/
    private String headerName;
    public String getHeaderName() {
        return headerName;
    }
    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

}