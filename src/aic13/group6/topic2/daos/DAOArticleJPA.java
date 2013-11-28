package aic13.group6.topic2.daos;

import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import aic13.group6.topic2.entities.Article;

public class DAOArticleJPA implements DAO<Article> {
	
	@PersistenceContext
	EntityManager em;

	@Override
	public Article create(Article obj) throws SQLException {
		if (obj.getUrl() == null) {
			throw new SQLException("No key!");
		}
		
		em.persist(obj);
		return obj;
	}

	@Override
	public Article findByID(Article obj) throws SQLException {
		if (obj.getUrl() == null) {
			throw new SQLException("No key!");
		}
		
		return em.find(Article.class, obj.getUrl());
	}

	public List<Article> findAllUsable() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Article> cq = cb.createQuery(Article.class);
		Root<Article> article = cq.from(Article.class);
		cq.where(cb.equal(article.get("usable"), true));
		TypedQuery<Article> q = em.createQuery(cq);
		
		return q.getResultList();
	}

}
