package com.mobileclient.service;

import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mobileclient.domain.Teacher;
import com.mobileclient.util.HttpUtil;

/*老师管理业务逻辑层*/
public class TeacherService {
	/* 添加老师 */
	public String AddTeacher(Teacher teacher) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("teacherNo", teacher.getTeacherNo());
		params.put("password", teacher.getPassword());
		params.put("name", teacher.getName());
		params.put("sex", teacher.getSex());
		params.put("birthday", teacher.getBirthday().toString());
		params.put("telephone", teacher.getTelephone());
		params.put("email", teacher.getEmail());
		params.put("address", teacher.getAddress());
		params.put("memo", teacher.getMemo());
		params.put("action", "add");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "TeacherServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 查询老师 */
	public List<Teacher> QueryTeacher(Teacher queryConditionTeacher) throws Exception {
		String urlString = HttpUtil.BASE_URL + "TeacherServlet?action=query";
		if(queryConditionTeacher != null) {
			urlString += "&teacherNo=" + URLEncoder.encode(queryConditionTeacher.getTeacherNo(), "UTF-8") + "";
			urlString += "&name=" + URLEncoder.encode(queryConditionTeacher.getName(), "UTF-8") + "";
			if(queryConditionTeacher.getBirthday() != null) {
				urlString += "&birthday=" + URLEncoder.encode(queryConditionTeacher.getBirthday().toString(), "UTF-8");
			}
			urlString += "&telephone=" + URLEncoder.encode(queryConditionTeacher.getTelephone(), "UTF-8") + "";
		}

		/* 2种数据解析方法，第一种是用SAXParser解析xml文件格式
		URL url = new URL(urlString);
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		XMLReader xr = sp.getXMLReader();

		TeacherListHandler teacherListHander = new TeacherListHandler();
		xr.setContentHandler(teacherListHander);
		InputStreamReader isr = new InputStreamReader(url.openStream(), "UTF-8");
		InputSource is = new InputSource(isr);
		xr.parse(is);
		List<Teacher> teacherList = teacherListHander.getTeacherList();
		return teacherList;*/
		//第2种是基于json数据格式解析，我们采用的是第2种
		List<Teacher> teacherList = new ArrayList<Teacher>();
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(urlString, null, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				Teacher teacher = new Teacher();
				teacher.setTeacherNo(object.getString("teacherNo"));
				teacher.setPassword(object.getString("password"));
				teacher.setName(object.getString("name"));
				teacher.setSex(object.getString("sex"));
				teacher.setBirthday(Timestamp.valueOf(object.getString("birthday")));
				teacher.setTelephone(object.getString("telephone"));
				teacher.setEmail(object.getString("email"));
				teacher.setAddress(object.getString("address"));
				teacher.setMemo(object.getString("memo"));
				teacherList.add(teacher);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return teacherList;
	}

	/* 更新老师 */
	public String UpdateTeacher(Teacher teacher) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("teacherNo", teacher.getTeacherNo());
		params.put("password", teacher.getPassword());
		params.put("name", teacher.getName());
		params.put("sex", teacher.getSex());
		params.put("birthday", teacher.getBirthday().toString());
		params.put("telephone", teacher.getTelephone());
		params.put("email", teacher.getEmail());
		params.put("address", teacher.getAddress());
		params.put("memo", teacher.getMemo());
		params.put("action", "update");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "TeacherServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 删除老师 */
	public String DeleteTeacher(String teacherNo) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("teacherNo", teacherNo);
		params.put("action", "delete");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "TeacherServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "老师信息删除失败!";
		}
	}

	/* 根据教师编号获取老师对象 */
	public Teacher GetTeacher(String teacherNo)  {
		List<Teacher> teacherList = new ArrayList<Teacher>();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("teacherNo", teacherNo);
		params.put("action", "updateQuery");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "TeacherServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				Teacher teacher = new Teacher();
				teacher.setTeacherNo(object.getString("teacherNo"));
				teacher.setPassword(object.getString("password"));
				teacher.setName(object.getString("name"));
				teacher.setSex(object.getString("sex"));
				teacher.setBirthday(Timestamp.valueOf(object.getString("birthday")));
				teacher.setTelephone(object.getString("telephone"));
				teacher.setEmail(object.getString("email"));
				teacher.setAddress(object.getString("address"));
				teacher.setMemo(object.getString("memo"));
				teacherList.add(teacher);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int size = teacherList.size();
		if(size>0) return teacherList.get(0); 
		else return null; 
	}
}
