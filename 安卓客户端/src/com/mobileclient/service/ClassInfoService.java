package com.mobileclient.service;

import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mobileclient.domain.ClassInfo;
import com.mobileclient.util.HttpUtil;

/*班级管理业务逻辑层*/
public class ClassInfoService {
	/* 添加班级 */
	public String AddClassInfo(ClassInfo classInfo) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("classNo", classInfo.getClassNo());
		params.put("className", classInfo.getClassName());
		params.put("bornDate", classInfo.getBornDate().toString());
		params.put("headerName", classInfo.getHeaderName());
		params.put("action", "add");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "ClassInfoServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 查询班级 */
	public List<ClassInfo> QueryClassInfo(ClassInfo queryConditionClassInfo) throws Exception {
		String urlString = HttpUtil.BASE_URL + "ClassInfoServlet?action=query";
		if(queryConditionClassInfo != null) {
			urlString += "&classNo=" + URLEncoder.encode(queryConditionClassInfo.getClassNo(), "UTF-8") + "";
			urlString += "&className=" + URLEncoder.encode(queryConditionClassInfo.getClassName(), "UTF-8") + "";
			if(queryConditionClassInfo.getBornDate() != null) {
				urlString += "&bornDate=" + URLEncoder.encode(queryConditionClassInfo.getBornDate().toString(), "UTF-8");
			}
		}

		/* 2种数据解析方法，第一种是用SAXParser解析xml文件格式
		URL url = new URL(urlString);
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		XMLReader xr = sp.getXMLReader();

		ClassInfoListHandler classInfoListHander = new ClassInfoListHandler();
		xr.setContentHandler(classInfoListHander);
		InputStreamReader isr = new InputStreamReader(url.openStream(), "UTF-8");
		InputSource is = new InputSource(isr);
		xr.parse(is);
		List<ClassInfo> classInfoList = classInfoListHander.getClassInfoList();
		return classInfoList;*/
		//第2种是基于json数据格式解析，我们采用的是第2种
		List<ClassInfo> classInfoList = new ArrayList<ClassInfo>();
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(urlString, null, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				ClassInfo classInfo = new ClassInfo();
				classInfo.setClassNo(object.getString("classNo"));
				classInfo.setClassName(object.getString("className"));
				classInfo.setBornDate(Timestamp.valueOf(object.getString("bornDate")));
				classInfo.setHeaderName(object.getString("headerName"));
				classInfoList.add(classInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return classInfoList;
	}

	/* 更新班级 */
	public String UpdateClassInfo(ClassInfo classInfo) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("classNo", classInfo.getClassNo());
		params.put("className", classInfo.getClassName());
		params.put("bornDate", classInfo.getBornDate().toString());
		params.put("headerName", classInfo.getHeaderName());
		params.put("action", "update");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "ClassInfoServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 删除班级 */
	public String DeleteClassInfo(String classNo) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("classNo", classNo);
		params.put("action", "delete");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "ClassInfoServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "班级信息删除失败!";
		}
	}

	/* 根据班级编号获取班级对象 */
	public ClassInfo GetClassInfo(String classNo)  {
		List<ClassInfo> classInfoList = new ArrayList<ClassInfo>();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("classNo", classNo);
		params.put("action", "updateQuery");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "ClassInfoServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				ClassInfo classInfo = new ClassInfo();
				classInfo.setClassNo(object.getString("classNo"));
				classInfo.setClassName(object.getString("className"));
				classInfo.setBornDate(Timestamp.valueOf(object.getString("bornDate")));
				classInfo.setHeaderName(object.getString("headerName"));
				classInfoList.add(classInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int size = classInfoList.size();
		if(size>0) return classInfoList.get(0); 
		else return null; 
	}
}
