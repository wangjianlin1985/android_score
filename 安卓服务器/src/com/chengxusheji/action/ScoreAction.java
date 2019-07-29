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
import com.chengxusheji.dao.ScoreDAO;
import com.chengxusheji.domain.Score;
import com.chengxusheji.dao.StudentDAO;
import com.chengxusheji.domain.Student;
import com.chengxusheji.dao.CourseDAO;
import com.chengxusheji.domain.Course;
import com.chengxusheji.utils.FileTypeException;
import com.chengxusheji.utils.ExportExcelUtil;

@Controller @Scope("prototype")
public class ScoreAction extends BaseAction {

    /*界面层需要查询的属性: 学生*/
    private Student studentObj;
    public void setStudentObj(Student studentObj) {
        this.studentObj = studentObj;
    }
    public Student getStudentObj() {
        return this.studentObj;
    }

    /*界面层需要查询的属性: 课程*/
    private Course courseObj;
    public void setCourseObj(Course courseObj) {
        this.courseObj = courseObj;
    }
    public Course getCourseObj() {
        return this.courseObj;
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

    private int scoreId;
    public void setScoreId(int scoreId) {
        this.scoreId = scoreId;
    }
    public int getScoreId() {
        return scoreId;
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
    @Resource StudentDAO studentDAO;
    @Resource CourseDAO courseDAO;
    @Resource ScoreDAO scoreDAO;

    /*待操作的Score对象*/
    private Score score;
    public void setScore(Score score) {
        this.score = score;
    }
    public Score getScore() {
        return this.score;
    }

    /*跳转到添加Score视图*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        /*查询所有的Student信息*/
        List<Student> studentList = studentDAO.QueryAllStudentInfo();
        ctx.put("studentList", studentList);
        /*查询所有的Course信息*/
        List<Course> courseList = courseDAO.QueryAllCourseInfo();
        ctx.put("courseList", courseList);
        return "add_view";
    }

    /*添加Score信息*/
    @SuppressWarnings("deprecation")
    public String AddScore() {
        ActionContext ctx = ActionContext.getContext();
        try {
            Student studentObj = studentDAO.GetStudentByStudentNo(score.getStudentObj().getStudentNo());
            score.setStudentObj(studentObj);
            Course courseObj = courseDAO.GetCourseByCourseNo(score.getCourseObj().getCourseNo());
            score.setCourseObj(courseObj);
            scoreDAO.AddScore(score);
            ctx.put("message",  java.net.URLEncoder.encode("Score添加成功!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("图片文件格式不对!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Score添加失败!"));
            return "error";
        }
    }

    /*查询Score信息*/
    public String QueryScore() {
        if(currentPage == 0) currentPage = 1;
        List<Score> scoreList = scoreDAO.QueryScoreInfo(studentObj, courseObj, currentPage);
        /*计算总的页数和总的记录数*/
        scoreDAO.CalculateTotalPageAndRecordNumber(studentObj, courseObj);
        /*获取到总的页码数目*/
        totalPage = scoreDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = scoreDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("scoreList",  scoreList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("studentObj", studentObj);
        List<Student> studentList = studentDAO.QueryAllStudentInfo();
        ctx.put("studentList", studentList);
        ctx.put("courseObj", courseObj);
        List<Course> courseList = courseDAO.QueryAllCourseInfo();
        ctx.put("courseList", courseList);
        return "query_view";
    }

    /*后台导出到excel*/
    public String QueryScoreOutputToExcel() { 
        List<Score> scoreList = scoreDAO.QueryScoreInfo(studentObj,courseObj);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "Score信息记录"; 
        String[] headers = { "成绩id","学生","课程","课程成绩","学生评价","添加时间"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<scoreList.size();i++) {
        	Score score = scoreList.get(i); 
        	dataset.add(new String[]{score.getScoreId() + "",score.getStudentObj().getName(),
score.getCourseObj().getCourseName(),
score.getCourseScore() + "",score.getEvaluate(),score.getAddTime()});
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
			response.setHeader("Content-disposition","attachment; filename="+"Score.xls");//filename是下载的xls的名，建议最好用英文 
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
    /*前台查询Score信息*/
    public String FrontQueryScore() {
        if(currentPage == 0) currentPage = 1;
        List<Score> scoreList = scoreDAO.QueryScoreInfo(studentObj, courseObj, currentPage);
        /*计算总的页数和总的记录数*/
        scoreDAO.CalculateTotalPageAndRecordNumber(studentObj, courseObj);
        /*获取到总的页码数目*/
        totalPage = scoreDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = scoreDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("scoreList",  scoreList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("studentObj", studentObj);
        List<Student> studentList = studentDAO.QueryAllStudentInfo();
        ctx.put("studentList", studentList);
        ctx.put("courseObj", courseObj);
        List<Course> courseList = courseDAO.QueryAllCourseInfo();
        ctx.put("courseList", courseList);
        return "front_query_view";
    }

    /*查询要修改的Score信息*/
    public String ModifyScoreQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键scoreId获取Score对象*/
        Score score = scoreDAO.GetScoreByScoreId(scoreId);

        List<Student> studentList = studentDAO.QueryAllStudentInfo();
        ctx.put("studentList", studentList);
        List<Course> courseList = courseDAO.QueryAllCourseInfo();
        ctx.put("courseList", courseList);
        ctx.put("score",  score);
        return "modify_view";
    }

    /*查询要修改的Score信息*/
    public String FrontShowScoreQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键scoreId获取Score对象*/
        Score score = scoreDAO.GetScoreByScoreId(scoreId);

        List<Student> studentList = studentDAO.QueryAllStudentInfo();
        ctx.put("studentList", studentList);
        List<Course> courseList = courseDAO.QueryAllCourseInfo();
        ctx.put("courseList", courseList);
        ctx.put("score",  score);
        return "front_show_view";
    }

    /*更新修改Score信息*/
    public String ModifyScore() {
        ActionContext ctx = ActionContext.getContext();
        try {
            Student studentObj = studentDAO.GetStudentByStudentNo(score.getStudentObj().getStudentNo());
            score.setStudentObj(studentObj);
            Course courseObj = courseDAO.GetCourseByCourseNo(score.getCourseObj().getCourseNo());
            score.setCourseObj(courseObj);
            scoreDAO.UpdateScore(score);
            ctx.put("message",  java.net.URLEncoder.encode("Score信息更新成功!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Score信息更新失败!"));
            return "error";
       }
   }

    /*删除Score信息*/
    public String DeleteScore() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            scoreDAO.DeleteScore(scoreId);
            ctx.put("message",  java.net.URLEncoder.encode("Score删除成功!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Score删除失败!"));
            return "error";
        }
    }

}
