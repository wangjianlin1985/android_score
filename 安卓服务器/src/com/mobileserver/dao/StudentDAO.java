package com.mobileserver.dao;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.mobileserver.domain.Student;
import com.mobileserver.util.DB;

public class StudentDAO {

	public List<Student> QueryStudent(String studentNo,String classObj,String name,Timestamp birthday,String telephone) {
		List<Student> studentList = new ArrayList<Student>();
		DB db = new DB();
		String sql = "select * from Student where 1=1";
		if (!studentNo.equals(""))
			sql += " and studentNo like '%" + studentNo + "%'";
		if (!classObj.equals(""))
			sql += " and classObj = '" + classObj + "'";
		if (!name.equals(""))
			sql += " and name like '%" + name + "%'";
		if(birthday!=null)
			sql += " and birthday='" + birthday + "'";
		if (!telephone.equals(""))
			sql += " and telephone like '%" + telephone + "%'";
		try {
			ResultSet rs = db.executeQuery(sql);
			while (rs.next()) {
				Student student = new Student();
				student.setStudentNo(rs.getString("studentNo"));
				student.setPassword(rs.getString("password"));
				student.setClassObj(rs.getString("classObj"));
				student.setName(rs.getString("name"));
				student.setSex(rs.getString("sex"));
				student.setBirthday(rs.getTimestamp("birthday"));
				student.setStudentPhoto(rs.getString("studentPhoto"));
				student.setTelephone(rs.getString("telephone"));
				student.setAddress(rs.getString("address"));
				studentList.add(student);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.all_close();
		}
		return studentList;
	}
	/* ����ѧ�����󣬽���ѧ�������ҵ�� */
	public String AddStudent(Student student) {
		DB db = new DB();
		String result = "";
		try {
			/* ����sqlִ�в�����ѧ�� */
			String sqlString = "insert into Student(studentNo,password,classObj,name,sex,birthday,studentPhoto,telephone,address) values (";
			sqlString += "'" + student.getStudentNo() + "',";
			sqlString += "'" + student.getPassword() + "',";
			sqlString += "'" + student.getClassObj() + "',";
			sqlString += "'" + student.getName() + "',";
			sqlString += "'" + student.getSex() + "',";
			sqlString += "'" + student.getBirthday() + "',";
			sqlString += "'" + student.getStudentPhoto() + "',";
			sqlString += "'" + student.getTelephone() + "',";
			sqlString += "'" + student.getAddress() + "'";
			sqlString += ")";
			db.executeUpdate(sqlString);
			result = "ѧ����ӳɹ�!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "ѧ�����ʧ��";
		} finally {
			db.all_close();
		}
		return result;
	}
	/* ɾ��ѧ�� */
	public String DeleteStudent(String studentNo) {
		DB db = new DB();
		String result = "";
		try {
			String sqlString = "delete from Student where studentNo='" + studentNo + "'";
			db.executeUpdate(sqlString);
			result = "ѧ��ɾ���ɹ�!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "ѧ��ɾ��ʧ��";
		} finally {
			db.all_close();
		}
		return result;
	}

	/* ����ѧ�Ż�ȡ��ѧ�� */
	public Student GetStudent(String studentNo) {
		Student student = null;
		DB db = new DB();
		String sql = "select * from Student where studentNo='" + studentNo + "'";
		try {
			ResultSet rs = db.executeQuery(sql);
			if (rs.next()) {
				student = new Student();
				student.setStudentNo(rs.getString("studentNo"));
				student.setPassword(rs.getString("password"));
				student.setClassObj(rs.getString("classObj"));
				student.setName(rs.getString("name"));
				student.setSex(rs.getString("sex"));
				student.setBirthday(rs.getTimestamp("birthday"));
				student.setStudentPhoto(rs.getString("studentPhoto"));
				student.setTelephone(rs.getString("telephone"));
				student.setAddress(rs.getString("address"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.all_close();
		}
		return student;
	}
	/* ����ѧ�� */
	public String UpdateStudent(Student student) {
		DB db = new DB();
		String result = "";
		try {
			String sql = "update Student set ";
			sql += "password='" + student.getPassword() + "',";
			sql += "classObj='" + student.getClassObj() + "',";
			sql += "name='" + student.getName() + "',";
			sql += "sex='" + student.getSex() + "',";
			sql += "birthday='" + student.getBirthday() + "',";
			sql += "studentPhoto='" + student.getStudentPhoto() + "',";
			sql += "telephone='" + student.getTelephone() + "',";
			sql += "address='" + student.getAddress() + "'";
			sql += " where studentNo='" + student.getStudentNo() + "'";
			db.executeUpdate(sql);
			result = "ѧ�����³ɹ�!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "ѧ������ʧ��";
		} finally {
			db.all_close();
		}
		return result;
	}
}
