<%@ page language="java" import="java.util.*"  contentType="text/html;charset=gb2312"%> 
<%@ page import="com.chengxusheji.domain.Score" %>
<%@ page import="com.chengxusheji.domain.Student" %>
<%@ page import="com.chengxusheji.domain.Course" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    //获取所有的Student信息
    List<Student> studentList = (List<Student>)request.getAttribute("studentList");
    //获取所有的Course信息
    List<Course> courseList = (List<Course>)request.getAttribute("courseList");
    Score score = (Score)request.getAttribute("score");

    String username=(String)session.getAttribute("username");
    if(username==null){
        response.getWriter().println("<script>top.location.href='" + basePath + "login/login_view.action';</script>");
    }
%>
<HTML><HEAD><TITLE>修改成绩</TITLE>
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
    var evaluate = document.getElementById("score.evaluate").value;
    if(evaluate=="") {
        alert('请输入学生评价!');
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
    <TD align="left" vAlign=top ><s:form action="Score/Score_ModifyScore.action" method="post" onsubmit="return checkForm();" enctype="multipart/form-data" name="form1">
<table width='100%' cellspacing='1' cellpadding='3' class="tablewidth">
  <tr>
    <td width=30%>成绩id:</td>
    <td width=70%><input id="score.scoreId" name="score.scoreId" type="text" value="<%=score.getScoreId() %>" readOnly /></td>
  </tr>

  <tr>
    <td width=30%>学生:</td>
    <td width=70%>
      <select name="score.studentObj.studentNo">
      <%
        for(Student student:studentList) {
          String selected = "";
          if(student.getStudentNo().equals(score.getStudentObj().getStudentNo()))
            selected = "selected";
      %>
          <option value='<%=student.getStudentNo() %>' <%=selected %>><%=student.getName() %></option>
      <%
        }
      %>
    </td>
  </tr>

  <tr>
    <td width=30%>课程:</td>
    <td width=70%>
      <select name="score.courseObj.courseNo">
      <%
        for(Course course:courseList) {
          String selected = "";
          if(course.getCourseNo().equals(score.getCourseObj().getCourseNo()))
            selected = "selected";
      %>
          <option value='<%=course.getCourseNo() %>' <%=selected %>><%=course.getCourseName() %></option>
      <%
        }
      %>
    </td>
  </tr>

  <tr>
    <td width=30%>课程成绩:</td>
    <td width=70%><input id="score.courseScore" name="score.courseScore" type="text" size="8" value='<%=score.getCourseScore() %>'/></td>
  </tr>

  <tr>
    <td width=30%>学生评价:</td>
    <td width=70%><input id="score.evaluate" name="score.evaluate" type="text" size="20" value='<%=score.getEvaluate() %>'/></td>
  </tr>

  <tr>
    <td width=30%>添加时间:</td>
    <td width=70%><input id="score.addTime" name="score.addTime" type="text" size="20" value='<%=score.getAddTime() %>'/></td>
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
