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
import com.chengxusheji.domain.ClassInfo;

@Service @Transactional
public class ClassInfoDAO {

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
    public void AddClassInfo(ClassInfo classInfo) throws Exception {
    	Session s = factory.getCurrentSession();
     s.save(classInfo);
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<ClassInfo> QueryClassInfoInfo(String classNo,String className,String bornDate,int currentPage) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From ClassInfo classInfo where 1=1";
    	if(!classNo.equals("")) hql = hql + " and classInfo.classNo like '%" + classNo + "%'";
    	if(!className.equals("")) hql = hql + " and classInfo.className like '%" + className + "%'";
    	if(!bornDate.equals("")) hql = hql + " and classInfo.bornDate like '%" + bornDate + "%'";
    	Query q = s.createQuery(hql);
    	/*计算当前显示页码的开始记录*/
    	int startIndex = (currentPage-1) * this.PAGE_SIZE;
    	q.setFirstResult(startIndex);
    	q.setMaxResults(this.PAGE_SIZE);
    	List classInfoList = q.list();
    	return (ArrayList<ClassInfo>) classInfoList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<ClassInfo> QueryClassInfoInfo(String classNo,String className,String bornDate) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From ClassInfo classInfo where 1=1";
    	if(!classNo.equals("")) hql = hql + " and classInfo.classNo like '%" + classNo + "%'";
    	if(!className.equals("")) hql = hql + " and classInfo.className like '%" + className + "%'";
    	if(!bornDate.equals("")) hql = hql + " and classInfo.bornDate like '%" + bornDate + "%'";
    	Query q = s.createQuery(hql);
    	List classInfoList = q.list();
    	return (ArrayList<ClassInfo>) classInfoList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<ClassInfo> QueryAllClassInfoInfo() {
        Session s = factory.getCurrentSession(); 
        String hql = "From ClassInfo";
        Query q = s.createQuery(hql);
        List classInfoList = q.list();
        return (ArrayList<ClassInfo>) classInfoList;
    }

    /*计算总的页数和记录数*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public void CalculateTotalPageAndRecordNumber(String classNo,String className,String bornDate) {
        Session s = factory.getCurrentSession();
        String hql = "From ClassInfo classInfo where 1=1";
        if(!classNo.equals("")) hql = hql + " and classInfo.classNo like '%" + classNo + "%'";
        if(!className.equals("")) hql = hql + " and classInfo.className like '%" + className + "%'";
        if(!bornDate.equals("")) hql = hql + " and classInfo.bornDate like '%" + bornDate + "%'";
        Query q = s.createQuery(hql);
        List classInfoList = q.list();
        recordNumber = classInfoList.size();
        int mod = recordNumber % this.PAGE_SIZE;
        totalPage = recordNumber / this.PAGE_SIZE;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取对象*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ClassInfo GetClassInfoByClassNo(String classNo) {
        Session s = factory.getCurrentSession();
        ClassInfo classInfo = (ClassInfo)s.get(ClassInfo.class, classNo);
        return classInfo;
    }

    /*更新ClassInfo信息*/
    public void UpdateClassInfo(ClassInfo classInfo) throws Exception {
        Session s = factory.getCurrentSession();
        s.update(classInfo);
    }

    /*删除ClassInfo信息*/
    public void DeleteClassInfo (String classNo) throws Exception {
        Session s = factory.getCurrentSession();
        Object classInfo = s.load(ClassInfo.class, classNo);
        s.delete(classInfo);
    }

}
