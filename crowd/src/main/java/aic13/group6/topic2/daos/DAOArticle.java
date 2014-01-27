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

	public Article update(Article obj) throws SQLException {
		EntityManagerFactory emf =   Persistence.createEntityManagerFactory("aic");
    	EntityManager em = emf.createEntityManager();
    	
		em.getTransaction().begin();
		obj = em.merge(obj);
		em.getTransaction().commit();
		
		em.close();
		emf.close();
		
		return obj;
	}
	
}
