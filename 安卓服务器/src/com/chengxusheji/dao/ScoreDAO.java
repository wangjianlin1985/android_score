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
    	/*���㵱ǰ��ʾҳ��Ŀ�ʼ��¼*/
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

    /*�����ܵ�ҳ���ͼ�¼��*/
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

    /*����������ȡ����*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public Score GetScoreByScoreId(int scoreId) {
        Session s = factory.getCurrentSession();
        Score score = (Score)s.get(Score.class, scoreId);
        return score;
    }

    /*����Score��Ϣ*/
    public void UpdateScore(Score score) throws Exception {
        Session s = factory.getCurrentSession();
        s.update(score);
    }

    /*ɾ��Score��Ϣ*/
    public void DeleteScore (int scoreId) throws Exception {
        Session s = factory.getCurrentSession();
        Object score = s.load(Score.class, scoreId);
        s.delete(score);
    }

}
