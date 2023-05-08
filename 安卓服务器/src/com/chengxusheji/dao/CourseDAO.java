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
    /*每页显示记录数目*/
    private final int PAGE_SIZE = 10;

    /*保存查询后总的页数*/
    private int totalPage;
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
    public int getTotalPage() {
        return totalPage;
    }

    /*保存查询到的总记录数*/
    private int recordNumber;
    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }
    public int getRecordNumber() {
        return recordNumber;
    }

    /*添加图书信息*/
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
    	/*计算当前显示页码的开始记录*/
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

    /*计算总的页数和记录数*/
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

    /*根据主键获取对象*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public Course GetCourseByCourseNo(String courseNo) {
        Session s = factory.getCurrentSession();
        Course course = (Course)s.get(Course.class, courseNo);
        return course;
    }

    /*更新Course信息*/
    public void UpdateCourse(Course course) throws Exception {
        Session s = factory.getCurrentSession();
        s.update(course);
    }

    /*删除Course信息*/
    public void DeleteCourse (String courseNo) throws Exception {
        Session s = factory.getCurrentSession();
        Object course = s.load(Course.class, courseNo);
        s.delete(course);
    }

}
