package com.mobileserver.domain;

public class Student {
    /*ѧ��*/
    private String studentNo;
    public String getStudentNo() {
        return studentNo;
    }
    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }

    /*��¼����*/
    private String password;
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    /*���ڰ༶*/
    private String classObj;
    public String getClassObj() {
        return classObj;
    }
    public void setClassObj(String classObj) {
        this.classObj = classObj;
    }

    /*����*/
    private String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /*�Ա�*/
    private String sex;
    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }

    /*��������*/
    private java.sql.Timestamp birthday;
    public java.sql.Timestamp getBirthday() {
        return birthday;
    }
    public void setBirthday(java.sql.Timestamp birthday) {
        this.birthday = birthday;
    }

    /*ѧ����Ƭ*/
    private String studentPhoto;
    public String getStudentPhoto() {
        return studentPhoto;
    }
    public void setStudentPhoto(String studentPhoto) {
        this.studentPhoto = studentPhoto;
    }

    /*��ϵ�绰*/
    private String telephone;
    public String getTelephone() {
        return telephone;
    }
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /*��ͥ��ַ*/
    private String address;
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

}