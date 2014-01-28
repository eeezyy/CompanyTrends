package aic13.group6.topic2.daos;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import aic13.group6.topic2.entities.Rating;
import aic13.group6.topic2.pojos.AnswerOptions;

public class DAORating implements DAO<Rating> {

	@Override
	public Rating create(Rating obj) throws SQLException {
		EntityManagerFactory emf =   Persistence.createEntityManagerFactory("aic");
    	EntityManager em = emf.createEntityManager();
    	
    	em.getTransaction().begin();
		em.persist(obj);
		em.getTransaction().commit();
		
		em.close();
		emf.close();
		
		return obj;
	}

	@Override
	public Rating findByID(Rating obj) throws SQLException {
		EntityManagerFactory emf =   Persistence.createEntityManagerFactory("aic");
    	EntityManager em = emf.createEntityManager();
    	
    	Rating r = em.find(Rating.class, obj.getId());
    	
    	em.close();
    	emf.close();
    	
    	return r;
	}
	
	public Map<Long, Double> calculateDistance() throws SQLException {
		Map<Long, Double> map = new TreeMap<Long, Double>();

		EntityManagerFactory emf =   Persistence.createEntityManagerFactory("aic");
    	EntityManager em = emf.createEntityManager();
    	
    	// Native query, because JPQL doesn't support nested queries
    	Query query = em.createNativeQuery("select userid, sum(abstand)/count(abstand) abstand_global from ("
    			+ "select userid, abs(user_durchschn-durchschnitt) abstand, url from ("
    			+ "select sum(value)*1.0/count(value) durchschnitt, url from rating join article where rating.article_url=article.url and value<>" + AnswerOptions.IRRELEVANT.value() + " group by url"
    			+ ") join ("
    			+ "select userid, sum(value)*1.0/count(value) user_durchschn, url url_user from rating join article where rating.article_url=article.url and  value<>" + AnswerOptions.IRRELEVANT.value() + " group by userid, url"
    			+ ") where url=url_user order by userid"
    			+ ") group by userid;");
    	
    	List<Object[]> resultList = query.getResultList(); 
    	
    	em.close();
    	emf.close();
    	
    	for(Object[] result: resultList) {
    		map.put(new Long((Integer)result[0]), (Double)result[1]);
    	}
    	
    	return map;
	}
	
	public Map<Long, Double> calculateTendency() throws SQLException {
		Map<Long, Double> map = new TreeMap<Long, Double>();

		EntityManagerFactory emf =   Persistence.createEntityManagerFactory("aic");
    	EntityManager em = emf.createEntityManager();
    	
    	Query query = em.createNativeQuery("select userid, sum(value)*1.0/sum(userid) from rating where value<>" + AnswerOptions.IRRELEVANT.value() + " group by userid");
    	
    	List<Object[]> resultList = query.getResultList(); 
    	
    	em.close();
    	emf.close();
    	
    	for(Object[] result: resultList) {
    		map.put((Long)result[0], (Double)result[1]);
    	}
    	
    	return map;
	}
	
}
