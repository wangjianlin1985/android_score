<%@ page language="java" import="java.util.*"  contentType="text/html;charset=gb2312"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    String username=(String)session.getAttribute("username");
    if(username==null){
        response.getWriter().println("<script>top.location.href='" + basePath + "login/login_view.action';</script>");
    }
%>
<HTML><HEAD><TITLE>�����ʦ</TITLE> 
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
    var teacherNo = document.getElementById("teacher.teacherNo").value;
    if(teacherNo=="") {
        alert('�������ʦ���!');
        return false;
    }
    var password = document.getElementById("teacher.password").value;
    if(password=="") {
        alert('�������¼����!');
        return false;
    }
    var name = document.getElementById("teacher.name").value;
    if(name=="") {
        alert('����������!');
        return false;
    }
    var sex = document.getElementById("teacher.sex").value;
    if(sex=="") {
        alert('�������Ա�!');
        return false;
    }
    var telephone = document.getElementById("teacher.telephone").value;
    if(telephone=="") {
        alert('��������ϵ�绰!');
        return false;
    }
    var email = document.getElementById("teacher.email").value;
    if(email=="") {
        alert('�������ʼ�!');
        return false;
    }
    var address = document.getElementById("teacher.address").value;
    if(address=="") {
        alert('�������ַ!');
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
    <s:form action="Teacher/Teacher_AddTeacher.action" method="post" id="teacherAddForm" onsubmit="return checkForm();"  enctype="multipart/form-data" name="form1">
<table width='100%' cellspacing='1' cellpadding='3' class="tablewidth">

  <tr>
    <td width=30%>��ʦ���:</td>
    <td width=70%><input id="teacher.teacherNo" name="teacher.teacherNo" type="text" /></td>
  </tr>

  <tr>
    <td width=30%>��¼����:</td>
    <td width=70%><input id="teacher.password" name="teacher.password" type="text" size="20" /></td>
  </tr>

  <tr>
    <td width=30%>����:</td>
    <td width=70%><input id="teacher.name" name="teacher.name" type="text" size="20" /></td>
  </tr>

  <tr>
    <td width=30%>�Ա�:</td>
    <td width=70%><input id="teacher.sex" name="teacher.sex" type="text" size="4" /></td>
  </tr>

  <tr>
    <td width=30%>��������:</td>
    <td width=70%><input type="text" readonly id="teacher.birthday"  name="teacher.birthday" onclick="setDay(this);"/></td>
  </tr>

  <tr>
    <td width=30%>��ϵ�绰:</td>
    <td width=70%><input id="teacher.telephone" name="teacher.telephone" type="text" size="20" /></td>
  </tr>

  <tr>
    <td width=30%>�ʼ�:</td>
    <td width=70%><input id="teacher.email" name="teacher.email" type="text" size="30" /></td>
  </tr>

  <tr>
    <td width=30%>��ַ:</td>
    <td width=70%><input id="teacher.address" name="teacher.address" type="text" size="50" /></td>
  </tr>

  <tr>
    <td width=30%>������Ϣ:</td>
    <td width=70%><textarea id="teacher.memo" name="teacher.memo" rows="5" cols="50"></textarea></td>
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
