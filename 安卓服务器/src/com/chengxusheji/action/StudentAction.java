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
import com.chengxusheji.dao.StudentDAO;
import com.chengxusheji.domain.Student;
import com.chengxusheji.dao.ClassInfoDAO;
import com.chengxusheji.domain.ClassInfo;
import com.chengxusheji.utils.FileTypeException;
import com.chengxusheji.utils.ExportExcelUtil;

@Controller @Scope("prototype")
public class StudentAction extends BaseAction {

	/*ͼƬ���ļ��ֶ�studentPhoto��������*/
	private File studentPhotoFile;
	private String studentPhotoFileFileName;
	private String studentPhotoFileContentType;
	public File getStudentPhotoFile() {
		return studentPhotoFile;
	}
	public void setStudentPhotoFile(File studentPhotoFile) {
		this.studentPhotoFile = studentPhotoFile;
	}
	public String getStudentPhotoFileFileName() {
		return studentPhotoFileFileName;
	}
	public void setStudentPhotoFileFileName(String studentPhotoFileFileName) {
		this.studentPhotoFileFileName = studentPhotoFileFileName;
	}
	public String getStudentPhotoFileContentType() {
		return studentPhotoFileContentType;
	}
	public void setStudentPhotoFileContentType(String studentPhotoFileContentType) {
		this.studentPhotoFileContentType = studentPhotoFileContentType;
	}
    /*�������Ҫ��ѯ������: ѧ��*/
    private String studentNo;
    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }
    public String getStudentNo() {
        return this.studentNo;
    }

    /*�������Ҫ��ѯ������: ���ڰ༶*/
    private ClassInfo classObj;
    public void setClassObj(ClassInfo classObj) {
        this.classObj = classObj;
    }
    public ClassInfo getClassObj() {
        return this.classObj;
    }

    /*�������Ҫ��ѯ������: ����*/
    private String name;
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }

    /*�������Ҫ��ѯ������: ��������*/
    private String birthday;
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
    public String getBirthday() {
        return this.birthday;
    }

    /*�������Ҫ��ѯ������: ��ϵ�绰*/
    private String telephone;
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    public String getTelephone() {
        return this.telephone;
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
    @Resource ClassInfoDAO classInfoDAO;
    @Resource StudentDAO studentDAO;

    /*��������Student����*/
    private Student student;
    public void setStudent(Student student) {
        this.student = student;
    }
    public Student getStudent() {
        return this.student;
    }

    /*��ת�����Student��ͼ*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        /*��ѯ���е�ClassInfo��Ϣ*/
        List<ClassInfo> classInfoList = classInfoDAO.QueryAllClassInfoInfo();
        ctx.put("classInfoList", classInfoList);
        return "add_view";
    }

    /*���Student��Ϣ*/
    @SuppressWarnings("deprecation")
    public String AddStudent() {
        ActionContext ctx = ActionContext.getContext();
        /*��֤ѧ���Ƿ��Ѿ�����*/
        String studentNo = student.getStudentNo();
        Student db_student = studentDAO.GetStudentByStudentNo(studentNo);
        if(null != db_student) {
            ctx.put("error",  java.net.URLEncoder.encode("��ѧ���Ѿ�����!"));
            return "error";
        }
        try {
            ClassInfo classObj = classInfoDAO.GetClassInfoByClassNo(student.getClassObj().getClassNo());
            student.setClassObj(classObj);
            /*����ѧ����Ƭ�ϴ�*/
            String studentPhotoPath = "upload/noimage.jpg"; 
       	 	if(studentPhotoFile != null)
       	 		studentPhotoPath = photoUpload(studentPhotoFile,studentPhotoFileContentType);
       	 	student.setStudentPhoto(studentPhotoPath);
            studentDAO.AddStudent(student);
            ctx.put("message",  java.net.URLEncoder.encode("Student��ӳɹ�!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("ͼƬ�ļ���ʽ����!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Student���ʧ��!"));
            return "error";
        }
    }

    /*��ѯStudent��Ϣ*/
    public String QueryStudent() {
        if(currentPage == 0) currentPage = 1;
        if(studentNo == null) studentNo = "";
        if(name == null) name = "";
        if(birthday == null) birthday = "";
        if(telephone == null) telephone = "";
        List<Student> studentList = studentDAO.QueryStudentInfo(studentNo, classObj, name, birthday, telephone, currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        studentDAO.CalculateTotalPageAndRecordNumber(studentNo, classObj, name, birthday, telephone);
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = studentDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
        recordNumber = studentDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("studentList",  studentList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("studentNo", studentNo);
        ctx.put("classObj", classObj);
        List<ClassInfo> classInfoList = classInfoDAO.QueryAllClassInfoInfo();
        ctx.put("classInfoList", classInfoList);
        ctx.put("name", name);
        ctx.put("birthday", birthday);
        ctx.put("telephone", telephone);
        return "query_view";
    }

    /*��̨������excel*/
    public String QueryStudentOutputToExcel() { 
        if(studentNo == null) studentNo = "";
        if(name == null) name = "";
        if(birthday == null) birthday = "";
        if(telephone == null) telephone = "";
        List<Student> studentList = studentDAO.QueryStudentInfo(studentNo,classObj,name,birthday,telephone);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "Student��Ϣ��¼"; 
        String[] headers = { "ѧ��","���ڰ༶","����","�Ա�","��������","ѧ����Ƭ","��ϵ�绰"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<studentList.size();i++) {
        	Student student = studentList.get(i); 
        	dataset.add(new String[]{student.getStudentNo(),student.getClassObj().getClassName(),
student.getName(),student.getSex(),new SimpleDateFormat("yyyy-MM-dd").format(student.getBirthday()),student.getStudentPhoto(),student.getTelephone()});
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
			response.setHeader("Content-disposition","attachment; filename="+"Student.xls");//filename�����ص�xls���������������Ӣ�� 
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
    /*ǰ̨��ѯStudent��Ϣ*/
    public String FrontQueryStudent() {
        if(currentPage == 0) currentPage = 1;
        if(studentNo == null) studentNo = "";
        if(name == null) name = "";
        if(birthday == null) birthday = "";
        if(telephone == null) telephone = "";
        List<Student> studentList = studentDAO.QueryStudentInfo(studentNo, classObj, name, birthday, telephone, currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        studentDAO.CalculateTotalPageAndRecordNumber(studentNo, classObj, name, birthday, telephone);
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = studentDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
        recordNumber = studentDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("studentList",  studentList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("studentNo", studentNo);
        ctx.put("classObj", classObj);
        List<ClassInfo> classInfoList = classInfoDAO.QueryAllClassInfoInfo();
        ctx.put("classInfoList", classInfoList);
        ctx.put("name", name);
        ctx.put("birthday", birthday);
        ctx.put("telephone", telephone);
        return "front_query_view";
    }

    /*��ѯҪ�޸ĵ�Student��Ϣ*/
    public String ModifyStudentQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������studentNo��ȡStudent����*/
        Student student = studentDAO.GetStudentByStudentNo(studentNo);

        List<ClassInfo> classInfoList = classInfoDAO.QueryAllClassInfoInfo();
        ctx.put("classInfoList", classInfoList);
        ctx.put("student",  student);
        return "modify_view";
    }

    /*��ѯҪ�޸ĵ�Student��Ϣ*/
    public String FrontShowStudentQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������studentNo��ȡStudent����*/
        Student student = studentDAO.GetStudentByStudentNo(studentNo);

        List<ClassInfo> classInfoList = classInfoDAO.QueryAllClassInfoInfo();
        ctx.put("classInfoList", classInfoList);
        ctx.put("student",  student);
        return "front_show_view";
    }

    /*�����޸�Student��Ϣ*/
    public String ModifyStudent() {
        ActionContext ctx = ActionContext.getContext();
        try {
            ClassInfo classObj = classInfoDAO.GetClassInfoByClassNo(student.getClassObj().getClassNo());
            student.setClassObj(classObj);
            /*����ѧ����Ƭ�ϴ�*/
            if(studentPhotoFile != null) {
            	String studentPhotoPath = photoUpload(studentPhotoFile,studentPhotoFileContentType);
            	student.setStudentPhoto(studentPhotoPath);
            }
            studentDAO.UpdateStudent(student);
            ctx.put("message",  java.net.URLEncoder.encode("Student��Ϣ���³ɹ�!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Student��Ϣ����ʧ��!"));
            return "error";
       }
   }

    /*ɾ��Student��Ϣ*/
    public String DeleteStudent() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            studentDAO.DeleteStudent(studentNo);
            ctx.put("message",  java.net.URLEncoder.encode("Studentɾ���ɹ�!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Studentɾ��ʧ��!"));
            return "error";
        }
    }

}
