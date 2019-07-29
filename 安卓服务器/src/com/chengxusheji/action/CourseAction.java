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

    /*界面层需要查询的属性: 课程编号*/
    private String courseNo;
    public void setCourseNo(String courseNo) {
        this.courseNo = courseNo;
    }
    public String getCourseNo() {
        return this.courseNo;
    }

    /*界面层需要查询的属性: 课程名称*/
    private String courseName;
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    public String getCourseName() {
        return this.courseName;
    }

    /*界面层需要查询的属性: 上课老师*/
    private Teacher teacherObj;
    public void setTeacherObj(Teacher teacherObj) {
        this.teacherObj = teacherObj;
    }
    public Teacher getTeacherObj() {
        return this.teacherObj;
    }

    /*当前第几页*/
    private int currentPage;
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
    public int getCurrentPage() {
        return currentPage;
    }

    /*一共多少页*/
    private int totalPage;
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
    public int getTotalPage() {
        return totalPage;
    }

    /*当前查询的总记录数目*/
    private int recordNumber;
    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }
    public int getRecordNumber() {
        return recordNumber;
    }

    /*业务层对象*/
    @Resource TeacherDAO teacherDAO;
    @Resource CourseDAO courseDAO;

    /*待操作的Course对象*/
    private Course course;
    public void setCourse(Course course) {
        this.course = course;
    }
    public Course getCourse() {
        return this.course;
    }

    /*跳转到添加Course视图*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        /*查询所有的Teacher信息*/
        List<Teacher> teacherList = teacherDAO.QueryAllTeacherInfo();
        ctx.put("teacherList", teacherList);
        return "add_view";
    }

    /*添加Course信息*/
    @SuppressWarnings("deprecation")
    public String AddCourse() {
        ActionContext ctx = ActionContext.getContext();
        /*验证课程编号是否已经存在*/
        String courseNo = course.getCourseNo();
        Course db_course = courseDAO.GetCourseByCourseNo(courseNo);
        if(null != db_course) {
            ctx.put("error",  java.net.URLEncoder.encode("该课程编号已经存在!"));
            return "error";
        }
        try {
            Teacher teacherObj = teacherDAO.GetTeacherByTeacherNo(course.getTeacherObj().getTeacherNo());
            course.setTeacherObj(teacherObj);
            courseDAO.AddCourse(course);
            ctx.put("message",  java.net.URLEncoder.encode("Course添加成功!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("图片文件格式不对!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Course添加失败!"));
            return "error";
        }
    }

    /*查询Course信息*/
    public String QueryCourse() {
        if(currentPage == 0) currentPage = 1;
        if(courseNo == null) courseNo = "";
        if(courseName == null) courseName = "";
        List<Course> courseList = courseDAO.QueryCourseInfo(courseNo, courseName, teacherObj, currentPage);
        /*计算总的页数和总的记录数*/
        courseDAO.CalculateTotalPageAndRecordNumber(courseNo, courseName, teacherObj);
        /*获取到总的页码数目*/
        totalPage = courseDAO.getTotalPage();
        /*当前查询条件下总记录数*/
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

    /*后台导出到excel*/
    public String QueryCourseOutputToExcel() { 
        if(courseNo == null) courseNo = "";
        if(courseName == null) courseName = "";
        List<Course> courseList = courseDAO.QueryCourseInfo(courseNo,courseName,teacherObj);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "Course信息记录"; 
        String[] headers = { "课程编号","课程名称","上课老师","上课地点","上课时间","总学时","课程学分"};
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
		HttpServletResponse response = null;//创建一个HttpServletResponse对象 
		OutputStream out = null;//创建一个输出流对象 
		try { 
			response = ServletActionContext.getResponse();//初始化HttpServletResponse对象 
			out = response.getOutputStream();//
			response.setHeader("Content-disposition","attachment; filename="+"Course.xls");//filename是下载的xls的名，建议最好用英文 
			response.setContentType("application/msexcel;charset=UTF-8");//设置类型 
			response.setHeader("Pragma","No-cache");//设置头 
			response.setHeader("Cache-Control","no-cache");//设置头 
			response.setDateHeader("Expires", 0);//设置日期头  
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
    /*前台查询Course信息*/
    public String FrontQueryCourse() {
        if(currentPage == 0) currentPage = 1;
        if(courseNo == null) courseNo = "";
        if(courseName == null) courseName = "";
        List<Course> courseList = courseDAO.QueryCourseInfo(courseNo, courseName, teacherObj, currentPage);
        /*计算总的页数和总的记录数*/
        courseDAO.CalculateTotalPageAndRecordNumber(courseNo, courseName, teacherObj);
        /*获取到总的页码数目*/
        totalPage = courseDAO.getTotalPage();
        /*当前查询条件下总记录数*/
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

    /*查询要修改的Course信息*/
    public String ModifyCourseQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键courseNo获取Course对象*/
        Course course = courseDAO.GetCourseByCourseNo(courseNo);

        List<Teacher> teacherList = teacherDAO.QueryAllTeacherInfo();
        ctx.put("teacherList", teacherList);
        ctx.put("course",  course);
        return "modify_view";
    }

    /*查询要修改的Course信息*/
    public String FrontShowCourseQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键courseNo获取Course对象*/
        Course course = courseDAO.GetCourseByCourseNo(courseNo);

        List<Teacher> teacherList = teacherDAO.QueryAllTeacherInfo();
        ctx.put("teacherList", teacherList);
        ctx.put("course",  course);
        return "front_show_view";
    }

    /*更新修改Course信息*/
    public String ModifyCourse() {
        ActionContext ctx = ActionContext.getContext();
        try {
            Teacher teacherObj = teacherDAO.GetTeacherByTeacherNo(course.getTeacherObj().getTeacherNo());
            course.setTeacherObj(teacherObj);
            courseDAO.UpdateCourse(course);
            ctx.put("message",  java.net.URLEncoder.encode("Course信息更新成功!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Course信息更新失败!"));
            return "error";
       }
   }

    /*删除Course信息*/
    public String DeleteCourse() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            courseDAO.DeleteCourse(courseNo);
            ctx.put("message",  java.net.URLEncoder.encode("Course删除成功!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Course删除失败!"));
            return "error";
        }
    }

}
