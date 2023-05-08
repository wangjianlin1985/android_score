package com.chengxusheji.action;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import com.opensymphony.xwork2.ActionContext;
import com.chengxusheji.dao.CourseDAO;
import com.chengxusheji.domain.Course;
import com.chengxusheji.dao.TeacherDAO;
import com.chengxusheji.domain.Teacher;
import com.chengxusheji.utils.FileTypeException;
import com.chengxusheji.utils.ExportExcelUtil;

@Controller @Scope("prototype")
public class CourseAction extends BaseAction {

    /*�������Ҫ��ѯ������: �γ̱��*/
    private String courseNo;
    public void setCourseNo(String courseNo) {
        this.courseNo = courseNo;
    }
    public String getCourseNo() {
        return this.courseNo;
    }

    /*�������Ҫ��ѯ������: �γ�����*/
    private String courseName;
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    public String getCourseName() {
        return this.courseName;
    }

    /*�������Ҫ��ѯ������: �Ͽ���ʦ*/
    private Teacher teacherObj;
    public void setTeacherObj(Teacher teacherObj) {
        this.teacherObj = teacherObj;
    }
    public Teacher getTeacherObj() {
        return this.teacherObj;
    }

    /*��ǰ�ڼ�ҳ*/
    private int currentPage;
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
    public int getCurrentPage() {
        return currentPage;
    }

    /*һ������ҳ*/
    private int totalPage;
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
    public int getTotalPage() {
        return totalPage;
    }

