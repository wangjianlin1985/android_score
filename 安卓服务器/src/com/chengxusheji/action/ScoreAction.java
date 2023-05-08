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

    /*�������Ҫ��ѯ������: ѧ��*/
    private Student studentObj;
    public void setStudentObj(Student studentObj) {
        this.studentObj = studentObj;
    }
    public Student getStudentObj() {
        return this.studentObj;
    }

    /*�������Ҫ��ѯ������: �γ�*/
    private Course courseObj;
    public void setCourseObj(Course courseObj) {
        this.courseObj = courseObj;
    }
    public Course getCourseObj() {
        return this.courseObj;
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

    private int scoreId;
    public void setScoreId(int scoreId) {
        this.scoreId = scoreId;
    }
    public int getScoreId() {
        return scoreId;
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
    @Resource StudentDAO studentDAO;
    @Resource CourseDAO courseDAO;
    @Resource ScoreDAO scoreDAO;

    /*��������Score����*/
    private Score score;
    public void setScore(Score score) {
        this.score = score;
    }
    public Score getScore() {
        return this.score;
    }

    /*��ת�����Score��ͼ*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        /*��ѯ���е�Student��Ϣ*/
        List<Student> studentList = studentDAO.QueryAllStudentInfo();
        ctx.put("studentList", studentList);
        /*��ѯ���е�Course��Ϣ*/
        List<Course> courseList = courseDAO.QueryAllCourseInfo();
        ctx.put("courseList", courseList);
        return "add_view";
    }

    /*���Score��Ϣ*/
    @SuppressWarnings("deprecation")
    public String AddScore() {
        ActionContext ctx = ActionContext.getContext();
        try {
            Student studentObj = studentDAO.GetStudentByStudentNo(score.getStudentObj().getStudentNo());
            score.setStudentObj(studentObj);
            Course courseObj = courseDAO.GetCourseByCourseNo(score.getCourseObj().getCourseNo());
            score.setCourseObj(courseObj);
            scoreDAO.AddScore(score);
            ctx.put("message",  java.net.URLEncoder.encode("Score��ӳɹ�!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("ͼƬ�ļ���ʽ����!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Score���ʧ��!"));
            return "error";
        }
    }

    /*��ѯScore��Ϣ*/
    public String QueryScore() {
        if(currentPage == 0) currentPage = 1;
        List<Score> scoreList = scoreDAO.QueryScoreInfo(studentObj, courseObj, currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        scoreDAO.CalculateTotalPageAndRecordNumber(studentObj, courseObj);
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = scoreDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
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

    /*��̨������excel*/
    public String QueryScoreOutputToExcel() { 
        List<Score> scoreList = scoreDAO.QueryScoreInfo(studentObj,courseObj);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "Score��Ϣ��¼"; 
        String[] headers = { "�ɼ�id","ѧ��","�γ�","�γ̳ɼ�","ѧ������","���ʱ��"};
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
		HttpServletResponse response = null;//����һ��HttpServletResponse���� 
		OutputStream out = null;//����һ����������� 
		try { 
			response = ServletActionContext.getResponse();//��ʼ��HttpServletResponse���� 
			out = response.getOutputStream();//
			response.setHeader("Content-disposition","attachment; filename="+"Score.xls");//filename�����ص�xls���������������Ӣ�� 
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
    /*ǰ̨��ѯScore��Ϣ*/
    public String FrontQueryScore() {
        if(currentPage == 0) currentPage = 1;
        List<Score> scoreList = scoreDAO.QueryScoreInfo(studentObj, courseObj, currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        scoreDAO.CalculateTotalPageAndRecordNumber(studentObj, courseObj);
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = scoreDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
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

    /*��ѯҪ�޸ĵ�Score��Ϣ*/
    public String ModifyScoreQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������scoreId��ȡScore����*/
        Score score = scoreDAO.GetScoreByScoreId(scoreId);

        List<Student> studentList = studentDAO.QueryAllStudentInfo();
        ctx.put("studentList", studentList);
        List<Course> courseList = courseDAO.QueryAllCourseInfo();
        ctx.put("courseList", courseList);
        ctx.put("score",  score);
        return "modify_view";
    }

    /*��ѯҪ�޸ĵ�Score��Ϣ*/
    public String FrontShowScoreQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������scoreId��ȡScore����*/
        Score score = scoreDAO.GetScoreByScoreId(scoreId);

        List<Student> studentList = studentDAO.QueryAllStudentInfo();
        ctx.put("studentList", studentList);
        List<Course> courseList = courseDAO.QueryAllCourseInfo();
        ctx.put("courseList", courseList);
        ctx.put("score",  score);
        return "front_show_view";
    }

    /*�����޸�Score��Ϣ*/
    public String ModifyScore() {
        ActionContext ctx = ActionContext.getContext();
        try {
            Student studentObj = studentDAO.GetStudentByStudentNo(score.getStudentObj().getStudentNo());
            score.setStudentObj(studentObj);
            Course courseObj = courseDAO.GetCourseByCourseNo(score.getCourseObj().getCourseNo());
            score.setCourseObj(courseObj);
            scoreDAO.UpdateScore(score);
            ctx.put("message",  java.net.URLEncoder.encode("Score��Ϣ���³ɹ�!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Score��Ϣ����ʧ��!"));
            return "error";
       }
   }

    /*ɾ��Score��Ϣ*/
    public String DeleteScore() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            scoreDAO.DeleteScore(scoreId);
            ctx.put("message",  java.net.URLEncoder.encode("Scoreɾ���ɹ�!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Scoreɾ��ʧ��!"));
            return "error";
        }
    }

}
