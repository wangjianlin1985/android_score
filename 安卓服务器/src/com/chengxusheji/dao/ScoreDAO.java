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
import com.chengxusheji.domain.Student;
import com.chengxusheji.domain.Course;
import com.chengxusheji.domain.Score;

@Service @Transactional
public class ScoreDAO {

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
    public void AddScore(Score score) throws Exception {
    	Session s = factory.getCurrentSession();
     s.save(score);
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<Score> QueryScoreInfo(Student studentObj,Course courseObj,int currentPage) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From Score score where 1=1";
    	if(null != studentObj && !studentObj.getStudentNo().equals("")) hql += " and score.studentObj.studentNo='" + studentObj.getStudentNo() + "'";
    	if(null != courseObj && !courseObj.getCourseNo().equals("")) hql += " and score.courseObj.courseNo='" + courseObj.getCourseNo() + "'";
    	Query q = s.createQuery(hql);
    	/*计算当前显示页码的开始记录*/
    	int startIndex = (currentPage-1) * this.PAGE_SIZE;
    	q.setFirstResult(startIndex);
    	q.setMaxResults(this.PAGE_SIZE);
    	List scoreList = q.list();
    	return (ArrayList<Score>) scoreList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<Score> QueryScoreInfo(Student studentObj,Course courseObj) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From Score score where 1=1";
    	if(null != studentObj && !studentObj.getStudentNo().equals("")) hql += " and score.studentObj.studentNo='" + studentObj.getStudentNo() + "'";
    	if(null != courseObj && !courseObj.getCourseNo().equals("")) hql += " and score.courseObj.courseNo='" + courseObj.getCourseNo() + "'";
    	Query q = s.createQuery(hql);
    	List scoreList = q.list();
    	return (ArrayList<Score>) scoreList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<Score> QueryAllScoreInfo() {
        Session s = factory.getCurrentSession(); 
        String hql = "From Score";
        Query q = s.createQuery(hql);
        List scoreList = q.list();
        return (ArrayList<Score>) scoreList;
    }

    /*计算总的页数和记录数*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public void CalculateTotalPageAndRecordNumber(Student studentObj,Course courseObj) {
        Session s = factory.getCurrentSession();
        String hql = "From Score score where 1=1";
        if(null != studentObj && !studentObj.getStudentNo().equals("")) hql += " and score.studentObj.studentNo='" + studentObj.getStudentNo() + "'";
        if(null != courseObj && !courseObj.getCourseNo().equals("")) hql += " and score.courseObj.courseNo='" + courseObj.getCourseNo() + "'";
        Query q = s.createQuery(hql);
        List scoreList = q.list();
        recordNumber = scoreList.size();
        int mod = recordNumber % this.PAGE_SIZE;
        totalPage = recordNumber / this.PAGE_SIZE;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取对象*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public Score GetScoreByScoreId(int scoreId) {
        Session s = factory.getCurrentSession();
        Score score = (Score)s.get(Score.class, scoreId);
        return score;
    }

    /*更新Score信息*/
    public void UpdateScore(Score score) throws Exception {
        Session s = factory.getCurrentSession();
        s.update(score);
    }

    /*删除Score信息*/
    public void DeleteScore (int scoreId) throws Exception {
        Session s = factory.getCurrentSession();
        Object score = s.load(Score.class, scoreId);
        s.delete(score);
    }

}
