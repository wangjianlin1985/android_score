package com.mobileserver.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Timestamp;
import java.util.List;

import com.mobileserver.dao.CourseDAO;
import com.mobileserver.domain.Course;

import org.json.JSONStringer;

public class CourseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/*����γ�ҵ������*/
	private CourseDAO courseDAO = new CourseDAO();

	/*Ĭ�Ϲ��캯��*/
	public CourseServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		/*��ȡaction����������action��ִֵ�в�ͬ��ҵ����*/
		String action = request.getParameter("action");
		if (action.equals("query")) {
			/*��ȡ��ѯ�γ̵Ĳ�����Ϣ*/
			String courseNo = request.getParameter("courseNo");
			courseNo = courseNo == null ? "" : new String(request.getParameter(
					"courseNo").getBytes("iso-8859-1"), "UTF-8");
			String courseName = request.getParameter("courseName");
			courseName = courseName == null ? "" : new String(request.getParameter(
					"courseName").getBytes("iso-8859-1"), "UTF-8");
			String teacherObj = "";
			if (request.getParameter("teacherObj") != null)
				teacherObj = request.getParameter("teacherObj");

			/*����ҵ���߼���ִ�пγ̲�ѯ*/
			List<Course> courseList = courseDAO.QueryCourse(courseNo,courseName,teacherObj);

			/*2�����ݴ����ʽ��һ����xml�ļ���ʽ������ѯ�Ľ����ͨ��xml��ʽ������ͻ���
			StringBuffer sb = new StringBuffer();
			sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>").append("\r\n")
			.append("<Courses>").append("\r\n");
			for (int i = 0; i < courseList.size(); i++) {
				sb.append("	<Course>").append("\r\n")
				.append("		<courseNo>")
				.append(courseList.get(i).getCourseNo())
				.append("</courseNo>").append("\r\n")
				.append("		<courseName>")
				.append(courseList.get(i).getCourseName())
				.append("</courseName>").append("\r\n")
				.append("		<teacherObj>")
				.append(courseList.get(i).getTeacherObj())
				.append("</teacherObj>").append("\r\n")
				.append("		<coursePlace>")
				.append(courseList.get(i).getCoursePlace())
				.append("</coursePlace>").append("\r\n")
				.append("		<courseTime>")
				.append(courseList.get(i).getCourseTime())
				.append("</courseTime>").append("\r\n")
				.append("		<courseHours>")
				.append(courseList.get(i).getCourseHours())
				.append("</courseHours>").append("\r\n")
				.append("		<courseScore>")
				.append(courseList.get(i).getCourseScore())
				.append("</courseScore>").append("\r\n")
				.append("		<memo>")
				.append(courseList.get(i).getMemo())
				.append("</memo>").append("\r\n")
				.append("	</Course>").append("\r\n");
			}
			sb.append("</Courses>").append("\r\n");
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(sb.toString());*/
			//��2�ֲ���json��ʽ(����������)�� �ͻ��˲�ѯ��ͼ����󣬷���json���ݸ�ʽ
			JSONStringer stringer = new JSONStringer();
			try {
			  stringer.array();
			  for(Course course: courseList) {
				  stringer.object();
			  stringer.key("courseNo").value(course.getCourseNo());
			  stringer.key("courseName").value(course.getCourseName());
			  stringer.key("teacherObj").value(course.getTeacherObj());
			  stringer.key("coursePlace").value(course.getCoursePlace());
			  stringer.key("courseTime").value(course.getCourseTime());
			  stringer.key("courseHours").value(course.getCourseHours());
			  stringer.key("courseScore").value(course.getCourseScore());
			  stringer.key("memo").value(course.getMemo());
				  stringer.endObject();
			  }
			  stringer.endArray();
			} catch(Exception e){}
			response.setContentType("text/json; charset=UTF-8");  //JSON������Ϊtext/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if (action.equals("add")) {
			/* ��ӿγ̣���ȡ�γ̲������������浽�½��Ŀγ̶��� */ 
			Course course = new Course();
			String courseNo = new String(request.getParameter("courseNo").getBytes("iso-8859-1"), "UTF-8");
			course.setCourseNo(courseNo);
			String courseName = new String(request.getParameter("courseName").getBytes("iso-8859-1"), "UTF-8");
			course.setCourseName(courseName);
			String teacherObj = new String(request.getParameter("teacherObj").getBytes("iso-8859-1"), "UTF-8");
			course.setTeacherObj(teacherObj);
			String coursePlace = new String(request.getParameter("coursePlace").getBytes("iso-8859-1"), "UTF-8");
			course.setCoursePlace(coursePlace);
			String courseTime = new String(request.getParameter("courseTime").getBytes("iso-8859-1"), "UTF-8");
			course.setCourseTime(courseTime);
			int courseHours = Integer.parseInt(request.getParameter("courseHours"));
			course.setCourseHours(courseHours);
			float courseScore = Float.parseFloat(request.getParameter("courseScore"));
			course.setCourseScore(courseScore);
			String memo = new String(request.getParameter("memo").getBytes("iso-8859-1"), "UTF-8");
			course.setMemo(memo);

			/* ����ҵ���ִ����Ӳ��� */
			String result = courseDAO.AddCourse(course);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		} else if (action.equals("delete")) {
			/*ɾ���γ̣���ȡ�γ̵Ŀγ̱��*/
			String courseNo = new String(request.getParameter("courseNo").getBytes("iso-8859-1"), "UTF-8");
			/*����ҵ���߼���ִ��ɾ������*/
			String result = courseDAO.DeleteCourse(courseNo);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			/*��ɾ���Ƿ�ɹ���Ϣ���ظ��ͻ���*/
			out.print(result);
		} else if (action.equals("updateQuery")) {
			/*���¿γ�֮ǰ�ȸ���courseNo��ѯĳ���γ�*/
			String courseNo = new String(request.getParameter("courseNo").getBytes("iso-8859-1"), "UTF-8");
			Course course = courseDAO.GetCourse(courseNo);

			// �ͻ��˲�ѯ�Ŀγ̶��󣬷���json���ݸ�ʽ, ��List<Book>��֯��JSON�ַ���
			JSONStringer stringer = new JSONStringer(); 
			try{
			  stringer.array();
			  stringer.object();
			  stringer.key("courseNo").value(course.getCourseNo());
			  stringer.key("courseName").value(course.getCourseName());
			  stringer.key("teacherObj").value(course.getTeacherObj());
			  stringer.key("coursePlace").value(course.getCoursePlace());
			  stringer.key("courseTime").value(course.getCourseTime());
			  stringer.key("courseHours").value(course.getCourseHours());
			  stringer.key("courseScore").value(course.getCourseScore());
			  stringer.key("memo").value(course.getMemo());
			  stringer.endObject();
			  stringer.endArray();
			}
			catch(Exception e){}
			response.setContentType("text/json; charset=UTF-8");  //JSON������Ϊtext/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if(action.equals("update")) {
			/* ���¿γ̣���ȡ�γ̲������������浽�½��Ŀγ̶��� */ 
			Course course = new Course();
			String courseNo = new String(request.getParameter("courseNo").getBytes("iso-8859-1"), "UTF-8");
			course.setCourseNo(courseNo);
			String courseName = new String(request.getParameter("courseName").getBytes("iso-8859-1"), "UTF-8");
			course.setCourseName(courseName);
			String teacherObj = new String(request.getParameter("teacherObj").getBytes("iso-8859-1"), "UTF-8");
			course.setTeacherObj(teacherObj);
			String coursePlace = new String(request.getParameter("coursePlace").getBytes("iso-8859-1"), "UTF-8");
			course.setCoursePlace(coursePlace);
			String courseTime = new String(request.getParameter("courseTime").getBytes("iso-8859-1"), "UTF-8");
			course.setCourseTime(courseTime);
			int courseHours = Integer.parseInt(request.getParameter("courseHours"));
			course.setCourseHours(courseHours);
			float courseScore = Float.parseFloat(request.getParameter("courseScore"));
			course.setCourseScore(courseScore);
			String memo = new String(request.getParameter("memo").getBytes("iso-8859-1"), "UTF-8");
			course.setMemo(memo);

			/* ����ҵ���ִ�и��²��� */
			String result = courseDAO.UpdateCourse(course);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		}
	}
}
