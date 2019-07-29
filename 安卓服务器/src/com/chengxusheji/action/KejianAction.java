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

	/*图片或文件字段kejianFile参数接收*/
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
    /*界面层需要查询的属性: 标题*/
    private String title;
    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return this.title;
    }

    /*界面层需要查询的属性: 上传老师*/
    private Teacher teacherObj;
    public void setTeacherObj(Teacher teacherObj) {
        this.teacherObj = teacherObj;
    }
    public Teacher getTeacherObj() {
        return this.teacherObj;
    }

    /*界面层需要查询的属性: 上传时间*/
    private String uploadTime;
    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }
    public String getUploadTime() {
        return this.uploadTime;
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

    private int kejianId;
    public void setKejianId(int kejianId) {
        this.kejianId = kejianId;
    }
    public int getKejianId() {
        return kejianId;
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
    @Resource KejianDAO kejianDAO;

    /*待操作的Kejian对象*/
    private Kejian kejian;
    public void setKejian(Kejian kejian) {
        this.kejian = kejian;
    }
    public Kejian getKejian() {
        return this.kejian;
    }

    /*跳转到添加Kejian视图*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        /*查询所有的Teacher信息*/
        List<Teacher> teacherList = teacherDAO.QueryAllTeacherInfo();
        ctx.put("teacherList", teacherList);
        return "add_view";
    }

    /*添加Kejian信息*/
    @SuppressWarnings("deprecation")
    public String AddKejian() {
        ActionContext ctx = ActionContext.getContext();
        try {
            Teacher teacherObj = teacherDAO.GetTeacherByTeacherNo(kejian.getTeacherObj().getTeacherNo());
            kejian.setTeacherObj(teacherObj);
            /*处理课件文件上传*/
            String kejianFilePath = "upload/noimage.jpg"; 
       	 	if(kejianFileFile != null)
       	 		kejianFilePath = photoUpload(kejianFileFile,kejianFileFileContentType);
       	 	kejian.setKejianFile(kejianFilePath);
            kejianDAO.AddKejian(kejian);
            ctx.put("message",  java.net.URLEncoder.encode("Kejian添加成功!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("图片文件格式不对!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Kejian添加失败!"));
            return "error";
        }
    }

    /*查询Kejian信息*/
    public String QueryKejian() {
        if(currentPage == 0) currentPage = 1;
        if(title == null) title = "";
        if(uploadTime == null) uploadTime = "";
        List<Kejian> kejianList = kejianDAO.QueryKejianInfo(title, teacherObj, uploadTime, currentPage);
        /*计算总的页数和总的记录数*/
        kejianDAO.CalculateTotalPageAndRecordNumber(title, teacherObj, uploadTime);
        /*获取到总的页码数目*/
        totalPage = kejianDAO.getTotalPage();
        /*当前查询条件下总记录数*/
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

    /*后台导出到excel*/
    public String QueryKejianOutputToExcel() { 
        if(title == null) title = "";
        if(uploadTime == null) uploadTime = "";
        List<Kejian> kejianList = kejianDAO.QueryKejianInfo(title,teacherObj,uploadTime);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "Kejian信息记录"; 
        String[] headers = { "课件id","标题","课件文件","上传老师","上传时间"};
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
		HttpServletResponse response = null;//创建一个HttpServletResponse对象 
		OutputStream out = null;//创建一个输出流对象 
		try { 
			response = ServletActionContext.getResponse();//初始化HttpServletResponse对象 
			out = response.getOutputStream();//
			response.setHeader("Content-disposition","attachment; filename="+"Kejian.xls");//filename是下载的xls的名，建议最好用英文 
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
    /*前台查询Kejian信息*/
    public String FrontQueryKejian() {
        if(currentPage == 0) currentPage = 1;
        if(title == null) title = "";
        if(uploadTime == null) uploadTime = "";
        List<Kejian> kejianList = kejianDAO.QueryKejianInfo(title, teacherObj, uploadTime, currentPage);
        /*计算总的页数和总的记录数*/
        kejianDAO.CalculateTotalPageAndRecordNumber(title, teacherObj, uploadTime);
        /*获取到总的页码数目*/
        totalPage = kejianDAO.getTotalPage();
        /*当前查询条件下总记录数*/
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

    /*查询要修改的Kejian信息*/
    public String ModifyKejianQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键kejianId获取Kejian对象*/
        Kejian kejian = kejianDAO.GetKejianByKejianId(kejianId);

        List<Teacher> teacherList = teacherDAO.QueryAllTeacherInfo();
        ctx.put("teacherList", teacherList);
        ctx.put("kejian",  kejian);
        return "modify_view";
    }

    /*查询要修改的Kejian信息*/
    public String FrontShowKejianQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键kejianId获取Kejian对象*/
        Kejian kejian = kejianDAO.GetKejianByKejianId(kejianId);

        List<Teacher> teacherList = teacherDAO.QueryAllTeacherInfo();
        ctx.put("teacherList", teacherList);
        ctx.put("kejian",  kejian);
        return "front_show_view";
    }

    /*更新修改Kejian信息*/
    public String ModifyKejian() {
        ActionContext ctx = ActionContext.getContext();
        try {
            Teacher teacherObj = teacherDAO.GetTeacherByTeacherNo(kejian.getTeacherObj().getTeacherNo());
            kejian.setTeacherObj(teacherObj);
            /*处理课件文件上传*/
            if(kejianFileFile != null) {
            	String kejianFilePath = photoUpload(kejianFileFile,kejianFileFileContentType);
            	kejian.setKejianFile(kejianFilePath);
            }
            kejianDAO.UpdateKejian(kejian);
            ctx.put("message",  java.net.URLEncoder.encode("Kejian信息更新成功!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Kejian信息更新失败!"));
            return "error";
       }
   }

    /*删除Kejian信息*/
    public String DeleteKejian() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            kejianDAO.DeleteKejian(kejianId);
            ctx.put("message",  java.net.URLEncoder.encode("Kejian删除成功!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Kejian删除失败!"));
            return "error";
        }
    }

}
