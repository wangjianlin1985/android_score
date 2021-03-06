<%@ page language="java" import="java.util.*"  contentType="text/html;charset=gb2312"%> 
<%@ page import="com.chengxusheji.domain.Kejian" %>
<%@ page import="com.chengxusheji.domain.Teacher" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    //获取所有的Teacher信息
    List<Teacher> teacherList = (List<Teacher>)request.getAttribute("teacherList");
    Kejian kejian = (Kejian)request.getAttribute("kejian");

    String username=(String)session.getAttribute("username");
    if(username==null){
        response.getWriter().println("<script>top.location.href='" + basePath + "login/login_view.action';</script>");
    }
%>
<HTML><HEAD><TITLE>修改课件</TITLE>
<STYLE type=text/css>
BODY {
	MARGIN-LEFT: 0px; BACKGROUND-COLOR: #ffffff
}
.STYLE1 {color: #ECE9D8}
.label {font-style.:italic; }
.errorLabel {font-style.:italic;  color:red; }
.errorMessage {font-weight:bold; color:red; }
</STYLE>
 <script src="<%=basePath %>calendar.js"></script>
<script language="javascript">
/*验证表单*/
function checkForm() {
    var title = document.getElementById("kejian.title").value;
    if(title=="") {
        alert('请输入标题!');
        return false;
    }
    var content = document.getElementById("kejian.content").value;
    if(content=="") {
        alert('请输入描述!');
        return false;
    }
    return true; 
}
 </script>
</HEAD>
<BODY background="<%=basePath %>images/adminBg.jpg">
<s:fielderror cssStyle="color:red" />
<TABLE align="center" height="100%" cellSpacing=0 cellPadding=0 width="80%" border=0>
  <TBODY>
  <TR>
    <TD align="left" vAlign=top ><s:form action="Kejian/Kejian_ModifyKejian.action" method="post" onsubmit="return checkForm();" enctype="multipart/form-data" name="form1">
<table width='100%' cellspacing='1' cellpadding='3' class="tablewidth">
  <tr>
    <td width=30%>课件id:</td>
    <td width=70%><input id="kejian.kejianId" name="kejian.kejianId" type="text" value="<%=kejian.getKejianId() %>" readOnly /></td>
  </tr>

  <tr>
    <td width=30%>标题:</td>
    <td width=70%><input id="kejian.title" name="kejian.title" type="text" size="50" value='<%=kejian.getTitle() %>'/></td>
  </tr>

  <tr>
    <td width=30%>描述:</td>
    <td width=70%><textarea id="kejian.content" name="kejian.content" rows=5 cols=50><%=kejian.getContent() %></textarea></td>
  </tr>

  <tr>
    <td width=30%>课件文件:</td>
    <td width=70%><img src="<%=basePath %><%=kejian.getKejianFile() %>" width="200px" border="0px"/><br/>
    <input type=hidden name="kejian.kejianFile" value="<%=kejian.getKejianFile() %>" />
    <input id="kejianFileFile" name="kejianFileFile" type="file" size="50" /></td>
  </tr>
  <tr>
    <td width=30%>上传老师:</td>
    <td width=70%>
      <select name="kejian.teacherObj.teacherNo">
      <%
        for(Teacher teacher:teacherList) {
          String selected = "";
          if(teacher.getTeacherNo().equals(kejian.getTeacherObj().getTeacherNo()))
            selected = "selected";
      %>
          <option value='<%=teacher.getTeacherNo() %>' <%=selected %>><%=teacher.getName() %></option>
      <%
        }
      %>
    </td>
  </tr>

  <tr>
    <td width=30%>上传时间:</td>
    <td width=70%><input id="kejian.uploadTime" name="kejian.uploadTime" type="text" size="20" value='<%=kejian.getUploadTime() %>'/></td>
  </tr>

  <tr bgcolor='#FFFFFF'>
      <td colspan="4" align="center">
        <input type='submit' name='button' value='保存' >
        &nbsp;&nbsp;
        <input type="reset" value='重写' />
      </td>
    </tr>

</table>
</s:form>
   </TD></TR>
  </TBODY>
</TABLE>
</BODY>
</HTML>
