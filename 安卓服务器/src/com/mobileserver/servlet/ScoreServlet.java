package com.mobileserver.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Timestamp;
import java.util.List;

import com.mobileserver.dao.ScoreDAO;
import com.mobileserver.domain.Score;

import org.json.JSONStringer;

public class ScoreServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/*����ɼ�ҵ������*/
	private ScoreDAO scoreDAO = new ScoreDAO();

	/*Ĭ�Ϲ��캯��*/
	public ScoreServlet() {
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
			/*��ȡ��ѯ�ɼ��Ĳ�����Ϣ*/
			String studentObj = "";
			if (request.getParameter("studentObj") != null)
				studentObj = request.getParameter("studentObj");
			String courseObj = "";
			if (request.getParameter("courseObj") != null)
				courseObj = request.getParameter("courseObj");

			/*����ҵ���߼���ִ�гɼ���ѯ*/
			List<Score> scoreList = scoreDAO.QueryScore(studentObj,courseObj);

			/*2�����ݴ����ʽ��һ����xml�ļ���ʽ������ѯ�Ľ����ͨ��xml��ʽ������ͻ���
			StringBuffer sb = new StringBuffer();
			sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>").append("\r\n")
			.append("<Scores>").append("\r\n");
			for (int i = 0; i < scoreList.size(); i++) {
				sb.append("	<Score>").append("\r\n")
				.append("		<scoreId>")
				.append(scoreList.get(i).getScoreId())
				.append("</scoreId>").append("\r\n")
				.append("		<studentObj>")
				.append(scoreList.get(i).getStudentObj())
				.append("</studentObj>").append("\r\n")
				.append("		<courseObj>")
				.append(scoreList.get(i).getCourseObj())
				.append("</courseObj>").append("\r\n")
				.append("		<courseScore>")
				.append(scoreList.get(i).getCourseScore())
				.append("</courseScore>").append("\r\n")
				.append("		<evaluate>")
				.append(scoreList.get(i).getEvaluate())
				.append("</evaluate>").append("\r\n")
				.append("		<addTime>")
				.append(scoreList.get(i).getAddTime())
				.append("</addTime>").append("\r\n")
				.append("	</Score>").append("\r\n");
			}
			sb.append("</Scores>").append("\r\n");
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(sb.toString());*/
			//��2�ֲ���json��ʽ(����������)�� �ͻ��˲�ѯ��ͼ����󣬷���json���ݸ�ʽ
			JSONStringer stringer = new JSONStringer();
			try {
			  stringer.array();
			  for(Score score: scoreList) {
				  stringer.object();
			  stringer.key("scoreId").value(score.getScoreId());
			  stringer.key("studentObj").value(score.getStudentObj());
			  stringer.key("courseObj").value(score.getCourseObj());
			  stringer.key("courseScore").value(score.getCourseScore());
			  stringer.key("evaluate").value(score.getEvaluate());
			  stringer.key("addTime").value(score.getAddTime());
				  stringer.endObject();
			  }
			  stringer.endArray();
			} catch(Exception e){}
			response.setContentType("text/json; charset=UTF-8");  //JSON������Ϊtext/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if (action.equals("add")) {
			/* ��ӳɼ�����ȡ�ɼ��������������浽�½��ĳɼ����� */ 
			Score score = new Score();
			int scoreId = Integer.parseInt(request.getParameter("scoreId"));
			score.setScoreId(scoreId);
			String studentObj = new String(request.getParameter("studentObj").getBytes("iso-8859-1"), "UTF-8");
			score.setStudentObj(studentObj);
			String courseObj = new String(request.getParameter("courseObj").getBytes("iso-8859-1"), "UTF-8");
			score.setCourseObj(courseObj);
			float courseScore = Float.parseFloat(request.getParameter("courseScore"));
			score.setCourseScore(courseScore);
			String evaluate = new String(request.getParameter("evaluate").getBytes("iso-8859-1"), "UTF-8");
			score.setEvaluate(evaluate);
			String addTime = new String(request.getParameter("addTime").getBytes("iso-8859-1"), "UTF-8");
			score.setAddTime(addTime);

			/* ����ҵ���ִ����Ӳ��� */
			String result = scoreDAO.AddScore(score);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		} else if (action.equals("delete")) {
			/*ɾ���ɼ�����ȡ�ɼ��ĳɼ�id*/
			int scoreId = Integer.parseInt(request.getParameter("scoreId"));
			/*����ҵ���߼���ִ��ɾ������*/
			String result = scoreDAO.DeleteScore(scoreId);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			/*��ɾ���Ƿ�ɹ���Ϣ���ظ��ͻ���*/
			out.print(result);
		} else if (action.equals("updateQuery")) {
			/*���³ɼ�֮ǰ�ȸ���scoreId��ѯĳ���ɼ�*/
			int scoreId = Integer.parseInt(request.getParameter("scoreId"));
			Score score = scoreDAO.GetScore(scoreId);

			// �ͻ��˲�ѯ�ĳɼ����󣬷���json���ݸ�ʽ, ��List<Book>��֯��JSON�ַ���
			JSONStringer stringer = new JSONStringer(); 
			try{
			  stringer.array();
			  stringer.object();
			  stringer.key("scoreId").value(score.getScoreId());
			  stringer.key("studentObj").value(score.getStudentObj());
			  stringer.key("courseObj").value(score.getCourseObj());
			  stringer.key("courseScore").value(score.getCourseScore());
			  stringer.key("evaluate").value(score.getEvaluate());
			  stringer.key("addTime").value(score.getAddTime());
			  stringer.endObject();
			  stringer.endArray();
			}
			catch(Exception e){}
			response.setContentType("text/json; charset=UTF-8");  //JSON������Ϊtext/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if(action.equals("update")) {
			/* ���³ɼ�����ȡ�ɼ��������������浽�½��ĳɼ����� */ 
			Score score = new Score();
			int scoreId = Integer.parseInt(request.getParameter("scoreId"));
			score.setScoreId(scoreId);
			String studentObj = new String(request.getParameter("studentObj").getBytes("iso-8859-1"), "UTF-8");
			score.setStudentObj(studentObj);
			String courseObj = new String(request.getParameter("courseObj").getBytes("iso-8859-1"), "UTF-8");
			score.setCourseObj(courseObj);
			float courseScore = Float.parseFloat(request.getParameter("courseScore"));
			score.setCourseScore(courseScore);
			String evaluate = new String(request.getParameter("evaluate").getBytes("iso-8859-1"), "UTF-8");
			score.setEvaluate(evaluate);
			String addTime = new String(request.getParameter("addTime").getBytes("iso-8859-1"), "UTF-8");
			score.setAddTime(addTime);

			/* ����ҵ���ִ�и��²��� */
			String result = scoreDAO.UpdateScore(score);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		}
	}
}
