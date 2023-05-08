package com.mobileserver.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Timestamp;
import java.util.List;

import com.mobileserver.dao.KejianDAO;
import com.mobileserver.domain.Kejian;

import org.json.JSONStringer;

public class KejianServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/*����μ�ҵ������*/
	private KejianDAO kejianDAO = new KejianDAO();

	/*Ĭ�Ϲ��캯��*/
	public KejianServlet() {
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
			/*��ȡ��ѯ�μ��Ĳ�����Ϣ*/
			String title = request.getParameter("title");
			title = title == null ? "" : new String(request.getParameter(
					"title").getBytes("iso-8859-1"), "UTF-8");
			String teacherObj = "";
			if (request.getParameter("teacherObj") != null)
				teacherObj = request.getParameter("teacherObj");
			String uploadTime = request.getParameter("uploadTime");
			uploadTime = uploadTime == null ? "" : new String(request.getParameter(
					"uploadTime").getBytes("iso-8859-1"), "UTF-8");

			/*����ҵ���߼���ִ�пμ���ѯ*/
			List<Kejian> kejianList = kejianDAO.QueryKejian(title,teacherObj,uploadTime);

			/*2�����ݴ����ʽ��һ����xml�ļ���ʽ������ѯ�Ľ����ͨ��xml��ʽ������ͻ���
			StringBuffer sb = new StringBuffer();
			sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>").append("\r\n")
			.append("<Kejians>").append("\r\n");
			for (int i = 0; i < kejianList.size(); i++) {
				sb.append("	<Kejian>").append("\r\n")
				.append("		<kejianId>")
				.append(kejianList.get(i).getKejianId())
				.append("</kejianId>").append("\r\n")
				.append("		<title>")
				.append(kejianList.get(i).getTitle())
				.append("</title>").append("\r\n")
				.append("		<content>")
				.append(kejianList.get(i).getContent())
				.append("</content>").append("\r\n")
				.append("		<kejianFile>")
				.append(kejianList.get(i).getKejianFile())
				.append("</kejianFile>").append("\r\n")
				.append("		<teacherObj>")
				.append(kejianList.get(i).getTeacherObj())
				.append("</teacherObj>").append("\r\n")
				.append("		<uploadTime>")
				.append(kejianList.get(i).getUploadTime())
				.append("</uploadTime>").append("\r\n")
				.append("	</Kejian>").append("\r\n");
			}
			sb.append("</Kejians>").append("\r\n");
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(sb.toString());*/
			//��2�ֲ���json��ʽ(����������)�� �ͻ��˲�ѯ��ͼ����󣬷���json���ݸ�ʽ
			JSONStringer stringer = new JSONStringer();
			try {
			  stringer.array();
			  for(Kejian kejian: kejianList) {
				  stringer.object();
			  stringer.key("kejianId").value(kejian.getKejianId());
			  stringer.key("title").value(kejian.getTitle());
			  stringer.key("content").value(kejian.getContent());
			  stringer.key("kejianFile").value(kejian.getKejianFile());
			  stringer.key("teacherObj").value(kejian.getTeacherObj());
			  stringer.key("uploadTime").value(kejian.getUploadTime());
				  stringer.endObject();
			  }
			  stringer.endArray();
			} catch(Exception e){}
			response.setContentType("text/json; charset=UTF-8");  //JSON������Ϊtext/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if (action.equals("add")) {
			/* ��ӿμ�����ȡ�μ��������������浽�½��Ŀμ����� */ 
			Kejian kejian = new Kejian();
			int kejianId = Integer.parseInt(request.getParameter("kejianId"));
			kejian.setKejianId(kejianId);
			String title = new String(request.getParameter("title").getBytes("iso-8859-1"), "UTF-8");
			kejian.setTitle(title);
			String content = new String(request.getParameter("content").getBytes("iso-8859-1"), "UTF-8");
			kejian.setContent(content);
			String kejianFile = new String(request.getParameter("kejianFile").getBytes("iso-8859-1"), "UTF-8");
			kejian.setKejianFile(kejianFile);
			String teacherObj = new String(request.getParameter("teacherObj").getBytes("iso-8859-1"), "UTF-8");
			kejian.setTeacherObj(teacherObj);
			String uploadTime = new String(request.getParameter("uploadTime").getBytes("iso-8859-1"), "UTF-8");
			kejian.setUploadTime(uploadTime);

			/* ����ҵ���ִ����Ӳ��� */
			String result = kejianDAO.AddKejian(kejian);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		} else if (action.equals("delete")) {
			/*ɾ���μ�����ȡ�μ��Ŀμ�id*/
			int kejianId = Integer.parseInt(request.getParameter("kejianId"));
			/*����ҵ���߼���ִ��ɾ������*/
			String result = kejianDAO.DeleteKejian(kejianId);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			/*��ɾ���Ƿ�ɹ���Ϣ���ظ��ͻ���*/
			out.print(result);
		} else if (action.equals("updateQuery")) {
			/*���¿μ�֮ǰ�ȸ���kejianId��ѯĳ���μ�*/
			int kejianId = Integer.parseInt(request.getParameter("kejianId"));
			Kejian kejian = kejianDAO.GetKejian(kejianId);

			// �ͻ��˲�ѯ�Ŀμ����󣬷���json���ݸ�ʽ, ��List<Book>��֯��JSON�ַ���
			JSONStringer stringer = new JSONStringer(); 
			try{
			  stringer.array();
			  stringer.object();
			  stringer.key("kejianId").value(kejian.getKejianId());
			  stringer.key("title").value(kejian.getTitle());
			  stringer.key("content").value(kejian.getContent());
			  stringer.key("kejianFile").value(kejian.getKejianFile());
			  stringer.key("teacherObj").value(kejian.getTeacherObj());
			  stringer.key("uploadTime").value(kejian.getUploadTime());
			  stringer.endObject();
			  stringer.endArray();
			}
			catch(Exception e){}
			response.setContentType("text/json; charset=UTF-8");  //JSON������Ϊtext/json
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
		} else if(action.equals("update")) {
			/* ���¿μ�����ȡ�μ��������������浽�½��Ŀμ����� */ 
			Kejian kejian = new Kejian();
			int kejianId = Integer.parseInt(request.getParameter("kejianId"));
			kejian.setKejianId(kejianId);
			String title = new String(request.getParameter("title").getBytes("iso-8859-1"), "UTF-8");
			kejian.setTitle(title);
			String content = new String(request.getParameter("content").getBytes("iso-8859-1"), "UTF-8");
			kejian.setContent(content);
			String kejianFile = new String(request.getParameter("kejianFile").getBytes("iso-8859-1"), "UTF-8");
			kejian.setKejianFile(kejianFile);
			String teacherObj = new String(request.getParameter("teacherObj").getBytes("iso-8859-1"), "UTF-8");
			kejian.setTeacherObj(teacherObj);
			String uploadTime = new String(request.getParameter("uploadTime").getBytes("iso-8859-1"), "UTF-8");
			kejian.setUploadTime(uploadTime);

			/* ����ҵ���ִ�и��²��� */
			String result = kejianDAO.UpdateKejian(kejian);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		}
	}
}
