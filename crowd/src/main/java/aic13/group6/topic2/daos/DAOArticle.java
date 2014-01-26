package aic13.group6.topic2.daos;

import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import aic13.group6.topic2.entities.Article;

public class DAOArticle implements DAO<Article> {

	@Override
	public Article create(Article obj) throws SQLException {
		if (obj.getUrl() == null) {
			throw new SQLException("No key!");
		}
		
		EntityManagerFactory emf =   Persistence.createEntityManagerFactory("aic");
    	EntityManager em = emf.createEntityManager();
    	
		em.getTransaction().begin();
		em.merge(obj);
		em.getTransaction().commit();
		
		em.close();
		emf.close();
		
		return obj;
	}

	@Override
	public Article findByID(Article obj) throws SQLException {
		if (obj.getUrl() == null) {
			throw new SQLException("No key!");
		}
		
		EntityManagerFactory emf =   Persistence.createEntityManagerFactory("aic");
    	EntityManager em = emf.createEntityManager();
		
		obj = em.find(Article.class, obj.getUrl());
		
		em.close();
		emf.close();
		
		return obj;
	}
	
//	public List<Article> findAllNew() {
//		EntityManagerFactory emf =   Persistence.createEntityManagerFactory("aic");
//    	EntityManager em = emf.createEntityManager();
//		
//		List<Article> ret = em.createQuery("SELECT a FROM articles a WHERE a.url NOT IN (SELECT a.url FROM tasks t LEFT JOIN t.article a))").getResultList();
//		
//		em.close();
//		emf.close();
//		
//		return ret;
//	}

}
