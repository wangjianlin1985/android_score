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
	/* ����μ����󣬽��пμ������ҵ�� */
	public String AddKejian(Kejian kejian) {
		DB db = new DB();
		String result = "";
		try {
			/* ����sqlִ�в����¿μ� */
			String sqlString = "insert into Kejian(title,content,kejianFile,teacherObj,uploadTime) values (";
			sqlString += "'" + kejian.getTitle() + "',";
			sqlString += "'" + kejian.getContent() + "',";
			sqlString += "'" + kejian.getKejianFile() + "',";
			sqlString += "'" + kejian.getTeacherObj() + "',";
			sqlString += "'" + kejian.getUploadTime() + "'";
			sqlString += ")";
			db.executeUpdate(sqlString);
			result = "�μ���ӳɹ�!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "�μ����ʧ��";
		} finally {
			db.all_close();
		}
		return result;
	}
	/* ɾ���μ� */
	public String DeleteKejian(int kejianId) {
		DB db = new DB();
		String result = "";
		try {
			String sqlString = "delete from Kejian where kejianId=" + kejianId;
			db.executeUpdate(sqlString);
			result = "�μ�ɾ���ɹ�!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "�μ�ɾ��ʧ��";
		} finally {
			db.all_close();
		}
		return result;
	}

	/* ���ݿμ�id��ȡ���μ� */
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
	/* ���¿μ� */
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
			result = "�μ����³ɹ�!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "�μ�����ʧ��";
		} finally {
			db.all_close();
		}
		return result;
	}
}
