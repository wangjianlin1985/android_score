<%@ page language="java" import="java.util.*"  contentType="text/html;charset=gb2312"%>
<%@ page import="com.chengxusheji.domain.Teacher" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    //��ȡ���е�Teacher��Ϣ
    List<Teacher> teacherList = (List<Teacher>)request.getAttribute("teacherList");
    String username=(String)session.getAttribute("username");
    if(username==null){
        response.getWriter().println("<script>top.location.href='" + basePath + "login/login_view.action';</script>");
    }
%>
<HTML><HEAD><TITLE>��ӿγ�</TITLE> 
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
/*��֤��*/
function checkForm() {
    var courseNo = document.getElementById("course.courseNo").value;
    if(courseNo=="") {
        alert('������γ̱��!');
        return false;
    }
    var courseName = document.getElementById("course.courseName").value;
    if(courseName=="") {
        alert('������γ�����!');
        return false;
    }
    var coursePlace = document.getElementById("course.coursePlace").value;
    if(coursePlace=="") {
        alert('�������Ͽεص�!');
        return false;
    }
    var courseTime = document.getElementById("course.courseTime").value;
    if(courseTime=="") {
        alert('�������Ͽ�ʱ��!');
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
    <TD align="left" vAlign=top >
    <s:form action="Course/Course_AddCourse.action" method="post" id="courseAddForm" onsubmit="return checkForm();"  enctype="multipart/form-data" name="form1">
<table width='100%' cellspacing='1' cellpadding='3' class="tablewidth">

  <tr>
    <td width=30%>�γ̱��:</td>
    <td width=70%><input id="course.courseNo" name="course.courseNo" type="text" /></td>
  </tr>

  <tr>
    <td width=30%>�γ�����:</td>
    <td width=70%><input id="course.courseName" name="course.courseName" type="text" size="20" /></td>
  </tr>

  <tr>
    <td width=30%>�Ͽ���ʦ:</td>
    <td width=70%>
      <select name="course.teacherObj.teacherNo">
      <%
        for(Teacher teacher:teacherList) {
      %>
          <option value='<%=teacher.getTeacherNo() %>'><%=teacher.getName() %></option>
      <%
        }
      %>
    </td>
  </tr>

  <tr>
    <td width=30%>�Ͽεص�:</td>
    <td width=70%><input id="course.coursePlace" name="course.coursePlace" type="text" size="20" /></td>
  </tr>

  <tr>
    <td width=30%>�Ͽ�ʱ��:</td>
    <td width=70%><input id="course.courseTime" name="course.courseTime" type="text" size="20" /></td>
  </tr>

  <tr>
    <td width=30%>��ѧʱ:</td>
    <td width=70%><input id="course.courseHours" name="course.courseHours" type="text" size="8" /></td>
  </tr>

  <tr>
    <td width=30%>�γ�ѧ��:</td>
    <td width=70%><input id="course.courseScore" name="course.courseScore" type="text" size="8" /></td>
  </tr>

  <tr>
    <td width=30%>������Ϣ:</td>
    <td width=70%><textarea id="course.memo" name="course.memo" rows="5" cols="50"></textarea></td>
  </tr>

  <tr bgcolor='#FFFFFF'>
      <td colspan="4" align="center">
        <input type='submit' name='button' value='����' >
        &nbsp;&nbsp;
        <input type="reset" value='��д' />
      </td>
    </tr>

</table>
</s:form>
   </TD></TR>
  </TBODY>
</TABLE>
</BODY>
</HTML>
