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
	/* 传入学生对象，进行学生的添加业务 */
	public String AddStudent(Student student) {
		DB db = new DB();
		String result = "";
		try {
			/* 构建sql执行插入新学生 */
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
			result = "学生添加成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "学生添加失败";
		} finally {
			db.all_close();
		}
		return result;
	}
	/* 删除学生 */
	public String DeleteStudent(String studentNo) {
		DB db = new DB();
		String result = "";
		try {
			String sqlString = "delete from Student where studentNo='" + studentNo + "'";
			db.executeUpdate(sqlString);
			result = "学生删除成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "学生删除失败";
		} finally {
			db.all_close();
		}
		return result;
	}

	/* 根据学号获取到学生 */
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
	/* 更新学生 */
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
			result = "学生更新成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "学生更新失败";
		} finally {
			db.all_close();
		}
		return result;
	}
}
