package com.mobileclient.service;

import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mobileclient.domain.Kejian;
import com.mobileclient.util.HttpUtil;

/*课件管理业务逻辑层*/
public class KejianService {
	/* 添加课件 */
	public String AddKejian(Kejian kejian) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("kejianId", kejian.getKejianId() + "");
		params.put("title", kejian.getTitle());
		params.put("content", kejian.getContent());
		params.put("kejianFile", kejian.getKejianFile());
		params.put("teacherObj", kejian.getTeacherObj());
		params.put("uploadTime", kejian.getUploadTime());
		params.put("action", "add");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "KejianServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 查询课件 */
	public List<Kejian> QueryKejian(Kejian queryConditionKejian) throws Exception {
		String urlString = HttpUtil.BASE_URL + "KejianServlet?action=query";
		if(queryConditionKejian != null) {
			urlString += "&title=" + URLEncoder.encode(queryConditionKejian.getTitle(), "UTF-8") + "";
			urlString += "&teacherObj=" + URLEncoder.encode(queryConditionKejian.getTeacherObj(), "UTF-8") + "";
			urlString += "&uploadTime=" + URLEncoder.encode(queryConditionKejian.getUploadTime(), "UTF-8") + "";
		}

		/* 2种数据解析方法，第一种是用SAXParser解析xml文件格式
		URL url = new URL(urlString);
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		XMLReader xr = sp.getXMLReader();

		KejianListHandler kejianListHander = new KejianListHandler();
		xr.setContentHandler(kejianListHander);
		InputStreamReader isr = new InputStreamReader(url.openStream(), "UTF-8");
		InputSource is = new InputSource(isr);
		xr.parse(is);
		List<Kejian> kejianList = kejianListHander.getKejianList();
		return kejianList;*/
		//第2种是基于json数据格式解析，我们采用的是第2种
		List<Kejian> kejianList = new ArrayList<Kejian>();
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(urlString, null, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				Kejian kejian = new Kejian();
				kejian.setKejianId(object.getInt("kejianId"));
				kejian.setTitle(object.getString("title"));
				kejian.setContent(object.getString("content"));
				kejian.setKejianFile(object.getString("kejianFile"));
				kejian.setTeacherObj(object.getString("teacherObj"));
				kejian.setUploadTime(object.getString("uploadTime"));
				kejianList.add(kejian);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return kejianList;
	}

	/* 更新课件 */
	public String UpdateKejian(Kejian kejian) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("kejianId", kejian.getKejianId() + "");
		params.put("title", kejian.getTitle());
		params.put("content", kejian.getContent());
		params.put("kejianFile", kejian.getKejianFile());
		params.put("teacherObj", kejian.getTeacherObj());
		params.put("uploadTime", kejian.getUploadTime());
		params.put("action", "update");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "KejianServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 删除课件 */
	public String DeleteKejian(int kejianId) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("kejianId", kejianId + "");
		params.put("action", "delete");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "KejianServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "课件信息删除失败!";
		}
	}

	/* 根据课件id获取课件对象 */
	public Kejian GetKejian(int kejianId)  {
		List<Kejian> kejianList = new ArrayList<Kejian>();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("kejianId", kejianId + "");
		params.put("action", "updateQuery");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "KejianServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				Kejian kejian = new Kejian();
				kejian.setKejianId(object.getInt("kejianId"));
				kejian.setTitle(object.getString("title"));
				kejian.setContent(object.getString("content"));
				kejian.setKejianFile(object.getString("kejianFile"));
				kejian.setTeacherObj(object.getString("teacherObj"));
				kejian.setUploadTime(object.getString("uploadTime"));
				kejianList.add(kejian);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int size = kejianList.size();
		if(size>0) return kejianList.get(0); 
		else return null; 
	}
}
