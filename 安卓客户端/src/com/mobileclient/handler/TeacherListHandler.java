package com.mobileclient.handler;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.mobileclient.domain.Teacher;
public class TeacherListHandler extends DefaultHandler {
	private List<Teacher> teacherList = null;
	private Teacher teacher;
	private String tempString;
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		super.characters(ch, start, length);
		if (teacher != null) { 
            String valueString = new String(ch, start, length); 
            if ("teacherNo".equals(tempString)) 
            	teacher.setTeacherNo(valueString); 
            else if ("password".equals(tempString)) 
            	teacher.setPassword(valueString); 
            else if ("name".equals(tempString)) 
            	teacher.setName(valueString); 
            else if ("sex".equals(tempString)) 
            	teacher.setSex(valueString); 
            else if ("birthday".equals(tempString)) 
            	teacher.setBirthday(Timestamp.valueOf(valueString));
            else if ("telephone".equals(tempString)) 
            	teacher.setTelephone(valueString); 
            else if ("email".equals(tempString)) 
            	teacher.setEmail(valueString); 
            else if ("address".equals(tempString)) 
            	teacher.setAddress(valueString); 
            else if ("memo".equals(tempString)) 
            	teacher.setMemo(valueString); 
        } 
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		super.endElement(uri, localName, qName);
		if("Teacher".equals(localName)&&teacher!=null){
			teacherList.add(teacher);
			teacher = null; 
		}
		tempString = null;
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		teacherList = new ArrayList<Teacher>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
        if ("Teacher".equals(localName)) {
            teacher = new Teacher(); 
        }
        tempString = localName; 
	}

	public List<Teacher> getTeacherList() {
		return this.teacherList;
	}
}
