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

	/*构造成绩业务层对象*/
	private ScoreDAO scoreDAO = new ScoreDAO();

	/*默认构造函数*/
	public ScoreServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		/*获取action参数，根据action的值执行不同的业务处理*/
		String action = request.getParameter("action");
		if (action.equals("query")) {
			/*获取查询成绩的参数信息*/
			String studentObj = "";
			if (request.getParameter("studentObj") != null)
				studentObj = request.getParameter("studentObj");
			String courseObj = "";
			if (request.getParameter("courseObj") != null)
				courseObj = request.getParameter("courseObj");

			/*调用业务逻辑层执行成绩查询*/
			List<Score> scoreList = scoreDAO.QueryScore(studentObj,courseObj);

			/*2种数据传输格式，一种是xml文件格式：将查询的结果集通过xml格式传输给客户端
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
			//第2种采用json格式(我们用这种)： 客户端查询的图书对象，返回json数据格式
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
			response.setContentType("text/json; charset=UTF-8");  //JSON的类型为text/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if (action.equals("add")) {
			/* 添加成绩：获取成绩参数，参数保存到新建的成绩对象 */ 
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

			/* 调用业务层执行添加操作 */
			String result = scoreDAO.AddScore(score);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		} else if (action.equals("delete")) {
			/*删除成绩：获取成绩的成绩id*/
			int scoreId = Integer.parseInt(request.getParameter("scoreId"));
			/*调用业务逻辑层执行删除操作*/
			String result = scoreDAO.DeleteScore(scoreId);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			/*将删除是否成功信息返回给客户端*/
			out.print(result);
		} else if (action.equals("updateQuery")) {
			/*更新成绩之前先根据scoreId查询某个成绩*/
			int scoreId = Integer.parseInt(request.getParameter("scoreId"));
			Score score = scoreDAO.GetScore(scoreId);

			// 客户端查询的成绩对象，返回json数据格式, 将List<Book>组织成JSON字符串
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
			response.setContentType("text/json; charset=UTF-8");  //JSON的类型为text/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if(action.equals("update")) {
			/* 更新成绩：获取成绩参数，参数保存到新建的成绩对象 */ 
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

			/* 调用业务层执行更新操作 */
			String result = scoreDAO.UpdateScore(score);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		}
	}
}
