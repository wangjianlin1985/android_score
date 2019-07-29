package com.mobileserver.dao;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.mobileserver.domain.Kejian;
import com.mobileserver.util.DB;

public class KejianDAO {

	public List<Kejian> QueryKejian(String title,String teacherObj,String uploadTime) {
		List<Kejian> kejianList = new ArrayList<Kejian>();
		DB db = new DB();
		String sql = "select * from Kejian where 1=1";
		if (!title.equals(""))
			sql += " and title like '%" + title + "%'";
		if (!teacherObj.equals(""))
			sql += " and teacherObj = '" + teacherObj + "'";
		if (!uploadTime.equals(""))
			sql += " and uploadTime like '%" + uploadTime + "%'";
		try {
			ResultSet rs = db.executeQuery(sql);
			while (rs.next()) {
				Kejian kejian = new Kejian();
				kejian.setKejianId(rs.getInt("kejianId"));
				kejian.setTitle(rs.getString("title"));
				kejian.setContent(rs.getString("content"));
				kejian.setKejianFile(rs.getString("kejianFile"));
				kejian.setTeacherObj(rs.getString("teacherObj"));
				kejian.setUploadTime(rs.getString("uploadTime"));
				kejianList.add(kejian);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.all_close();
		}
		return kejianList;
	}
	/* 传入课件对象，进行课件的添加业务 */
	public String AddKejian(Kejian kejian) {
		DB db = new DB();
		String result = "";
		try {
			/* 构建sql执行插入新课件 */
			String sqlString = "insert into Kejian(title,content,kejianFile,teacherObj,uploadTime) values (";
			sqlString += "'" + kejian.getTitle() + "',";
			sqlString += "'" + kejian.getContent() + "',";
			sqlString += "'" + kejian.getKejianFile() + "',";
			sqlString += "'" + kejian.getTeacherObj() + "',";
			sqlString += "'" + kejian.getUploadTime() + "'";
			sqlString += ")";
			db.executeUpdate(sqlString);
			result = "课件添加成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "课件添加失败";
		} finally {
			db.all_close();
		}
		return result;
	}
	/* 删除课件 */
	public String DeleteKejian(int kejianId) {
		DB db = new DB();
		String result = "";
		try {
			String sqlString = "delete from Kejian where kejianId=" + kejianId;
			db.executeUpdate(sqlString);
			result = "课件删除成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "课件删除失败";
		} finally {
			db.all_close();
		}
		return result;
	}

	/* 根据课件id获取到课件 */
	public Kejian GetKejian(int kejianId) {
		Kejian kejian = null;
		DB db = new DB();
		String sql = "select * from Kejian where kejianId=" + kejianId;
		try {
			ResultSet rs = db.executeQuery(sql);
			if (rs.next()) {
				kejian = new Kejian();
				kejian.setKejianId(rs.getInt("kejianId"));
				kejian.setTitle(rs.getString("title"));
				kejian.setContent(rs.getString("content"));
				kejian.setKejianFile(rs.getString("kejianFile"));
				kejian.setTeacherObj(rs.getString("teacherObj"));
				kejian.setUploadTime(rs.getString("uploadTime"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.all_close();
		}
		return kejian;
	}
	/* 更新课件 */
	public String UpdateKejian(Kejian kejian) {
		DB db = new DB();
		String result = "";
		try {
			String sql = "update Kejian set ";
			sql += "title='" + kejian.getTitle() + "',";
			sql += "content='" + kejian.getContent() + "',";
			sql += "kejianFile='" + kejian.getKejianFile() + "',";
			sql += "teacherObj='" + kejian.getTeacherObj() + "',";
			sql += "uploadTime='" + kejian.getUploadTime() + "'";
			sql += " where kejianId=" + kejian.getKejianId();
			db.executeUpdate(sql);
			result = "课件更新成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "课件更新失败";
		} finally {
			db.all_close();
		}
		return result;
	}
}
