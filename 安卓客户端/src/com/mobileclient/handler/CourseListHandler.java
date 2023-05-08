package com.mobileclient.handler;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.mobileclient.domain.Course;
public class CourseListHandler extends DefaultHandler {
	private List<Course> courseList = null;
	private Course course;
	private String tempString;
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		super.characters(ch, start, length);
		if (course != null) { 
            String valueString = new String(ch, start, length); 
            if ("courseNo".equals(tempString)) 
            	course.setCourseNo(valueString); 
            else if ("courseName".equals(tempString)) 
            	course.setCourseName(valueString); 
            else if ("teacherObj".equals(tempString)) 
            	course.setTeacherObj(valueString); 
            else if ("coursePlace".equals(tempString)) 
            	course.setCoursePlace(valueString); 
            else if ("courseTime".equals(tempString)) 
            	course.setCourseTime(valueString); 
            else if ("courseHours".equals(tempString)) 
            	course.setCourseHours(new Integer(valueString).intValue());
            else if ("courseScore".equals(tempString)) 
            	course.setCourseScore(new Float(valueString).floatValue());
            else if ("memo".equals(tempString)) 
            	course.setMemo(valueString); 
        } 
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		super.endElement(uri, localName, qName);
		if("Course".equals(localName)&&course!=null){
			courseList.add(course);
			course = null; 
		}
		tempString = null;
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		courseList = new ArrayList<Course>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
        if ("Course".equals(localName)) {
            course = new Course(); 
        }
        tempString = localName; 
	}

	public List<Course> getCourseList() {
		return this.courseList;
	}
}
