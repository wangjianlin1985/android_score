package com.chengxusheji.dao;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service; 
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.chengxusheji.domain.Teacher;
import com.chengxusheji.domain.Course;

@Service @Transactional
public class CourseDAO {

	@Resource SessionFactory factory;
    /*ÿҳ��ʾ��¼��Ŀ*/
    private final int PAGE_SIZE = 10;

    /*�����ѯ���ܵ�ҳ��*/
    private int totalPage;
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
    public int getTotalPage() {
        return totalPage;
    }

    /*�����ѯ�����ܼ�¼��*/
    private int recordNumber;
    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }
    public int getRecordNumber() {
        return recordNumber;
    }

    /*���ͼ����Ϣ*/
    public void AddCourse(Course course) throws Exception {
    	Session s = factory.getCurrentSession();
     s.save(course);
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<Course> QueryCourseInfo(String courseNo,String courseName,Teacher teacherObj,int currentPage) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From Course course where 1=1";
    	if(!courseNo.equals("")) hql = hql + " and course.courseNo like '%" + courseNo + "%'";
    	if(!courseName.equals("")) hql = hql + " and course.courseName like '%" + courseName + "%'";
    	if(null != teacherObj && !teacherObj.getTeacherNo().equals("")) hql += " and course.teacherObj.teacherNo='" + teacherObj.getTeacherNo() + "'";
    	Query q = s.createQuery(hql);
    	/*���㵱ǰ��ʾҳ��Ŀ�ʼ��¼*/
    	int startIndex = (currentPage-1) * this.PAGE_SIZE;
    	q.setFirstResult(startIndex);
    	q.setMaxResults(this.PAGE_SIZE);
    	List courseList = q.list();
    	return (ArrayList<Course>) courseList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<Course> QueryCourseInfo(String courseNo,String courseName,Teacher teacherObj) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From Course course where 1=1";
    	if(!courseNo.equals("")) hql = hql + " and course.courseNo like '%" + courseNo + "%'";
    	if(!courseName.equals("")) hql = hql + " and course.courseName like '%" + courseName + "%'";
    	if(null != teacherObj && !teacherObj.getTeacherNo().equals("")) hql += " and course.teacherObj.teacherNo='" + teacherObj.getTeacherNo() + "'";
    	Query q = s.createQuery(hql);
    	List courseList = q.list();
    	return (ArrayList<Course>) courseList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<Course> QueryAllCourseInfo() {
        Session s = factory.getCurrentSession(); 
        String hql = "From Course";
        Query q = s.createQuery(hql);
        List courseList = q.list();
        return (ArrayList<Course>) courseList;
    }

    /*�����ܵ�ҳ���ͼ�¼��*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public void CalculateTotalPageAndRecordNumber(String courseNo,String courseName,Teacher teacherObj) {
        Session s = factory.getCurrentSession();
        String hql = "From Course course where 1=1";
        if(!courseNo.equals("")) hql = hql + " and course.courseNo like '%" + courseNo + "%'";
        if(!courseName.equals("")) hql = hql + " and course.courseName like '%" + courseName + "%'";
        if(null != teacherObj && !teacherObj.getTeacherNo().equals("")) hql += " and course.teacherObj.teacherNo='" + teacherObj.getTeacherNo() + "'";
        Query q = s.createQuery(hql);
        List courseList = q.list();
        recordNumber = courseList.size();
        int mod = recordNumber % this.PAGE_SIZE;
        totalPage = recordNumber / this.PAGE_SIZE;
        if(mod != 0) totalPage++;
    }

    /*����������ȡ����*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public Course GetCourseByCourseNo(String courseNo) {
        Session s = factory.getCurrentSession();
        Course course = (Course)s.get(Course.class, courseNo);
        return course;
    }

    /*����Course��Ϣ*/
    public void UpdateCourse(Course course) throws Exception {
        Session s = factory.getCurrentSession();
        s.update(course);
    }

    /*ɾ��Course��Ϣ*/
    public void DeleteCourse (String courseNo) throws Exception {
        Session s = factory.getCurrentSession();
        Object course = s.load(Course.class, courseNo);
        s.delete(course);
    }

}
