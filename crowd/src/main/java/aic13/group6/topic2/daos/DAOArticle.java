package aic13.group6.topic2.daos;

import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import aic13.group6.topic2.entities.Article;

public class DAOArticle implements DAO<Article> {

	@Override
	public Article create(Article obj) throws SQLException {
		if (obj.getUrl() == null) {
			throw new SQLException("No key!");
		}
		
		synchronized(DAO.SYNC) {	
	    	EntityManager em = emf.createEntityManager();
	    	
			em.getTransaction().begin();
			em.persist(obj);
			em.getTransaction().commit();
			
			em.close();
		}
		
		return obj;
	}

	@Override
	public Article findByID(Article obj) throws SQLException {
		if (obj.getUrl() == null) {
			throw new SQLException("No key!");
		}
		
    	EntityManager em = emf.createEntityManager();
		
		obj = em.find(Article.class, obj.getUrl());
		
		em.close();
		
		return obj;
	}

	public Article update(Article obj) throws SQLException {
		synchronized(DAO.SYNC) {
	    	EntityManager em = emf.createEntityManager();
	    	
			em.getTransaction().begin();
			obj = em.merge(obj);
			em.getTransaction().commit();
			
			em.close();
		}
		
		return obj;
	}
	
	public Double calculateProgress(Article article) {
    	EntityManager em = emf.createEntityManager();
    	
    	Query query = em.createNativeQuery("select count(a.url)*1.0/(count(a.url) + sum(a.workercounter)) opentask from article a join rating r on a.url=r.article_url where a.url='" + article.getUrl() + "' group by a.url");
    	
    	List<Double> result = query.getResultList(); 
    	
    	em.close();
    	
    	// should be only zero or one result
    	Double value = null;
    	if(result.size() > 0) {
    		value = result.get(0);
    	}
    	return value;
	}
	
}
