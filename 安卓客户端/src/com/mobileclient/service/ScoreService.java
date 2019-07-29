package com.mobileclient.service;

import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mobileclient.domain.Score;
import com.mobileclient.util.HttpUtil;

/*成绩管理业务逻辑层*/
public class ScoreService {
	/* 添加成绩 */
	public String AddScore(Score score) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("scoreId", score.getScoreId() + "");
		params.put("studentObj", score.getStudentObj());
		params.put("courseObj", score.getCourseObj());
		params.put("courseScore", score.getCourseScore() + "");
		params.put("evaluate", score.getEvaluate());
		params.put("addTime", score.getAddTime());
		params.put("action", "add");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "ScoreServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 查询成绩 */
	public List<Score> QueryScore(Score queryConditionScore) throws Exception {
		String urlString = HttpUtil.BASE_URL + "ScoreServlet?action=query";
		if(queryConditionScore != null) {
			urlString += "&studentObj=" + URLEncoder.encode(queryConditionScore.getStudentObj(), "UTF-8") + "";
			urlString += "&courseObj=" + URLEncoder.encode(queryConditionScore.getCourseObj(), "UTF-8") + "";
		}

		/* 2种数据解析方法，第一种是用SAXParser解析xml文件格式
		URL url = new URL(urlString);
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		XMLReader xr = sp.getXMLReader();

		ScoreListHandler scoreListHander = new ScoreListHandler();
		xr.setContentHandler(scoreListHander);
		InputStreamReader isr = new InputStreamReader(url.openStream(), "UTF-8");
		InputSource is = new InputSource(isr);
		xr.parse(is);
		List<Score> scoreList = scoreListHander.getScoreList();
		return scoreList;*/
		//第2种是基于json数据格式解析，我们采用的是第2种
		List<Score> scoreList = new ArrayList<Score>();
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(urlString, null, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				Score score = new Score();
				score.setScoreId(object.getInt("scoreId"));
				score.setStudentObj(object.getString("studentObj"));
				score.setCourseObj(object.getString("courseObj"));
				score.setCourseScore((float) object.getDouble("courseScore"));
				score.setEvaluate(object.getString("evaluate"));
				score.setAddTime(object.getString("addTime"));
				scoreList.add(score);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return scoreList;
	}

	/* 更新成绩 */
	public String UpdateScore(Score score) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("scoreId", score.getScoreId() + "");
		params.put("studentObj", score.getStudentObj());
		params.put("courseObj", score.getCourseObj());
		params.put("courseScore", score.getCourseScore() + "");
		params.put("evaluate", score.getEvaluate());
		params.put("addTime", score.getAddTime());
		params.put("action", "update");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "ScoreServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 删除成绩 */
	public String DeleteScore(int scoreId) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("scoreId", scoreId + "");
		params.put("action", "delete");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "ScoreServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "成绩信息删除失败!";
		}
	}

	/* 根据成绩id获取成绩对象 */
	public Score GetScore(int scoreId)  {
		List<Score> scoreList = new ArrayList<Score>();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("scoreId", scoreId + "");
		params.put("action", "updateQuery");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "ScoreServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				Score score = new Score();
				score.setScoreId(object.getInt("scoreId"));
				score.setStudentObj(object.getString("studentObj"));
				score.setCourseObj(object.getString("courseObj"));
				score.setCourseScore((float) object.getDouble("courseScore"));
				score.setEvaluate(object.getString("evaluate"));
				score.setAddTime(object.getString("addTime"));
				scoreList.add(score);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int size = scoreList.size();
		if(size>0) return scoreList.get(0); 
		else return null; 
	}
}
