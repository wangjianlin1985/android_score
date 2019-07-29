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
	/* 传入成绩对象，进行成绩的添加业务 */
	public String AddScore(Score score) {
		DB db = new DB();
		String result = "";
		try {
			/* 构建sql执行插入新成绩 */
			String sqlString = "insert into Score(studentObj,courseObj,courseScore,evaluate,addTime) values (";
			sqlString += "'" + score.getStudentObj() + "',";
			sqlString += "'" + score.getCourseObj() + "',";
			sqlString += score.getCourseScore() + ",";
			sqlString += "'" + score.getEvaluate() + "',";
			sqlString += "'" + score.getAddTime() + "'";
			sqlString += ")";
			db.executeUpdate(sqlString);
			result = "成绩添加成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "成绩添加失败";
		} finally {
			db.all_close();
		}
		return result;
	}
	/* 删除成绩 */
	public String DeleteScore(int scoreId) {
		DB db = new DB();
		String result = "";
		try {
			String sqlString = "delete from Score where scoreId=" + scoreId;
			db.executeUpdate(sqlString);
			result = "成绩删除成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "成绩删除失败";
		} finally {
			db.all_close();
		}
		return result;
	}

	/* 根据成绩id获取到成绩 */
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
	/* 更新成绩 */
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
			result = "成绩更新成功!";
		} catch (Exception e) {
			e.printStackTrace();
			result = "成绩更新失败";
		} finally {
			db.all_close();
		}
		return result;
	}
}
