package com.mobileserver.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Timestamp;
import java.util.List;

import com.mobileserver.dao.ClassInfoDAO;
import com.mobileserver.domain.ClassInfo;

import org.json.JSONStringer;

public class ClassInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/*构造班级业务层对象*/
	private ClassInfoDAO classInfoDAO = new ClassInfoDAO();

	/*默认构造函数*/
	public ClassInfoServlet() {
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
			/*获取查询班级的参数信息*/
			String classNo = request.getParameter("classNo");
			classNo = classNo == null ? "" : new String(request.getParameter(
					"classNo").getBytes("iso-8859-1"), "UTF-8");
			String className = request.getParameter("className");
			className = className == null ? "" : new String(request.getParameter(
					"className").getBytes("iso-8859-1"), "UTF-8");
			Timestamp bornDate = null;
			if (request.getParameter("bornDate") != null)
				bornDate = Timestamp.valueOf(request.getParameter("bornDate"));

			/*调用业务逻辑层执行班级查询*/
			List<ClassInfo> classInfoList = classInfoDAO.QueryClassInfo(classNo,className,bornDate);

			/*2种数据传输格式，一种是xml文件格式：将查询的结果集通过xml格式传输给客户端
			StringBuffer sb = new StringBuffer();
			sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>").append("\r\n")
			.append("<ClassInfos>").append("\r\n");
			for (int i = 0; i < classInfoList.size(); i++) {
				sb.append("	<ClassInfo>").append("\r\n")
				.append("		<classNo>")
				.append(classInfoList.get(i).getClassNo())
				.append("</classNo>").append("\r\n")
				.append("		<className>")
				.append(classInfoList.get(i).getClassName())
				.append("</className>").append("\r\n")
				.append("		<bornDate>")
				.append(classInfoList.get(i).getBornDate())
				.append("</bornDate>").append("\r\n")
				.append("		<headerName>")
				.append(classInfoList.get(i).getHeaderName())
				.append("</headerName>").append("\r\n")
				.append("	</ClassInfo>").append("\r\n");
			}
			sb.append("</ClassInfos>").append("\r\n");
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(sb.toString());*/
			//第2种采用json格式(我们用这种)： 客户端查询的图书对象，返回json数据格式
			JSONStringer stringer = new JSONStringer();
			try {
			  stringer.array();
			  for(ClassInfo classInfo: classInfoList) {
				  stringer.object();
			  stringer.key("classNo").value(classInfo.getClassNo());
			  stringer.key("className").value(classInfo.getClassName());
			  stringer.key("bornDate").value(classInfo.getBornDate());
			  stringer.key("headerName").value(classInfo.getHeaderName());
				  stringer.endObject();
			  }
			  stringer.endArray();
			} catch(Exception e){}
			response.setContentType("text/json; charset=UTF-8");  //JSON的类型为text/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if (action.equals("add")) {
			/* 添加班级：获取班级参数，参数保存到新建的班级对象 */ 
			ClassInfo classInfo = new ClassInfo();
			String classNo = new String(request.getParameter("classNo").getBytes("iso-8859-1"), "UTF-8");
			classInfo.setClassNo(classNo);
			String className = new String(request.getParameter("className").getBytes("iso-8859-1"), "UTF-8");
			classInfo.setClassName(className);
			Timestamp bornDate = Timestamp.valueOf(request.getParameter("bornDate"));
			classInfo.setBornDate(bornDate);
			String headerName = new String(request.getParameter("headerName").getBytes("iso-8859-1"), "UTF-8");
			classInfo.setHeaderName(headerName);

			/* 调用业务层执行添加操作 */
			String result = classInfoDAO.AddClassInfo(classInfo);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		} else if (action.equals("delete")) {
			/*删除班级：获取班级的班级编号*/
			String classNo = new String(request.getParameter("classNo").getBytes("iso-8859-1"), "UTF-8");
			/*调用业务逻辑层执行删除操作*/
			String result = classInfoDAO.DeleteClassInfo(classNo);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			/*将删除是否成功信息返回给客户端*/
			out.print(result);
		} else if (action.equals("updateQuery")) {
			/*更新班级之前先根据classNo查询某个班级*/
			String classNo = new String(request.getParameter("classNo").getBytes("iso-8859-1"), "UTF-8");
			ClassInfo classInfo = classInfoDAO.GetClassInfo(classNo);

			// 客户端查询的班级对象，返回json数据格式, 将List<Book>组织成JSON字符串
			JSONStringer stringer = new JSONStringer(); 
			try{
			  stringer.array();
			  stringer.object();
			  stringer.key("classNo").value(classInfo.getClassNo());
			  stringer.key("className").value(classInfo.getClassName());
			  stringer.key("bornDate").value(classInfo.getBornDate());
			  stringer.key("headerName").value(classInfo.getHeaderName());
			  stringer.endObject();
			  stringer.endArray();
			}
			catch(Exception e){}
			response.setContentType("text/json; charset=UTF-8");  //JSON的类型为text/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if(action.equals("update")) {
			/* 更新班级：获取班级参数，参数保存到新建的班级对象 */ 
			ClassInfo classInfo = new ClassInfo();
			String classNo = new String(request.getParameter("classNo").getBytes("iso-8859-1"), "UTF-8");
			classInfo.setClassNo(classNo);
			String className = new String(request.getParameter("className").getBytes("iso-8859-1"), "UTF-8");
			classInfo.setClassName(className);
			Timestamp bornDate = Timestamp.valueOf(request.getParameter("bornDate"));
			classInfo.setBornDate(bornDate);
			String headerName = new String(request.getParameter("headerName").getBytes("iso-8859-1"), "UTF-8");
			classInfo.setHeaderName(headerName);

			/* 调用业务层执行更新操作 */
			String result = classInfoDAO.UpdateClassInfo(classInfo);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		}
	}
}
