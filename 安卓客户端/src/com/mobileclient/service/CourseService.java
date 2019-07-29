package com.mobileclient.service;

import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mobileclient.domain.Course;
import com.mobileclient.util.HttpUtil;

/*课程管理业务逻辑层*/
public class CourseService {
	/* 添加课程 */
	public String AddCourse(Course course) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("courseNo", course.getCourseNo());
		params.put("courseName", course.getCourseName());
		params.put("teacherObj", course.getTeacherObj());
		params.put("coursePlace", course.getCoursePlace());
		params.put("courseTime", course.getCourseTime());
		params.put("courseHours", course.getCourseHours() + "");
		params.put("courseScore", course.getCourseScore() + "");
		params.put("memo", course.getMemo());
		params.put("action", "add");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "CourseServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 查询课程 */
	public List<Course> QueryCourse(Course queryConditionCourse) throws Exception {
		String urlString = HttpUtil.BASE_URL + "CourseServlet?action=query";
		if(queryConditionCourse != null) {
			urlString += "&courseNo=" + URLEncoder.encode(queryConditionCourse.getCourseNo(), "UTF-8") + "";
			urlString += "&courseName=" + URLEncoder.encode(queryConditionCourse.getCourseName(), "UTF-8") + "";
			urlString += "&teacherObj=" + URLEncoder.encode(queryConditionCourse.getTeacherObj(), "UTF-8") + "";
		}

		/* 2种数据解析方法，第一种是用SAXParser解析xml文件格式
		URL url = new URL(urlString);
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		XMLReader xr = sp.getXMLReader();

		CourseListHandler courseListHander = new CourseListHandler();
		xr.setContentHandler(courseListHander);
		InputStreamReader isr = new InputStreamReader(url.openStream(), "UTF-8");
		InputSource is = new InputSource(isr);
		xr.parse(is);
		List<Course> courseList = courseListHander.getCourseList();
		return courseList;*/
		//第2种是基于json数据格式解析，我们采用的是第2种
		List<Course> courseList = new ArrayList<Course>();
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(urlString, null, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				Course course = new Course();
				course.setCourseNo(object.getString("courseNo"));
				course.setCourseName(object.getString("courseName"));
				course.setTeacherObj(object.getString("teacherObj"));
				course.setCoursePlace(object.getString("coursePlace"));
				course.setCourseTime(object.getString("courseTime"));
				course.setCourseHours(object.getInt("courseHours"));
				course.setCourseScore((float) object.getDouble("courseScore"));
				course.setMemo(object.getString("memo"));
				courseList.add(course);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return courseList;
	}

	/* 更新课程 */
	public String UpdateCourse(Course course) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("courseNo", course.getCourseNo());
		params.put("courseName", course.getCourseName());
		params.put("teacherObj", course.getTeacherObj());
		params.put("coursePlace", course.getCoursePlace());
		params.put("courseTime", course.getCourseTime());
		params.put("courseHours", course.getCourseHours() + "");
		params.put("courseScore", course.getCourseScore() + "");
		params.put("memo", course.getMemo());
		params.put("action", "update");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "CourseServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 删除课程 */
	public String DeleteCourse(String courseNo) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("courseNo", courseNo);
		params.put("action", "delete");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "CourseServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "课程信息删除失败!";
		}
	}

	/* 根据课程编号获取课程对象 */
	public Course GetCourse(String courseNo)  {
		List<Course> courseList = new ArrayList<Course>();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("courseNo", courseNo);
		params.put("action", "updateQuery");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "CourseServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				Course course = new Course();
				course.setCourseNo(object.getString("courseNo"));
				course.setCourseName(object.getString("courseName"));
				course.setTeacherObj(object.getString("teacherObj"));
				course.setCoursePlace(object.getString("coursePlace"));
				course.setCourseTime(object.getString("courseTime"));
				course.setCourseHours(object.getInt("courseHours"));
				course.setCourseScore((float) object.getDouble("courseScore"));
				course.setMemo(object.getString("memo"));
				courseList.add(course);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int size = courseList.size();
		if(size>0) return courseList.get(0); 
		else return null; 
	}
}
