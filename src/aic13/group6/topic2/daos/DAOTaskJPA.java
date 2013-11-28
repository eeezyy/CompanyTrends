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
import aic13.group6.topic2.entities.Task;

public class DAOTaskJPA implements DAO<Task> {

	@PersistenceContext
	EntityManager em;

	@Override
	public Task create(Task obj) throws SQLException {
		em.persist(obj);
		return obj;
	}

	@Override
	public Task findByID(Task obj) throws SQLException {
		return em.find(Task.class, obj.getTid());
	}
	
	public List<Task> findByArticle(Article a) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Task> cq = cb.createQuery(Task.class);
		Root<Task> task = cq.from(Task.class);
		cq.where(cb.equal(task.get("article"), a.getUrl()));
		TypedQuery<Task> q = em.createQuery(cq);
		
		return q.getResultList();
	}

}
