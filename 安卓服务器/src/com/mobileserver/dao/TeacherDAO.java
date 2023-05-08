package com.mobileserver.dao;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.mobileserver.domain.Teacher;
import com.mobileserver.util.DB;

public class TeacherDAO {

	public List<Teacher> QueryTeacher(String teacherNo,String name,Timestamp birthday,String telephone) {
		List<Teacher> teacherList = new ArrayList<Teacher>();
		DB db = new DB();
		String sql = "select * from Teacher where 1=1";
		if (!teacherNo.equals(""))
			sql += " and teacherNo like '%" + teacherNo + "%'";
		if (!name.equals(""))
			sql += " and name like '%" + name + "%'";
		if(birthday!=null)
			sql += " and birthday='" + birthday + "'";
		if (!telephone.equals(""))
			sql += " and telephone like '%" + telephone + "%'";
		try {
			ResultSet rs = db.executeQuery(sql);
			while (rs.next()) {
				Teacher teacher = new Teacher();
				teacher.setTeacherNo(rs.getString("teacherNo"));
				teacher.setPassword(rs.getString("password"));
				teacher.setName(rs.getString("name"));
				teacher.setSex(rs.getString("sex"));
				teacher.setBirthday(rs.getTimestamp("birthday"));
				teacher.setTelephone(rs.getString("telephone"));
				teacher.setEmail(rs.getString("email"));
				teacher.setAddress(rs.getString("address"));
				teacher.setMemo(rs.getString("memo"));
				teacherList.add(teacher);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.all_close();
		}
		return teacherList;
	}
	/* ������ʦ���󣬽�����ʦ�����ҵ�� */
	public String AddTeacher(Teacher teacher) {
		DB db = new DB();
		String result = "";
		try {
			/* ����sqlִ�в�������ʦ */
			String sqlString = "insert into Teacher(teacherNo,password,name,sex,birthday,telephone,email,address,memo) values (";
			sqlString += "'" + teacher.getTeacherNo() + "',";
			sqlString += "'" + teacher.getPassword() + "',";
			sqlString += "'" + teacher.getName() + "',";
			sqlString += "'" + teacher.getSex() + "',";
			sqlString += "'" + teacher.getBirthday() + "',";
			sqlString += "'" + teacher.getTelephone() + "',";
			sqlString += "'" + teacher.getEmail() + "',";
			sqlString += "'" + teacher.getAddress() + "',";
			sqlString += "'" + teacher.getMemo() + "'";
			sqlString += ")";
			db.executeUpdate(sqlString);
			result = "��ʦ��ӳɹ�!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "��ʦ���ʧ��";
		} finally {
			db.all_close();
		}
		return result;
	}
	/* ɾ����ʦ */
	public String DeleteTeacher(String teacherNo) {
		DB db = new DB();
		String result = "";
		try {
			String sqlString = "delete from Teacher where teacherNo='" + teacherNo + "'";
			db.executeUpdate(sqlString);
			result = "��ʦɾ���ɹ�!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "��ʦɾ��ʧ��";
		} finally {
			db.all_close();
		}
		return result;
	}

	/* ���ݽ�ʦ��Ż�ȡ����ʦ */
	public Teacher GetTeacher(String teacherNo) {
		Teacher teacher = null;
		DB db = new DB();
		String sql = "select * from Teacher where teacherNo='" + teacherNo + "'";
		try {
			ResultSet rs = db.executeQuery(sql);
			if (rs.next()) {
				teacher = new Teacher();
				teacher.setTeacherNo(rs.getString("teacherNo"));
				teacher.setPassword(rs.getString("password"));
				teacher.setName(rs.getString("name"));
				teacher.setSex(rs.getString("sex"));
				teacher.setBirthday(rs.getTimestamp("birthday"));
				teacher.setTelephone(rs.getString("telephone"));
				teacher.setEmail(rs.getString("email"));
				teacher.setAddress(rs.getString("address"));
				teacher.setMemo(rs.getString("memo"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.all_close();
		}
		return teacher;
	}
	/* ������ʦ */
	public String UpdateTeacher(Teacher teacher) {
		DB db = new DB();
		String result = "";
		try {
			String sql = "update Teacher set ";
			sql += "password='" + teacher.getPassword() + "',";
			sql += "name='" + teacher.getName() + "',";
			sql += "sex='" + teacher.getSex() + "',";
			sql += "birthday='" + teacher.getBirthday() + "',";
			sql += "telephone='" + teacher.getTelephone() + "',";
			sql += "email='" + teacher.getEmail() + "',";
			sql += "address='" + teacher.getAddress() + "',";
			sql += "memo='" + teacher.getMemo() + "'";
			sql += " where teacherNo='" + teacher.getTeacherNo() + "'";
			db.executeUpdate(sql);
			result = "��ʦ���³ɹ�!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "��ʦ����ʧ��";
		} finally {
			db.all_close();
		}
		return result;
	}
}
