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
import com.chengxusheji.dao.KejianDAO;
import com.chengxusheji.domain.Kejian;
import com.chengxusheji.dao.TeacherDAO;
import com.chengxusheji.domain.Teacher;
import com.chengxusheji.utils.FileTypeException;
import com.chengxusheji.utils.ExportExcelUtil;

@Controller @Scope("prototype")
public class KejianAction extends BaseAction {

	/*ͼƬ���ļ��ֶ�kejianFile��������*/
	private File kejianFileFile;
	private String kejianFileFileFileName;
	private String kejianFileFileContentType;
	public File getKejianFileFile() {
		return kejianFileFile;
	}
	public void setKejianFileFile(File kejianFileFile) {
		this.kejianFileFile = kejianFileFile;
	}
	public String getKejianFileFileFileName() {
		return kejianFileFileFileName;
	}
	public void setKejianFileFileFileName(String kejianFileFileFileName) {
		this.kejianFileFileFileName = kejianFileFileFileName;
	}
	public String getKejianFileFileContentType() {
		return kejianFileFileContentType;
	}
	public void setKejianFileFileContentType(String kejianFileFileContentType) {
		this.kejianFileFileContentType = kejianFileFileContentType;
	}
    /*�������Ҫ��ѯ������: ����*/
    private String title;
    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return this.title;
    }

    /*�������Ҫ��ѯ������: �ϴ���ʦ*/
    private Teacher teacherObj;
    public void setTeacherObj(Teacher teacherObj) {
        this.teacherObj = teacherObj;
    }
    public Teacher getTeacherObj() {
        return this.teacherObj;
    }

    /*�������Ҫ��ѯ������: �ϴ�ʱ��*/
    private String uploadTime;
    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }
    public String getUploadTime() {
        return this.uploadTime;
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

    private int kejianId;
    public void setKejianId(int kejianId) {
        this.kejianId = kejianId;
    }
    public int getKejianId() {
        return kejianId;
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
    @Resource KejianDAO kejianDAO;

    /*��������Kejian����*/
    private Kejian kejian;
    public void setKejian(Kejian kejian) {
        this.kejian = kejian;
    }
    public Kejian getKejian() {
        return this.kejian;
    }

    /*��ת�����Kejian��ͼ*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        /*��ѯ���е�Teacher��Ϣ*/
        List<Teacher> teacherList = teacherDAO.QueryAllTeacherInfo();
        ctx.put("teacherList", teacherList);
        return "add_view";
    }

    /*���Kejian��Ϣ*/
    @SuppressWarnings("deprecation")
    public String AddKejian() {
        ActionContext ctx = ActionContext.getContext();
        try {
            Teacher teacherObj = teacherDAO.GetTeacherByTeacherNo(kejian.getTeacherObj().getTeacherNo());
            kejian.setTeacherObj(teacherObj);
            /*����μ��ļ��ϴ�*/
            String kejianFilePath = "upload/noimage.jpg"; 
       	 	if(kejianFileFile != null)
       	 		kejianFilePath = photoUpload(kejianFileFile,kejianFileFileContentType);
       	 	kejian.setKejianFile(kejianFilePath);
            kejianDAO.AddKejian(kejian);
            ctx.put("message",  java.net.URLEncoder.encode("Kejian��ӳɹ�!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("ͼƬ�ļ���ʽ����!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Kejian���ʧ��!"));
            return "error";
        }
    }

    /*��ѯKejian��Ϣ*/
    public String QueryKejian() {
        if(currentPage == 0) currentPage = 1;
        if(title == null) title = "";
        if(uploadTime == null) uploadTime = "";
        List<Kejian> kejianList = kejianDAO.QueryKejianInfo(title, teacherObj, uploadTime, currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        kejianDAO.CalculateTotalPageAndRecordNumber(title, teacherObj, uploadTime);
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = kejianDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
        recordNumber = kejianDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("kejianList",  kejianList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("title", title);
        ctx.put("teacherObj", teacherObj);
        List<Teacher> teacherList = teacherDAO.QueryAllTeacherInfo();
        ctx.put("teacherList", teacherList);
        ctx.put("uploadTime", uploadTime);
        return "query_view";
    }

    /*��̨������excel*/
    public String QueryKejianOutputToExcel() { 
        if(title == null) title = "";
        if(uploadTime == null) uploadTime = "";
        List<Kejian> kejianList = kejianDAO.QueryKejianInfo(title,teacherObj,uploadTime);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "Kejian��Ϣ��¼"; 
        String[] headers = { "�μ�id","����","�μ��ļ�","�ϴ���ʦ","�ϴ�ʱ��"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<kejianList.size();i++) {
        	Kejian kejian = kejianList.get(i); 
        	dataset.add(new String[]{kejian.getKejianId() + "",kejian.getTitle(),kejian.getKejianFile(),kejian.getTeacherObj().getName(),
kejian.getUploadTime()});
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
			response.setHeader("Content-disposition","attachment; filename="+"Kejian.xls");//filename�����ص�xls���������������Ӣ�� 
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
    /*ǰ̨��ѯKejian��Ϣ*/
    public String FrontQueryKejian() {
        if(currentPage == 0) currentPage = 1;
        if(title == null) title = "";
        if(uploadTime == null) uploadTime = "";
        List<Kejian> kejianList = kejianDAO.QueryKejianInfo(title, teacherObj, uploadTime, currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        kejianDAO.CalculateTotalPageAndRecordNumber(title, teacherObj, uploadTime);
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = kejianDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
        recordNumber = kejianDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("kejianList",  kejianList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("title", title);
        ctx.put("teacherObj", teacherObj);
        List<Teacher> teacherList = teacherDAO.QueryAllTeacherInfo();
        ctx.put("teacherList", teacherList);
        ctx.put("uploadTime", uploadTime);
        return "front_query_view";
    }

    /*��ѯҪ�޸ĵ�Kejian��Ϣ*/
    public String ModifyKejianQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������kejianId��ȡKejian����*/
        Kejian kejian = kejianDAO.GetKejianByKejianId(kejianId);

        List<Teacher> teacherList = teacherDAO.QueryAllTeacherInfo();
        ctx.put("teacherList", teacherList);
        ctx.put("kejian",  kejian);
        return "modify_view";
    }

    /*��ѯҪ�޸ĵ�Kejian��Ϣ*/
    public String FrontShowKejianQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������kejianId��ȡKejian����*/
        Kejian kejian = kejianDAO.GetKejianByKejianId(kejianId);

        List<Teacher> teacherList = teacherDAO.QueryAllTeacherInfo();
        ctx.put("teacherList", teacherList);
        ctx.put("kejian",  kejian);
        return "front_show_view";
    }

    /*�����޸�Kejian��Ϣ*/
    public String ModifyKejian() {
        ActionContext ctx = ActionContext.getContext();
        try {
            Teacher teacherObj = teacherDAO.GetTeacherByTeacherNo(kejian.getTeacherObj().getTeacherNo());
            kejian.setTeacherObj(teacherObj);
            /*����μ��ļ��ϴ�*/
            if(kejianFileFile != null) {
            	String kejianFilePath = photoUpload(kejianFileFile,kejianFileFileContentType);
            	kejian.setKejianFile(kejianFilePath);
            }
            kejianDAO.UpdateKejian(kejian);
            ctx.put("message",  java.net.URLEncoder.encode("Kejian��Ϣ���³ɹ�!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Kejian��Ϣ����ʧ��!"));
            return "error";
       }
   }

    /*ɾ��Kejian��Ϣ*/
    public String DeleteKejian() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            kejianDAO.DeleteKejian(kejianId);
            ctx.put("message",  java.net.URLEncoder.encode("Kejianɾ���ɹ�!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Kejianɾ��ʧ��!"));
            return "error";
        }
    }

}
