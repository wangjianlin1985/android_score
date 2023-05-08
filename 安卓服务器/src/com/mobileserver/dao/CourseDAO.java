package com.mobileserver.dao;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.mobileserver.domain.Course;
import com.mobileserver.util.DB;

public class CourseDAO {

	public List<Course> QueryCourse(String courseNo,String courseName,String teacherObj) {
		List<Course> courseList = new ArrayList<Course>();
		DB db = new DB();
		String sql = "select * from Course where 1=1";
		if (!courseNo.equals(""))
			sql += " and courseNo like '%" + courseNo + "%'";
		if (!courseName.equals(""))
			sql += " and courseName like '%" + courseName + "%'";
		if (!teacherObj.equals(""))
			sql += " and teacherObj = '" + teacherObj + "'";
		try {
			ResultSet rs = db.executeQuery(sql);
			while (rs.next()) {
				Course course = new Course();
				course.setCourseNo(rs.getString("courseNo"));
				course.setCourseName(rs.getString("courseName"));
				course.setTeacherObj(rs.getString("teacherObj"));
				course.setCoursePlace(rs.getString("coursePlace"));
				course.setCourseTime(rs.getString("courseTime"));
				course.setCourseHours(rs.getInt("courseHours"));
				course.setCourseScore(rs.getFloat("courseScore"));
				course.setMemo(rs.getString("memo"));
				courseList.add(course);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.all_close();
		}
		return courseList;
	}
	/* ����γ̶��󣬽��пγ̵����ҵ�� */
	public String AddCourse(Course course) {
		DB db = new DB();
		String result = "";
		try {
			/* ����sqlִ�в����¿γ� */
			String sqlString = "insert into Course(courseNo,courseName,teacherObj,coursePlace,courseTime,courseHours,courseScore,memo) values (";
			sqlString += "'" + course.getCourseNo() + "',";
			sqlString += "'" + course.getCourseName() + "',";
			sqlString += "'" + course.getTeacherObj() + "',";
			sqlString += "'" + course.getCoursePlace() + "',";
			sqlString += "'" + course.getCourseTime() + "',";
			sqlString += course.getCourseHours() + ",";
			sqlString += course.getCourseScore() + ",";
			sqlString += "'" + course.getMemo() + "'";
			sqlString += ")";
			db.executeUpdate(sqlString);
			result = "�γ���ӳɹ�!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "�γ����ʧ��";
		} finally {
			db.all_close();
		}
		return result;
	}
	/* ɾ���γ� */
	public String DeleteCourse(String courseNo) {
		DB db = new DB();
		String result = "";
		try {
			String sqlString = "delete from Course where courseNo='" + courseNo + "'";
			db.executeUpdate(sqlString);
			result = "�γ�ɾ���ɹ�!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "�γ�ɾ��ʧ��";
		} finally {
			db.all_close();
		}
		return result;
	}

	/* ���ݿγ̱�Ż�ȡ���γ� */
	public Course GetCourse(String courseNo) {
		Course course = null;
		DB db = new DB();
		String sql = "select * from Course where courseNo='" + courseNo + "'";
		try {
			ResultSet rs = db.executeQuery(sql);
			if (rs.next()) {
				course = new Course();
				course.setCourseNo(rs.getString("courseNo"));
				course.setCourseName(rs.getString("courseName"));
				course.setTeacherObj(rs.getString("teacherObj"));
				course.setCoursePlace(rs.getString("coursePlace"));
				course.setCourseTime(rs.getString("courseTime"));
				course.setCourseHours(rs.getInt("courseHours"));
				course.setCourseScore(rs.getFloat("courseScore"));
				course.setMemo(rs.getString("memo"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.all_close();
		}
		return course;
	}
	/* ���¿γ� */
	public String UpdateCourse(Course course) {
		DB db = new DB();
		String result = "";
		try {
			String sql = "update Course set ";
			sql += "courseName='" + course.getCourseName() + "',";
			sql += "teacherObj='" + course.getTeacherObj() + "',";
			sql += "coursePlace='" + course.getCoursePlace() + "',";
			sql += "courseTime='" + course.getCourseTime() + "',";
			sql += "courseHours=" + course.getCourseHours() + ",";
			sql += "courseScore=" + course.getCourseScore() + ",";
			sql += "memo='" + course.getMemo() + "'";
			sql += " where courseNo='" + course.getCourseNo() + "'";
			db.executeUpdate(sql);
			result = "�γ̸��³ɹ�!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "�γ̸���ʧ��";
		} finally {
			db.all_close();
		}
		return result;
	}
}