    /*��ǰ��ѯ���ܼ�¼��Ŀ*/
    private int recordNumber;
    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }
    public int getRecordNumber() {
        return recordNumber;
    }

    /*ҵ������*/
    @Resource TeacherDAO teacherDAO;
    @Resource CourseDAO courseDAO;

    /*��������Course����*/
    private Course course;
    public void setCourse(Course course) {
        this.course = course;
    }
    public Course getCourse() {
        return this.course;
    }

    /*��ת�����Course��ͼ*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        /*��ѯ���е�Teacher��Ϣ*/
        List<Teacher> teacherList = teacherDAO.QueryAllTeacherInfo();
        ctx.put("teacherList", teacherList);
        return "add_view";
    }

    /*���Course��Ϣ*/
    @SuppressWarnings("deprecation")
    public String AddCourse() {
        ActionContext ctx = ActionContext.getContext();
        /*��֤�γ̱���Ƿ��Ѿ�����*/
        String courseNo = course.getCourseNo();
        Course db_course = courseDAO.GetCourseByCourseNo(courseNo);
        if(null != db_course) {
            ctx.put("error",  java.net.URLEncoder.encode("�ÿγ̱���Ѿ�����!"));
            return "error";
        }
        try {
            Teacher teacherObj = teacherDAO.GetTeacherByTeacherNo(course.getTeacherObj().getTeacherNo());
            course.setTeacherObj(teacherObj);
            courseDAO.AddCourse(course);
            ctx.put("message",  java.net.URLEncoder.encode("Course��ӳɹ�!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("ͼƬ�ļ���ʽ����!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Course���ʧ��!"));
            return "error";
        }
    }

    /*��ѯCourse��Ϣ*/
    public String QueryCourse() {
        if(currentPage == 0) currentPage = 1;
        if(courseNo == null) courseNo = "";
        if(courseName == null) courseName = "";
        List<Course> courseList = courseDAO.QueryCourseInfo(courseNo, courseName, teacherObj, currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        courseDAO.CalculateTotalPageAndRecordNumber(courseNo, courseName, teacherObj);
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = courseDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
        recordNumber = courseDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("courseList",  courseList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("courseNo", courseNo);
        ctx.put("courseName", courseName);
        ctx.put("teacherObj", teacherObj);
        List<Teacher> teacherList = teacherDAO.QueryAllTeacherInfo();
        ctx.put("teacherList", teacherList);
        return "query_view";
    }

    /*��̨������excel*/
    public String QueryCourseOutputToExcel() { 
        if(courseNo == null) courseNo = "";
        if(courseName == null) courseName = "";
        List<Course> courseList = courseDAO.QueryCourseInfo(courseNo,courseName,teacherObj);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "Course��Ϣ��¼"; 
        String[] headers = { "�γ̱��","�γ�����","�Ͽ���ʦ","�Ͽεص�","�Ͽ�ʱ��","��ѧʱ","�γ�ѧ��"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<courseList.size();i++) {
        	Course course = courseList.get(i); 
        	dataset.add(new String[]{course.getCourseNo(),course.getCourseName(),course.getTeacherObj().getName(),
course.getCoursePlace(),course.getCourseTime(),course.getCourseHours() + "",course.getCourseScore() + ""});
        }
        /*
        OutputStream out = null;
		try {
			out = new FileOutputStream("C://output.xls");
			ex.exportExcel(title,headers, dataset, out);
		    out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
		HttpServletResponse response = null;//����һ��HttpServletResponse���� 
		OutputStream out = null;//����һ����������� 
		try { 
			response = ServletActionContext.getResponse();//��ʼ��HttpServletResponse���� 
			out = response.getOutputStream();//
			response.setHeader("Content-disposition","attachment; filename="+"Course.xls");//filename�����ص�xls���������������Ӣ�� 
			response.setContentType("application/msexcel;charset=UTF-8");//�������� 
			response.setHeader("Pragma","No-cache");//����ͷ 
			response.setHeader("Cache-Control","no-cache");//����ͷ 
			response.setDateHeader("Expires", 0);//��������ͷ  
			String rootPath = ServletActionContext.getServletContext().getRealPath("/");
			ex.exportExcel(rootPath,title,headers, dataset, out);
			out.flush();
		} catch (IOException e) { 
			e.printStackTrace(); 
		}finally{
			try{
				if(out!=null){ 
					out.close(); 
				}
			}catch(IOException e){ 
				e.printStackTrace(); 
			} 
		}
		return null;
    }
    /*ǰ̨��ѯCourse��Ϣ*/
    public String FrontQueryCourse() {
        if(currentPage == 0) currentPage = 1;
        if(courseNo == null) courseNo = "";
        if(courseName == null) courseName = "";
        List<Course> courseList = courseDAO.QueryCourseInfo(courseNo, courseName, teacherObj, currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        courseDAO.CalculateTotalPageAndRecordNumber(courseNo, courseName, teacherObj);
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = courseDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
        recordNumber = courseDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("courseList",  courseList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("courseNo", courseNo);
        ctx.put("courseName", courseName);
        ctx.put("teacherObj", teacherObj);
        List<Teacher> teacherList = teacherDAO.QueryAllTeacherInfo();
        ctx.put("teacherList", teacherList);
        return "front_query_view";
    }

    /*��ѯҪ�޸ĵ�Course��Ϣ*/
    public String ModifyCourseQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������courseNo��ȡCourse����*/
        Course course = courseDAO.GetCourseByCourseNo(courseNo);

        List<Teacher> teacherList = teacherDAO.QueryAllTeacherInfo();
        ctx.put("teacherList", teacherList);
        ctx.put("course",  course);
        return "modify_view";
    }

    /*��ѯҪ�޸ĵ�Course��Ϣ*/
    public String FrontShowCourseQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������courseNo��ȡCourse����*/
        Course course = courseDAO.GetCourseByCourseNo(courseNo);

        List<Teacher> teacherList = teacherDAO.QueryAllTeacherInfo();
        ctx.put("teacherList", teacherList);
        ctx.put("course",  course);
        return "front_show_view";
    }

    /*�����޸�Course��Ϣ*/
    public String ModifyCourse() {
        ActionContext ctx = ActionContext.getContext();
        try {
            Teacher teacherObj = teacherDAO.GetTeacherByTeacherNo(course.getTeacherObj().getTeacherNo());
            course.setTeacherObj(teacherObj);
            courseDAO.UpdateCourse(course);
            ctx.put("message",  java.net.URLEncoder.encode("Course��Ϣ���³ɹ�!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Course��Ϣ����ʧ��!"));
            return "error";
       }
   }

    /*ɾ��Course��Ϣ*/
    public String DeleteCourse() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            courseDAO.DeleteCourse(courseNo);
            ctx.put("message",  java.net.URLEncoder.encode("Courseɾ���ɹ�!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Courseɾ��ʧ��!"));
            return "error";
        }
    }

}
