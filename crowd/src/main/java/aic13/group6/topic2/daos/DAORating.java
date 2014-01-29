package aic13.group6.topic2.daos;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import aic13.group6.topic2.entities.Article;
import aic13.group6.topic2.entities.Job;
import aic13.group6.topic2.entities.Rating;
import aic13.group6.topic2.pojos.AnswerOptions;

public class DAORating implements DAO<Rating> {

	@Override
	public Rating create(Rating obj) throws SQLException {
		synchronized(DAO.SYNC) {
			EntityManagerFactory emf =	Persistence.createEntityManagerFactory("aic");
	    	EntityManager em = emf.createEntityManager();
	    	
	    	em.getTransaction().begin();
			em.persist(obj);
			em.getTransaction().commit();
			
			em.close();
			emf.close();
		}
		
		return obj;
	}

	@Override
	public Rating findByID(Rating obj) throws SQLException {
		Rating r;
		
		synchronized(DAO.SYNC) {
			EntityManagerFactory emf =	Persistence.createEntityManagerFactory("aic");
	    	EntityManager em = emf.createEntityManager();
	    	
	    	r = em.find(Rating.class, obj.getId());
	    	
	    	em.close();
	    	emf.close();
		}
    	
    	return r;
	}
	
	/**
	 * Rating distance. Calculated by the average of each Article. Taken the distance of user rating to the average of Article. 
	 * The average of the distance of all article is the result. 
	 * @return global distance for every user
	 * @throws SQLException
	 */
	public Map<Long, Double> calculateDistance() throws SQLException {
		Map<Long, Double> map = new TreeMap<Long, Double>();
		List<Object[]> resultList;

		synchronized(DAO.SYNC) {
			EntityManagerFactory emf =	Persistence.createEntityManagerFactory("aic");
	    	EntityManager em = emf.createEntityManager();
	    	
	    	// Native query, because JPQL doesn't support nested queries
	    	Query query = em.createNativeQuery("select userid, sum(abstand)/count(abstand) abstand_global from ("
	    			+ "select userid, abs(user_durchschn-durchschnitt) abstand, url from ("
	    			+ "select sum(value)*1.0/count(value) durchschnitt, url from rating join article where rating.article_url=article.url and value<>:irrelevantValue group by url"
	    			+ ") join ("
	    			+ "select userid, sum(value)*1.0/count(value) user_durchschn, url url_user from rating join article where rating.article_url=article.url and  value<>:irrelevantValue group by userid, url"
	    			+ ") where url=url_user order by userid"
	    			+ ") group by userid;").setParameter("irrelevantValue", AnswerOptions.IRRELEVANT.value());
	    	
	    	resultList = query.getResultList(); 
	    	
	    	em.close();
	    	emf.close();
		}
    	
    	for(Object[] result: resultList) {
    		map.put(new Long((Integer)result[0]), (Double)result[1]);
    	}
    	
    	return map;
	}
	
	/**
	 * Rating tendeny. The average rating value for each user.
	 * @return rating tendency of every user
	 * @throws SQLException
	 */
	public Map<Long, Double> calculateTendency() throws SQLException {
		Map<Long, Double> map = new TreeMap<Long, Double>();
		List<Object[]> resultList;

		synchronized(DAO.SYNC) {
			EntityManagerFactory emf =	Persistence.createEntityManagerFactory("aic");
	    	EntityManager em = emf.createEntityManager();
	    	
	    	Query query = em.createNativeQuery("select userid, sum(value)*1.0/sum(userid) from rating where value<>:irrelevantValue group by userid").setParameter("irrelevantValue", AnswerOptions.IRRELEVANT.value());
	    	
	    	resultList = query.getResultList(); 
	    	
	    	em.close();
	    	emf.close();
		}
	    	
    	for(Object[] result: resultList) {
    		map.put(new Long((Integer)result[0]), (Double)result[1]);
    	}
    	
    	return map;
	}
	
	public Double calculateRatingResultForArticle(Article article) {
		List<Double> result;
		
		synchronized(DAO.SYNC) {
			EntityManagerFactory emf =	Persistence.createEntityManagerFactory("aic");
	    	EntityManager em = emf.createEntityManager();
	    	
	    	Query query = em.createNativeQuery("select sum(value)*1.0/count(value) average from article a join rating r on a.url=r.article_url where a.url = ':url' group by a.url").setParameter("url", article.getUrl());
	    	
	    	result = query.getResultList(); 
	    	
	    	em.close();
	    	emf.close();
		}
    	
    	// should be only zero or one result
    	Double value = null;
    	if(result.size() > 0) {
    		value = result.get(0);
    	}
    	return value;
	}
	
	public Double calculateRatingResultForJob(Job job) {
		List<Double> result;
		
		synchronized(DAO.SYNC) {
			EntityManagerFactory emf =	Persistence.createEntityManagerFactory("aic");
	    	EntityManager em = emf.createEntityManager();
	    	
	    	Query query = em.createNativeQuery("select sum(r.value)*1.0/count(r.value) average from job j join job_article jb on j.id=jb.jobs_ID join article a on a.url=jb.articles_URL join rating r on r.article_url=a.url where j.id=:id group by j.id").setParameter("id", job.getId());
	    	
	    	result = query.getResultList(); 
	    	
	    	em.close();
	    	emf.close();
		}
    	
    	// should be only zero or one result
    	Double value = null;
    	if(result.size() > 0) {
    		value = result.get(0);
    	}
    	return value;
	}
	
}
