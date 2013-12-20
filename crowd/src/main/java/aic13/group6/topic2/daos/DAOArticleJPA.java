package aic13.group6.topic2.daos;

import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import aic13.group6.topic2.entities.Article;

public class DAOArticleJPA implements DAO<Article> {

	@Override
	public Article create(Article obj) throws SQLException {
		if (obj.getUrl() == null) {
			throw new SQLException("No key!");
		}
		
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
	
	public Article findByUrl(Article obj) throws SQLException {
		if (obj.getUrl() == null) {
			throw new SQLException("No URL!");
		}
		
		EntityManagerFactory emf =   Persistence.createEntityManagerFactory("aic");
    	EntityManager em = emf.createEntityManager();
    	
		Query q = em.createQuery("SELECT a FROM Article a WHERE a.url = :url");
		q.setParameter("url", obj.getUrl());
		List<Article> list = (List<Article>) q.getResultList();
		if(list.size() > 0) 
			obj = list.get(0);
		else 
			obj = null;
//		obj = (q.getSingleResult() instanceof Article) ? (Article) q.getSingleResult() : null;
		
		em.close();
		emf.close();
		
		return obj;
	}

	public List<Article> findAllNew() {
		EntityManagerFactory emf =   Persistence.createEntityManagerFactory("aic");
    	EntityManager em = emf.createEntityManager();
		
		List<Article> ret = em.createQuery("SELECT a FROM articles a WHERE a.url NOT IN (SELECT a.url FROM tasks t LEFT JOIN t.article a))").getResultList();
		
		em.close();
		emf.close();
		
		return ret;
	}

}
