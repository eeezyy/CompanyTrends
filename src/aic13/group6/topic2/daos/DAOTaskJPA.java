package aic13.group6.topic2.daos;

import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import aic13.group6.topic2.entities.Article;
import aic13.group6.topic2.entities.Task;

public class DAOTaskJPA implements DAO<Task> {
	
	@Override
	public Task create(Task obj) throws SQLException {
		EntityManagerFactory emf =   Persistence.createEntityManagerFactory("aic");
    	EntityManager em = emf.createEntityManager();
    	
    	em.getTransaction().begin();
		em.persist(obj);
		em.getTransaction().commit();
		
		return obj;
	}

	@Override
	public Task findByID(Task obj) throws SQLException {
		EntityManagerFactory emf =   Persistence.createEntityManagerFactory("aic");
    	EntityManager em = emf.createEntityManager();
    	
    	Task ret = em.find(Task.class, obj.getTid());
    	
    	em.close();
    	emf.close();
    	
    	return ret;
	}
	
	public List<Task> findByArticle(Article a) {
		EntityManagerFactory emf =   Persistence.createEntityManagerFactory("aic");
    	EntityManager em = emf.createEntityManager();
    	
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Task> cq = cb.createQuery(Task.class);
		Root<Task> task = cq.from(Task.class);
		cq.where(cb.equal(task.get("article"), a.getUrl()));
		TypedQuery<Task> q = em.createQuery(cq);
		
		List<Task> ret = q.getResultList();
		
		em.close();
		emf.close();
		
		return ret;
	}

}
