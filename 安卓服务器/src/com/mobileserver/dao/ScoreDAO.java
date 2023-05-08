package com.mobileserver.dao;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.mobileserver.domain.Score;
import com.mobileserver.util.DB;

public class ScoreDAO {

	public List<Score> QueryScore(String studentObj,String courseObj) {
		List<Score> scoreList = new ArrayList<Score>();
		DB db = new DB();
		String sql = "select * from Score where 1=1";
		if (!studentObj.equals(""))
			sql += " and studentObj = '" + studentObj + "'";
		if (!courseObj.equals(""))
			sql += " and courseObj = '" + courseObj + "'";
		try {
			ResultSet rs = db.executeQuery(sql);
			while (rs.next()) {
				Score score = new Score();
				score.setScoreId(rs.getInt("scoreId"));
				score.setStudentObj(rs.getString("studentObj"));
				score.setCourseObj(rs.getString("courseObj"));
				score.setCourseScore(rs.getFloat("courseScore"));
				score.setEvaluate(rs.getString("evaluate"));
				score.setAddTime(rs.getString("addTime"));
				scoreList.add(score);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.all_close();
		}
		return scoreList;
	}
	/* ����ɼ����󣬽��гɼ������ҵ�� */
	public String AddScore(Score score) {
		DB db = new DB();
		String result = "";
		try {
			/* ����sqlִ�в����³ɼ� */
			String sqlString = "insert into Score(studentObj,courseObj,courseScore,evaluate,addTime) values (";
			sqlString += "'" + score.getStudentObj() + "',";
			sqlString += "'" + score.getCourseObj() + "',";
			sqlString += score.getCourseScore() + ",";
			sqlString += "'" + score.getEvaluate() + "',";
			sqlString += "'" + score.getAddTime() + "'";
			sqlString += ")";
			db.executeUpdate(sqlString);
			result = "�ɼ���ӳɹ�!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "�ɼ����ʧ��";
		} finally {
			db.all_close();
		}
		return result;
	}
	/* ɾ���ɼ� */
	public String DeleteScore(int scoreId) {
		DB db = new DB();
		String result = "";
		try {
			String sqlString = "delete from Score where scoreId=" + scoreId;
			db.executeUpdate(sqlString);
			result = "�ɼ�ɾ���ɹ�!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "�ɼ�ɾ��ʧ��";
		} finally {
			db.all_close();
		}
		return result;
	}

	/* ���ݳɼ�id��ȡ���ɼ� */
	public Score GetScore(int scoreId) {
		Score score = null;
		DB db = new DB();
		String sql = "select * from Score where scoreId=" + scoreId;
		try {
			ResultSet rs = db.executeQuery(sql);
			if (rs.next()) {
				score = new Score();
				score.setScoreId(rs.getInt("scoreId"));
				score.setStudentObj(rs.getString("studentObj"));
				score.setCourseObj(rs.getString("courseObj"));
				score.setCourseScore(rs.getFloat("courseScore"));
				score.setEvaluate(rs.getString("evaluate"));
				score.setAddTime(rs.getString("addTime"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.all_close();
		}
		return score;
	}
	/* ���³ɼ� */
	public String UpdateScore(Score score) {
		DB db = new DB();
		String result = "";
		try {
			String sql = "update Score set ";
			sql += "studentObj='" + score.getStudentObj() + "',";
			sql += "courseObj='" + score.getCourseObj() + "',";
			sql += "courseScore=" + score.getCourseScore() + ",";
			sql += "evaluate='" + score.getEvaluate() + "',";
			sql += "addTime='" + score.getAddTime() + "'";
			sql += " where scoreId=" + score.getScoreId();
			db.executeUpdate(sql);
			result = "�ɼ����³ɹ�!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "�ɼ�����ʧ��";
		} finally {
			db.all_close();
		}
		return result;
	}
}
